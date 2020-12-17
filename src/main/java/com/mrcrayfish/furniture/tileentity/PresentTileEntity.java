package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class PresentTileEntity extends LockableLootTileEntity {
    protected NonNullList<ItemStack> inventory;
    public String ownerName = "Unknown";

    public PresentTileEntity() {
        super(ModTileEntities.PRESENT);
        this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    public void setOwner(String username) {
        this.ownerName = username;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.ownerName = compound.getString("OwnerName");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putString("OwnerName", ownerName);
        return compound;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> newInventory) {
        inventory = newInventory;
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }
}
