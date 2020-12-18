package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.Bounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class ModernTVBlock extends AbstractTVBlock {
    public static final BooleanProperty MOUNTED = BooleanProperty.create("mounted");

//    private static final AxisAlignedBB[] SELECTION_BOX = new Bounds(6, 0, -5, 11, 18, 21).getRotatedBounds();
//    private static final AxisAlignedBB[] SELECTION_BOX_MOUNTED = new Bounds(12, 2, -5, 16, 18, 21).getRotatedBounds();

//    private static final AxisAlignedBB[] COLLISION_BOX = new Bounds(7, 0, -4, 10, 17, 20).getRotatedBounds();
//    private static final AxisAlignedBB[] COLLISION_BOX_MOUNTED = new Bounds(13, 3, -4, 15, 17, 20).getRotatedBounds();

    private static final Bounds UNMOUNTED_BOUNDS = new Bounds(6, 0, -5, 11, 18, 21);
    private static final Bounds MOUNTED_BOUNDS = new Bounds(12, 2, -5, 16, 18, 21);

    public ModernTVBlock(AbstractBlock.Properties properties) {
        super(properties, 22, 12, 4.0, -0.35);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH).with(MOUNTED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (state.get(MOUNTED))
            return rotatedShape(MOUNTED_BOUNDS, state.get(DIRECTION));
        return rotatedShape(UNMOUNTED_BOUNDS, state.get(DIRECTION));
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
//        Direrction facing = state.getValue(FACING);
//        if (state.get(MOUNTED)) {
//            return SELECTION_BOX_MOUNTED[facing.getHorizontalIndex()];
//        }
//        return SELECTION_BOX[facing.getHorizontalIndex()];
//    }
//
//    @Override
//    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
//        EnumFacing facing = state.getValue(FACING);
//        if(state.getValue(MOUNTED))
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX_MOUNTED[facing.getHorizontalIndex()]);
//        else
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX[facing.getHorizontalIndex()]);
//    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Vector3d hit = context.getHitVec();
        BlockState state = super.getStateForPlacement(context);
        if (context.getFace().getHorizontalIndex() != -1)
            state = state.with(MOUNTED, true);
        if (context.getPlacementHorizontalFacing().getHorizontalIndex() != -1)
            state = state.with(DIRECTION, context.getPlacementHorizontalFacing().getOpposite());
        return state;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(MOUNTED);
    }

    @Override
    public double getScreenZOffset(BlockState state) {
        if (state.get(MOUNTED))
            return 4.65;
        return super.getScreenZOffset(state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return null;
    }
}
