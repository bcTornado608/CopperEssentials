package com.github.tianer2820.copperessentials.tasks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.tianer2820.copperessentials.CopperEssentials;
import com.github.tianer2820.copperessentials.utils.PotHelpers;

public class SlimePotTask extends BukkitRunnable{
    private Set<UUID> itemsInWater = new HashSet<>();

    @Override
    public void run() {
        Set<Item> allItems = new HashSet<>();
        for (World world : CopperEssentials.getInstance().getServer().getWorlds()) {
            Collection<Item> items = world.getEntitiesByClass(Item.class);
            allItems.addAll(items);
        }
        itemsInWater.retainAll(allItems.stream().map(Item::getUniqueId).collect(Collectors.toSet()));
        allItems.forEach(this::updateItemInWater);
    }

    private void updateItemInWater(Item item){
        if(item.getLocation().getBlock().getType() == Material.WATER){
            // item in water
            if(itemsInWater.contains(item.getUniqueId())){
                // already in water, ignore
            } else {
                // just entered water, call trigger
                processItemEnterWaterEvent(item);
                itemsInWater.add(item.getUniqueId());
            }
        } else {
            // item not in water
            itemsInWater.remove(item.getUniqueId());
        }
    }
    
    private void processItemEnterWaterEvent(Item item){
        ItemStack itemStack = item.getItemStack();
        if (itemStack.getType() == Material.SLIME_BALL){
            // slime ball entered water, detect copper pot and spawn slime
            if (PotHelpers.detectPot(Material.COPPER_BLOCK, Material.WATER, item.getLocation().getBlock(), 256).isEmpty()){
                CopperEssentials.getInstance().getLogger().info("slime ball thrown in, not a pot");
            } else {
                CopperEssentials.getInstance().getLogger().info("slime ball thrown in, is a pot!");

            }
        } else if (itemStack.getType() == Material.SUGAR){
            // sugar entered water, detect copper pot and grow slime
        }
    }
}
