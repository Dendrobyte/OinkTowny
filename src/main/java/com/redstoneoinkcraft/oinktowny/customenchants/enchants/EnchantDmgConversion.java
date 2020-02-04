package com.redstoneoinkcraft.oinktowny.customenchants.enchants;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentFramework;
import org.bukkit.ChatColor;
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
public class EnchantDmgConversion extends EnchantmentFramework {

    public EnchantDmgConversion(){
        super(new NamespacedKey(Main.getInstance(), "CONVERSION"));
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().contains("CHESTPLATE");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other == Enchantment.PROTECTION_PROJECTILE;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_TORSO;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public String getName() {
        return "CONVERSION";
    }

    @Override
    public boolean staysAfterAnvil() {
        return false;
    }

    @Override // Level represents odds of conversion. Lvl 1 - 10%, Lvl 2 - 20%
    public int getEnchantmentTableMinimumLevel() {
        return 1;
    }

    @Override
    public int getEnchantmentTableMaximumLevel() {
        return 2;
    }

    @Override
    public double getEnchantmentChance() {
        return 0;
    }

    public Material getIcon() { return Material.GOLDEN_APPLE; }

    public String getDescription() { return "Convert damage to health! (Chance increases with level)" + ChatColor.RED + ChatColor.BOLD + "Mark hasn't fully tested this one..."; }

    public int getCost(int currentLevel) {
        return 24 + (int)(6.2*currentLevel);
    }
}
