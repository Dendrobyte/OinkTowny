package com.redstoneoinkcraft.oinktowny.portals;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class PortalListener implements Listener {

    /* THIS IS NO LONGER IN USE!
     * There should be some default way to manage portal usage outside of this plugin.
     */

    Main mainInstance = Main.getInstance();
    String prefix = mainInstance.getPrefix();

    @EventHandler
    public void onPlayerPortalTeleport(PlayerPortalEvent event){
        Player player = event.getPlayer();
        if(!mainInstance.isTownyWorld(player.getWorld().getName())) return;
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (player.getWorld().getName().equalsIgnoreCase(mainInstance.getWorldName())) {
                event.setCancelled(true);
                try {
                    player.sendMessage(prefix + "Teleporting to the towny nether hub...");
                    player.teleport(Bukkit.getServer().getWorld(mainInstance.getConfig().getString("world-nether")).getSpawnLocation());
                } catch (NullPointerException e) {
                    player.sendMessage(prefix + ChatColor.RED + "There seems to be a problem with the nether world configuration. Please contact an admin!");
                }
                return;
            } else if (player.getWorld().getName().equalsIgnoreCase(mainInstance.getConfig().getString("world-nether"))) {
                event.setCancelled(true);
                player.sendMessage(prefix + "Teleporting back to the towny spawn...");
                player.teleport(Bukkit.getServer().getWorld(mainInstance.getWorldName()).getSpawnLocation());
                return;
            }
        }

        if(event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL || event.getCause() == PlayerTeleportEvent.TeleportCause.END_GATEWAY){
            if(player.getWorld().getName().equalsIgnoreCase(mainInstance.getWorldName())){
                event.setCancelled(true);
                try {
                    player.sendMessage(prefix + "Teleporting to the end world hub...");
                    player.teleport(Bukkit.getServer().getWorld(mainInstance.getConfig().getString("world-end")).getSpawnLocation());
                } catch (NullPointerException e){
                    player.sendMessage(prefix + ChatColor.RED + "There seems to be a problem with the end world configuration. Please contact an admin!");
                }
                return;
            } else if (player.getWorld().getName().equalsIgnoreCase(mainInstance.getConfig().getString("world-end"))){
                event.setCancelled(true);
                player.sendMessage(prefix + "Teleporting back to the towny spawn...");
                player.teleport(Bukkit.getServer().getWorld(mainInstance.getWorldName()).getSpawnLocation());
                return;
            }
        }


    }

}
