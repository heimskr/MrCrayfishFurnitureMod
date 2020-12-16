package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.furniture.block.PhotoFrameBlock;
import com.mrcrayfish.furniture.client.ImageCache;
import com.mrcrayfish.furniture.client.ImageDownloadThread;
import com.mrcrayfish.furniture.client.Texture;
import com.mrcrayfish.furniture.tileentity.PhotoFrameTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameTileEntityRenderer extends TileEntityRenderer<PhotoFrameTileEntity> {
    private static final ResourceLocation NOISE = new ResourceLocation("cfm:textures/noise.png");
    private static final Random RAND = new Random();

    public PhotoFrameTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(PhotoFrameTileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int i0, int i1) {
//        if (te.getWorld() != null) {
//            System.out.println("Rendering is " + (te.getWorld().isRemote? "remote" : "local") + ".");
//        } else System.out.println("Rendering is done in the void.");
        if (te.getPhoto() == null) {
//            System.out.println("Photo URL is null!");
            return;
        }

        BlockPos pos = te.getPos();
        BlockState state = te.getWorld().getBlockState(pos);
        if (!state.getProperties().contains(PhotoFrameBlock.DIRECTION)) {
            System.out.println("Not rendering block without direction??..");
            return;
        }

//        System.out.println("Oh hello there.");

        //GlStateManager.pushMatrix();
        stack.push();
        {
            double frameWidth = 14;
            double frameHeight = 14;
            double frameYOffset = 1;
            double frameZOffset = 7.49;
            final double x = te.getPos().getX(), y = te.getPos().getY(), z = te.getPos().getZ();

            ImageDownloadThread.ImageDownloadResult result = te.getResult();
            if (result != null && result != ImageDownloadThread.ImageDownloadResult.SUCCESS) {
                //GlStateManager.translate(x, y, z);
                stack.translate(x, y, z);

                //GlStateManager.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
                stack.translate(8 * 0.0625D, frameYOffset * 0.0625D, 8 * 0.0625D);

                Direction facing = state.get(PhotoFrameBlock.DIRECTION);

                //GlStateManager.rotate(facing.getHorizontalIndex() * -90F, 0, 1, 0);
                stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));

                //GlStateManager.translate(-frameWidth / 2 * 0.0625, 0, 0);
                stack.translate(-frameWidth / 2 * 0.0625, 0, 0);

                //GlStateManager.translate(0, 0, frameZOffset * 0.0625);
                stack.translate(0, 0, frameZOffset * 0.0625);

                //GlStateManager.translate(frameWidth * 0.0625 - 0.0625, frameHeight * 0.0625 - 0.0625, 0);
                stack.translate(frameWidth * 0.0625 - 0.0625, frameHeight * 0.0625 - 0.0625, 0);

                //GlStateManager.scale(1, -1, -1);
                stack.scale(1, -1, -1);

                //GlStateManager.scale(0.01F, 0.01F, 0.01F);
                stack.scale(0.01F, 0.01F, 0.01F);

                //GlStateManager.rotate(180F, 0, 1, 0);
                stack.rotate(Vector3f.YP.rotationDegrees(180F));

                String message = I18n.format(result.getKey());
                FontRenderer renderer = Minecraft.getInstance().fontRenderer;
//                List<String> lines = renderer.listFormattedStringToWidth(message, (int) ((frameWidth - 2.0) * 6.3));
//                for (int i = 0; i < lines.size(); i++)
//                    renderer.drawString(stack, lines.get(i), 0, renderer.FONT_HEIGHT * i, 16777215);
                renderer.drawString(stack, message, 0, 0, 16777215); // TODO: fix this garbage.
            } else {
                //GlStateManager.translate(x, y, z);
                stack.translate(x, y, z);
                //GlStateManager.enableBlend();
                RenderSystem.enableBlend();
                //GlStateManager.color(0.65F, 0.65F, 0.65F, 1.0F);
                //OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                //GlStateManager.disableLighting();
                RenderSystem.disableLighting();

                double startX = 0.0;
                double startY = 0.0;

                if (te.isLoading()) {
                    Minecraft.getInstance().getTextureManager().bindTexture(NOISE);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                    // Setups translations
                    //GlStateManager.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
                    stack.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
                    //EnumFacing facing = state.getValue(BlockFurnitureTile.FACING);
                    Direction facing = state.get(PhotoFrameBlock.DIRECTION);
                    //GlStateManager.rotate(facing.getHorizontalIndex() * -90F, 0, 1, 0);
                    stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));
                    //GlStateManager.translate(-frameWidth / 2 * 0.0625, 0, 0);
                    stack.translate(-frameWidth / 2 * 0.0625, 0, 0);
                    //GlStateManager.translate(0, 0, frameZOffset * 0.0625);
                    stack.translate(0, 0, frameZOffset * 0.0625);

                    double pixelScale = 1.0 / 256;
                    double scaledWidth = frameWidth * 4;
                    double scaledHeight = frameHeight * 4;
                    double u = ((int)((256 - scaledWidth) * RAND.nextDouble()) * pixelScale);
                    double v = ((int)((256 - scaledHeight) * RAND.nextDouble()) * pixelScale);

                    startX *= 0.0625;
                    startY *= 0.0625;
                    frameWidth *= 0.0625;
                    frameHeight *= 0.0625;

                    //Render the Image
                    //GlStateManager.translate(0, 0, -0.01 * 0.0625);
                    stack.translate(0, 0, -0.01 * 0.0625);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(startX, startY, 0).tex((float) u, (float) v).endVertex();
                    buffer.pos(startX, startY + frameHeight, 0).tex((float) u, (float) (v + scaledHeight * pixelScale)).endVertex();
                    buffer.pos(startX + frameWidth, startY + frameHeight, 0).tex((float) (u + scaledWidth * pixelScale), (float) (v + scaledHeight * pixelScale)).endVertex();
                    buffer.pos(startX + frameWidth, startY, 0).tex((float) (u + scaledWidth * pixelScale), (float) v).endVertex();
                    tessellator.draw();
                } else if (te.isLoaded()) {
                    Texture texture = ImageCache.INSTANCE.get(te.getPhoto());
                    if (texture != null) {
                        texture.bind();

                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                        double imageWidth = frameWidth;
                        double imageHeight = frameHeight;

                        if (!te.isStretched()) {
                            //Calculates the positioning and scale so the GIF keeps its ratio and renders within the screen
                            double scaleWidth = frameWidth / (double) texture.getWidth();
                            double scaleHeight = frameWidth / (double) texture.getHeight();
                            double scale = Math.min(scaleWidth, scaleHeight);
                            imageWidth = texture.getWidth() * scale;
                            imageHeight = texture.getHeight() * scale;
                            startX = (frameWidth - imageWidth) / 2.0;
                            startY = (frameHeight - imageHeight) / 2.0;
                        }

                        startX *= 0.0625;
                        startY *= 0.0625;
                        imageWidth *= 0.0625;
                        imageHeight *= 0.0625;

                        // Setup translations
                        //GlStateManager.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
                        stack.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
                        //EnumFacing facing = state.getValue(BlockFurnitureTile.FACING);
                        Direction facing = state.get(PhotoFrameBlock.DIRECTION);
                        //GlStateManager.rotate(facing.getHorizontalIndex() * -90F, 0, 1, 0);
                        stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));
                        //GlStateManager.translate(-frameWidth / 2 * 0.0625, 0, 0);
                        stack.translate(-frameWidth / 2 * 0.0625, 0, 0);
                        //GlStateManager.translate(0, 0, frameZOffset * 0.0625);
                        stack.translate(0, 0, frameZOffset * 0.0625);

                        //Render a black quad
                        Tessellator tessellator = Tessellator.getInstance();
                        BufferBuilder buffer = tessellator.getBuffer();
                        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                        buffer.pos(0, 0, 0).color(0, 0, 0, 255).endVertex();
                        buffer.pos(0, imageHeight * 0.0625, 0).color(0, 0, 0, 255).endVertex();
                        buffer.pos(imageWidth * 0.0625, imageHeight * 0.0625, 0).color(0, 0, 0, 255).endVertex();
                        buffer.pos(imageWidth * 0.0625, 0, 0).color(0, 0, 0, 255).endVertex();
                        tessellator.draw();

                        //Render the Image
                        //GlStateManager.translate(0, 0, -0.01 * 0.0625);
                        stack.translate(0, 0, -0.01 * 0.0625);
                        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                        buffer.pos(startX, startY, 0).tex(0, 0).endVertex();
                        buffer.pos(startX, startY + imageHeight, 0).tex(0, 1).endVertex();
                        buffer.pos(startX + imageWidth, startY + imageHeight, 0).tex(1, 1).endVertex();
                        buffer.pos(startX + imageWidth, startY, 0).tex(1, 0).endVertex();
                        tessellator.draw();
                    } else {
                        String photo = te.getPhoto();
                        if (photo != null)
                            te.loadUrl(photo);
                    }
                } else {
//                    System.out.println("Sad state of affairs here.");
                }
                //GlStateManager.disableBlend();
                RenderSystem.disableBlend();
                //GlStateManager.enableLighting();
                RenderSystem.enableLighting();
            }
        }
        //GlStateManager.popMatrix();
        stack.pop();
    }
}
