package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.api.RecipeAPI;
import com.mrcrayfish.furniture.api.RecipeData;
import com.mrcrayfish.furniture.tileentity.ToasterTileEntity;
import com.mrcrayfish.furniture.util.Bounds;
import com.mrcrayfish.furniture.util.CollisionHelper;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.List;

public class ToasterBlock extends FurnitureTileBlock {
//    private static final AxisAlignedBB BOUNDING_BOX_NORTH = CollisionHelper.getBlockBounds(EnumFacing.NORTH, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.45, 13 * 0.0625);
//    private static final AxisAlignedBB BOUNDING_BOX_EAST = CollisionHelper.getBlockBounds(EnumFacing.EAST, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.45, 13 * 0.0625);
//    private static final AxisAlignedBB BOUNDING_BOX_SOUTH = CollisionHelper.getBlockBounds(EnumFacing.SOUTH, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.45, 13 * 0.0625);
//    private static final AxisAlignedBB BOUNDING_BOX_WEST = CollisionHelper.getBlockBounds(EnumFacing.WEST, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.45, 13 * 0.0625);
//    private static final AxisAlignedBB[] BOUNDING_BOX = {BOUNDING_BOX_SOUTH, BOUNDING_BOX_WEST, BOUNDING_BOX_NORTH, BOUNDING_BOX_EAST};

//    private static final AxisAlignedBB COLLISION_BOX_NORTH = CollisionHelper.getBlockBounds(EnumFacing.NORTH, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.4, 13 * 0.0625);
//    private static final AxisAlignedBB COLLISION_BOX_NORTH = CollisionHelper.getBlockBounds(EnumFacing.NORTH, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.4, 13 * 0.0625);
//    private static final AxisAlignedBB COLLISION_BOX_EAST = CollisionHelper.getBlockBounds(EnumFacing.EAST, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.4, 13 * 0.0625);
//    private static final AxisAlignedBB COLLISION_BOX_SOUTH = CollisionHelper.getBlockBounds(EnumFacing.SOUTH, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.4, 13 * 0.0625);
//    private static final AxisAlignedBB COLLISION_BOX_WEST = CollisionHelper.getBlockBounds(EnumFacing.WEST, 5 * 0.0625, 0.0, 3 * 0.0625, 11 * 0.0625, 0.4, 13 * 0.0625);
//    private static final AxisAlignedBB[] COLLISION_BOX = {COLLISION_BOX_SOUTH, COLLISION_BOX_WEST, COLLISION_BOX_NORTH, COLLISION_BOX_EAST};

    public static final Bounds BOUNDS = new Bounds(5 * 0.0625, 0, 3 * 0.0625, 11 * 0.0625, 0.45, 13 * 0.0625);
    public static final Bounds COLLISION_BOUNDS = new Bounds(5 * 0.0625, 0, 3 * 0.0625, 11 * 0.0625, 0.4, 13 * 0.0625);

    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    public ToasterBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return rotatedShape(BOUNDS, state.get(DIRECTION));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return rotatedShape(COLLISION_BOUNDS, state.get(DIRECTION));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (context.getPlacementHorizontalFacing().getHorizontalIndex() != -1)
            state = state.with(DIRECTION, context.getPlacementHorizontalFacing());
        return state;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
//        if (placer instanceof PlayerEntity)
//            Triggers.trigger(Triggers.PLACE_APPLIANCE, (PlayerEntity) placer);
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(hand);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof ToasterTileEntity) {
            ToasterTileEntity tileEntityToaster = (ToasterTileEntity) tileEntity;
            if (!heldItem.isEmpty() && !tileEntityToaster.isToasting()) {
                RecipeData data = RecipeAPI.getToasterRecipeFromInput(heldItem);
                if (data != null) {
                    if (tileEntityToaster.addSlice(new ItemStack(heldItem.getItem(), 1))) {
                        TileEntityUtil.markBlockForUpdate(world, pos);
                        heldItem.shrink(1);
                    }
                } else
                    tileEntityToaster.removeSlice();
            } else {
                if (player.isSneaking()) {
                    if (!tileEntityToaster.isToasting()) {
                        tileEntityToaster.startToasting();
                        world.updateComparatorOutputLevel(pos, this);
//                        if (!worldIn.isRemote)
//                            worldIn.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, FurnitureSounds.toaster_down, SoundCategory.BLOCKS, 0.75F, 1.0F, false);
                    }
                } else if (!tileEntityToaster.isToasting())
                    tileEntityToaster.removeSlice();
            }
        }
        return ActionResultType.SUCCESS;
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        EnumFacing facing = state.getValue(FACING);
//        return BOUNDING_BOX[facing.getHorizontalIndex()];
//    }
//
//    @Override
//    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
//        EnumFacing facing = state.getValue(FACING);
//        addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX[facing.getHorizontalIndex()]);
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new ToasterTileEntity();
    }

//    @Override
//    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
//        ToasterTileEntity toaster = (ToasterTileEntity) world.getTileEntity(pos);
//        return toaster.isToasting() ? 1 : 0;
//    }
}
