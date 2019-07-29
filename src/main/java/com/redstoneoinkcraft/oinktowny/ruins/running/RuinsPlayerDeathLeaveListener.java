package com.redstoneoinkcraft.oinktowny.ruins.running;

import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsPlayerDeathLeaveListener implements Listener {

    RuinsManager rm = RuinsManager.getInstance();

    @EventHandler
    public void playerDeathInRuins(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (rm.isPlayerCreatingRuins(player)) {
            removePlayerCreating(player);
        }
        if(rm.getActivePlayers().containsKey(player)){
            removePlayerPlaying(player);
            event.setKeepInventory(true);
        }
    }

    @EventHandler
    public void playerLeaveServerInRuins(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (rm.isPlayerCreatingRuins(player)) {
            removePlayerCreating(player);
        }
        if(rm.getActivePlayers().containsKey(player)){
            removePlayerPlaying(player);
        }
    }

    private void removePlayerCreating(Player player){
        rm.removePlayerFromCreation(player);
    }

    private void removePlayerPlaying(Player player){
        rm.kickPlayerFromRuins(player);
        player.sendMessage(rm.getRuinsPrefix() + "Aww, better luck next time!");
    }

}
