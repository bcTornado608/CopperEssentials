package com.github.tianer2820.copperessentials.listeners;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.tianer2820.copperessentials.items.CopperShovel;


/**
 * NewPlayerListener
 */
public class ShovelListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();

        if(!CopperShovel.isItem(handItem)){
            return;
        }

        Block centerBlock = event.getBlock();
        ItemStack functionalItemStack = new ItemStack(Material.IRON_SHOVEL);

        // copy the enchantments to the functional item
        ItemMeta meta = functionalItemStack.getItemMeta();
        for (var enchantment : handItem.getEnchantments().entrySet()) {
            meta.addEnchant(enchantment.getKey(), enchantment.getValue(), false);
        }
        functionalItemStack.setItemMeta(meta);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if(dx == 0 && dy == 0 && dz == 0){
                        continue;
                    }
                    Block edgeBlock = centerBlock.getRelative(dx, dy, dz);
                    if(!edgeBlock.isPreferredTool(functionalItemStack)){
                        continue;
                    }

                    edgeBlock.breakNaturally(functionalItemStack, true, true);
                }
            }
        }

    }

}