package com.redstoneoinkcraft.oinktowny.ruins;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsManager {

    private static RuinsManager instance = new RuinsManager();
    private String ruinsPrefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "TownyRuins" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY;

    public static RuinsManager getInstance(){
        return instance;
    }

    private RuinsManager(){}

    // Main initializing method called in the main file (just a fun name for it)
    public void rebuildRuins(){
        // TODO
    }

    /* Ruins creation objects */
    HashMap<Player, RuinsCreationStates> playerCreationStates = new HashMap<Player, RuinsCreationStates>();
    /* Ruins creation methods */

    public void initiateRuinsCreation(Player player, String name){
        player.sendMessage(ruinsPrefix + "Welcome to the Towny Ruins creation wizard! This wizard will use a combination of chat instructions and selection.");
        playerCreationStates.put(player, RuinsCreationStates.LOBBY);

    }

    public boolean isPlayerCreatingRuins(Player player){
        return playerCreationStates.containsKey(player);
    }

    public RuinsCreationStates getPlayerCreationState(Player player){
        if(!isPlayerCreatingRuins(player)) return null;
        return playerCreationStates.get(player);
    }

    public void changePlayerCreationState(Player player, RuinsCreationStates state){
        playerCreationStates.put(player, state);
    }

    public void endRuinsCreation(Player player, RuinsObj ruins){
        playerCreationStates.remove(player);
    }

    /* Ruins running methods */

}
