package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.furniture.block.PhotoFrameBlock;
import com.mrcrayfish.furniture.client.ImageCache;
import com.mrcrayfish.furniture.client.ImageDownloadThread;
import com.mrcrayfish.furniture.tileentity.PhotoFrameTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

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
        if (te.getPhoto() == null)
            return;

        BlockPos pos = te.getPos();
        BlockState state = te.getWorld().getBlockState(pos);
        if (!state.getProperties().contains(PhotoFrameBlock.DIRECTION))
            return;

        RenderSystem.enableDepthTest();

        stack.push();
        {
            double frameWidth = 14;
            double frameHeight = 14;
            double frameYOffset = 1;
            double frameZOffset = 7.49;
            final double x = te.getPos().getX(), y = te.getPos().getY(), z = te.getPos().getZ();

            ImageDownloadThread.ImageDownloadResult result = te.getResult();
            if (result != null && result != ImageDownloadThread.ImageDownloadResult.SUCCESS) {
                stack.translate(8 * 0.0625D, frameYOffset * 0.0625D, 8 * 0.0625D);
                Direction facing = state.get(PhotoFrameBlock.DIRECTION);
                stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));
                stack.translate(-frameWidth / 2 * 0.0625, 0, 0);
                stack.translate(0, 0, frameZOffset * 0.0625);
                stack.translate(frameWidth * 0.0625 - 0.0625, frameHeight * 0.0625 - 0.0625, 0);
                stack.scale(1, -1, -1);
                stack.scale(0.01F, 0.01F, 0.01F);

                String message = I18n.format(result.getKey());
                FontRenderer renderer = Minecraft.getInstance().fontRenderer;
//                List<String> lines = renderer.listFormattedStringToWidth(message, (int) ((frameWidth - 2.0) * 6.3));
//                for (int i = 0; i < lines.size(); i++)
//                    renderer.drawString(stack, lines.get(i), 0, renderer.FONT_HEIGHT * i, 16777215);
                renderer.drawString(stack, message, 0, 0, 16777215); // TODO: fix this garbage.
            } else {
                RenderSystem.pushLightingAttributes();
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                RenderSystem.disableLighting();
                RenderSystem.enableTexture();

                double startX = 0.0;
                double startY = 0.0;

                if (te.isLoading()) {
                    Minecraft.getInstance().getTextureManager().bindTexture(NOISE);

                    RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                    RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                    // Setups translations
                    stack.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
                    Direction facing = state.get(PhotoFrameBlock.DIRECTION);
                    stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));
                    stack.translate(-frameWidth / 2 * 0.0625, 0, 0);
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

                    // Render the image
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
                    DynamicTexture dynamicTexture = ImageCache.INSTANCE.getDynamic(te.getPhoto());
                    if (dynamicTexture != null) {
                        NativeImage nativeImage = dynamicTexture.getTextureData();
                        dynamicTexture.bindTexture();

                        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                        double imageWidth = frameWidth;
                        double imageHeight = frameHeight;

                        if (!te.isStretched()) {
                            // Calculates the positioning and scale so the image keeps its ratio and renders within the screen
                            int nativeWidth = nativeImage.getWidth();
                            int nativeHeight = nativeImage.getHeight();
                            double scaleWidth = frameWidth / (double) nativeWidth;
                            double scaleHeight = frameWidth / (double) nativeHeight;
                            double scale = Math.min(scaleWidth, scaleHeight);
                            imageWidth = nativeWidth * scale;
                            imageHeight = nativeHeight * scale;
                            startX = (frameWidth - imageWidth) / 2.0;
                            startY = (frameHeight - imageHeight) / 2.0;
                        }

                        startX *= 0.0625;
                        startY *= 0.0625;
                        imageWidth *= 0.0625;
                        imageHeight *= 0.0625;

                        // Set up translations
                        stack.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
                        Direction facing = state.get(PhotoFrameBlock.DIRECTION);
                        stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F /*!/- 90F/*!*/ + 180F));
                        stack.translate(-frameWidth / 2 * 0.0625, 0, 0);
                        stack.translate(0, 0, frameZOffset * 0.0625);

                        Tessellator tessellator = Tessellator.getInstance();
                        BufferBuilder buffer = tessellator.getBuffer();

                        // Render the image
                        stack.translate(0, 0, -0.01 * 0.0625);

                        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                        Matrix4f matrix4f = stack.getLast().getMatrix();
                        buffer.pos(matrix4f, (float) startX, (float) startY, 0).tex(1, 1).endVertex();
                        buffer.pos(matrix4f, (float) startX, (float) (startY + imageHeight), 0).tex(1, 0).endVertex();
                        buffer.pos(matrix4f, (float) (startX + imageWidth), (float) (startY + imageHeight), 0).tex(0, 0).endVertex();
                        buffer.pos(matrix4f, (float) (startX + imageWidth), (float) startY, 0).tex(0, 1).endVertex();
                        tessellator.draw();
                    } else {
                        String photo = te.getPhoto();
                        if (photo != null)
                            te.loadUrl(photo);
                    }
                }
                RenderSystem.disableBlend();
                RenderSystem.enableLighting();
                RenderSystem.popAttributes();
            }
        }
        stack.pop();

        RenderSystem.disableDepthTest();
    }
}
