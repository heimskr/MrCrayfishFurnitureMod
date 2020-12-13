package com.mrcrayfish.furniture.tileentity;

import com.google.common.collect.Lists;
import com.mrcrayfish.furniture.client.ImageCache;
import com.mrcrayfish.furniture.client.ImageDownloadThread;
import com.mrcrayfish.furniture.core.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameTileEntity extends TileEntity implements IValueContainer {
    private int color = 0;
    private String url;
    private boolean stretch;
    private boolean disabled;

    @OnlyIn(Dist.CLIENT)
    private boolean loading;

    @OnlyIn(Dist.CLIENT)
    private boolean loaded;

    @OnlyIn(Dist.CLIENT)
    private ImageDownloadThread.ImageDownloadResult result;

    public PhotoFrameTileEntity() {
        super(ModTileEntities.PHOTO_FRAME);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (this.url != null)
            compound.putString("Photo", this.url);
        compound.putBoolean("Stretch", this.stretch);
        compound.putBoolean("DisableInteraction", this.disabled);
        compound.putInt("Color", color);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if (compound.contains("Photo", Constants.NBT.TAG_STRING))
            this.url = compound.getString("Photo");
        if (compound.contains("Stretch", Constants.NBT.TAG_BYTE))
            this.stretch = compound.getBoolean("Stretch");
        if (compound.contains("DisableInteraction", Constants.NBT.TAG_BYTE))
            this.disabled = compound.getBoolean("DisableInteraction");
        if (compound.contains("Color", Constants.NBT.TAG_INT))
            this.color = compound.getInt("Color");
        if (world != null && world.isRemote && url != null)
            this.loadUrl(url);
    }

    @Nullable
    public String getPhoto() {
        return url;
    }

    @OnlyIn(Dist.CLIENT)
    public void loadUrl(String url) {
        if (loading)
            return;

        this.loaded = false;
        this.result = null;
        if (!ImageCache.INSTANCE.loadCached(url)) {
            this.loading = true;
            new ImageDownloadThread(url, (result, message) -> {
                this.loading = false;
                this.result = result;
                if (result == ImageDownloadThread.ImageDownloadResult.SUCCESS)
                    this.loaded = true;
            }).start();
        } else
            this.loaded = true;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isLoading() {
        return loading;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isLoaded() {
        return loaded && !loading;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public ImageDownloadThread.ImageDownloadResult getResult() {
        return result;
    }

    @Override
    public List<IValueContainer.Entry> getEntries() {
        List<IValueContainer.Entry> entries = Lists.newArrayList();
        entries.add(new IValueContainer.Entry("photo", "Photo URL", Entry.Type.TEXT_FIELD, this.url));
        entries.add(new IValueContainer.Entry("stretch", "Stretch to Border", Entry.Type.TOGGLE, this.stretch));
        return entries;
    }

    @Override
    public void updateEntries(Map<String, String> entries) {
        this.url = entries.get("photo");
        this.stretch = Boolean.valueOf(entries.get("stretch"));
        this.markDirty();
    }

    @Override
    public boolean requiresTool() {
        return false;
    }

    public boolean isStretched() {
        return stretch;
    }

    @Override
    public BlockPos getContainerPos() {
        return this.pos;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setColor(int color) {
        this.color = 15 - color;
    }

    public int getColor() {
        return color;
    }
}
