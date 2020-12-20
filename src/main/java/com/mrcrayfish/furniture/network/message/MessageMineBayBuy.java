package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.tileentity.ComputerTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.function.Supplier;

public class MessageMineBayBuy implements IMessage<MessageMineBayBuy> {
    private int itemNum, x, y, z;

    public MessageMineBayBuy() {}

    public MessageMineBayBuy(int itemNum, int x, int y, int z) {
        this.itemNum = itemNum;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public MessageMineBayBuy decode(PacketBuffer buffer) {
        return new MessageMineBayBuy(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void encode(MessageMineBayBuy message, PacketBuffer buffer) {
        buffer.writeInt(message.itemNum);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    @Override
    public void handle(MessageMineBayBuy message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = supplier.get().getSender();
            TileEntity tileEntity = player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (tileEntity instanceof ComputerTileEntity) {
                ComputerTileEntity computer = (ComputerTileEntity) tileEntity;
                ItemStack buySlot = computer.getStackInSlot(0);
                if (buySlot.isEmpty())
                    return null;

                RecipeData[] data = Recipes.getMineBayItems();
                if (message.itemNum < 0 || message.itemNum >= data.length)
                    return null;

                RecipeData recipe = data[message.itemNum];
                int price = recipe.getPrice();
                if (recipe.getCurrency().getItem() == buySlot.getItem() && buySlot.getCount() >= price) {
                    computer.takeEmeraldFromSlot(price);
                    ItemEntity item = new ItemEntity(player.world, player.getPosX(), player.getPosY() + 1, player.getPosZ(), data[message.itemNum].getInput().copy());
                    player.world.addEntity(item);
//                    Triggers.trigger(Triggers.MINEBAY_PURCHASE, player);
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }

}
