package com.redstoneoinkcraft.oinktowny.economy;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    @EventHandler
    public void playerRightClickWallSign(PlayerInteractEvent event){
        if(!event.getPlayer().getWorld().getName().equals(worldName)) return; // Ensure it's the towny world TODO: Multiple worlds with signs?
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return; // Ensure it's a right click
        Block clickedBlock = event.getClickedBlock();
        if(!clickedBlock.getType().equals(Material.WALL_SIGN)) return; // Ensure it's a sign

        // wallSign.update(); !!!!
        Sign wallSign = (Sign) clickedBlock;
        if(!wallSign.getLine(0).equals(Main.getInstance().getBundlesConfig().getString("sign-tag"))) return; // Ensure the sign has the proper tag on top

        /* Get variables and make various fields for things
         *
         * Sign Information:
         * Right click = Information about the bundle
         * Shift + right click = Quick purchase bundle
         * Line 0 = Sign tag
         * Line 1 = Bundle Name
         * Line 2 = Bundle Price
         * Line 4 = ""
         */
        Player player = event.getPlayer();
        String signTag = wallSign.getLine(0);
        System.out.println(signTag); // Future debug in case the above if statement does not work
        String bundleName = wallSign.getLine(1);
        String bundlePrice = wallSign.getLine(2);
        String lineFour = wallSign.getLine(3);

        /* Create inventory for information and purchase details */
        String invName = "" + ChatColor.GREEN + ChatColor.BOLD + bundleName;
        Inventory bundleInv = Bukkit.createInventory(null, 18, invName);
        List<String> bundleItems = Main.getInstance().getBundlesConfig().getStringList("bundles." + bundleName);

        // Open the inventory
        player.openInventory(bundleInv);
        for(String bundleItem : bundleItems){
            // TODO: Load from configuration
        }

    }

}
