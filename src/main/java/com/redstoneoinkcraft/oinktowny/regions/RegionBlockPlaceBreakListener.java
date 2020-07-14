package com.redstoneoinkcraft.oinktowny.regions;

import com.google.gson.stream.JsonToken;
import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.clans.ClanManager;
import com.redstoneoinkcraft.oinktowny.clans.ClanObj;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RegionBlockPlaceBreakListener implements Listener {

    Main mainInstance = Main.getInstance();
    String prefix = mainInstance.getPrefix();

    RegionsManager rm = RegionsManager.getInstance();
    ClanManager cm = ClanManager.getInstance();

    @EventHandler
    public void onBreakInRegion(BlockBreakEvent event){
        if(!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase(mainInstance.getWorldName())){
            return;
        }
        Player player = event.getPlayer();
        if(rm.bypassEnabled(player)){
            return;
        }
        if(!rm.canPlayerEdit(event.getBlock().getChunk(), player)){
            event.setCancelled(true);
            player.sendMessage(prefix + "You are not in the proper clan to edit this claim.");
        }
    }

    @EventHandler
    public void onPlaceInRegion(BlockPlaceEvent event){
        if(!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase(mainInstance.getWorldName())){
            return;
        }
        Player player = event.getPlayer();
        if(rm.bypassEnabled(player)){
            return;
        }
        if(!rm.canPlayerEdit(event.getBlock().getLocation().getChunk(), player)){
            event.setCancelled(true);
            player.sendMessage(prefix + "You are not in the proper clan to edit this claim.");
        }
    }

    @EventHandler
    public void onPlayerOpenContainer(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getState() instanceof InventoryHolder) {
            Player player = event.getPlayer();
            if(rm.bypassEnabled(player)){
                return;
            }
            if(!rm.canPlayerEdit(event.getClickedBlock().getChunk(), player)){
                event.setCancelled(true);
                player.sendMessage(prefix + "You can not access containers in this claim.");
            }
        }
    }

}
