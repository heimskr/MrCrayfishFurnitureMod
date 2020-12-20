package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.furniture.block.BathBlock;
import com.mrcrayfish.furniture.tileentity.BathTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeHooksClient;

public class BathTileEntityRenderer extends TileEntityRenderer<BathTileEntity> {
    public BathTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BathTileEntity bath, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
        stack.push();
        stack.translate(0.5, 0.5, 0.5);
        Direction direction = bath.getBlockState().get(BathBlock.DIRECTION);
        stack.rotate(Vector3f.YP.rotationDegrees(direction.getHorizontalIndex() * -90F - 90F));
        stack.translate(-0.5, -0.5, -0.5);

        if (!bath.isEmpty()) {
            Fluid fluid = bath.getFluid().getFluid();
            TextureAtlasSprite sprite = ForgeHooksClient.getFluidSprites(bath.getWorld(), bath.getPos(), fluid.getDefaultState())[0];

            float height = 11F * 0.0625F;
            float width = 16F * 0.0625F;
            float x = 0F * 0.0625F, y = 2.1F * 0.0625F, z = 2F * 0.0625F;
            float depth = 12F * 0.0625F;

            float minU = sprite.getMinU();
            float maxU = Math.min(minU + (sprite.getMaxU() - minU) * depth, sprite.getMaxU());
            float minV = sprite.getMinV();
            float maxV = Math.min(minV + (sprite.getMaxV() - minV) * width, sprite.getMaxV());
            int waterColor = fluid.getAttributes().getColor(bath.getWorld(), bath.getPos());
            float red = (float) (waterColor >> 16 & 255) / 255.0F;
            float green = (float) (waterColor >> 8 & 255) / 255.0F;
            float blue = (float) (waterColor & 255) / 255.0F;

            height *= ((double) bath.getTank().getFluidAmount() / (double) bath.getTank().getCapacity());

            IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
            Matrix4f matrix = stack.getLast().getMatrix();

            light = 200;

            builder.pos(matrix, x, y + height, z).color(red, green, blue, 1.0F).tex(maxU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
            builder.pos(matrix, x, y + height, z + depth).color(red, green, blue, 1.0F).tex(minU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
            builder.pos(matrix, x + width, y + height, z + depth).color(red, green, blue, 1.0F).tex(minU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
            builder.pos(matrix, x + width, y + height, z).color(red, green, blue, 1.0F).tex(maxU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        }

        stack.pop();
    }
}
