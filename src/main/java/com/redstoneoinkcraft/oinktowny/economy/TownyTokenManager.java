package com.redstoneoinkcraft.oinktowny.economy;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 8/17/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class TownyTokenManager {

    private static TownyTokenManager instance = new TownyTokenManager();
    String prefix = Main.getInstance().getPrefix();

    private TownyTokenManager(){}

    public static TownyTokenManager getInstance(){
        return instance;
    }

    private ItemStack townyToken;

    public ItemStack createToken(int amount){
        // Create initial item
        townyToken = new ItemStack(Material.EMERALD, amount);

        // Reset name and add unique enchantment
        ItemMeta tokenMeta = townyToken.getItemMeta();
        tokenMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Towny Token");
        tokenMeta.addEnchant(Enchantment.LUCK, 4, true);

        // Add item lore
        List<String> tokenLore = new ArrayList<String>();
        tokenLore.add("You can use this token to...");
        tokenLore.add("- Trade with players!");
        tokenLore.add("- Purchase seasonal items!");
        tokenLore.add("- Purchase item bundles!");
        tokenLore.add("... and probably more!");
        tokenMeta.setLore(tokenLore);

        // Set the item meta
        townyToken.setItemMeta(tokenMeta);

        return townyToken;

    }

    public int getTokenInvSlot(Player player){
        ItemStack testToken = createToken(1);
        int itemSlot = -1;
        ItemStack itemsInInv[] = player.getInventory().getContents();
        for(int i = 0; i < itemsInInv.length; i++){
            if(itemsInInv[i] == null) continue;
            ItemStack item = itemsInInv[i];
            if(!item.hasItemMeta()) continue;
            if(!item.getEnchantments().keySet().equals(testToken.getEnchantments().keySet())){
                continue;
            } else {
                if(item.getType().equals(testToken.getType()) && item.getItemMeta().getDisplayName().equals(testToken.getItemMeta().getDisplayName())){
                    // We've definitely got a token
                    itemSlot = i;
                    break;
                }
            }
        }

        return itemSlot;
    }

    public boolean validPurchase(Player player, int amount){
        int tokenSlot = getTokenInvSlot(player);
        boolean haveToken = false;
        if(tokenSlot >= 0) haveToken = true;
        if(!haveToken){
            player.sendMessage(prefix + "You don't seem to be carrying any tokens, traveler!");
            return false;
        }
        ItemStack itemsInInv[] = player.getInventory().getContents();
        int itemInInvAmt = itemsInInv[tokenSlot].getAmount();
        if(itemInInvAmt < amount){
            player.sendMessage(prefix + "It appears you don't have enough tokens, traveler!");
            player.sendMessage(prefix + ChatColor.RED +  "You need " + (amount-itemInInvAmt) + " more tokens to make this purchase.");
            return false;
        }
        return true; // Valid purchase
    }

    public void makeTransaction(Player player, int amount){
        int itemSlot = getTokenInvSlot(player);
        if(itemSlot == -1) { // Just another double check
            player.sendMessage(prefix + "You don't seem to be carrying any tokens, traveler!");
            return;
        }
        int amountInInv = player.getInventory().getItem(itemSlot).getAmount();
        player.getInventory().getItem(itemSlot).setAmount(amountInInv-amount);
        player.sendMessage(prefix + ChatColor.GREEN + ChatColor.BOLD + "Transaction successful!");
    }

    /* Code for End of Day Box */
    private HashMap<Player, Inventory> playerBoxes = new HashMap<>();
    public HashMap<Player, Inventory> getPlayerBoxes(){
        return playerBoxes;
    }

    public void openPlayerBox(Player player){
        player.sendMessage(prefix + ChatColor.GREEN + ChatColor.ITALIC + "Opening the end of day box...");
        if(!playerBoxes.containsKey(player)){
            playerBoxes.put(player, Bukkit.createInventory(player, 18, "" + ChatColor.GREEN + ChatColor.BOLD +"Towny Bank Dropoff Station"));
        }
        Inventory pInv = player.openInventory(playerBoxes.get(player)).getTopInventory();

        ItemStack clock = new ItemStack(Material.CLOCK, 1);
        ItemMeta clockMeta = clock.getItemMeta();
        clockMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "How Does This Work?");
        clockMeta.setLore(new ArrayList<>(Arrays.asList("Drop your items in the box!", "Once exiting the box, tokens will be dropped.", "Leftover items will also be dropped.")));
        clock.setItemMeta(clockMeta);
        pInv.setItem(17, clock);
    }

    // Reminder: For the box closing/finishing, if someone is in a box while the timer runs out, close the inventory before clearing/closing.


}
