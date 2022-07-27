package org.hypbase.stock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.hypbase.stock.block.StockTierRegistry;
import org.hypbase.stock.commands.GiveItemCommand;
import org.hypbase.stock.durability.DamageListener;
import org.hypbase.stock.event.StockListener;
import org.hypbase.stock.event.StockRegisterEvent;
import org.hypbase.stock.item.*;

import java.util.ArrayList;
import java.util.Map;

public class StockPlugin extends JavaPlugin {

    private StockItemUtil utils;
    private StockItemsRegistry registry;

    private ModelDataRegistry modelDataRegistry;

    private StockTierRegistry tierRegistry;
    private static StockPlugin INSTANCE = null;

    public static StockPlugin getInstance() {
        return INSTANCE;
    }

    public StockItemsRegistry getRegistry() {
        return registry;
    }

    public ModelDataRegistry getModelDataRegistry() {
        return modelDataRegistry;
    }

    public StockTierRegistry getTierRegistry() {return tierRegistry;}

    public StockItemUtil getUtils() {
        return this.utils;
    }

    public StockPlugin() {
        if(INSTANCE != null) {
            throw new IllegalStateException("Instanciating plugin twice.");
        }
        this.getDataFolder()
        INSTANCE = this;

        registry = new StockItemsRegistry();
        utils = new StockItemUtil();
        modelDataRegistry = new ModelDataRegistry();
        tierRegistry = new StockTierRegistry();
    }

    @Override
    public void onEnable() {
        System.out.println(Material.COAL.getTranslationKey());
        utils = new StockItemUtil();
        StockRegisterEvent registryEvent = new StockRegisterEvent(registry, modelDataRegistry, tierRegistry);
        this.getServer().getPluginManager().registerEvents(new StockListener(utils), this);
        beginRegistry(this.getConfig());
        this.getServer().getPluginManager().callEvent(registryEvent);
        endRegistry();
        if(!this.getDataFolder().isDirectory()) {
            this.getDataFolder().mkdir();
        }

        this.saveDefaultConfig();
        registerCommands();
        registerItemEventHandlers();
    }


    private void beginRegistry(Configuration config) {
        modelDataRegistry.beginningOfRegistryEvent(config);
    }

    private void endRegistry() {
        getTierRegistry().iterator().forEachRemaining(entry -> {
            System.out.println(((Map.Entry<NamespacedKey, StockItem.StockTiers>) entry).getValue());
        });
    }

    private void registerItemEventHandlers() {
        this.getServer().getPluginManager().registerEvents(StockListener.DEBUG_ExplosionStick, this);
        this.getServer().getPluginManager().registerEvents(new DamageListener(), this);
    }

    public void generateConfig(ArrayList<ModelDataRange> registeredPlugins) {
        for(ModelDataRange plugin : registeredPlugins) {
            this.getConfig().addDefault(plugin.getNamespace(), 0);
        }
    }
    private void registerCommands() {
        GiveItemCommand giveItemCommand = new GiveItemCommand(utils);
        this.getCommand("givestock").setExecutor(giveItemCommand);
    }


}