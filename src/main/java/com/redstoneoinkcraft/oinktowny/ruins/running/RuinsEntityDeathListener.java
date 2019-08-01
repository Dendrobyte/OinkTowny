package com.redstoneoinkcraft.oinktowny.ruins.running;

import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsEntityDeathListener implements Listener {

    RuinsManager rm = RuinsManager.getInstance();

    @EventHandler
    public void onEntityDeathInRuins(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if(!rm.getSpawnedEntitiesToPlayer().containsKey(entity)) return;
        Player player = rm.getSpawnedEntitiesToPlayer().get(entity);
        rm.getSpawnedEntitiesToPlayer().remove(entity);
        rm.getSpawnedEntities().get(player).remove(entity);
        if(rm.getSpawnedEntities().get(player).size() == 0){
            rm.advancePlayer(player);
        }
    }

}
