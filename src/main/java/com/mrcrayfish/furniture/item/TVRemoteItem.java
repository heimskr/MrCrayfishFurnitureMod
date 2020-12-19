package com.mrcrayfish.furniture.item;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.tileentity.TVTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class TVRemoteItem extends Item {
    public TVRemoteItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
//            String info = I18n.format("cfm.tv_remote.info");
//            tooltip.addAll(Minecraft.getInstance().fontRenderer.listFormattedStringToWidth(info, 150));
//            List<IReorderingProcessor> lines = Minecraft.getInstance().fontRenderer.trimStringToWidth(ITextProperties.func_240652_a_(info), 150);
//            for (int i = 0; i < lines.size(); ++i) {
//            }
//            tooltip.add(new TranslationTextComponent(I18n.format("message.cfm.tv_remote.info")));
        } else {
//            tooltip.add(new TranslationTextComponent(TextFormatting.YELLOW + I18n.format("message.cfm.info")));
//            tooltip.add(TextFormatting.YELLOW + I18n.format("cfm.info"));
        }
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        return activateTelevision(context.getWorld(), context.getPlayer());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        return new ActionResult<ItemStack>(activateTelevision(world, player), player.getHeldItem(hand));
    }

    public ActionResultType activateTelevision(World world, PlayerEntity player) {
        Vector3d startVec = player.getEyePosition(0F);
        Vector3d endVec = startVec.add(player.getLookVec().normalize().scale(16));
        RayTraceContext context = new RayTraceContext(startVec, endVec, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, player);
        RayTraceResult result = world.rayTraceBlocks(context);
        if (result != null && result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = new BlockPos(result.getHitVec()); // ???
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TVTileEntity) {
                TVTileEntity tv = (TVTileEntity) tileEntity;
                if (player.isSneaking())
                    tv.setPowered(!tv.isPowered());
                else if (tv.nextChannel())
                    world.playSound(null, pos, ModSounds.WHITE_NOISE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
