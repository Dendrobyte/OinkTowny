package com.redstoneoinkcraft.oinktowny.customenchants;

import com.redstoneoinkcraft.oinktowny.customenchants.enchants.EnchantDmgConversion;
import com.redstoneoinkcraft.oinktowny.customenchants.enchants.EnchantGlowEntity;
import com.redstoneoinkcraft.oinktowny.customenchants.enchants.EnchantJumpBoost;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class EnchantmentManager {

    private static EnchantmentManager instance = new EnchantmentManager();

    private EnchantmentManager() {}

    public static EnchantmentManager getInstance() {
        return instance;
    }

    /* Constants
     * Note: All constant names are the namespace passed in in the class.
     */
    public static final EnchantmentFramework GLOW_STRIKE = new EnchantGlowEntity();
    public static final EnchantmentFramework JUMP_BOOST = new EnchantJumpBoost();
    public static final EnchantmentFramework CONVERSION = new EnchantDmgConversion(); // Converts damage to health

    public static void registerEnchants(){
        registerCustomEnchantment(GLOW_STRIKE);
        registerCustomEnchantment(JUMP_BOOST);
        registerCustomEnchantment(CONVERSION);
        Bukkit.getLogger().log(Level.INFO, "[OinkTowny] All enchantments have been loaded!");
    }
    /* Register and inject all enchantments to the server, the return is the result of the registration */
    public static void registerCustomEnchantment(EnchantmentFramework enchantment){
        try {
            // Allow registration using Reflection API
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);

            // Actual enchantment registration
            System.out.println("1: " + enchantment);
            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            Bukkit.getLogger().log(Level.WARNING, "[OinKTowny] Enchants: Field value failure... but it still works? (o. o)");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            Bukkit.getLogger().log(Level.WARNING, "[OinKTowny] Enchants: Not accepting new values!");
        }
    }

    /* For the command usage */
    public void enchantItem(Player player, EnchantmentFramework enchantment, int level){
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if(enchantment.canEnchantItem(itemInHand)){
            ItemMeta itemMeta = itemInHand.getItemMeta();
            itemMeta.addEnchant(enchantment, level, true);

            String loreAddition = "" + ChatColor.GOLD + enchantment.getCustomName() + ", Level " + level;
            List<String> newLore = new ArrayList<>();
            newLore.add(loreAddition);
            itemMeta.setLore(newLore);
            itemInHand.setItemMeta(itemMeta);
            player.sendMessage("[Enchants]: Enchantment added!");
            System.out.println("IT: " + enchantment);
            System.out.println("E: " + itemInHand.getEnchantments());
        } else {
            player.sendMessage("[Enchants]: You can not enchant this item!");
        }
    }

    /* Enchantment specific methods */

    // Calculate odds for the conversion enchantment
    public boolean calculateConversion(int level){
        Random rand = new Random();
        int odds = rand.nextInt(100);
        System.out.println("ODDS: " + odds);
        if(level == 1){
            return odds > 0 && odds < 11;
        }
        if(level == 2){
            return odds > 0 && odds < 21;
        }
        return false;
    }
}
