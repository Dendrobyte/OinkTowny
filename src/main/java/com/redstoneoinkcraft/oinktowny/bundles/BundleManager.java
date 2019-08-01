package com.redstoneoinkcraft.oinktowny.bundles;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentFramework;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 9/1/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class BundleManager {

    private static BundleManager instance = new BundleManager();
    private String prefix = Main.getInstance().getPrefix();

    private BundleManager() { }
    public static BundleManager getInstance() {
        return instance;
    }

    /* Check to make sure a bundle does not already exist */
    public boolean bundleExists(String bundleName){
        if(Main.getInstance().getBundlesConfig().contains("bundles." + bundleName)) {
            return true;
        } else {
            return false;
        }
    }

    public String listBundles(){
        String returnStr = prefix + ChatColor.GOLD + ChatColor.BOLD + "Existing Bundles: " + ChatColor.GREEN;
        for(String key : Main.getInstance().getBundlesConfig().getConfigurationSection("bundles").getKeys(true)){
            returnStr += "" + key + ", ";
        }

        returnStr = returnStr.substring(0, returnStr.length()-2); // Remove the last comma & space
        return returnStr;
    }

    public void removeBundle(String bundleName, Player player){
        Main.getInstance().getBundlesConfig().set("bundles." + bundleName, null);
        player.sendMessage(prefix + ChatColor.GREEN + "The bundle, " + ChatColor.GOLD + ChatColor.BOLD + bundleName + ", " + ChatColor.GREEN + "has been removed!");
        Main.getInstance().saveBundlesConfig();
    }

    /* In case storing the inventories doesn't work... lol */
    public void createBundleItems(Player player, String bundleName, boolean override){
        if(bundleExists(bundleName) && !override) {
            player.sendMessage(prefix + "Sorry, but that bundle already exists!");
            player.sendMessage(prefix + "Please use " + ChatColor.GOLD + " /oinktowny bundle override <name>");
            return;
        }

        Inventory playerInv = player.getInventory();
        int i = 0;
        for(ItemStack item : playerInv.getContents()){
            Main.getInstance().getBundlesConfig().set("bundles." + bundleName + "." + i, item);
            i++;
        }
        Main.getInstance().saveBundlesConfig();
        player.sendMessage(prefix + ChatColor.GREEN + "Your bundle, " + ChatColor.GOLD + ChatColor.BOLD + bundleName + "," + ChatColor.GREEN + " has been created!");
    }

    /* Retrieve and parse the bundle items from a specified bundle */
    public ArrayList<ItemStack> getBundle(String bundleName, Player player){
        if(!bundleExists(bundleName)){
            player.sendMessage(prefix + "Sorry, it appears that bundle doesn't exist.\n" + prefix + "Please contact a staff member.");
            return null;
        }
        ArrayList<ItemStack> bundleItems = new ArrayList<>();

        for(int i = 0; i <= Main.getInstance().getBundlesConfig().getKeys(false).size(); i++){
            bundleItems.add(Main.getInstance().getBundlesConfig().getItemStack("bundles." + bundleName + "." + i));
        }
        return bundleItems;
    }

    public void giveBundle(String bundleName, Player player){
        // TODO: If there isn't enough space, drop it on the ground and warn them, or cancel the command and warn them (preferable)
        for(ItemStack is : getBundle(bundleName, player)){
            ItemStack[] invContents = player.getInventory().getContents();
            for(int i = 0; i < player.getInventory().getSize(); i++){
                if(invContents[i] == null){
                    player.getInventory().setItem(i, is);
                    break;
                }
            }
        }
    }
}
