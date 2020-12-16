package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.client.ImageCache;
import com.mrcrayfish.furniture.client.ImageDownloadThread;
import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameTileEntity extends TileEntity {
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


        if (getWorld() == null)
            System.out.println("Writing PhotoFrameTileEntity (null)");
        else
            System.out.println("Writing PhotoFrameTileEntity (" + (getWorld().isRemote? "remote" : "local") + ")");

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

        if (getWorld() == null)
            System.out.println("Reading PhotoFrameTileEntity (null)");
        else
            System.out.println("Reading PhotoFrameTileEntity (" + (getWorld().isRemote? "remote" : "local") + ")");

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
        return this.url;
    }

    @OnlyIn(Dist.CLIENT)
    public void loadUrl(String url) {
        System.out.println("Hello from loadUrl(" + url + ").");
        if (loading) {
            System.out.println("Already loading.");
            return;
        }

        this.loaded = false;
        this.result = null;
        if (!ImageCache.INSTANCE.loadCached(url)) {
            this.loading = true;
            System.out.println("Starting to load...");
            new ImageDownloadThread(url, (result, message) -> {
                System.out.println("Finished loading: " + message);
                this.loading = false;
                this.result = result;
                if (result == ImageDownloadThread.ImageDownloadResult.SUCCESS) {
                    System.out.println("Loaded.");
                    this.loaded = true;
                } else {
                    System.out.println("Not loaded.");
                }
            }).start();
        } else {
            System.out.println("Already cached?");
            this.loaded = true;
        }
    }

    public void setUrl(String url_) {
        this.url = url_;
        TileEntityUtil.sendUpdatePacket(this);
    }

    public String getUrl() {
        return this.url;
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

    public boolean isStretched() {
        return stretch;
    }

    public void setStretched(boolean stretch_) {
        this.stretch = stretch_;
        TileEntityUtil.sendUpdatePacket(this);
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

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.read(this.getBlockState(), packet.getNbtCompound());
    }
}
