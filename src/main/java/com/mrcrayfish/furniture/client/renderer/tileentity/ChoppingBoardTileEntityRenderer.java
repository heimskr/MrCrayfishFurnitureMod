package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.furniture.block.ChoppingBoardBlock;
import com.mrcrayfish.furniture.tileentity.ChoppingBoardTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

public class ChoppingBoardTileEntityRenderer extends TileEntityRenderer<ChoppingBoardTileEntity> {
    private ItemStack item = null;

    public ChoppingBoardTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ChoppingBoardTileEntity board, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (board.getFood() != null) {
            item = board.getFood();

            stack.push();

            float xOffset = 0.0F;
            float zOffset = 0.0F;

            Direction facing = board.getBlockState().get(ChoppingBoardBlock.DIRECTION);

            switch(facing) {
                case NORTH:
                    zOffset -= 0.1F;
                    break;
                case EAST:
                    xOffset += 0.3F;
                    zOffset += 0.2F;
                    break;
                case SOUTH:
                    zOffset += 0.5F;
                    break;
                case WEST:
                    xOffset -= 0.3F;
                    zOffset += 0.2F;
                    break;
            }

            stack.translate(0.5F + xOffset, 0.02F, 0.3F + zOffset);
            stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));
            stack.rotate(new Vector3f(0, 0, 1).rotationDegrees(180F));
            stack.rotate(new Vector3f(1, 0, 0).rotationDegrees(90F));
            stack.translate(0, -0.35F + 0.05F, 0.05);
            stack.scale(0.45F, 0.45F, 0.45F);
            Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, stack, buffer);
            stack.pop();
        }
    }
}
