package com.redstoneoinkcraft.oinktowny.lootdrops;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.economy.TownyTokenManager;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class LootdropManager {

    private static LootdropManager instance = new LootdropManager();
    private Main mainInstance = Main.getInstance();
    private String prefix = Main.getInstance().getPrefix();

    // Get the bounds from the configuration file, make sure they're positive
    private int x_1 = Math.abs(mainInstance.getLootdropsConfig().getInt("x-bound"));
    private int z_1 = Math.abs(mainInstance.getLootdropsConfig().getInt("z-bound"));
    private World townyWorld = Bukkit.getServer().getWorld(mainInstance.getWorldName());
    // In case someone puts the values in as negative... [they should really be positive though]
    private int x_2 = x_1 * -1;
    private int z_2 = z_1 * -1;

    private LootdropManager(){}

    public static LootdropManager getInstance(){
        return instance;
    }

    /* Expiration and already-opened checks */
    private ArrayList<Chest> existingLootcrates = new ArrayList<>();
    private HashMap<Chest, LootdropExpirationTimer> chestExpirationTimers = new HashMap<>();
    public ArrayList<Chest> getExistingLootcrates(){
        return existingLootcrates;
    }

    private void replaceChestExpirationTimer(Chest chest, LootdropExpirationTimer let){
        chestExpirationTimers.get(chest).cancel();
        chestExpirationTimers.put(chest, let);
    }

    public boolean isLootcrate(Chest chest){
        return existingLootcrates.contains(chest);
    }

    public void startLootcrateExpiration(Chest chest, int seconds, Player finder){
        finder.sendMessage(prefix + ChatColor.GREEN + ChatColor.BOLD + "Nice Find!" + ChatColor.RED + " This crate will expire in " + seconds + " seconds!");
        LootdropExpirationTimer let = new LootdropExpirationTimer(chest, seconds);
        let.runTaskTimer(mainInstance, 0L, 20L);
        replaceChestExpirationTimer(chest, let);
    }

    private ArrayList<Chest> foundCrates = new ArrayList<>();
    public boolean lootcrateIsFound(Chest chest){
        return foundCrates.contains(chest);
    }

    public void setLootcrateFound(Chest chest, boolean found){
        if(found) foundCrates.add(chest);
        else foundCrates.remove(chest);
    }

    // Generate random location, then pass into drop loot location
    private Location generateRandomDropLocation(){
        Random random = new Random();
        int resultX = random.nextInt(2*x_1) - x_1;
        int resultZ = random.nextInt(2*z_1) - z_1;
        int resultY = generateRandomY(resultX, resultZ);
        return new Location(townyWorld, resultX, resultY, resultZ);
    }

    // Generate a working y value to place a chest
    private int generateRandomY(int x, int z){
        int height = townyWorld.getMaxHeight()-1;
        while(height != 0){
            // Check if the block below is not air starting at the top of the world, that should do it.
            Location loc = new Location(townyWorld, x, height-1, z);
            Material blockType = loc.getBlock().getType();
            if(blockType.equals(Material.AIR) || blockType.equals(Material.WATER) || blockType.equals(Material.GRASS) || blockType.equals(Material.SNOW)){
                height -= 1;
            } else {
                // We have a block below where we want to put the chest!
                return height;
            }
        }
        return 67; // Return sea level because why not- I don't want to deal with this
    }

    // Generate values based on player location, then pass into drop loot location

    // Drop loot
    public void dropLootChest(Location location){
        location.getBlock().setType(Material.CHEST);
        location.getBlock().getState().update();
        Chest chest = (Chest) location.getBlock().getState();
        existingLootcrates.add(chest);
        Inventory chestInv = chest.getBlockInventory();
        fillLootContents(chestInv);
        // Start an expiration timer
        LootdropExpirationTimer let = new LootdropExpirationTimer(chest, 7200);
        let.runTaskTimer(mainInstance, 0L, 20L);
        chestExpirationTimers.put(chest, let);
    }

    // Drop loot randomly
    public Location dropLootChestRandom(){
        Location dropLoc = generateRandomDropLocation();
        dropLootChest(dropLoc);
        broadcastDrop(dropLoc);
        return dropLoc;
    }

    // Drop loot predictably (mainly serves debug purposes)
    public void dropLootChestPredictably(Player player){

        if (!player.getLocation().getWorld().getName().equalsIgnoreCase(townyWorld.getName())) {
            player.sendMessage(prefix + "You are not in a world with towny drops enabled!");
            return;
        }
        dropLootChest(player.getLocation());
        player.sendMessage(prefix + "Loot dropping at your location...");
    }

    // Fill contents of a passed in inventory with random loot
    private void fillLootContents(Inventory inv){
        ArrayList<ItemStack> finalItems = new ArrayList<>();
        // Get 3 tier one items
        addItemsToList(finalItems, mainInstance.getLootdropsConfig().getStringList("tier1"), 3);

        // Get 3 tier two items
        addItemsToList(finalItems, mainInstance.getLootdropsConfig().getStringList("tier2"), 3);

        // Get 1 tier three items
        addItemsToList(finalItems, mainInstance.getLootdropsConfig().getStringList("tier3"), 2);

        finalItems.add(TownyTokenManager.getInstance().createToken(1)); // Throw in a token for good fun

        // TODO: Throw in one or two artifacts/season items when that creation comes about (maybe random gen 1 or 2 towny tokens?)

        // Finally fill the given inventory in random slots
        Random rand = new Random();
        for(ItemStack item : finalItems){
            int slot = rand.nextInt(inv.getSize());
            while(inv.getContents()[slot] != null){ // Make sure you don't overwrite another item
                slot = rand.nextInt(inv.getSize());
            }
            inv.setItem(slot, item);
        }
    }

    private void addItemsToList(ArrayList<ItemStack> returnList, List<String> listOfItems, int max){
        int counter = 0;
        Random rand = new Random();
        while(counter < max){
            int label = rand.nextInt(listOfItems.size());
            String itemInList = listOfItems.get(label);
            String material = itemInList.substring(0, itemInList.indexOf(","));
            int amt = Integer.parseInt(itemInList.substring(itemInList.indexOf(",")+1));
            returnList.add(new ItemStack(Material.getMaterial(material), amt));
            counter++;
        }
    }

    private void broadcastDrop(Location location){
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Random rand = new Random();
        String message = prefix + "A" + ChatColor.GOLD + ChatColor.BOLD + " loot chest has appeared" + ChatColor.getLastColors(prefix) + " in the towny world ";
        String coords = "around " + ChatColor.GOLD + ChatColor.BOLD + "x" + (x+rand.nextInt(16)) + "/x" + (x-rand.nextInt(16)) +
                ChatColor.getLastColors(prefix) + " and " + ChatColor.GOLD + ChatColor.BOLD + "z" + (z+rand.nextInt(16)) + "/z" + (z-rand.nextInt(16)) +
                ChatColor.getLastColors(prefix) + " on " + ChatColor.GOLD + ChatColor.BOLD + "level " + y;

        // Broadcast to players
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            // if(player.getWorld().equals(townyWorld)) player.sendMessage(message + "\n" + coords); Should it be restricted?
            player.sendMessage(message + "\n" + coords);
        }

        // Broadcast to Discord server
        // TODO: Bukkit.getServer().dispatchCommand("todiscord voice_and_bot_chat " + message + "\n" + coords)
    }

    public void initializeLootdropTimer(){
        LootdropTimer ldt = new LootdropTimer(mainInstance.getLootdropsConfig().getInt("drop-interval"));
        ldt.runTaskTimer(mainInstance, 0L, 20L);
        System.out.println(prefix + "Lootdrop timer has been successfully initialized!");
    }
}
