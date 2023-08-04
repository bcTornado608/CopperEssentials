package com.github.tianer2820.copperessentials.listeners;


import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

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

        // remove the drops of this event, drop manually with functional item
        event.setDropItems(false);
        Collection<ItemStack> centerDrops = centerBlock.getDrops(functionalItemStack, player);
        for (ItemStack drop : centerDrops) {
            player.getWorld().dropItemNaturally(centerBlock.getLocation(), drop);
        }

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