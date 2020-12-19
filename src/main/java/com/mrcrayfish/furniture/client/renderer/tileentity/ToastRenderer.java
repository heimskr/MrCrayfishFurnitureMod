package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.furniture.block.ToasterBlock;
import com.mrcrayfish.furniture.tileentity.ToasterTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import org.lwjgl.opengl.GL11;

public class ToastRenderer extends TileEntityRenderer<ToasterTileEntity> {
    private ItemEntity[] slots = {new ItemEntity(Minecraft.getInstance().world, 0D, 0D, 0D), new ItemEntity(Minecraft.getInstance().world, 0D, 0D, 0D)};

    public ToastRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }
    @Override
    public void render(ToasterTileEntity toaster, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Direction facing = toaster.getBlockState().get(ToasterBlock.DIRECTION);

        for (int i = 0; i < 2; i++) {
            ItemStack slice = toaster.getSlice(i);
            if (slice != null) {
                this.slots[i].setItem(slice);
//                GL11.glPushMatrix();
                stack.push();
//                GL11.glTranslatef((float) x + 0.5F, (float) y + 0.05F, (float) z + 0.3F);

                double xOffset = 0.0D;
                double zOffset = 0.0D;

                switch(facing) {
                    case EAST:
                        if (i == 1)
                            zOffset += 0.27F;
                        else
                            zOffset += 0.14F;
                        break;
                    case NORTH:
                        xOffset += 0.2F;
                        if (i == 1)
                            zOffset += 0.07F;
                        else
                            zOffset -= 0.06F;
                        GL11.glRotatef(270, 0, 1, 0);
                        break;
                    case WEST:
                        if (i == 1)
                            zOffset -= 0.13F;
                        else
                            zOffset -= 0.26F;
                        GL11.glRotatef(180, 0, 1, 0);
                        break;
                    case SOUTH:
                        xOffset -= 0.2F;
                        if(i == 1)
                            zOffset += 0.07F;
                        else
                            zOffset -= 0.06F;
                        GL11.glRotatef(90, 0, 1, 0);
                        break;
                }

                double yOffset = toaster.isToasting() ? -0.25 : -0.15;
                // E entityIn, double xIn, double yIn, double zIn, float rotationYawIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn
                Minecraft.getInstance().getRenderManager().renderEntityStatic(slots[i], 0.0D + xOffset, yOffset, 0.0D + zOffset, 0.0F, 0.0F, stack, buffer, combinedLight);
                stack.pop();
            }
        }
    }
}
