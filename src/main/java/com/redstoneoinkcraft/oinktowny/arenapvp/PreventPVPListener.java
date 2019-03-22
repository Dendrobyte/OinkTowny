package com.redstoneoinkcraft.oinktowny.arenapvp;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class PreventPVPListener implements Listener {

    ArenaPVPManager apm = ArenaPVPManager.getInstance();

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        // We know that to get to this point, the damager and entity have to be players
        Player attacker = (Player) event.getDamager();
        Player defender = (Player) event.getEntity();
        /* TODO: If the players are in the active arena players list, also return so damage isn't cancelled
         * We also know that we need to check damage, so if the damage goes below or equal to zero, we don't have to deal with the death event
         * That would all be managed in its own block... then moving on, would cancel if the player is in the towny world.
         * All methods and checks should/will be handled from within
         */
        if(event.getEntity().getWorld().getName().equalsIgnoreCase(Main.getInstance().getWorldName())){
            event.setCancelled(true);
            attacker.playSound(attacker.getLocation(), Sound.BLOCK_ANVIL_FALL, 2.0f, 1.0f);
            attacker.sendMessage(Main.getInstance().getPrefix() + "PvP is not enabled in towny!");
        }
    }

}
