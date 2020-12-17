package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.Bounds;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;
import java.util.Locale;

public class FairyLightBlock extends FurnitureBlock {
    public static final EnumProperty<FairyLightType> TYPE = EnumProperty.create("type", FairyLightType.class);
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    private static final Bounds BOUNDS = new Bounds(0.375, 0.6875, 0.0, 0.6875, 1.0, 1.0);

    public FairyLightBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH).with(TYPE, FairyLightType.EVEN));
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return hasEnoughSolidSide(worldIn, pos.up(), Direction.DOWN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if ((context.getPos().getX() % 2) == (context.getPos().getZ() % 2))
            return this.getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing()).with(TYPE, FairyLightType.EVEN);
        return this.getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing()).with(TYPE, FairyLightType.ODD);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
        builder.add(TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        AxisAlignedBB rotated = BOUNDS.getRotation(state.get(DIRECTION));
        return Block.makeCuboidShape(rotated.minX * 16F, rotated.minY * 16F, rotated.minZ * 16F, rotated.maxX * 16F, rotated.maxY * 16F, rotated.maxZ * 16F);
    }

    public enum FairyLightType implements IStringSerializable {
        EVEN,
        ODD;

        @Override
        public String getString() { return this.toString().toLowerCase(Locale.US); }
    }
}
