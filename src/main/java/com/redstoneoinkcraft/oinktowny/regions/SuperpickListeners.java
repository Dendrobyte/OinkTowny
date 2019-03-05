package com.redstoneoinkcraft.oinktowny.regions;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class SuperpickListeners implements Listener {

    RegionsManager rm = RegionsManager.getInstance();
    String prefix = Main.getInstance().getPrefix();

    @EventHandler // When they leave, remove them from the superpick list
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(rm.isSuperpick(player)){
            rm.toggleSuperpick(player);
        }
    }

    @EventHandler // Remove them from the superpick list if they teleport to a different world
    public void onPlayerTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        if(!rm.isSuperpick(player)) return;
        if(event.getFrom().getWorld() != event.getTo().getWorld()){
            rm.toggleSuperpick(player);
            player.sendMessage(prefix + "(You teleported into a different world)");
        }
    }

    @EventHandler // Immediately break block if activated and if holding pick
    public void onBlockHit(PlayerInteractEvent event){
        Player player = event.getPlayer();
        // if(!event.getClickedBlock().getWorld().getName().equals(configpath)) return;
        // TODO: If player is in a region, deny the action.
        if(!rm.isSuperpick(player)) return;
        if(event.getAction() != Action.LEFT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) return;
        Material blockType = event.getClickedBlock().getType();
        if(blockType == Material.BEDROCK || blockType == Material.OBSIDIAN) return;
        ItemStack pick = event.getItem();
        if(!pick.getType().toString().contains("PICKAXE")) return;

        event.getClickedBlock().breakNaturally();
    }

}
