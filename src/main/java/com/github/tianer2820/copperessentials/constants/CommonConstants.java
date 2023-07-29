package com.github.tianer2820.copperessentials.constants;



import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class CommonConstants {    
    public static final String ITEM_ID = "item_id";
    public static NamespacedKey ITEM_ID_KEY = null;


    public static final String COPPER_TOOLS = "copper_tools";

    public static final String COPPER_PICKAXE_RECIPE = "copper_pickaxe_recipe";
    public static NamespacedKey COPPER_PICKAXE_RECIPE_KEY = null;


    public static final String COPPER_AXE_RECIPE = "copper_axe_recipe";
    public static NamespacedKey COPPER_AXE_RECIPE_KEY = null;

    public static void initializeConstants(Plugin plugin){
        ITEM_ID_KEY = new NamespacedKey(plugin, ITEM_ID);
        COPPER_PICKAXE_RECIPE_KEY = new NamespacedKey(plugin, COPPER_PICKAXE_RECIPE);
        COPPER_AXE_RECIPE_KEY = new NamespacedKey(plugin, COPPER_AXE_RECIPE);
    }
}
