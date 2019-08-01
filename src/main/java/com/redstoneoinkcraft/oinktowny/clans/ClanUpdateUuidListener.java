package com.redstoneoinkcraft.oinktowny.clans;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ClanUpdateUuidListener implements Listener {

    ClanManager cm = ClanManager.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!cm.playerHasClan(player)) return; // Don't care about storing/updating players not in a clan
        String name = player.getName();
        String uuid = player.getUniqueId().toString();

        String checkFullString = name + ":" + uuid;
        List<String> storedPlayers = Main.getInstance().getClansConfig().getStringList("stored-players");
        if(storedPlayers.contains(checkFullString)) return;
        // This is going to take a while with a lot of players, so in the future I'll load this at startup... But it won't be used often so maybe that won't ever have to happen?

        for(String str : storedPlayers){
            String justID = str.substring(0, str.indexOf(":"));
            if(justID.equalsIgnoreCase(uuid)){
                String storedName = str.substring(str.indexOf(":") + 1);
                if(storedName.equalsIgnoreCase(name)) return;
                storedPlayers.remove(str);
                storedPlayers.add(checkFullString);
                Main.getInstance().getClansConfig().set("stored-players", storedPlayers);
                Main.getInstance().saveClansConfig();
                return;
            }
        }
        storedPlayers.add(checkFullString);
        Main.getInstance().getClansConfig().set("stored-players", storedPlayers);
        Main.getInstance().saveClansConfig();

    }

}
