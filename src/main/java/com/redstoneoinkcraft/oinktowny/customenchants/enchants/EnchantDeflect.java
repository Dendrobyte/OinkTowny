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
public class EnchantDeflect extends EnchantmentFramework {

    public EnchantDeflect(){
        super(new NamespacedKey(Main.getInstance(), "DEFLECT"));
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().contains("CHESTPLATE") || item.getType().toString().contains("LEGGINGS");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other == Enchantment.PROTECTION_PROJECTILE;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public String getName() {
        return "DEFLECT";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean staysAfterAnvil() {
        return true;
    }

    @Override
    public int getEnchantmentTableMinimumLevel() {
        return 21;
    }

    @Override
    public int getEnchantmentTableMaximumLevel() {
        return 3;
    }

    @Override
    public double getEnchantmentChance() {
        return 20;
    }
}
