package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.gui.inventory.ISimpleInventory;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

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
//            RecipeData data = RecipeAPI.getChoppingBoardRecipeFromInput(food);
//            if(data != null)
//            {
//                if(!world.isRemote)
//                {
//                    EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, data.getOutput().copy());
//                    world.spawnEntity(entityItem);
//                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), FurnitureSounds.knife_chop, SoundCategory.BLOCKS, 0.75F, 1.0F);
//                }
//                setFood(null);
//                TileEntityUtil.markBlockForUpdate(world, pos);
//                return true;
//            }
        }
        return false;
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
