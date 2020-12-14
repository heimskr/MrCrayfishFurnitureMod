package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import com.mrcrayfish.furniture.tileentity.PhotoFrameTileEntity;
import com.mrcrayfish.furniture.util.TileEntityUtil;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameBlock extends FurnitureTileBlock {
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 15);
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

//    private static final AxisAlignedBB[] BOUNDING_BOX = new Bounds(15, 0, 0, 16, 16, 16).getRotatedBounds();

    public PhotoFrameBlock(Properties properties) {
        super(properties);
//        this.setUnlocalizedName("photo_frame");
//        this.setRegistryName("photo_frame");
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
    }

//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side) {
//        return side.getHorizontalIndex() != -1;
//    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        return BOUNDING_BOX[state.getValue(FACING).getHorizontalIndex()];
//    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
//        BlockState state = this.getStateFromMeta(meta);
        BlockState state = super.getStateForPlacement(context);
        if (context.getPlacementHorizontalFacing().getHorizontalIndex() != -1) {
            return state.with(DIRECTION, context.getPlacementHorizontalFacing().getOpposite());
        }
        return state;
    }

    //    @Override
//    public IBlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos) {
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//        if (tileEntity instanceof TileEntityPhotoFrame) {
//            int colour = ((TileEntityPhotoFrame) tileEntity).getColour();
//            state = state.withProperty(COLOUR, colour);
//        }
//        return state;
//    }



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

//    @Override
//    public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items) {
//        for (int i = 0; i < DyeColor.values().length; i++) {
//            items.add(new ItemStack(this, 1, i));
//        }
//    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        int metadata = 0;
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
            if (tileEntity instanceof PhotoFrameTileEntity) {
                PhotoFrameTileEntity photoFrame = (PhotoFrameTileEntity) tileEntity;
                if (!heldItem.isEmpty()) {
                    if (heldItem.getItem() instanceof DyeItem) {
                        DyeItem dye = (DyeItem) heldItem.getItem();
                        if (photoFrame.getColor() != dye.getDyeColor().getColorValue()) {
                            photoFrame.setColor(dye.getDyeColor().getColorValue());
                            if (!playerIn.isCreative())
                                heldItem.shrink(1);
                            TileEntityUtil.markBlockForUpdate(world, pos);
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            if (tileEntity instanceof IValueContainer && tileEntity instanceof PhotoFrameTileEntity) {
                if (!((PhotoFrameTileEntity) tileEntity).isDisabled()) {
//                    playerIn.openGui(FurnitureMod.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new PhotoFrameTileEntity();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }

//    @Override
//    public BlockRenderLayer getBlockLayer() {
//        return BlockRenderLayer.CUTOUT;
//    }

//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, FACING, COLOUR);
//    }
}
