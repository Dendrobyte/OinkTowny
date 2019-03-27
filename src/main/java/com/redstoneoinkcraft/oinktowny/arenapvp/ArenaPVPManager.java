package com.redstoneoinkcraft.oinktowny.arenapvp;

import com.redstoneoinkcraft.oinktowny.Main;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaPVPManager {

    private static ArenaPVPManager instance = new ArenaPVPManager();
    private String prefix = Main.getInstance().getPrefix();
    private String arenaPrefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "TownyArena" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    private ArenaPVPManager(){}

    public static ArenaPVPManager getInstance(){
        return instance;
    }

    public String getArenaPrefix(){
        return arenaPrefix;
    }

    /* Arena creation */
    private HashMap<Player, ArenaCreationStage> playerCurrentStage = new HashMap<>();
    private HashMap<Player, ArenaObj> currentArenaInCreation = new HashMap<>();
    private ArrayList<ArenaObj> loadedArenas = new ArrayList<>();

    public boolean isCreating(Player player){
        return(playerCurrentStage.containsKey(player));
    }

    ItemStack creationWand = new ItemStack(Material.STICK, 1);
    {
        ItemMeta creationWandMeta = creationWand.getItemMeta();
        creationWandMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Towny Arena Creation Wand");
        creationWandMeta.addEnchant(Enchantment.SWEEPING_EDGE, 10, true);
        creationWand.setItemMeta(creationWandMeta);
    }
    public ItemStack getCreationWand(){
        return creationWand;
    }

    private boolean isInCreation(Player player){
        return playerCurrentStage.containsKey(player);
    }

    public ArrayList<String> getExistingArenas(){
        try {
            return new ArrayList<>(Main.getInstance().getArenasConfig().getConfigurationSection("arenas").getKeys(false));
        } catch (NullPointerException e){ // I actually know why it would be null so I'm accepting this try/catch block >:( Fight me.
            ArrayList<String> npe = new ArrayList<>();
            npe.add("No existing arenas found");
            return npe;
        }
    }

    public ArrayList<String> getLoadedArenas(){
        ArrayList<String> arenas = new ArrayList<>(); // Bad naming
        for(ArenaObj arena : loadedArenas){
            arenas.add(arena.getName());
        }
        return arenas;
    }

    public boolean arenaExists(String name){
        return getExistingArenas().contains(name);
    }

    public void initiateArenaCreation(Player player, String name){
        if(isInCreation(player)){
            player.sendMessage(prefix + ChatColor.RED + ChatColor.BOLD + "Restarting arena creation...");
        }
        if(arenaExists(name)){
            player.sendMessage(prefix + ChatColor.RED + "That arena already exists!");
            return;
        }
        playerCurrentStage.put(player, ArenaCreationStage.ARENA_LOC);
        player.getInventory().setItem(0, getCreationWand());
        setPlayerStage(player, ArenaCreationStage.ARENA_LOC);
        currentArenaInCreation.put(player, new ArenaObj(name));
        player.sendMessage(prefix + "Arena creation has been initiated for arena " + ChatColor.BOLD + name + "!");
        player.sendMessage(prefix + ChatColor.RED + "To leave creation, use /ot arena leave");
        player.sendMessage(prefix + ChatColor.GREEN + "Please use the creation wand (the stick) to select the " + ChatColor.BOLD + " center (or close to it) of the arena.");
    }

    public ArenaCreationStage getPlayerStage(Player player){
        if(!isInCreation(player)){
            return null;
        }
        return playerCurrentStage.get(player);
    }

    public void setPlayerStage(Player player, ArenaCreationStage stage){
        playerCurrentStage.put(player, stage);
    }

    public ArenaObj getPlayerCreationArena(Player player){
        return currentArenaInCreation.get(player);
    }

    public void finishCreation(Player player) {
        playerCurrentStage.remove(player);
        player.sendMessage(prefix + "Creating the arena " + ChatColor.BOLD + getPlayerCreationArena(player).getName() + "...");
        writeArenaToConfig(currentArenaInCreation.get(player), Main.getInstance().getArenasConfig());
        player.sendMessage(prefix + ChatColor.GREEN + "Creation successful for " + ChatColor.BOLD + currentArenaInCreation.get(player).getName() + "!");
        currentArenaInCreation.remove(player);
        player.getInventory().getItemInMainHand().setAmount(0);
    }

    public void quitCreation(Player player){
        playerCurrentStage.remove(player);
        currentArenaInCreation.remove(player);
        player.sendMessage(prefix + "You have left arena creation! Progress was lost.");
    }

    private void writeArenaToConfig(ArenaObj arena, FileConfiguration arenasConfig){
        String name = arena.getName();
        // Save central loc
        arenasConfig.set("arenas." + name + ".central_loc.x", arena.getArenaLoc().getBlockX());
        arenasConfig.set("arenas." + name + ".central_loc.y", arena.getArenaLoc().getBlockY());
        arenasConfig.set("arenas." + name + ".central_loc.z", arena.getArenaLoc().getBlockZ());
        // Save lobby
        arenasConfig.set("arenas." + name + ".lobby.x", arena.getLobby().getBlockX());
        arenasConfig.set("arenas." + name + ".lobby.y", arena.getLobby().getBlockY());
        arenasConfig.set("arenas." + name + ".lobby.z", arena.getLobby().getBlockZ());
        // Save spawn one
        arenasConfig.set("arenas." + name + ".spawn_one.x", arena.getSpawn_one().getBlockX());
        arenasConfig.set("arenas." + name + ".spawn_one.y", arena.getSpawn_one().getBlockY());
        arenasConfig.set("arenas." + name + ".spawn_one.z", arena.getSpawn_one().getBlockZ());
        // Save spawn two
        arenasConfig.set("arenas." + name + ".spawn_two.x", arena.getSpawn_two().getBlockX());
        arenasConfig.set("arenas." + name + ".spawn_two.y", arena.getSpawn_two().getBlockY());
        arenasConfig.set("arenas." + name + ".spawn_two.z", arena.getSpawn_two().getBlockZ());

        Main.getInstance().saveArenasConfig();
    }

    public void teleportPlayer(Player player, String arenaName){
        if(!arenaExists(arenaName)){
            player.sendMessage(prefix + "That arena doesn't exist!");
            return;
        }
        Location loc = getArenaObj(arenaName).getArenaLoc();
        player.teleport(loc);
        player.sendMessage(prefix + "Teleporting to " + arenaName + "...");
    }

    /* Arena join */
    private HashMap<Player, ArenaObj> playersInArenas = new HashMap<>();

    public boolean isPlayerInArena(Player player){
        return playersInArenas.containsKey(player);
    }

    public ArenaObj getPlayerArena(Player player){
        if(!isPlayerInArena(player)) return null;
        return playersInArenas.get(player);
    }

    public void loadArenas(){
        FileConfiguration arenasConfig = Main.getInstance().getArenasConfig();
        for(String arenaName : getExistingArenas()){
            ArenaObj workingArena = new ArenaObj(arenaName);
            World world = Bukkit.getServer().getWorld(Main.getInstance().getWorldName());

            Location arenaLoc = new Location(world, arenasConfig.getInt("arenas." + arenaName + ".central_loc.x"), arenasConfig.getInt("arenas." + arenaName + ".central_loc.y"), arenasConfig.getInt("arenas." + arenaName + ".central_loc.z"));
            Location lobby = new Location(world, arenasConfig.getInt("arenas." + arenaName + ".lobby.x"), arenasConfig.getInt("arenas." + arenaName + ".lobby.y"), arenasConfig.getInt("arenas." + arenaName + ".lobby.z"));
            Location spawn_one = new Location(world, arenasConfig.getInt("arenas." + arenaName + ".spawn_one.x"), arenasConfig.getInt("arenas." + arenaName + ".spawn_one.y"), arenasConfig.getInt("arenas." + arenaName + ".spawn_one.z"));
            Location spawn_two = new Location(world, arenasConfig.getInt("arenas." + arenaName + ".spawn_two.x"), arenasConfig.getInt("arenas." + arenaName + ".spawn_two.y"), arenasConfig.getInt("arenas." + arenaName + ".spawn_two.z"));

            workingArena.setAllValues(arenaLoc, lobby, spawn_one, spawn_two, ArenaStatus.WAITING);

            loadedArenas.add(workingArena);
        }
        Bukkit.getLogger().log(Level.INFO, prefix + "Successfully loaded all PvP arenas! " + getLoadedArenas().toString());
    }

    public ArenaObj getArenaObj(String name){
        for(ArenaObj arena : loadedArenas){
            if(arena.getName().equalsIgnoreCase(name)){
                return arena;
            }
        }
        return null; // This should never happen since the check occurs with sign creation... unless an arena is removed!
    }

    public void addPlayerToArena(ArenaObj workingArena, Player player, Sign wallSign){
        if(isPlayerInArena(player)){
            player.sendMessage(arenaPrefix + "I don't know what sorcery you're using, but you're already in an arena!");
            return;
        }
        if(workingArena.getPlayerOne() == null){
            workingArena.setPlayerOne(player);
            workingArena.setArenaSign(wallSign);
            player.teleport(workingArena.getArenaLoc());
            player.sendMessage(arenaPrefix + "You have been added to " + workingArena.getName() + ".");
            player.sendMessage(arenaPrefix + ChatColor.GOLD + "Currently waiting for a second player...");
        } else {
            workingArena.setPlayerTwo(player);
            player.sendMessage(arenaPrefix + "You have been added to " + ChatColor.BOLD + workingArena.getName() + ".");
            workingArena.getPlayerOne().sendMessage(arenaPrefix + ChatColor.GOLD + "You will be facing " + ChatColor.BOLD + player.getName() + "...");
            player.sendMessage(arenaPrefix + ChatColor.GOLD + "You will be facing " + ChatColor.BOLD + workingArena.getPlayerOne().getName() + "...");
            player.teleport(workingArena.getArenaLoc()); // Needs to be before startArena so that the teleport listener doesn't kill everything.
            startArena(workingArena);
        }
        playersInArenas.put(player, workingArena);
    }

    /* Arena play */
    public void startArena(ArenaObj workingArena){
        workingArena.setStatus(ArenaStatus.IN_USE);
        workingArena.getArenaSign().setLine(2, "" + ChatColor.GREEN + ChatColor.BOLD + "IN USE");
        workingArena.getArenaSign().update();
        workingArena.sendPlayersMessage(arenaPrefix + ChatColor.GOLD + ChatColor.BOLD + "Both players have joined!" +
                ChatColor.GRAY + ChatColor.ITALIC + " Initiating timer...");
        ArenaTimer timer = new ArenaTimer(workingArena);
        timer.runTaskTimer(Main.getInstance(), 0, 20L);
    }

    public void endArena(ArenaObj workingArena, Player winner){
        World towny = Bukkit.getServer().getWorld(Main.getInstance().getWorldName());
        Player playerOne = workingArena.getPlayerOne();
        Player playerTwo = workingArena.getPlayerTwo();
        for(Player players : towny.getPlayers()){
            players.sendMessage(prefix + ChatColor.GOLD + ChatColor.BOLD + winner.getName() + ChatColor.getLastColors(prefix) + " with "
                    + workingArena.getPlayerOne().getHealth() + " health remaining won a Towny Arena battle! " + ChatColor.GRAY + ChatColor.ITALIC + "Huzzah!");
        }
        playersInArenas.remove(playerOne);
        playersInArenas.remove(playerTwo);
        workingArena.resetArena();
        playerOne.teleport(workingArena.getLobby());
        playerTwo.teleport(workingArena.getLobby());
    }

    public void prematurelyEndArena(Player player, String msg){
        if(!isPlayerInArena(player)) return;
        ArenaObj workingArena = getPlayerArena(player);
        if(workingArena.getStatus() == ArenaStatus.IN_USE){
            Player winner;
            if(player == workingArena.getPlayerOne()) winner = workingArena.getPlayerTwo();
            else winner = workingArena.getPlayerOne();
            endArena(workingArena, winner);
            player.sendMessage(getArenaPrefix() + msg);
        } else {
            workingArena.resetArena();
        }
    }

}


