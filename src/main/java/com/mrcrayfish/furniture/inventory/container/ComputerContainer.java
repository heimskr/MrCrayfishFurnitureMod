package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ComputerContainer extends Container {
    private IInventory computerInventory;

    public ComputerContainer(int windowId, IInventory playerInventory, IInventory computerInventory) {
        super(ModContainers.COMPUTER, windowId);
        this.computerInventory = computerInventory;
        computerInventory.openInventory(null);

        this.addSlot(new Slot(computerInventory, 0, 119, 40));

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, j * 18 + 8, i * 18 + 95));

        for (int i = 0; i < 9; i++)
            this.addSlot(new Slot(playerInventory, i, i * 18 + 8, 153));
    }

    public boolean canInteractWith(PlayerEntity player) {
        return this.computerInventory.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotNum) {
        ItemStack itemCopy = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack()) {
            ItemStack item = slot.getStack();
            itemCopy = item.copy();

            if(slotNum == 0) {
                if (!this.mergeItemStack(item, 1, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.mergeItemStack(item, 0, 1, false))
                return ItemStack.EMPTY;

            if (item.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }

        return itemCopy;
    }
}
