package com.redstoneoinkcraft.oinktowny.ruins.running;

import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsEndingTimer extends BukkitRunnable {

    private Player player;
    private Location location, itemClearLoc;
    private int seconds = 20;

    public RuinsEndingTimer(Player player, Location ruinsLobbyLoc, Location itemClearLoc){
        this.player = player;
        this.location = ruinsLobbyLoc;
        this.itemClearLoc = itemClearLoc;
    }

    @Override
    public void run() {
        if(seconds == 0){
            RuinsManager.getInstance().kickPlayerFromRuins(player);
            player.teleport(location);
            player.sendMessage(RuinsManager.getInstance().getRuinsPrefix() + "Thanks for fighting through!");

            for(Entity entity : itemClearLoc.getChunk().getEntities()){
                entity.remove();
            }
            cancel();
        } else {
            seconds--;
        }
    }
}
