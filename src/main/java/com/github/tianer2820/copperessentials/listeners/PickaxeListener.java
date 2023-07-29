package com.github.tianer2820.copperessentials.listeners;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.tianer2820.copperessentials.items.CopperPickaxe;


/**
 * NewPlayerListener
 */
public class PickaxeListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();

        if(!CopperPickaxe.isItem(handItem)){
            return;
        }

        Block centerBlock = event.getBlock();
        ItemStack functionalItemStack = new ItemStack(Material.IRON_PICKAXE);

        // copy the enchantments to the functional item
        ItemMeta meta = functionalItemStack.getItemMeta();
        for (var enchantment : handItem.getEnchantments().entrySet()) {
            meta.addEnchant(enchantment.getKey(), enchantment.getValue(), false);
        }
        functionalItemStack.setItemMeta(meta);

        for (int dx = 0; dx < 3; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                for (int dz = 0; dz < 3; dz++) {
                    if(dx == 1 && dy == 1 && dz == 1){
                        continue;
                    }
                    Block edgeBlock = centerBlock.getRelative(dx-1, dy-1, dz-1);
                    if(!edgeBlock.isPreferredTool(functionalItemStack)){
                        continue;
                    }

                    edgeBlock.breakNaturally(functionalItemStack, true, true);
                }
            }
        }

    }

}