package com.redstoneoinkcraft.oinktowny.clans;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ClanManager {
    private Main mainInstance = Main.getInstance();
    private static ClanManager instance = new ClanManager();
    private HashMap<UUID, ClanObj> playerClans = new HashMap<>();
    private ArrayList<ClanObj> cachedClans = new ArrayList<>();
    private FileConfiguration clansConfig = mainInstance.getClansConfig();
    private String prefix = mainInstance.getPrefix();

    public static ClanManager getInstance() {
        return instance;
    }

    private ClanManager() {}

    /* Clan creation */
    public void createClan(Player player){
        if(playerHasClan(player)){
            player.sendMessage(prefix + "You are already in a clan! It's led by " + Bukkit.getServer().getOfflinePlayer(playerClans.get(player.getUniqueId()).getLeaderId()).getName());
            return;
        }
        addNewClan(player);
        player.sendMessage(prefix + "Your clan has been created! Invite others with " + ChatColor.GOLD + "/ot clan invite <name>");
    }

    // Primary method involved in adding a new clan to configuration
    private void addNewClan(Player player){
        // Add player to overall list
        updateConfigPlayerList(player);
        UUID playerId = player.getUniqueId();

        // Construct the player-info value (this method is called upon creation, so set leader to true)
        clansConfig.set("player-info." + playerId + ".leader", true);
        clansConfig.set("number-of-clans", clansConfig.getInt("number-of-clans")+1);
        clansConfig.set("player-info." + playerId + ".clan-id", clansConfig.getInt("number-of-clans"));
        mainInstance.saveClansConfig();

        // Update the cached data
        ClanObj newClan = new ClanObj(clansConfig.getInt("number-of-clans"));
        playerClans.put(playerId, newClan);
        newClan.setLeaderId(playerId);
        newClan.addMemberId(playerId);
        cachedClans.add(newClan);
    }

    /* Clan disband */
    public void disbandClan(Player player){
        if(!playerHasClan(player)){
            player.sendMessage(prefix + "You aren't a part of a clan!");
            return;
        }
        ClanObj clan = getPlayerClan(player);
        if(!playerIsLeader(player, clan)){
            player.sendMessage(prefix + "Only the clan leader can do this action.");
            return;
        }
        removeClan(player);
        player.sendMessage(prefix + "Your clan has successfully been disbanded!");
    }

    // Primary method involved in removing clans from configuration
    private void removeClan(Player player){
        // Remove player from overall list
        List<String> playerIDs = clansConfig.getStringList("player-list");
        UUID playerId = player.getUniqueId();
        String playerIdString = playerId.toString();
        String idToRemove = null;
        for(String str : playerIDs){
            if(str.equalsIgnoreCase(playerIdString)){
                idToRemove = str;
                break;
            }
        }
        playerIDs.remove(idToRemove);
        ClanObj playerClan = playerClans.get(player.getUniqueId());

        // Remove the player-info values
        clansConfig.set("player-info." + playerId, null);

        // Remove all other members
        // TODO: Test this with other people/accounts
        int clanId = playerClans.get(player.getUniqueId()).getId();
        for(String memberId : clansConfig.getConfigurationSection("player-info").getKeys(false)){
            System.out.println("1: " + memberId);
            if(clansConfig.getInt("player-info." + memberId + ".clan-id") == clanId) {
                clansConfig.set("player-info." + memberId, null);
                playerClans.remove(UUID.fromString(memberId));
            }
            System.out.println("2: Removed " + memberId);
            playerIDs.remove(memberId);
        }

        // Clear cached values (hashmap val taken care of in for loop)
        cachedClans.remove(playerClan);
        playerClans.remove(player.getUniqueId());

        // Update overall list and save
        clansConfig.set("player-list", playerIDs);
        mainInstance.saveClansConfig();
    }

    /* Clan invite */
    private HashMap<Player, Player> invitedPlayers = new HashMap<>(); // First player is invitee, second is inviter.
    private HashMap<Player, InviteTimer> inviteTimers = new HashMap<>(); // The player is the invitee

    public void inviteToClan(Player player, Player invitee){
        if(!playerHasClan(player)){
            player.sendMessage(prefix + "You need to be in a clan to do this.");
            return;
        }
        if(playerHasClan(invitee)){
            player.sendMessage(prefix + "That player is already in a clan!");
            return;
        }
        if(invitedPlayers.containsKey(invitee)){
            player.sendMessage(prefix + "That player already has an invite pending.");
            return;
        }
        ClanObj clan = getPlayerClan(player);
        if(!playerIsLeader(player, clan)){
            player.sendMessage(prefix + "You must be the leader of a clan to invite someone!");
            return;
        }
        player.sendMessage(prefix + "An invite has been sent to " + ChatColor.GOLD + invitee.getName());
        invitee.sendMessage(prefix + "You have been invited to join " + player.getName() + "'s clan!");
        invitee.sendMessage(prefix + "Type " + ChatColor.GOLD + "/ot clan accept " + ChatColor.getLastColors(prefix) + "to join!");
        invitedPlayers.put(invitee, player);
        InviteTimer it = new InviteTimer(invitee, player);
        it.runTaskTimer(mainInstance, 0L, 20L);
        inviteTimers.put(invitee, it);
    }

    public void acceptClanInvite(Player invitee){
        if(!invitedPlayers.containsKey(invitee)){
            invitee.sendMessage(prefix + "You have no invites pending.");
            return;
        }
        Player inviter = invitedPlayers.get(invitee);
        invitedPlayers.get(invitee).sendMessage(prefix + invitee.getName() + " has accepted your invitation!");
        invitedPlayers.remove(invitee);
        inviteTimers.get(invitee).cancel();
        inviteTimers.remove(invitee);
        addPlayerToClan(invitee, getPlayerClan(inviter));
    }

    public void denyClanInvite(Player invitee){
        if(!invitedPlayers.containsKey(invitee)){
            invitee.sendMessage(prefix + "You have no invites pending.");
            return;
        }
        invitee.sendMessage(prefix + ChatColor.RED + "The request has been denied.");
        invitedPlayers.get(invitee).sendMessage(prefix + invitee.getName() + ChatColor.RED + " has denied your invitation.");
        invitedPlayers.remove(invitee);
        inviteTimers.get(invitee).cancel();
        inviteTimers.remove(invitee);
    }

    public void inviteTimerExpired(Player invitee, Player player){
        invitee.sendMessage(prefix + ChatColor.RED + "Your clan invite has expired!");
        player.sendMessage(prefix + ChatColor.RED + "The invite to " + invitee.getName() + " has expired...");
        invitedPlayers.remove(invitee);
        inviteTimers.remove(invitee);
    }

    private void addPlayerToClan(Player invitee, ClanObj clan){
        updateConfigPlayerList(invitee);
        UUID inviteeId = invitee.getUniqueId();

        // Construct player-info data in the clans configuration file
        clansConfig.set("player-info." + inviteeId + ".leader", false);
        clansConfig.set("player-info." + inviteeId + ".clan-id", clan.getId());

        clan.addMemberId(inviteeId);
        playerClans.put(inviteeId, clan);
        mainInstance.saveClansConfig();

        invitee.sendMessage(prefix + "Welcome to " + Bukkit.getPlayer(clan.getLeaderId()).getName() + "'s clan!");
    }

    /* Clan kick */
    public void kickPlayer(Player kicker, Player kicked){
        if(!playerHasClan(kicker)) {
            kicker.sendMessage(prefix + "You have to be in a clan to do this.");
            return;
        }
        ClanObj clan = getPlayerClan(kicker);
        if(!playerIsLeader(kicker, clan)){
            kicker.sendMessage(prefix + "Only the clan leader can do this action.");
            return;
        }
        clan.getMemberIds().remove(kicked.getUniqueId());
        clansConfig.set("player-info." + kicked, null);
        updateConfigPlayerList(kicked);
        kicked.sendMessage(prefix + "You have been kicked from " + Bukkit.getPlayer(clan.getLeaderId()).getName() + "'s clan!");
        kicker.sendMessage(prefix + "You have kicked " + kicked.getName() + " from your clan.");

        mainInstance.saveClansConfig();
    }

    /* Clan leave */
    public void leaveClan(Player player){
        if(!playerHasClan(player)) {
            player.sendMessage(prefix + "You have to be in a clan to do this.");
            return;
        }
        if(playerIsLeader(player, getPlayerClan(player))){
            player.sendMessage(prefix + "Please use the " + ChatColor.GOLD + "/ot clan disband " + ChatColor.getLastColors(prefix) + "command.");
            player.sendMessage(prefix + ChatColor.DARK_RED + ChatColor.BOLD + "NOTE: Disbanding a clan can not be undone. It will remove all players" +
                    " from the clan, and will NOT pass on leadership.");
            return;
        }

        ClanObj clan = getPlayerClan(player);
        clan.getMemberIds().remove(player.getUniqueId());
        clansConfig.set("player-info." + player, null);
        // Remove player from list
        List<String> playerIDs = clansConfig.getStringList("player-list");
        playerIDs.remove(player.getUniqueId().toString());
        clansConfig.set("player-list", playerIDs);
        mainInstance.saveClansConfig();

        player.sendMessage(prefix + "You have left " + Bukkit.getPlayer(clan.getLeaderId()).getName() + "'s clan.");

        mainInstance.saveClansConfig();
    }

    /* Utility methods */
    public void cacheClans(){
        List<String> playerIDs = clansConfig.getStringList("player-list");
        if(playerIDs == null) return;
        for(String str : playerIDs){
            // Construct the player data
            UUID addPlayerID = UUID.fromString(str);
            int clanNum = clansConfig.getInt("player-info." + addPlayerID + ".clan-id");

            // Set up the ClanObj
            if(!clanObjExists(clanNum)){
                cachedClans.add(createClanObj(clanNum));
            } // Otherwise it does exist so just move on
            ClanObj currentClan = getExistingClanObj(clanNum);
            boolean isLeader = clansConfig.getBoolean("player-info." + addPlayerID + ".leader");
            if(isLeader){
                currentClan.setLeaderId(addPlayerID);
            }
            currentClan.addMemberId(addPlayerID);

            // Add them in the hashmap
            playerClans.put(addPlayerID, currentClan);
        }
        System.out.println(prefix + "All clans have been successfully cached!");
        mainInstance.saveClansConfig();
    }

    private boolean clanObjExists(int id){
        boolean result = false;
        for(ClanObj clan : cachedClans){
            if(clan.getId() == id) result = true;
        }

        return result;
    }

    // TODO: Implementation needs to be tested
    private void updateConfigPlayerList(Player player){
        List<String> playerIDs = clansConfig.getStringList("player-list");
        playerIDs.add(player.getUniqueId().toString());
        clansConfig.set("player-list", playerIDs);
        mainInstance.saveClansConfig();
    }

    private ClanObj createClanObj(int id){
        return new ClanObj(id);
    }

    private ClanObj getExistingClanObj(int id){
        ClanObj returnClan = null;
        for(ClanObj clan : cachedClans){
            if(clan.getId() == id) returnClan = clan;
        }
        if(returnClan != null) return returnClan;
        System.out.println("Clan does not exist!");
        return null;
    }

    public boolean playerHasClan(Player player){
        return playerClans.containsKey(player.getUniqueId());
    }

    public ClanObj getPlayerClan(Player player){
        if(playerHasClan(player)) return playerClans.get(player.getUniqueId());
        System.out.println("Player does not have a clan!");
        return null;
    }

    public boolean playerIsLeader(Player player, ClanObj clan){
        return clan.getLeaderId().equals(player.getUniqueId());
    }

}
