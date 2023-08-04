package com.github.tianer2820.copperessentials.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.joml.Vector3i;

import com.github.tianer2820.copperessentials.items.IceBomb;

public class IceBombListener implements Listener {

    private List<Vector3i> nearByPositions;

    public IceBombListener(){
        super();
        nearByPositions = new ArrayList<>();
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    if(dx*dx + dy*dy + dz*dz <= 3*3){
                        nearByPositions.add(new Vector3i(dx, dy, dz));
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSnowballHit(ProjectileHitEvent event) {
        if(event.getEntityType() != EntityType.SNOWBALL){
            return;
        }
        Snowball projectile = (Snowball)event.getEntity();
        if(!IceBomb.isItem(projectile.getItem())){
            return;
        }

        Block hitBlock = event.getEntity().getLocation().getBlock();
        for (Vector3i delta : nearByPositions) {
            Block currentBlock = hitBlock.getRelative(delta.x, delta.y, delta.z);

            if(currentBlock.getType() == Material.WATER){
                Levelled levelled = (Levelled)currentBlock.getBlockData();
                if(levelled.getLevel() % 8 <= 3){
                    currentBlock.setType(Material.PACKED_ICE);
                }
            }
            else if(currentBlock.getType() == Material.LAVA){
                Levelled levelled = (Levelled)currentBlock.getBlockData();
                if(levelled.getLevel() == 0){
                    currentBlock.setType(Material.OBSIDIAN);
                }
                else{
                    currentBlock.setType(Material.STONE);
                }
            }
            else if(currentBlock.getBlockData() instanceof Waterlogged){
                Waterlogged waterlogged = (Waterlogged)currentBlock.getBlockData();
                waterlogged.setWaterlogged(false);
                currentBlock.setBlockData(waterlogged, true);
            }
        }
    }
}
