package com.redstoneoinkcraft.oinktowny.bundles;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class PreventItemStealListener implements Listener {

    @EventHandler
    public void onBundleInvClick(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Token(s)")) return;
        event.setCancelled(true);
    }

}
