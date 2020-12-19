package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.core.ModTileEntities;
import com.mrcrayfish.furniture.gui.inventory.ISimpleInventory;
import com.mrcrayfish.furniture.item.crafting.RecipeType;
import com.mrcrayfish.furniture.item.crafting.ToasterCookingRecipe;
import com.mrcrayfish.furniture.util.ItemStackHelper;
import com.mrcrayfish.furniture.util.TileEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.Optional;

public class ToasterTileEntity extends SyncClientTileEntity implements ITickableTileEntity, ISimpleInventory {
    public NonNullList<ItemStack> slots = NonNullList.withSize(2, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[] {0, 0};
    private final int[] cookingTotalTimes = new int[2];
    private final float[] experienceAmounts = new float[] {0F, 0F};
    private boolean toasting = false;

    public ToasterTileEntity() {
        super(ModTileEntities.TOASTER);
    }

    public boolean addSlice(ItemStack item, int cookTime, float experience) {
        for(int i = 0; i < slots.size(); i++) {
            if (slots.get(i).isEmpty()) {
                ItemStack copy = item.copy();
                copy.setCount(1); // item should already have a size of 1.
                slots.set(i, copy);
                cookingTimes[i] = 0;
                cookingTotalTimes[i] = cookTime;
                experienceAmounts[i] = experience;
                CompoundNBT compound = new CompoundNBT();
                this.writeItems(compound);
                this.writeCookingTimes(compound);
                this.writeCookingTotalTimes(compound);
                this.writeExperience(compound);
                TileEntityUtil.sendUpdatePacket(this, super.write(compound));
                return true;
            }
        }
        return false;
    }

    public Optional<ToasterCookingRecipe> findMatchingRecipe(ItemStack input) {
        return this.slots.stream().noneMatch(ItemStack::isEmpty)? Optional.empty() : this.world.getRecipeManager().getRecipe(RecipeType.TOASTER_COOKING, new Inventory(input), this.world);
    }

    public void removeSlice() {
        for(int i = 0; i < slots.size(); i++) {
            if (!slots.get(i).isEmpty()) {
                if (!world.isRemote)
                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, slots.get(i)));
                slots.set(i, ItemStack.EMPTY);
                TileEntityUtil.markBlockForUpdate(world, pos);
                return;
            }
        }
    }

    public void startToasting() {
        this.toasting = true;
        if (!world.isRemote) {
            BlockPos pos = this.getPos();
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, ModSounds.BLOCK_TOASTER_DOWN, SoundCategory.BLOCKS, 0.75F, 1.0F);
        }
        TileEntityUtil.markBlockForUpdate(world, pos);
    }

    public boolean isToasting() {
        return toasting;
    }

    public ItemStack getSlice(int slot) {
        return slots.get(slot);
    }

    @Override
    public void tick() {
        if (toasting) {
            boolean anyChanged = false;
            boolean soundPlayed = false;
            int remaining = 0;
            for (int i = 0; i < slots.size(); ++i) {
                if (slots.get(i).isEmpty())
                    continue;
                ++remaining;
                if (cookingTimes[i] == cookingTotalTimes[i]) {
                    --remaining;
                    Optional<ToasterCookingRecipe> optional = this.world.getRecipeManager().getRecipe(RecipeType.TOASTER_COOKING, new Inventory(this.slots.get(i)), this.world);
                    if (optional.isPresent()) {
                        world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, optional.get().getRecipeOutput().copy()));
                        int amount = (int) experienceAmounts[i];
                        BlockPos pos = this.getPos();
                        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
                        while (amount > 0) {
                            int splitAmount = ExperienceOrbEntity.getXPSplit(amount);
                            amount -= splitAmount;
                            this.world.addEntity(new ExperienceOrbEntity(this.world, x, y, z, splitAmount));
                        }
                    }
                    anyChanged = true;
                    slots.set(i, ItemStack.EMPTY);
                    cookingTimes[i] = 0;
                    cookingTotalTimes[i] = 0;
                    if (!world.isRemote && !soundPlayed) {
                        BlockPos pos = this.getPos();
                        world.playSound(null, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, ModSounds.BLOCK_TOASTER_UP, SoundCategory.BLOCKS, 0.75F, 1.0F);
                        soundPlayed = true;
                    }
                } else {
                    ++cookingTimes[i];
                }
            }

            if (remaining == 0) {
                toasting = false;
                anyChanged = true;
            }

            if (anyChanged) {
                CompoundNBT compound = new CompoundNBT();
                this.writeItems(compound);
                this.writeCookingTimes(compound);
                this.writeToasting(compound);
                TileEntityUtil.sendUpdatePacket(this, super.write(compound));
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if (compound.contains("Items", 9)) {
            slots.clear();
            ItemStackHelper.loadAllItems("Items", compound, slots);
        }
        if (compound.contains("CookingTimes"))
            System.arraycopy(compound.getIntArray("CookingTimes"), 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, cookingTimes.length));
        if (compound.contains("CookingTotalTimes", Constants.NBT.TAG_INT_ARRAY))
            System.arraycopy(compound.getIntArray("CookingTotalTimes"), 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, cookingTimes.length));
        if (compound.contains("Experience", Constants.NBT.TAG_INT_ARRAY)) {
            int[] experience = compound.getIntArray("Experience");
            for (int i = 0; i < Math.min(this.experienceAmounts.length, experience.length); i++)
                this.experienceAmounts[i] = Float.intBitsToFloat(experience[i]);
        }
        if (compound.contains("Toasting"))
            toasting = compound.getBoolean("Toasting");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        writeItems(compound);
        writeCookingTimes(compound);
        writeCookingTotalTimes(compound);
        writeExperience(compound);
        writeToasting(compound);
        return super.write(compound);
    }

    private CompoundNBT writeItems(CompoundNBT compound) {
        ItemStackHelper.saveAllItems("Items", compound, this.slots, true);
        return compound;
    }

    private CompoundNBT writeCookingTimes(CompoundNBT compound) {
        compound.putIntArray("CookingTimes", this.cookingTimes);
        return compound;
    }

    private CompoundNBT writeCookingTotalTimes(CompoundNBT compound) {
        compound.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        return compound;
    }

    private CompoundNBT writeExperience(CompoundNBT compound) {
        int[] experience = new int[this.experienceAmounts.length];
        for (int i = 0; i < this.experienceAmounts.length; i++)
            experience[i] = Float.floatToIntBits(experience[i]);
        compound.putIntArray("Experience", experience);
        return compound;
    }

    private CompoundNBT writeToasting(CompoundNBT compound) {
        compound.putBoolean("Toasting", toasting);
        return compound;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public ItemStack getItem(int i) {
        return slots.get(i);
    }

    @Override
    public void clear() {
        for (int i = 0; i < slots.size(); i++)
            slots.set(i, ItemStack.EMPTY);
    }
}
