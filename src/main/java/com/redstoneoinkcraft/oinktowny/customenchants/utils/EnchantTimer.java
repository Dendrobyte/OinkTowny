package com.redstoneoinkcraft.oinktowny.customenchants.utils;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 * Just a generic timer to use for various enchantment things.
 */
public class EnchantTimer extends BukkitRunnable {

    private int counter;
    private Entity ent;

    // Constructor for glow strike
    public EnchantTimer(int counter, Entity glowingEnt){
        this.counter = counter;
        this.ent = glowingEnt;
    }

    @Override
    public void run(){
        if(counter == 0){
            // For glow strike
            if(ent != null) {
                ent.setGlowing(false);
            }
            cancel();
        }
        counter--;
    }
}
