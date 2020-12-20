package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.TileFluidHandler;

public class BathTileEntity extends FluidHandlerSyncedTileEntity {
    public BathTileEntity() {
        super(ModTileEntities.BATH, FluidAttributes.BUCKET_VOLUME * 16);
    }

    public FluidStack getFluid() {
        return tank.getFluid();
    }

    public void setFluid(FluidStack stack) {
        tank.setFluid(stack.copy());
    }

    public boolean hasFluid() {
        return 0 < tank.getFluidAmount();
    }

    public boolean hasBucketAmount() {
        return FluidAttributes.BUCKET_VOLUME <= tank.getFluidAmount();
    }

    public boolean isFull() {
        return tank.getCapacity() == tank.getFluidAmount();
    }

    public boolean isEmpty() {
        return tank.isEmpty();
    }

    public int getFluidAmount() {
        return tank.getFluidAmount();
    }

    public void addFluidLevel() {
        if (tank.getFluidAmount() <= tank.getCapacity() - FluidAttributes.BUCKET_VOLUME) {
            FluidStack fluid = tank.getFluid();
            fluid.grow(FluidAttributes.BUCKET_VOLUME);
            tank.setFluid(fluid);
        }
    }

    public void removeFluidLevel() {
        if (FluidAttributes.BUCKET_VOLUME <= tank.getFluidAmount()) {
            FluidStack fluid = tank.getFluid();
            fluid.shrink(FluidAttributes.BUCKET_VOLUME);
            tank.setFluid(fluid);
        }
    }

    public void setFluidLevel(int level) {
        FluidStack fluid = tank.getFluid();
        fluid.setAmount(level);
        tank.setFluid(fluid);
    }

    public int getComparatorLevel() {
        return Math.min(15, (16 * tank.getFluidAmount()) / tank.getCapacity());
    }
}
