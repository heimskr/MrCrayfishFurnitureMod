package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.furniture.tileentity.TreeTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

public class TreeTileEntityRenderer extends TileEntityRenderer<TreeTileEntity> {
    private ItemEntity ornament = new ItemEntity(Minecraft.getInstance().world, 0D, 0D, 0D);

    public TreeTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TreeTileEntity tree, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i0, int i1) {
//        Block block = tree.getWorld().getBlockState(tree.getPos()).getBlock();

        float yOffset = 0.0F;
        float spread = 0.3F;

//        if(block == FurnitureBlocks.TREE_BOTTOM) {
//            spread = 0.45F;
//            yOffset = 0.5F;
//        }

//      GL11.glPushMatrix();
        matrixStack.push();

//      GL11.glTranslatef((float) x + 0.5F, (float) y + yOffset, (float) z + 0.5F);
        //matrixStack.translate((double) x + 0.5F, (double) y + yOffset, (double) z + 0.5F);
        matrixStack.translate(0.5F, yOffset, 0.5F);

//      GL11.glRotatef(90, 0, 1, 0);   matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(90F));

        for (int i = 0; i < tree.getSize(); i++) {
            ItemStack item = tree.getItem(i);
            if (item != null) {
                this.ornament.setItem(item);
//              this.ornament.hoverStart = 0F;

//              GL11.glPushMatrix();
                matrixStack.push();

                GL11.glDisable(GL11.GL_LIGHTING);


//              GL11.glRotatef(-90 * i, 0, 1, 0);
                matrixStack.rotate(Vector3f.YP.rotationDegrees(-90 * i));;
//              GL11.glTranslatef(spread, 0.0F, 0.0F);
                matrixStack.translate(spread, 0.0F, 0.0F);
//              GL11.glRotatef(90, 0, 1, 0);
                matrixStack.rotate(Vector3f.YP.rotationDegrees(90F));
//              GL11.glRotatef(-15, 1, 0, 0);
                matrixStack.rotate(Vector3f.XP.rotationDegrees(-15));;
//              GL11.glScalef(0.9F, 0.9F, 0.9F);
                matrixStack.scale(0.9F, 0.9F, 0.9F);

//              Minecraft.getMinecraft().getRenderManager().renderEntity(ornament, 0.0D, 0.0D, 0.0D, 180.0F, 0.0F, false);
                Minecraft.getInstance().getItemRenderer().renderItem(ornament.getItem(), ItemCameraTransforms.TransformType.FIXED, i0, i1, matrixStack, renderTypeBuffer);

                GL11.glEnable(GL11.GL_LIGHTING);

//              GL11.glPopMatrix();
                matrixStack.pop();
            }
        }

//      GL11.glPopMatrix();
        matrixStack.pop();
    }
}
