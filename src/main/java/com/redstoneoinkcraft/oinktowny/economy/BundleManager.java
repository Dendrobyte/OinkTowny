package com.redstoneoinkcraft.oinktowny.economy;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    private BundleManager() {

    }
    public static BundleManager getInstance() {
        return instance;
    }

    /* Save an inventory for creating a new bundle */
    public void createNewBundle(Player player, String bundleName){
        if(Main.getInstance().getBundlesConfig().contains("bundles." + bundleName)){
            player.sendMessage(prefix + "Sorry, but that bundle already exists!");
            player.sendMessage(prefix + "Please use " + ChatColor.GOLD + " /oinktowny bundle override <name>");
            return;
        }
        overrideBundle(player, bundleName);
    }

    /* It would be the same as above, so I'm just putting this in after the check... */
    public void overrideBundle(Player player, String bundleName){
        Inventory playerInv = player.getInventory();
        ArrayList<ItemStack> bundleItems = new ArrayList<ItemStack>();
        Collections.addAll(bundleItems, playerInv.getContents());

        for(ItemStack itemStack : bundleItems){
            Main.getInstance().getBundlesConfig().addDefault("bundles." + bundleName, bundleItems);
        }
        Main.getInstance().saveResource("bundles.yml", true);
        player.sendMessage(prefix + "The " + ChatColor.GOLD + ChatColor.BOLD + bundleName + ChatColor.getLastColors(prefix) + " bundle has been created!");
    }

    /* Check to make sure a bundle does not already exist */
    public boolean bundleExists(String bundleName){
        return Main.getInstance().getBundlesConfig().contains("bundles." + bundleName);
    }

    @SuppressWarnings("deprecation")
    /* In case storing the inventories doesn't work... lol */
    public ArrayList<String> createBundleItems(Player player, String bundleName, boolean override){
        if(bundleExists(bundleName) && !override) {
            player.sendMessage(prefix + "Sorry, but that bundle already exists!");
            player.sendMessage(prefix + "Please use " + ChatColor.GOLD + " /oinktowny bundle override <name>");
            return null;
        }

        // The format is ITEM_NAME; AMOUNT; name:name on the item; enchants:[ENCHANT_NAME-LVL, ENCHANT_NAME2-LVL, ]; lore:[Line one, Line two]
        // TODO: This will be re-written with a StringBuilder at some point. Done - Zaph
        ArrayList<String> bundleItems = new ArrayList<String>();
        Inventory playerInv = player.getInventory();
        for(ItemStack item : playerInv.getContents()){
            StringBuilder builder = new StringBuilder();
            if(item == null) continue; // Make sure the item isn't null
            String formatItemDetails;
            // Get material
            builder.append(item.getType().toString()).append("; ");
            // Get amount
            builder.append(item.getAmount()).append("; ");
            // Get name
            builder.append("name:");
            if(item.getItemMeta().getDisplayName() != null){
                builder.append(item.getItemMeta().getDisplayName());
            }
            builder.append("; ");
            // Get enchants
            builder.append("enchants:[");
            if(item.getEnchantments() != null) {
                Map<Enchantment, Integer> itemEnchantments = item.getEnchantments();
                for (Enchantment enchant : itemEnchantments.keySet()) {
                    builder.append(enchant.getName()).append("-").append(itemEnchantments.get(enchant)).append(", ");
                }
            }
            builder.append("]; ");

            // Get lore
            builder.append("lore:");
            if(item.getItemMeta().getLore() != null) {
                builder.append(ChatColor.stripColor(item.getItemMeta().getLore().toString()));
            }
            formatItemDetails = builder.toString();
            bundleItems.add(formatItemDetails);
        }

        System.out.println("Voila! The final string would be...");
        Main.getInstance().getBundlesConfig().set("bundles." + bundleName, bundleItems);
        Main.getInstance().saveBundlesConfig();
        try {
            Main.getInstance().getBundlesConfig().save(Main.getInstance().getBundlesFile());
        } catch(IOException e){
            e.printStackTrace();
        }
        for(String str : bundleItems){
            System.out.println(str + "\n");
        }
        player.sendMessage(prefix + ChatColor.GREEN + "Your bundle, " + ChatColor.GOLD + ChatColor.BOLD + bundleName + "," + ChatColor.GREEN + " has been created!");
        return bundleItems;
    }
}
