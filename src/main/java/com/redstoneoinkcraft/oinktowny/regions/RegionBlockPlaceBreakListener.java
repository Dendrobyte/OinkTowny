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
        Player player = event.getPlayer();
        if(rm.bypassEnabled(player)){
            return;
        }
        if(!canPlayerEdit(event.getBlock().getChunk(), player)){
            event.setCancelled(true);
            player.sendMessage(prefix + "You are not in the proper clan to edit this claim.");
        }
    }

    @EventHandler
    public void onPlaceInRegion(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(rm.bypassEnabled(player)){
            return;
        }
        if(!canPlayerEdit(event.getBlock().getLocation().getChunk(), player)){
            event.setCancelled(true);
            player.sendMessage(prefix + "You are not in the proper clan to edit this claim.");
        }
    }

    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST || event.getClickedBlock().getType() == Material.CHEST_MINECART ||
                event.getClickedBlock().getType() == Material.HOPPER || event.getClickedBlock().getType() == Material.HOPPER_MINECART) {
            Player player = event.getPlayer();
            if(rm.bypassEnabled(player)){
                return;
            }
            if(!canPlayerEdit(event.getClickedBlock().getChunk(), player)){
                event.setCancelled(true);
                player.sendMessage(prefix + "You can not access containers in this claim.");
            }
        }
    }

    private boolean canPlayerEdit(Chunk eventChunkData, Player editor){
        if(!rm.chunkIsClaimed(eventChunkData)) return true;

        UUID editorID = editor.getUniqueId();
        ChunkCoords eventChunk = ChunkCoords.createChunkCoords(eventChunkData);

        // Ignoring clans, check if it's the owner
        for(ChunkCoords cc : rm.getClaimedChunks().keySet()){
            if(cc.equals(eventChunk)){
                if(rm.getClaimedChunks().get(cc).equals(editorID)){
                    return true;
                }
                break;
            }
        }

        // If the player is not in a clan, or the chunk owner has no clan, cancel the event
        ClanObj eventPlayerClan = cm.getPlayerClanID(editorID);
        if(eventPlayerClan == null){
            // If they own it then go for it. If they're not in a clan, don't worry about it.
            return rm.getClaimedChunks().get(eventChunk).equals(editorID);
        }

        // Now we have a player who is in a clan trying to edit a chunk that is not their own
        UUID chunkOwnerID = rm.getClaimedChunks().get(eventChunk);

        // If the claim owner doesn't have a clan, then cancel the event since clearly no one else would be able to edit it
        ClanObj chunkOwnerClan = cm.getPlayerClanID(chunkOwnerID);
        if(chunkOwnerClan == null) return false;

        // Now the player is in a clan and the chunk owner is also in a clan. Let's check if they are in the same clan. If so, we're good! Otherwise, block the access
        return eventPlayerClan.equals(chunkOwnerClan);
    }


}
