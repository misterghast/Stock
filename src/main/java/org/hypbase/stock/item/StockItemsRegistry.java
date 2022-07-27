package org.hypbase.stock.item;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.hypbase.stock.StockPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.Name;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StockItemsRegistry implements Registry {
    protected Map<NamespacedKey, StockItem> customItems;

    public StockItemsRegistry() {
        customItems = new HashMap<NamespacedKey, StockItem>();
    }

    public void register(String namespace, String key, StockItem item) {
        item.setNamespacedKey(new NamespacedKey(namespace, key));
        ModelDataRegistry inst = ModelDataRegistry.getInstance();
        customItems.put(new NamespacedKey(namespace, key), item);
    }

    public boolean contains(NamespacedKey key) {
        return customItems.containsKey(key);
    }

    @Nullable
    @Override
    public Keyed get(@NotNull NamespacedKey namespacedKey) {
        return customItems.get(namespacedKey);
    }

    public StockItem get(ItemStack stack) {
        return (StockItem) get(StockItemUtil.getInst().getNamespacedKeyFromStack(stack));
    }

    public static StockItemsRegistry getInstance() {
        return StockPlugin.getInstance().getRegistry();
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return customItems.values().iterator();
    }
}
