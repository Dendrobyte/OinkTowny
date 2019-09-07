package com.redstoneoinkcraft.oinktowny.lockette;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LocketteChestPlaceBreakListener implements Listener {

    String prefix = Main.getInstance().getPrefix();
    LocketteManager lm = LocketteManager.getInstance();

    @EventHandler
    public void onChestPlace(BlockPlaceEvent event){
        if(event.getBlockPlaced().getType() != Material.CHEST) return;
        Player player = event.getPlayer();
        Chest chest = (Chest)event.getBlock().getState();
        if(!Main.getInstance().isTownyWorld(player.getWorld().getName())) return;
        // if(event.getBlockAgainst().getType().equals(Material.CHEST)) return;
        if (lm.getActiveTimers().containsKey(player)) {
            lm.getActiveTimers().get(player).cancel();
            player.sendMessage(prefix + ChatColor.GRAY + "Previous Lockette timer canceled.");
        }
        if(lm.sendInfoMessage(player)) {
            player.sendMessage(prefix + ChatColor.AQUA + "To private this chest, " + ChatColor.YELLOW + ChatColor.BOLD + "SNEAK + RIGHT CLICK");
            lm.getSendInfoMessagePlayers().add(player);
        }

        lm.addActiveChest(chest, player);
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event){
        if(event.getBlock().getType() != Material.CHEST) return;
        System.out.println("Chest broken!");
        Player player = event.getPlayer();
        /* if(player.isOp()){ // Allow ops to break chests for extenuating circumstances. Permission later? -- TODO: Removed for testing server
            return;
        } */
        Chest chest = (Chest)event.getBlock().getState();
        if(!lm.isLocketteChest(chest)){
            if(lm.getActiveTimers().containsValue(chest)){
                player.sendMessage(prefix + "Chest timer canceled.");
                lm.getActiveTimers().get(player).cancel();
                return;
            }
            return;
        }
        if(!lm.playerOwnsChest(player, chest)) {
            // Don't need to worry about world, since technically no private chests can exist in other worlds
            player.sendMessage(prefix + ChatColor.RED + "This chest is privately owned. Only the owner can break it.");
            event.setCancelled(true);
            return;
        } else {
            player.sendMessage(prefix + "This chest is privated. Unprivate to break it with, " + ChatColor.YELLOW + ChatColor.BOLD + "SNEAK + LEFT CLICK");
            event.setCancelled(true);
        }

    }
}
