package org.hypbase.stock.item;


import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.Configuration;
import org.hypbase.stock.StockPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class ModelDataRegistry {
    //This is literally just stolen from Vane-Core. Sorry!
    private final Map<String, ModelDataRange> dataMap;
    private final Map<String, Integer> priorityMap;
    private List<ModelDataRange> toRegister;

    private final ModelDataSort comparator;

    public ModelDataRegistry() {
        dataMap = new HashMap<String, ModelDataRange>();
        priorityMap = new HashMap<String, Integer>();

        comparator = new ModelDataSort();
        toRegister = new ArrayList<ModelDataRange>();
    }

    public boolean has(int data) {
        return dataMap.containsValue(data);
    }


    public ModelDataRange get(@NotNull NamespacedKey namespacedKey) {
        return dataMap.get(namespacedKey);
    }


    public @Nullable String get(int data) {
        for(var value : dataMap.entrySet()) {
            if(value.getValue().inRange(data)) {
                return value.getKey();
            }
        }

        return null;
    }

    public @Nullable String get(ModelDataRange range) {
        for(final var value : dataMap.entrySet()) {
            if(value.getValue().overlaps(range)) {
                return value.getKey();
            }
        }

        return null;
    }


    public void registerRange(String namespace, int max) {
        toRegister.add(new ModelDataRange(namespace, max));
    }

    public void beginningOfRegistryEvent(Configuration config) {
        for(String namespace : config.getKeys(true)) {
            priorityMap.put(namespace, config.getInt(namespace));
        }
    }

    public void endOfRegistryEvent() {
        boolean writeConfig;
        if(!priorityMap.isEmpty()) {
            toRegister.sort(comparator);
        }

        int lastId = 0;
        for(ModelDataRange dataRange : toRegister) {
            dataRange.setRangeStart(lastId);
            lastId = dataRange.getMaxRange() + 1;
            dataMap.put(dataRange.getNamespace(), dataRange);

        }
    }

    public static ModelDataRegistry getInstance() {
        return StockPlugin.getInstance().getModelDataRegistry();
    }

    private class ModelDataSort implements Comparator<ModelDataRange> {

        @Override
        public int compare(ModelDataRange o1, ModelDataRange o2) {
                return priorityMap.get(o1.getNamespace()) - priorityMap.get(o2.getNamespace());
        }
    }
}
