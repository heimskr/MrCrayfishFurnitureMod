package com.mrcrayfish.furniture.api;

import com.mrcrayfish.furniture.FurnitureMod;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class Recipes {
    // Used for sending to clients
    public static ArrayList<String> recipeData = new ArrayList<>();

    /**
     * Recipes registered from the Config
     */
    public static ArrayList<RecipeData> localMineBayRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localOvenRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localFreezerRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localPrinterRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localToasterRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localChoppingBoardRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localBlenderRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localDishwasherRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localWashingMachineRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localMicrowaveRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> localGrillRecipes = new ArrayList<>();

    /**
     * Recipes registered through FMLInterModComms
     */
    public static ArrayList<RecipeData> commonMineBayRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonOvenRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonFreezerRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonPrinterRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonToasterRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonChoppingBoardRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonBlenderRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonDishwasherRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonWashingMachineRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonMicrowaveRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> commonGrillRecipes = new ArrayList<>();

    /**
     * Recipes registered from a server
     */
    public static ArrayList<RecipeData> remoteMineBayRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteOvenRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteFreezerRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remotePrinterRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteToasterRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteChoppingBoardRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteBlenderRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteDishwasherRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteWashingMachineRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteMicrowaveRecipes = new ArrayList<>();
    public static ArrayList<RecipeData> remoteGrillRecipes = new ArrayList<>();

    public static RecipeData[] getMineBayItems() {
        return getRecipes("minebay").toArray(new RecipeData[0]);
    }

    public static RecipeData getOvenRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("oven");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem())
                return recipe;
        }
        return null;
    }

    public static RecipeData getFreezerRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("freezer");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem())
                return recipe;
        }
        return null;
    }

    public static RecipeData getPrinterRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("printer");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem() && itemStack.getCount() == 1)
                return recipe;
        }
        return null;
    }

    public static RecipeData getToasterRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("toaster");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem())
                return recipe;
        }
        return null;
    }

    public static RecipeData getChoppingBoardRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("choppingboard");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem())
                return recipe;
        }
        return null;
    }

    public static RecipeData getBlenderRecipeFromIngredients(ItemStack[] itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("blender");
        for (RecipeData recipe: recipes) {
            int count = 0;
            ArrayList<ItemStack> ingredients = recipe.getIngredients();
            for (ItemStack item: itemStack) {
                if (item != null) {
                    for (ItemStack ingredient: ingredients) {
                        if (ingredient.getItem() == item.getItem() && ingredient.getCount() == item.getCount())
                            count++;
                        if (count == ingredients.size())
                            return recipe;
                    }
                }
            }
        }
        return null;
    }

    public static RecipeData getMicrowaveRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("microwave");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem())
                return recipe;
        }
        return null;
    }

    public static RecipeData getWashingMachineRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("washingmachine");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem())
                return recipe;
        }
        return null;
    }

    public static RecipeData getDishwasherRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("dishwasher");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem()) {
                return recipe;
            }
        }
        return null;
    }

    public static RecipeData getGrillRecipeFromInput(ItemStack itemStack) {
        ArrayList<RecipeData> recipes = getRecipes("grill");
        for (RecipeData recipe: recipes) {
            ItemStack validItemStack = recipe.getInput();
            if (validItemStack.getItem() == itemStack.getItem())
                return recipe;
        }
        return null;
    }

    public static void updateDataList() {
        recipeData.clear();
        for (RecipeData data : localMineBayRecipes)
            recipeData.add("type=minebay," + data.toString());
        for (RecipeData data : localOvenRecipes)
            recipeData.add("type=oven," + data.toString());
        for (RecipeData data : localFreezerRecipes)
            recipeData.add("type=freezer," + data.toString());
        for (RecipeData data : localPrinterRecipes)
            recipeData.add("type=printer," + data.toString());
        for (RecipeData data : localToasterRecipes)
            recipeData.add("type=toaster," + data.toString());
        for (RecipeData data : localChoppingBoardRecipes)
            recipeData.add("type=choppingboard," + data.toString());
        for (RecipeData data : localBlenderRecipes)
            recipeData.add("type=blender," + data.toString());
        for (RecipeData data : localMicrowaveRecipes)
            recipeData.add("type=microwave," + data.toString());
        for (RecipeData data : localWashingMachineRecipes)
            recipeData.add("type=washingmachine," + data.toString());
        for (RecipeData data : localDishwasherRecipes)
            recipeData.add("type=dishwasher," + data.toString());
        for (RecipeData data : localGrillRecipes)
            recipeData.add("type=grill," + data.toString());
    }

    public static ArrayList<RecipeData> getRecipes(String type) {
        if (FurnitureMod.PROXY.isSinglePlayer() || FurnitureMod.PROXY.isDedicatedServer()) {
            if (type.equalsIgnoreCase("minebay"))
                return localMineBayRecipes;
            if (type.equalsIgnoreCase("freezer"))
                return localFreezerRecipes;
            if (type.equalsIgnoreCase("oven"))
                return localOvenRecipes;
            if (type.equalsIgnoreCase("printer"))
                return localPrinterRecipes;
            if (type.equalsIgnoreCase("toaster"))
                return localToasterRecipes;
            if (type.equalsIgnoreCase("choppingboard"))
                return localChoppingBoardRecipes;
            if (type.equalsIgnoreCase("blender"))
                return localBlenderRecipes;
            if (type.equalsIgnoreCase("microwave"))
                return localMicrowaveRecipes;
            if (type.equalsIgnoreCase("washingmachine"))
                return localWashingMachineRecipes;
            if (type.equalsIgnoreCase("dishwasher"))
                return localDishwasherRecipes;
            if (type.equalsIgnoreCase("grill"))
                return localGrillRecipes;
        } else {
            if (type.equalsIgnoreCase("minebay"))
                return remoteMineBayRecipes;
            if (type.equalsIgnoreCase("freezer"))
                return remoteFreezerRecipes;
            if (type.equalsIgnoreCase("oven"))
                return remoteOvenRecipes;
            if (type.equalsIgnoreCase("printer"))
                return remotePrinterRecipes;
            if (type.equalsIgnoreCase("toaster"))
                return remoteToasterRecipes;
            if (type.equalsIgnoreCase("choppingboard"))
                return remoteChoppingBoardRecipes;
            if (type.equalsIgnoreCase("blender"))
                return remoteBlenderRecipes;
            if (type.equalsIgnoreCase("microwave"))
                return remoteMicrowaveRecipes;
            if (type.equalsIgnoreCase("washingmachine"))
                return remoteWashingMachineRecipes;
            if (type.equalsIgnoreCase("dishwasher"))
                return remoteDishwasherRecipes;
            if (type.equalsIgnoreCase("grill"))
                return remoteGrillRecipes;
        }
        return new ArrayList<>();
    }

    public static void addCommonRecipesToLocal() {
        localMineBayRecipes.addAll(commonMineBayRecipes);
        localOvenRecipes.addAll(commonOvenRecipes);
        localFreezerRecipes.addAll(commonFreezerRecipes);
        localPrinterRecipes.addAll(commonPrinterRecipes);
        localToasterRecipes.addAll(commonToasterRecipes);
        localChoppingBoardRecipes.addAll(commonChoppingBoardRecipes);
        localBlenderRecipes.addAll(commonBlenderRecipes);
        localMicrowaveRecipes.addAll(commonMicrowaveRecipes);
        localWashingMachineRecipes.addAll(commonWashingMachineRecipes);
        localDishwasherRecipes.addAll(commonDishwasherRecipes);
        localGrillRecipes.addAll(commonGrillRecipes);
    }

    public static void clearLocalRecipes() {
        localMineBayRecipes.clear();
        localOvenRecipes.clear();
        localFreezerRecipes.clear();
        localPrinterRecipes.clear();
        localToasterRecipes.clear();
        localChoppingBoardRecipes.clear();
        localBlenderRecipes.clear();
        localMicrowaveRecipes.clear();
        localWashingMachineRecipes.clear();
        localDishwasherRecipes.clear();
        localGrillRecipes.clear();
    }

    public static void clearRemoteRecipes() {
        remoteMineBayRecipes.clear();
        remoteOvenRecipes.clear();
        remoteFreezerRecipes.clear();
        remotePrinterRecipes.clear();
        remoteToasterRecipes.clear();
        remoteChoppingBoardRecipes.clear();
        remoteBlenderRecipes.clear();
        remoteMicrowaveRecipes.clear();
        remoteWashingMachineRecipes.clear();
        remoteDishwasherRecipes.clear();
        remoteGrillRecipes.clear();
    }

    public static void clearCommonRecipes() {
        commonMineBayRecipes.clear();
        commonOvenRecipes.clear();
        commonFreezerRecipes.clear();
        commonPrinterRecipes.clear();
        commonToasterRecipes.clear();
        commonChoppingBoardRecipes.clear();
        commonBlenderRecipes.clear();
        commonMicrowaveRecipes.clear();
        commonWashingMachineRecipes.clear();
        commonDishwasherRecipes.clear();
        commonGrillRecipes.clear();
    }

    public static void clearAll() {
        clearLocalRecipes();
        clearRemoteRecipes();
        clearCommonRecipes();
    }
}
