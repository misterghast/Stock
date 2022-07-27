package org.hypbase.stock.recipe;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class StockRecipeParser {
    private List<File> recipesFolders;
    public StockRecipeParser(Path configPath, String recipesFolder) {

    }

    public void addRecipeFolder(String folder) {
        recipesFolders.add(new File(folder));
    }

    public void loadRecipes() {
        //instantantiate vanilla-typed recipes.
        for(File rootFolder : recipesFolders) {
            for(File f : rootFolder.listFiles()) {
                
            }
        }
    }
}
