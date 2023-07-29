package com.github.tianer2820.copperessentials.listeners;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.tianer2820.copperessentials.items.CopperSword;
import com.google.common.collect.ImmutableMap;

public class SwordListener implements Listener {
    Map<EntityType, Material> entityToHeadMap = ImmutableMap.of(
            EntityType.CREEPER, Material.CREEPER_HEAD,
            EntityType.ZOMBIE, Material.ZOMBIE_HEAD,
            EntityType.SKELETON, Material.SKELETON_SKULL,
            EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SKULL,
            EntityType.PIGLIN, Material.PIGLIN_HEAD,
            EntityType.ENDER_DRAGON, Material.DRAGON_HEAD,
            EntityType.PLAYER, Material.PLAYER_HEAD);

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null || !CopperSword.isItem(killer.getInventory().getItemInMainHand())) {
            return;
        }
        // Killed by player with copper sword

        EntityType entityType = event.getEntityType();
        if(!entityToHeadMap.keySet().contains(entityType)){
            return;
        }
        Material headMaterial = entityToHeadMap.get(entityType);
        ItemStack head = null;
        if(headMaterial == Material.PLAYER_HEAD){
            // set the head data to the player
            Player victim = (Player) event.getEntity();
            head = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta skullMeta = (SkullMeta)head.getItemMeta();
            skullMeta.setOwningPlayer(victim);
            head.setItemMeta(skullMeta);
        } else {
            head = new ItemStack(headMaterial, 1);
        }

        event.getDrops().clear();
        event.getDrops().add(head);
    }
}
