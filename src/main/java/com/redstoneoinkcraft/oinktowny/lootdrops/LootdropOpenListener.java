package com.redstoneoinkcraft.oinktowny.lootdrops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LootdropOpenListener implements Listener {

    LootdropManager lm = LootdropManager.getInstance();

    @EventHandler
    public void onLootdropOpen(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if(block.getType() != Material.CHEST) return;
        Chest chest = (Chest) event.getClickedBlock().getState();
        if(!lm.isLootcrate(chest)) return;
        if(lm.lootcrateIsFound(chest)) return;
        lm.setLootcrateFound(chest, true);
        lm.startLootcrateExpiration(chest, 10, event.getPlayer());
    }

}
