package com.redstoneoinkcraft.oinktowny.lootdrops;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.economy.TownyTokenManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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
    World townyWorld = Bukkit.getServer().getWorld(mainInstance.getLootdropsConfig().getString("world-name"));
    // In case someone puts the values in as negative... [they should really be positive though]
    private int x_2 = x_1 * -1;
    private int z_2 = z_1 * -1;

    private LootdropManager(){}

    public static LootdropManager getInstance(){
        return instance;
    }

    // Generate random location, then pass into drop loot location
    private Location generateRandomDropLocation(){
        Random random = new Random();
        int resultX = random.nextInt(x_1);
        int resultZ = random.nextInt(z_1);
        int resultY = generateRandomY(resultX, resultZ);
        Bukkit.getPlayer("Mobkinz78").teleport(new Location(townyWorld, resultX, resultY, resultZ));
        return new Location(townyWorld, resultX, resultY, resultZ);
    }

    // Generate a working y value to place a chest
    // TODO: Find a y value on the ground instead of guessing via random check
    private int generateRandomY(int x, int z){
        Random random = new Random();
        boolean isValid = false;
        int counter = 0;
        int returnY = 67;
        while(!isValid){
            if(counter == 40){
                isValid = true; // Just to break out of the while loop
                System.out.println("2: Y value not generated- 67 returned as default");
            } else {
                int yLoc = random.nextInt(40) + 40; // Limited to certain altitudes
                Location below = new Location(townyWorld, x, yLoc - 1, z);
                Location above = new Location(townyWorld, x, yLoc + 1, z);
                if (below.getBlock().getType().equals(Material.AIR) ||
                        !above.getBlock().getType().equals(Material.AIR)) { // Nesting these ifs for readability
                    counter++;
                } else {
                    returnY = yLoc;
                    isValid = true;
                }
            }
        }

        return returnY;
    }

    // Generate values based on player location, then pass into drop loot location

    // Drop loot
    public void dropLootChest(Location location){
        location.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) location.getBlock().getState();
        Inventory chestInv = chest.getBlockInventory();
        fillLootContents(chestInv);
    }

    // Drop loot randomly
    public void dropLootChestRandom(){
        Location dropLoc = generateRandomDropLocation();
        dropLootChest(dropLoc);
        broadcastDrop(dropLoc);
    }

    // Drop loot predictably (mainly serves debug purposes)
    public void dropLootChestPredictably(Player player){
        if(!player.getLocation().getWorld().getName().equalsIgnoreCase(townyWorld.getName())){
            player.sendMessage(prefix + "You are not in a world with towny drops enabled!");
            return;
        }
        dropLootChest(player.getLocation());
        player.sendMessage(prefix + "Loot dropping at your location...");
    }

    // Fill contents of a passed in inventory with random loot
    private void fillLootContents(Inventory inv){
        ArrayList<ItemStack> finalItems = new ArrayList<>();
        // Get 4 tier one items
        addItemsToList(finalItems, mainInstance.getLootdropsConfig().getStringList("tier1"), 4);

        // Get 2 tier two items
        addItemsToList(finalItems, mainInstance.getLootdropsConfig().getStringList("tier2"), 2);

        // Get 1 tier three items
        addItemsToList(finalItems, mainInstance.getLootdropsConfig().getStringList("tier3"), 1);

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
        LootDropTimer ldt = new LootDropTimer(mainInstance.getLootdropsConfig().getInt("drop-interval"));
        ldt.runTaskTimer(mainInstance, 0L, 20L);
    }

}
