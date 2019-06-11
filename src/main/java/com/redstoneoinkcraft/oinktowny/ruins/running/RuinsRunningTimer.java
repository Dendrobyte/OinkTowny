package com.redstoneoinkcraft.oinktowny.ruins.running;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import com.redstoneoinkcraft.oinktowny.ruins.RuinsObjLevel;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsRunningTimer extends BukkitRunnable {

    RuinsManager rm = RuinsManager.getInstance();
    String prefix = rm.getRuinsPrefix();

    int seconds;
    String message;
    Player player;
    Location nextLocation;

    public RuinsRunningTimer(String message, int seconds, Player player, Location nextLocation){
        this.message = message;
        this.seconds = seconds;
        this.player = player;
        this.nextLocation = nextLocation;
    }

    @Override
    public void run(){
        if(seconds == 3){
            player.sendMessage(prefix + "Teleporting in 3 seconds!");
        }
        else if(seconds == 0){
            player.sendMessage(prefix + message);
            player.teleport(nextLocation);
            RuinsObjLevel level = rm.getActivePlayers().get(player).getPlayerCurrentLevel().get(player);
            RuinsMobspawningTimer rmt = new RuinsMobspawningTimer(level.getMonsters(), level.getSpawnLocation(), player);
            rmt.runTaskTimer(Main.getInstance(), 0, 20L);
            cancel();
        }
        seconds--;
    }

}
