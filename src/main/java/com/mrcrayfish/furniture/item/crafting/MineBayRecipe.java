package com.mrcrayfish.furniture.item.crafting;

import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class MineBayRecipe extends AbstractCookingRecipe {
    public MineBayRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookTime) {
        super(RecipeType.MINEBAY, id, group, ingredient, result, experience, cookTime);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.COMPUTER);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MINEBAY;
    }
}
