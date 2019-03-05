package com.redstoneoinkcraft.oinktowny.clans;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class InviteTimer extends BukkitRunnable {

    private int counter = 20;
    private Player player;
    private Player invitee;
    ClanManager cm = ClanManager.getInstance();

    public InviteTimer(Player invitee, Player player) {
        this.player = player;
        this.invitee = invitee;
    }

    public void run() {
        if (this.counter == 0) {
            cm.inviteTimerExpired(invitee, player);
            this.cancel();
        }
        else {
            this.counter -= 1;
        }
    }
}
