package com.redstoneoinkcraft.oinktowny.economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 8/17/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class TownyTokenItem extends ItemStack {

    private TownyTokenItem townyToken;

    public TownyTokenItem(int amount){
        // Create initial item
        townyToken.setAmount(amount);
        townyToken.setType(Material.EMERALD);

        // Reset name and add unique enchantment
        ItemMeta tokenMeta = townyToken.getItemMeta();
        tokenMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Towny Token");
        tokenMeta.addEnchant(Enchantment.LUCK, 4, true);

        // Add item lore
        List<String> tokenLore = new ArrayList<String>();
        tokenLore.add("" + ChatColor.RESET + ChatColor.DARK_PURPLE + "You can use this token to...");
        tokenLore.add("" + ChatColor.RESET + ChatColor.DARK_PURPLE + "- Trade with players!");
        tokenLore.add("" + ChatColor.RESET + ChatColor.DARK_PURPLE + "- Purchase seasonal items!");
        tokenLore.add("" + ChatColor.RESET + ChatColor.DARK_PURPLE + "- Purchase item packages!");
        tokenLore.add("" + ChatColor.RESET + ChatColor.DARK_PURPLE + "... and probably more!");
        tokenMeta.setLore(tokenLore);

        // Set the item meta
        townyToken.setItemMeta(tokenMeta);

    }


}
