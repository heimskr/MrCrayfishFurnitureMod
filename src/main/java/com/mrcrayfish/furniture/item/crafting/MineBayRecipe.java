package com.mrcrayfish.furniture.item.crafting;

import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MineBayRecipe implements IRecipe<IInventory> {
    protected final IRecipeType<?> type;
    protected final ResourceLocation id;
    protected final String group;
    protected final ItemStack input;
    protected final ItemStack result;

    public MineBayRecipe(ResourceLocation id, String group, ItemStack input, ItemStack result) {
        this.type = RecipeType.MINEBAY;
        this.id = id;
        this.group = group;
        this.input = input;
        this.result = result;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return inv.getStackInSlot(0).equals(this.input, false);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.COMPUTER);
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(Ingredient.fromStacks(this.input));
        return nonnulllist;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MINEBAY;
    }

    @Override
    public IRecipeType<?> getType() {
        return this.type;
    }

    public ItemStack getInput() {
        return this.input;
    }
}
