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
public class EnchantRust extends EnchantmentFramework {

    public EnchantRust(){
        super(new NamespacedKey(Main.getInstance(), "RUST"));
    }


    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().contains("SWORD");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
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
        return "RUST";
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
        return 2;
    }

    @Override
    public double getEnchantmentChance() {
        return 0;
    }

    public Material getIcon() { return Material.WOODEN_SWORD; }

    public String getDescription() { return "Potentially poison your enemies! (10% chance)"; }

    public int getCost(int currentLevel) {
        return 23;
    }
}
