package com.mrcrayfish.furniture.item.crafting;

import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
public class ToasterCookingRecipe extends AbstractCookingRecipe {
    public ToasterCookingRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        super(RecipeType.TOASTER_COOKING, idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.TOASTER);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.TOASTER_COOKING;
    }
}
