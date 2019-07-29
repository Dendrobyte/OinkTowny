package com.redstoneoinkcraft.oinktowny.ruins.creation;

import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import com.redstoneoinkcraft.oinktowny.ruins.RuinsObj;
import com.redstoneoinkcraft.oinktowny.ruins.RuinsObjLevel;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsChatListener implements Listener {

    RuinsManager rm = RuinsManager.getInstance();
    String prefix = rm.getRuinsPrefix();

    @EventHandler
    public void playerChatRuinsCommand(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if(!rm.isPlayerCreatingRuins(player)) return;
        event.setCancelled(true);
        String message = ChatColor.stripColor(event.getMessage());
        RuinsCreationStates playerState = rm.getPlayerCreationState(player);
        RuinsObj ruinsObj = rm.getCreatedRuins(player);
        String goldBold = "" + ChatColor.GOLD + ChatColor.BOLD;
        switch(playerState){
            case LOBBY:
                if(message.equalsIgnoreCase("EXIT")){
                    forceExitRuins(player);
                }
                if(message.equalsIgnoreCase("BEGIN")){
                    rm.setLobbyLocation(rm.getCreatedRuins(player), player.getLocation());
                    rm.setPlayerCreationState(player, RuinsCreationStates.JOIN_SIGNS);
                    player.sendMessage(prefix + "Lobby location set!");
                    player.sendMessage(prefix + "Please " + goldBold + "LEFT CLICK" + ChatColor.getLastColors(prefix) + " all join signs. (It doesn't" +
                            " matter what is written on the signs)");
                    return;
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Please type " + ChatColor.BOLD + "BEGIN" + ChatColor.RED + " at the lobby location.");
                    player.sendMessage(prefix + "Type " + ChatColor.RED + "EXIT" + ChatColor.getLastColors(prefix) + " at any point to leave." + ChatColor.DARK_RED + ChatColor.ITALIC +
                            " (ALL PROGRESS WILL BE LOST)");
                    return;
                }
            case JOIN_SIGNS:
                if(message.equalsIgnoreCase("EXIT")){
                    forceExitRuins(player);
                }
                if(message.equalsIgnoreCase("DONE")){
                    player.sendMessage(prefix + "Join signs added!");
                    rm.setPlayerCreationState(player, RuinsCreationStates.LEVELS);
                    player.sendMessage(prefix + "Type " + goldBold + "NEXT" + ChatColor.getLastColors(prefix) + " to begin level creation!");
                    return;
                } else {
                    player.sendMessage(prefix + "Type " + goldBold + "DONE" + ChatColor.getLastColors(prefix) + " to move on.");
                    player.sendMessage(prefix + "Type " + ChatColor.RED + "EXIT" + ChatColor.getLastColors(prefix) + " at any point to leave." + ChatColor.DARK_RED + ChatColor.ITALIC +
                            " (ALL PROGRESS WILL BE LOST)");
                    return;
                }
            case LEVELS:
                if(message.equalsIgnoreCase("EXIT")){
                    forceExitRuins(player);
                }
                if(message.equalsIgnoreCase("NEXT")){
                    player.sendMessage(prefix + "Beginning creation for a new level...");
                    player.sendMessage(prefix + "Please type " + goldBold + "SPAWNPOINT" + ChatColor.getLastColors(prefix) + " to mark the spawn location of this level.");
                    return;
                }
                else if(message.equalsIgnoreCase("SPAWNPOINT")){
                    if(ruinsObj.getLatestLevel().getSpawnLocation() != null){
                        player.sendMessage(prefix + ChatColor.RED + "A spawnpoint for this level has already been set.");
                        return;
                    }
                    RuinsObjLevel newLevel = new RuinsObjLevel(player.getLocation());
                    ruinsObj.addLevel(newLevel);
                    player.sendMessage(prefix + "Player spawnpoint added!");
                    player.sendMessage(prefix + "Now entering monster addition phase...");
                    player.sendMessage(prefix + "Type " + goldBold + "NEW MONSTER" + ChatColor.getLastColors(prefix) + " to add monsters.");
                    player.sendMessage(prefix + goldBold + "LEFT CLICK" + ChatColor.getLastColors(prefix) + " on blocks where you would like monsters to spawn.");
                    player.sendMessage(prefix + "Type " + ChatColor.RED + "EXIT" + ChatColor.getLastColors(prefix) + " at any point to leave.");
                    rm.setPlayerCreationState(player, RuinsCreationStates.MONSTERS);
                    return;
                }
                else if(message.equalsIgnoreCase("DONE")){
                    // Make sure they added a spawnpoint
                    if(ruinsObj.getLatestLevel().getSpawnLocation() == null){
                        player.sendMessage(prefix + ChatColor.RED + "Please set a spawnpoint!");
                        return;
                    }
                    // Make sure they added monster spawnpoints
                    if(ruinsObj.getLatestLevel().getMonsterSpawnLocations().isEmpty()){
                        player.sendMessage(prefix + ChatColor.RED + "Please add at least one monster spawnpoint.");
                        return;
                    }
                    // Make sure they added mobs
                    if(ruinsObj.getLatestLevel().getMonsters().size() == 0){
                        player.sendMessage(prefix + ChatColor.RED + "You need at least one mob per level.");
                        return;
                    }
                    player.sendMessage(prefix + "Moving on to loot configuration...");
                    rm.setPlayerCreationState(player, RuinsCreationStates.FINAL_LOOT);
                    return;
                }
                else {
                    player.sendMessage(prefix + "Type " + goldBold + "SPAWNPOINT" + ChatColor.getLastColors(prefix) + " to mark this level's spawnpoint.");
                    player.sendMessage(prefix + "Finish creation by typing " + goldBold + "DONE" + ChatColor.getLastColors(prefix) + ".");
                    player.sendMessage(prefix + "Type " + ChatColor.RED + "EXIT" + ChatColor.getLastColors(prefix) + " at any point to leave.");
                    return;
            }
            case MONSTERS:
                if(message.equalsIgnoreCase("EXIT")){
                    forceExitRuins(player);
                }
                if(message.substring(0, 3).equalsIgnoreCase("NEW")){
                    if(message.substring(4).equalsIgnoreCase("MONSTER")){
                        player.sendMessage(prefix + "To create a monster: " + goldBold + "NEW MOB_TYPE AMOUNT");
                        player.sendMessage(prefix + goldBold + "Valid Types: " + ChatColor.getLastColors(prefix) + "[ZOMBIE, SKELETON, WITHER_SKELETON, CREEPER, CAVE_SPIDER, WITCH]");
                        player.sendMessage(prefix + "Amounts should be whole numbers.");
                        player.sendMessage(prefix + "Example: " + goldBold + "NEW ZOMBIE 10");
                        return;
                    }
                    else if(message.substring(4).equalsIgnoreCase("ROOM")){
                        if(ruinsObj.getLatestLevel().getMonsters().size() == 0){
                            player.sendMessage(prefix + ChatColor.RED + "You need at least one mob per level.");
                            return;
                        }
                        rm.setPlayerCreationState(player, RuinsCreationStates.LEVELS);
                        player.sendMessage(prefix + "Please type " + goldBold + "NEXT" + ChatColor.getLastColors(prefix) + " to start a new level.");
                        return;
                    }
                    String[] creationString = message.split(" ");
                    EntityType entityType;
                    try {
                        entityType = EntityType.valueOf(creationString[1].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(prefix + "Mob type invalid! Type" + goldBold + " NEW MONSTER " + ChatColor.getLastColors(prefix) + "to see the creation info.");
                        return;
                    }
                    int amount;
                    try {
                        amount = Integer.parseInt(creationString[2]);
                    } catch(NumberFormatException e){
                        player.sendMessage(prefix + "Amount invalid! Type" + goldBold + " NEW MONSTER " + ChatColor.getLastColors(prefix) + "to see the creation info.");
                        return;
                    }

                    String finalString = entityType.toString() + ";" + amount;
                    ruinsObj.getLatestLevel().addMonster(finalString);
                    player.sendMessage(prefix + "Added " + goldBold + amount + ChatColor.getLastColors(prefix) + " of " + goldBold + entityType.toString() + ChatColor.getLastColors(prefix) +  " entities.");
                    player.sendMessage(prefix + "Create a new monster. move on to another room by typing" + goldBold + " NEW ROOM" + ChatColor.getLastColors(prefix) +
                            ", or finish by typing " + goldBold + "DONE");

                    return;
                }
                else if(message.equalsIgnoreCase("DONE")){
                    if(ruinsObj.getLatestLevel().getMonsters().size() == 0) {
                        player.sendMessage(prefix + ChatColor.RED + "You need at least one mob per level.");
                        return;
                    } else {
                        player.sendMessage(prefix + "Moving on to loot configuration...");
                        player.sendMessage(prefix + "Configure your inventory to contain the loot players should be awarded.");
                        player.sendMessage(prefix + "When you're done, type " + goldBold + "FINISH" + ChatColor.getLastColors(prefix) + ". (This will wrap up ruin creation)");
                        rm.setPlayerCreationState(player, RuinsCreationStates.FINAL_LOOT);
                        return;
                    }
                }
                else {
                    player.sendMessage(prefix + "Type " + goldBold + "NEW ROOM" + ChatColor.getLastColors(prefix) + " when finished to start creation of a new level.");
                    player.sendMessage(prefix + "To move on to the last step, type " + goldBold + "DONE" + ChatColor.getLastColors(prefix) + " to configure the final loot.");
                    return;
                }
            case FINAL_LOOT:
                if(message.equalsIgnoreCase("EXIT")){
                    forceExitRuins(player);
                }
                if(message.equalsIgnoreCase("FINISH")){
                    if(ruinsObj.getFirstLevel() == null){
                        player.sendMessage(prefix + "Please set up at least one level.");
                        return;
                    }
                    player.sendMessage(prefix + "Wrapping up ruin creation for " + goldBold + ruinsObj.getName() + ChatColor.getLastColors(prefix) + "... (This may take a second)");
                    ruinsObj.setRewardItems(player.getInventory());
                    rm.endRuinsCreation(player, ruinsObj);
                    player.sendMessage(prefix + "The " + goldBold + ruinsObj.getName() + ChatColor.getLastColors(prefix) + " ruins have been configured!");
                } else {
                    player.sendMessage(prefix + "Configure your inventory to contain the loot players should be awarded.");
                    player.sendMessage(prefix + "When you're done, type " + goldBold + "FINISH" + ChatColor.getLastColors(prefix) + ". (This will wrap up ruin creation)");
                }
        }
    }

    private void forceExitRuins(Player player){
        rm.removePlayerFromCreation(player);
        player.sendMessage(prefix + ChatColor.RED + "Ruins creation quit. " + ChatColor.BOLD + "All progress lost.");
    }

}
