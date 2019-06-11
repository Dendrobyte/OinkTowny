package com.redstoneoinkcraft.oinktowny.ruins.running;

import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
public class RuinsSignClickListener implements Listener {

    RuinsManager rm = RuinsManager.getInstance();
    String prefix = rm.getRuinsPrefix();
    String tag = "[Ruins]";

    @EventHandler
    public void onPlayerJoinRuins(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) return; // Ensure it's a right click
        if(!event.getHand().equals(EquipmentSlot.HAND)) return;
        Block clickedBlock = event.getClickedBlock();
        if(!clickedBlock.getType().toString().contains("WALL_SIGN")) return; // Ensure it's a wall sign

        Sign wallSign = (Sign) clickedBlock.getState();
        if(!ChatColor.stripColor(wallSign.getLine(0)).equals(tag)) return; // Ensure the sign has the proper tag on top - Defined at the top of the class
        Player player = event.getPlayer();
        String ruinsName = wallSign.getLine(1);
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(rm.isPlayerCreatingRuins(player)) return;
            event.setCancelled(true);
        }
        if(rm.getActivePlayers().keySet().contains(player)) {
            player.sendMessage(prefix + "You're already in ruins!");
            return;
        }
        rm.initiatePlayerInRuins(player, ruinsName);
    }

}
