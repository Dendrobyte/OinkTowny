package com.redstoneoinkcraft.oinktowny.regions;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class SuperpickCommand implements CommandExecutor {

    String prefix = Main.getInstance().getPrefix();
    RegionsManager rm = RegionsManager.getInstance();

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String args[]){
        if(!(sender instanceof Player)){
            sender.sendMessage(prefix + "Only players can active superpick!");
            return true;
        }
        Player player = (Player) sender;
        if(player.hasPermission("oinktowny.superpick")){
            rm.toggleSuperpick(player);
            return true;
        } else {
            player.sendMessage(prefix + "Sorry, you don't have access to this command!" + ChatColor.RED + ChatColor.BOLD + " oinktowny.superpick");
        }
        return true;
    }



}
