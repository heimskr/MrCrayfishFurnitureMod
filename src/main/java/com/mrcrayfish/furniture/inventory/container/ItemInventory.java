package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.util.NBTHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

/**
 * Author: MrCrayfish
 */
public class ItemInventory extends Inventory {
    private ItemStack stack;
    private boolean canInsertItems = true;
    private String title;

    public ItemInventory(ItemStack stack, String title, int slotCount) {
        super(slotCount);
        this.stack = stack;
        this.title = title;
        this.loadInventory();
    }

    public ItemInventory setCanInsertItems(boolean canInsertItems) {
        this.canInsertItems = canInsertItems;
        return this;
    }

    private void loadInventory() {
        ListNBT itemList = NBTHelper.getCompoundTag(stack, this.title).getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < itemList.size(); i++) {
            CompoundNBT slotEntry = itemList.getCompound(i);
            int j = slotEntry.getByte("Slot") & 0xff;
            if (j >= 0 && j < this.getSizeInventory())
                this.setInventorySlotContents(j, ItemStack.read(slotEntry));
        }
    }

    public boolean canInsertItems()
    {
        return canInsertItems;
    }
}
