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
public class EnchantExplosiveArrow extends EnchantmentFramework {

    public EnchantExplosiveArrow(){
        super(new NamespacedKey(Main.getInstance(), "EXPLOSIVE_ARROWS"));
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().equals(Material.BOW);
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.BOW;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public String getName() {
        return "EXPLOSIVE_ARROWS";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean staysAfterAnvil() {
        return false;
    }

    @Override
    public int getEnchantmentTableMinimumLevel() {
        return 1;
    }

    @Override
    public int getEnchantmentTableMaximumLevel() {
        return 1;
    }

    @Override
    public double getEnchantmentChance() {
        return 0;
    }

    public Material getIcon() { return Material.TNT; }

    public String getDescription() { return "BOOM! [Doesn't break blocks]"; }

    public int getCost(int currentLevel) {
        return 29;
    }
}
