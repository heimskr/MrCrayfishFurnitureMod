package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.item.IAuthored;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageSignItem implements IMessage<MessageSignItem> {
    ListNBT itemList;

    public MessageSignItem(ListNBT itemList) {
        System.out.println("Constructing MessageSignItem with length " + itemList.size() + ".");
        this.itemList = itemList;
    }

    public MessageSignItem() {
        System.out.println("Constructing empty MessageSignItem.");
        this.itemList = null;
    }

    @Override
    public void handle(MessageSignItem message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            System.out.println("Handling MessageSignItem.");
            PlayerEntity player = supplier.get().getSender();
            ItemStack stack = player.inventory.getCurrentItem();
            if (stack.getItem() instanceof IAuthored) {
                ItemStack signedItem = new ItemStack(((IAuthored) stack.getItem()).getSignedItem(stack), 1);
                if (stack.getTag() != null)
                    signedItem.setTag(stack.getTag().copy());
                signedItem.setTagInfo("Author", StringNBT.valueOf(player.getName().getString()));
                CompoundNBT compound = new CompoundNBT();
                compound.put("Items", message.itemList);
                signedItem.setTagInfo("Present", compound);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, signedItem);
                player.closeScreen();
            } else {
                System.out.println("Item isn't an instance of IAuthored.");
            }
        });
        supplier.get().setPacketHandled(true);
    }

    @Override
    public void encode(MessageSignItem message, PacketBuffer buffer) {
        CompoundNBT compound = new CompoundNBT();
        if (message.itemList != null) {
            compound.put("Items", message.itemList);
        } else {
            System.out.println("MessageSignItem.itemList is null!");
        }
        buffer.writeCompoundTag(compound);
    }

    @Override
    public MessageSignItem decode(PacketBuffer buffer) {
        CompoundNBT compound = buffer.readCompoundTag();
        ListNBT list = new ListNBT();
        if (compound != null && compound.contains("Items")) {
            list = compound.getList("Items", Constants.NBT.TAG_COMPOUND);
        } else {
            System.out.println("Compound is null or doesn't contain Items.");
        }
        return new MessageSignItem(list);
    }
}
