package com.redstoneoinkcraft.oinktowny.artifacts;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
import com.redstoneoinkcraft.oinktowny.regions.RegionsManager;
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
    private String prefix = Main.getInstance().getPrefix();

    private ArtifactManager(){}

    public boolean isItemStackAnArtifact(ItemStack is){
        return is.getEnchantments().containsKey(EnchantmentManager.ARTIFACT);
    }

    /* General Artifact Commands */
    public ItemStack initializeArtifact(Material material, ArtifactType type, int uses, int amount){
        ItemStack artifact = new ItemStack(material, amount);
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
        if(item.getAmount() > 1){
            int newUses = getUses(item);
            ItemStack newStack = initializeArtifact(item.getType(), ArtifactType.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName())), newUses, item.getAmount()-1);

            item.setAmount(1);
            addArtifactToPlayerInventory(player, newStack);
        }
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

    private void addArtifactToPlayerInventory(Player player, ItemStack itemStack){
        boolean emptyFound = false;
        for(int i = 0; i < player.getInventory().getSize()-5; i++){
            if(player.getInventory().getItem(i) == null){
                player.getInventory().setItem(i, itemStack);
                emptyFound = true;
                break;
            }
        }
        if(!emptyFound){
            player.getWorld().dropItem(player.getLocation(), itemStack);
        }
    }

    /*public ItemStack createArtifact(String artifactType, int uses){
        ArtifactType type = ArtifactType.valueOf(artifactType);
        return initializeArtifact()
    }*/

    /* Artifact Specific Methods */
    public ItemStack createJackhammer(){
        return initializeArtifact(Material.DIAMOND_PICKAXE, ArtifactType.JACKHAMMER, 20, 1);
    }

    public boolean jackhammerBreak(Block block, BlockFace face, Player player){
        ArrayList<Block> blocksToBreak = new ArrayList<>();
        // Break surrounding blocks, depending on which face of the block is hit.
        if(face == BlockFace.UP || face == BlockFace.DOWN){
            blocksToBreak.add(block.getRelative(BlockFace.NORTH));
            blocksToBreak.add(block.getRelative(BlockFace.SOUTH));
            blocksToBreak.add(block.getRelative(BlockFace.EAST));
            blocksToBreak.add(block.getRelative(BlockFace.WEST));
            blocksToBreak.add(block.getRelative(BlockFace.NORTH_EAST));
            blocksToBreak.add(block.getRelative(BlockFace.SOUTH_EAST));
            blocksToBreak.add(block.getRelative(BlockFace.NORTH_WEST));
            blocksToBreak.add(block.getRelative(BlockFace.SOUTH_WEST));
        }
        // I guess I'm just going with individual cases ((((::::
        if(face == BlockFace.NORTH || face == BlockFace.SOUTH || face == BlockFace.EAST || face == BlockFace.WEST){
            blocksToBreak.add(block.getRelative(BlockFace.UP));
            blocksToBreak.add(block.getRelative(BlockFace.DOWN));
            if(face == BlockFace.NORTH || face == BlockFace.SOUTH){
                blocksToBreak.add(block.getRelative(BlockFace.EAST));
                blocksToBreak.add(block.getRelative(BlockFace.EAST).getRelative(BlockFace.UP));
                blocksToBreak.add(block.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN));
                blocksToBreak.add(block.getRelative(BlockFace.WEST));
                blocksToBreak.add(block.getRelative(BlockFace.WEST).getRelative(BlockFace.UP));
                blocksToBreak.add(block.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN));
            }
            if(face == BlockFace.EAST || face == BlockFace.WEST){
                blocksToBreak.add(block.getRelative(BlockFace.NORTH));
                blocksToBreak.add(block.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP));
                blocksToBreak.add(block.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN));
                blocksToBreak.add(block.getRelative(BlockFace.SOUTH));
                blocksToBreak.add(block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP));
                blocksToBreak.add(block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN));
            }
        }
        blocksToBreak.add(block);

        // Make sure we aren't affecting a region claim
        RegionsManager rm = RegionsManager.getInstance();
        if(rm.containsClaimedBlock(blocksToBreak, player)){
            player.sendMessage(prefix + ChatColor.RED + "You can not edit this claim.");
            return false;
        }

        for(Block b : blocksToBreak){
            b.breakNaturally();
        }

        // Create particle effect
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 100, block.getBlockData());
        block.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        return true;
    }

    public ItemStack createGravityShifter(){
        return initializeArtifact(Material.ENDER_EYE, ArtifactType.GRAVITY_SHIFTER, 10, 1);
    }

    public void gravityShift(Player player){
        int hoverTime = 8*20; // *20 for the ticks
        PotionEffect gravity = new PotionEffect(PotionEffectType.LEVITATION, hoverTime, 1, true, true);
        player.addPotionEffect(gravity);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, hoverTime+80, 1, true, true)); // Extend resistance to prevent fall damage
        player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1, 1);
    }

    public ItemStack createHealthShifter(){
        return initializeArtifact(Material.POISONOUS_POTATO, ArtifactType.HEALTH_SHIFTER, 4, 1);
    }

    public void healthShift(Player player){
        double health = player.getHealth();
        double saturation = player.getFoodLevel();
        player.setHealth(saturation);
        player.setFoodLevel((int)health);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    public ItemStack createDestructoid(){
        return initializeArtifact(Material.GUNPOWDER, ArtifactType.DESTRUCTOID, 2, 1);
    }

    public void destruct(Player player){
        Location loc = player.getLocation();
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 8, true, true));
        player.getWorld().createExplosion(loc.getBlockX(), loc.getY(), loc.getZ(), 2.0f, false, false);
    }

}