package com.redstoneoinkcraft.oinktowny.bundles;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.economy.TownyTokenManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 9/1/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class SignClickListener implements Listener {

    String worldName = Main.getInstance().getConfig().getString("world-name");
    String prefix = Main.getInstance().getPrefix();
    BundleManager bundleManager = BundleManager.getInstance();
    TownyTokenManager tokenManager = TownyTokenManager.getInstance();
    String tag = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "[" + ChatColor.DARK_AQUA + ChatColor.BOLD
            + Main.getInstance().getBundlesConfig().getString("sign-tag") + ChatColor.DARK_GRAY + ChatColor.BOLD + "]";

    @EventHandler
    public void playerCreatesSign(SignChangeEvent event){
        Block block = event.getBlock();
        if(!(block.getType() == Material.WALL_SIGN)) return;
        if(!event.getLine(0).equals(ChatColor.stripColor(tag))) return;
        Player player = event.getPlayer();
        String line1;
        String line2 = event.getLine(1);
        String line3 = event.getLine(2);
        // TODO: If the 4th line isn't blank, a player needs permission oinktowny.bundlename (or oinktowny.fourthline) to purchase the bundle
        String line4 = event.getLine(3);
        if(line2.isEmpty()){
            resetLineOne(event, "Please add the bundle name to the second line.");
            return;
        }
        else if(line3.isEmpty()){
            resetLineOne(event, "Please add the bundle price to the second line, i.e. 4T");
            return;
        }
        // Successful sign!
        line1 = tag;
        event.setLine(0, line1);
        if(!bundleManager.bundleExists(line2)){
            resetLineOne(event, "That bundle does not exist!");
            return;
        }
        int cost;
        if(!line3.contains("T")) {
            resetLineOne(event, "Please ensure your number is an integer followed by \'T\'.");
            return;
        }
        try {
            String noT = line3.substring(0, line3.length()-1); // Remove the T
            cost = Integer.parseInt(noT);
        } catch (NumberFormatException exception){
            resetLineOne(event, "Please ensure your number is an integer followed by \'T\'.");
            return;
        }
        player.sendMessage(prefix + ChatColor.GREEN + "Sign for the " + ChatColor.GOLD + line2 + ChatColor.GREEN + " bundle has successfully been created!");
        return;

    }

    /* Sign Information:
     * Right click = Information about the bundle
     * Shift + right click = Quick purchase bundle
     * Line 0 = Sign tag
     * Line 1 = Bundle Name
     * Line 2 = Bundle Price
     * Line 4 = ""
     */
    @EventHandler
    public void playerRightClickWallSign(PlayerInteractEvent event){
        // if(!event.getPlayer().getWorld().getName().equals(worldName)) return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return; // Ensure it's a right click
        if(!event.getHand().equals(EquipmentSlot.HAND)) return;
        Block clickedBlock = event.getClickedBlock();
        if(!clickedBlock.getType().equals(Material.WALL_SIGN)) return; // Ensure it's a wall sign

        Sign wallSign = (Sign) clickedBlock.getState();
        if(!wallSign.getLine(0).equals(tag)) return; // Ensure the sign has the proper tag on top - Defined at the top of the class

        Player player = event.getPlayer();
        String bundleName = wallSign.getLine(1);
        int bundlePrice = Integer.parseInt(wallSign.getLine(2).substring(0, wallSign.getLine(2).length()-1)); // Based on my flawlessness, I assume the number has been properly parsed with a 'T' on the end

        if(!player.isSneaking()) {
            /* Create inventory for information and purchase details */
            String invName = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + bundleName + " bundle - " +
                    ChatColor.GOLD + ChatColor.BOLD + bundlePrice + " Token(s)";
            ArrayList<ItemStack> bundleItems = bundleManager.getBundle(bundleName, player);
            Inventory bundleInv = Bukkit.createInventory(null, adaptInvSize(bundleItems.size()), invName);

            // Open the inventory
            player.openInventory(bundleInv);
            for (int i = 0; i < bundleItems.size(); i++) {
                bundleInv.setItem(i, bundleItems.get(i));
            }
            player.sendMessage(prefix + "To buy this bundle, " + ChatColor.GOLD + ChatColor.BOLD + "SHIFT + RIGHT CLICK");
        } else if (player.isSneaking()){
            if(tokenManager.validPurchase(player, bundlePrice)){
                tokenManager.makeTransaction(player, bundlePrice);
                bundleManager.giveBundle(bundleName, player);
                player.sendMessage(prefix + "Thank you for your purchase!");
            } // Invalid transaction messages handled by validPurchase method
        }

    }

    public void resetLineOne(SignChangeEvent event, String errorMsg){
        event.getPlayer().sendMessage(prefix + ChatColor.RED + errorMsg);
        String line1 = "" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.stripColor(tag);
        event.setLine(0, line1);
    }

    public int adaptInvSize(int bundleSize){
        if(bundleSize > 45) return 54;
        else if (bundleSize > 36) return 45;
        else if (bundleSize > 27) return 36;
        else if (bundleSize > 18) return 27;
        else if (bundleSize > 9) return 18;
        else return 9;
    }

}
