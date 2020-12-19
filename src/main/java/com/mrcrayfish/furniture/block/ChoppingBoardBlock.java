package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.item.crafting.ChoppingBoardRecipe;
import com.mrcrayfish.furniture.item.crafting.ToasterCookingRecipe;
import com.mrcrayfish.furniture.tileentity.ChoppingBoardTileEntity;
import com.mrcrayfish.furniture.util.Bounds;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class ChoppingBoardBlock extends FurnitureTileBlock {
//    private static final AxisAlignedBB BOUNDING_BOX_ONE = CollisionHelper.getBlockBounds(EnumFacing.NORTH, 0.0, 0.0, 3 * 0.0625, 1.0, 1.5 * 0.0625, 13 * 0.0625);
//    private static final AxisAlignedBB BOUNDING_BOX_TWO = CollisionHelper.getBlockBounds(EnumFacing.EAST, 0.0, 0.0, 3 * 0.0625, 1.0, 1.5 * 0.0625, 13 * 0.0625);

//    private static final AxisAlignedBB COLLISION_BOX_ONE = CollisionHelper.getBlockBounds(EnumFacing.NORTH, 0.0, 0.0, 3 * 0.0625, 1.0, 0.0625, 13 * 0.0625);
//    private static final AxisAlignedBB COLLISION_BOX_TWO = CollisionHelper.getBlockBounds(EnumFacing.EAST, 0.0, 0.0, 3 * 0.0625, 1.0, 0.0625, 13 * 0.0625);
//    private static final Bounds BOUNDS = new Bounds(0.0, 0.0, 3 * 0.0625, 1.0, 1.5 * 0.0625, 13 * 0.0625);
//    private static final Bounds COLLISION_BOUNDS = new Bounds(0.0, 0.0, 3 * 0.0625, 1.0, 0.0625, 13 * 0.0625);
    private static final Bounds BOUNDS = new Bounds(3 * 0.0625, 0.0, 0.0, 13 * 0.0625, 1.5 * 0.0625, 1.0);
    private static final Bounds COLLISION_BOUNDS = new Bounds(3 * 0.0625, 0.0, 0.0, 13 * 0.0625, 0.0625, 1.0);

    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    public ChoppingBoardBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return rotatedShape(BOUNDS, state.get(DIRECTION));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return rotatedShape(COLLISION_BOUNDS, state.get(DIRECTION));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof ChoppingBoardTileEntity) {
            ChoppingBoardTileEntity board = (ChoppingBoardTileEntity) tileEntity;
            if (!heldItem.isEmpty()) {
                Optional<ChoppingBoardRecipe> optional = board.findMatchingRecipe(heldItem);
//                if (Recipes.getChoppingBoardRecipeFromInput(heldItem) != null) {
                if (optional.isPresent()) {
                    if (board.getFood() == null) {
                        board.setFood(new ItemStack(heldItem.getItem(), 1));
                        TileEntityUtil.markBlockForUpdate(worldIn, pos);
                        worldIn.updateComparatorOutputLevel(pos, this);
                        heldItem.shrink(1);
                    } else {
                        if (!worldIn.isRemote) {
                            ItemEntity entityFood = new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5, board.getFood());
                            worldIn.addEntity(entityFood);
                            board.setFood(null);
                            TileEntityUtil.markBlockForUpdate(worldIn, pos);
                        }
                        worldIn.updateComparatorOutputLevel(pos, this);
                    }
                    return ActionResultType.SUCCESS;
                } else if (heldItem.getItem() == ModItems.KNIFE && board.getFood() != null) {
                    if (board.chopFood())
                        heldItem.damageItem(1, playerIn, (PlayerEntity breaker) -> {});
                    return ActionResultType.SUCCESS;
                }
            }
            if (board.getFood() != null) {
                if (!worldIn.isRemote) {
                    ItemEntity entityFood = new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5, board.getFood());
                    worldIn.addEntity(entityFood);
                }
                board.setFood(null);
                TileEntityUtil.markBlockForUpdate(worldIn, pos);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }
        return ActionResultType.SUCCESS;
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
//    {
//        if(getMetaFromState(state) % 2 == 1)
//        {
//            return BOUNDING_BOX_ONE;
//        }
//        return BOUNDING_BOX_TWO;
//    }

//    @Override
//    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_)
//    {
//        if(getMetaFromState(state) % 2 == 1)
//        {
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX_ONE);
//            return;
//        }
//        addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX_TWO);
//    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof ChoppingBoardTileEntity)
            return ((ChoppingBoardTileEntity) world.getTileEntity(pos)).getFood() != null ? 1 : 0;
        return 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new ChoppingBoardTileEntity();
    }
}
