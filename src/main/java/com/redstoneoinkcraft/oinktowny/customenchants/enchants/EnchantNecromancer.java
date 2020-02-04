package com.redstoneoinkcraft.oinktowny.customenchants.enchants;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentFramework;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
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
public class EnchantNecromancer extends EnchantmentFramework {

    public EnchantNecromancer(){
        super(new NamespacedKey(Main.getInstance(), "NECROMANCER"));
    }


    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().contains("SWORD");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other == EnchantmentManager.DOG_MASTER;
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
        return "NECROMANCER";
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
        return 3;
    }

    @Override
    public int getEnchantmentTableMaximumLevel() {
        return 3;
    }

    @Override
    public double getEnchantmentChance() {
        return 0;
    }

    public Material getIcon() { return Material.ZOMBIE_HEAD; }

    public String getDescription() { return "Spawn monsters when you hit something! (10% chance)"; }

    public int getCost(int currentLevel) {
        return 45;
    }
}
