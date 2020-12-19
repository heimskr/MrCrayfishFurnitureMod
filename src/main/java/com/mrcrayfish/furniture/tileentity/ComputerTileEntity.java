package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.inventory.container.ComputerContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class ComputerTileEntity extends FurnitureTileEntity {
    private int stockNum = 0;
    private boolean isTrading = false;

    public ComputerTileEntity() {
        super(ModTileEntities.COMPUTER,"computer", 1);
    }

    public void takeEmeraldFromSlot(int price) {
        this.getStackInSlot(0).shrink(price);
        this.markDirty();
    }

    public void setBrowsingInfo(int stockNum) {
        this.stockNum = stockNum;
    }

    public int getBrowsingInfo() {
        return stockNum;
    }

    public void setTrading(boolean isUsing) {
        this.isTrading = isUsing;
    }

    public boolean isTrading() {
        return isTrading;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.stockNum = compound.getInt("StockNum");
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
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("StockNum", stockNum);
        return compound;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        setTrading(true);
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        setTrading(false);
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerIn) {
        this.fillWithLoot(playerIn);
        return new ComputerContainer(windowId, playerInventory, this);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {

    }
}
