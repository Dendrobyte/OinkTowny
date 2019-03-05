package com.redstoneoinkcraft.oinktowny.bundles;

import com.redstoneoinkcraft.oinktowny.Main;
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
                    builder.append(enchant.getKey()).append("-").append(itemEnchantments.get(enchant)).append(", ");
                }
            }
            builder.append("]; ");

            // Get lore
            builder.append("lore:");
            if(item.getItemMeta().getLore() != null) {
                builder.append(ChatColor.stripColor(item.getItemMeta().getLore().toString()));
            }
            // Add a comma to the last lore item, by removing bracket then putting both
            builder.deleteCharAt(builder.length()-1);
            builder.append(",]");
            formatItemDetails = builder.toString();
            bundleItems.add(formatItemDetails);
        }

        Main.getInstance().getBundlesConfig().set("bundles." + bundleName, bundleItems);
        Main.getInstance().saveBundlesConfig();
        player.sendMessage(prefix + ChatColor.GREEN + "Your bundle, " + ChatColor.GOLD + ChatColor.BOLD + bundleName + "," + ChatColor.GREEN + " has been created!");
        return bundleItems;
    }

    /* Retrieve and parse the bundle items from a specified bundle */
    public ArrayList<ItemStack> getBundle(String bundleName, Player player){
        ArrayList<ItemStack> bundleItems = new ArrayList<>();
        if(!bundleExists(bundleName)){
            player.sendMessage(prefix + "Sorry, it appears that bundle doesn't exist.\n" + prefix + "Please contact a staff member.");
            return null;
        }
        List<String> bundleItemList = Main.getInstance().getBundlesConfig().getStringList("bundles." + bundleName);

        // The format is ITEM_NAME; AMOUNT; name:name on the item; enchants:[ENCHANT_NAME-LVL, ENCHANT_NAME2-LVL, ]; lore:[Line one, Line two]
        for(String origin : bundleItemList){
            String str = origin;

            // Get indexes of semicolons
            int matSemi = str.indexOf(";");
            int amtSemi = str.indexOf(";", matSemi+1);
            int nameSemi = str.indexOf(";", amtSemi+1);
            int enchantSemi = str.indexOf(";", nameSemi+1);

            // Set material
            String itemMat = str.substring(0, matSemi);
            Material newItemMaterial = Material.getMaterial(itemMat);

            // Set amount
            String itemAmount = str.substring(matSemi+2, amtSemi);
            int newItemAmount = Integer.parseInt(itemAmount);

            // Define item and itemmeta
            ItemStack tempItem = new ItemStack(newItemMaterial, newItemAmount);
            ItemMeta tempMeta = tempItem.getItemMeta();

            // Set enchants - enchants:[NAME-LVL, NAME-LVL, ]
            String itemEnchants = str.substring(nameSemi+11, enchantSemi);
            int enchantsIndexCheck = 0;
            int startEnchantIndex = 1; // Start after the first bracket, then move after the comma and sapce
            while(enchantsIndexCheck != -1){
                if(!itemEnchants.contains(",")){
                    break;
                }
                String enchantName = itemEnchants.substring(startEnchantIndex+10, itemEnchants.indexOf("-")); // +10 accounts for 'minecraft:'
                String enchantLevel = itemEnchants.substring(itemEnchants.indexOf("-")+1, itemEnchants.indexOf(","));
                int enchantLevelInt;
                try {
                    enchantLevelInt = Integer.parseInt(enchantLevel);
                    Enchantment itemEnchant = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(enchantName.toLowerCase()));
                    if(itemEnchant != null){
                        tempMeta.addEnchant(itemEnchant, enchantLevelInt, true);
                    }
                } catch (NumberFormatException exception){
                    player.sendMessage(prefix + ChatColor.RED + "It looks like an enchantment, " + enchantName + " is improperly defined! Contact an admin.");
                }
                enchantsIndexCheck = itemEnchants.indexOf(",");
                if(enchantsIndexCheck == -1) break;
                itemEnchants = "[" + itemEnchants.substring(itemEnchants.indexOf(",")+2);
            }

            // Set name
            String newItemName = str.substring(amtSemi+7, nameSemi);

            // Set lore
            String itemLore = str.substring(enchantSemi+7);
            ArrayList<String> newItemLore = new ArrayList<>();
            int num = 0; // Keep track of item so we can remove space from all items beyond the first
            while(itemLore.contains(",")){
                String loreString = itemLore.substring(1, itemLore.indexOf(","));
                if(num > 0 && loreString.length() > 1){
                    newItemLore.add(loreString.substring(1));
                } else {
                    newItemLore.add(loreString);
                }
                itemLore = "[" + itemLore.substring(itemLore.indexOf(",")+1);
                num++;
            }

            // Set values
            tempMeta.setDisplayName(newItemName);
            tempMeta.setLore(newItemLore);

            // Finally add the updated meta
            tempItem.setItemMeta(tempMeta);
            bundleItems.add(tempItem);
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
