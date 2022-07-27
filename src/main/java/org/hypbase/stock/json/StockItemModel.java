package org.hypbase.stock.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockItemModel {
    Map<NamespacedKey, Integer> itemKeys;

    String baseKey;

    public StockItemModel(String baseKey) {
        this.itemKeys = new HashMap<NamespacedKey, Integer>();
        this.baseKey = baseKey;
    }

    public void addItem(NamespacedKey itemKey, int customModelData) {
        if(!(itemKeys.containsKey(itemKey))) {
            itemKeys.put(itemKey, customModelData);
        }
    }


    public JsonObject build() {
        JsonObject baseObject = new JsonObject();
        baseObject.addProperty("parent", "item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "item/" + baseKey);
        JsonArray overrides = new JsonArray();
        for(var value : itemKeys.entrySet()) {
            JsonObject entry = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty("custom_model_data", value.getValue());
            entry.add("predicate", predicate);
            entry.addProperty("model", value.getKey().getNamespace() + ":" + "item/" + value.getKey().getKey());
            overrides.add(entry);
        }
        baseObject.add("textures", new JsonObject());
        return baseObject;
    }

    public String baseKey() {
        return this.baseKey;
    }
}
