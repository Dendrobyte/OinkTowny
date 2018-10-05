package com.redstoneoinkcraft.oinktowny.economy;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
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

    private BundleManager() {

    }
    public static BundleManager getInstance() {
        return instance;
    }

    /* Check to make sure a bundle does not already exist */
    public boolean bundleExists(String bundleName){
        return Main.getInstance().getBundlesConfig().contains("bundles." + bundleName);
    }

    /* In case storing the inventories doesn't work... lol */
    public ArrayList<String> createBundleItems(Player player, String bundleName, boolean override){
        if(bundleExists(bundleName) && !override) {
            player.sendMessage(prefix + "Sorry, but that bundle already exists!");
            player.sendMessage(prefix + "Please use " + ChatColor.GOLD + " /oinktowny bundle override <name>");
            return null;
        }

        // The format is ITEM_NAME; AMOUNT; name:name on the item; enchants:[ENCHANT_NAME-LVL, ENCHANT_NAME2-LVL, ]; lore:[Line one, Line two]
        ArrayList<String> bundleItems = new ArrayList<>();
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

    /* Retrieve and parse the bundle items from a specified bundle */
    public void getBundle(String bundleName, Player player){
        ArrayList<ItemStack> bundleItems = new ArrayList<>();
        if(!bundleExists(bundleName)){
            player.sendMessage(prefix + "Sorry, it appears that bundle doesn't exist.\n" + prefix + "Please contact a staff member.");
            return;
        }
        List<String> bundleItemList = Main.getInstance().getBundlesConfig().getStringList("bundles." + bundleName);

        // The format is ITEM_NAME; AMOUNT; name:name on the item; enchants:[ENCHANT_NAME-LVL, ENCHANT_NAME2-LVL, ]; lore:[Line one, Line two]
        for(String origin : bundleItemList){
            String str = origin;
            System.out.println("Origin: " + str);

            // Get indexes of semicolons
            int matSemi = str.indexOf(";");
            int amtSemi = str.indexOf(";", matSemi+1);
            int nameSemi = str.indexOf(";", amtSemi+1);
            int enchantSemi = str.indexOf(";", nameSemi+1);

            // Set material
            String itemMat = str.substring(0, matSemi);
            System.out.println("Material: " + itemMat);

            // Set amount
            String itemAmount = str.substring(matSemi+2, amtSemi);
            System.out.println("Amount: " + itemAmount);
            int amt = Integer.parseInt(itemAmount);

            ItemStack tempItem = new ItemStack(Material.STONE, amt);
            ItemMeta tempMeta = tempItem.getItemMeta();
            // Set name
            String itemName = str.substring(amtSemi+7, nameSemi);
            System.out.println("Name: " + itemName);
            tempMeta.setDisplayName(itemName);

            // Set enchants - enchants:[NAME-LVL, NAME-LVL, ]
            String itemEnchants = str.substring(nameSemi+11, enchantSemi);
            int enchantsIndexCheck = 0;
            int startEnchantIndex = 1; // Start after the first bracket, then move after the comma and sapce
            while(enchantsIndexCheck != -1){
                if(!itemEnchants.contains(",")){
                    enchantsIndexCheck = -1;
                    break;
                }
                String enchantName = itemEnchants.substring(startEnchantIndex, itemEnchants.indexOf("-"));
                String enchantLevel = itemEnchants.substring(itemEnchants.indexOf("-")+1, itemEnchants.indexOf(","));
                System.out.println("EName: " + enchantName + "\nELevel: " + enchantLevel);
                // tempItem.getEnchantments().put(Enchantment.)
                enchantsIndexCheck = itemEnchants.indexOf(",");
                if(enchantsIndexCheck == -1) break;
                startEnchantIndex = enchantsIndexCheck+2;
            }
            System.out.println("Enchants: " + itemEnchants);

            // Set lore
            String itemLore = str.substring(enchantSemi+7);
            System.out.println("Lore: " + itemLore);

            tempItem.setItemMeta(tempMeta);
            bundleItems.add(tempItem);

        }
        for(ItemStack is : bundleItems){
            player.getInventory().addItem(is);
        }
    }
}
