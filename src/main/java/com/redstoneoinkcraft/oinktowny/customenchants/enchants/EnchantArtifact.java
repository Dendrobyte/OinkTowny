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
public class EnchantArtifact extends EnchantmentFramework {

    public EnchantArtifact() {
        super(new NamespacedKey(Main.getInstance(), "ARTIFACT"));
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public String getName() {
        return "ARTIFACT";
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public boolean staysAfterAnvil() {
        return false;
    }

    @Override
    public int getEnchantmentTableMinimumLevel() {
        return 0;
    }

    @Override
    public int getEnchantmentTableMaximumLevel() {
        return 0;
    }

    @Override
    public double getEnchantmentChance() {
        return 0;
    }
}
