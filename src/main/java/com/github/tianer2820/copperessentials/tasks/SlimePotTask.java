package com.github.tianer2820.copperessentials.tasks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.tianer2820.copperessentials.CopperEssentials;
import com.github.tianer2820.copperessentials.utils.PotHelpers;
import com.google.common.collect.ImmutableSet;

public class SlimePotTask extends BukkitRunnable{
    private Random rng = new Random();

    private static final int MAX_SLIME_SIZE = 126;
    private static final int MAX_POT_SIZE = 512;

    private static final Set<Material> POT_MATERIALS = ImmutableSet.of(
            Material.COPPER_BLOCK,
            Material.EXPOSED_COPPER,
            Material.WEATHERED_COPPER,
            Material.OXIDIZED_COPPER,
            Material.CUT_COPPER,
            Material.EXPOSED_CUT_COPPER,
            Material.WEATHERED_CUT_COPPER,
            Material.OXIDIZED_CUT_COPPER,
            Material.CUT_COPPER_STAIRS,
            Material.EXPOSED_CUT_COPPER_STAIRS,
            Material.WEATHERED_CUT_COPPER_STAIRS,
            Material.OXIDIZED_CUT_COPPER_STAIRS,
            Material.CUT_COPPER_SLAB,
            Material.EXPOSED_CUT_COPPER_SLAB,
            Material.WEATHERED_CUT_COPPER_SLAB,
            Material.OXIDIZED_CUT_COPPER_SLAB,
            Material.WAXED_COPPER_BLOCK,
            Material.WAXED_EXPOSED_COPPER,
            Material.WAXED_WEATHERED_COPPER,
            Material.WAXED_OXIDIZED_COPPER,
            Material.WAXED_CUT_COPPER,
            Material.WAXED_EXPOSED_CUT_COPPER,
            Material.WAXED_WEATHERED_CUT_COPPER,
            Material.WAXED_OXIDIZED_CUT_COPPER,
            Material.WAXED_CUT_COPPER_STAIRS,
            Material.WAXED_EXPOSED_CUT_COPPER_STAIRS,
            Material.WAXED_WEATHERED_CUT_COPPER_STAIRS,
            Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
            Material.WAXED_CUT_COPPER_SLAB,
            Material.WAXED_EXPOSED_CUT_COPPER_SLAB,
            Material.WAXED_WEATHERED_CUT_COPPER_SLAB,
            Material.WAXED_OXIDIZED_CUT_COPPER_SLAB
    );


    @Override
    public void run() {
        Set<Block> knownPotRegion = new HashSet<>();
        
        // get all items in water
        Set<Item> allItems = new HashSet<>();
        for (World world : CopperEssentials.getInstance().getServer().getWorlds()) {
            Collection<Item> items = world.getEntitiesByClass(Item.class);
            allItems.addAll(items);
        }
        
        // find all slime item stacks
        Set<Item> allSlimeStacks = allItems.stream().filter(stack -> stack.getItemStack().getType() == Material.SLIME_BALL).collect(Collectors.toSet());
        // check slime balls in a pot, try to make slimes
        for (Item slimeStack : allSlimeStacks) {
            Block stackBlock = slimeStack.getLocation().getBlock();
            if(stackBlock.getType() != Material.WATER){
                // not in water, no need for check
                continue;
            }
            if(!knownPotRegion.contains(stackBlock)){
                // check if this is a new pot
                Set<Block> potRegion = PotHelpers.detectPot(POT_MATERIALS, Material.WATER, stackBlock.getLocation().getBlock(), MAX_POT_SIZE);
                knownPotRegion.addAll(potRegion);
            }
            if(knownPotRegion.contains(stackBlock)){
                trySummonSlime(slimeStack);
            }
        }

        // find all slimes, try to grow them
        Set<Slime> allSlimes = new HashSet<>();
        for (World world : CopperEssentials.getInstance().getServer().getWorlds()) {
            Collection<Slime> items = world.getEntitiesByClass(Slime.class);
            allSlimes.addAll(items);
        }
        // check slimes in a pot, try to grow slimes
        for (Slime slime : allSlimes) {
            Block slimeBlock = slime.getLocation().getBlock();
            if(slimeBlock.getType() != Material.WATER){
                // not in water, no need for check
                continue;
            }
            if(!knownPotRegion.contains(slimeBlock)){
                // check if this is a new pot
                Set<Block> potRegion = PotHelpers.detectPot(POT_MATERIALS, Material.WATER, slimeBlock.getLocation().getBlock(), MAX_POT_SIZE);
                knownPotRegion.addAll(potRegion);
            }
            if(knownPotRegion.contains(slimeBlock)){
                tryGrowSlime(slime);
            }
        }
    }

    private void trySummonSlime(Item slimeItem){
        Optional<Item> nearBySugar = slimeItem.getWorld().getNearbyEntitiesByType(Item.class, slimeItem.getLocation(), 2.0)
                .stream().filter(stack -> stack.getItemStack().getType() == Material.SUGAR).findAny();
        
        if(nearBySugar.isPresent()){
            ItemStack sugarStack = nearBySugar.get().getItemStack();
            sugarStack.setAmount(sugarStack.getAmount() - 1);

            ItemStack slimeStack = slimeItem.getItemStack();
            slimeStack.setAmount(slimeStack.getAmount() - 1);

            Location loc = slimeItem.getLocation();
            Slime newSlime = (Slime)loc.getWorld().spawnEntity(loc, EntityType.SLIME, SpawnReason.CUSTOM);
            newSlime.setSize(1);
        }
    }

    private void tryGrowSlime(Slime slime){
        int oldSize = slime.getSize();
        if(oldSize >= MAX_SLIME_SIZE){
            return;
        }

        Optional<Item> nearBySugar = slime.getWorld().getNearbyEntitiesByType(Item.class, slime.getLocation(), 2.0)
                .stream().filter(stack -> stack.getItemStack().getType() == Material.SUGAR).findAny();
        
        if(nearBySugar.isPresent()){
            ItemStack sugarStack = nearBySugar.get().getItemStack();
            sugarStack.setAmount(sugarStack.getAmount() - 1);

            if(rng.nextDouble() < 1.0 / oldSize){
                slime.setSize(oldSize + 1);
            }
        }
    }
}
