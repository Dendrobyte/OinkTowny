package com.redstoneoinkcraft.oinktowny.arenapvp;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaPlayerQuitListener implements Listener {

    ArenaPVPManager apm = ArenaPVPManager.getInstance();

    @EventHandler
    public void arenaPlayerQuitServer(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(!apm.isPlayerInArena(player)) return;
        apm.prematurelyEndArena(player, "Bye bye, you lose :(");
    }

    @EventHandler
    public void arenaPlayerTeleportOut(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        if(!apm.isPlayerInArena(player)) return;
        apm.prematurelyEndArena(player, "You teleported away, thus forfeiting the match!");
    }

    @EventHandler
    public void arenaPlayerActuallyDies(PlayerDeathEvent event){
        Player player = event.getEntity();
        if(!apm.isPlayerInArena(player)) return;
        apm.prematurelyEndArena(player, "How did you manage... whatever, you died, so you lost.");
    }
}
