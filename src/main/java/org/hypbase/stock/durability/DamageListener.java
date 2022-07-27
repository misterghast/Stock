package org.hypbase.stock.durability;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.hypbase.stock.StockPlugin;
import org.hypbase.stock.item.StockItem;
import org.hypbase.stock.item.StockItemsRegistry;

import java.util.UUID;

public class DamageListener implements Listener {
    public static final NamespacedKey MAX_DURABILITY_KEY = new NamespacedKey("stock", "durability.max");
    public static final NamespacedKey CURRENT_DAMAGE_KEY = new NamespacedKey("stock", "durability.damage");

    public static boolean initOrSetCustomDurableStack(StockItem item, ItemStack stack) {
        if(stack.hasItemMeta()) {
            PersistentDataContainer data = stack.getItemMeta().getPersistentDataContainer();
            data.set(MAX_DURABILITY_KEY,  PersistentDataType.INTEGER, item.getDurability());
            data.set(CURRENT_DAMAGE_KEY, PersistentDataType.INTEGER, 0);
            return true;
        } else {
            return false;
        }
    }

    public static int getDamageLeft(ItemStack stack) {
        if(stack.hasItemMeta()) {
            PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();

            int maxDurability = container.getOrDefault(MAX_DURABILITY_KEY, PersistentDataType.INTEGER, -1);

            if(maxDurability == -1) {
                return -1;
            } else {
                int damage = container.getOrDefault(CURRENT_DAMAGE_KEY, PersistentDataType.INTEGER, 0);
                return Math.max(0, Math.min(maxDurability - damage, maxDurability));
            }
        } else {
            return -1;
        }
    }

    public static int adjustedDamageBarDamage(int customDamage, int customMax, short baseMax) {
        if(customDamage <= 0) {

        } else if(customDamage == customMax - 1) {
            // This forces item's that have 1 durability left to also show 1 durability left
            // in their visual damage, which allows client mods to swap items if necessary.
            // Comment and code for this else clause taken directly from Vane - all the credit to OddLama
            return baseMax - 1;
        }

        if(customDamage >= customMax) {
            return baseMax;
        }

        double damagePercent = (double) customDamage / customMax;
        return Math.min(customMax - 1, (int)(damagePercent * baseMax));
    }

    public static void damageCustomItem(StockItem item, ItemStack stack, int amount, UUID playerId) {
        if(!stack.getItemMeta().getPersistentDataContainer().has(MAX_DURABILITY_KEY)) {
            if(!initOrSetCustomDurableStack(item, stack)) {
                return;
            }
        }

        int damage = stack.getItemMeta().getPersistentDataContainer().getOrDefault(CURRENT_DAMAGE_KEY, PersistentDataType.INTEGER, 0);
        setDamage(item, stack, damage + amount, playerId);

    }

    private static void setDamage(StockItem item, ItemStack stack, int amount, UUID playerId) {
        ItemMeta meta = stack.getItemMeta();
        if(meta.isUnbreakable()) {
            return;
        }

        int custom_max = stack.getItemMeta().getPersistentDataContainer().getOrDefault(MAX_DURABILITY_KEY, PersistentDataType.INTEGER, 0);

        int custom_damage = Math.min(custom_max, Math.max(0, amount));

        if(custom_damage >= custom_max) {
            Player player = Bukkit.getPlayer(playerId);
            player.getInventory().remove(stack);
            Sound sound = Sound.sound(new NamespacedKey("minecraft","entity.item.break"), Sound.Source.PLAYER, 1.0f, 1f);
            player.playSound(sound);
        } else {
            stack.editMeta(itemMeta -> {
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                container.set(CURRENT_DAMAGE_KEY, PersistentDataType.INTEGER, custom_damage);
                if(!container.has(MAX_DURABILITY_KEY)) {
                    container.set(MAX_DURABILITY_KEY, PersistentDataType.INTEGER, custom_max);
                }
            });

            stack.editMeta(Damageable.class, dmg -> {
                short base_maximum = stack.getType().getMaxDurability();
                dmg.setDamage(adjustedDamageBarDamage(custom_damage, custom_max, base_maximum));
            });
        }
    }

    @EventHandler
    private static void onDamageCustomItem(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        StockItem stockItem = null;
        if(item != null) {
            stockItem = StockItemsRegistry.getInstance().get(item);
        }

        if(stockItem != null) {
            if(stockItem.getDurability() != 0 && item.getItemMeta() instanceof Damageable) {
                damageCustomItem(stockItem, item, event.getDamage(), event.getPlayer().getUniqueId());

                // Wow this is hacky but the only workaround to prevent recusivly
                // calling this event. We always increase the visual durability by 1
                // and let the server implementation decrease it again to
                // allow the item to break.
                // Literally like 99% of the fixes are taken directly from OddLama
                item.editMeta(Damageable.class, damage_meta -> {
                    damage_meta.setDamage(damage_meta.getDamage() - 1);
                });
                event.setDamage(1);
            }
        }
    }
}
