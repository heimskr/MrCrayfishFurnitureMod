package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.util.NBTHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class PresentTileEntity extends LockableLootTileEntity {
    public NonNullList<ItemStack> inventory;
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
        ListNBT list = compound.getList("Items", Constants.NBT.TAG_COMPOUND);
        System.out.println("Reading PresentTileEntity.");
        for (int i = 0; i < list.size(); ++i) {
            ItemStack stack = ItemStack.read(list.getCompound(i));
            System.out.println("stack: " + stack.getDisplayName() + " x " + stack.getCount());
            this.inventory.set(i, stack);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putString("OwnerName", ownerName);
        ListNBT list = new ListNBT();
        System.out.println("Writing PresentTileEntity.");
        for (ItemStack stack: inventory) {
            System.out.println("stack: " + stack.getDisplayName() + " x " + stack.getCount());
            CompoundNBT itemCompound = new CompoundNBT();
            stack.write(itemCompound);
            list.add(itemCompound);
        }
        compound.put("Items", list);
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
