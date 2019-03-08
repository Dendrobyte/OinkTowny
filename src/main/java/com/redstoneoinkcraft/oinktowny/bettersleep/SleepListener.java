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
    String worldName = "world"; //Main.getInstance().getConfig().getString("world-name");
    String prefix = Main.getInstance().getPrefix();

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event){
        if(!event.getBed().getWorld().getName().equalsIgnoreCase(worldName)) return;
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
            sm.removePlayerSleeping(event.getPlayer());
            for(Player player : sm.getAsleepPlayers()){
                player.sendMessage(prefix + ChatColor.RED + "Someone has left their bed!");
            }
        }
    }

}
