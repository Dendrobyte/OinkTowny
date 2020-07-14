package com.redstoneoinkcraft.oinktowny.listeners;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class PlayerDeathListener implements Listener {

    Main mainInstance = Main.getInstance();
    String prefix = mainInstance.getPrefix();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if(mainInstance.isTownyWorld(player.getWorld().getName())){

            // Idk if this will fire on time, but it's worth a shot
            player.teleport(Bukkit.getServer().getWorld(mainInstance.getWorldName()).getSpawnLocation());

            boolean found = false;
            for(int i = 0; i < player.getInventory().getSize(); i++){
                try {
                    if (player.getInventory().getItem(i).getEnchantments().containsKey(Enchantment.VANISHING_CURSE)) {
                        player.getInventory().getItem(i).setAmount(0);
                        found = true;
                    }
                } catch (NullPointerException e){
                    // No enchantments
                }
            }
            if(found){
                player.sendMessage(prefix + "Items with " + ChatColor.DARK_PURPLE + ChatColor.ITALIC + " Curse of Vanishing " + ChatColor.getLastColors(prefix) + " have been removed.");
            }

        }
    }

}
