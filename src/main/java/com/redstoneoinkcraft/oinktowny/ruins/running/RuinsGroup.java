package com.redstoneoinkcraft.oinktowny.ruins.running;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsGroup {

    private ArrayList<Player> playersInGroup = new ArrayList<>(2);

    public RuinsGroup(Player player){
        playersInGroup.add(player);
    }

    public int getPlayerNumber(){
        return playersInGroup.size();
    }

    public void sendMessageToPlayers(String message) {
        for (Player player : playersInGroup) {
            player.sendMessage(message);

        }
    }

}
