package com.mrcrayfish.furniture.network.message;

import com.google.common.collect.Maps;
import com.mrcrayfish.furniture.client.gui.widget.ValueComponent;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MessageUpdateValueContainer implements IMessage<MessageUpdateValueContainer> {
    private Map<String, String> entryMap;
    private BlockPos pos;

    public MessageUpdateValueContainer() {}

    public MessageUpdateValueContainer(List<ValueComponent> valueEntries, IValueContainer valueContainer) {
        this.pos = valueContainer.getContainerPos();
        this.entryMap = valueEntries.stream().collect(Collectors.toMap(ValueComponent::getId, ValueComponent::getValue));
    }

    public MessageUpdateValueContainer(Map<String, String> entryMap, BlockPos pos) {
        this.entryMap = entryMap;
        this.pos = pos;
    }

    @Override
    public void encode(MessageUpdateValueContainer message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeInt(message.entryMap.size());
        message.entryMap.forEach((key, value) -> {
            buffer.writeString(key);
            buffer.writeString(value);
        });
    }

    @Override
    public MessageUpdateValueContainer decode(PacketBuffer buffer) {
        BlockPos newPos = buffer.readBlockPos();
        int size = buffer.readInt();
        Map<String, String> newEntryMap = Maps.newHashMap();
        for (int i = 0; i < size; i++) {
            String id = buffer.readString();
            String value = buffer.readString();
            newEntryMap.put(id, value);
        }
        return new MessageUpdateValueContainer(newEntryMap, newPos);
    }

    @Override
    public void handle(MessageUpdateValueContainer message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            if (message.pos == null || message.entryMap == null)
                return;
            ServerPlayerEntity player = supplier.get().getSender();
            System.out.println("Handling MessageUpdateValueContainer: " + message.pos.getCoordinatesAsString() + " / " + message.entryMap.size());
            if (player != null) {
                World world = player.getServerWorld();
                if (world.isAreaLoaded(message.pos, 0)) {
                    TileEntity tileEntity = world.getTileEntity(message.pos);
                    if (tileEntity instanceof IValueContainer) {
                        ((IValueContainer) tileEntity).updateEntries(message.entryMap);
                        TileEntityUtil.sendUpdatePacket(tileEntity);
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
