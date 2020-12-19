package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.gui.inventory.ISimpleInventory;
import com.mrcrayfish.furniture.item.crafting.ChoppingBoardRecipe;
import com.mrcrayfish.furniture.item.crafting.RecipeType;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.Constants;

import java.util.Optional;

public class ChoppingBoardTileEntity extends SyncClientTileEntity implements ISimpleInventory {
    private ItemStack food = null;

    public ChoppingBoardTileEntity() {
        super(ModTileEntities.CHOPPING_BOARD);
    }

    public void setFood(ItemStack food) {
        this.food = food;
    }

    public ItemStack getFood() {
        return food;
    }

    public boolean chopFood() {
        if (food != null) {
            Optional<ChoppingBoardRecipe> recipe = findMatchingRecipe(food);
            if (recipe.isPresent()) {
                if (!world.isRemote) {
                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, recipe.get().getRecipeOutput().copy()));
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, ModSounds.ITEM_KNIFE_CHOP, SoundCategory.BLOCKS, 0.75F, 1.0F);
                }
                setFood(null);
                TileEntityUtil.markBlockForUpdate(world, getPos());
                return true;
            }
        }
        return false;
    }

    public Optional<ChoppingBoardRecipe> findMatchingRecipe(ItemStack input) {
        if (input == null || input.isEmpty())
            return Optional.empty();
        return this.world.getRecipeManager().getRecipe(RecipeType.CHOPPING_BOARD, new Inventory(input), this.world);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if (compound.contains("Food", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT nbt = compound.getCompound("Food");
            food = ItemStack.read(nbt);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        CompoundNBT nbt = new CompoundNBT();
        if (food != null) {
            food.write(nbt);
            compound.put("Food", nbt);
        }
        return compound;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public ItemStack getItem(int i) {
        return food;
    }

    @Override
    public void clear() {
        food = null;
    }
}
