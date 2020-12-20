package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.tileentity.ComputerTileEntity;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMineBayBrowse implements IMessage<MessageMineBayBrowse> {
    private int itemNum, x, y, z;

    public MessageMineBayBrowse() {}

    public MessageMineBayBrowse(int itemNum, int x, int y, int z) {
        this.itemNum = itemNum;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public MessageMineBayBrowse decode(PacketBuffer buffer) {
        return new MessageMineBayBrowse(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
    }


    @Override
    public void encode(MessageMineBayBrowse message, PacketBuffer buffer) {
        buffer.writeInt(message.itemNum);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    @Override
    public void handle(MessageMineBayBrowse message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = supplier.get().getSender();
            TileEntity tileEntity = player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (tileEntity instanceof ComputerTileEntity) {
                ComputerTileEntity computer = (ComputerTileEntity) tileEntity;
                computer.setBrowsingInfo(message.itemNum);
            }
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            TileEntityUtil.markBlockForUpdate(player.world, pos);
        });
        supplier.get().setPacketHandled(true);
    }
}
