package com.mrcrayfish.furniture.client.gui.screen;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.furniture.client.gui.widget.TextFieldComponent;
import com.mrcrayfish.furniture.client.gui.widget.ToggleComponent;
import com.mrcrayfish.furniture.client.gui.widget.ValueComponent;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class EditValueContainerScreen extends Screen {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("cfm:textures/gui/value_container.png");

    public static final int WIDTH = 176;
    public static final int PADDING = 10;
    public static final int VALUE_HEIGHT = 35;

    private List<ValueComponent> values = new ArrayList<>();
    private IValueContainer valueContainer;
    private int containerHeight;

    public EditValueContainerScreen(IValueContainer valueContainer) {
        super(new TranslationTextComponent("gui.cfm.door_mat_message"));
        this.font = Minecraft.getInstance().fontRenderer;
        this.valueContainer = valueContainer;
        valueContainer.getEntries().forEach(entry -> {
            if (entry.getType() != null) {
                switch (entry.getType()) {
                    case TEXT_FIELD:
                        TextFieldComponent field = new TextFieldComponent(this.font, entry);
                        values.add(field);
//                        this.children.add(field.textFieldLootTable);
                        break;
                    case TOGGLE:
                        values.add(new ToggleComponent(entry));
                        break;
                }
            }
        });
        this.containerHeight = values.size() * VALUE_HEIGHT + 10 * 2;
    }

    @Override
    public void init() {
        int startX = (this.width - WIDTH) / 2;
        int startY = (this.height - this.containerHeight) / 2;
//        this.buttonList.add(new GuiButton(0, startX + WIDTH + 5, startY + 5, 20, 20, "X"));
        this.addButton(new Button(startX + WIDTH + 5, startY + 5, 20, 20, new TranslationTextComponent("gui.button.cfm.close"), button -> {
            this.minecraft.player.closeScreen();
        }));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        
        int startX = (this.width - WIDTH) / 2;
        int startY = (this.height - this.containerHeight) / 2;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);

        // Top
        drawScaledCustomSizeModalRect(startX, startY, 0, 0, WIDTH, 10, WIDTH, 10, 256, 256);

        // Middle
        drawScaledCustomSizeModalRect(startX, startY + 10, 0, 10, WIDTH, 1, WIDTH, values.size() * VALUE_HEIGHT, 256, 256);

        // Bottom
        drawScaledCustomSizeModalRect(startX, startY + values.size() * VALUE_HEIGHT + 10, 0, 10, WIDTH, 10, WIDTH, 10, 256, 256);

        for (int i = 0; i < values.size(); i++)
            values.get(i).render(stack, startX + PADDING, startY + i * VALUE_HEIGHT + 10, mouseX, mouseY);

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ValueComponent value: values)
            value.mouseClicked((int) mouseX, (int) mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        boolean out = super.keyPressed(keyCode, scanCode, modifiers);
//        for (ValueComponent value: values)
//            value.keyPressed(keyCode, scanCode, modifiers);
//        return out;
//    }
//
    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        boolean out = super.charTyped(codePoint, modifiers);
        for (ValueComponent value: values)
            value.charTyped(codePoint, modifiers);
        return out;
    }

    @Override
    public void onClose() {
        System.out.println("EditValueContainerScreen closed.");
//        PacketHandler.INSTANCE.sendToServer(new MessageUpdateValueContainer(values, valueContainer));
    }

    public static void drawScaledCustomSizeModalRect(int startX, int startY, float float0, float float1, int int0, int int1, int int2, int int3, float float2, float float3) {
        float lvt_10_1_ = 1.0F / float2;
        float lvt_11_1_ = 1.0F / float3;
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(startX, startY + int3, 0.0D).tex(float0 * lvt_10_1_, (float1 + (float) int1) * lvt_11_1_).endVertex();
        builder.pos(startX + int2, startY + int3, 0.0D).tex((float0 + (float) int0) * lvt_10_1_, (float1 + (float) int1) * lvt_11_1_).endVertex();
        builder.pos(startX + int2, startY, 0.0D).tex((float0 + (float) int0) * lvt_10_1_, float1 * lvt_11_1_).endVertex();
        builder.pos(startX, startY, 0.0D).tex(float0 * lvt_10_1_, float1 * lvt_11_1_).endVertex();
        builder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(builder);
    }
}
