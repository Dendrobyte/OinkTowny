package com.redstoneoinkcraft.oinktowny.bettersleep;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class SleepManager {

    private static SleepManager instance = new SleepManager();

    private int playersSleeping = 0;
    private int totalPlayersInWorld = 0;
    private ArrayList<Player> asleepPlayers = new ArrayList<>();

    private SleepManager(){}

    public static SleepManager getInstance(){
        return instance;
    }

    public int getPlayersSleeping(){
        return playersSleeping;
    }

    public void resetPlayerSleeping(){
        playersSleeping = 0;
        asleepPlayers.clear();
    }

    public void addPlayerSleeping(Player player){
        asleepPlayers.add(player);
        playersSleeping++;
    }

    public void removePlayerSleeping(Player player){
        asleepPlayers.remove(player);
        playersSleeping--;
    }

    public ArrayList<Player> getAsleepPlayers(){
        return asleepPlayers;
    }

    public int setTotalPlayers(int total){
        totalPlayersInWorld = total;
        return total;
    }

}
