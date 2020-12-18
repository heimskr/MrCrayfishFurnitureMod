package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.furniture.block.PhotoFrameBlock;
import com.mrcrayfish.furniture.client.ImageCache;
import com.mrcrayfish.furniture.client.ImageDownloadThread;
import com.mrcrayfish.furniture.tileentity.PhotoFrameTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
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

        stack.push();
        {
            double frameWidth = 14;
            double frameHeight = 14;
            double frameYOffset = 1;
            double frameZOffset = 7.49;

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
                renderer.drawString(stack, message, 0, 0, 16777215); // TODO: fix this garbage (see DoorMatTileEntityRenderer).
            } else {
                double startX = 0.0;
                double startY = 0.0;

                if (te.isLoading()) {
                    IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                    IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(NOISE));

                    RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                    RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                    // Setups translations
                    stack.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
                    Direction facing = state.get(PhotoFrameBlock.DIRECTION);
                    stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F + 180F));
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
                    Matrix4f matrix4f = stack.getLast().getMatrix();
                    builder.pos(matrix4f, (float) startX, (float) startY, 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex((float) u, (float) v).overlay(OverlayTexture.NO_OVERLAY).lightmap(i0).normal(0, 1, 0).endVertex();
                    builder.pos(matrix4f, (float) startX, (float) (startY + frameHeight), 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex((float) u, (float) (v + scaledHeight * pixelScale)).overlay(OverlayTexture.NO_OVERLAY).lightmap(i0).normal(0, 1, 0).endVertex();
                    builder.pos(matrix4f, (float) (startX + frameWidth), (float) (startY + frameHeight), 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex((float) (u + scaledWidth * pixelScale), (float) (v + scaledHeight * pixelScale)).overlay(OverlayTexture.NO_OVERLAY).lightmap(i0).normal(0, 1, 0).endVertex();
                    builder.pos(matrix4f, (float) (startX + frameWidth), (float) startY, 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex((float) (u + scaledWidth * pixelScale), (float) v).overlay(OverlayTexture.NO_OVERLAY).lightmap(i0).normal(0, 1, 0).endVertex();
                } else if (te.isLoaded()) {
                    ImageCache.DynamicPair pair = ImageCache.INSTANCE.getDynamic(te.getPhoto());
                    DynamicTexture dynamicTexture = pair.texture;
                    ResourceLocation location = pair.location;
                    if (dynamicTexture != null) {
                        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                        IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(location));
                        NativeImage nativeImage = dynamicTexture.getTextureData();

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
                        stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F + 180F));
                        stack.translate(-frameWidth / 2 * 0.0625, 0, 0);
                        stack.translate(0, 0, frameZOffset * 0.0625);

                        // Render the image
                        stack.translate(0, 0, -0.01 * 0.0625);
                        Matrix4f matrix4f = stack.getLast().getMatrix();
                        builder.pos(matrix4f, (float) startX, (float) startY, 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(i0).normal(0, 1, 0).endVertex();
                        builder.pos(matrix4f, (float) startX, (float) (startY + imageHeight), 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(i0).normal(0, 1, 0).endVertex();
                        builder.pos(matrix4f, (float) (startX + imageWidth), (float) (startY + imageHeight), 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(i0).normal(0, 1, 0).endVertex();
                        builder.pos(matrix4f, (float) (startX + imageWidth), (float) startY, 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(i0).normal(0, 1, 0).endVertex();
                    } else {
                        String photo = te.getPhoto();
                        if (photo != null)
                            te.loadUrl(photo);
                    }
                }
            }
        }
        stack.pop();

        RenderSystem.disableDepthTest();
    }
}
