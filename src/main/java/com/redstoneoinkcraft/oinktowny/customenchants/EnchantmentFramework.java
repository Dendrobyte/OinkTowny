package com.redstoneoinkcraft.oinktowny.customenchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public abstract class EnchantmentFramework extends Enchantment {

    public NamespacedKey key;

    public EnchantmentFramework(NamespacedKey key) {
        super(key);
        this.key = key;
    }

    /*
        WHEN YOU MAKE A NEW ENCHANTMENT, you MUST do the following:
        1) Add it as a final variable in the EnchantmentManager
        2) Register it in the EnchantmentManager
        3) Add it to the getByCustomKey method's switch statement (used for bundle items and such)
     */

    public abstract boolean canEnchantItem(ItemStack item);

    public abstract boolean conflictsWith(Enchantment other);

    public abstract EnchantmentTarget getItemTarget();

    public abstract int getMaxLevel();

    @Override
    public NamespacedKey getKey() { return key; }

    public String getCustomName(){
        String result = getKey().toString().substring(10);
        while(result.contains("_")){
            String modified;
            int index = result.indexOf("_");
            modified = result.substring(0, 1).toUpperCase();
            modified += result.substring(1, index);
            modified += " ";
            modified += result.substring(index+1, index+2).toUpperCase(); // Skip over the underscore
            modified += result.substring(index+2);
            result = modified;
        }
        // In case there are no underscores, capitalize first letter
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

    public abstract boolean staysAfterAnvil();

    public abstract int getEnchantmentTableMinimumLevel();

    public abstract int getEnchantmentTableMaximumLevel();

    public abstract double getEnchantmentChance();

    public static EnchantmentFramework getByCustomKey(String name){
        // TODO: Make this automated by having an accessible HashMap of names loaded up upon server start.
        EnchantmentFramework enchantment;
        switch(name){
            case "glow_strike":
                enchantment = EnchantmentManager.GLOW_STRIKE;
                break;
            case "jump_boost":
                enchantment = EnchantmentManager.JUMP_BOOST;
                break;
            case "explosive_arrows":
                enchantment = EnchantmentManager.EXPLOSIVE_ARROWS;
                break;
            case "conversion":
                enchantment = EnchantmentManager.CONVERSION;
                break;
            case "artifact":
                enchantment = EnchantmentManager.ARTIFACT;
                break;
            default:
                System.out.println("Custom enchantment " + name + " returned null!");
                enchantment = null;
        }

        return enchantment;
    }

    @Override
    public int getStartLevel() { return 1; }

    @Override
    public boolean isTreasure() { return false; }

}
