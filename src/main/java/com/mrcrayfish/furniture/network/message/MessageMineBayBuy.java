package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.item.crafting.MineBayRecipe;
import com.mrcrayfish.furniture.item.crafting.RecipeType;
import com.mrcrayfish.furniture.tileentity.ComputerTileEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
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
                    return;

                List<MineBayRecipe> data = player.world.getRecipeManager().getRecipesForType(RecipeType.MINEBAY);

                if (message.itemNum < 0 || data.size() <= message.itemNum) {
                    FurnitureMod.LOGGER.warn("Invalid message.itemNum: " + message.itemNum);
                    return;
                }

                MineBayRecipe recipe = data.get(message.itemNum);
                Ingredient ingredient = recipe.getIngredients().get(0);
                ItemStack[] stacks = ingredient.getMatchingStacks();
                if (stacks.length != 1) {
                    FurnitureMod.LOGGER.warn("ingredient.getMatchingStacks().length == " + stacks.length);
                } else {
                    ItemStack cost = stacks[0];
                    int price = cost.getCount();
                    if (cost.getItem() == buySlot.getItem() && price <= buySlot.getCount()) {
                        computer.takeEmeraldFromSlot(price);
                        player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY() + 1, player.getPosZ(), recipe.getRecipeOutput().copy()));
//                        Triggers.trigger(Triggers.MINEBAY_PURCHASE, player);
                    }
                }
            } else FurnitureMod.LOGGER.warn("Tile entity isn't a ComputerTileEntity");
        });
        supplier.get().setPacketHandled(true);
    }

}
