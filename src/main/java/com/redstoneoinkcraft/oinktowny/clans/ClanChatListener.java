package com.redstoneoinkcraft.oinktowny.clans;

import com.redstoneoinkcraft.oinktowny.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ClanChatListener implements Listener {

    Main mainInstance = Main.getInstance();
    ClanManager cm = ClanManager.getInstance();

    // Current clan chat character: %
    @EventHandler
    public void onClanMemberChat(AsyncPlayerChatEvent event){
        if(!event.getMessage().substring(0, 1).equalsIgnoreCase("%")) return;
        if(!cm.playerHasClan(event.getPlayer())) return;
        ClanObj clan = cm.getPlayerClan(event.getPlayer());
        String clanPrefix = ChatColor.GOLD + "Clan Chat >> " + ChatColor.DARK_AQUA + event.getPlayer().getName() + ": "
                + ChatColor.GRAY;
        String newMessage = clanPrefix + event.getMessage().substring(1);
        for(UUID playerIds : clan.getMemberIds()){
            Player player = Bukkit.getPlayer(playerIds);
            if(player == null) continue;
            if(player.isOnline()){
                player.sendMessage(newMessage);
            }
        }
        event.setCancelled(true);
    }

}
