package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.inventory.container.ComputerContainer;
import com.mrcrayfish.furniture.item.crafting.MineBayRecipe;
import com.mrcrayfish.furniture.item.crafting.RecipeType;
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
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class ComputerScreen extends ContainerScreen<ComputerContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/computer.png");

    private int itemNum;
    private ItemStack buySlot;
    private ComputerTileEntity computer;
    private List<MineBayRecipe> recipes;

    public ComputerScreen(ComputerContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.computer = container.computer;
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
            if (recipes.size() - 1 < itemNum)
                itemNum = recipes.size() - 1;
            this.computer.setBrowsingInfo(itemNum);
        }));
        this.addButton(new Button(posX - 48, posY - 57, 29, 20, new TranslationTextComponent("gui.button.cfm.buy"), button -> {
            this.buySlot = this.computer.getStackInSlot(0);
            if (!buySlot.isEmpty()) {
                ItemStack money = getIngredient(itemNum);
                if (money != null && buySlot.getItem() == money.getItem())
                    PacketHandler.instance.sendToServer(new MessageMineBayBuy(this.itemNum, this.computer.getPos().getX(), this.computer.getPos().getY(), this.computer.getPos().getZ()));
            }
        }));

        this.itemNum = computer.getBrowsingInfo();
        this.recipes = container.computer.getWorld().getRecipeManager().getRecipesForType(RecipeType.MINEBAY);

        for (int i = 0; i < recipes.size(); ++i) {
            MineBayRecipe recipe = recipes.get(i);
            FurnitureMod.LOGGER.warn("Recipe: " + recipe.getRecipeOutput().getTranslationKey() + " x " + recipe.getRecipeOutput().getCount());
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            for (int j = 0; j < ingredients.size(); ++j) {
                Ingredient ingredient = ingredients.get(j);
                FurnitureMod.LOGGER.warn("- Ingredient: " + (ingredient.isSimple()? "simple" : "simplen't"));
                for (ItemStack stack: ingredient.getMatchingStacks())
                    FurnitureMod.LOGGER.warn("  - Stack: " + stack.getTranslationKey() + " x " + stack.getCount());
            }
        }
    }

    @Override
    public void onClose() {
        PacketHandler.instance.sendToServer(new MessageMineBayClosed(computer.getPos().getX(), computer.getPos().getY(), computer.getPos().getZ()));
        super.onClose();
    }

    private ItemStack getIngredient(int index) {
        MineBayRecipe recipe;
        try {
            recipe = recipes.get(index);
        } catch (IndexOutOfBoundsException ioob) {
            FurnitureMod.LOGGER.warn("Index out of bounds: [" + index + "].0.0 (recipes.size(): " + recipes.size() + ")");
            return null;
        }

        try {
            return recipe.getIngredients().get(0).getMatchingStacks()[0];
        } catch (ArrayIndexOutOfBoundsException aioob) {
            FurnitureMod.LOGGER.warn("Array index out of bounds: " + index + ".0.[0]");
        } catch (IndexOutOfBoundsException ioob) {
            FurnitureMod.LOGGER.warn("Index out of bounds: " + index + ".[0].0");
        }
        return null;
    }

    private ItemStack getOutput(int index) {
        try {
            return recipes.get(index).getRecipeOutput();
        } catch (IndexOutOfBoundsException ioob) {
            FurnitureMod.LOGGER.warn("Index out of bounds: [" + index + "].0 (recipes.size(): " + recipes.size() + ")");
        }
        return null;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x, int y) {
        this.font.drawString(stack, I18n.format("container.inventory"), 8, (ySize - 103), 0x404040);

        stack.push();
        RenderHelper.enableStandardItemLighting();
        RenderSystem.disableLighting();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableColorMaterial();
        RenderSystem.enableLighting();
        RenderSystem.enableBlend();

        itemRenderer.zLevel = 100.0F;

        if (1 <= itemNum) {
            ItemStack pre = getOutput(itemNum - 1);
            if (pre != null) {
                itemRenderer.renderItemAndEffectIntoGUI(pre, 57, 16);
                itemRenderer.renderItemOverlays(this.font, pre, 57, 16);
            }
        }

        ItemStack stock = getOutput(itemNum);
        if (stock != null) {
            itemRenderer.renderItemAndEffectIntoGUI(stock, 80, 16);
             itemRenderer.renderItemOverlays(this.font, stock, 80, 16);
        }

        if (itemNum < recipes.size() - 1) {
            ItemStack post = getOutput(itemNum + 1);
            if (post != null) {
                itemRenderer.renderItemAndEffectIntoGUI(post, 103, 16);
                itemRenderer.renderItemOverlays(this.font, post, 103, 16);
            }
        }

        ItemStack cost = getIngredient(itemNum);
        if (cost != null) {
            itemRenderer.renderItemAndEffectIntoGUI(cost, 73, 40);
            itemRenderer.renderItemOverlays(this.font, cost, 73, 40);
            itemRenderer.zLevel = 0.0F;
        }

        RenderSystem.disableLighting();

        if (cost != null) {
            int price = cost.getCount();
            this.font.drawString(stack, "x" + price, 90, 44, 0);
        }

        stack.pop();
        RenderSystem.enableLighting();
        RenderSystem.enableDepthTest();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack, mouseX, mouseY);

        ItemStack stock = getIngredient(itemNum);
        if (stock != null && this.isPointInRegion(80, 16, 16, 16, mouseX, mouseY))
            this.renderTooltip(stack, stock, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.F, 1.F, 1.F, 1.F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int posX = (width - xSize) / 2;
        int posY = (height - ySize) / 2;
        this.blit(stack, posX, posY - 10, 0, 0, xSize, ySize + 21);
    }
}
