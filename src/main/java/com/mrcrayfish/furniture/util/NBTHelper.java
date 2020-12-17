package com.mrcrayfish.furniture.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * NBTHelper
 *
 * @author pahimar
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class NBTHelper {
    /**
     * Initializes the NBT Tag Compound for the given ItemStack if it is null
     *
     * @param itemStack The ItemStack for which its NBT Tag Compound is being checked
     *                  for initialization
     */
    private static void initNBTTagCompound(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundNBT());
    }

    public static boolean hasTag(ItemStack itemStack, String keyName) {
        return itemStack.getTag() != null && itemStack.getTag().contains(keyName);
    }

    public static void removeTag(ItemStack itemStack, String keyName) {
        if (itemStack.getTag() != null)
            itemStack.getTag().remove(keyName);
    }

    public static String getString(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(keyName))
            setString(itemStack, keyName, "");
        return itemStack.getTag().getString(keyName);
    }

    public static void setString(ItemStack itemStack, String keyName, String keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().putString(keyName, keyValue);
    }

    public static boolean getBoolean(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(keyName))
            setBoolean(itemStack, keyName, false);
        return itemStack.getTag().getBoolean(keyName);
    }

    public static void setBoolean(ItemStack itemStack, String keyName, boolean keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().putBoolean(keyName, keyValue);
    }

    public static byte getByte(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(keyName))
            setByte(itemStack, keyName, (byte) 0);
        return itemStack.getTag().getByte(keyName);
    }

    public static void setByte(ItemStack itemStack, String keyName, byte keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().putByte(keyName, keyValue);
    }

    public static short getShort(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(keyName))
            setShort(itemStack, keyName, (short) 0);
        return itemStack.getTag().getShort(keyName);
    }

    public static void setShort(ItemStack itemStack, String keyName, short keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().putShort(keyName, keyValue);
    }

    public static int getInt(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(keyName))
            setInteger(itemStack, keyName, 0);
        return itemStack.getTag().getInt(keyName);
    }

    public static void setInteger(ItemStack itemStack, String keyName, int keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().putInt(keyName, keyValue);
    }

    public static long getLong(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(keyName))
            setLong(itemStack, keyName, 0);
        return itemStack.getTag().getLong(keyName);
    }

    public static void setLong(ItemStack itemStack, String keyName, long keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().putLong(keyName, keyValue);
    }

    public static float getFloat(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(keyName))
            setFloat(itemStack, keyName, 0);
        return itemStack.getTag().getFloat(keyName);
    }

    public static void setFloat(ItemStack itemStack, String keyName, float keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().putFloat(keyName, keyValue);
    }

    public static double getDouble(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(keyName))
            setDouble(itemStack, keyName, 0);
        return itemStack.getTag().getDouble(keyName);
    }

    public static void setDouble(ItemStack itemStack, String keyName, double keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().putDouble(keyName, keyValue);
    }

    public static CompoundNBT getCompoundTag(ItemStack itemStack, String tagName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTag().contains(tagName))
            itemStack.getTag().put(tagName, new CompoundNBT());
        return itemStack.getTag().getCompound(tagName);
    }

    public static void setCompoundTag(ItemStack itemStack, String tagName, CompoundNBT tagValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTag().put(tagName, tagValue);
    }

}