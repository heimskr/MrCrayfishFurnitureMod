package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.PresentTileEntity;
import com.mrcrayfish.furniture.util.Bounds;
import com.mrcrayfish.furniture.util.PlayerUtil;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.IRegistryDelegate;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PresentBlock extends FurnitureBlock implements ITileEntityProvider {
    private static final VoxelShape SHAPE = Block.makeCuboidShape(4, 0, 4, 12, 5.6, 12);
    public static HashMap<DyeColor, IRegistryDelegate<Block>> colorRegistry = new HashMap<>();

    public DyeColor color;

    public PresentBlock(DyeColor color, AbstractBlock.Properties properties) {
        super(properties);
        this.color = color;

        colorRegistry.put(color, this.delegate);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        PresentTileEntity present = (PresentTileEntity) world.getTileEntity(pos);
        if (present != null) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 0.75F, 1.0F, false);
            if (world.isRemote)
                PlayerUtil.sendMessage(player, new TranslationTextComponent("cfm.message.present_christmas", TextFormatting.RED + present.ownerName));
//            Triggers.trigger(Triggers.UNWRAP_PRESENT, player);
        }
    }


    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
                world.updateComparatorOutputLevel(pos, this);
            }
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        return BOUNDING_BOX;
//    }

//    @Override
//    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
//        addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDING_BOX);
//    }

//    @Override
//    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
//        return null;
//    }

//    @Override
//    public int damageDropped(IBlockState state) {
//        return state.getValue(COLOUR).getMetadata();
//    }

//    @Override
//    public int getMetaFromState(IBlockState state) {
//        return state.getValue(COLOUR).getMetadata();
//    }

//    @Override
//    public IBlockState getStateFromMeta(int meta) {
//        return this.getDefaultState().withProperty(COLOUR, EnumDyeColor.byMetadata(meta));
//    }

//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, COLOUR);
//    }

//    @Override
//    public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items) {
//        for (int i = 0; i < EnumDyeColor.values().length; ++i) {
//            items.add(new ItemStack(this, 1, i));
//        }
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
//        return BlockFaceShape.UNDEFINED;
//    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new PresentTileEntity();
    }
}
