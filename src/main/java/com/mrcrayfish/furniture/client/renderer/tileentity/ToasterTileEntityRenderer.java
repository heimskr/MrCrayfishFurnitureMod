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

public class ToasterTileEntityRenderer extends TileEntityRenderer<ToasterTileEntity> {
    private ItemStack[] slots = new ItemStack[2];

    public ToasterTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }
    @Override
    public void render(ToasterTileEntity toaster, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Direction facing = toaster.getBlockState().get(ToasterBlock.DIRECTION);

        for (int i = 0; i < 2; i++) {
            ItemStack slice = toaster.getSlice(i);
            if (slice != null) {
                this.slots[i] = slice;
                stack.push();
                stack.translate(0.5F, 0.05F, 0.3F);

                double xOffset = 0.0D;
                double zOffset = 0.0D;

                switch(facing) {
                    case NORTH:
                    case SOUTH:
                        zOffset += i == 1? 0.27F : 0.14F;
                        break;
                    case EAST:
                    case WEST:
                        xOffset += 0.2F;
                        zOffset += i == 1? 0.07F : -0.06F;
                        stack.rotate(Vector3f.YP.rotationDegrees(270F));
                        break;
                }

                double yOffset = toaster.isToasting() ? -0.25 : -0.15;
                yOffset += 0.4F;
                if (slots[i] != null) {
                    stack.translate(xOffset, yOffset, zOffset);
                    stack.scale(0.6F, 0.6F, 0.6F);
                    Minecraft.getInstance().getItemRenderer().renderItem(slots[i], ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, stack, buffer);
                }
                stack.pop();
            }
        }
    }
}
