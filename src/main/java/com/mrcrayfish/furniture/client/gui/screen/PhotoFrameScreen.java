package com.mrcrayfish.furniture.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.network.PacketHandler;
import com.mrcrayfish.furniture.network.message.MessageSetDoorMatMessage;
import com.mrcrayfish.furniture.network.message.MessageUpdatePhotoFrame;
import com.mrcrayfish.furniture.tileentity.PhotoFrameTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;

public class PhotoFrameScreen extends Screen {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/photo_frame_settings.png");

    private int xSize = 176;
    private int ySize = 100;

    private PhotoFrameTileEntity photoFrameTileEntity;
    private TextFieldWidget urlField;
    private Button btnStretch;
    private boolean stretch;

    public PhotoFrameScreen(PhotoFrameTileEntity photoFrameTileEntity) {
        super(new TranslationTextComponent("gui.cfm.photo_frame_url")); // Not really a title, but...
        this.photoFrameTileEntity = photoFrameTileEntity;
        this.stretch = photoFrameTileEntity.isStretched();
    }

    @Override
    protected void init() {
        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        this.urlField = new TextFieldWidget(this.font, guiLeft + 8, guiTop + 18, 160, 18, ITextComponent.getTextComponentOrEmpty(""));
        if (this.photoFrameTileEntity.getUrl() != null)
            this.urlField.setText(this.photoFrameTileEntity.getUrl());
        this.children.add(this.urlField);


        this.btnStretch = this.addButton(new Button(guiLeft + 7, guiTop + 42 + 10, 162, 20, ITextComponent.getTextComponentOrEmpty(I18n.format("gui.button.cfm." + (this.stretch? "on" : "off"))), button -> {
            this.stretch = !this.stretch;
            btnStretch.setMessage(ITextComponent.getTextComponentOrEmpty(I18n.format("gui.button.cfm." + (this.stretch? "on" : "off"))));
        }));

        this.addButton(new Button(guiLeft + 7, guiTop + 42 + 10 + 22, 79, 20, ITextComponent.getTextComponentOrEmpty(I18n.format("gui.button.cfm.save")), button -> {
            PacketHandler.instance.sendToServer(new MessageUpdatePhotoFrame(this.photoFrameTileEntity.getPos(), this.urlField.getText(), this.stretch));
            this.minecraft.player.closeScreen();
        }));

        this.addButton(new Button(guiLeft + 91, guiTop + 42 + 10 + 22, 79, 20, ITextComponent.getTextComponentOrEmpty(I18n.format("gui.button.cfm.cancel")), button -> {
            this.minecraft.player.closeScreen();
        }));
    }

    @Override
    public void tick() {
        super.tick();
        this.urlField.tick();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, startX, startY, 0, 0, this.xSize, this.ySize);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.font.drawString(matrixStack, this.title.getString(), startX + 8.0F, startY + 6.0F, 0x404040);
        this.font.drawString(matrixStack, I18n.format("gui.cfm.stretch"), startX + 8.0F, startY + 42.0F, 0x404040);

        this.urlField.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
