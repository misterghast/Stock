package org.hypbase.stock.item;

import com.destroystokyo.paper.Namespaced;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Color;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.naming.Name;

public class StockItem implements Keyed, Listener {
    org.bukkit.NamespacedKey key;
    int customModelData;

    int durability;
    String[] rawLore;
    NamedTextColor rarity;

    String translationKey;

    NamespacedKey vanillaItem;

    StockTiers tier;

    public StockItem(@Nullable NamespacedKey key, @Nullable String[] rawLore, int durability, int customModelData, NamedTextColor rarityColor, String displayName, NamespacedKey vanillaItem, @Nullable StockTiers tier) {
        this.key = key;
        this.customModelData = customModelData;
        this.rawLore = rawLore;
        this.rarity = rarityColor;
        this.translationKey = displayName;
        this.vanillaItem = vanillaItem;
        this.durability = durability;
        this.tier = tier;
    }

    public StockItem(String displayName, NamedTextColor rarity, int customModelData) {
        this(null, null, 0, customModelData, rarity, displayName, NamespacedKey.minecraft("paper"), null);
    }

    public StockItem(String displayName, NamedTextColor rarity, int customModelData, int durability) {
        this(null, null, durability, customModelData, rarity, displayName, NamespacedKey.minecraft("paper"), null);
    }

    public int getCustomModelData() {
        return this.customModelData;
    }


    public StockItem(String displayName, NamedTextColor rarity, NamespacedKey vanillaItem, int customModelData) {
        this(null, null, 0, customModelData, rarity, displayName, vanillaItem, null);
    }

    public StockItem(String displayName, NamedTextColor rarity, NamespacedKey vanillaItem, int customModelData, int durability) {
        this(null, null, durability, customModelData, rarity, displayName, vanillaItem, null);
    }

    public StockItem(String displayName, NamedTextColor rarity, NamespacedKey vanillaItem, int customModelData, int durability, StockTiers tier) {
        this(null, null, durability, customModelData, rarity, displayName, vanillaItem, tier);
    }


    public NamespacedKey getNamespacedKey() {
        return this.key;
    }

    public void setNamespacedKey(NamespacedKey key) {
        this.key = key;
    }

    public String getDisplayName() {
        return this.translationKey;
    }

    public String getUnlocalizedName() {
        return this.key.getKey();
    }

    public Component getFormattedName() {
        return Component.translatable(getDisplayName()).color(rarity).decoration(TextDecoration.ITALIC, false);
    }


    public String[] getRawLore() {
        return rawLore;
    }

    public int getDurability() {
        return this.durability;
    }

    public int getTier() {
        if(tier != null) {
            return this.tier.ordinal();
        } else {
            return 0;
        }
    }

    public NamespacedKey getBaseItem() {
        return this.vanillaItem;
    }

    public Component[] getFormattedLore() {
        if(getRawLore() != null) {
            Component[] returnedText = new TextComponent[getRawLore().length];
            MiniMessage messageBuilder = MiniMessage.miniMessage();
            for(int i = 0; i < returnedText.length; i++) {
                returnedText[i] = messageBuilder.deserialize(getRawLore()[i], TagResolver.standard());
            }
            return returnedText;
        } else {
            return null;
        }
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    public enum StockRarity {
        COMMON,
        UNCOMMON,
        RARE
    }

    public enum StockTiers {
        VANILLA_COAL,
        VANILLA_IRON,
        VANILLA_REDSTONE,
        VANILLA_OBSIDIAN,
        STOCK_1,
        STOCK_2,
        STOCK_3,
        STOCK_4
    }
}

