package com.mrcrayfish.furniture.client;

import at.dhyan.open_imaging.GifDecoder;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.furniture.FurnitureMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class AnimatedTexture extends Texture {
    private List<ByteBuffer> framesTextureData;
    private List<NativeImage> natives;
    private int frameCounter;

    public AnimatedTexture(File file) {
        super(file);
    }

    @Override
    public void load(File file) {
        framesTextureData = Lists.newArrayList();
        natives = Lists.newArrayList();
        THREAD_SERVICE.submit(() -> {
//            for (int i = 0; i < 100; ++i) FurnitureMod.LOGGER.warn("Loading AnimatedTexture.");
            try {
                FileInputStream inputStream = new FileInputStream(file);
                GifDecoder.GifImage decoder = GifDecoder.read(inputStream);
                this.width = decoder.getWidth();
                this.height = decoder.getHeight();

                int frameCount = decoder.getFrameCount();
//                for (int i = 0; i < 100; ++i) FurnitureMod.LOGGER.warn("frameCount = " + frameCount);
                for (int i = 0; i < frameCount; i++) {
                    FurnitureMod.LOGGER.warn("Hello. i = " + i);
                    BufferedImage image = parseFrame(decoder.getFrame(i));
                    int width = image.getWidth(), height = image.getHeight();
                    NativeImage n = new NativeImage(width, height, true);
                    WritableRaster raster = image.getRaster();
                    FurnitureMod.LOGGER.warn("[[");
                    for (int j = 0; j < width; j++) {
//                        FurnitureMod.LOGGER.warn("j = " + j + " / " + (width - 1));
                        for (int k = 0; k < height; k++) {
//                            FurnitureMod.LOGGER.warn("k = " + k + " / " + (height - 1));
                            int[] c = raster.getPixel(j, k, new int[4]);
//                            n.setPixelRGBA(j, k, (((((c[3] >> 8) + c[2]) >> 8) + c[1]) >> 8) + c[0]);
//                            n.setPixelRGBA(j, k, (((((c[3] >> 8) | c[2]) >> 8) | c[1]) >> 8) + c[0]);
                            n.setPixelRGBA(j, k, c[0] | (c[1] << 8) | (c[2] << 16) | (c[3] << 24));
                        }
                    }
                    FurnitureMod.LOGGER.warn("]]");
                    int[] imageData = new int[this.width * this.height];
                    image.getRGB(0, 0, this.width, this.height, imageData, 0, this.width);
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    ImageIO.write(image, "PNG", stream);
//                    pngFrames.add(stream.toByteArray());
//                    ImageIO
                    framesTextureData.add(createBuffer(imageData));
                    natives.add(n);
//                    FurnitureMod.LOGGER.warn("Added native " + (natives.size() - 1));
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update() {
        FurnitureMod.LOGGER.warn("Ticking gif.");
        if (framesTextureData.size() > 0) {
            if (++frameCounter >= framesTextureData.size())
                frameCounter = 0;
            ByteBuffer buffer = framesTextureData.get(frameCounter);
//            GlStateManager.bindTexture(getTextureId());
            RenderSystem.bindTexture(getTextureId());
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        }
        if (counter++ >= 600) {
            delete = true;
            GlStateManager.deleteTexture(getTextureId());
        }
    }

    public ResourceLocation makeLocation() throws IOException {
//        byte[] bytes = pngFrames.get(frameCounter);
//        ByteBuffer buffer = ByteBuffer.wrap(bytes);
//        NativeImage nativeImage = NativeImage.read(this.framesTextureData.get(frameCounter));
//        NativeImage.
//        for (int i = 0; i < 25; ++i) FurnitureMod.LOGGER.warn("natives.size() = " + natives.size());
        DynamicTexture dynamicTexture = new DynamicTexture(natives.get(frameCounter));
        Minecraft.getInstance().deferTask(dynamicTexture::updateDynamicTexture);
        return Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("cfm_tv_" + frameCounter, dynamicTexture);
    }

    /**
     * Filters a BufferedImage and converts all non-opaque pixels to black.
     *
     * @param bufferedImage the image to filter
     * @return a new Bufferred
     */
    private static BufferedImage parseFrame(BufferedImage bufferedImage) {
        ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                return (((rgb >> 24) & 0xff) != 255)? 0 : rgb;
            }
        };

        ImageProducer producer = new FilteredImageSource(bufferedImage.getSource(), filter);
        Image image = Toolkit.getDefaultToolkit().createImage(producer);

        // Convert Image back to a BufferedImage
        BufferedImage resultBufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resultBufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return resultBufferedImage;
    }
}
