package com.redstoneoinkcraft.oinktowny.lockette;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

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
    public void onPlayerPrivatesChest(PlayerInteractEvent event) {
        try {
            if (event.getClickedBlock().getType() != Material.CHEST) return;
        } catch (NullPointerException e){
            return;
        }
        Player player = event.getPlayer();
        if(!Main.getInstance().isTownyWorld(player.getWorld().getName())) return;
        Chest chest = (Chest)event.getClickedBlock().getState();
        boolean isDouble = false;
        DoubleChest doubleChest = null;
        Chest otherHalf = null;
        if(lm.isDoubleChest(chest)) {
            System.out.println("Double chest!");
            isDouble = true;
            doubleChest = (DoubleChest)chest;
            /* Should this not work
             * I suppose trying to write a method that compares the contents of each half of the chest could work.
             * I can either check the configuration for the half not used, assuming they didn't click on a Lockette chest,
             * or I can store the second half upon placement. The latter one is ideal.
             * OR delete the existing solo chest and only store the double chest (but then don't I have to re-write everything?)
             * Dealing with each chest singly is the best way to go, I just want to make sure I can add
             */
            otherHalf = lm.getOtherHalfOfDouble(doubleChest, chest);
        }
        else {
            System.out.println("Not a double chest...");
        }
        System.out.println("Inv size: " + chest.getInventory().getSize());
        // If (chest.InventoryHolder instanceof chest)...
        System.out.println("Lockette chest? " + lm.isLocketteChest(chest));
        System.out.println("The chest: " + chest.toString());
        if(lm.isLocketteChest(chest)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(event.getHand() == EquipmentSlot.OFF_HAND) return;
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
                    event.setCancelled(true);
                    return;
                } else {
                    // Nothing to do, since only player who owns the chest can add people
                    return;
                }
            }
        } else { //!lm.isLocketteChest(chest)
            if(!player.isSneaking()) return;
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (lm.chestIsActive(chest)) {
                    if (player.equals(lm.activeChestPlayer(chest))) {
                        if(isDouble){
                            if(lm.isLocketteChest(otherHalf)){
                                if(!lm.playerOwnsChest(player, otherHalf)){
                                    player.sendMessage(prefix + "You don't own the other half of this chest!");
                                    return;
                                }
                                lm.makeNewPrivateChest(chest, player, true, otherHalf);
                                player.sendMessage(prefix + "Double chest added to existing privated chest.");
                                return;
                            } else {
                                lm.makeNewPrivateChest(otherHalf, player, false, chest); // Make the first half a private chest with no double
                                // If there isn't a logged half already, it would return an error.
                                lm.makeNewPrivateChest(chest, player, true, otherHalf); // Make the second half of the chest privated
                            }

                        } else {
                            lm.makeNewPrivateChest(chest, player, false, otherHalf);
                        }
                        player.sendMessage(prefix + "Chest has been privated! To edit it, " + ChatColor.YELLOW + ChatColor.BOLD + "SHIFT + LEFT CLICK");
                        lm.getActiveTimers().get(player).cancel();
                        lm.removeActiveChest(chest);
                        event.setCancelled(true);
                        return;
                    } else {
                        System.out.println("4");
                        player.sendMessage(prefix + "You did not place this chest!");
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

}
