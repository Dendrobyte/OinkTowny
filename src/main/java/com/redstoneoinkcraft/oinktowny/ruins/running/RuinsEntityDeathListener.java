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

    // TODO: Change this so that remaining entities is 0 versus killing the final spawned entity

    @EventHandler
    public void onEntityDeathInRuins(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if(!rm.getFinalMobInLevel().containsKey(entity)) return;
        Player player = rm.getFinalMobInLevel().get(entity);
        rm.advancePlayer(player); // Doesn't matter if it is the player that kills it or not.
        rm.getFinalMobInLevel().remove(entity);
    }

}
