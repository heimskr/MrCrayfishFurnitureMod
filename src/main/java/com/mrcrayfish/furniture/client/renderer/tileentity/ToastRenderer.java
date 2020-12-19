package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.block.ToasterBlock;
import com.mrcrayfish.furniture.tileentity.ToasterTileEntity;
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

public class ToastRenderer extends TileEntityRenderer<ToasterTileEntity> {
    private ItemStack[] slots = new ItemStack[2];

    public ToastRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }
    @Override
    public void render(ToasterTileEntity toaster, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Direction facing = toaster.getBlockState().get(ToasterBlock.DIRECTION);

        for (int i = 0; i < 2; i++) {
            ItemStack slice = toaster.getSlice(i);
            if (slice != null) {
                this.slots[i] = slice;
//                GL11.glPushMatrix();
                stack.push();
//                GL11.glTranslatef((float) x + 0.5F, (float) y + 0.05F, (float) z + 0.3F);
                stack.translate(0.5F, 0.05F, 0.3F);

                double xOffset = 0.0D;
                double zOffset = 0.0D;

                switch(facing) {
                    case NORTH:
                    case SOUTH:
                        if (i == 1)
                            zOffset += 0.27F;
                        else
                            zOffset += 0.14F;
                        break;
//                        xOffset += 0.5F;
//                        break;
//                    case NORTH:
                    case EAST:
                    case WEST:
                        xOffset += 0.2F;
                        if (i == 1)
                            zOffset += 0.07F;
                        else
                            zOffset -= 0.06F;
//                        GL11.glRotatef(270, 0, 1, 0);
                        stack.rotate(Vector3f.YP.rotationDegrees(270F));
                        break;
//                    case WEST:
//                        if (i == 1)
//                            zOffset -= 0.13F;
//                        else
//                            zOffset -= 0.26F;
//                        stack.rotate(Vector3f.YP.rotationDegrees(180F));
//                        break;
//                    case SOUTH:
//                        xOffset -= 0.2F;
//                        if(i == 1)
//                            zOffset += 0.07F;
//                        else
//                            zOffset -= 0.06F;
//                        stack.rotate(Vector3f.YP.rotationDegrees(90F));
//                        break;
                }



                double yOffset = toaster.isToasting() ? -0.25 : -0.15;
                yOffset += 0.3F;
                // E entityIn, double xIn, double yIn, double zIn, float rotationYawIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn
                if (slots[i] == null) {
//                    for (int z = 0; z < 100; ++z) FurnitureMod.LOGGER.warn("Null slot " + i + "! " + z);;
                } else {
                    stack.translate(xOffset, yOffset, zOffset);
                    stack.scale(0.6F, 0.6F, 0.6F);
                    Minecraft.getInstance().getItemRenderer().renderItem(slots[i], ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, stack, buffer);
//                    Minecraft.getInstance().getRenderManager().renderEntityStatic(slots[i], 0.0D + xOffset, yOffset, 0.0D + zOffset, 0.0F, 0.0F, stack, buffer, combinedLight);
                }
                stack.pop();
            }
        }
    }
}
