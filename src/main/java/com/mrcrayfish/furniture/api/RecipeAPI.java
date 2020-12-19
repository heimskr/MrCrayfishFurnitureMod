package com.mrcrayfish.furniture.api;

import net.minecraft.item.ItemStack;

public class RecipeAPI {
    public static final int LOCAL = 0;
    public static final int REMOTE = 1;
    public static final int COMMON = 2;

    public static void addMineBayRecipe(RecipeData itemData, int type) {
        switch(type) {
            case LOCAL:
                Recipes.localMineBayRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteMineBayRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonMineBayRecipes.add(itemData);
                break;
        }
    }

    public static void addOvenRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localOvenRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteOvenRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonOvenRecipes.add(itemData);
                break;
        }
    }

    public static void addFreezerRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localFreezerRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteFreezerRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonFreezerRecipes.add(itemData);
                break;
        }
    }

    public static void addPrinterRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localPrinterRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remotePrinterRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonPrinterRecipes.add(itemData);
                break;
        }
    }

    public static void addToasterRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localToasterRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteToasterRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonToasterRecipes.add(itemData);
                break;
        }
    }

    public static void addChoppingBoardRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localChoppingBoardRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteChoppingBoardRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonChoppingBoardRecipes.add(itemData);
                break;
        }
    }

    public static void addBlenderRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localBlenderRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteBlenderRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonBlenderRecipes.add(itemData);
                break;
        }
    }

    public static void addMicrowaveRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localMicrowaveRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteMicrowaveRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonMicrowaveRecipes.add(itemData);
                break;
        }
    }

    public static void addWashingMachineRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localWashingMachineRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteWashingMachineRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonWashingMachineRecipes.add(itemData);
                break;
        }
    }

    public static void addDishwasherRecipe(RecipeData itemData, int type) {
        switch (type) {
            case LOCAL:
                Recipes.localDishwasherRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteDishwasherRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonDishwasherRecipes.add(itemData);
                break;
        }
    }

    public static void addGrillRecipe(RecipeData itemData, int type) {
        switch(type) {
            case LOCAL:
                Recipes.localGrillRecipes.add(itemData);
                break;
            case REMOTE:
                Recipes.remoteGrillRecipes.add(itemData);
                break;
            case COMMON:
                Recipes.commonGrillRecipes.add(itemData);
                break;
        }
    }

    public static RecipeData[] getMineBayItems() {
        return Recipes.getMineBayItems();
    }

    public static RecipeData getOvenRecipeFromInput(ItemStack itemStack) {
        return Recipes.getOvenRecipeFromInput(itemStack);
    }

    public static RecipeData getFreezerRecipeFromInput(ItemStack itemStack) {
        return Recipes.getFreezerRecipeFromInput(itemStack);
    }

    public static RecipeData getPrinterRecipeFromInput(ItemStack itemStack) {
        return Recipes.getPrinterRecipeFromInput(itemStack);
    }

    public static RecipeData getToasterRecipeFromInput(ItemStack itemStack) {
        return Recipes.getToasterRecipeFromInput(itemStack);
    }

    public static RecipeData getChoppingBoardRecipeFromInput(ItemStack itemStack) {
        return Recipes.getChoppingBoardRecipeFromInput(itemStack);
    }

    public static RecipeData getBlenderRecipeDataFromIngredients(ItemStack[] itemStack) {
        return Recipes.getBlenderRecipeFromIngredients(itemStack);
    }

    public static RecipeData getMicrowaveRecipeFromIngredients(ItemStack itemStack) {
        return Recipes.getMicrowaveRecipeFromInput(itemStack);
    }

    public static RecipeData getWashingMachineRecipeFromInput(ItemStack itemStack) {
        return Recipes.getWashingMachineRecipeFromInput(itemStack);
    }

    public static RecipeData getDishwasherRecipeFromInput(ItemStack itemStack) {
        return Recipes.getDishwasherRecipeFromInput(itemStack);
    }

    public static RecipeData getGrillRecipeFromInput(ItemStack itemStack) {
        return Recipes.getGrillRecipeFromInput(itemStack);
    }
}
