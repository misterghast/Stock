package org.hypbase.stock.recipe;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

public class RecipeTypeRegistry {

    private Map<NamespacedKey, StockRecipeType> recipeTypeRegistry;

    @Nullable
    public StockRecipeType get(@NotNull NamespacedKey namespacedKey) {
        return recipeTypeRegistry.get(namespacedKey);
    }

    @NotNull
    public Iterator iterator() {
        return null;
    }
}
