package com.mrcrayfish.furniture.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.client.Minecraft;

import java.awt.*;

/**
 * Author: MrCrayfish
 */
public abstract class ValueComponent {
    protected String id;
    protected String name;

    public ValueComponent(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void render(MatrixStack stack, int x, int y, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawStringWithShadow(stack, name, x, y, Color.WHITE.getRGB());
    }

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void keyPressed(int keyCode, int scanCode, int modifiers);

    public void charTyped(char codePoint, int modifiers) {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract String getValue();

    public abstract IValueContainer.Entry toEntry();
}
