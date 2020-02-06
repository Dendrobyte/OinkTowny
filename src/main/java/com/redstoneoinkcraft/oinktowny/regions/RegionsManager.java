package com.redstoneoinkcraft.oinktowny.regions;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.clans.ClanManager;
import com.redstoneoinkcraft.oinktowny.clans.ClanObj;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
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
public class RegionsManager {

    // TODO: Support for multiple worlds.
    // Right now, regions are cached based on overworld chunks. Make subregions in the config?
    // Would need to update [world name of claim, subcategories in configuration, chunks per world...]

    private static RegionsManager instance = new RegionsManager();
    private Main mainInstance = Main.getInstance();
    String prefix = mainInstance.getPrefix();

    private RegionsManager() {}

    public static RegionsManager getInstance(){
        return instance;
    }

    /* Chunk claiming related things */
    // Variables
    private HashMap<UUID, ArrayList<Chunk>> playerChunks = new HashMap<>();
    private HashMap<Chunk, UUID> claimedChunks = new HashMap<>();

    public HashMap<UUID, ArrayList<Chunk>> getPlayerChunks(){
        return playerChunks;
    }
    public HashMap<Chunk, UUID> getClaimedChunks(){
        return claimedChunks;
    }

    private ClanManager cm = ClanManager.getInstance();

    // Methods
    public void claimChunk(Player claimer){
        if(!claimer.getWorld().getName().equalsIgnoreCase(mainInstance.getWorldName())){
            claimer.sendMessage(prefix + "Claims are not enabled in this world!");
            return;
        }
        Chunk chunk = claimer.getLocation().getChunk();
        if(chunkIsClaimed(chunk)){
            claimer.sendMessage(prefix + ChatColor.RED + "This chunk is already claimed!");
            return;
        }
        UUID claimerID = claimer.getUniqueId();

        // Check if they can claim another chunk
        // TODO: This should become dynamic over time... so each player has a different base chunk claim. For now, it'll be teamwork!
        int limit = mainInstance.getRegionsConfig().getInt("base-limit");
        if(claimer.hasPermission("oinktowny.claims.sponsor")) limit += 4;
        if(claimer.hasPermission("oinktowny.claims.piglet")) limit += 4;
        if(claimer.hasPermission("oinktowny.claims.boar")) limit += 4;
        if(claimer.hasPermission("oinktowny.claims.spiderpig")) limit += 8;
        if(claimer.hasPermission("oinktowny.claims.flyingpig")) limit += 8;
        if(claimer.hasPermission("oinktowny.claims.octopig")) limit += 12;
        int currentClaims;
        try {
            currentClaims = mainInstance.getRegionsConfig().getStringList("chunks." + claimerID).size();
        } catch(NullPointerException e){
            // Player doesn't exist in configuration
            currentClaims = 0;
        }

        if(currentClaims >= limit){
            claimer.sendMessage(prefix + "You have already claimed your maximum amount of chunks!");
            return;
        }

        // Add the claim region and cache it
        addRegionClaim(claimerID, chunk);
        currentClaims += 1;
        claimer.sendMessage(prefix + "Chunk has been claimed! " + ChatColor.GOLD + ChatColor.BOLD + "Claimed chunks: " + currentClaims + "/" + limit);
        claimer.sendMessage(prefix + ChatColor.GRAY + "To unclaim a chunk, use " + ChatColor.GOLD + "/ot unclaim");
        mainInstance.saveRegionsConfig();
        mainInstance.reloadRegionsConfig();
    }

    public void unclaimChunk(Player claimer){
        if(!claimer.getWorld().getName().equalsIgnoreCase(mainInstance.getWorldName())){
            claimer.sendMessage(prefix + "Claims are not enabled in this world!");
            return;
        }
        removeRegionClaim(claimer, claimer.getUniqueId(), claimer.getLocation().getChunk());
    }

    public void unclaimAllChunks(Player claimer){
        ArrayList<Chunk> playerChunks = new ArrayList<>(getPlayerChunks().get(claimer.getUniqueId()));
        for(Chunk chunk : playerChunks){
            removeRegionClaim(claimer, claimer.getUniqueId(), chunk);
        }
        claimer.sendMessage(prefix + ChatColor.RED + ChatColor.BOLD + "All of your chunks have been cleared.");
    }

    // Util methods
    public void cacheRegions(){
        // Load chunks into their respective HashMaps
        try {
            mainInstance.getRegionsConfig().getConfigurationSection("chunks").getKeys(false);
        } catch(NullPointerException e){
            System.out.println(prefix + "No regions to cache.");
            mainInstance.saveRegionsConfig();
            return;
        }
        for (String key : mainInstance.getRegionsConfig().getConfigurationSection("chunks").getKeys(false)) {
            List<String> chunksPerUUID = mainInstance.getRegionsConfig().getStringList("chunks." + key);
            UUID playerID = UUID.fromString(key);
            for (String info : chunksPerUUID) {
                int dataX = Integer.parseInt(info.substring(0, info.indexOf(":")));
                int dataZ = Integer.parseInt(info.substring(info.indexOf(":") + 1));

                Chunk chunkToAdd = Bukkit.getServer().getWorld(mainInstance.getWorldName()).getChunkAt(dataX, dataZ);
                if (!playerChunks.keySet().contains(playerID)) playerChunks.put(playerID, new ArrayList<>());
                playerChunks.get(playerID).add(chunkToAdd);
                claimedChunks.put(chunkToAdd, playerID);
            }
        }
        System.out.println(prefix + "All regions have been successfully cached!");
        System.out.println("Player Chunks: " + playerChunks);
        System.out.println("Claimed Chunks: " + claimedChunks);
        mainInstance.saveRegionsConfig();
    }

    private Player bypassEnabled = null;
    public void enableBypassForAdmin(Player admin){
        if(bypassEnabled == null || !bypassEnabled.equals(admin)){
            bypassEnabled = admin;
            admin.sendMessage(prefix + ChatColor.RED + "You are now bypassing claims. Retype command to disable.");
        } else {
            bypassEnabled = null;
            admin.sendMessage(prefix + ChatColor.RED + "You are now no longer bypassing claims.");
        }
    }
    boolean bypassEnabled(Player player){
        if(bypassEnabled == null) return false;
        return bypassEnabled.equals(player);
    }

    public void addAdminRegionClaim(Player admin, Chunk chunk){
        if(chunkIsClaimed(chunk)){
            admin.sendMessage(prefix + ChatColor.RED + "This chunk is already claimed!");
            return;
        }
        admin.sendMessage(prefix + "Adding an admin claim...");
        addRegionClaim(UUID.fromString("00000000-0ad0-0011-1234-444888444888"), chunk);
        admin.sendMessage(prefix + ChatColor.GREEN + "Admin claim added under the following UUID: " + "> 00000000-0ad0-0011-1234-444888444888");
    }

    public void addRegionClaim(UUID playerID, Chunk chunk){
        List<String> listOfClaims;
        try {
            listOfClaims = mainInstance.getRegionsConfig().getStringList("chunks." + playerID.toString());
        } catch (NullPointerException e){
            listOfClaims = new ArrayList<>();
        }
        String chunkCoords = chunk.getX() + ":" + chunk.getZ();
        listOfClaims.add(chunkCoords);

        // Cache and add to file
        claimedChunks.put(chunk, playerID);
        if(!playerChunks.keySet().contains(playerID)) playerChunks.put(playerID, new ArrayList<Chunk>());
        playerChunks.get(playerID).add(chunk);
        mainInstance.getRegionsConfig().set("chunks." + playerID.toString(), listOfClaims);
        mainInstance.saveRegionsConfig();
    }

    public void removeRegionClaim(Player player, UUID playerID, Chunk chunk){
        if(!playerChunks.containsKey(playerID)){
            player.sendMessage(prefix + "You don't have any claims!");
            return;
        }

        if(!isTheirChunk(player, chunk)) {
            if(claimedChunks.containsKey(chunk)){
                player.sendMessage(prefix + "You do not own this chunk.");
                return;
            } else {
                player.sendMessage(prefix + "This chunk is not claimed");
                return;
            }
        }

        List<String> listOfClaims = mainInstance.getRegionsConfig().getStringList("chunks." + playerID.toString());
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        for(String data : listOfClaims){
            int dataX = Integer.parseInt(data.substring(0, data.indexOf(":")));
            int dataZ = Integer.parseInt(data.substring(data.indexOf(":")+1));
            if(chunkX == dataX && chunkZ == dataZ){
                listOfClaims.remove(data);
                Chunk toRemove = player.getWorld().getChunkAt(dataX, dataZ);
                claimedChunks.remove(chunk);
                playerChunks.get(playerID).remove(chunk);
                break;
            }
        }

        mainInstance.getRegionsConfig().set("chunks." + playerID.toString(), listOfClaims);
        mainInstance.saveRegionsConfig();
        player.sendMessage(prefix + "Your chunk has been successfully unclaimed!");
    }

    public void listClaims(Player player){
        UUID playerID = player.getUniqueId();
        if(!getPlayerChunks().containsKey(playerID)){
            player.sendMessage(prefix + "No claims found!");
            return;
        }
        ArrayList<Chunk> playerChunks = getPlayerChunks().get(playerID);
        player.sendMessage(prefix + ChatColor.GOLD + "Your claims are located at coordinates:");
        int i = 1;
        for(Chunk chunk : playerChunks){
            player.sendMessage("" + ChatColor.GOLD + i + " - " + ChatColor.GRAY + "X: " + chunk.getBlock(8, 64, 8).getX() + ", Z: " + chunk.getBlock(8, 64, 8).getZ());
            i++;
        }
        player.sendMessage(prefix + ChatColor.RED + ChatColor.ITALIC + "Pagination yet to be added.");
    }

    private boolean isTheirChunk(Player player, Chunk chunk){
        boolean isTheirChunk = false;
        List<String> playersChunks = mainInstance.getRegionsConfig().getStringList("chunks." + player.getUniqueId());
        for(String chunkInfo : playersChunks){
            int chunkInfoX = Integer.parseInt(chunkInfo.substring(0, chunkInfo.indexOf(":")));
            int chunkInfoZ = Integer.parseInt(chunkInfo.substring(chunkInfo.indexOf(":")+1));
            if(chunkInfoX == chunk.getX() && chunkInfoZ == chunk.getZ()){
                isTheirChunk = true;
                break;
            }
        }

        return isTheirChunk;
    }

    public boolean chunkIsClaimed(Chunk chunk){
        return claimedChunks.keySet().contains(chunk);
    }

    /* Superpick related things */
    private ArrayList<Player> superpickPlayers = new ArrayList<Player>();

    public void toggleSuperpick(Player player){
        if(isSuperpick(player)){
            superpickPlayers.remove(player);
            player.sendMessage(prefix + ChatColor.GOLD + "Super pickaxe has been disabled!");
        } else {
            superpickPlayers.add(player);
            player.sendMessage(prefix + ChatColor.GOLD + "Super pickaxe has been enabled!");
        }
    }

    public boolean isSuperpick(Player player){
        return superpickPlayers.contains(player);
    }

    /* Artifact Checks */
    public boolean containsClaimedBlock(ArrayList<Block> blocks, Player player){
        for(Block b : blocks){
            if(instance.getClaimedChunks().containsKey(b.getChunk())){
                UUID chunkOwner = instance.getClaimedChunks().get(b.getChunk());
                ClanObj clan = cm.getPlayerClanID(chunkOwner);
                if(!clan.getMemberIds().contains(player.getUniqueId())){
                    return true;
                }
            }
        }
        return false;
    }

}
