package com.redstoneoinkcraft.oinktowny.ruins;

import com.redstoneoinkcraft.oinktowny.ruins.creation.RuinDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsObj {

    private String name;
    private Location lobby;
    private ArrayList<Sign> joinSigns = new ArrayList<>(2);
    private RuinDifficulty difficulty; // TODO
    private LinkedList<RuinsObjLevel> levels = new LinkedList<>();
    private ArrayList<ItemStack> rewardItems = new ArrayList<>(9);

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Location getLobby(){
        return lobby;
    }

    public void setLobby(Location location){
        this.lobby = location;
    }

    public ArrayList<Sign> getJoinSigns(){
        return this.joinSigns;
    }

    public void addJoinSign(Sign sign){
        joinSigns.add(sign);
    }

    public LinkedList<RuinsObjLevel> getLevels(){
        return levels;
    }

    public RuinsObjLevel getFirstLevel(){
        return levels.getFirst();
    }

    public RuinsObjLevel getLatestLevel(){
        return levels.getLast();
    }

    public void addLevel(RuinsObjLevel level){
        levels.add(level);
    }

    public ArrayList<ItemStack> getRewardItems(){
        return rewardItems;
    }

    public void setRewardItems(Inventory inventory){
        for(int i = 0; i < inventory.getContents().length; i++){
            if(inventory.getItem(i) != null) {
                rewardItems.add(inventory.getItem(i));
            }
        }
    }

    public void setRewardItems(ArrayList<ItemStack> itemList){
        this.rewardItems = itemList;
    }

    private HashMap<Player, RuinsObjLevel> playerCurrentLevel = new HashMap<>();

    public HashMap<Player, RuinsObjLevel> getPlayerCurrentLevel() {
        return playerCurrentLevel;
    }

    public RuinsObjLevel nextLevel(RuinsObjLevel currentLevel){
        int nextIndex = levels.indexOf(currentLevel) + 1;
        return levels.get(nextIndex);
    }
}
