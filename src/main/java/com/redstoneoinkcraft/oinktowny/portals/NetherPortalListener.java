package com.redstoneoinkcraft.oinktowny.portals;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.util.Vector;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class NetherPortalListener implements Listener {

    @EventHandler
    public void onPlayerPortalTeleport(PlayerPortalEvent event){
        Player player = event.getPlayer();
        if(player.getWorld().getName().equalsIgnoreCase(Main.getInstance().getWorldName())){
            event.setCancelled(true);
            try {
                player.sendMessage(Main.getInstance().getPrefix() + "Teleporting to the towny nether hub...");
                player.teleport(Bukkit.getServer().getWorld(Main.getInstance().getConfig().getString("world-nether")).getSpawnLocation());
            } catch (NullPointerException e){
                player.sendMessage(Main.getInstance().getPrefix() + ChatColor.RED + "There seems to be a problem with the nether world configuration. Please contact an admin!");
            }
            return;
        }
        else if(player.getWorld().getName().equalsIgnoreCase(Main.getInstance().getConfig().getString("world-nether"))){
            event.setCancelled(true);
            player.sendMessage(Main.getInstance().getPrefix() + "Teleporting back to towny spawn...");
            player.teleport(Bukkit.getServer().getWorld(Main.getInstance().getWorldName()).getSpawnLocation());
            return;
        }



    }

}
