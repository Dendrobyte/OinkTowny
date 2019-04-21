package com.redstoneoinkcraft.oinktowny.artifacts;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArtifactsListeners implements Listener {

    ArtifactManager am = ArtifactManager.getInstance();

    private void decrementUses(Player player, ItemStack item){
        am.setUses(player, item, am.getUses(item)-1);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
        if(!am.isItemStackAnArtifact(itemInHand)) return;
        Player player = event.getPlayer();

        // JackHammer
        if(am.getArtifactType(itemInHand) == ArtifactType.JACKHAMMER){
            if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;
            am.jackhammerBreak(event.getClickedBlock(), event.getBlockFace());
            decrementUses(player, itemInHand);
        }

        // Gravity Shifter
        if(am.getArtifactType(itemInHand) == ArtifactType.GRAVITY_SHIFTER){
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                am.gravityShift(player);
                decrementUses(player, itemInHand);
            }
        }

        // Health Shifter
        if(am.getArtifactType(itemInHand) == ArtifactType.HEALTH_SHIFTER){
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                am.healthShift(player);
                decrementUses(player, itemInHand);
            }
        }

        // Destructoid
        if(am.getArtifactType(itemInHand) == ArtifactType.HEALTH_SHIFTER){
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                am.destruct(player);
                decrementUses(player, itemInHand);
            }
        }
    }

}
