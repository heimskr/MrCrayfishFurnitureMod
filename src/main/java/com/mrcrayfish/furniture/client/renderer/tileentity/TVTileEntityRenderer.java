package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.furniture.block.AbstractTVBlock;
import com.mrcrayfish.furniture.client.AnimatedTexture;
import com.mrcrayfish.furniture.client.GifCache;
import com.mrcrayfish.furniture.client.GifDownloadThread;
import com.mrcrayfish.furniture.tileentity.TVTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class TVTileEntityRenderer extends TileEntityRenderer<TVTileEntity> {
    private static final ResourceLocation NOISE = new ResourceLocation("cfm:textures/noise.png");
    private static final Random RAND = new Random();

    public TVTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TVTileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int combinedLight, int combinedOverlay) {
        if (!te.isPowered())
            return;

        BlockPos pos = te.getPos();
        BlockState state = te.getWorld().getBlockState(pos);
        if (!state.getProperties().contains(AbstractTVBlock.DIRECTION))
            return;

        stack.push();
        {
            GifDownloadThread.ImageDownloadResult result = te.getResult();
            if (result != null && result != GifDownloadThread.ImageDownloadResult.SUCCESS) {
                System.out.println("result is neither null nor success.");
                stack.translate(8 * 0.0625, te.getScreenYOffset() * 0.0625, 8 * 0.0625);
                Direction facing = state.get(AbstractTVBlock.DIRECTION);
                stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));
                stack.translate(-te.getWidth() / 2 * 0.0625, 0, 0);
                stack.translate(0, 0, te.getScreenZOffset() * 0.0625);
                stack.translate(te.getWidth() * 0.0625 - 0.0625, te.getHeight() * 0.0625 - 0.0625, 0);
                stack.scale(1, -1, -1);
                stack.scale(0.01F, 0.01F, 0.01F);
                stack.rotate(Vector3f.YP.rotationDegrees(180F));

                String message = I18n.format(result.getKey());
                FontRenderer renderer = Minecraft.getInstance().fontRenderer;
//                List<String> lines = renderer.listFormattedStringToWidth(message, (int) ((te.getWidth() - 2.0) * 6.3));
//                for (int i = 0; i < lines.size(); i++) {
//                    renderer.drawString(lines.get(i), 0, renderer.FONT_HEIGHT * i, 16777215);
//                }
                renderer.drawString(stack, message, 0, 0, 16777215);
            } else {

//                GlStateManager.enableBlend();
//                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
//                GlStateManager.disableLighting();

                double startX = 0.0;
                double startY = 0.0;
                double width = te.getWidth();
                double height = te.getHeight();

                if (te.isLoading()) {
                    System.out.println("TE is loading.");
                    Minecraft.getInstance().getTextureManager().bindTexture(NOISE);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                    // Set up translations
                    stack.translate(8 * 0.0625, te.getScreenYOffset() * 0.0625, 8 * 0.0625);
                    Direction facing = state.get(AbstractTVBlock.DIRECTION);
                    stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));
                    stack.translate(-te.getWidth() / 2 * 0.0625, 0, 0);
                    stack.translate(0, 0, te.getScreenZOffset() * 0.0625);

                    double pixelScale = 1.0 / 256;
                    double scaledWidth = te.getWidth() * 4;
                    double scaledHeight = te.getHeight() * 4;
                    double u = ((int)((256 - scaledWidth) * RAND.nextDouble()) * pixelScale);
                    double v = ((int)((256 - scaledHeight) * RAND.nextDouble()) * pixelScale);

                    startX *= 0.0625;
                    startY *= 0.0625;
                    width *= 0.0625;
                    height *= 0.0625;

                    // Render the GIF
                    stack.translate(0, 0, -0.01 * 0.0625);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(startX, startY, 0).tex((float) u, (float) v).endVertex();
                    buffer.pos(startX, startY + height, 0).tex((float) u, (float) (v + scaledHeight * pixelScale)).endVertex();
                    buffer.pos(startX + width, startY + height, 0).tex((float) (u + scaledWidth * pixelScale), (float) (v + scaledHeight * pixelScale)).endVertex();
                    buffer.pos(startX + width, startY, 0).tex((float) (u + scaledWidth * pixelScale), (float) v).endVertex();
                    tessellator.draw();
                } else if (te.isLoaded()) {
                    AnimatedTexture texture = GifCache.INSTANCE.get(te.getCurrentChannel());
                    if (texture != null) {
                        IVertexBuilder builder = null;
                        try {
                            builder = bufferIn.getBuffer(RenderType.getEntitySolid(texture.makeLocation()));
                        } catch (Exception error) {
                            error.printStackTrace();
                        }

                        if (builder != null) {
                            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                            if (!te.isStretched()) {
                                // Calculates the positioning and scale so the GIF keeps its ratio and renders within the screen
                                double scaleWidth = (double) te.getWidth() / (double) texture.getWidth();
                                double scaleHeight = (double) te.getHeight() / (double) texture.getHeight();
                                double scale = Math.min(scaleWidth, scaleHeight);
                                width = texture.getWidth() * scale;
                                height = texture.getHeight() * scale;
                                startX = (te.getWidth() - width) / 2.0;
                                startY = (te.getHeight() - height) / 2.0;
                            }

                            startX *= 0.0625;
                            startY *= 0.0625;
                            width *= 0.0625;
                            height *= 0.0625;

                            // Set up translations
                            stack.translate(8 * 0.0625, te.getScreenYOffset() * 0.0625, 8 * 0.0625);
                            Direction facing = state.get(AbstractTVBlock.DIRECTION);
                            stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * -90F));
                            stack.translate(-te.getWidth() / 2 * 0.0625, 0, 0);
                            stack.translate(0, 0, te.getScreenZOffset() * 0.0625);

                            // Render a black quad
//                            Tessellator tessellator = Tessellator.getInstance();
//                            BufferBuilder buffer = tessellator.getBuffer();
//                            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
//                            buffer.pos(0, 0, 0).color(0, 0, 0, 255).endVertex();
//                            buffer.pos(0, te.getHeight() * 0.0625, 0).color(0, 0, 0, 255).endVertex();
//                            buffer.pos(te.getWidth() * 0.0625, te.getHeight() * 0.0625, 0).color(0, 0, 0, 255).endVertex();
//                            buffer.pos(te.getWidth() * 0.0625, 0, 0).color(0, 0, 0, 255).endVertex();
//                            tessellator.draw();

                            // Render the GIF
                            stack.translate(0, 0, -0.01 * 0.0625);
                            Matrix4f matrix4f = stack.getLast().getMatrix();
                            builder.pos(matrix4f, (float) startX, (float) startY, 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLight).normal(0, 1, 0).endVertex();
                            builder.pos(matrix4f, (float) startX, (float) (startY + height), 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLight).normal(0, 1, 0).endVertex();
                            builder.pos(matrix4f, (float) (startX + width), (float) (startY + height), 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLight).normal(0, 1, 0).endVertex();
                            builder.pos(matrix4f, (float) (startX + width), (float) startY, 0).color(1.0f, 1.0f, 1.0f, 1.0f).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLight).normal(0, 1, 0).endVertex();
                        } else System.out.println("Oh no, builder is null :(");
                    } else {
                        String currentChannel = te.getCurrentChannel();
                        System.out.println("Going to load " + currentChannel);
                        if (currentChannel != null)
                            te.loadUrl(currentChannel);
                    }
                }
            }
        }
        stack.pop();
    }
}
