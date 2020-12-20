package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.tileentity.ComputerTileEntity;
import com.mrcrayfish.furniture.util.Bounds;
import com.mrcrayfish.furniture.util.PlayerUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ComputerBlock extends FurnitureTileBlock {
    private static final Bounds BOUNDS = new Bounds(0.1, 0.0, 0.1, 0.9, 1.0, 0.9);

    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    public ComputerBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
//        if (placer instanceof PlayerEntity) {
//            Triggers.trigger(Triggers.PLACE_APPLIANCE, (EntityPlayer) placer);
//        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing());
    }


    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        FurnitureMod.LOGGER.warn("Hello.");

        if (tileEntity instanceof ComputerTileEntity) {
            FurnitureMod.LOGGER.warn("It's a tile entity.");

            ComputerTileEntity computer = (ComputerTileEntity) tileEntity;
            if (!computer.isTrading()) {
                FurnitureMod.LOGGER.warn("It's not trading.");
                computer.setTrading(true);
                if (!world.isRemote())
                    NetworkHooks.openGui((ServerPlayerEntity) player, computer, pos);
            } else if (!world.isRemote()) {
                FurnitureMod.LOGGER.warn("It's trading.");
                PlayerUtil.sendTranslatedMessage(player, "message.cfm.computer");
            }
        }
        else FurnitureMod.LOGGER.warn("It's not a tile entity.");
        return ActionResultType.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDS.toShape();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new ComputerTileEntity();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }
}
