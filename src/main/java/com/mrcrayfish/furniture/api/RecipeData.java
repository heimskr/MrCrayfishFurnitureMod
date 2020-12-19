package com.mrcrayfish.furniture.api;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class RecipeData {
    private ItemStack input, output, currency;

    private int price;

    /**
     * Blender Variables
     */
    private String name;
    private int heal;
    private int red;
    private int green;
    private int blue;
    private ArrayList<ItemStack> ingredients;

    /**
     * This method is used to set the input ItemStack. For instance, the Raw
     * Beef before it gets cooked into Cooked Beef. Note: For MineBay, the input
     * ItemStack is the item you are going to sell.
     *
     * @param input Sets the input ItemStack. E.g. Raw Beef
     */
    public RecipeData setInput(ItemStack input) {
        this.input = input;
        return this;
    }

    /**
     * This method is used to set the out ItemStack. For instance, its the
     * Cooked Beef after its been cooked from Raw Beef. Note: For MineBay, you
     * do not need to use this method at all. Please refer to setInput() and
     * setCurrency() only.
     *
     * @param output Sets the output ItemStack. E.g. Cooked Beef
     */
    public RecipeData setOutput(ItemStack output) {
        this.output = output;
        return this;
    }

    /**
     * This method is used to set the currency in MineBay for the specific item.
     * Note: For the Oven and Freezer, you do not need to use this method at
     * all. Please refer to setInput() and setOutput() only.
     *
     * @param currency Sets the currency ItemStack. E.g. Gold Ingot
     */
    public RecipeData setCurrency(ItemStack currency) {
        this.currency = currency;
        return this;
    }

    public RecipeData setPrice(int price) {
        this.price = price;
        return this;
    }

    public RecipeData setColour(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        return this;
    }

    public RecipeData setName(String name) {
        this.name = name;
        return this;
    }

    public RecipeData setHeal(int heal) {
        this.heal = heal;
        return this;
    }

    public String getDrinkName() {
        return this.name;
    }

    public int getHealAmount() {
        return this.heal;
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    public RecipeData addIngredient(ItemStack ingredient) {
        if (ingredients == null)
            ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        return this;
    }

    public ArrayList<ItemStack> getIngredients() {
        return ingredients;
    }

    /**
     * Gets the input ItemStack
     */
    public ItemStack getInput() {
        return input;
    }

    /**
     * Gets the output ItemStack
     */
    public ItemStack getOutput() {
        return output;
    }

    /**
     * Gets the currency ItemStack
     */
    public ItemStack getCurrency() {
        return currency;
    }

    /**
     * Gets the price
     */
    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String result = "";
        if (input != null) {
            String name = input.getItem().getRegistryName().toString();
            result += "input-item=" + name + ",input-amount=" + input.getCount() + ",";
        }
        if (output != null) {
            String name = output.getItem().getRegistryName().toString();
            result += "output-item=" + name + ",output-amount=" + output.getCount() + ",";
        }
        if (currency != null) {
            String name = currency.getItem().getRegistryName().toString();
            result += "payment-item=" + name + "," + "payment-price=" + price + ",";
        }
        if (name != null) {
            result += "name=" + name + ",";
            result += "heal=" + heal + ",";
            result += "color=" + red + "-" + green + "-" + blue + ",";
        }
        if(ingredients != null) {
            StringBuilder pre = new StringBuilder("ingredients=");
            for (int i = 0; i < ingredients.size(); i++) {
                String name = ingredients.get(i).getItem().getRegistryName().toString();
                pre.append(name);
                pre.append(":");
                pre.append(ingredients.get(i).getCount());
                if (i != ingredients.size() - 1)
                    pre.append("/");
            }
            result += pre + ",";
        }
        return result.substring(0, result.length() - 1);
    }

    public static RecipeData convertFrom(Map<String, Object> params) {
        RecipeData data = new RecipeData();
        if(params.containsKey("input")) {
            Object input = params.get("input");
            if (input instanceof ItemStack)
                data.setInput((ItemStack) input);
        }
        if (params.containsKey("output")) {
            Object output = params.get("output");
            if (output instanceof ItemStack)
                data.setOutput((ItemStack) output);
        }
        if (params.containsKey("currency")) {
            Object currency = params.get("currency");
            if (currency instanceof ItemStack)
                data.setCurrency((ItemStack) currency);
        }
        if (params.containsKey("price")) {
            Object price = params.get("price");
            if (price instanceof Integer)
                data.setPrice((Integer) price);
        }
        if (params.containsKey("ingredients")) {
            Object ingredients = params.get("ingredients");
            if (ingredients instanceof ItemStack[])
                for (ItemStack ingredient: (ItemStack[]) ingredients)
                    data.addIngredient(new ItemStack(ingredient.getItem(), ingredient.getCount()));
        }
        if (params.containsKey("color")) {
            Object color = params.get("color");
            if (color instanceof int[]) {
                int[] rgb = (int[]) color;
                data.setColour(rgb[0], rgb[1], rgb[2]);
            }
        }
        if (params.containsKey("name")) {
            Object name = params.get("name");
            if (name instanceof String)
                data.setName((String) name);
        }
        if (params.containsKey("heal")) {
            Object heal = params.get("heal");
            if (heal instanceof Integer)
                data.setHeal((Integer) heal);
        }
        return data;
    }
}
