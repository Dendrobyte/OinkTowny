package com.redstoneoinkcraft.oinktowny.customenchants.utils;

import com.google.common.collect.Sets;
import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentFramework;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
import com.redstoneoinkcraft.oinktowny.customenchants.enchants.EnchantJumpBoost;
import org.bukkit.*;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 * Is it safe to have all these in one listener? I mean... better than having a bunch... maybe?
 */
public class EnchantListeners implements Listener {

    private EnchantmentManager em = EnchantmentManager.getInstance();

    private Set<UUID> prevPlayersOnGround = Sets.newHashSet(); // For the jump thing

    Random rand = new Random(); // For generating odds

    /* For the actual enchantments */

    @EventHandler
    public void onPlayerJump(PlayerStatisticIncrementEvent event){
        if(event.getStatistic() == Statistic.JUMP){
            Player player = event.getPlayer();
            if(player.getInventory().getBoots() == null) return;
            if(player.getInventory().getBoots().getEnchantments().containsKey(EnchantmentManager.JUMP_BOOST)) {
                double jumpLevel = em.getJumpBoostLevel(player.getInventory().getBoots()) * .2;
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE); // In case they already have it one
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 10));
                player.setVelocity(new Vector(player.getVelocity().getX(), player.getVelocity().getY() + jumpLevel, player.getVelocity().getZ()));
            }
        }
    }

    // TODO: Make a method that replaces "itemStack.getEnchantments().containsKey()" -- Does the same thing under the hood but may be a bit cleaner

    @EventHandler
    public void enchantOnEntityHit(EntityDamageByEntityEvent event){
        /* GLOBAL USAGE */
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        double damage = event.getFinalDamage();

        /* Player is the one damaged */
        if(damaged instanceof Player){
            Player player = (Player) damaged;
            /* Conversion Enchantment - Converts damage to health */
            try {
                ItemStack chestplate = player.getInventory().getChestplate();
                if (chestplate.getEnchantments().containsKey(EnchantmentManager.CONVERSION)) {
                    boolean converts = em.calculateConversion(chestplate.getEnchantments().get(EnchantmentManager.CONVERSION));
                    if (converts) {
                        event.setCancelled(true);
                        // Removed by request; player.sendTitle("", "" + ChatColor.DARK_RED + "<3", 20, 10, 20);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f, 1.0f);
                        if (player.getHealth() + damage >= 20.0) {
                            player.setHealth(20.0);
                        } else {
                            player.setHealth(player.getHealth() + damage);
                        }
                    }
                }
            } catch(NullPointerException e){
                // I hate doing this, but I have no idea why I can't cancel the check earlier.
            }
        }

        /* Player is the damager */
        if(damager instanceof Player) {
            /* Glowing Strike Enchantment */
            Player player = (Player) damager;
            Entity victim = damaged;
            ItemStack sword = player.getInventory().getItemInMainHand();
            if (sword.getEnchantments().containsKey(EnchantmentManager.GLOW_STRIKE)) {
                damaged.setGlowing(true);
                EnchantTimer et = new EnchantTimer(10, damaged);
                et.runTaskTimer(Main.getInstance(), 0, 20L);
            }

            /* Dog master enchantment -- 10% chance of spawning at level 1*/
            if (sword.getEnchantments().containsKey(EnchantmentManager.DOG_MASTER)){
                if(player.isSneaking()) return; // Cancel if someone is sneaking
                int odds = rand.nextInt(100);
                if(odds >= 5) return;
                Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
                wolf.setAdult();
                wolf.setAngry(true);
                wolf.setCollarColor(DyeColor.ORANGE);
                wolf.setCustomName("" + ChatColor.GOLD + ChatColor.BOLD + player.getName() + "'s Summoned Wolf");
                wolf.setOwner(player);
                if(victim instanceof LivingEntity)  wolf.setTarget((LivingEntity)victim);
                // TODO: Kill wolves after a certain time
            }

            /* Necromancer enchantment  -- 20% chance of spawning*/
            if(sword.getEnchantments().containsKey(EnchantmentManager.NECROMANCER)){
                int odds = rand.nextInt(100);
                if(odds >= 10) return;
                Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
                if(victim instanceof LivingEntity)  zombie.setTarget((LivingEntity)victim);
                zombie.setCustomName("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + player.getName() + "'s Summoned Zombie");
                zombie.setHealth(1);
                player.sendMessage("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + "*zombie noises*");
            }

            /* Rust Enchantment */
            if(sword.getEnchantments().containsKey(EnchantmentManager.RUST)){
                int odds = rand.nextInt(100);
                if(odds >= 10) return;
                if(victim instanceof LivingEntity) ((LivingEntity) victim).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*4, 1));
                // This is towny, so no pvp... hitter.sendMessage("Poisoned!");
            }
        }

    }

    private ArrayList<Arrow> explosiveArrows = new ArrayList<>();
    @EventHandler
    public void enchantOnArrowShoot(EntityShootBowEvent event){
        ItemStack bow = event.getBow();
        if(!(event.getProjectile() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getProjectile();
        /* Explosive Arrows Enchantment Pt. 2*/
        try {
            if (bow.getEnchantments().containsKey(EnchantmentManager.EXPLOSIVE_ARROWS)) {
                explosiveArrows.add(arrow);
            }
        } catch(NullPointerException e){
            // I hate doing this, but I have no idea why I can't cancel the check earlier.
        }
    }

    @EventHandler
    public void enchantOnProjectileHit(ProjectileHitEvent event){
        /* Projectile is an arrow */
        if(event.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            /* Explosive Arrows Enchantment Pt. 1*/
            if (explosiveArrows.contains(arrow)) {
                explosiveArrows.remove(arrow);
                Location loc = arrow.getLocation();
                loc.getWorld().createExplosion(loc.getBlockX(), loc.getY(), loc.getZ(), 2.0f, false, false);
            }
        }
        /* Deflection enchantment */
        if(event.getHitEntity() instanceof Player){
            Player damaged = (Player) event.getHitEntity();
            Projectile proj = event.getEntity();

            // TODO: Make a method that just checks all armor for enchantments
            try {
                ItemStack chestplate = damaged.getInventory().getChestplate();
                ItemStack leggings = damaged.getInventory().getLeggings();
                if(chestplate.getEnchantments().containsKey(EnchantmentManager.DEFLECT) || leggings.getEnchantments().containsKey(EnchantmentManager.DEFLECT)){
                    Projectile newProj = (Projectile) damaged.getWorld().spawnEntity(damaged.getLocation(), proj.getType());
                    newProj.setShooter(damaged);
                    newProj.setVelocity(proj.getVelocity().rotateAroundY(180));
                }
            } catch (NullPointerException e){
                // Not wearing any armor
            }
        }
    }
}
