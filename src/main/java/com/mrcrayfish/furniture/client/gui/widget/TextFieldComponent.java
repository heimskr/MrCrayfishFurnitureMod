package com.mrcrayfish.furniture.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.furniture.client.gui.screen.EditValueContainerScreen;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;

/**
 * Author: MrCrayfish
 */
public class TextFieldComponent extends ValueComponent {
    public TextFieldWidget textFieldLootTable;

    public TextFieldComponent(FontRenderer font, IValueContainer.Entry entry) {
        super(entry.getId(), entry.getName());
        textFieldLootTable = new TextFieldWidget(font, 0, 0, EditValueContainerScreen.WIDTH - EditValueContainerScreen.PADDING * 2, 20, ITextComponent.getTextComponentOrEmpty(""));
        textFieldLootTable.setMaxStringLength(256);
        if (entry.getValue() != null)
            textFieldLootTable.setText(entry.getValue());
    }

    @Override
    public void render(MatrixStack stack, int x, int y, int mouseX, int mouseY) {
        super.render(stack, x, y, mouseX, mouseY);
        textFieldLootTable.x = x;
        textFieldLootTable.y = y + 10;
        textFieldLootTable.render(stack, mouseX, mouseY, 0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        textFieldLootTable.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        textFieldLootTable.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        textFieldLootTable.charTyped(codePoint, modifiers);
    }

    @Override
    public String getValue() {
        return textFieldLootTable.getText();
    }

    @Override
    public IValueContainer.Entry toEntry() {
        return new IValueContainer.Entry(id, getValue());
    }
}
