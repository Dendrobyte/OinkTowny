package com.redstoneoinkcraft.oinktowny.listeners;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.economy.TownyTokenManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;

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
        World world = event.getTo().getWorld();
        if(!world.getName().equalsIgnoreCase(Main.getInstance().getWorldName())) return; // Not towny world
        if(world.getName().equalsIgnoreCase(event.getFrom().getWorld().getName())) return;
        Player player = event.getPlayer();
        // Do greeting message
        player.sendMessage(prefix + ChatColor.GOLD + ChatColor.ITALIC + "Welcome to Towny, " + player.getName() + "!");

        // UBI on world teleport
        LocalDate date = LocalDate.now();
        if(date.getDayOfWeek().toString().equalsIgnoreCase("SATURDAY")) {
            List<String> playersPaid = Main.getInstance().getConfig().getStringList("players-paid");
            if (playersPaid.contains(player.getUniqueId().toString())) {
                return; // No way anything else would run either if they had never joined
            }
            int tokenAmount = Main.getInstance().getConfig().getInt("ubi-tokens");
            // Give player tokens
            player.getInventory().addItem(TownyTokenManager.getInstance().createToken(2));
            player.sendMessage(prefix + ChatColor.GREEN + ChatColor.ITALIC + "Enjoy your bimonthly " + tokenAmount + " token income, on us!");
            playersPaid.add(player.getUniqueId().toString());
            Main.getInstance().getConfig().set("players-paid", playersPaid);
            Main.getInstance().saveConfig();
            Main.getInstance().reloadConfig();
            GiveTokensTimer gtt = new GiveTokensTimer(player, tokenAmount);
            gtt.runTaskTimer(Main.getInstance(), 20L, 0L);
        }

        // Any things to set up upon teleportation goes here.
        List<String> players = Main.getInstance().getConfig().getStringList("players-joined");
        if(players.contains(player.getUniqueId().toString())) return;
        int startAmount = Main.getInstance().getConfig().getInt("starting-tokens");
        player.sendMessage(prefix + ChatColor.GREEN + ChatColor.ITALIC + "Here are some tokens to start you off!");
        GiveTokensTimer gtt = new GiveTokensTimer(player, startAmount);
        gtt.runTaskTimer(Main.getInstance(), 20L, 0L);
        players.add(player.getUniqueId().toString());
        Main.getInstance().getConfig().set("players-joined", players);
        Main.getInstance().saveConfig();

    }
}

class GiveTokensTimer extends BukkitRunnable{

    private Player player;
    private int amount;

    public GiveTokensTimer(Player player, int amount){
        this.player = player;
        this.amount = amount;
    }

    @Override
    public void run() {
        player.getInventory().addItem(TownyTokenManager.getInstance().createToken(amount));
        cancel();
    }
}
