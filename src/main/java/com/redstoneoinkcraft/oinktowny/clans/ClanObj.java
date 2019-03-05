package com.redstoneoinkcraft.oinktowny.clans;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ClanObj {

    private int id;
    private ArrayList<UUID> members = new ArrayList<>();
    private UUID leader;

    public ClanObj(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public UUID getLeaderId(){
        return leader;
    }

    public void setLeaderId(UUID playerId){
        leader = playerId;
    }

    public ArrayList<UUID> getMemberIds(){
        return members;
    }

    public void addMemberId(UUID playerId){
        members.add(playerId);
    }

    public String toString(){
        return "Clan " + id + "'s leader is " + leader + " and contains members: " + members.toString();
    }

}
