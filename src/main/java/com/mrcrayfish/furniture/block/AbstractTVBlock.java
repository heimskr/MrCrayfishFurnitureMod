package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import com.mrcrayfish.furniture.tileentity.TVTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public abstract class AbstractTVBlock extends FurnitureTileBlock {
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    private int width;
    private int height;
    private double screenYOffset;
    private double screenZOffset;

    public AbstractTVBlock(Properties properties, int width, int height, double screenYOffset, double screenZOffset) {
        super(properties);
        this.width = width;
        this.height = height;
        this.screenYOffset = screenYOffset;
        this.screenZOffset = screenZOffset;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        System.out.println("AbstractTVBlock.onBlockActivate.");
        if (world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            System.out.println("World is remote. ");
            if (tileEntity == null) {
                System.out.println("Tile entity is null.");
            } else {
                System.out.println((tileEntity instanceof IValueContainer) + " " + (tileEntity instanceof TVTileEntity) + " " + tileEntity.getClass().getCanonicalName());
            }
            if (tileEntity instanceof IValueContainer && tileEntity instanceof TVTileEntity) {
                System.out.println("Tile entity is an instance of IValueContainer and TVTileEntity.");
                if (!((TVTileEntity) tileEntity).isDisabled()) {
                    System.out.println("TV isn't disabled.");
                    FurnitureMod.PROXY.showEditValueScreen(world, pos);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }

    public double getScreenZOffset(BlockState state) {
        return screenZOffset;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TVTileEntity(width, height, screenYOffset, screenZOffset);
    }
}
