package org.hypbase.stock.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hypbase.stock.item.StockItem;
import org.hypbase.stock.item.StockItemsRegistry;
import org.hypbase.stock.json.StockItemModel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenModelsCommand implements CommandExecutor {

    private File dataFolder;

    public GenModelsCommand(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        int i = 0;
        Map<String, StockItemModel> models = new HashMap<String, StockItemModel>();
        StockItemsRegistry.getInstance().iterator().forEachRemaining(stockitem -> {
            StockItem item = ((StockItem) stockitem);
            String baseItemKey = ((StockItem) stockitem).getBaseItem().getKey();
            NamespacedKey stockItemKey = ((StockItem) stockitem).getKey();
            if(!models.containsKey(((StockItem) stockitem).getKey())) {
                models.put(baseItemKey, new StockItemModel(baseItemKey));
            } else {
                models.get(baseItemKey).addItem(stockItemKey, ((StockItem) stockitem).getCustomModelData());
            }
        } );

        for(var value : models.entrySet()) {
            try {
                FileWriter writer = new FileWriter(dataFolder + "/generatedModels/" + value.getKey() + ".json");
                writer.write(value.getValue().build().getAsString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
