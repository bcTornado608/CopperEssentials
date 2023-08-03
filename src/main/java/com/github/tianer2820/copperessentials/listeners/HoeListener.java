package com.github.tianer2820.copperessentials.listeners;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.github.tianer2820.copperessentials.items.CopperHoe;
import com.google.common.collect.ImmutableMap;


/**
 * NewPlayerListener
 */
public class HoeListener implements Listener {

    private static final Map<Material, Material> cropMaterialsToSeedsMap = ImmutableMap.of(
        Material.WHEAT, Material.WHEAT_SEEDS,
        Material.POTATOES, Material.POTATO,
        Material.CARROTS, Material.CARROT,
        Material.NETHER_WART, Material.NETHER_WART,
        Material.BEETROOTS, Material.BEETROOT_SEEDS,
        Material.TORCHFLOWER_CROP, Material.TORCHFLOWER_SEEDS
    );

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
        if(!CopperHoe.isItem(handItem)){
            return;
        }
        
        // the copper hoe cannot break any block
        event.setCancelled(true);

        Block centerBlock = event.getBlock();
        if(!cropMaterialsToSeedsMap.keySet().contains(centerBlock.getType())){
            return;
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block currentBlock = centerBlock.getRelative(dx, 0, dz);
                if(cropMaterialsToSeedsMap.keySet().contains(currentBlock.getType())){
                    tryHarvest(currentBlock, handItem, event.getPlayer());
                }
            }
        }
    }

    private void tryHarvest(Block block, ItemStack tool, Player player){
        Ageable ageableData = (Ageable)block.getBlockData();
        if(ageableData.getAge() != ageableData.getMaximumAge()){
            // crop not ready to harvest, ignore
            return;
        }

        Collection<ItemStack> drops = block.getDrops(tool, player);
        List<ItemStack> actualDrops = new LinkedList<>();

        Material seetMaterial = cropMaterialsToSeedsMap.get(block.getType());
        for (ItemStack itemStack : drops) {
            if(itemStack.getType() == seetMaterial){
                int amount = itemStack.getAmount() - 1;
                if(amount > 0){
                    itemStack.setAmount(amount);
                    actualDrops.add(itemStack);
                }
            }
        }
        drops.forEach(player.getInventory()::addItem);
        for (ItemStack stack : drops) {
            Map<Integer, ItemStack> overflow = player.getInventory().addItem(stack);
            for (ItemStack itemStack : overflow.values()) {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }
        }

        ageableData.setAge(0);
        block.setBlockData(ageableData);
    }

}