package com.redstoneoinkcraft.oinktowny.arenapvp;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaDamageListener implements Listener {

    ArenaPVPManager apm = ArenaPVPManager.getInstance();
    String prefix = apm.getArenaPrefix();
    Main mainInstance = Main.getInstance();

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;

        /* Damage instances being players */
        if(event.getDamager() instanceof Player){
            event.setCancelled(processDamage((Player)event.getDamager(), (Player)event.getEntity(), event.getDamage()));
            return;
        }
        /* Damange instances being arrows */
        if(event.getDamager() instanceof Arrow){
            Arrow arrow = (Arrow) event.getDamager();
            if(!(arrow.getShooter() instanceof Player)) return;
            event.setCancelled(processDamage((Player)arrow.getShooter(), (Player)event.getEntity(), event.getDamage()));
        }
        /* Damage instances being tridents */
        if(event.getDamager() instanceof Trident){
            Trident trident = (Trident) event.getDamager();
            if(!(trident.getShooter() instanceof Player)) return;
            event.setCancelled(processDamage((Player)trident.getShooter(), (Player)event.getEntity(), event.getDamage()));
        }
    }

    private boolean processDamage(Player attacker, Player defender, double damage){
        if(apm.isPlayerInArena(attacker) && apm.isPlayerInArena(defender)){
            ArenaObj workingArena = apm.getPlayerArena(attacker); // There's... no way for them to be in different arenas...
            if(!workingArena.getCanHitEachOther()){
                attacker.sendMessage(prefix + ChatColor.RED + ChatColor.ITALIC + "Wait until the match starts-- gain some distance!");
                return true;
            }
            if(defender.getHealth() - damage <= 0){
                apm.endArena(workingArena, attacker);
                attacker.setHealth(attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                defender.setHealth(defender.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                return true;
            }
        }
        else if (mainInstance.isTownyWorld(attacker.getWorld().getName())){
            attacker.playSound(attacker.getLocation(), Sound.BLOCK_ANVIL_PLACE, 6.0f, 1.0f);
            attacker.sendMessage(Main.getInstance().getPrefix() + "PvP is not enabled in towny!");
            return true;
        }
        return false;
    }
}
