package com.github.tianer2820.copperessentials.listeners;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
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
    private boolean debug = false;
    private void dbg(Player p, String msg){
        if(debug)
            p.sendMessage(msg);
    }

    private static final Set<Material> logMaterials = ImmutableSet.of(
        Material.OAK_LOG,
        Material.SPRUCE_LOG,
        Material.BIRCH_LOG,
        Material.JUNGLE_LOG,
        Material.ACACIA_LOG,
        Material.CHERRY_LOG,
        Material.DARK_OAK_LOG,
        Material.MANGROVE_LOG,
        Material.CRIMSON_STEM,
        Material.WARPED_STEM
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
        Material.FLOWERING_AZALEA_LEAVES,
        Material.NETHER_WART_BLOCK,
        Material.WARPED_WART_BLOCK
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
        dbg(event.getPlayer(), "Broke: " + block.getType().toString());
        if(!logMaterials.contains(block.getType())){
            return;
        }

        // trace all connected log
        Set<Block> visitedLog = new HashSet<>();
        Set<Block> logWaveFront = new HashSet<>();
        // add upper blocks
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block currentBlock = block.getRelative(dx, 1, dz);
                if(logMaterials.contains(currentBlock.getType())){
                    logWaveFront.add(currentBlock);
                }
            }
        }
        visitedLog.add(block);

        // find connected floating logs, will return empty if not floating
        Set<Block> floatingLogs = FloatingBlocksHelpers.getConnectedFloatingBlocks(visitedLog, logWaveFront,
                b -> logMaterials.contains(b.getType()), 
                AxeListener::isLogSupportingBlock,
                512);

        // break the log blocks and prepare for finding connected leaves
        floatingLogs.remove(block);
        Set<Block> leaveWavefront = new HashSet<>();
        for (Block logBlock : floatingLogs) {
            Block blockAbove = logBlock.getRelative(BlockFace.UP);
            if(leaveMaterials.contains(blockAbove.getType())){
                leaveWavefront.add(blockAbove);
            }
            logBlock.breakNaturally();
        }

        // find floating leaves and break them
        Set<Block> floatingLeaves = FloatingBlocksHelpers.getConnectedFloatingBlocks(Collections.emptySet(), leaveWavefront, 
                b -> leaveMaterials.contains(b.getType()),
                AxeListener::isLeafSupportingBlock,
                512);
        if(!floatingLeaves.isEmpty()){
            floatingLeaves.forEach(Block::breakNaturally);
        }
    }

    private static boolean isLogSupportingBlock(Block block){
        if(block.isLiquid() || block.isPassable() || block.isEmpty()){
            return false;
        }
        if(leaveMaterials.contains(block.getType()) || logMaterials.contains(block.getType())){
            return false;
        }
        return true;
    }

    private static boolean isLeafSupportingBlock(Block block){
        if(block.isLiquid() || block.isPassable() || block.isEmpty()){
            return false;
        }
        if(leaveMaterials.contains(block.getType())){
            return false;
        }
        return true;
    }

}