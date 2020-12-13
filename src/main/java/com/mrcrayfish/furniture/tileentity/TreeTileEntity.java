package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.gui.inventory.ISimpleInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.ITickableTileEntity;

import java.util.Arrays;

public class TreeTileEntity extends TileEntity implements ITickableTileEntity, ISimpleInventory {
    private ItemStack[] ornaments = new ItemStack[4];

    public TreeTileEntity() {
        super(ModTileEntities.TREE);
    }

    @Override
    public int getSize() {
        return ornaments.length;
    }

    @Override
    public ItemStack getItem(int i) {
        return ornaments[i];
    }

    @Override
    public void clear() {
        Arrays.fill(ornaments, null);
    }

    @Override
    public void tick() {
    }

    public void addOrnament(Direction facing, ItemStack item) {
        ItemStack temp = ornaments[facing.getHorizontalIndex()];
        if (temp != null) {
            assert world != null;
            if (!world.isRemote) {
                ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0D, pos.getZ() + 0.5, temp);
                world.addEntity(entityItem);
            }
            ornaments[facing.getHorizontalIndex()] = null;
        }

        if (item != null) {
            ornaments[facing.getHorizontalIndex()] = item.copy().split(1);
        }
    }

    @Override
    public void read(BlockState blockState, CompoundNBT tagCompound) {
        super.read(blockState, tagCompound);

        if (tagCompound.contains("Items", 9)) {
            ListNBT tagList = (ListNBT) tagCompound.get("Items");
            this.ornaments = new ItemStack[this.getSize()];

            for (int i = 0; i < tagList.size(); ++i) {
                CompoundNBT itemTag = tagList.getCompound(i);
                int slot = itemTag.getByte("Slot") & 255;

                if (slot >= 0 && slot < this.ornaments.length)
                    this.ornaments[slot] = ItemStack.read(itemTag);
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        ListNBT tagList = new ListNBT();

        for (int slot = 0; slot < this.ornaments.length; ++slot) {
            if (this.ornaments[slot] != null) {
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putByte("Slot", (byte) slot);
                this.ornaments[slot].write(itemTag);
                tagList.add(itemTag);
            }
        }

        tagCompound.put("Items", tagList);
        return tagCompound;
    }
}
