package com.redstoneoinkcraft.oinktowny.ruins;

import org.bukkit.Location;
import org.bukkit.entity.Monster;

import java.util.ArrayList;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsObjLevel {

    private Location spawnLocation;
    private ArrayList<String> monsters = new ArrayList<>();
    private ArrayList<Location> monsterSpawnLocations = new ArrayList<>(2);

    public RuinsObjLevel(Location spawnLocation){
        this.spawnLocation = spawnLocation;
    }

    public ArrayList<String> getMonsters() {
        return this.monsters;
    }

    // Formatted MONSTER;AMOUNT
    public void addMonster(String monster){
        monsters.add(monster);
    }

    public Location getSpawnLocation(){
        return spawnLocation;
    }

    public void addMonsterSpawnLocation(Location spawnLoc){
        monsterSpawnLocations.add(spawnLoc);
    }

    public ArrayList<Location> getMonsterSpawnLocations(){
        return monsterSpawnLocations;
    }

    public void setMonsterSpawnLocations(ArrayList<Location> replacementSpawnLocations){
        monsterSpawnLocations = replacementSpawnLocations;
    }

}
