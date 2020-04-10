package com.redstoneoinkcraft.oinktowny.regions;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * OinkTowny created/started by markb (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class PreventTntAndCreepersListener implements Listener {

    @EventHandler
    public void onTntExplodeInClaim(EntityExplodeEvent event){
        if(event.getEntityType() == EntityType.PRIMED_TNT){
            Entity tnt = event.getEntity();
            if(Main.getInstance().isTownyWorld(tnt.getWorld().getName())){
                Chunk chunk = tnt.getLocation().getChunk();
                if(RegionsManager.getInstance().chunkIsClaimed(chunk)){
                    event.setCancelled(true);
                }
            }
        }

        // Prevent all Creeper explosions
        if(event.getEntityType() == EntityType.CREEPER){
            Entity creeper = event.getEntity();
            if(Main.getInstance().isTownyWorld(creeper.getWorld().getName())){
                event.setCancelled(true);
            }
        }
    }
}
