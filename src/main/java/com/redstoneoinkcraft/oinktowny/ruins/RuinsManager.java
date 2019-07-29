package com.redstoneoinkcraft.oinktowny.ruins;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.ruins.creation.RuinsCreationStates;
import com.redstoneoinkcraft.oinktowny.ruins.running.RuinsEndingTimer;
import com.redstoneoinkcraft.oinktowny.ruins.running.RuinsMobspawningTimer;
import com.redstoneoinkcraft.oinktowny.ruins.running.RuinsRunningTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsManager {

    private static RuinsManager instance = new RuinsManager();
    private String ruinsPrefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "TownyRuins" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    public static RuinsManager getInstance(){
        return instance;
    }
    public String getRuinsPrefix() {
        return ruinsPrefix;
    }

    private RuinsManager(){}

    private ArrayList<RuinsObj> cachedRuins = new ArrayList<>(2);

    // Main initializing method called in the main file (just a fun name for it)
    public void rebuildRuins(){
        FileConfiguration ruinsConfig = Main.getInstance().getRuinsConfig();
        System.out.println(ruinsPrefix + "Beginning ruins reconstruction process...");
        try {
            ruinsConfig.getConfigurationSection("ruins");
        } catch (NullPointerException e){
            System.out.println(ruinsPrefix + "No ruins in file. Skipping rebuilding process.");
            return;
        }
        for(String ruinsName : ruinsConfig.getConfigurationSection("ruins").getKeys(false)){
            RuinsObj load = new RuinsObj();
            load.setName(ruinsName);
            // Lobby
            Location lobbyLoc = new Location(Bukkit.getWorld(ruinsConfig.getString("ruins." + ruinsName + ".lobby.world")), ruinsConfig.getInt("ruins." + ruinsName + ".lobby.x"), ruinsConfig.getInt("ruins." + ruinsName + ".lobby.y"), ruinsConfig.getInt("ruins." + ruinsName + ".lobby.z"));
            load.setLobby(lobbyLoc);
            // Signs
            for(String signNum : ruinsConfig.getConfigurationSection("ruins." + ruinsName + ".signs").getKeys(false)){
                Location signLoc = new Location(Bukkit.getWorld(ruinsConfig.getString("ruins." + ruinsName + ".signs." + signNum + ".world")), ruinsConfig.getInt("ruins." + ruinsName + ".signs." + signNum + ".x"), ruinsConfig.getInt("ruins." + ruinsName + ".signs." + signNum + ".y"), ruinsConfig.getInt("ruins." + ruinsName + ".signs." + signNum + ".z"));
                if(signLoc.getBlock().getType().toString().contains("WALL_SIGN")) {
                    load.addJoinSign((Sign) signLoc.getBlock().getState());
                }
            }
            // Levels
            for(String levelNum : ruinsConfig.getConfigurationSection("ruins." + ruinsName + ".levels").getKeys(false)){
                RuinsObjLevel level = new RuinsObjLevel(new Location(Bukkit.getWorld(ruinsConfig.getString("ruins." + ruinsName + ".levels." + levelNum + ".spawnpoint.world")), ruinsConfig.getInt("ruins." + ruinsName + ".levels." + levelNum + ".spawnpoint.x"), ruinsConfig.getInt("ruins." + ruinsName + ".levels." + levelNum + ".spawnpoint.y"), ruinsConfig.getInt("ruins." + ruinsName + ".levels." + levelNum + ".spawnpoint.z")));
                // Monsters
                List<String> monsterList = ruinsConfig.getStringList("ruins." + ruinsName + ".levels." + levelNum + ".monsters");
                for(String str : monsterList){
                    level.addMonster(str);
                }
                load.addLevel(level);
                // Monster spawn locations
                ArrayList<Location> monsterSpawnLocs = new ArrayList<>(2);
                World world = level.getSpawnLocation().getWorld();
                for(String coords : ruinsConfig.getConfigurationSection("ruins." + ruinsName + ".levels." + levelNum + ".monster_spawnlocs").getKeys(false)){
                    String manip = coords;
                    int x = Integer.parseInt(manip.substring(0, manip.indexOf(",")));
                    manip = manip.replace(x + ",", "");
                    int y = Integer.parseInt(manip.substring(0, manip.indexOf(",")));
                    manip = manip.replace(y + ",", "");
                    int z = Integer.parseInt(manip);
                    Location loc = new Location(world, x, y, z);
                    monsterSpawnLocs.add(loc);
                }
                level.setMonsterSpawnLocations(monsterSpawnLocs);
            }
            // Reward items
            ArrayList<ItemStack> itemList = new ArrayList<>();
            for(String itemNum : ruinsConfig.getConfigurationSection("ruins." + ruinsName + ".rewards").getKeys(false)){
                ItemStack item = ruinsConfig.getItemStack("ruins." + ruinsName + ".rewards." + itemNum);
                itemList.add(item);
            }
            load.setRewardItems(itemList);
            cachedRuins.add(load);
            System.out.println(ruinsPrefix + "'Rebuilt' the " + ruinsName + " ruins.");
        }
        System.out.println(ruinsPrefix + "All ruins successfully rebuilt!");
    }

    /* Ruins creation objects */
    HashMap<Player, RuinsCreationStates> playerCreationStates = new HashMap<>();
    HashMap<Player, RuinsObj> ruinsBeingCreated = new HashMap<>(2);

    /* Ruins creation methods */
    public void initiateRuinsCreation(Player player, String name){
        if(ruinsExist(name)){
            player.sendMessage(ruinsPrefix + "Ruins by that name already exist. Please try another name.");
            return;
        }
        player.sendMessage(ruinsPrefix + "Welcome to the Towny Ruins creation wizard!");
        playerCreationStates.put(player, RuinsCreationStates.LOBBY);
        RuinsObj ruins = new RuinsObj();
        ruins.setName(name);
        ruinsBeingCreated.put(player, ruins);
        player.sendMessage(ruinsPrefix + "Type " + ChatColor.GREEN + "BEGIN" + ChatColor.getLastColors(ruinsPrefix) + " at the lobby location.");
    }

    public boolean ruinsExist(String name){
        for(RuinsObj ruins : cachedRuins){
            if(ruins.getName().equalsIgnoreCase(name)){
                return false;
            }
        }
        return true;
    }

    public boolean isPlayerCreatingRuins(Player player){
        return playerCreationStates.containsKey(player);
    }

    public RuinsCreationStates getPlayerCreationState(Player player){
        if(!isPlayerCreatingRuins(player)) return null;
        return playerCreationStates.get(player);
    }

    public void removePlayerFromCreation(Player player){
        playerCreationStates.remove(player);
    }

    public void setPlayerCreationState(Player player, RuinsCreationStates state){
        playerCreationStates.put(player, state);
    }

    public void endRuinsCreation(Player player, RuinsObj ruins){
        FileConfiguration ruinsConfig = Main.getInstance().getRuinsConfig();
        String name = ruins.getName();
        // Lobby
        ruinsConfig.set("ruins." + name + ".lobby.world", ruins.getLobby().getWorld().getName());
        ruinsConfig.set("ruins." + name + ".lobby.x", ruins.getLobby().getBlockX());
        ruinsConfig.set("ruins." + name + ".lobby.y", ruins.getLobby().getBlockY());
        ruinsConfig.set("ruins." + name + ".lobby.z", ruins.getLobby().getBlockZ());
        // Join signs
        int counter = 0;
        for(Sign sign : ruins.getJoinSigns()){
            ruinsConfig.set("ruins." + name + ".signs." + counter + ".world", sign.getLocation().getWorld().getName());
            ruinsConfig.set("ruins." + name + ".signs." + counter + ".x", sign.getLocation().getBlockX());
            ruinsConfig.set("ruins." + name + ".signs." + counter + ".y", sign.getLocation().getBlockY());
            ruinsConfig.set("ruins." + name + ".signs." + counter + ".z", sign.getLocation().getBlockZ());
        }
        // Levels
        counter = 0;
        for(RuinsObjLevel level : ruins.getLevels()){
            // Spawnpoint
            ruinsConfig.set("ruins." + name + ".levels." + counter  + ".spawnpoint.world", level.getSpawnLocation().getWorld().getName());
            ruinsConfig.set("ruins." + name + ".levels." + counter  + ".spawnpoint.x", level.getSpawnLocation().getBlockX());
            ruinsConfig.set("ruins." + name + ".levels." + counter  + ".spawnpoint.y", level.getSpawnLocation().getBlockY());
            ruinsConfig.set("ruins." + name + ".levels." + counter  + ".spawnpoint.z", level.getSpawnLocation().getBlockZ());
            // Monsters
            ruinsConfig.set("ruins." + name + ".levels." + counter  + ".monsters", level.getMonsters());
            // Monster spawnpoints
            int mslCounter = 0;
            for(Location spawnLoc : level.getMonsterSpawnLocations()) {
                ruinsConfig.set("ruins." + name + ".levels." + counter + ".monster_spawnlocs." + mslCounter,
                        spawnLoc.getBlockX() + "," + spawnLoc.getBlockY() + "," + spawnLoc.getBlockZ());
                mslCounter++;
            }

            counter ++;
        }
        // Rewards
        counter  = 0;
        for(ItemStack item : ruins.getRewardItems()){
            ruinsConfig.set("ruins." + name + ".rewards." + counter, item);
            counter ++;
        }

        Main.getInstance().saveRuinsConfig();
        playerCreationStates.remove(player);

        cachedRuins.add(ruins);
        player.sendMessage(ruinsPrefix + "Ruin creation successfully added to file and cached in memory.");
    }

    public RuinsObj getCreatedRuins(Player player){
        return ruinsBeingCreated.get(player);
    }

    public void setLobbyLocation(RuinsObj ruins, Location lobbyLocation){
        ruins.setLobby(lobbyLocation);
    }

    public void destroyRuins(String ruinsName, Player player){
        if(getRuins(ruinsName) == null){
            player.sendMessage(ruinsPrefix + "The " + ruinsName + " ruins could not be found.");
            return;
        }
        // Remove from file
        RuinsObj ruinsToRemove = getRuins(ruinsName);
        Main.getInstance().getRuinsConfig().set("ruins." + ruinsName, null);
        Main.getInstance().saveRuinsConfig();
        // Remove from cache
        cachedRuins.remove(ruinsToRemove);

        player.sendMessage(ruinsPrefix + "The " + ruinsName + " ruins have crumbled! [Removed from configuration and cache]");
    }

    /* Ruins running methods */
    HashMap<Player, RuinsObj> activePlayers = new HashMap<>();
    HashMap<Player, RuinsMobspawningTimer> playerMobSpawningTimers = new HashMap<>();

    public HashMap<Player, RuinsObj> getActivePlayers() {
        return activePlayers;
    }

    public HashMap<Player, RuinsMobspawningTimer> getPlayerMobSpawningTimers() {
        return playerMobSpawningTimers;
    }

    public RuinsObj getRuins(String name){
        for(RuinsObj ruins : cachedRuins){
            if(ruins.getName().equalsIgnoreCase(name)){
                return ruins;
            }
        }
        return null;
    }

    public void initiatePlayerInRuins(Player player, String name){
        if(getRuins(name) == null){
            player.sendMessage(ruinsPrefix + "Something went wrong with joining " + name + " ruins... Please contact staff.");
            return;
        }
        RuinsObj ruins = getRuins(name);
        ruins.getPlayerCurrentLevel().put(player, ruins.getFirstLevel());
        getActivePlayers().put(player, ruins);
        String message = "Get ready for the wave of mobs!";
        int seconds = 20;
        player.sendMessage(ruinsPrefix + "Your ruins excursion will begin in " + seconds + " seconds!");
        RuinsRunningTimer rrt = new RuinsRunningTimer(message, seconds, player, ruins.getFirstLevel().getSpawnLocation());
        rrt.runTaskTimer(Main.getInstance(), 0, 20L);
        activeTimers.put(player, rrt);
    }

    private HashMap<Player, RuinsRunningTimer> activeTimers = new HashMap<>(2);
    private HashMap<Player, ArrayList<Entity>> spawnedEntities = new HashMap<>();
    private HashMap<Entity, Player> spawnedEntitiesToPlayer = new HashMap<>(); // oof

    public HashMap<Player, ArrayList<Entity>> getSpawnedEntities() {
        return spawnedEntities;
    }

    public HashMap<Entity, Player> getSpawnedEntitiesToPlayer(){
        return spawnedEntitiesToPlayer;
    }

    public void advancePlayer(Player player){
        RuinsObj ruins = getActivePlayers().get(player);
        RuinsObjLevel currentLevel = ruins.getPlayerCurrentLevel().get(player);
        if(currentLevel.equals(ruins.getLatestLevel())){
            player.sendMessage(ruinsPrefix + "Congratulations on making it through the " + ruins.getName() + " ruins!");
            player.sendMessage(ruinsPrefix + "Your rewards have been dropped at this rooms spawnpoint. You'll be returned to the lobby in 30 seconds.");
            RuinsEndingTimer ret = new RuinsEndingTimer(player, ruins.getLobby(), ruins.getLatestLevel().getSpawnLocation());
            ret.runTaskTimer(Main.getInstance(), 0, 20L);

            for(ItemStack item : ruins.getRewardItems()){
                ruins.getLatestLevel().getSpawnLocation().getWorld().dropItem(ruins.getLatestLevel().getSpawnLocation(), item);
            }
        } else {
            RuinsObjLevel nextLevel = ruins.nextLevel(currentLevel);
            ruins.getPlayerCurrentLevel().put(player, nextLevel);
            player.sendMessage(ruinsPrefix + "Heading into next wave in 10 seconds...");
            String message = "Next wave starts now!";
            int seconds = 10;
            RuinsRunningTimer rrt = new RuinsRunningTimer(message, seconds, player, nextLevel.getSpawnLocation());
            rrt.runTaskTimer(Main.getInstance(), 0, 20L);
        }

    }

    public void kickPlayerFromRuins(Player player){
        RuinsObj ruins = getActivePlayers().get(player);
        ruins.getPlayerCurrentLevel().remove(player);
        getActivePlayers().remove(player);
        getSpawnedEntities().remove(player);
        activeTimers.get(player).cancel();
        activeTimers.remove(player);
        player.teleport(ruins.getLobby());
    }

}
