package org.hypbase.stock.block;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.hypbase.stock.StockPlugin;
import org.hypbase.stock.item.StockItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StockTierRegistry {

    private Map<NamespacedKey, StockItem.StockTiers> tierRegistryMap;
    private List<NamespacedKey> isTool;

    public StockTierRegistry() {
        tierRegistryMap = new HashMap<NamespacedKey, StockItem.StockTiers>();
        isTool = new ArrayList<NamespacedKey>();

        populateSilkTouch();
    }

    public void populateSilkTouch() {
        /*for(Material material : Material.values()) {

        }*/
    }

    public StockItem.StockTiers get(@NotNull NamespacedKey namespacedKey) {
        for(Map.Entry<NamespacedKey, StockItem.StockTiers> entry : tierRegistryMap.entrySet()) {
            System.out.println(entry.getKey().asString() + ", " + entry.getValue().ordinal());
        }
        return tierRegistryMap.get(namespacedKey);
    }

    public int getOrdinal(NamespacedKey namespacedKey) {
        return tierRegistryMap.get(namespacedKey).ordinal();
    }

    public boolean canMine(NamespacedKey A, NamespacedKey B) {
        if(!isTool(A) && keyHasTier(B)) {
            return false;
        }


        if(keyHasTier(A) && keyHasTier(B)) {
            return getOrdinal(A) >= getOrdinal(B);
        } else if(keyHasTier(A)) {
            return true;
        } else if(keyHasTier(B)){
            return false;
        } else {
            return true;
        }
    }

    public boolean canMine(ItemStack item, Block block) {
        return canMine(item.getType().getKey(), block.getType().getKey());
    }

    public boolean canMine(int tier, NamespacedKey block) {
        return tier >= getOrdinal(block);
    }

    public boolean keyHasTier(NamespacedKey key) {
        return tierRegistryMap.containsKey(key);
    }

    public boolean isTool(NamespacedKey key) {
        return isTool.contains(key);
    }

    public Iterator iterator() {
        return tierRegistryMap.entrySet().iterator();
    }

    public void register(NamespacedKey key, StockItem.StockTiers tier, boolean tool) {
        if(tool) {
            isTool.add(key);
        }
        this.tierRegistryMap.put(key, tier);
    }

    public static StockTierRegistry getInstance() {
        return StockPlugin.getInstance().getTierRegistry();
    }


}
