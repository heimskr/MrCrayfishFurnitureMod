package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.Bounds;
import com.mrcrayfish.furniture.util.CollisionHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class WreathBlock extends FurnitureBlock {
//    private static final AxisAlignedBB BOUNDING_BOX_NORTH = CollisionHelper.getBlockBounds(Direction.NORTH, 0.8125, 0, 0, 1, 1, 1);
//    private static final AxisAlignedBB BOUNDING_BOX_EAST  = CollisionHelper.getBlockBounds(Direction.EAST,  0.8125, 0, 0, 1, 1, 1);
//    private static final AxisAlignedBB BOUNDING_BOX_SOUTH = CollisionHelper.getBlockBounds(Direction.SOUTH, 0.8125, 0, 0, 1, 1, 1);
//    private static final AxisAlignedBB BOUNDING_BOX_WEST  = CollisionHelper.getBlockBounds(Direction.WEST,  0.8125, 0, 0, 1, 1, 1);
//    private static final AxisAlignedBB[] BOUNDING_BOX = {BOUNDING_BOX_SOUTH, BOUNDING_BOX_WEST, BOUNDING_BOX_NORTH, BOUNDING_BOX_EAST};

    public static final Bounds BOUNDS = new Bounds(0.8125, 0, 0, 0.99, 1, 1);

    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    public WreathBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
//        if (this.canPlaceCheck(world, pos, state)) {
//            Direction direction = state.get(DIRECTION);

//            if (!world.getBlockState(pos.offset(direction)).isNormalCube(world, pos)) {
//                this.breakBlock(world, pos, state);
//                this.dropBlockAsItem(world, pos, state, 0);
                System.out.println("IWorldReader instanceof World? " + (world instanceof World));

//            }
//        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.with(DIRECTION, rotation.rotate(state.get(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(DIRECTION)));
    }

    public boolean canPlaceBlockOnSide(IWorldReader world, BlockPos pos, Direction side) {
        return side.getHorizontalIndex() != -1;
    }

    private boolean canPlaceCheck(IWorldReader world, BlockPos pos, BlockState state) {
        Direction direction = state.get(DIRECTION);
        if(!this.canPlaceBlockOnSide(world, pos, direction)) {
//            this.dropBlockAsItem(world, pos, state, 0);
//            world.setBlockToAir(pos);
            return false;
        }

        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        AxisAlignedBB rotated = BOUNDS.getRotation(state.get(DIRECTION));
        return Block.makeCuboidShape(rotated.minX * 16F, rotated.minY * 16F, rotated.minZ * 16F, rotated.maxX * 16F, rotated.maxY * 16F, rotated.maxZ * 16F);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        EnumFacing facing = state.getValue(FACING);
//        return BOUNDING_BOX[facing.getHorizontalIndex()];
//    }
}
