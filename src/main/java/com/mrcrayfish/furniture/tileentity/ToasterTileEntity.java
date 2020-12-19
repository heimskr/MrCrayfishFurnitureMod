package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.api.RecipeAPI;
import com.mrcrayfish.furniture.api.RecipeData;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.gui.inventory.ISimpleInventory;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;

public class ToasterTileEntity extends SyncClientTileEntity implements ITickableTileEntity, ISimpleInventory {
    public ItemStack[] slots = new ItemStack[2];

    private int toastingTime = 0;
    private boolean toasting = false;

    public ToasterTileEntity() {
        super(ModTileEntities.TOASTER);
    }

    public boolean addSlice(ItemStack item) {
        for(int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                slots[i] = item.copy();
                return true;
            }
        }
        return false;
    }

    public void removeSlice() {
        for(int i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                if(!world.isRemote) {
                    ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, slots[i]);
                    world.addEntity(entityItem);
                }
                slots[i] = null;
                TileEntityUtil.markBlockForUpdate(world, pos);
                return;
            }
        }
    }

    public void startToasting() {
        this.toasting = true;
        TileEntityUtil.markBlockForUpdate(world, pos);
    }

    public boolean isToasting() {
        return toasting;
    }

    public ItemStack getSlice(int slot) {
        return slots[slot];
    }

    @Override
    public void tick() {
        if (toasting) {
            if (toastingTime == 200) {
                for (int i = 0; i < slots.length; i++) {
                    if (slots[i] != null) {
                        if (!world.isRemote) {
                            RecipeData data = RecipeAPI.getToasterRecipeFromInput(slots[i]);
                            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, data.getOutput().copy());
                            world.addEntity(itemEntity);
                        }
                        slots[i] = null;
                    }
                }
//                if (!world.isRemote)
//                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, FurnitureSounds.toaster_down, SoundCategory.BLOCKS, 0.75F, 1.0F);
                toastingTime = 0;
                toasting = false;
                TileEntityUtil.markBlockForUpdate(world, pos);
                world.updateComparatorOutputLevel(pos, ModBlocks.TOASTER);
            } else
                toastingTime++;
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if (compound.contains("Items", 9)) {
            ListNBT tagList = (ListNBT) compound.get("Items");
            this.slots = new ItemStack[2];
            for (int i = 0; i < tagList.size(); ++i) {
                CompoundNBT itemTag = tagList.getCompound(i);
                byte slot = itemTag.getByte("Slot");
                if (slot >= 0 && slot < this.slots.length)
                    this.slots[slot] = ItemStack.read(itemTag);
            }
        }
        this.toastingTime = compound.getInt("ToastTime");
        this.toasting = compound.getBoolean("Toasting");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ListNBT tagList = new ListNBT();
        for (int slot = 0; slot < this.slots.length; ++slot) {
            if (this.slots[slot] != null) {
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putByte("Slot", (byte) slot);
                this.slots[slot].write(itemTag);
                tagList.add(itemTag);
            }
        }
        compound.put("Items", tagList);
        compound.putInt("ToastTime", this.toastingTime);
        compound.putBoolean("Toasting", toasting);
        return compound;
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public ItemStack getItem(int i) {
        return slots[i];
    }

    @Override
    public void clear() {
        for(int i = 0; i < slots.length; i++)
            slots[i] = null;
    }
}
