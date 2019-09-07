package com.redstoneoinkcraft.oinktowny.lockette;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LocketteActiveChestTimer extends BukkitRunnable {

    private int counter = 10;
    private Chest chest;
    private Player player;

    LocketteManager lm = LocketteManager.getInstance();

    public LocketteActiveChestTimer(Chest chest, Player player){
        this.chest = chest;
        this.player = player;
    }

    @Override
    public void run() {
        if(counter == 0){
            if(lm.isDoubleChest(chest)) {
                DoubleChest doubleChest = lm.toDoubleChest(chest);
                Chest otherHalf = lm.getOtherHalfOfDouble(doubleChest, chest);
                if(lm.isLocketteChest(otherHalf)){
                    if(!lm.playerOwnsChest(player, otherHalf)){
                        player.sendMessage(Main.getInstance().getPrefix() + "The other half of this chest is not owned by you. Chest creation canceled.");
                        cancel();
                        chest.getBlock().breakNaturally();
                    }
                }
            }
        }
        if(counter <= 0){
            this.cancel();
            lm.removeActiveChest(chest);
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LocketteTimer" + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW + " Chest timer expired.");
        } else {
            if(counter == 10 || counter == 5){
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LocketteTimer" + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW +
                        " You have " + ChatColor.GRAY + counter + " seconds " + ChatColor.YELLOW + "to private your chest!");
            }
            counter--;
        }
    }
}
