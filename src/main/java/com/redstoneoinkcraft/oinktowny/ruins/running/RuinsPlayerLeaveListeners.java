package com.redstoneoinkcraft.oinktowny.ruins.running;

import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsPlayerLeaveListeners implements Listener {

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

    @EventHandler
    public void playerTeleportInRuins(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        if(rm.getActivePlayers().containsKey(player)){
            if(!event.getFrom().getWorld().equals(event.getTo().getWorld())){
                event.setCancelled(true);
                player.sendMessage(rm.getRuinsPrefix() + "You can't teleport away! Type /leave if you want to leave.");
            }
            // TODO: Add player states amongst ruins so I can cancel outside teleportation events.
            // Right now, they need to be able to teleport to the next levels...
        }
    }

    @EventHandler
    public void playerCommandInRuins(PlayerCommandPreprocessEvent event){
        String command = event.getMessage();
        Player player = event.getPlayer();
        if(!rm.getActivePlayers().containsKey(player)) return;
        if(command.equalsIgnoreCase("leave")){
            player.sendMessage(rm.getRuinsPrefix() + ChatColor.RED + "Leaving ruins...");
            rm.kickPlayerFromRuins(player);
        } else {
            player.sendMessage(rm.getRuinsPrefix() + "No using commands while in ruins. Type " + ChatColor.RED + "/leave " + ChatColor.getLastColors(rm.getRuinsPrefix()) + "to leave.");
        }
        event.setCancelled(true);
    }

    private void removePlayerCreating(Player player){
        rm.removePlayerFromCreation(player);
    }

    private void removePlayerPlaying(Player player){
        rm.kickPlayerFromRuins(player);
        player.sendMessage(rm.getRuinsPrefix() + "Aww, better luck next time!");
    }

}
