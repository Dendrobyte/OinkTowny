package com.redstoneoinkcraft.oinktowny.arenapvp;

import com.redstoneoinkcraft.oinktowny.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArenaPVPManager {

    private static ArenaPVPManager instance = new ArenaPVPManager();
    private String prefix = Main.getInstance().getPrefix();

    private ArenaPVPManager(){}

    public static ArenaPVPManager getInstance(){
        return instance;
    }

    /* Arena creation */
    ItemStack creationWand = new ItemStack(Material.STICK, 1);
    {
        ItemMeta creationWandMeta = creationWand.getItemMeta();
        creationWandMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Towny Arena Creation Wand");
        creationWandMeta.addEnchant(Enchantment.SWEEPING_EDGE, 10, false);
        creationWand.setItemMeta(creationWandMeta);
    }
    public ItemStack getCreationWand(){
        return creationWand;
    }

    private HashMap<Player, ArenaCreationStage> playerCurrentStage = new HashMap<>();
    private HashMap<Player, ArenaTemplateObj> currentArenaInCreation = new HashMap<>();

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

    private boolean arenaExists(String name){
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
        currentArenaInCreation.put(player, new ArenaTemplateObj(name));
        player.sendMessage(prefix + "Arena creation has been initiated for arena " + ChatColor.BOLD + name + "!");
        player.sendMessage(prefix + ChatColor.RED + "To leave creation, use /ot arena leave");
        player.sendMessage(prefix + ChatColor.GREEN + "Please use the creation wand to select the " + ChatColor.BOLD + "main arena location.");
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

    public ArenaTemplateObj getPlayerArenaTemplate(Player player){
        return currentArenaInCreation.get(player);
    }

    // TODO: Get rid of the exception throw- shouldn't throw that so I'm just making sure I don't hit that point... I could always keep it.
    public void finishCreation(Player player) {
        playerCurrentStage.remove(player);
        player.sendMessage(prefix + "Creating the arena " + ChatColor.BOLD + getPlayerArenaTemplate(player).getName() + "...");
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

    private void writeArenaToConfig(ArenaTemplateObj arena, FileConfiguration arenasConfig){
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

    /* Arena join */

    /* Arena play */

}
