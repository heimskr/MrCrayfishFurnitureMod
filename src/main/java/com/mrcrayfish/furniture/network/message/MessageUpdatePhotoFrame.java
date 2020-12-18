package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.tileentity.DoorMatTileEntity;
import com.mrcrayfish.furniture.tileentity.PhotoFrameTileEntity;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.ObjectUtils;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageUpdatePhotoFrame implements IMessage<MessageUpdatePhotoFrame> {
    private BlockPos pos;
    private String url;
    private boolean stretch;

    public MessageUpdatePhotoFrame() {}

    public MessageUpdatePhotoFrame(BlockPos pos, String url, boolean stretch) {
        this.pos = pos;
        this.url = url;
        this.stretch = stretch;
    }

    @Override
    public void encode(MessageUpdatePhotoFrame message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeString(message.url, 128);
        buffer.writeBoolean(message.stretch);
    }

    @Override
    public MessageUpdatePhotoFrame decode(PacketBuffer buffer) {
        return new MessageUpdatePhotoFrame(buffer.readBlockPos(), buffer.readString(128), buffer.readBoolean());
    }

    @Override
    public void handle(MessageUpdatePhotoFrame message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = supplier.get().getSender();
            if (player != null) {
                World world = player.getServerWorld();
                if (world.isAreaLoaded(message.pos, 0)) {
                    TileEntity tileEntity = world.getTileEntity(message.pos);
                    if (tileEntity instanceof PhotoFrameTileEntity) {
                        PhotoFrameTileEntity photoFrameEntity = (PhotoFrameTileEntity) tileEntity;
                        photoFrameEntity.setStretched(message.stretch);
                        photoFrameEntity.setUrl(message.url);
                        if (!world.isRemote)
                            photoFrameEntity.loadUrl(message.url);
                        photoFrameEntity.markDirty();
                        TileEntityUtil.markBlockForUpdate(world, message.pos);
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
