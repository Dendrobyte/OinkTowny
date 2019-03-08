package com.redstoneoinkcraft.oinktowny.economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class EmeraldBlockListener implements Listener {

    @EventHandler
    public void onEmeraldBlockCraft(PrepareItemCraftEvent event){
        if(!event.getRecipe().getResult().equals(new ItemStack(Material.EMERALD_BLOCK))) return;
        ItemStack token = TownyTokenManager.getInstance().createToken(1);
        if(event.getInventory().contains(token)){ // TODO: Make sure it isn't just one
            Bukkit.getServer().getLogger().log(Level.WARNING, "SOMEONE ACTUALLY DID THE EMERALD THING!");
        }

    }

}
