package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.network.PacketHandler;
import com.mrcrayfish.furniture.tileentity.BathTileEntity;
import com.mrcrayfish.furniture.util.CollisionHelper;
import com.mrcrayfish.furniture.util.PlayerUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
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
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

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
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(DIRECTION, context.getPlacementHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        System.out.println("Salutations from onBlockPlacedBy");
        world.setBlockState(pos.offset(placer.getHorizontalFacing()), ModBlocks.BATH.getDefaultState().with(TOP, true).with(DIRECTION, state.get(DIRECTION)));
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
        if(tileEntity1 instanceof BathTileEntity && tileEntity2 instanceof BathTileEntity) {
            BathTileEntity bath1 = (BathTileEntity) tileEntity1;
            BathTileEntity bath2 = (BathTileEntity) tileEntity2;

            if (!heldItem.isEmpty()) {
                if (heldItem.getItem() == Items.BUCKET) {
                    if(bath1.hasWater()) {
                        if (!world.isRemote()) {
                            if (!player.isCreative()) {
                                if (heldItem.getCount() > 1) {
                                    if(player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
                                        heldItem.shrink(1);
                                } else
                                    player.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                            }
                            bath1.removeFluidLevel();
                            bath2.removeFluidLevel();
                            world.updateComparatorOutputLevel(pos, this);
                        } else
                            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                    }
                } else if(heldItem.getItem() == Items.WATER_BUCKET) {
                    if(!bath1.isFull()) {
                        if (!world.isRemote()) {
                            bath1.addFluidLevel();
                            bath2.addFluidLevel();
                            if (!player.isCreative())
                                player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                            world.updateComparatorOutputLevel(pos, this);
                        } else {
                            player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
                            world.addParticle(ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 0.75 + bath1.getFluidAmount() * 0.0265, pos.getZ() + 0.5, 0, 0.1, 0);
                        }
                    }
                } else if(heldItem.getItem() == Items.GLASS_BOTTLE) {
                    if(bath1.hasWater()) {
                        if (!world.isRemote()) {
                            if (!player.isCreative()) {
                                if (heldItem.getCount() > 1) {
                                    if (player.inventory.addItemStackToInventory(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER)))
                                        heldItem.shrink(1);
                                } else
                                    player.setHeldItem(hand, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER));
                            }
                            bath1.removeFluidLevel();
                            bath2.removeFluidLevel();
                            world.updateComparatorOutputLevel(pos, this);
                        } else
                            player.playSound(SoundEvents.ITEM_BOTTLE_FILL, 1.0F, 1.0F);
                    }
                } else if(PotionUtils.getPotionFromItem(heldItem) == Potions.WATER) {
                    if (!bath1.isFull()) {
                        if (!world.isRemote()) {
                            bath1.addFluidLevel();
                            bath2.addFluidLevel();
                            if (!player.isCreative()) {
                                if (heldItem.getItem() == Items.POTION)
                                    player.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE));
                                else
                                    player.setHeldItem(hand, ItemStack.EMPTY);
                            }
                            world.updateComparatorOutputLevel(pos, this);
                        } else {
                            player.playSound(SoundEvents.ITEM_BOTTLE_EMPTY, 1.0F, 1.0F);
                            world.addParticle(ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 0.75 + bath1.getFluidAmount() * 0.0265, pos.getZ() + 0.5, 0, 0.1, 0);
                        }
                    }
                } else if (!bath1.isFull()) {
                    if (hasWaterSource(world, pos)) {
                        if (state.get(TOP)) {
                            if (!world.isRemote) {
                                bath1.addFluidLevel();
                                bath2.addFluidLevel();
                                world.removeBlock(pos.add(0, -2, 0), false);
                                world.updateComparatorOutputLevel(pos, this);
                            } else
                                world.playSound(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, ModSounds.BLOCK_BATH_TAP, SoundCategory.BLOCKS, 0.75F, 0.8F, true);
                        }
                    } else if (!world.isRemote())
                        PlayerUtil.sendTranslatedMessage(player, "message.cfm.bath");
                }
            } else {
                if (player.isSneaking()) {
                    if (!bath1.isFull()) {
                        if (hasWaterSource(world, pos)) {
                            if (state.get(TOP)) {
                                if (!world.isRemote()) {
                                    bath1.addFluidLevel();
                                    bath2.addFluidLevel();
                                    world.removeBlock(pos.add(0, -2, 0), false);
                                    world.updateComparatorOutputLevel(pos, this);
                                } else
                                    world.playSound(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, ModSounds.BLOCK_BATH_TAP, SoundCategory.BLOCKS, 0.75F, 0.8F, true);
                            }
                        } else if (!world.isRemote())
                            PlayerUtil.sendTranslatedMessage(player, "cfm.message.bath");
                    }
                    return ActionResultType.SUCCESS;
                }
//                else return SeatUtil.sitOnBlock(world, pos.getX(), pos.getY(), pos.getZ(), player, 0);
            }
//            PacketHandler.instance.sendToAllAround(new MessageFillBath(bath1.getFluidAmount(), pos.getX(), pos.getY(), pos.getZ(), otherBathPos.getX(), otherBathPos.getY(), otherBathPos.getZ()), new TargetPoint(player.dimension, pos.getX(), pos.getY(), pos.getZ(), 128D));
//            world.markBlockRangeForRenderUpdate(pos, pos);
//            world.markBlockRangeForRenderUpdate(otherBathPos, otherBathPos);
        }
        return ActionResultType.SUCCESS;

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
