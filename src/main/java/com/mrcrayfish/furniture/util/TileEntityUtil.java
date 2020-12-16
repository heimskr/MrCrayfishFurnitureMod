package com.mrcrayfish.furniture.util;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.stream.Stream;

/**
 * Author: MrCrayfish
 */
public class TileEntityUtil {
    /**
     * Sends an update packet to clients tracking a tile entity.
     *
     * @param tileEntity the tile entity to update
     */
    public static void sendUpdatePacket(TileEntity tileEntity) {
        SUpdateTileEntityPacket packet = tileEntity.getUpdatePacket();
        if (packet != null) {
            System.out.println("Sending an update packet.");
            sendUpdatePacket(tileEntity.getWorld(), tileEntity.getPos(), packet);
        } else {
            System.out.println("Not sending an update packet.");
        }
    }

    /**
     * Sends an update packet to clients tracking a tile entity with a specific CompoundNBT
     *
     * @param tileEntity the tile entity to update
     */
    public static void sendUpdatePacket(TileEntity tileEntity, CompoundNBT compound) {
        SUpdateTileEntityPacket packet = new SUpdateTileEntityPacket(tileEntity.getPos(), 0, compound);
        sendUpdatePacket(tileEntity.getWorld(), tileEntity.getPos(), packet);
    }

    private static void sendUpdatePacket(World world, BlockPos pos, SUpdateTileEntityPacket packet) {
        if (world instanceof ServerWorld) {
            System.out.println("Sending packet (this is a server world).");
            ServerWorld server = (ServerWorld) world;
            Stream<ServerPlayerEntity> players = server.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false);
            players.forEach(player -> player.connection.sendPacket(packet));
        } else {
            System.out.println("Not sending packet: this is a client world.");
        }
    }

    public static void markBlockForUpdate(World world, BlockPos pos) {
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }
}
