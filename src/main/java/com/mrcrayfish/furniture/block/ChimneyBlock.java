package com.mrcrayfish.furniture.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;
import java.util.Random;

public class ChimneyBlock extends Block {
    public static final EnumProperty<ChimneyType> TYPE = EnumProperty.create("type", ChimneyType.class);

    private static final VoxelShape SHAPE = Block.makeCuboidShape(2, 0.0, 2, 14, 16, 14);

    public ChimneyBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(TYPE, ChimneyType.TOP));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
        if (world.getBlockState(pos.up()).getBlock() == this)
            return state.with(TYPE, ChimneyType.BOTTOM);
        return state.with(TYPE, ChimneyType.TOP);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (state.get(TYPE) != ChimneyType.TOP)
            return;

        for (int i = 0; i < 2; i++) {
            double posX = 0.25 + (0.5 * random.nextDouble());
            double posZ = 0.25 + (0.5 * random.nextDouble());
            worldIn.spawnParticle(ParticleTypes.SMOKE,       pos.getX() + posX, pos.getY() + 0.9, pos.getZ() + posZ, 1, 0.0D, 0.0D, 0.0D, 1);
            worldIn.spawnParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + posX, pos.getY() + 0.9, pos.getZ() + posZ, 1, 0.0D, 0.0D, 0.0D, 1);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(TYPE);
    }

    public enum ChimneyType implements IStringSerializable {
        TOP,
        BOTTOM;

        @Override
        public String getString() {
            return this.toString().toLowerCase(Locale.US);
        }
    }

}
