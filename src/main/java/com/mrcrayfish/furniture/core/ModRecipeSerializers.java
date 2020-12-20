package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.item.crafting.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeSerializers {
    private static final List<IRecipeSerializer> RECIPES = new ArrayList<>();

    public static final CookingRecipeSerializer<GrillCookingRecipe> GRILL_COOKING = register("cfm:grill_cooking", new CookingRecipeSerializer<>(GrillCookingRecipe::new, 100));
    public static final CookingRecipeSerializer<FreezerSolidifyRecipe> FREEZER_SOLIDIFY = register("cfm:freezer_solidify", new CookingRecipeSerializer<>(FreezerSolidifyRecipe::new, 100));
    public static final CookingRecipeSerializer<ToasterCookingRecipe> TOASTER_COOKING = register("cfm:toaster_cooking", new CookingRecipeSerializer<>(ToasterCookingRecipe::new, 100));
    public static final CookingRecipeSerializer<ChoppingBoardRecipe> CHOPPING_BOARD = register("cfm:chopping_board_chop", new CookingRecipeSerializer<>(ChoppingBoardRecipe::new, 0));
    // Everything's a cooking recipe if you ~Believe~ hard enough. Everything.
    public static final MineBayRecipeSerializer MINEBAY = register("cfm:minebay", new MineBayRecipeSerializer());

    private static <T extends IRecipeSerializer<? extends IRecipe<?>>> T register(String name, T t) {
        t.setRegistryName(new ResourceLocation(name));
        RECIPES.add(t);
        return t;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerItems(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        RECIPES.forEach(item -> event.getRegistry().register(item));
        RECIPES.clear();
    }
}
