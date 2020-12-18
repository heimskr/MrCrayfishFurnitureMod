package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.furniture.inventory.container.CrateContainer;
import com.mrcrayfish.furniture.inventory.container.ItemInventory;
import com.mrcrayfish.furniture.inventory.container.PresentContainer;
import com.mrcrayfish.furniture.network.PacketHandler;
import com.mrcrayfish.furniture.network.message.MessageSignItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.opengl.GL11;

public class PresentScreen extends ContainerScreen<PresentContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("cfm:textures/gui/present.png");
    private Button btnWrap;
    private PlayerEntity player;
    private ItemStack heldItem;

    public PresentScreen(PresentContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.player = playerInventory.player;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        if (heldItem == null)
            heldItem = player.getHeldItemMainhand();
        if (heldItem != player.getHeldItemMainhand())
            this.minecraft.player.closeScreen();
        this.minecraft.fontRenderer.drawString(matrixStack, I18n.format("container.inventory"), 8, ySize - 94, 4210752);
    }

    @Override
    protected void init() {
        super.init();
        int posX = width / 2 + 40;
        int posY = height / 2 - 50;
        btnWrap = this.addButton(new Button(posX, posY - 10, 40, 20, ITextComponent.getTextComponentOrEmpty(I18n.format("cfm.button.wrap")), button -> {
            ListNBT itemList = new ListNBT();
            System.out.println("Enumerating inventory.");
            ItemInventory itemInventory = this.container.getItemInventory();
            for (int i = 0; i < itemInventory.getSizeInventory(); ++i) {
                ItemStack stack = itemInventory.getStackInSlot(i);
                System.out.println("Item: " + stack.getTranslationKey() + " x " + stack.getCount());
                CompoundNBT compound = new CompoundNBT();
                stack.write(compound);
                itemList.add(compound);
            }
            PacketHandler.instance.sendToServer(new MessageSignItem(itemList));
        }));
        btnWrap.visible = true;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, startX, startY, 0, 0, this.xSize, this.ySize);
    }
}
//*/
