package com.mrcrayfish.furniture.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;

public class MineBayRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MineBayRecipe> {
    public MineBayRecipeSerializer() {}

    @Override
    public MineBayRecipe read(ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        JsonObject inputObject = JSONUtils.getJsonObject(json, "ingredient");
        ItemStack inputStack = CraftingHelper.getItemStack(inputObject, true);
        if (!json.has("result"))
            throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        ItemStack resultStack = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), true);
        return new MineBayRecipe(recipeId, s, inputStack, resultStack);
    }

    @Nullable
    @Override
    public MineBayRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        String group = buffer.readString(32767);
        ItemStack input = buffer.readItemStack();
        ItemStack output = buffer.readItemStack();
        return new MineBayRecipe(recipeId, group, input, output);
    }

    @Override
    public void write(PacketBuffer buffer, MineBayRecipe recipe) {
        buffer.writeString(recipe.getGroup());
        buffer.writeItemStack(recipe.getInput());
        buffer.writeItemStack(recipe.getRecipeOutput());
    }
}
