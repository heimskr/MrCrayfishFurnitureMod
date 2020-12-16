package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import com.mrcrayfish.furniture.tileentity.PhotoFrameTileEntity;
import com.mrcrayfish.furniture.util.Bounds;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameBlock extends FurnitureTileBlock {
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 15);
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    private static final Bounds BOUNDS = new Bounds(15, 0, 0, 16, 16, 16);

    public PhotoFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (context.getPlacementHorizontalFacing().getHorizontalIndex() != -1)
            return state.with(DIRECTION, context.getPlacementHorizontalFacing().getOpposite());
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        AxisAlignedBB rotated = BOUNDS.getRotation(state.get(DIRECTION).getOpposite());
        return Block.makeCuboidShape(rotated.minX * 16F, rotated.minY * 16F, rotated.minZ * 16F, rotated.maxX * 16F, rotated.maxY * 16F, rotated.maxZ * 16F);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof PhotoFrameTileEntity) {
            if (stack.hasTag()) {
                CompoundNBT tag = stack.getTag();
                if (tag.contains("Color"))
                    ((PhotoFrameTileEntity) tileEntity).setColor(tag.getInt("Color"));
            }
        }
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack stack) {
        if (tileEntity instanceof PhotoFrameTileEntity) {
            PhotoFrameTileEntity photoFrame = (PhotoFrameTileEntity) tileEntity;
            ItemStack itemstack = new ItemStack(this, 1);
            CompoundNBT tag = itemstack.getOrCreateTag();
            tag.putInt("Color", photoFrame.getColor());
            spawnAsEntity(worldIn, pos, itemstack);
        } else {
            super.harvestBlock(worldIn, player, pos, state, tileEntity, stack);
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        ItemStack stack = new ItemStack(this, 1);
        CompoundNBT tag = stack.getOrCreateTag();
        if (tileEntity instanceof PhotoFrameTileEntity)
            tag.putInt("Color", ((PhotoFrameTileEntity) tileEntity).getColor());
        else
            tag.putInt("Color", 0);
        return stack;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult result) {
        if (world.isRemote) {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof PhotoFrameTileEntity && !((PhotoFrameTileEntity) tileEntity).isDisabled())
                FurnitureMod.PROXY.showPhotoFrameScreen(world, pos);
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new PhotoFrameTileEntity();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }
}
