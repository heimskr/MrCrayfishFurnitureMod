package com.mrcrayfish.furniture.proxy;

import com.mrcrayfish.furniture.client.GifCache;
import com.mrcrayfish.furniture.client.ImageCache;
import com.mrcrayfish.furniture.client.MailBoxEntry;
import com.mrcrayfish.furniture.client.event.CreativeScreenEvents;
import com.mrcrayfish.furniture.client.gui.screen.DoorMatScreen;
import com.mrcrayfish.furniture.client.gui.screen.EditValueContainerScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.*;
import com.mrcrayfish.furniture.client.gui.screen.PhotoFrameScreen;
import com.mrcrayfish.furniture.client.renderer.SeatRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.*;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.core.ModEntities;
import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.inventory.container.PresentContainer;
import com.mrcrayfish.furniture.tileentity.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Author: MrCrayfish
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void onSetupClient() {
        super.onSetupClient();

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.GRILL, GrillTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.DOOR_MAT, DoorMatTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.KITCHEN_SINK, KitchenSinkTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TREE, TreeTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.PHOTO_FRAME, PhotoFrameTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TV, TVTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TOASTER, ToastRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SEAT, SeatRenderer::new);

        ScreenManager.registerFactory(ModContainers.CRATE, CrateScreen::new);
        ScreenManager.registerFactory(ModContainers.POST_BOX, PostBoxScreen::new);
        ScreenManager.registerFactory(ModContainers.MAIL_BOX, MailBoxScreen::new);
        ScreenManager.registerFactory(ModContainers.FREEZER, FreezerScreen::new);
        //ScreenManager.registerFactory(ModContainers.PHOTO_FRAME, PhotoFrameScreen::new);
        ScreenManager.registerFactory(ModContainers.PRESENT, PresentScreen::new);

        Predicate<RenderType> leavesPredicate = renderType -> this.useFancyGraphics() ? renderType == RenderType.getCutoutMipped() : renderType == RenderType.getSolid();
        RenderTypeLookup.setRenderLayer(ModBlocks.HEDGE_OAK, leavesPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.HEDGE_SPRUCE, leavesPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.HEDGE_BIRCH, leavesPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.HEDGE_JUNGLE, leavesPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.HEDGE_ACACIA, leavesPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.HEDGE_DARK_OAK, leavesPredicate);

        Predicate<RenderType> cutoutPredicate = renderType -> renderType == RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_WHITE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_ORANGE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_MAGENTA, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_LIGHT_BLUE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_YELLOW, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_LIME, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_PINK, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_GRAY, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_LIGHT_GRAY, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_CYAN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_PURPLE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_BLUE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_BROWN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_GREEN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_RED, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAMPOLINE_BLACK, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_WHITE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_ORANGE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_MAGENTA, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_LIGHT_BLUE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_YELLOW, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_LIME, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_PINK, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_GRAY, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_LIGHT_GRAY, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_CYAN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_PURPLE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_BLUE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_BROWN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_GREEN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_RED, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.GRILL_BLACK, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.POST_BOX, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.TREE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PHOTO_FRAME, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.WREATH, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_WHITE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_ORANGE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_MAGENTA, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_LIGHT_BLUE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_YELLOW, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_LIME, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_PINK, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_GRAY, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_LIGHT_GRAY, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_CYAN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_PURPLE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_BLUE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_BROWN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_GREEN, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_RED, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.PRESENT_BLACK, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.MANTELPIECE, cutoutPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.MODERN_TV, cutoutPredicate);

        RenderTypeLookup.setRenderLayer(ModBlocks.TREE, leavesPredicate);
        RenderTypeLookup.setRenderLayer(ModBlocks.WREATH, leavesPredicate);

        Predicate<RenderType> translucentPredicate = renderType -> renderType == RenderType.getTranslucent();
        RenderTypeLookup.setRenderLayer(ModBlocks.FAIRY_LIGHT, translucentPredicate);

        this.registerColors();

        MinecraftForge.EVENT_BUS.register(ImageCache.INSTANCE);
        MinecraftForge.EVENT_BUS.register(GifCache.INSTANCE);

        if (!ModList.get().isLoaded("filters"))
            MinecraftForge.EVENT_BUS.register(new CreativeScreenEvents());
    }

    private void registerColors() {
        Minecraft.getInstance().getBlockColors().register((state, reader, pos, i) -> i == 1 ? 0xCCCCCC : 0,
            ModBlocks.PICKET_FENCE_WHITE,
            ModBlocks.PICKET_FENCE_ORANGE,
            ModBlocks.PICKET_FENCE_MAGENTA,
            ModBlocks.PICKET_FENCE_LIGHT_BLUE,
            ModBlocks.PICKET_FENCE_YELLOW,
            ModBlocks.PICKET_FENCE_LIME,
            ModBlocks.PICKET_FENCE_PINK,
            ModBlocks.PICKET_FENCE_GRAY,
            ModBlocks.PICKET_FENCE_LIGHT_GRAY,
            ModBlocks.PICKET_FENCE_CYAN,
            ModBlocks.PICKET_FENCE_PURPLE,
            ModBlocks.PICKET_FENCE_BLUE,
            ModBlocks.PICKET_FENCE_BROWN,
            ModBlocks.PICKET_FENCE_GREEN,
            ModBlocks.PICKET_FENCE_RED,
            ModBlocks.PICKET_FENCE_BLACK,
            ModBlocks.PICKET_GATE_WHITE,
            ModBlocks.PICKET_GATE_ORANGE,
            ModBlocks.PICKET_GATE_MAGENTA,
            ModBlocks.PICKET_GATE_LIGHT_BLUE,
            ModBlocks.PICKET_GATE_YELLOW,
            ModBlocks.PICKET_GATE_LIME,
            ModBlocks.PICKET_GATE_PINK,
            ModBlocks.PICKET_GATE_GRAY,
            ModBlocks.PICKET_GATE_LIGHT_GRAY,
            ModBlocks.PICKET_GATE_CYAN,
            ModBlocks.PICKET_GATE_PURPLE,
            ModBlocks.PICKET_GATE_BLUE,
            ModBlocks.PICKET_GATE_BROWN,
            ModBlocks.PICKET_GATE_GREEN,
            ModBlocks.PICKET_GATE_RED,
            ModBlocks.PICKET_GATE_BLACK,
            ModBlocks.POST_BOX
        );

        Minecraft.getInstance().getItemColors().register((stack, i) -> i == 1 ? 0xCCCCCC : 0,
            ModBlocks.PICKET_FENCE_WHITE,
            ModBlocks.PICKET_FENCE_ORANGE,
            ModBlocks.PICKET_FENCE_MAGENTA,
            ModBlocks.PICKET_FENCE_LIGHT_BLUE,
            ModBlocks.PICKET_FENCE_YELLOW,
            ModBlocks.PICKET_FENCE_LIME,
            ModBlocks.PICKET_FENCE_PINK,
            ModBlocks.PICKET_FENCE_GRAY,
            ModBlocks.PICKET_FENCE_LIGHT_GRAY,
            ModBlocks.PICKET_FENCE_CYAN,
            ModBlocks.PICKET_FENCE_PURPLE,
            ModBlocks.PICKET_FENCE_BLUE,
            ModBlocks.PICKET_FENCE_BROWN,
            ModBlocks.PICKET_FENCE_GREEN,
            ModBlocks.PICKET_FENCE_RED,
            ModBlocks.PICKET_FENCE_BLACK,
            ModBlocks.PICKET_GATE_WHITE,
            ModBlocks.PICKET_GATE_ORANGE,
            ModBlocks.PICKET_GATE_MAGENTA,
            ModBlocks.PICKET_GATE_LIGHT_BLUE,
            ModBlocks.PICKET_GATE_YELLOW,
            ModBlocks.PICKET_GATE_LIME,
            ModBlocks.PICKET_GATE_PINK,
            ModBlocks.PICKET_GATE_GRAY,
            ModBlocks.PICKET_GATE_LIGHT_GRAY,
            ModBlocks.PICKET_GATE_CYAN,
            ModBlocks.PICKET_GATE_PURPLE,
            ModBlocks.PICKET_GATE_BLUE,
            ModBlocks.PICKET_GATE_BROWN,
            ModBlocks.PICKET_GATE_GREEN,
            ModBlocks.PICKET_GATE_RED,
            ModBlocks.PICKET_GATE_BLACK,
            ModBlocks.POST_BOX
        );

        Minecraft.getInstance().getBlockColors().register((state, reader, pos, i) -> i == 1 ? 0xBBBBBB : 0,
            ModBlocks.CRATE_STRIPPED_OAK,
            ModBlocks.CRATE_STRIPPED_SPRUCE,
            ModBlocks.CRATE_STRIPPED_BIRCH,
            ModBlocks.CRATE_STRIPPED_JUNGLE,
            ModBlocks.CRATE_STRIPPED_ACACIA,
            ModBlocks.CRATE_STRIPPED_DARK_OAK,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_OAK,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_SPRUCE,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_BIRCH,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_JUNGLE,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_ACACIA,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_DARK_OAK,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_OAK,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_SPRUCE,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_BIRCH,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_JUNGLE,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_ACACIA,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_DARK_OAK,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_OAK,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_SPRUCE,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_BIRCH,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_JUNGLE,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_ACACIA,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_DARK_OAK,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_OAK,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_SPRUCE,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_BIRCH,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_JUNGLE,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_ACACIA,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_DARK_OAK
        );

        Minecraft.getInstance().getItemColors().register((stack, i) -> i == 1 ? 0xBBBBBB : 0,
            ModBlocks.CRATE_STRIPPED_OAK,
            ModBlocks.CRATE_STRIPPED_SPRUCE,
            ModBlocks.CRATE_STRIPPED_BIRCH,
            ModBlocks.CRATE_STRIPPED_JUNGLE,
            ModBlocks.CRATE_STRIPPED_ACACIA,
            ModBlocks.CRATE_STRIPPED_DARK_OAK,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_OAK,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_SPRUCE,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_BIRCH,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_JUNGLE,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_ACACIA,
            ModBlocks.KITCHEN_COUNTER_STRIPPED_DARK_OAK,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_OAK,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_SPRUCE,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_BIRCH,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_JUNGLE,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_ACACIA,
            ModBlocks.KITCHEN_DRAWER_STRIPPED_DARK_OAK,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_OAK,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_SPRUCE,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_BIRCH,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_JUNGLE,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_ACACIA,
            ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_DARK_OAK,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_OAK,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_SPRUCE,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_BIRCH,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_JUNGLE,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_ACACIA,
            ModBlocks.KITCHEN_SINK_DARK_STRIPPED_DARK_OAK
        );

        Minecraft.getInstance().getBlockColors().register((state, reader, pos, i) -> i == 1 ? 0x999999 : 0,
            ModBlocks.PARK_BENCH_STRIPPED_OAK,
            ModBlocks.PARK_BENCH_STRIPPED_SPRUCE,
            ModBlocks.PARK_BENCH_STRIPPED_BIRCH,
            ModBlocks.PARK_BENCH_STRIPPED_JUNGLE,
            ModBlocks.PARK_BENCH_STRIPPED_ACACIA,
            ModBlocks.PARK_BENCH_STRIPPED_DARK_OAK
        );

        Minecraft.getInstance().getItemColors().register((stack, i) -> i == 1 ? 0x999999 : 0,
            ModBlocks.PARK_BENCH_STRIPPED_OAK,
            ModBlocks.PARK_BENCH_STRIPPED_SPRUCE,
            ModBlocks.PARK_BENCH_STRIPPED_BIRCH,
            ModBlocks.PARK_BENCH_STRIPPED_JUNGLE,
            ModBlocks.PARK_BENCH_STRIPPED_ACACIA,
            ModBlocks.PARK_BENCH_STRIPPED_DARK_OAK
        );

        Minecraft.getInstance().getBlockColors().register((state, reader, pos, i) -> i == 1 ? 0xCCCCCC : 0,
            ModBlocks.FRIDGE_LIGHT,
            ModBlocks.FREEZER_LIGHT,
            ModBlocks.FRIDGE_DARK,
            ModBlocks.FREEZER_DARK,
            ModBlocks.PHOTO_FRAME
        );

        Minecraft.getInstance().getItemColors().register((stack, i) -> i == 1 ? 0xCCCCCC : 0,
            ModBlocks.FRIDGE_LIGHT,
            ModBlocks.FREEZER_LIGHT,
            ModBlocks.FRIDGE_DARK,
            ModBlocks.FREEZER_DARK,
            ModBlocks.PHOTO_FRAME
        );

        Minecraft.getInstance().getBlockColors().register((state, reader, pos, i) -> FoliageColors.getSpruce(),
            ModBlocks.HEDGE_SPRUCE,
            ModBlocks.TREE,
            ModBlocks.WREATH);

        Minecraft.getInstance().getBlockColors().register((state, reader, pos, i) -> FoliageColors.getBirch(),
            ModBlocks.HEDGE_BIRCH);

        Minecraft.getInstance().getBlockColors().register((state, reader, pos, i) -> reader != null && pos != null ? BiomeColors.getFoliageColor(reader, pos) : FoliageColors.getDefault(),
            ModBlocks.HEDGE_OAK,
            ModBlocks.HEDGE_JUNGLE,
            ModBlocks.HEDGE_ACACIA,
            ModBlocks.HEDGE_DARK_OAK);

        Minecraft.getInstance().getItemColors().register((stack, i) -> Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, i),
            ModBlocks.HEDGE_OAK,
            ModBlocks.HEDGE_SPRUCE,
            ModBlocks.HEDGE_BIRCH,
            ModBlocks.HEDGE_JUNGLE,
            ModBlocks.HEDGE_ACACIA,
            ModBlocks.HEDGE_DARK_OAK,
            ModBlocks.TREE,
            ModBlocks.WREATH);
    }

    @Override
    public void updateMailBoxes(CompoundNBT compound) {
        if (Minecraft.getInstance().currentScreen instanceof PostBoxScreen) {
            if (compound.contains("MailBoxes", Constants.NBT.TAG_LIST)) {
                List<MailBoxEntry> entries = new ArrayList<>();
                ListNBT mailBoxList = compound.getList("MailBoxes", Constants.NBT.TAG_COMPOUND);
                mailBoxList.forEach(nbt -> entries.add(new MailBoxEntry((CompoundNBT) nbt)));
                ((PostBoxScreen) Minecraft.getInstance().currentScreen).updateMailBoxes(entries);
            }
        }
    }

    @Override
    public boolean useFancyGraphics() {
        Minecraft mc = Minecraft.getInstance();
        return mc.gameSettings.graphicFanciness.func_238162_a_() > 0;
    }

    @Override
    public void setGrillFlipping(BlockPos pos, int position) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.world != null) {
            TileEntity tileEntity = minecraft.world.getTileEntity(pos);
            if (tileEntity instanceof GrillTileEntity)
                ((GrillTileEntity) tileEntity).setFlipping(position);
        }
    }

    @Override
    public void showDoorMatScreen(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof DoorMatTileEntity)
            Minecraft.getInstance().displayGuiScreen(new DoorMatScreen((DoorMatTileEntity) tileEntity));
    }

    @Override
    public void showPhotoFrameScreen(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof PhotoFrameTileEntity)
            Minecraft.getInstance().displayGuiScreen(new PhotoFrameScreen((PhotoFrameTileEntity) tileEntity));
    }

    @Override
    public void showEditValueScreen(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IValueContainer)
            Minecraft.getInstance().displayGuiScreen(new EditValueContainerScreen((IValueContainer) tileEntity));
    }

    @Override
    public boolean isSinglePlayer() {
        return Minecraft.getInstance().isSingleplayer();
    }

    @Override
    public boolean isDedicatedServer() {
        return false;
    }
}
