package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.util.Names;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    private static final List<Item> ITEMS = new ArrayList<>();

    public static final Item SPATULA = register(Names.Item.SPATULA, new SwordItem(ItemTier.IRON, 3, -1.4F, new Item.Properties().group(FurnitureMod.GROUP)));
    public static final Item KNIFE = register(Names.Item.KNIFE, new SwordItem(ItemTier.IRON, 3, -1.4F, new Item.Properties().group(FurnitureMod.GROUP)));
    public static final Item BREAD_SLICE = register(Names.Item.BREAD_SLICE, new Item((new Item.Properties()).group(FurnitureMod.GROUP).food((new Food.Builder()).hunger(5).saturation(0.6F).build())));
    public static final Item TOAST = register(Names.Item.TOAST, new Item((new Item.Properties()).group(FurnitureMod.GROUP).food((new Food.Builder()).hunger(7).saturation(1.0F).build())));

    private static Item register(String name, Item item) {
        item.setRegistryName(name);
        ITEMS.add(item);
        return item;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        ITEMS.forEach(item -> event.getRegistry().register(item));
        ITEMS.clear();
    }
}
