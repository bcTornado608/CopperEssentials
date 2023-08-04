package com.github.tianer2820.copperessentials;



import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.tianer2820.copperessentials.constants.CommonConstants;
import com.github.tianer2820.copperessentials.items.CopperAxe;
import com.github.tianer2820.copperessentials.items.CopperHoe;
import com.github.tianer2820.copperessentials.items.CopperPickaxe;
import com.github.tianer2820.copperessentials.items.CopperShovel;
import com.github.tianer2820.copperessentials.items.CopperSword;
import com.github.tianer2820.copperessentials.items.IceBomb;
import com.github.tianer2820.copperessentials.listeners.AxeListener;
import com.github.tianer2820.copperessentials.listeners.HoeListener;
import com.github.tianer2820.copperessentials.listeners.IceBombListener;
import com.github.tianer2820.copperessentials.listeners.PickaxeListener;
import com.github.tianer2820.copperessentials.listeners.ShovelListener;
import com.github.tianer2820.copperessentials.listeners.SwordListener;



public class CopperEssentials extends JavaPlugin implements Listener{
    static private CopperEssentials instance;


    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        CommonConstants.initializeConstants(this);

        registerCommands();
        registerListeners();
        registerRecipies();
    }

    public static CopperEssentials getInstance(){
        return instance;
    }
    
    private void registerCommands(){
    }

    private void registerListeners(){
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new PickaxeListener(), this);
        manager.registerEvents(new AxeListener(), this);
        manager.registerEvents(new ShovelListener(), this);
        manager.registerEvents(new HoeListener(), this);
        manager.registerEvents(new SwordListener(), this);
        manager.registerEvents(new IceBombListener(), this);
    }

    private void registerRecipies(){
        // copper pickaxe
        ShapedRecipe recipe = new ShapedRecipe(CommonConstants.COPPER_PICKAXE_RECIPE_KEY, CopperPickaxe.getItemStack(1));
        recipe.shape(
                "CCC",
                " I ",
                " I ");
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('I', Material.STICK);
        getServer().addRecipe(recipe);

        // copper axe
        recipe = new ShapedRecipe(CommonConstants.COPPER_AXE_RECIPE_KEY, CopperAxe.getItemStack(1));
        recipe.shape(
            "CC",
            "IC",
            "I ");
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('I', Material.STICK);
        getServer().addRecipe(recipe);

        // copper shovel
        recipe = new ShapedRecipe(CommonConstants.COPPER_SHOVEL_RECIPE_KEY, CopperShovel.getItemStack(1));
        recipe.shape(
            "C",
            "I",
            "I");
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('I', Material.STICK);
        getServer().addRecipe(recipe);


        // copper hoe
        recipe = new ShapedRecipe(CommonConstants.COPPER_HOE_RECIPE_KEY, CopperHoe.getItemStack(1));
        recipe.shape(
            "CC",
            "I ",
            "I ");
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('I', Material.STICK);
        getServer().addRecipe(recipe);

        // copper sword
        recipe = new ShapedRecipe(CommonConstants.COPPER_SWORD_RECIPE_KEY, CopperSword.getItemStack(1)); 
        recipe.shape(
            "C",
            "C",
            "I"
        );
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('I', Material.STICK);
        getServer().addRecipe(recipe);

        // copper name tag
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(CommonConstants.COPPER_NAME_TAG_RECIPE_KEY, new ItemStack(Material.NAME_TAG));
        shapelessRecipe.addIngredient(1, Material.STRING);
        shapelessRecipe.addIngredient(1, Material.COPPER_INGOT);
        getServer().addRecipe(shapelessRecipe);

        // ice bomb
        recipe = new ShapedRecipe(CommonConstants.ICE_BOMB_RECIPE_KEY, IceBomb.getItemStack(1)); 
        recipe.shape(
            " S ",
            "SGS",
            " S "
        );
        recipe.setIngredient('S', Material.SNOWBALL);
        recipe.setIngredient('G', Material.GUNPOWDER);
        getServer().addRecipe(recipe);

    }
}
