package com.redstoneoinkcraft.oinktowny.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * OinkTowny created/started by markb (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class TownyBankInvListener implements Listener {

    TownyTokenManager ttm = TownyTokenManager.getInstance();

    @EventHandler
    public void playerClosesBox(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        if(!ttm.getPlayerBoxes().values().contains(inv)) return;

        ArrayList<ItemStack> activeItems = new ArrayList<>();
        for(int i = 0; i < 17; i++){
            if(inv.getContents()[i] != null){
                if(inv.getContents()[i].getType() == Material.AIR) return;
                activeItems.add(inv.getContents()[i]);
            }
        }
        ttm.calculateItems(activeItems, (Player)event.getPlayer());
    }

    @EventHandler
    public void playerClicksInBox(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        if(ttm.getPlayerBoxes().values().contains(inv)){
            if(event.getSlot() == 17){
                event.setCancelled(true);
            }
        }
    }

}
