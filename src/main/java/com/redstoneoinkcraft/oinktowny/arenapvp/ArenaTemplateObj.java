package com.redstoneoinkcraft.oinktowny.arenapvp;

import org.bukkit.Location;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaTemplateObj {

    private Location arenaLoc, lobby, spawn_one, spawn_two;
    private String name;

    public ArenaTemplateObj(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setArenaLoc(Location loc){
        arenaLoc = loc;
    }

    public void setLobby(Location loc){
        lobby = loc;
    }

    public void setSpawn_one(Location loc){
        spawn_one = loc;
    }

    public void setSpawn_two(Location loc){
        spawn_two = loc;
    }

    public Location getArenaLoc(){
        return arenaLoc;
    }

    public Location getLobby(){
        return lobby;
    }

    public Location getSpawn_one(){
        return spawn_one;
    }

    public Location getSpawn_two(){
        return spawn_two;
    }

}
