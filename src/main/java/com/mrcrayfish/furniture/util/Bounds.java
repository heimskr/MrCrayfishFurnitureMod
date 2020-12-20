package com.mrcrayfish.furniture.util;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;

/**
 * Author: MrCrayfish
 */
public class Bounds {
    public double x1, y1, z1;
    public double x2, y2, z2;

    public Bounds(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public Bounds(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.x1 = x1 * 0.0625;
        this.y1 = y1 * 0.0625;
        this.z1 = z1 * 0.0625;
        this.x2 = x2 * 0.0625;
        this.y2 = y2 * 0.0625;
        this.z2 = z2 * 0.0625;
    }

    public Bounds(AxisAlignedBB aabb) {
        this.x1 = aabb.minX;
        this.y1 = aabb.minY;
        this.z1 = aabb.minX;
        this.x2 = aabb.maxX;
        this.y2 = aabb.maxY;
        this.z2 = aabb.maxZ;
    }

    public VoxelShape toShape() {
        return Block.makeCuboidShape(x1 * 16F, y1 * 16F, z1 * 16F, x2 * 16F, y2 * 16F, z2 * 16F);
    }

    public AxisAlignedBB toAABB() {
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    public AxisAlignedBB getRotation(Direction facing) {
        return CollisionHelper.getBlockBounds(facing, this);
    }

    public VoxelShape rotatedShape(Direction facing) {
        return new Bounds(getRotation(facing)).toShape();
    }

    public AxisAlignedBB[] getRotatedBounds() {
        AxisAlignedBB boundsNorth = CollisionHelper.getBlockBounds(Direction.NORTH, this);
        AxisAlignedBB boundsEast  = CollisionHelper.getBlockBounds(Direction.EAST,  this);
        AxisAlignedBB boundsSouth = CollisionHelper.getBlockBounds(Direction.SOUTH, this);
        AxisAlignedBB boundsWest  = CollisionHelper.getBlockBounds(Direction.WEST,  this);
        return new AxisAlignedBB[] { boundsSouth, boundsWest, boundsNorth, boundsEast };
    }
}
