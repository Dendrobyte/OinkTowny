package com.redstoneoinkcraft.oinktowny.lootdrops;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LootdropExpirationTimer extends BukkitRunnable {

    Chest chest;
    int seconds;

    LootdropManager lm = LootdropManager.getInstance();

    public LootdropExpirationTimer(Chest chest, int seconds){
        this.chest = chest;
        this.seconds = seconds;
    }

    @Override
    public void run(){
        if(seconds == 0){
            if(!lm.getExistingLootcrates().contains(chest)){ // Another timer was created when lootdrop was found
                cancel();
                return;
            }
            if(chest.getLocation().getBlock().getType().equals(Material.CHEST)){
                chest.getWorld().spawnParticle(Particle.SMOKE_LARGE, chest.getLocation(), 20);
                chest.getWorld().playSound(chest.getLocation(), Sound.BLOCK_ANVIL_BREAK, 3, 5);
                chest.getLocation().getBlock().setType(Material.AIR);
            }
            lm.getExistingLootcrates().remove(chest);
            lm.setLootcrateFound(chest, false);
            cancel();
        } else {
            seconds--;
        }
    }

}
