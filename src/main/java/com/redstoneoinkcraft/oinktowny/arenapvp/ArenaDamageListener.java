package com.redstoneoinkcraft.oinktowny.arenapvp;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
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
public class ArenaDamageListener implements Listener {

    ArenaPVPManager apm = ArenaPVPManager.getInstance();
    String prefix = apm.getArenaPrefix();

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        // We know that to get to this point, the damager and entity have to be players
        Player attacker = (Player) event.getDamager();
        Player defender = (Player) event.getEntity();
        if(apm.isPlayerInArena(attacker) && apm.isPlayerInArena(defender)){
            ArenaObj workingArena = apm.getPlayerArena(attacker); // There's... no way for them to be in different arenas...
            if(!workingArena.getCanHitEachOther()){
                attacker.sendMessage(prefix + ChatColor.RED + ChatColor.ITALIC + "Wait until the match starts-- gain some distance!");
                event.setCancelled(true);
                return;
            }
            double damage = event.getFinalDamage();
            if(defender.getHealth() - damage <= 0){
                event.setCancelled(true);
                attacker.setHealth(attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                defender.setHealth(defender.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                apm.endArena(workingArena, attacker);
            }
        }
        else if (event.getEntity().getWorld().getName().equalsIgnoreCase(Main.getInstance().getWorldName())){
            event.setCancelled(true);
            attacker.playSound(attacker.getLocation(), Sound.BLOCK_ANVIL_PLACE, 6.0f, 1.0f);
            attacker.sendMessage(Main.getInstance().getPrefix() + "PvP is not enabled in towny!");
        }
    }
}
