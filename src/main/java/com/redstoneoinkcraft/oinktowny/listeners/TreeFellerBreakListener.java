package com.redstoneoinkcraft.oinktowny.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.ChatColor.DARK_GREEN;
import static org.bukkit.ChatColor.ITALIC;

/**
 * OinkTowny created/started by mobki (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class TreeFellerBreakListener implements Listener {

    @EventHandler
    public void onLogBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        if(!isLog(block)) return;
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if(itemInHand.getType() == Material.IRON_AXE || itemInHand.getType() == Material.GOLDEN_AXE || itemInHand.getType() == Material.DIAMOND_AXE){
            if(itemInHand.getEnchantments().size() != 0) return;
            int blocksBroken = breakTree(block);
            if(blocksBroken > 1) {
                player.sendMessage("" + DARK_GREEN + ITALIC + "Tree Feller activated!");
                int currentHealth = ((Damageable) itemInHand.getItemMeta()).getDamage();
                ItemMeta meta = itemInHand.getItemMeta();
                ((Damageable) meta).setDamage(currentHealth + blocksBroken);
                itemInHand.setItemMeta(meta);
            }
        }
    }

    private int breakTree(Block initialLog){
        int result = 1;
        initialLog.breakNaturally();
        Block up = initialLog.getRelative(BlockFace.UP);
        if(isLog(up)){
            result += breakTree(up);
        }
        Block down = initialLog.getRelative(BlockFace.DOWN);
        if(isLog(down)){
            result += breakTree(down);
        }
        Block north = initialLog.getRelative(BlockFace.NORTH);
        if(isLog(north)){
            result += breakTree(north);
        }
        Block east = initialLog.getRelative(BlockFace.EAST);
        if(isLog(east)){
            result += breakTree(east);
        }
        Block south = initialLog.getRelative(BlockFace.SOUTH);
        if(isLog(south)){
            result += breakTree(south);
        }
        Block west = initialLog.getRelative(BlockFace.WEST);
        if(isLog(west)){
            result += breakTree(west);
        }
        return result;
    }

    private boolean isLog(Block block){
        return block.getType().toString().contains("LOG");
    }

}
