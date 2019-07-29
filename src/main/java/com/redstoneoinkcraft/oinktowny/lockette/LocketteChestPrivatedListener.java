package com.redstoneoinkcraft.oinktowny.lockette;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LocketteChestPrivatedListener implements Listener {

    LocketteManager lm = LocketteManager.getInstance();
    String prefix = Main.getInstance().getPrefix();

    @EventHandler
    public void onPlayerPrivatesChest(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Material.CHEST) return;
        Player player = event.getPlayer();
        if(!Main.getInstance().isTownyWorld(player.getWorld().getName())) return;
        Chest chest = (Chest)event.getClickedBlock().getState();
        if(lm.isLocketteChest(chest)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (lm.playerCanAccessChest(player, chest)) {
                    if (lm.playerOwnsChest(player, chest)) {
                        if(!player.isSneaking()) return;
                        // If they own it, tell them how to add people
                        player.sendMessage(prefix + "If you would like to add players to this chest, remove them, or delete it," + ChatColor.YELLOW + ChatColor.BOLD + " SHIFT + LEFT CLICK");
                        return;
                    }
                    // Let them open it
                    return;
                } else {
                    event.setCancelled(true);
                    player.sendMessage(prefix + ChatColor.RED + "This chest is privately owned, and you are not added!");
                    return;
                }
            }
            else if (event.getAction() == Action.LEFT_CLICK_BLOCK){
                if(lm.playerOwnsChest(player, chest)){
                    if(!player.isSneaking()) return;
                    player.sendMessage(prefix + "Now entering the chest editing wizard...");
                    lm.initiatePlayerEditing(player, chest);
                    return;
                } else {
                    // Nothing to do, since only player who owns the chest can add people
                    return;
                }
            }
        } else { //!lm.isLocketteChest(chest)
            if(lm.chestIsActive(chest)){
                if(!player.equals(lm.activeChestPlayer(chest))){
                    if(!player.isSneaking()) return;
                    player.sendMessage(prefix + "You did not place this chest!");
                    return;
                }
                lm.makeNewPrivateChest(chest, player);
                player.sendMessage(prefix + "Chest has been privated! To edit it, " + ChatColor.YELLOW + ChatColor.BOLD + "SHIFT + LEFT CLICK");
                return;
            }
        }
    }

}
