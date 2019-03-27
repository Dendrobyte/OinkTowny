package com.redstoneoinkcraft.oinktowny.arenapvp;

import com.redstoneoinkcraft.oinktowny.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaClickListener implements Listener {

    ArenaPVPManager apm = ArenaPVPManager.getInstance();
    ItemStack creationWand = apm.getCreationWand();
    String prefix = Main.getInstance().getPrefix();

    @EventHandler // Arena creation
    public void checkArenaCreation(PlayerInteractEvent event) {
        ItemStack itemInHand = event.getItem();
        if(event.getClickedBlock() == null) return;
        if(itemInHand == null || !itemInHand.equals(creationWand)) return;
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();
        ArenaCreationStage playerStage = apm.getPlayerStage(player);
        if(playerStage == null){
            player.sendMessage(prefix + "You aren't in arena creation mode! (Removing wand...)");
            player.getInventory().getItemInMainHand().setAmount(0);
            event.setCancelled(true);
            return;
        }
        Location blockLoc = event.getClickedBlock().getLocation();
        Location returnLoc = new Location(blockLoc.getWorld(), blockLoc.getBlockX(), blockLoc.getBlockY()+1, blockLoc.getBlockZ());
        if(playerStage == ArenaCreationStage.ARENA_LOC){
            apm.getPlayerCreationArena(player).setArenaLoc(returnLoc);
            apm.setPlayerStage(player, ArenaCreationStage.LOBBY);
            player.sendMessage(prefix + ChatColor.GREEN +  "Please select the " + ChatColor.BOLD + "arena lobby location.");
        }
        if(playerStage == ArenaCreationStage.LOBBY){
            apm.getPlayerCreationArena(player).setLobby(returnLoc);
            apm.setPlayerStage(player, ArenaCreationStage.SPAWN_ONE);
            player.sendMessage(prefix + ChatColor.GREEN + "Please select the " + ChatColor.BOLD + "first spawn location.");
        }
        if(playerStage == ArenaCreationStage.SPAWN_ONE){
            apm.getPlayerCreationArena(player).setSpawn_one(returnLoc);
            apm.setPlayerStage(player, ArenaCreationStage.SPAWN_TWO);
            player.sendMessage(prefix + ChatColor.GREEN + "Please select the " + ChatColor.BOLD + "second spawn location.");
        }
        if(playerStage == ArenaCreationStage.SPAWN_TWO){
            apm.getPlayerCreationArena(player).setSpawn_two(returnLoc);
            apm.finishCreation(player);
        }
        event.setCancelled(true);
    }

}
