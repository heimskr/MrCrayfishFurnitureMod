package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.tileentity.*;
import com.mrcrayfish.furniture.util.Names;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntities {
    private static final List<TileEntityType> TILE_ENTITY_TYPES = new ArrayList<>();

    public static final TileEntityType<CabinetTileEntity> CABINET = buildType(Names.TileEntity.CABINET, TileEntityType.Builder.create(CabinetTileEntity::new, ModBlocks.CABINET_OAK, ModBlocks.CABINET_SPRUCE, ModBlocks.CABINET_BIRCH, ModBlocks.CABINET_JUNGLE, ModBlocks.CABINET_ACACIA, ModBlocks.CABINET_DARK_OAK, ModBlocks.CABINET_STONE, ModBlocks.CABINET_GRANITE, ModBlocks.CABINET_DIORITE, ModBlocks.CABINET_ANDESITE, ModBlocks.CABINET_STRIPPED_OAK, ModBlocks.CABINET_STRIPPED_SPRUCE, ModBlocks.CABINET_STRIPPED_BIRCH, ModBlocks.CABINET_STRIPPED_JUNGLE, ModBlocks.CABINET_STRIPPED_ACACIA, ModBlocks.CABINET_STRIPPED_DARK_OAK));
    public static final TileEntityType<BedsideCabinetTileEntity> BEDSIDE_CABINET = buildType(Names.TileEntity.BEDSIDE_CABINET, TileEntityType.Builder.create(BedsideCabinetTileEntity::new, ModBlocks.BEDSIDE_CABINET_OAK, ModBlocks.BEDSIDE_CABINET_SPRUCE, ModBlocks.BEDSIDE_CABINET_BIRCH, ModBlocks.BEDSIDE_CABINET_JUNGLE, ModBlocks.BEDSIDE_CABINET_ACACIA, ModBlocks.BEDSIDE_CABINET_DARK_OAK, ModBlocks.BEDSIDE_CABINET_STONE, ModBlocks.BEDSIDE_CABINET_GRANITE, ModBlocks.BEDSIDE_CABINET_DIORITE, ModBlocks.BEDSIDE_CABINET_ANDESITE, ModBlocks.BEDSIDE_CABINET_STRIPPED_OAK, ModBlocks.BEDSIDE_CABINET_STRIPPED_SPRUCE, ModBlocks.BEDSIDE_CABINET_STRIPPED_BIRCH, ModBlocks.BEDSIDE_CABINET_STRIPPED_JUNGLE, ModBlocks.BEDSIDE_CABINET_STRIPPED_ACACIA, ModBlocks.BEDSIDE_CABINET_STRIPPED_DARK_OAK));
    public static final TileEntityType<DeskCabinetTileEntity> DESK_CABINET = buildType(Names.TileEntity.DESK_CABINET, TileEntityType.Builder.create(DeskCabinetTileEntity::new, ModBlocks.DESK_CABINET_OAK, ModBlocks.DESK_CABINET_SPRUCE, ModBlocks.DESK_CABINET_BIRCH, ModBlocks.DESK_CABINET_JUNGLE, ModBlocks.DESK_CABINET_ACACIA, ModBlocks.DESK_CABINET_DARK_OAK, ModBlocks.DESK_CABINET_STONE, ModBlocks.DESK_CABINET_GRANITE, ModBlocks.DESK_CABINET_DIORITE, ModBlocks.DESK_CABINET_ANDESITE, ModBlocks.DESK_CABINET_STRIPPED_OAK, ModBlocks.DESK_CABINET_STRIPPED_SPRUCE, ModBlocks.DESK_CABINET_STRIPPED_BIRCH, ModBlocks.DESK_CABINET_STRIPPED_JUNGLE, ModBlocks.DESK_CABINET_STRIPPED_ACACIA, ModBlocks.DESK_CABINET_STRIPPED_DARK_OAK));
    public static final TileEntityType<CrateTileEntity> CRATE = buildType(Names.TileEntity.CRATE, TileEntityType.Builder.create(CrateTileEntity::new, ModBlocks.CRATE_OAK, ModBlocks.CRATE_SPRUCE, ModBlocks.CRATE_BIRCH, ModBlocks.CRATE_JUNGLE, ModBlocks.CRATE_ACACIA, ModBlocks.CRATE_DARK_OAK));
    public static final TileEntityType<MailBoxTileEntity> MAIL_BOX = buildType(Names.TileEntity.MAIL_BOX, TileEntityType.Builder.create(MailBoxTileEntity::new, ModBlocks.MAIL_BOX_OAK, ModBlocks.MAIL_BOX_SPRUCE, ModBlocks.MAIL_BOX_BIRCH, ModBlocks.MAIL_BOX_JUNGLE, ModBlocks.MAIL_BOX_ACACIA, ModBlocks.MAIL_BOX_DARK_OAK, ModBlocks.MAIL_BOX_STRIPPED_OAK, ModBlocks.MAIL_BOX_STRIPPED_SPRUCE, ModBlocks.MAIL_BOX_STRIPPED_BIRCH, ModBlocks.MAIL_BOX_STRIPPED_JUNGLE, ModBlocks.MAIL_BOX_STRIPPED_ACACIA, ModBlocks.MAIL_BOX_STRIPPED_DARK_OAK));
    public static final TileEntityType<TrampolineTileEntity> TRAMPOLINE = buildType(Names.TileEntity.TRAMPOLINE, TileEntityType.Builder.create(TrampolineTileEntity::new, ModBlocks.TRAMPOLINE_WHITE, ModBlocks.TRAMPOLINE_ORANGE, ModBlocks.TRAMPOLINE_MAGENTA, ModBlocks.TRAMPOLINE_LIGHT_BLUE, ModBlocks.TRAMPOLINE_YELLOW, ModBlocks.TRAMPOLINE_LIME, ModBlocks.TRAMPOLINE_PINK, ModBlocks.TRAMPOLINE_GRAY, ModBlocks.TRAMPOLINE_LIGHT_GRAY, ModBlocks.TRAMPOLINE_CYAN, ModBlocks.TRAMPOLINE_PURPLE, ModBlocks.TRAMPOLINE_BLUE, ModBlocks.TRAMPOLINE_BROWN, ModBlocks.TRAMPOLINE_GREEN, ModBlocks.TRAMPOLINE_RED, ModBlocks.TRAMPOLINE_BLACK));
    public static final TileEntityType<CoolerTileEntity> COOLER = buildType(Names.TileEntity.COOLER, TileEntityType.Builder.create(CoolerTileEntity::new, ModBlocks.COOLER_WHITE, ModBlocks.COOLER_ORANGE, ModBlocks.COOLER_MAGENTA, ModBlocks.COOLER_LIGHT_BLUE, ModBlocks.COOLER_YELLOW, ModBlocks.COOLER_LIME, ModBlocks.COOLER_PINK, ModBlocks.COOLER_GRAY, ModBlocks.COOLER_LIGHT_GRAY, ModBlocks.COOLER_CYAN, ModBlocks.COOLER_PURPLE, ModBlocks.COOLER_BLUE, ModBlocks.COOLER_BROWN, ModBlocks.COOLER_GREEN, ModBlocks.COOLER_RED, ModBlocks.COOLER_BLACK));
    public static final TileEntityType<GrillTileEntity> GRILL = buildType(Names.TileEntity.GRILL, TileEntityType.Builder.create(GrillTileEntity::new, ModBlocks.GRILL_WHITE, ModBlocks.GRILL_ORANGE, ModBlocks.GRILL_MAGENTA, ModBlocks.GRILL_LIGHT_BLUE, ModBlocks.GRILL_YELLOW, ModBlocks.GRILL_LIME, ModBlocks.GRILL_PINK, ModBlocks.GRILL_GRAY, ModBlocks.GRILL_LIGHT_GRAY, ModBlocks.GRILL_CYAN, ModBlocks.GRILL_PURPLE, ModBlocks.GRILL_BLUE, ModBlocks.GRILL_BROWN, ModBlocks.GRILL_GREEN, ModBlocks.GRILL_RED, ModBlocks.GRILL_BLACK));
    public static final TileEntityType<DoorMatTileEntity> DOOR_MAT = buildType(Names.TileEntity.DOOR_MAT, TileEntityType.Builder.create(DoorMatTileEntity::new, ModBlocks.DOOR_MAT));
    public static final TileEntityType<KitchenDrawerTileEntity> KITCHEN_DRAWER = buildType(Names.TileEntity.KITCHEN_DRAWER, TileEntityType.Builder.create(KitchenDrawerTileEntity::new, ModBlocks.KITCHEN_DRAWER_OAK, ModBlocks.KITCHEN_DRAWER_SPRUCE, ModBlocks.KITCHEN_DRAWER_BIRCH, ModBlocks.KITCHEN_DRAWER_JUNGLE, ModBlocks.KITCHEN_DRAWER_ACACIA, ModBlocks.KITCHEN_DRAWER_DARK_OAK, ModBlocks.KITCHEN_DRAWER_STRIPPED_OAK, ModBlocks.KITCHEN_DRAWER_STRIPPED_SPRUCE, ModBlocks.KITCHEN_DRAWER_STRIPPED_BIRCH, ModBlocks.KITCHEN_DRAWER_STRIPPED_JUNGLE, ModBlocks.KITCHEN_DRAWER_STRIPPED_ACACIA, ModBlocks.KITCHEN_DRAWER_STRIPPED_DARK_OAK, ModBlocks.KITCHEN_DRAWER_WHITE, ModBlocks.KITCHEN_DRAWER_ORANGE, ModBlocks.KITCHEN_DRAWER_MAGENTA, ModBlocks.KITCHEN_DRAWER_LIGHT_BLUE, ModBlocks.KITCHEN_DRAWER_YELLOW, ModBlocks.KITCHEN_DRAWER_LIME, ModBlocks.KITCHEN_DRAWER_PINK, ModBlocks.KITCHEN_DRAWER_GRAY, ModBlocks.KITCHEN_DRAWER_LIGHT_GRAY, ModBlocks.KITCHEN_DRAWER_CYAN, ModBlocks.KITCHEN_DRAWER_PURPLE, ModBlocks.KITCHEN_DRAWER_BLUE, ModBlocks.KITCHEN_DRAWER_BROWN, ModBlocks.KITCHEN_DRAWER_GREEN, ModBlocks.KITCHEN_DRAWER_RED, ModBlocks.KITCHEN_DRAWER_BLACK));
    public static final TileEntityType<KitchenSinkTileEntity> KITCHEN_SINK = buildType(Names.TileEntity.KITCHEN_SINK, TileEntityType.Builder.create(KitchenSinkTileEntity::new, ModBlocks.KITCHEN_SINK_LIGHT_OAK, ModBlocks.KITCHEN_SINK_LIGHT_SPRUCE, ModBlocks.KITCHEN_SINK_LIGHT_BIRCH, ModBlocks.KITCHEN_SINK_LIGHT_JUNGLE, ModBlocks.KITCHEN_SINK_LIGHT_ACACIA, ModBlocks.KITCHEN_SINK_LIGHT_DARK_OAK, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_OAK, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_SPRUCE, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_BIRCH, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_JUNGLE, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_ACACIA, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_DARK_OAK, ModBlocks.KITCHEN_SINK_DARK_OAK, ModBlocks.KITCHEN_SINK_DARK_SPRUCE, ModBlocks.KITCHEN_SINK_DARK_BIRCH, ModBlocks.KITCHEN_SINK_DARK_JUNGLE, ModBlocks.KITCHEN_SINK_DARK_ACACIA, ModBlocks.KITCHEN_SINK_DARK_DARK_OAK, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_OAK, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_SPRUCE, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_BIRCH, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_JUNGLE, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_ACACIA, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_DARK_OAK, ModBlocks.KITCHEN_SINK_WHITE, ModBlocks.KITCHEN_SINK_ORANGE, ModBlocks.KITCHEN_SINK_MAGENTA, ModBlocks.KITCHEN_SINK_LIGHT_BLUE, ModBlocks.KITCHEN_SINK_YELLOW, ModBlocks.KITCHEN_SINK_LIME, ModBlocks.KITCHEN_SINK_PINK, ModBlocks.KITCHEN_SINK_GRAY, ModBlocks.KITCHEN_SINK_LIGHT_GRAY, ModBlocks.KITCHEN_SINK_CYAN, ModBlocks.KITCHEN_SINK_PURPLE, ModBlocks.KITCHEN_SINK_BLUE, ModBlocks.KITCHEN_SINK_BROWN, ModBlocks.KITCHEN_SINK_GREEN, ModBlocks.KITCHEN_SINK_RED, ModBlocks.KITCHEN_SINK_BLACK));
    public static final TileEntityType<FridgeTileEntity> FRIDGE = buildType(Names.TileEntity.FRIDGE, TileEntityType.Builder.create(FridgeTileEntity::new, ModBlocks.FRIDGE_LIGHT, ModBlocks.FRIDGE_DARK));
    public static final TileEntityType<FreezerTileEntity> FREEZER = buildType(Names.TileEntity.FREEZER, TileEntityType.Builder.create(FreezerTileEntity::new, ModBlocks.FREEZER_LIGHT, ModBlocks.FREEZER_DARK));
    public static final TileEntityType<TreeTileEntity> TREE = buildType(Names.TileEntity.TREE, TileEntityType.Builder.create(TreeTileEntity::new, ModBlocks.TREE));
    public static final TileEntityType<PhotoFrameTileEntity> PHOTO_FRAME = buildType(Names.TileEntity.PHOTO_FRAME, TileEntityType.Builder.create(PhotoFrameTileEntity::new, ModBlocks.PHOTO_FRAME));
    public static final TileEntityType<PresentTileEntity> PRESENT = buildType(Names.TileEntity.PRESENT, TileEntityType.Builder.create(PresentTileEntity::new, ModBlocks.PRESENT_WHITE, ModBlocks.PRESENT_ORANGE, ModBlocks.PRESENT_MAGENTA, ModBlocks.PRESENT_LIGHT_BLUE, ModBlocks.PRESENT_YELLOW, ModBlocks.PRESENT_LIME, ModBlocks.PRESENT_PINK, ModBlocks.PRESENT_GRAY, ModBlocks.PRESENT_LIGHT_GRAY, ModBlocks.PRESENT_CYAN, ModBlocks.PRESENT_PURPLE, ModBlocks.PRESENT_BLUE, ModBlocks.PRESENT_BROWN, ModBlocks.PRESENT_GREEN, ModBlocks.PRESENT_RED, ModBlocks.PRESENT_BLACK));
    public static final TileEntityType<TVTileEntity> TV = buildType(Names.TileEntity.TV, TileEntityType.Builder.create(TVTileEntity::new, ModBlocks.MODERN_TV));
    public static final TileEntityType<ToasterTileEntity> TOASTER = buildType(Names.TileEntity.TOASTER, TileEntityType.Builder.create(ToasterTileEntity::new, ModBlocks.TOASTER));
    public static final TileEntityType<ChoppingBoardTileEntity> CHOPPING_BOARD = buildType(Names.TileEntity.CHOPPING_BOARD, TileEntityType.Builder.create(ChoppingBoardTileEntity::new, ModBlocks.CHOPPING_BOARD));
    public static final TileEntityType<ComputerTileEntity> COMPUTER = buildType(Names.TileEntity.COMPUTER, TileEntityType.Builder.create(ComputerTileEntity::new, ModBlocks.COMPUTER));
    public static final TileEntityType<BathTileEntity> BATH = buildType(Names.TileEntity.BATH, TileEntityType.Builder.create(BathTileEntity::new, ModBlocks.BATH));

    private static <T extends TileEntity> TileEntityType<T> buildType(String id, TileEntityType.Builder<T> builder) {
        TileEntityType<T> type = builder.build(null); //TODO may not allow null
        type.setRegistryName(id);
        TILE_ENTITY_TYPES.add(type);
        return type;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerTypes(final RegistryEvent.Register<TileEntityType<?>> event) {
        TILE_ENTITY_TYPES.forEach(type -> event.getRegistry().register(type));
        TILE_ENTITY_TYPES.clear();
    }
}
