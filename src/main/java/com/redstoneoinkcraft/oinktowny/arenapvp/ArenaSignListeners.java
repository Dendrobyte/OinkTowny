package com.redstoneoinkcraft.oinktowny.arenapvp;

import com.redstoneoinkcraft.oinktowny.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaSignListeners implements Listener {

    ArenaPVPManager apm = ArenaPVPManager.getInstance();
    String tag = "" + ChatColor.DARK_GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "TownyArena" + ChatColor.DARK_GRAY + "]";
    String prefix = Main.getInstance().getPrefix();

    @EventHandler
    public void arenaSignCreation(SignChangeEvent event){
        Block block = event.getBlock();
        if(!(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN)) return;
        if(!event.getLine(0).equals(ChatColor.stripColor(tag))) return;
        String line2 = event.getLine(1); // Arena name

        if(!event.getPlayer().hasPermission("oinktowny.arenas")) return;
        if(line2.isEmpty()) {
            resetLineOne(event, "Please ensure that the second line contains an arena name!");
            return;
        }
        if(!apm.arenaExists(line2)){
            resetLineOne(event, "That arena does not appear to exist!");
            return;
        }
        event.setLine(0, tag);
        event.setLine(2, "" + ChatColor.WHITE + ChatColor.BOLD + "WAITING");
    }

    @EventHandler
    public void arenaSignClick(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return; // Ensure it's a right click
        if(!event.getHand().equals(EquipmentSlot.HAND)) return;
        Block clickedBlock = event.getClickedBlock();
        if(!clickedBlock.getType().equals(Material.WALL_SIGN)) return; // Ensure it's a wall sign

        Sign wallSign = (Sign) clickedBlock.getState();
        if(!wallSign.getLine(0).equals(tag)) return; // Ensure the sign has the proper tag on top - Defined at the top of the class
        Player player = event.getPlayer();
        String line2 = wallSign.getLine(1);
        if(!apm.arenaExists(line2)){
            player.sendMessage(prefix + ChatColor.RED + "That arena no longer exists, or never did!");
            wallSign.setLine(0, ChatColor.DARK_RED + "[TownyArena]");
            wallSign.setLine(1, "" + ChatColor.DARK_RED + ChatColor.BOLD + "BROKEN");
            wallSign.update();
            return;
        }
        ArenaObj workingArena = apm.getArenaObj(line2);
        if(workingArena.getStatus() == ArenaStatus.WAITING){
            apm.addPlayerToArena(workingArena, player, wallSign);
            return;
        }
        if(workingArena.getStatus() != ArenaStatus.WAITING){
            player.sendMessage(prefix + "That arena is currently in use by " + workingArena.getPlayerOne().getName()
                     + " and " + workingArena.getPlayerTwo().getName() + ".");
            return;
        }
    }

    public void resetLineOne(SignChangeEvent event, String errorMsg){
        event.getPlayer().sendMessage(Main.getInstance().getPrefix() + ChatColor.RED + errorMsg);
        String line1 = "" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.stripColor(tag);
        event.setLine(0, line1);
    }
}
