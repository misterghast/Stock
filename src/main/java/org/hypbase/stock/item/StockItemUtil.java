package org.hypbase.stock.item;

import com.destroystokyo.paper.MaterialTags;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.hypbase.stock.StockPlugin;
import org.hypbase.stock.durability.DamageListener;
import org.jetbrains.annotations.NotNull;

import javax.naming.Name;
import javax.xml.stream.events.Namespace;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StockItemUtil {

    private Map<Material, StockItem.StockTiers> tiers;


    private NamespacedKey debugItemKey;
    public NamespacedKey stockIdKey;

    public static final NamedTextColor COMMON_COLOR = NamedTextColor.WHITE;

    public static final NamedTextColor UNCOMMON_COLOR = NamedTextColor.AQUA;

    public static final NamedTextColor RARE_COLOR = NamedTextColor.YELLOW;

    public StockItemUtil() {
        debugItemKey = new NamespacedKey("stock", "debug_item");
        stockIdKey = new NamespacedKey("stock", "namespacedkey");
        tiers = new HashMap<Material, StockItem.StockTiers>();
        initTiers();
    }

    public ItemStack getNewStack(NamespacedKey stockItem, int amount) {
        StockItem registeredItem = (StockItem) StockItemsRegistry.getInstance().get(stockItem);
        if(registeredItem == null) {
            registeredItem = (StockItem) StockItemsRegistry.getInstance().get(debugItemKey);
        }

        int durability = registeredItem.getDurability();
        ItemStack newItemStack = new ItemStack(Registry.MATERIAL.get(registeredItem.getBaseItem()));
        ItemMeta meta = newItemStack.getItemMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(stockIdKey, PersistentDataType.STRING, registeredItem.getNamespacedKey().asString());
        container.set(DamageListener.MAX_DURABILITY_KEY, PersistentDataType.INTEGER, registeredItem.getDurability());
        container.set(DamageListener.CURRENT_DAMAGE_KEY, PersistentDataType.INTEGER, 0);

        meta.displayName(registeredItem.getFormattedName());

        if(registeredItem.getFormattedLore() != null) {
            meta.lore(Arrays.asList(registeredItem.getFormattedLore()));
        }

        newItemStack.setItemMeta(meta);
        newItemStack.setAmount(amount);

        return newItemStack;
    }

    public NamespacedKey getNamespacedKeyFromStack(@NotNull final ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(stockIdKey)) {
            return NamespacedKey.fromString(item.getItemMeta().getPersistentDataContainer().get(stockIdKey, PersistentDataType.STRING));
        } else {
            return null;
        }
    }

    public void initTiers() {

    }

    public static StockItemUtil getInst() {
        return StockPlugin.getInstance().getUtils();
    }

}
