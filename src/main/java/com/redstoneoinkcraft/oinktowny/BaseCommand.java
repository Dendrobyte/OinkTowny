package com.redstoneoinkcraft.oinktowny;

import com.redstoneoinkcraft.oinktowny.economy.BundleManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 9/1/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class BaseCommand implements CommandExecutor {

    String prefix = Main.getInstance().getPrefix();
    BundleManager manager = BundleManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage(prefix + "Sorry, you have to be a player to run these commands!");
            sender.sendMessage(prefix + "We use player inventories and, well, you don't have one... =(");
            return true;
        }
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("oinktowny")){
            if(args.length == 0){
                player.sendMessage(prefix + "No arguments provided! " + ChatColor.GOLD + "/oinktowny help");
                return true;
            }
            if(args[0].equalsIgnoreCase("bundle")){
                if(!player.hasPermission("oinktowny.bundle")){
                    player.sendMessage(prefix + "Sorry, you don't have access to do this." + ChatColor.RED + "oinktowny.bundle");
                    return true;
                }
                /* Command structure: /oinktowny bundle create/override <name> */
                if(args.length == 1){
                    player.sendMessage(prefix + "Not enough arguments!");
                    player.sendMessage(prefix + "Proper usage: " + ChatColor.GOLD + "/oinktowny bundle create/override/give <name>");
                    return true;
                }
                if(args.length > 3){
                    player.sendMessage(prefix + "Too many arguments!");
                    player.sendMessage(prefix + "Proper usage: " + ChatColor.GOLD + "/oinktowny bundle create/override/give <name>");
                    return true;
                }
                if(args[1].equalsIgnoreCase("create")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please provide a bundle name!");
                        return true;
                    }
                    String bundleName = args[2];
                    manager.createBundleItems(player, bundleName, false);
                    return true;
                }
                if(args[1].equalsIgnoreCase("override")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please provide a bundle name!");
                        return true;
                    }
                    String bundleName = args[2];
                    manager.createBundleItems(player, bundleName, true);
                    return true;
                }
                if(args[1].equalsIgnoreCase("give")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please provide a bundle name!");
                        return true;
                    }
                    String bundleName = args[2];
                    manager.getBundle(bundleName, player);
                    return true;
                }
                else {
                    player.sendMessage(prefix + "It apperas something isn't working... \n" + prefix + "Please contact a staff member.");
                    return true;
                }
            }
        }
        return true;
    }

}
