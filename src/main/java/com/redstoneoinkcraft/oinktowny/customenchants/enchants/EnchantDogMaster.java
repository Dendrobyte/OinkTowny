package com.redstoneoinkcraft.oinktowny.customenchants.enchants;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentFramework;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
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
public class EnchantDogMaster extends EnchantmentFramework {

    public EnchantDogMaster(){
        super(new NamespacedKey(Main.getInstance(), "DOG_MASTER"));
    }


    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().contains("SWORD");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other == EnchantmentManager.NECROMANCER;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public String getName() {
        return "DOG_MASTER";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean staysAfterAnvil() {
        return true;
    }

    @Override
    public int getEnchantmentTableMinimumLevel() {
        return 2;
    }

    @Override
    public int getEnchantmentTableMaximumLevel() {
        return 3;
    }

    @Override
    public double getEnchantmentChance() {
        return 0;
    }
}
