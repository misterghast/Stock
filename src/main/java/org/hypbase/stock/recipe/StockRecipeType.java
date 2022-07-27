package org.hypbase.stock.recipe;

import org.bukkit.inventory.ItemStack;

public class StockRecipeType {

    //after implementing this you'll have to make your own registry for recipes of the new custom type.
    private StockRecipeRegistry registry;
    private Format ingredientFormat;
    private int gridSize;
    private int outputs;

    private int outputChance;
    private boolean loweringChance;


    //0: "Singled"
    //1: "Shaped"
    //2: "Shapeless"
    public Format getIngredientFormat() {
        return ingredientFormat;
    }

    //only used by "shaped" recipes
    //vanilla crafting table uses 2x2, while inventory crafting is 1x1 (starts at 0, so actually 3x3 and 2x2).
    public int getGridSize() {
        if(gridSize == 0) {
            return 2;
        } else {
            return gridSize;
        }
    }

    //Returns the amount of results the recipe type usually has. Typically 1 but some weird custom types could have 2 or even 3. Starts at 1 because it's not an array :)
    public int outputs() {
        if(outputs == 0) {
            return 1;
        } else {
            return outputs;
        }
    }

    //Returns the chance of each secondary output. The chance is 50% to the power of the result slot (first result is 0, second is 1, third is 3) by default. Implement to change it.
    public double secondaryOutputChance(int resultSlot) {
        if(loweringChance) {
            return Math.pow(outputChance, resultSlot);
        } else {
            return 1;
        }
    }

    public enum Format {
        SINGLED,
        SHAPED,
        SHAPELESS
    }
}
