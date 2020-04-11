package com.redstoneoinkcraft.oinktowny.customenchants.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * OinkTowny created/started by markb (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class CustomWolfDeathListener implements Listener {

    @EventHandler
    public void onDogTamerWolfDeath(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Wolf){
            Wolf wolf = (Wolf) event.getEntity();
            if(!wolf.isTamed()) return;
            if(wolf.getHealth() - event.getDamage() <= 0){
                String name = wolf.getCustomName();
                if(ChatColor.stripColor(name).contains("Summoned Wolf")){
                    wolf.setTamed(false);
                    wolf.setCustomName(null);
                }
            }
        }
    }

}
