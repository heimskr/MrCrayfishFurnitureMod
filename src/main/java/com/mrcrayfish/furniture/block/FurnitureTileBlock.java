package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.gui.inventory.ISimpleInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class FurnitureTileBlock extends FurnitureBlock implements ITileEntityProvider {
    public FurnitureTileBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return Container.calcRedstone(tileEntity);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IInventory) {
            IInventory inv = (IInventory) tileEntity;
            InventoryHelper.dropInventoryItems(world, pos, inv);
        } else if (tileEntity instanceof ISimpleInventory) {
            ISimpleInventory inv = (ISimpleInventory) tileEntity;
            NonNullList<ItemStack> stacks = NonNullList.create();
            for (int i = 0; i < inv.getSize(); ++i) {
                ItemStack stack = inv.getItem(i);
                if (stack != null)
                    stacks.add(stack);
            }
            InventoryHelper.dropItems(world, pos, stacks);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }
}
