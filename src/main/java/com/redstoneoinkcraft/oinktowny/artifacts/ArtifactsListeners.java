package com.redstoneoinkcraft.oinktowny.artifacts;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;

        // JackHammer TODO: Work with blockbreakevent and player location relative to a given blockface
        if(am.getArtifactType(itemInHand) == ArtifactType.JACKHAMMER){
            if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;
            if(am.jackhammerBreak(event.getClickedBlock(), event.getBlockFace(), event.getPlayer())) decrementUses(player, itemInHand);
            return;
        }

        // Right click events
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            // Gravity Shifter
            if(am.getArtifactType(itemInHand) == ArtifactType.GRAVITY_SHIFTER){
                event.setCancelled(true);
                am.gravityShift(player);
                decrementUses(player, itemInHand);
                return;
            }

            // Health Shifter
            if(am.getArtifactType(itemInHand) == ArtifactType.HEALTH_SHIFTER){
                event.setCancelled(true);
                am.healthShift(player);
                decrementUses(player, itemInHand);
                return;
            }

            // Destructoid
            if(am.getArtifactType(itemInHand) == ArtifactType.DESTRUCTOID){
                am.destruct(player);
                decrementUses(player, itemInHand);
                return;
            }

            // Telepoof
            if(am.getArtifactType(itemInHand) == ArtifactType.TELEPOOF){
                am.poofTeleport(player);
                event.setCancelled(true);
                decrementUses(player, itemInHand);
            }

            // Lucky Hoe
            if(am.getArtifactType(itemInHand) == ArtifactType.LUCKY_HOE){
                if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if(am.instantGrowth(event.getClickedBlock())){
                        decrementUses(player, itemInHand);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;
        Player player = event.getPlayer();
        // Headlamp
        ItemStack helmet = player.getInventory().getHelmet();
        if(!am.isItemStackAnArtifact(helmet)) return;
        try {
            if (am.getArtifactType(helmet) == ArtifactType.HEADLAMP) {
                if(!player.getEyeLocation().getBlock().getType().equals(Material.AIR)) return;
                am.replaceTorch(player, player.getEyeLocation().getBlock());
                decrementUses(player, helmet);
            }
        } catch (NullPointerException e){
            return;
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event){
        if(!event.getInventory().getType().equals(InventoryType.CRAFTING)) return;
        Player player = (Player) event.getWhoClicked();
        // Headlamp
        if(event.getSlotType() == InventoryType.SlotType.ARMOR){
            if(event.getCurrentItem().equals((null))) return;
            if(am.getArtifactType(event.getCurrentItem()) == ArtifactType.HEADLAMP){
                am.clearPlayerTorches(player);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        // Headlamp
        if(block.getType().equals(Material.TORCH)){
            if(am.isHeadlampTorch(player, block)) {
                event.setCancelled(true);
            }
        }
    }
}
