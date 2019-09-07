package com.redstoneoinkcraft.oinktowny.artifacts;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
import com.redstoneoinkcraft.oinktowny.regions.RegionsManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */

// This could honestly be a bit more organized... ._.
public class ArtifactManager {

    private static ArtifactManager instance = new ArtifactManager();
    public static ArtifactManager getInstance(){
        return instance;
    }

    private String label = "" + ChatColor.DARK_GREEN + "Artifact";
    private String usesLabel = "" + ChatColor.RED + "Uses: ";
    private String prefix = Main.getInstance().getPrefix();

    private ArtifactManager(){}

    public boolean isItemStackAnArtifact(ItemStack is){
        try {
            return is.getEnchantments().containsKey(EnchantmentManager.ARTIFACT);
        } catch(NullPointerException e){
            return false;
        }
    }

    /* General Artifact Commands */
    public ItemStack initializeArtifact(Material material, ArtifactType type, int uses, int amount){
        ItemStack artifact = new ItemStack(material, amount);
        ItemMeta meta = artifact.getItemMeta();

        String name = type.toString().replaceAll("_", " ");
        name = name.substring(0, 1) + name.substring(1).toLowerCase();
        meta.setDisplayName("" + ChatColor.GREEN + name);
        meta.addEnchant(EnchantmentManager.ARTIFACT, 1, true);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(label);
        lore.add(usesLabel + uses);
        meta.setLore(lore);

        artifact.setItemMeta(meta);
        return artifact;
    }

    public ArtifactType getArtifactType(ItemStack item){
        if(item == null) return null;
        try {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).toUpperCase();
            name = name.replaceAll(" ", "_");
            return ArtifactType.valueOf(name);
        } catch (NullPointerException e){
            return null;
        }
    }

    /* Effecting item information via lore */
    public int getUses(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        for (String str : meta.getLore()){
            if (str.contains("Uses: ")) {
                return Integer.parseInt(str.substring(6 + 2)); // +2 for the color code
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
            if(getArtifactType(item) == ArtifactType.HEADLAMP) clearPlayerTorches(player);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 1);
            return;
        }
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        for(int i = 0; i < lore.size(); i++){
            if(lore.get(i).contains("Uses: ")){
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
        for(Block illegal : blocksToBreak){
            if(illegal.getType() == Material.BEDROCK || illegal.getType() == Material.OBSIDIAN || illegal.getType() == Material.WATER || illegal.getType() == Material.LAVA){
                blocksToBreak.remove(illegal);
            }
        }

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
        PotionEffect gravity = new PotionEffect(PotionEffectType.LEVITATION, hoverTime, 8, true, true);
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

    public ItemStack createHeadlamp(){
        return initializeArtifact(Material.LEATHER_HELMET, ArtifactType.HEADLAMP, 400, 1);
    }

    private HashMap<Player, Block> torchReplacements = new HashMap<>(2);
    public void replaceTorch(Player player, Block block){
        if(!torchReplacements.containsKey(player)){
            torchReplacements.put(player, block);
        }
        torchReplacements.get(player).setType(Material.AIR);
        block.setType(Material.TORCH);
        torchReplacements.put(player, block);
    }

    public boolean isHeadlampTorch(Player player, Block block){
        return torchReplacements.get(player).equals(block);
    }

    public void clearPlayerTorches(Player player){
        torchReplacements.get(player).setType(Material.AIR);
        torchReplacements.remove(player);
    }

    public ItemStack createTelepoof(){
        return initializeArtifact(Material.FIRE_CHARGE, ArtifactType.TELEPOOF, 10, 1);
    }

    public void poofTeleport(Player player){
        Location playerLoc = player.getLocation();
        playerLoc.getWorld().spawnParticle(Particle.SMOKE_LARGE, playerLoc, 2);
        Random rand = new Random();
        int xChange = rand.nextInt(30) - 20;
        int zChange = rand.nextInt(30) - 20;
        int yChange = rand.nextInt(5); // Only go up
        boolean viableLocation = false;
        Location newLoc = new Location(playerLoc.getWorld(), playerLoc.getBlockX()+xChange, playerLoc.getBlockY()+yChange, playerLoc.getBlockZ()+zChange);
        while(!viableLocation){
            newLoc = new Location(newLoc.getWorld(), newLoc.getBlockX(), newLoc.getBlockY()+4, newLoc.getBlockZ());
            if(newLoc.getBlock().getType().equals(Material.AIR)) viableLocation = true;
        }
        newLoc.getWorld().spawnParticle(Particle.HEART, playerLoc, 100);
        player.teleport(newLoc);
        newLoc.getWorld().playSound(newLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 8, 0);
    }

    public ItemStack createLuckyHoe(){
        return initializeArtifact(Material.DIAMOND_HOE, ArtifactType.LUCKY_HOE, 20, 1);
    }

    public boolean instantGrowth(Block block){
        if(block.getBlockData() instanceof Ageable){
            Ageable crop = (Ageable)block.getBlockData();
            if(crop.getAge() == crop.getMaximumAge()){
                return false;
            }
            crop.setAge(crop.getMaximumAge());
            block.setBlockData(crop);
            block.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, block.getLocation(), 40);
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 4, 0);
            return true;
        } else {
            return false;
        }
    }

}
