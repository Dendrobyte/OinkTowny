package com.redstoneoinkcraft.oinktowny.clans;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ClanScoreboardTimer extends BukkitRunnable {

    int seconds = 10;
    Player player;
    Scoreboard scoreboard;

    public ClanScoreboardTimer(Player player, Scoreboard scoreboard){
        this.player = player;
        this.scoreboard = scoreboard;
    }

    @Override
    public void run() {
        if(seconds == 0){
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            cancel();
        } else {
            seconds--;
        }
    }
}
