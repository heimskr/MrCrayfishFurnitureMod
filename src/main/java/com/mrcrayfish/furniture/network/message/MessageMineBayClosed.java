package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.tileentity.ComputerTileEntity;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMineBayClosed implements IMessage<MessageMineBayClosed> {
    private int x, y, z;

    public MessageMineBayClosed() {}

    public MessageMineBayClosed(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public MessageMineBayClosed decode(PacketBuffer buffer) {
        return new MessageMineBayClosed(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void encode(MessageMineBayClosed message, PacketBuffer buffer) {
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    @Override
    public void handle(MessageMineBayClosed message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = supplier.get().getSender();
            TileEntity tileEntity = player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (tileEntity instanceof ComputerTileEntity) {
                ComputerTileEntity computer = (ComputerTileEntity) tileEntity;
                if (!computer.getStackInSlot(0).isEmpty()) {
                    player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), computer.getStackInSlot(0)));
                    computer.setInventorySlotContents(0, ItemStack.EMPTY);
                }
                computer.setTrading(false);
            }
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            TileEntityUtil.markBlockForUpdate(player.world, pos);
        });
        supplier.get().setPacketHandled(true);
    }

}
