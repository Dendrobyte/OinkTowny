package com.redstoneoinkcraft.oinktowny.ruins.creation;

import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import com.redstoneoinkcraft.oinktowny.ruins.RuinsObj;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsSelectionListener implements Listener {

    RuinsManager rm = RuinsManager.getInstance();

    @EventHandler
    public void onPlayerLeftClickJoinSign(PlayerInteractEvent event){
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if(!rm.isPlayerCreatingRuins(player)) return;
        RuinsObj ruinsObj = rm.getCreatedRuins(player);
        if(rm.getPlayerCreationState(player) == RuinsCreationStates.JOIN_SIGNS) {
            if (!event.getClickedBlock().getType().toString().contains("WALL_SIGN")) {
                player.sendMessage(rm.getRuinsPrefix() + "Please left click a sign");
                return;
            }
            event.setCancelled(true);

            // Alright, it's definitely a sign and we're in the clear
            Sign sign = (Sign) event.getClickedBlock().getState();
            sign.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Ruins" + ChatColor.DARK_GRAY + "]");
            sign.setLine(1, ruinsObj.getName());
            sign.update();
            ruinsObj.addJoinSign(sign);
            player.sendMessage(rm.getRuinsPrefix() + "Sign created! Make more or type " + ChatColor.GOLD + ChatColor.BOLD + "DONE" + ChatColor.getLastColors(rm.getRuinsPrefix()) + ".");
        }
        else if(rm.getPlayerCreationState(player) == RuinsCreationStates.LEVELS){
            event.setCancelled(true);
            Block block = event.getClickedBlock();
            Location spawnPoint = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ());
            ruinsObj.addMonsterSpawnpoint(spawnPoint);
        }
    }

}
