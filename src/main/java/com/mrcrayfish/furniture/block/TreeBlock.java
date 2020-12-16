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
    public static final VoxelShape SHAPE_TOP    = VoxelShapeHelper.combineAll(Arrays.asList(Block.makeCuboidShape(4.5, 0.0 - 16.0, 4.5, 11.5, 1.0 - 16.0, 11.5), Block.makeCuboidShape(11.0, 0.5 - 16.0, 4.5, 12.0, 5.5 - 16.0, 11.5), Block.makeCuboidShape(4.0, 0.5 - 16.0, 4.5, 5.0, 5.5 - 16.0, 11.5), Block.makeCuboidShape(4.5, 0.5 - 16.0, 4.0, 11.5, 5.5 - 16.0, 5.0), Block.makeCuboidShape(4.5, 0.5 - 16.0, 11.0, 11.5, 5.5 - 16.0, 12.0), Block.makeCuboidShape(5.0, 4.0 - 16.0, 5.0, 11.0, 5.0 - 16.0, 11.0), Block.makeCuboidShape(7.0, 5.0 - 16.0, 7.0, 9.0, 16.0 - 16.0, 9.0), Block.makeCuboidShape(1.0, 7.0 - 16.0, 1.0, 15.0, 10.0 - 16.0, 15.0), Block.makeCuboidShape(2.0, 10.0 - 16.0, 2.0, 14.0, 13.0 - 16.0, 14.0), Block.makeCuboidShape(3.0, 13.0 - 16.0, 3.0, 13.0, 16.0 - 16.0, 13.0), Block.makeCuboidShape(4.0, 16.0 - 16.0, 4.0, 12.0, 19.0 - 16.0, 12.0), Block.makeCuboidShape(5.0, 19.0 - 16.0, 5.0, 11.0, 22.0 - 16.0, 11.0), Block.makeCuboidShape(6.0, 22.0 - 16.0, 6.0, 10.0, 25.0 - 16.0, 10.0), Block.makeCuboidShape(7.0, 25.0 - 16.0, 7.0, 9.0, 28.0 - 16.0, 9.0), Block.makeCuboidShape(6.0, 27.0 - 16.0, 7.5, 10.0, 31.0 - 16.0, 8.5)));
    public static final VoxelShape SHAPE_BOTTOM = VoxelShapeHelper.combineAll(Arrays.asList(Block.makeCuboidShape(4.5, 0.0, 4.5, 11.5, 1.0, 11.5), Block.makeCuboidShape(11.0, 0.5, 4.5, 12.0, 5.5, 11.5), Block.makeCuboidShape(4.0, 0.5, 4.5, 5.0, 5.5, 11.5), Block.makeCuboidShape(4.5, 0.5, 4.0, 11.5, 5.5, 5.0), Block.makeCuboidShape(4.5, 0.5, 11.0, 11.5, 5.5, 12.0), Block.makeCuboidShape(5.0, 4.0, 5.0, 11.0, 5.0, 11.0), Block.makeCuboidShape(7.0, 5.0, 7.0, 9.0, 16.0, 9.0), Block.makeCuboidShape(1.0, 7.0, 1.0, 15.0, 10.0, 15.0), Block.makeCuboidShape(2.0, 10.0, 2.0, 14.0, 13.0, 14.0), Block.makeCuboidShape(3.0, 13.0, 3.0, 13.0, 16.0, 13.0), Block.makeCuboidShape(4.0, 16.0, 4.0, 12.0, 19.0, 12.0), Block.makeCuboidShape(5.0, 19.0, 5.0, 11.0, 22.0, 11.0), Block.makeCuboidShape(6.0, 22.0, 6.0, 10.0, 25.0, 10.0), Block.makeCuboidShape(7.0, 25.0, 7.0, 9.0, 28.0, 9.0), Block.makeCuboidShape(6.0, 27.0, 7.5, 10.0, 31.0, 8.5)));

    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty TOP = BooleanProperty.create("top");


    public TreeBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(TOP, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(DIRECTION, context.getPlacementHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), this.getDefaultState().with(TOP, true).with(DIRECTION, placer.getHorizontalFacing()));
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
        builder.add(TOP);
    }

    @OnlyIn(Dist.CLIENT)
    public float func_220080_a(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(TOP)? SHAPE_TOP : SHAPE_BOTTOM;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader reader, BlockPos pos) {
        return reader.getBlockState(pos.up()).isAir();
    }

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

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        worldIn.destroyBlock(state.get(TOP)? pos.down() : pos.up(), false);
    }
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
        return state.get(TOP);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TreeTileEntity();
    }
}
