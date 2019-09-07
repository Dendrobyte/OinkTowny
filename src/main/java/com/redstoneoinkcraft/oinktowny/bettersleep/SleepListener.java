package com.redstoneoinkcraft.oinktowny.bettersleep;

import com.redstoneoinkcraft.oinktowny.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class SleepListener implements Listener {

    SleepManager sm = SleepManager.getInstance();
    String worldName = Main.getInstance().getWorldName();
    String prefix = Main.getInstance().getPrefix();

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event){
        // TODO: If you sleep while monsters nearby it counts 1 person as sleeping, so you can spam the bed until it turns to day
        if(!event.getBed().getWorld().getName().equalsIgnoreCase(worldName)) return;
        // if(event.getBed().getWorld().getTime() > 12000 && event.getBed().getWorld().getTime() < 23000) return; // Assuming the player can get in a bed, fine by me
        if(event.isCancelled()){ // For nearby monsters cancellations, or if it's day time.
            return;
        } // If it's not canceled, they successfully get in bed
        Player player = event.getPlayer();
        sm.addPlayerSleeping(player);

        // Update player count
        int totalNum = sm.setTotalPlayers(Bukkit.getWorld(worldName).getPlayers().size());
        int threshold = totalNum/2 +1;

        for(Player sleepingPlayer : sm.getAsleepPlayers()){
            sleepingPlayer.sendMessage(prefix + ChatColor.GREEN + player.getName() + " has gone to bed. (" + sm.getPlayersSleeping() + "/" + threshold + ")");
        }
        if(!sm.getAsleepPlayers().contains(player)) sm.getAsleepPlayers().add(player);
        if(sm.getPlayersSleeping() >= threshold){
            event.getPlayer().getWorld().setTime(6000);
            event.getPlayer().getWorld().setTime(23000);
            for(Player sleepingPlayer : sm.getAsleepPlayers()){
                sleepingPlayer.sendMessage(prefix + ChatColor.GOLD + "Rise and shine, it's time to get to work!");
            }
            sm.getAsleepPlayers().clear();
            sm.resetPlayerSleeping();
        }
    }

    @EventHandler
    public void onPlayerLeaveBed(PlayerBedLeaveEvent event){
        if(!event.getBed().getWorld().getName().equalsIgnoreCase(worldName)) return;
        if(sm.getAsleepPlayers().contains(event.getPlayer())){
            for(Player player : sm.getAsleepPlayers()){
                player.sendMessage(prefix + ChatColor.RED + "Someone has left their bed!");
            }
            sm.removePlayerSleeping(event.getPlayer());
        }
    }

}
