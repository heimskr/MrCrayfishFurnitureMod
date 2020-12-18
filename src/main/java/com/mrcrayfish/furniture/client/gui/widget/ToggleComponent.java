package com.mrcrayfish.furniture.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.furniture.client.gui.screen.EditValueContainerScreen;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Author: MrCrayfish
 */
public class ToggleComponent extends ValueComponent {
    private Button button;
    private boolean state;

    public ToggleComponent(IValueContainer.Entry entry) {
        super(entry.getId(), entry.getName());
        this.button = new Button(0, 0, 0, EditValueContainerScreen.WIDTH - EditValueContainerScreen.PADDING * 2, new TranslationTextComponent("gui.button.cfm.off"), button -> {
            this.setState(!state);
        });
        this.setState(Boolean.valueOf(entry.getValue()));
    }

    @Override
    public void render(MatrixStack stack, int x, int y, int mouseX, int mouseY) {
//        System.out.println("Rendering button: (" + x + ", " + y + ") :: (" + mouseX + ", " + mouseY + ")");
        button.x = x;
        button.y = y + 10;
        button.renderButton(stack, mouseX, mouseY, 0);
        super.render(stack, x, y, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override
    public String getValue() {
        return Boolean.toString(state);
    }

    @Override
    public IValueContainer.Entry toEntry() {
        return new IValueContainer.Entry(id, getValue());
    }

    public void setState(boolean state) {
        this.state = state;
        if (state)
            button.setMessage(new TranslationTextComponent("gui.button.cfm.on"));
        else
            button.setMessage(new TranslationTextComponent("gui.button.cfm.off"));
    }
}
