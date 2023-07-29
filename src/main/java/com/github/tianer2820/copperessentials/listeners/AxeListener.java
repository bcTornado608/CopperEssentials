package com.github.tianer2820.copperessentials.listeners;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3i;

import com.github.tianer2820.copperessentials.items.CopperAxe;
import com.github.tianer2820.copperessentials.utils.FloatingBlocksHelpers;
import com.google.common.collect.ImmutableSet;


/**
 * NewPlayerListener
 */
public class AxeListener implements Listener {

    private static final Set<Material> logMaterials = ImmutableSet.of(
        Material.OAK_LOG,
        Material.SPRUCE_LOG,
        Material.BIRCH_LOG,
        Material.JUNGLE_LOG,
        Material.ACACIA_LOG,
        Material.CHERRY_LOG,
        Material.DARK_OAK_LOG,
        Material.MANGROVE_LOG
    );
    private static final Set<Material> leaveMaterials = ImmutableSet.of(
        Material.OAK_LEAVES,
        Material.SPRUCE_LEAVES,
        Material.BIRCH_LEAVES,
        Material.JUNGLE_LEAVES,
        Material.ACACIA_LEAVES,
        Material.CHERRY_LEAVES,
        Material.DARK_OAK_LEAVES,
        Material.MANGROVE_LEAVES,
        Material.AZALEA_LEAVES,
        Material.FLOWERING_AZALEA_LEAVES
    );

    // un-nest loops, make code more readable
    private static List<Vector3i> surroundingOffsets = new ArrayList<>();

    public AxeListener(){
        super();
        

        // initialize surrounding offsets
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if(dx == 0 && dy == 0 && dz == 0){
                        continue;
                    }
                    surroundingOffsets.add(new Vector3i(dx, dy, dz));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();

        if(!CopperAxe.isItem(handItem)){
            return;
        }
        Block block = event.getBlock();
        if(!logMaterials.contains(block.getType())){
            return;
        }

        // trace all upper connected log

        Set<Block> visited = new HashSet<>();
        Set<Block> waveFront = new HashSet<>();

        // add upper blocks
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block currentBlock = block.getRelative(dx, 1, dz);
                if(logMaterials.contains(currentBlock.getType())){
                    waveFront.add(currentBlock);
                }
            }
        }
        visited.add(block);

        // find connected floating logs, will return empty if not floating
        Set<Block> floatingLogs = FloatingBlocksHelpers.getConnectedFloatingBlocks(visited, waveFront,
                b -> logMaterials.contains(b.getType()), 
                AxeListener::isSupportingBlock, 512);

        for (Block logBlock : floatingLogs) {
            logBlock.breakNaturally();
        }
    }

    private static boolean isSupportingBlock(Block block){
        if(block.isLiquid() || block.isPassable() || block.isEmpty()){
            return false;
        }
        if(leaveMaterials.contains(block.getType()) || logMaterials.contains(block.getType())){
            return false;
        }
        return true;
    }

}