package com.redstoneoinkcraft.oinktowny.customenchants.utils;

import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentFramework;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * OinkTowny created/started by markb (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class CustomEnchantInvListener implements Listener {

    EnchantmentManager em = EnchantmentManager.getInstance();

    @EventHandler
    public void playerClosesCustomEnchantInventory(InventoryCloseEvent event){
        if(!event.getInventory().getType().equals(InventoryType.WORKBENCH)) return;

        Player player = (Player)event.getPlayer();
        if(em.getActivePlayerCustomEnchantInventories().keySet().contains(player)) {
            player.sendMessage(em.prefix + ChatColor.RED + "Transaction cancelled: Closed Inventory");
            em.getActivePlayerCustomEnchantInventories().remove(player);
        }
    }

    @EventHandler
    public void playerClicksCustomEnchantInventory(InventoryClickEvent event){
        if(!event.getInventory().getType().equals(InventoryType.WORKBENCH)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();
        if(currentItem == null) return;
        if(em.getActivePlayerCustomEnchantInventories().keySet().contains(player)) {
            if(event.getSlot() == 1){
                // Cancel the transaction and close the inventory
                player.sendMessage(em.prefix + ChatColor.RED + "Transaction cancelled!");
                em.getActivePlayerCustomEnchantInventories().remove(player);
                player.getOpenInventory().close();
            }

            // Compare levels and give them the item
            else if(event.getSlot() == 3) {
                // Check to see if an enchantment has been selected
                if(inv.getItem(0) == null) {
                    event.setCancelled(true); // If there is an item, then cost paper should have been placed
                    return;
                }

                // Check levels
                int levels = player.getLevel();
                String costLine = ChatColor.stripColor(inv.getItem(9).getItemMeta().getLore().get(1)); // This just gets the item... I didn't want to make a new variable :(
                int cost = Integer.parseInt(costLine.substring(0, costLine.indexOf("L")-1));

                // Give (or not) the item
                if(levels >= cost){
                    player.setLevel(levels-cost);
                    player.getInventory().setItemInMainHand(inv.getItem(0));
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 5, 5);
                    player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 10, 5);
                    player.sendMessage(em.prefix + "Majik!");
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 5, 5);
                    player.sendMessage(em.prefix + "Insufficient amount of levels!");
                }

                // Give the items
                em.getActivePlayerCustomEnchantInventories().remove(player);
                player.getOpenInventory().close();
            }

            else if(currentItem.getType().equals(Material.BOOK)){
                // Get all relevant info
                String bookName = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
                EnchantmentFramework enchant = em.getEnchantmentByName(ChatColor.stripColor(bookName).substring(1, bookName.length()-1));
                String bookLevel = currentItem.getItemMeta().getLore().get(0);
                String bookCost = currentItem.getItemMeta().getLore().get(1);
                int level = Integer.parseInt(bookLevel.substring(bookLevel.indexOf(":")+2));

                // Enchant the item and put it in the appropriate slot
                ItemStack itemEnchanted = inv.getItem(2);
                em.addEnchantmentToItem(itemEnchanted, enchant, level);
                inv.setItem(0, itemEnchanted);

                // Set up current level and cost on bottom right
                ItemStack costPaper = new ItemStack(Material.PAPER, 1);
                ItemMeta costPaperMeta = costPaper.getItemMeta();
                costPaperMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.ITALIC + "Current Cost Info");
                costPaperMeta.setLore(Arrays.asList("Enchantment Level: " + level, bookCost));
                costPaper.setItemMeta(costPaperMeta);
                inv.setItem(9, costPaper);

            }

            event.setCancelled(true);
        }
    }

}
