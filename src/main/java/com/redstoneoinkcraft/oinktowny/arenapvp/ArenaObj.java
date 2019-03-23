package com.redstoneoinkcraft.oinktowny.arenapvp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaObj {

    private Location arenaLoc, lobby, spawn_one, spawn_two;
    private String name;
    private ArenaStatus status;
    private Player playerOne, playerTwo;
    private boolean canHitEachOther = false;
    private Sign arenaSign;

    public ArenaObj(String name){
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

    public void setStatus(ArenaStatus status){
        this.status = status;
    }

    public void setPlayerOne(Player player){
        playerOne = player;
    }

    public void setPlayerTwo(Player player){
        playerTwo = player;
    }

    public void setCanHitEachOther(boolean value){
        canHitEachOther = value;
    }

    public void setArenaSign(Sign sign){
        arenaSign = sign;
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

    public ArenaStatus getStatus(){
        return this.status;
    }

    public Player getPlayerOne(){
        return playerOne;
    }

    public Player getPlayerTwo(){
        return playerTwo;
    }

    public boolean getCanHitEachOther(){
        return canHitEachOther;
    }

    public Sign getArenaSign(){
        return arenaSign;
    }

    public void setAllValues(Location arenaLoc, Location lobby, Location spawn_one, Location spawn_two, ArenaStatus status){
        setArenaLoc(arenaLoc);
        setLobby(lobby);
        setSpawn_one(spawn_one);
        setSpawn_two(spawn_two);
        this.status = status;
    }

    public void sendPlayersMessage(String message){
        playerOne.sendMessage(message);
        playerTwo.sendMessage(message);
    }

    public void resetArena(){
        playerOne = null;
        playerTwo = null;
        setCanHitEachOther(false);
        status = ArenaStatus.WAITING;
        arenaSign.setLine(2, "" + ChatColor.WHITE + ChatColor.BOLD + "WAITING");
        arenaSign.update();
    }

}
