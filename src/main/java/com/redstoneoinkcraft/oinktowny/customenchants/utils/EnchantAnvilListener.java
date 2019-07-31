package com.redstoneoinkcraft.oinktowny.customenchants.utils;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class EnchantAnvilListener implements Listener {

    /*
     Slot 0: Item slot
     Slot 1: Item edit slot
     Slot 2: Item result slot
     */

    @EventHandler
    public void onAnvilUse(PrepareAnvilEvent event){
        try {
            AnvilInventory inv = event.getInventory();
            ItemStack originalItem = inv.getItem(0);
            if (ChatColor.stripColor(originalItem.getItemMeta().getLore().get(0)).equalsIgnoreCase("Artifact")){
                event.setResult(new ItemStack(originalItem.getType(), originalItem.getAmount()));
            }
        } catch (NullPointerException e){
            // It's okay, it means slots are null or no enchantments are necessary
        }
    }
}
