package com.redstoneoinkcraft.oinktowny.customenchants.enchants;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentFramework;
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
        return 2;
    }

    @Override
    public NamespacedKey getKey() {
        return null;
    }

    @Override
    public String getName() {
        return null;
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
}
