package com.redstoneoinkcraft.oinktowny.arenapvp;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaTimer extends BukkitRunnable {

    ArenaObj workingArena;
    int timer = 10;
    String prefix = ArenaPVPManager.getInstance().getArenaPrefix();

    public ArenaTimer(ArenaObj workingArena){
        this.workingArena = workingArena;
    }

    @Override
    public void run(){
        if(timer == 0){
            System.out.println("5: Timer hit zero");
            workingArena.sendPlayersMessage(prefix + ChatColor.GOLD + ChatColor.BOLD + "BEGIN!");
            workingArena.setCanHitEachOther(true);
            Player playerOne = workingArena.getPlayerOne();
            Player playerTwo = workingArena.getPlayerTwo();
            playerOne.teleport(workingArena.getSpawn_one());
            playerTwo.teleport(workingArena.getSpawn_two());
            System.out.println("6: Players teleported!");
            playerOne.setHealth(playerOne.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
            playerTwo.setHealth(playerTwo.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
            System.out.println("8: Arena to be set to running!");
            workingArena.setStatus(ArenaStatus.RUNNING);
            cancel();
            return;
        }
        if(timer == 1){
            workingArena.sendPlayersMessage(prefix + "Match starts in " + ChatColor.GOLD + ChatColor.BOLD + timer + ChatColor.getLastColors(prefix) + " second!");

        }
        else if (timer == 10 || timer < 4){
            workingArena.sendPlayersMessage(prefix + "Match starts in " + ChatColor.GOLD + ChatColor.BOLD + timer + ChatColor.getLastColors(prefix) + " seconds!");
        }
        timer--;
    }

}
