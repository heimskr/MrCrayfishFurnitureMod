package com.mrcrayfish.furniture.item.crafting;

import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private int hashItemStack(ItemStack stack) {
        return Arrays.hashCode(new int[] {stack.getTranslationKey().hashCode(), stack.getCount(), stack.getTag() == null? 0 : stack.getTag().hashCode()});
    }

    // The server and client won't necessarily agree on the ordering from getRecipesForType, so it can be necessary to make them sortable.
    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] {hashItemStack(input), hashItemStack(result)});
    }

    public static List<MineBayRecipe> sort(List<MineBayRecipe> list) {
        return list.stream().sorted(Comparator.comparing(MineBayRecipe::hashCode)).collect(Collectors.toList());
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
