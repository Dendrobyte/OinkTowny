package com.redstoneoinkcraft.oinktowny.lockette;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LocketteChatListener implements Listener {

    private LocketteManager lm = LocketteManager.getInstance();
    private String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LocketteEditor" + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW + " ";

    @EventHandler
    public void playerEditingChest(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if(!lm.isPlayerEditingChest(player)) return;
        System.out.println("Editing!");
        event.setCancelled(true);
        String message = ChatColor.stripColor(event.getMessage());
        if (message.toUpperCase().contains("ADD")) {
            if(message.length() <= 3){
                player.sendMessage(prefix + "Please provide a player name after \'ADD \'");
                return;
            }
            String playerName = message.substring(message.indexOf(" "));
            boolean playerOnline = false;
            UUID playerId = null;
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                if(onlinePlayer.getName().equalsIgnoreCase(playerName)){
                    playerOnline = true;
                    playerId = onlinePlayer.getUniqueId();
                }
            }
            if(!playerOnline){
                player.sendMessage(prefix + "Please make sure the player you are trying to add is online.");
                return;
            }
            boolean success = lm.addPlayerToChest(lm.getPlayersEditing().get(player), playerId, playerName);
            if(success) {
                player.sendMessage(prefix + playerName + " has been added to your chest!");
            } else {
                player.sendMessage(prefix + "Player was not added!");
            }
            return;
        }
        else if (message.toUpperCase().contains("REMOVE")){
            String playerName = message.substring(message.indexOf(" "));
            boolean removed = lm.removePlayerFromChest(lm.getPlayersEditing().get(player), playerName);
            if(!removed){
                player.sendMessage(prefix + "Player not listed as added to chest, thus not removed.");
            } else { // removed
                player.sendMessage(prefix + "Removed " + playerName + " from chest!");
            }
            return;
        }
        else if (message.equalsIgnoreCase("DELETE")){
            player.sendMessage(prefix + "This command deletes your currently edited chest (items will be dropped). To confirm, type " + ChatColor.RED + ChatColor.BOLD + "CONFIRM DELETE");
            return;
        }
        else if (message.equalsIgnoreCase("CONFIRM DELETE")){
            player.sendMessage(prefix + "Breaking chest...");
            int amt = 1;
            lm.getPlayersEditing().get(player).getBlock().breakNaturally();
            InventoryHolder ih = lm.getPlayersEditing().get(player).getInventory().getHolder();
            if(ih instanceof DoubleChest){
                amt = 2;
            }
            Bukkit.getWorld(player.getWorld().getName()).dropItem(player.getLocation(), new ItemStack(Material.CHEST, amt));
            lm.removeChest(lm.getPlayersEditing().get(player));
            player.sendMessage(prefix + "Chest destroyed. Exiting editor...");
            lm.getPlayersEditing().remove(player);
            return;
        }
        else if (message.equalsIgnoreCase("DONE")){
            player.sendMessage(prefix + "Exiting editor...");
            lm.getPlayersEditing().remove(player);
            return;
        } else {
            player.sendMessage(prefix + "ADD <name> - " + ChatColor.GRAY + "Add a player to the chest you just clicked");
            player.sendMessage(prefix + "REMOVE <name> - " + ChatColor.GRAY + "Remove a player from the chest you just clicked");
            player.sendMessage(prefix + "DELETE - " + ChatColor.GRAY + "Remove the chest you just clicked");
            player.sendMessage(prefix + "DONE - " + ChatColor.GRAY + "Leave this edit wizard");
        }
    }

}
