package com.redstoneoinkcraft.oinktowny.regions;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.clans.ClanManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class SuperpickListeners implements Listener {

    RegionsManager rm = RegionsManager.getInstance();
    ClanManager cm = ClanManager.getInstance();
    String prefix = Main.getInstance().getPrefix();

    @EventHandler // When they leave, remove them from the superpick list
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(rm.isSuperpick(player)){
            rm.toggleSuperpick(player);
        }
    }

    @EventHandler // Remove them from the superpick list if they teleport to a different world
    public void onPlayerTeleport(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        if(!rm.isSuperpick(player)) return;
        rm.toggleSuperpick(player);
        player.sendMessage(prefix + "(You teleported into a different world)");
    }

    @EventHandler // Remove them from the superpick list if they die
    public void onPlayerDeathWithSuperick(PlayerDeathEvent event){
        Player player = event.getEntity();
        if(!rm.isSuperpick(player)) return;
        rm.toggleSuperpick(player);
        player.sendMessage(prefix + "(You died... take a breath and try again!)");
    }

    @EventHandler // Immediately break block if activated and if holding pick
    public void onBlockHit(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!Main.getInstance().isTownyWorld(player.getWorld().getName())) return;
        if(!rm.isSuperpick(player)) return;

        if(event.getAction() != Action.LEFT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) return;
        Material blockType = event.getClickedBlock().getType();
        if(blockType == Material.BEDROCK || blockType == Material.OBSIDIAN) return;
        ItemStack pick = event.getItem();
        if(pick == null) return;
        if (!(pick.getType().toString().equals("IRON_PICKAXE") || pick.getType().toString().equals("DIAMOND_PICKAXE"))) return;

        // Check if they can edit there
        if(!rm.canPlayerEdit(event.getClickedBlock().getChunk(), player)){
            player.sendMessage(prefix + "You can not superpick here. The chunk is claimed.");
            return;
        }

        // Do damage to pickaxe
        ItemMeta pickMeta = pick.getItemMeta();
        Damageable pickDamageable = (Damageable) pickMeta;
        int currDamage = pickDamageable.getDamage();
        pickDamageable.setDamage(currDamage+1);
        pick.setItemMeta((ItemMeta)pickDamageable);
        if(pick.getType().getMaxDurability()-pickDamageable.getDamage() <= 1){
            pick.setAmount(0);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
        }

        // Break block
        event.getClickedBlock().breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
    }

}
