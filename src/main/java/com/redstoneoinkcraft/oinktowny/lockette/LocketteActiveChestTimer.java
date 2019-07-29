package com.redstoneoinkcraft.oinktowny.lockette;

import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LocketteActiveChestTimer extends BukkitRunnable {

    private int counter = 30;
    private Chest chest;
    private Player player;

    LocketteManager lm = LocketteManager.getInstance();

    public LocketteActiveChestTimer(Chest chest, Player player){
        this.chest = chest;
        this.player = player;
    }

    @Override
    public void run() {
        if(counter <= 0){
            this.cancel();
            lm.removeActiveChest(chest);
        } else {
            if(counter == 20 || counter == 10){
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LocketteTimer" + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW +
                        " You have " + ChatColor.GRAY + counter + " seconds " + ChatColor.YELLOW + "to private your chest!");
            }
            counter--;
        }
    }
}
