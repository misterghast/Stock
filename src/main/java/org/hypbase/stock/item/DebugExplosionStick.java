package org.hypbase.stock.item;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.Damageable;
import org.hypbase.stock.StockPlugin;
import org.hypbase.stock.durability.DamageListener;

public class DebugExplosionStick extends StockItem {
    public DebugExplosionStick(String displayName, NamedTextColor rarity, NamespacedKey vanillaItem, int customModelData) {
        super(displayName, rarity, vanillaItem, customModelData, 230);
    }

    public DebugExplosionStick(String displayName, NamedTextColor rarity, NamespacedKey vanillaItem, int customModelData, int durability) {
        super(displayName, rarity, vanillaItem, customModelData, durability);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if(event.getItem() != null && (event.getInteractionPoint() != null)) {
            if(StockItemsRegistry.getInstance().get(event.getItem()) instanceof DebugExplosionStick) {
                System.out.println("test");
                event.getInteractionPoint().getWorld().createExplosion(event.getInteractionPoint(), 15);
                DamageListener.damageCustomItem(StockItemsRegistry.getInstance().get(event.getItem()), event.getItem(), 10, event.getPlayer().getUniqueId());
            }
        }
    }
}
