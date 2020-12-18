package com.mrcrayfish.furniture.item;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.block.PresentBlock;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.inventory.container.PresentContainer;
import com.mrcrayfish.furniture.inventory.container.PresentInventory;
import com.mrcrayfish.furniture.tileentity.PresentTileEntity;
import com.mrcrayfish.furniture.util.NBTHelper;
import com.mrcrayfish.furniture.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.IRegistryDelegate;

import javax.annotation.Nullable;
import java.util.List;

public class PresentItem extends BlockItem implements INamedContainerProvider, IAuthored {
    public PresentItem(Block block, Item.Properties properties) {
        super(block, properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
//        this.maxStackSi
    }


//    @Override
//    public Item getSignedItem() {
//        return Item.getItemFromBlock(ModBlocks.PRESENT);
//    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTag()) {
            CompoundNBT compound = stack.getTag();
            StringNBT stringTag = (StringNBT) compound.get("Author");
            if (stringTag != null)
                tooltip.add(new TranslationTextComponent(I18n.format("message.cfm.mail_signed_info", stringTag.getString())).mergeStyle(TextFormatting.GRAY));
            else
                tooltip.add(new TranslationTextComponent(I18n.format("message.cfm.present_unsigned_info")).mergeStyle(TextFormatting.GRAY));
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        Hand hand = context.getHand();
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        ItemStack stack = player.getHeldItem(hand);

        if (world.isDirectionSolid(pos, player, Direction.UP)) {
            if (stack.hasTag()) {
                CompoundNBT compound = stack.getTag();
                StringNBT nbttagstring = (StringNBT) compound.get("Author");

                if (nbttagstring != null) {
                    ListNBT itemList = (ListNBT) NBTHelper.getCompoundTag(stack, "Present").get("Items");
                    if (itemList != null) {
                        if (itemList.size() > 0) {
//                        BlockState state = ModBlocks.PRESENT.getDefaultState().withProperty(BlockPresent.COLOUR, EnumDyeColor.byMetadata(stack.getItemDamage()));

//                            BlockState state = PresentBlock.colorRegistry.get(color).get().getDefaultState();
                            BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
                            world.setBlockState(pos.up(), state, 2);
                            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, state.getBlock().getSoundType(state).getPlaceSound(), SoundCategory.BLOCKS, (state.getBlock().getSoundType(state).getVolume() + 1.0F) / 2.0F, state.getBlock().getSoundType(state).getPitch() * 0.8F, false);

                            PresentTileEntity pte = new PresentTileEntity();
                            pte.setOwner(player.getName().getString());

                            for (int i = 0; i < itemList.size(); i++)
                                pte.setInventorySlotContents(i, ItemStack.read(itemList.getCompound(i)));

                            world.setTileEntity(pos.up(), pte);

                            stack.shrink(1);
                            return ActionResultType.SUCCESS;
                        } else if (world.isRemote)
                            PlayerUtil.sendTranslatedMessage(player, "message.cfm.present_place");
                    }
                } else if (world.isRemote)
                    PlayerUtil.sendTranslatedMessage(player, "message.cfm.present_sign");
            } else if (world.isRemote)
                PlayerUtil.sendTranslatedMessage(player, "message.cfm.present_sign");
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && hand == Hand.MAIN_HAND) {
            CompoundNBT tagCompound = stack.getTag();
            if (tagCompound != null) {
                String author = tagCompound.getString("Author");
                if (author.isEmpty())
                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) stack.getItem());
                else
                    PlayerUtil.sendTranslatedMessage(player, "message.cfm.present_wrap");
            } else
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) stack.getItem());
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public ITextComponent getDisplayName() {
        return ITextComponent.getTextComponentOrEmpty(I18n.format("container.cfm.present"));
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof PresentItem)
            return new PresentContainer(windowID, playerInventory, new PresentInventory(stack));
        return null;
    }

    @Override
    public Item getSignedItem() {
        return this;
    }
}
