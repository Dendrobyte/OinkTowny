package com.redstoneoinkcraft.oinktowny.lootdrops;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LootdropTimer extends BukkitRunnable {

    private int counter;

    public LootdropTimer(int minutes){
        this.counter = minutes*20;
    }

    @Override
    public void run(){
        if(counter == 0){
            if(Bukkit.getServer().getOnlinePlayers().size() >= 1) LootdropManager.getInstance().dropLootChestRandom();
            cancel();
            LootdropManager.getInstance().initializeLootdropTimer();
        } else {
            counter--;
        }
    }
}
