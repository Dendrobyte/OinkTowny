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
        event.setCancelled(true);
        String message = ChatColor.stripColor(event.getMessage());
        if (message.toUpperCase().contains("ADD")) {
            if(message.length() <= 3){
                player.sendMessage(prefix + "Please provide a player name after \'ADD \'");
                return;
            }
            String playerName = message.substring(message.indexOf(" ")+1);
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
            Chest original = lm.getPlayersEditing().get(player);
            boolean success = lm.addPlayerToChest(original, playerId, playerName);
            if(success) {
                if(lm.isDoubleChest(original)){
                    lm.addPlayerToChest(lm.getOtherHalfOfDouble(lm.toDoubleChest(original), original), playerId, playerName);
                }
                player.sendMessage(prefix + playerName + " has been added to your chest!");
            } else {
                player.sendMessage(prefix + "Player was not added!");
            }
            return;
        }
        else if (message.toUpperCase().contains("REMOVE")){
            String playerName = message.substring(message.indexOf(" "));
            Chest original = lm.getPlayersEditing().get(player);
            boolean removed = lm.removePlayerFromChest(original, playerName);
            if(!removed){
                player.sendMessage(prefix + "Player not listed as added to chest, thus not removed.");
            } else { // removed
                if(lm.isDoubleChest(original)) {
                    lm.removePlayerFromChest(lm.getOtherHalfOfDouble(lm.toDoubleChest(original), original), playerName);
                }
                player.sendMessage(prefix + "Removed " + playerName + " from chest!");
            }
            return;
        }
        // TODO: If it's a double chest... Break the whole thing and remove both chests from configuration
        else if (message.equalsIgnoreCase("DELETE")){
            player.sendMessage(prefix + "This command permanently unprivates your chest. To confirm, type " + ChatColor.RED + ChatColor.BOLD + "CONFIRM DELETE");
            return;
        }
        else if (message.equalsIgnoreCase("CONFIRM DELETE")){
            player.sendMessage(prefix + "Unprivating chest...");
            Chest original = lm.getPlayersEditing().get(player);
            Chest otherHalf;
            if(lm.isDoubleChest(original)){
                otherHalf = lm.getOtherHalfOfDouble(lm.toDoubleChest(original), original);
                lm.removeChest(otherHalf); // Other have taken care of in removeChest method

            } else {
                lm.removeChest(original);
            }

            player.sendMessage(prefix + "Chest unprivated. Exiting editor...");
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
            player.sendMessage(prefix + "DELETE - " + ChatColor.GRAY + "Unprivate the chest you just clicked");
            player.sendMessage(prefix + "DONE - " + ChatColor.GRAY + "Leave this edit wizard");
        }
    }

}
