package com.mrcrayfish.furniture.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

/**
 * Author: Kai Tamkun
 */
public class PlayerUtil {
    public static void sendTranslatedMessage(PlayerEntity player, String id) {
        player.sendMessage(new TranslationTextComponent(id), UUID.fromString("75e44e9f-d37f-4ff3-8683-2b4d89e4b70c"));
    }

    public static void sendMessage(PlayerEntity player, TranslationTextComponent ttc) {
        player.sendMessage(ttc, UUID.fromString("75e44e9f-d37f-4ff3-8683-2b4d89e4b70c"));
    }
}
