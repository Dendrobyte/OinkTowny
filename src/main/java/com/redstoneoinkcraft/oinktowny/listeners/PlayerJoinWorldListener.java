package com.redstoneoinkcraft.oinktowny.listeners;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 8/17/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class PlayerJoinWorldListener implements Listener {

    String prefix = Main.getInstance().getPrefix();

    @EventHandler
    public void playerJoinTownyWorld(PlayerTeleportEvent event){
        // Do greeting message

        // Any things to set up upon teleportation goes here.
    }
}
