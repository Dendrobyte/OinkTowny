package com.redstoneoinkcraft.oinktowny.customenchants.utils;

import com.google.common.collect.Sets;
import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
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

    @EventHandler
    public void enchantOnPlayerMove(PlayerMoveEvent event) {
        /* Jumping check
         * Taken from: https://bukkit.org/threads/detect-player-jump.445415/ */
        Player player = event.getPlayer();
        if (player.getVelocity().getY() > 0) {
            double jumpVelocity = (double) 0.42F;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += (double) ((float) (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F);
            }
            if (event.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
                if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
                    // Enchantment check
                    ItemStack boots = player.getInventory().getBoots();
                    try {
                        if (boots.getEnchantments().isEmpty()) return;
                        if (boots.getEnchantments().containsKey(EnchantmentManager.JUMP_BOOST)) {
                            int level = boots.getEnchantments().get(EnchantmentManager.JUMP_BOOST);
                            player.setVelocity(player.getVelocity().setY(player.getVelocity().getY() + ((double)level/8)));
                        }
                    } catch(NullPointerException e){
                        // I hate doing this, but I have no idea why I can't cancel the check earlier.
                    }
                }
            }
        }
        if (player.isOnGround()) {
            prevPlayersOnGround.add(player.getUniqueId());
        } else {
            prevPlayersOnGround.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void enchantOnEntityHit(EntityDamageByEntityEvent event){
        /* GLOBAL USAGE */
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        double damage = event.getFinalDamage();

        /* Player is the one damaged */
        if(damaged instanceof Player){
            /* Conversion Enchantment - Converts damage to health */
            Player player = (Player) damaged;
            ItemStack chestplate = player.getInventory().getChestplate();
            try {
                if (chestplate.getEnchantments().containsKey(EnchantmentManager.CONVERSION)) {
                    boolean converts = em.calculateConversion(chestplate.getEnchantments().get(EnchantmentManager.CONVERSION));
                    if (converts) {
                        event.setCancelled(true);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f, 4.0f);
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
            ItemStack sword = player.getInventory().getItemInMainHand();
            if (sword.getEnchantments().containsKey(EnchantmentManager.GLOW_STRIKE)) {
                damaged.setGlowing(true);
                EnchantTimer et = new EnchantTimer(10, damaged);
                et.runTaskTimer(Main.getInstance(), 0, 20L);
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
    public void enchantOnArrowHit(ProjectileHitEvent event){
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
    }
}
