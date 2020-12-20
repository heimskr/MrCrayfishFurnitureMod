package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.furniture.inventory.container.ComputerContainer;
import com.mrcrayfish.furniture.inventory.container.CrateContainer;
import com.mrcrayfish.furniture.network.PacketHandler;
import com.mrcrayfish.furniture.network.message.MessageMineBayBuy;
import com.mrcrayfish.furniture.network.message.MessageMineBayClosed;
import com.mrcrayfish.furniture.tileentity.ComputerTileEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ComputerScreen extends ContainerScreen<ComputerContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("cfm:textures/gui/computer.png");

    private int itemNum;
    private ItemStack buySlot;
    private ComputerTileEntity computer;
    private RecipeData[] itemdata;

    public ComputerScreen(ComputerContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.computer = container.;
        this.xSize = 176;
        this.ySize = 187;
    }

    @Override
    public void init() {
        super.init();

        int posX = width / 2;
        int posY = height / 2;

        this.addButton(new Button(posX - 48, posY - 80, 15, 20, ITextComponent.getTextComponentOrEmpty("<"), button -> {
            --itemNum;
            if (itemNum < 0)
                itemNum = 0;
            this.computer.setBrowsingInfo(itemNum);
        }));
        this.addButton(new Button(posX + 34, posY - 80, 15, 20, ITextComponent.getTextComponentOrEmpty(">"), button -> {
            ++itemNum;
            if (itemdata.length - 1 < itemNum)
                itemNum = itemdata.length - 1;
            this.computer.setBrowsingInfo(itemNum);
        }));
        this.addButton(new Button(2, posX - 48, posY - 57, 29, 20, I18n.format("cfm.button.buy"), button -> {
            this.buySlot = this.computer.getStackInSlot(0);
            if (!buySlot.isEmpty()) {
                ItemStack money = itemdata[itemNum].getCurrency();
                if (buySlot.getItem() == money.getItem() && buySlot.getItemDamage() == money.getItemDamage())
                    PacketHandler.instance.sendToServer(new MessageMineBayBuy(this.itemNum, this.computer.getPos().getX(), this.computer.getPos().getY(), this.computer.getPos().getZ()));
            }
        }));

        this.itemNum = computer.getBrowsingInfo();
        itemdata = Recipes.getMineBayItems();
    }

    @Override
    public void onClose() {
        PacketHandler.instance.sendToServer(new MessageMineBayClosed(computer.getPos().getX(), computer.getPos().getY(), computer.getPos().getZ()));
        super.onClose();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x, int y) {
        this.font.drawString(stack, I18n.format("container.inventory"), 8, (ySize - 103), 0x404040);

        GL11.glPushMatrix();
        RenderHelper.enableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        itemRender.zLevel = 100.0F;

        if (1 <= itemNum) {
            ItemStack pre = itemdata[itemNum - 1].getInput();
            itemRender.renderItemAndEffectIntoGUI(pre, 57, 16);
            itemRender.renderItemOverlays(this.fontRenderer, pre, 57, 16);
        }

        ItemStack stock = itemdata[itemNum].getInput();
        itemRender.renderItemAndEffectIntoGUI(stock, 80, 16);
        itemRender.renderItemOverlays(this.fontRenderer, stock, 80, 16);

        if (itemNum < itemdata.length - 1) {
            ItemStack post = itemdata[itemNum + 1].getInput();
            itemRender.renderItemAndEffectIntoGUI(post, 103, 16);
            itemRender.renderItemOverlays(this.fontRenderer, post, 103, 16);
        }

        ItemStack currency = itemdata[itemNum].getCurrency();
        itemRender.renderItemAndEffectIntoGUI(currency, 73, 40);
        itemRender.renderItemOverlays(this.fontRenderer, currency, 73, 40);
        itemRender.zLevel = 0.0F;
        GL11.glDisable(GL11.GL_LIGHTING);

        int price = itemdata[itemNum].getPrice();
        this.font.drawString(stack, "x" + Integer.toString(price), 90, 44, 0);

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack, mouseX, mouseY);

        ItemStack stock = itemdata[itemNum].getInput();
        if (this.isPointInRegion(80, 16, 16, 16, mouseX, mouseY))
            this.renderTooltip(stack, stock, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.color4f(1.F, 1.F, 1.F, 1.F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        this.drawTexturedModalRect(l, i1 - 10, 0, 0, xSize, ySize + 21);
    }
}
