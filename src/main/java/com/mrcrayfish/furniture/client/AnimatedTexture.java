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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class AnimatedTexture extends Texture {
    private List<ByteBuffer> framesTextureData;
    private List<NativeImage> natives;
    private int frameCounter;
    private List<ResourceLocation> locations;
    private boolean ready = false;

    public AnimatedTexture(File file, String hash) {
        super(file, hash);
    }

    @Override
    public void load(File file) {
        framesTextureData = Lists.newArrayList();
        natives = Lists.newArrayList();
        THREAD_SERVICE.submit(() -> {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                GifDecoder.GifImage decoder = GifDecoder.read(inputStream);
                this.width = decoder.getWidth();
                this.height = decoder.getHeight();

                int frameCount = decoder.getFrameCount();
                locations = new ArrayList<>(frameCount);
                for (int i = 0; i < frameCount; i++) {
                    locations.add(null);
                    BufferedImage image = parseFrame(decoder.getFrame(i));
                    int width = image.getWidth(), height = image.getHeight();
                    NativeImage n = new NativeImage(width, height, true);
                    WritableRaster raster = image.getRaster();
                    for (int j = 0; j < width; j++)
                        for (int k = 0; k < height; k++) {
                            int[] c = raster.getPixel(j, k, new int[4]);
                            n.setPixelRGBA(j, k, c[0] | (c[1] << 8) | (c[2] << 16) | (c[3] << 24));
                        }
                    int[] imageData = new int[this.width * this.height];
                    image.getRGB(0, 0, this.width, this.height, imageData, 0, this.width);
                    framesTextureData.add(createBuffer(imageData));
                    natives.add(n);
                }

                ready = true;
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update() {
        if (ready && framesTextureData.size() > 0) {
            if (++frameCounter >= framesTextureData.size())
                frameCounter = 0;
        }
    }

    public ResourceLocation makeLocation() throws IOException {
        if (locations.get(frameCounter) == null) {
            DynamicTexture dynamicTexture = new DynamicTexture(natives.get(frameCounter));
            Minecraft.getInstance().deferTask(dynamicTexture::updateDynamicTexture);
            locations.set(frameCounter, Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("cfm_tv_" + hash + "_" + frameCounter, dynamicTexture));
        }
        return locations.get(frameCounter);
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
