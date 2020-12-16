package com.mrcrayfish.furniture.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public final class ImageCache {
    public static final ImageCache INSTANCE = new ImageCache();

    private final File cache;
    private Map<String, Texture> cacheMap = new HashMap<>();

    private Map<String, DynamicPair> dynamicMap = new HashMap<>();

    public class DynamicPair {
        public DynamicTexture texture;
        public ResourceLocation location;

        public DynamicPair(DynamicTexture texture, String hash) {
            this.texture = texture;
            this.location = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("cfm_photo_frame_" + hash, texture);
        }
    }

    private ImageCache() {
        cache = new File(Minecraft.getInstance().gameDir, "photo-frame-cache");
        cache.mkdir();
        this.init();
    }

    private void init() {
        try {
            FileUtils.writeStringToFile(new File(cache, "!read-me.txt"), "This is a cache for GIFs that are played on the TV (in MrCrayfish's Furniture Mod) in order to speed up load time.\nIt is safe to delete the entire folder in case you are running out of space, but it will mean that all GIFs will have to be downloaded again.", "UTF-8");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /*
    @Nullable
    public Texture get(String url) {
        if (url == null)
            return null;
        synchronized (this) {
            Texture texture = cacheMap.get(url);
            if (texture != null)
                return texture;
        }
        return null;
    }
    //*/

    @Nullable
    public DynamicPair getDynamic(String url) {
        if (url == null)
            return null;
        synchronized (this) {
            DynamicPair pair = dynamicMap.get(url);
            if (pair != null)
                return pair;
        }
        return null;
    }

    public void add(String url, File file) {
        synchronized (this) {
//            if (!cacheMap.containsKey(url)) {
//                Texture texture = new Texture(file);
//                cacheMap.put(url, texture);
//            }
            if (!dynamicMap.containsKey(url)) {
                try {
                    this.addDynamic(url, IOUtils.toByteArray(new FileInputStream(file)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    public boolean add(String url, byte[] data) {
        synchronized (this) {
            try {
                if (!cacheMap.containsKey(url)) {
                    String id = DigestUtils.sha1Hex(url.getBytes());
                    File image = new File(getCache(), id);
                    FileUtils.writeByteArrayToFile(image, data);
                    Texture texture = new Texture(image);
                    cacheMap.put(url, texture);
                    Minecraft.getInstance().deferTask(texture::update); // ??? Previously addScheduledTask
                }
                return true;
            } catch(IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
    //*/

    public boolean addDynamic(String url, byte[] data) {
        synchronized (this) {
            System.out.println("addDynamic(\"" + url + "\")");
            try {
                if (!dynamicMap.containsKey(url)) {
                    String id = DigestUtils.sha1Hex(url.getBytes());
                    File image = new File(getCache(), id);
                    FileUtils.writeByteArrayToFile(image, data);
                    NativeImage nativeImage = NativeImage.read(new FileInputStream(image));
                    DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
                    dynamicMap.put(url, new DynamicPair(dynamicTexture, id));
                    Minecraft.getInstance().deferTask(dynamicTexture::updateDynamicTexture);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private void tick() {
        synchronized (this) {
            cacheMap.values().forEach(Texture::update);
            dynamicMap.values().forEach((pair) -> pair.texture.updateDynamicTexture());
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            this.tick();
    }

    public boolean loadCached(String url) {
        synchronized (this) {
            if (dynamicMap.containsKey(url))
                return true;
        }

        String id = DigestUtils.sha1Hex(url.getBytes());
        File file = new File(getCache(), id);
        if (file.exists()) {
            this.add(url, file);
            return true;
        }
        return false;
    }

    public boolean isCached(String url) {
        synchronized (this) {
            return dynamicMap.containsKey(url);
        }
    }

    public File getCache() {
        cache.mkdir();
        return cache;
    }
}
