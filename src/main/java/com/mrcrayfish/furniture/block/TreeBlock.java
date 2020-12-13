package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.tileentity.GrillTileEntity;
import com.mrcrayfish.furniture.tileentity.TreeTileEntity;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
// import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
// import net.minecraftforge.fml.relauncher.Side;
// import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

public class TreeBlock extends FurnitureBlock {
    // private static final AxisAlignedBB BOUNDING_BOX_BOTTOM = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 2.0, 0.9375);
    // private static final AxisAlignedBB BOUNDING_BOX_BOTTOM_ALT = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
    // private static final AxisAlignedBB BOUNDING_BOX_TOP = new AxisAlignedBB(0.0625, -1.0, 0.0625, 0.9375, 1.0, 0.9375);

    public static final VoxelShape SHAPE = VoxelShapeHelper.combineAll(Arrays.asList(Block.makeCuboidShape(0.0, 0.001, 0.0, 16.0, 32.0, 16.0)));
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

//    public static final BooleanProperty BOTTOM = BooleanProperty.create("BOTTOM");

    public TreeBlock(Properties properties) {
        super(properties);
//        super(material);
//        this.setLightLevel(0.3F);
//        if (top)
//            this.setCreativeTab(null);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(DIRECTION, context.getPlacementHorizontalFacing());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.with(DIRECTION, rotation.rotate(state.get(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(DIRECTION)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }

    @OnlyIn(Dist.CLIENT)
    public float func_220080_a(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader reader, BlockPos pos) {
        return reader.getBlockState(pos.up()).isAir();
    }

//    @Override
//    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
//        if (this == ModBlocks.TREE_BOTTOM) {
//            world.setBlockState(pos.up(), ModBlocks.TREE_TOP.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()));
//        }
//        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);
//    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult result) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TreeTileEntity) {
            TreeTileEntity tileEntityTree = (TreeTileEntity) tileEntity;
            tileEntityTree.addOrnament(playerIn.getHorizontalFacing(), heldItem);
            if (!heldItem.isEmpty())
                heldItem.shrink(1);
            TileEntityUtil.markBlockForUpdate(world, pos);
        }
        return ActionResultType.SUCCESS;
    }

    // @Override
    // public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
//        if (this == ModBlocks.TREE_TOP) {
//            if (player.getHeldItemMainhand() != null) {
//                if (player.getHeldItemMainhand().getItem() != Items.SHEARS)
//                    worldIn.destroyBlock(pos.down(), false);
//                else
//                    player.getHeldItemMainhand().damageItem(1, player, (player1) -> {
//                        player1.sendBreakAnimation(Hand.MAIN_HAND);
//                    });
//            } else {
//                worldIn.destroyBlock(pos.down(), false);
//            }
//        } else {
//            worldIn.destroyBlock(pos.up(), false);
//        }
    // }

//    @Override
//    public AxisAlignedBB getBoundingBox(BlockState state, IWorldReader source, BlockPos pos) {
//        if (this == ModBlocks.TREE_BOTTOM) {
//            if (source.getBlockState(pos.up()).getBlock() == ModBlocks.TREE_TOP)
//                return BOUNDING_BOX_BOTTOM;
//            else
//                return BOUNDING_BOX_BOTTOM_ALT;
//        } else {
//            return BOUNDING_BOX_TOP;
//        }
//    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune) {
//        return new ItemStack(ModBlocks.TREE_BOTTOM).getItem();
//    }

//    @Override
//    public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
//        return new ItemStack(ModBlocks.TREE_BOTTOM);
//    }

    // @SideOnly(Side.CLIENT)
    // public BlockRenderLayer getBlockLayer() {
    //     return BlockRenderLayer.CUTOUT;
    // }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TreeTileEntity();
    }
}
