package com.redstoneoinkcraft.oinktowny.customenchants.enchants;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentFramework;
import org.bukkit.Material;
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
public class EnchantJumpBoost extends EnchantmentFramework {


    public EnchantJumpBoost() {
        super(new NamespacedKey(Main.getInstance(), "JUMP_BOOST"));
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().contains("BOOTS");
    }

    @Override
    public boolean conflictsWith(Enchantment other){
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_FEET;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public String getName() {
        return "JUMP_BOOST";
    }

    @Override
    public NamespacedKey getKey(){
        return key;
    }

    @Override // Level represents odds of conversion. Lvl 1 - 2 blocks, Lvl 2 - 3 blocks, Lvl 3 - 4 blocks
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getEnchantmentTableMinimumLevel() {
        return 1;
    }

    @Override
    public int getEnchantmentTableMaximumLevel() {
        return 3;
    }

    @Override
    public double getEnchantmentChance() {
        return 100;
    }

    @Override
    public boolean staysAfterAnvil(){
        return true;
    }

    public Material getIcon() { return Material.TNT; }

    public String getDescription() { return "Enhanced jumping!"; }

    public int getCost(int currentLevel) {
        return 15 + (int)(5.4*currentLevel);
    }
}
