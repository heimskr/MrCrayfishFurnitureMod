package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.inventory.container.slot.DisabledSlot;
import com.mrcrayfish.furniture.item.IItemInventory;
import com.mrcrayfish.furniture.tileentity.PresentTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */

public class PresentContainer extends Container {
    protected ItemInventory itemInventory;

    public PresentContainer(int windowId, PlayerInventory playerInventory, ItemInventory itemInventory) {
        super(ModContainers.PRESENT, windowId);
        this.itemInventory = itemInventory;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, j * 18 + 8, i * 18 + 84));

        int currentItemIndex = playerInventory.currentItem;
        for (int i = 0; i < 9; i++) {
            if (i == currentItemIndex)
                this.addSlot(new DisabledSlot(playerInventory, i, i * 18 + 8, 142));
            else
                this.addSlot(new Slot(playerInventory, i, i * 18 + 8, 142));
        }

        this.addSlot(new PortableSlot(itemInventory, 0, 8 + 63, 16));
        this.addSlot(new PortableSlot(itemInventory, 1, 8 + 81, 16));
        this.addSlot(new PortableSlot(itemInventory, 2, 8 + 63, 34));
        this.addSlot(new PortableSlot(itemInventory, 3, 8 + 81, 34));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return this.itemInventory.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotNum) {
        ItemStack itemCopy = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack()) {
            ItemStack item = slot.getStack();
            itemCopy = item.copy();

            if (item.getItem() instanceof IItemInventory)
                return ItemStack.EMPTY;

            if (slotNum < 4) {
                if (!this.mergeItemStack(item, 4, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.mergeItemStack(item, 0, 4, false))
                return ItemStack.EMPTY;

            if (item.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }

        return itemCopy;
    }

    public ItemInventory getItemInventory() {
        return itemInventory;
    }
}
