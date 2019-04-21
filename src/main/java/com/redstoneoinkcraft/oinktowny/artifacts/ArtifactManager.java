package com.redstoneoinkcraft.oinktowny.artifacts;

import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArtifactManager {

    private static ArtifactManager instance = new ArtifactManager();
    public static ArtifactManager getInstance(){
        return instance;
    }

    private String label = "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "ARTIFACT";
    private String usesLabel = "" + ChatColor.RED + ChatColor.BOLD + "USES: ";

    private ArtifactManager(){}

    public boolean isItemStackAnArtifact(ItemStack is){
        return is.getEnchantments().containsKey(EnchantmentManager.ARTIFACT);
    }

    /* General Artifact Commands */
    public ItemStack initializeArtifact(Material material, ArtifactType type, int uses){
        ItemStack artifact = new ItemStack(material);
        ItemMeta meta = artifact.getItemMeta();

        meta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + type.toString());
        meta.addEnchant(EnchantmentManager.ARTIFACT, 1, true);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(label);
        lore.add(usesLabel + uses);
        meta.setLore(lore);

        artifact.setItemMeta(meta);
        return artifact;
    }

    public ArtifactType getArtifactType(ItemStack item){
        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        return ArtifactType.valueOf(name);
    }

    /* Effecting item information via lore */
    public int getUses(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        for (String str : meta.getLore()){
            if (str.contains("USES: ")) {
                return Integer.parseInt(str.substring(6 + 4)); // +4 for the color codes
            }
        }

        return 0;
    }

    public void setUses(Player player, ItemStack item, int newAmount){
        if(newAmount <= 0){
            item.setAmount(0);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 1);
            return;
        }
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        for(int i = 0; i < lore.size(); i++){
            if(lore.get(i).contains("USES: ")){
                lore.set(i, usesLabel + newAmount);
                break;
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /* Artifact Specific Methods */
    public ItemStack createJackhammer(){
        return initializeArtifact(Material.DIAMOND_PICKAXE, ArtifactType.JACKHAMMER, 20);
    }

    public void jackhammerBreak(Block block, BlockFace face){
        // Break surrounding blocks, depending on which face of the block is hit.
        if(face == BlockFace.UP || face == BlockFace.DOWN){
            block.getRelative(BlockFace.NORTH).setType(Material.AIR);
            block.getRelative(BlockFace.SOUTH).setType(Material.AIR);
            block.getRelative(BlockFace.EAST).setType(Material.AIR);
            block.getRelative(BlockFace.WEST).setType(Material.AIR);
            block.getRelative(BlockFace.NORTH_EAST).setType(Material.AIR);
            block.getRelative(BlockFace.SOUTH_EAST).setType(Material.AIR);
            block.getRelative(BlockFace.NORTH_WEST).setType(Material.AIR);
            block.getRelative(BlockFace.SOUTH_WEST).setType(Material.AIR);
        }
        // I guess I'm just going with individual cases ((((::::
        if(face == BlockFace.NORTH || face == BlockFace.SOUTH || face == BlockFace.EAST || face == BlockFace.WEST){
            block.getRelative(BlockFace.UP).setType(Material.AIR);
            block.getRelative(BlockFace.DOWN).setType(Material.AIR);
            if(face == BlockFace.NORTH || face == BlockFace.SOUTH){
                block.getRelative(BlockFace.EAST).setType(Material.AIR);
                block.getRelative(BlockFace.EAST).getRelative(BlockFace.UP).setType(Material.AIR);
                block.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).setType(Material.AIR);
                block.getRelative(BlockFace.WEST).setType(Material.AIR);
                block.getRelative(BlockFace.WEST).getRelative(BlockFace.UP).setType(Material.AIR);
                block.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).setType(Material.AIR);
            }
            if(face == BlockFace.EAST || face == BlockFace.WEST){
                block.getRelative(BlockFace.NORTH).setType(Material.AIR);
                block.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).setType(Material.AIR);
                block.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).setType(Material.AIR);
                block.getRelative(BlockFace.SOUTH).setType(Material.AIR);
                block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
                block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).setType(Material.AIR);
            }
        }
        block.setType(Material.AIR);

        // Create particle effect
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 100, block.getBlockData());
        block.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
    }

    public ItemStack createGravityShifter(){
        return initializeArtifact(Material.ENDER_EYE, ArtifactType.GRAVITY_SHIFTER, 10);
    }

    public void gravityShift(Player player){
        PotionEffect gravity = new PotionEffect(PotionEffectType.LEVITATION, 16*20, 1, true, true); // *20 for the ticks
        player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1, 1);
    }

    public ItemStack createHealthShifter(){
        return initializeArtifact(Material.POISONOUS_POTATO, ArtifactType.HEALTH_SHIFTER, 4);
    }

    public void healthShift(Player player){
        double health = player.getHealth();
        double saturation = player.getSaturation();
        player.setHealth(saturation);
        player.setSaturation((float)health);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    public ItemStack createDestructoid(){
        return initializeArtifact(Material.GUNPOWDER, ArtifactType.DESTRUCTOID, 2);
    }

    public void destruct(Player player){
        Location loc = player.getLocation();
        player.getWorld().createExplosion(loc.getBlockX(), loc.getY(), loc.getZ(), 2.0f, false, false);
    }

}
