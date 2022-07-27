package org.hypbase.stock.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class StockRec {
    NamespacedKey name;
    StockRecipeType type;

    ItemStack[][] ingredients;
    ItemStack[] results;
}
