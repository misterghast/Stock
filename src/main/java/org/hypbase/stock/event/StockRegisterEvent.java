package org.hypbase.stock.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.hypbase.stock.block.StockTierRegistry;
import org.hypbase.stock.item.ModelDataRegistry;
import org.hypbase.stock.item.StockItemsRegistry;
import org.jetbrains.annotations.NotNull;

public class StockRegisterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private StockItemsRegistry registry;
    private ModelDataRegistry dataRegistry;

    private StockTierRegistry tierRegistry;

    public StockRegisterEvent(StockItemsRegistry registry, ModelDataRegistry dataRegistry, StockTierRegistry tierRegistry) {

        this.registry = registry;
        this.dataRegistry = dataRegistry;
        this.tierRegistry = tierRegistry;
    }

    public StockItemsRegistry getRegistry() {
        return this.registry;
    }
    public ModelDataRegistry getModelRegistry() {return this.dataRegistry;}

    public StockTierRegistry getTierRegistry() {return this.tierRegistry;}

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
