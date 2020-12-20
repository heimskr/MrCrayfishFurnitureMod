package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.entity.SeatEntity;
import com.mrcrayfish.furniture.tileentity.BathTileEntity;
import com.mrcrayfish.furniture.util.Bounds;
import com.mrcrayfish.furniture.util.PlayerUtil;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BathBlock extends FurnitureTileBlock {
//    public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("level", 0, 16);
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    // Collision bounds
//    public static final AxisAlignedBB BOTTOM = new AxisAlignedBB(0, 0, 0, 1, 2 * 0.0625, 1);
//    public static final AxisAlignedBB SIDE_NORTH = CollisionHelper.getBlockBounds(EnumFacing.NORTH, 0.0, 0.125, 0.0, 1.0, 0.875, 0.125);
//    public static final AxisAlignedBB SIDE_EAST = CollisionHelper.getBlockBounds(EnumFacing.EAST, 0.0, 0.125, 0.0, 1.0, 0.875, 0.125);
//    public static final AxisAlignedBB SIDE_SOUTH = CollisionHelper.getBlockBounds(EnumFacing.SOUTH, 0.0, 0.125, 0.0, 1.0, 0.875, 0.125);
//    public static final AxisAlignedBB SIDE_WEST = CollisionHelper.getBlockBounds(EnumFacing.WEST, 0.0, 0.125, 0.0, 1.0, 0.875, 0.125);
//
//    public static final AxisAlignedBB[] TOP_BOXES = {new AxisAlignedBB(0, 0, -1, 1, 15 * 0.0625, 1), new AxisAlignedBB(0, 0, 0, 2, 15 * 0.0625, 1), new AxisAlignedBB(0, 0, 0, 1, 15 * 0.0625, 2), new AxisAlignedBB(-1, 0, 0, 1, 15 * 0.0625, 1)};
//    public static final AxisAlignedBB[] BOTTOM_BOXES = {new AxisAlignedBB(0, 0, 0, 1, 15 * 0.0625, 2), new AxisAlignedBB(-1, 0, 0, 1, 15 * 0.0625, 1), new AxisAlignedBB(0, 0, -1, 1, 15 * 0.0625, 1), new AxisAlignedBB(0, 0, 0, 2, 15 * 0.0625, 1)};

    public BathBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(TOP, false));
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return super.isValidPosition(state, worldIn, pos);
//        BlockState bs = worldIn.getBlockState(pos.offset(placer))
//        return worldIn.isAirBlock(pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getPlacementHorizontalFacing();
        BlockPos pos = context.getPos();
        BlockPos offset = pos.offset(direction);
//        BlockState state = super.getStateForPlacement(context);
//        return state.with(DIRECTION, context.getPlacementHorizontalFacing());
        return context.getWorld().getBlockState(offset).isReplaceable(context)? this.getDefaultState().with(DIRECTION, direction) : null;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (!world.isRemote) {
            world.setBlockState(pos.offset(placer.getHorizontalFacing()), ModBlocks.BATH.getDefaultState().with(TOP, true).with(DIRECTION, state.get(DIRECTION)));
            state.updateNeighbours(world, pos, 3);
        }
//        if (placer instanceof PlayerEntity)
//            Triggers.trigger(Triggers.PLACE_BATHTROOM_FURNITURE, (EntityPlayer) placer);
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        EnumFacing facing = state.getValue(FACING);
//        return state.getBlock() == FurnitureBlocks.BATH_1 ? BOTTOM_BOXES[facing.getHorizontalIndex()] : TOP_BOXES[facing.getHorizontalIndex()];
//    }

//    @Override
//    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
//        Direction facing = state.get(DIRECTION);
//        addCollisionBoxToList(pos, entityBox, collidingBoxes, BOTTOM);
//        if (!state.get(TOP)) {
//            if(facing != Direction.WEST)
//                addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_NORTH);
//            if(facing != Direction.EAST)
//                addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_SOUTH);
//            if(facing != Direction.NORTH)
//                addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_EAST);
//            if(facing != Direction.SOUTH)
//                addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_WEST);
//        } else {
//            if (facing != Direction.EAST)
//                addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_NORTH);
//            if (facing != Direction.WEST)
//                addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_SOUTH);
//            if (facing != Direction.SOUTH)
//                addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_EAST);
//            if (facing != Direction.NORTH)
//                addCollisionBoxToList(pos, entityBox, collidingBoxes, SIDE_WEST);
//        }
//    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(hand);
        BlockPos otherBathPos = pos.offset(state.get(TOP)? state.get(DIRECTION).getOpposite() : state.get(DIRECTION));

        TileEntity tileEntity1 = world.getTileEntity(pos);
        TileEntity tileEntity2 = world.getTileEntity(otherBathPos);
        if (tileEntity1 instanceof BathTileEntity && tileEntity2 instanceof BathTileEntity) {
            BathTileEntity bath1 = (BathTileEntity) tileEntity1;
            BathTileEntity bath2 = (BathTileEntity) tileEntity2;

            if (!heldItem.isEmpty()) {
                FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace());
                bath2.setFluid(bath1.getFluid());
                if (!world.isRemote) {
                    TileEntityUtil.sendUpdatePacket(bath1);
                    TileEntityUtil.sendUpdatePacket(bath2);
                }
            } else {
                return SeatEntity.create(world, pos, 0.1, player);
//                return SeatUtil.sitOnBlock(world, pos.getX(), pos.getY(), pos.getZ(), player, 0);
            }
//            PacketHandler.instance.sendToAllAround(new MessageFillBath(bath1.getFluidAmount(), pos.getX(), pos.getY(), pos.getZ(), otherBathPos.getX(), otherBathPos.getY(), otherBathPos.getZ()), new TargetPoint(player.dimension, pos.getX(), pos.getY(), pos.getZ(), 128D));
//            world.markBlockRangeForRenderUpdate(pos, pos);
//            world.markBlockRangeForRenderUpdate(otherBathPos, otherBathPos);
        } else {
            FurnitureMod.LOGGER.warn("At least one tile entity is missing.");
        }
        return ActionResultType.SUCCESS;
    }

    public boolean isFluidContainer(ItemStack stack) {
        LazyOptional<IFluidHandlerItem> optional = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(stack, 1));
        if (optional.isPresent()) {
            IFluidHandlerItem handler = optional.resolve().get();
        }

        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!state.get(TOP))
            worldIn.destroyBlock(pos.offset(state.get(DIRECTION)), false);
        else
            worldIn.destroyBlock(pos.offset(state.get(DIRECTION).getOpposite()), false);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BathTileEntity();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
        builder.add(TOP);
    }

    public static boolean canPlaceBath(World world, BlockPos pos, Direction facing) {
        return (world.isAirBlock(pos) && world.isAirBlock(pos.offset(facing, 1)));
    }

    public boolean hasWaterSource(World world, BlockPos pos) {
        return world.getBlockState(pos.add(0, -2, 0)) == Blocks.WATER.getDefaultState();
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof BathTileEntity)
            return ((BathTileEntity) tileEntity).getComparatorLevel();
        return 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new BathTileEntity();
    }
}
