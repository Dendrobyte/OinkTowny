package com.redstoneoinkcraft.oinktowny.ruins.running;

import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import com.redstoneoinkcraft.oinktowny.ruins.RuinsObjLevel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RuinsMobspawningTimer extends BukkitRunnable {

    RuinsManager rm = RuinsManager.getInstance();

    HashMap<EntityType, Integer> monsters = new HashMap<>();
    int total;
    Location playerSpawnLoc;
    ArrayList<Location> monsterSpawnLocs;
    Player player;

    public RuinsMobspawningTimer(ArrayList<String> monstersOnLevel, ArrayList<Location> monsterSpawnLocs, Location playerSpawnLoc, Player player){
        for(String str : monstersOnLevel){
            String monsterStr = str.substring(0, str.indexOf(";"));
            int amount = Integer.parseInt(str.substring(str.indexOf(";")+1));
            EntityType monster = EntityType.valueOf(monsterStr);
            monsters.put(monster, amount);
            total += amount;
        }
        this.playerSpawnLoc = playerSpawnLoc;
        this.monsterSpawnLocs = monsterSpawnLocs;
        this.player = player;
        rm.getPlayerMobSpawningTimers().put(player, this);
        rm.getSpawnedEntities().put(player, new ArrayList<Entity>());
    }

    private Random rand = new Random();

    ArrayList<EntityType> entities = new ArrayList<>();
    // TODO: HashMap<EntityType, SomeObjectRepresentingCustomFeatures> thing...

    // Scramble the mobs for the level so different ones spawn, versus exactly in a row.
    private void scrambleMobs(){
        // Put everything into its own list
        for(HashMap.Entry<EntityType, Integer> entry : monsters.entrySet()){
            EntityType entity = entry.getKey();
            int amount = entry.getValue();
            while(amount > 0){
                entities.add(entity);
                amount--;
            }
        }

        // Shuffle
        Collections.shuffle(entities);
    }


    private void spawnMob(){
        // Location
        int randIndex = rand.nextInt(monsterSpawnLocs.size());
        Location mobSpawnLoc = monsterSpawnLocs.get(randIndex);
        // Mob
        World world = playerSpawnLoc.getWorld();
        Entity entity = world.spawnEntity(mobSpawnLoc, entities.get(0));
        entities.remove(0);
        rm.getSpawnedEntities().get(player).add(entity);
        rm.getSpawnedEntitiesToPlayer().put(entity, player);
    }

    private int delay = 0;
    private boolean scrambled = false;
    @Override
    public void run() {
        if(!scrambled){
            scrambleMobs();
            scrambled = true;
        }
        if (delay <= 0) {
            if(entities.size() == 0){
                this.cancel();
            }
            spawnMob();
            delay = rand.nextInt(5)+1;
        }
        delay--;

    }

}
