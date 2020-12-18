package com.mrcrayfish.furniture.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public interface IAuthored {
    Item getSignedItem(ItemStack stack);
}
