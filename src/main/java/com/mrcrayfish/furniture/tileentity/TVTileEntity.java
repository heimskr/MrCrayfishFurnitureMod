package com.mrcrayfish.furniture.tileentity;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mrcrayfish.furniture.block.AbstractTVBlock;
import com.mrcrayfish.furniture.client.GifCache;
import com.mrcrayfish.furniture.client.GifDownloadThread;
import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class TVTileEntity extends SyncClientTileEntity implements IValueContainer {
    private int width;
    private int height;
    private double screenYOffset;
    private double screenZOffset;
    private boolean stretch;
    private boolean powered;
    private boolean disabled;

    private List<String> channels = new ArrayList<>();
    private int currentChannel;

    @OnlyIn(Dist.CLIENT)
    private boolean loading;

    @OnlyIn(Dist.CLIENT)
    private boolean loaded;

    @OnlyIn(Dist.CLIENT)
    private GifDownloadThread.ImageDownloadResult result;

    public TVTileEntity() {
        super(ModTileEntities.TV);
    }

    public TVTileEntity(int width, int height, double screenYOffset, double screenZOffset) {
        super(ModTileEntities.TV);
        this.width = width;
        this.height = height;
        this.screenYOffset = screenYOffset;
        this.screenZOffset = screenZOffset;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ListNBT channelList = new ListNBT();
        channels.forEach(url -> channelList.add(StringNBT.valueOf(url)));
        compound.put("Channels", channelList);
        compound.putInt("CurrentChannel", this.currentChannel);
        compound.putBoolean("Stretch", this.stretch);
        compound.putBoolean("Powered", this.powered);
        compound.putBoolean("DisableInteraction", this.disabled);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.channels.clear();
        if (compound.contains("Channels", Constants.NBT.TAG_LIST)) {
            ListNBT channelList = compound.getList("Channels", Constants.NBT.TAG_STRING);
            channelList.forEach(nbtBase -> {
                if (nbtBase instanceof StringNBT)
                    channels.add(((StringNBT) nbtBase).getString());
            });
        } else if (compound.contains("URL", Constants.NBT.TAG_STRING))
            this.channels.add(compound.getString("URL"));
        if (compound.contains("CurrentChannel", Constants.NBT.TAG_INT))
            this.currentChannel = compound.getInt("CurrentChannel");
        if (compound.contains("Stretch", Constants.NBT.TAG_BYTE))
            this.stretch = compound.getBoolean("Stretch");
        if (compound.contains("Powered", Constants.NBT.TAG_BYTE))
            this.powered = compound.getBoolean("Powered");
        if (compound.contains("DisableInteraction", Constants.NBT.TAG_BYTE))
            this.disabled = compound.getBoolean("DisableInteraction");
        if (world != null && world.isRemote && powered && channels.size() > 0 && currentChannel >= 0 && currentChannel < channels.size())
            this.loadUrl(channels.get(currentChannel));
    }

    @Nullable
    public String getCurrentChannel() {
        if(channels.size() > 0 && currentChannel >= 0 && currentChannel < channels.size())
            return channels.get(currentChannel);
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void loadUrl(String url) {
        if(loading)
            return;

        this.loaded = false;
        this.result = null;
        if (!GifCache.INSTANCE.loadCached(url)) {
            this.loading = true;
            new GifDownloadThread(url, (result, message) -> {
                this.loading = false;
                this.result = result;
                if (result == GifDownloadThread.ImageDownloadResult.SUCCESS)
                    this.loaded = true;
            }).start();
        } else {
            this.loaded = true;
        }
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
    public GifDownloadThread.ImageDownloadResult getResult() {
        return result;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getScreenYOffset() {
        return screenYOffset;
    }

    public double getScreenZOffset() {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof AbstractTVBlock)
            return ((AbstractTVBlock) state.getBlock()).getScreenZOffset(state);
        return screenZOffset;
    }

    @Override
    public List<Entry> getEntries() {
        List<Entry> entries = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            String url = "";
            if (channels.size() > 0 && i >= 0 && i < channels.size())
                url = channels.get(i);
            entries.add(new Entry("channel_" + i, "Channel #" + (i + 1), Entry.Type.TEXT_FIELD, url));
        }
        entries.add(new Entry("stretch", "Stretch to Screen", Entry.Type.TOGGLE, this.stretch));
        entries.add(new Entry("powered", "Powered", Entry.Type.TOGGLE, this.powered));
        return entries;
    }

    @Override
    public void updateEntries(Map<String, String> entries) {
        channels.clear();
        for (int i = 0; i < 3; i++) {
            String url = entries.get("channel_" + i);
            if (!Strings.isNullOrEmpty(url))
                channels.add(url);
        }
        this.stretch = Boolean.valueOf(entries.get("stretch"));
        this.powered = Boolean.valueOf(entries.get("powered"));
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

    public void setPowered(boolean powered) {
        if (!disabled) {
            this.powered = powered;
//            TileEntityUtil.syncToClient(this);
            TileEntityUtil.sendUpdatePacket(this);
        }
    }

    public boolean isPowered() {
        return powered;
    }

    public boolean nextChannel() {
        if (!disabled && powered && channels.size() > 1) {
            this.currentChannel++;
            if (this.currentChannel >= channels.size())
                this.currentChannel = 0;
//            TileEntityUtil.syncToClient(this);
            TileEntityUtil.sendUpdatePacket(this);
            return true;
        }
        return false;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
