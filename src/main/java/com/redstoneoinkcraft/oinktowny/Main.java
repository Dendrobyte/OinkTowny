package com.redstoneoinkcraft.oinktowny;

import com.redstoneoinkcraft.oinktowny.bettersleep.SleepListener;
import com.redstoneoinkcraft.oinktowny.bundles.SignClickListener;
import com.redstoneoinkcraft.oinktowny.clans.ClanChatListener;
import com.redstoneoinkcraft.oinktowny.clans.ClanManager;
import com.redstoneoinkcraft.oinktowny.listeners.PlayerJoinWorldListener;
import com.redstoneoinkcraft.oinktowny.lootdrops.LootdropManager;
import com.redstoneoinkcraft.oinktowny.regions.RegionBlockPlaceBreakListener;
import com.redstoneoinkcraft.oinktowny.regions.RegionsManager;
import com.redstoneoinkcraft.oinktowny.regions.SuperpickCommand;
import com.redstoneoinkcraft.oinktowny.regions.SuperpickListeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 8/17/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private String prefix = "§8(§3OinkTowny§8)§3 ";

    /* Custom configurations */
    // bundles.yml
    private File bundlesFile;
    private FileConfiguration bundlesConfig;
    // clans.yml
    private File clansFile;
    private FileConfiguration clansConfig;
    // lootdrops.yml
    private File lootdropsFile;
    private FileConfiguration lootdropsConfig;
    // regions.yml
    private File regionsFile;
    private FileConfiguration regionsConfig;


    @Override
    public void onEnable(){
        // Define instance
        instance = this;

        // Create config
        createConfig();

        /* Register Events */
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinWorldListener(), this);
        // Economy events
        Bukkit.getServer().getPluginManager().registerEvents(new SignClickListener(), this);
        // Region events
        Bukkit.getServer().getPluginManager().registerEvents(new SuperpickListeners(), this);
        // Clan events
        Bukkit.getServer().getPluginManager().registerEvents(new ClanChatListener(), this);
        // Region events
        Bukkit.getServer().getPluginManager().registerEvents(new RegionBlockPlaceBreakListener(), this);
        // Better sleep events
        Bukkit.getServer().getPluginManager().registerEvents(new SleepListener(), this);

        // Register Commands
        getCommand("oinktowny").setExecutor(new BaseCommand());
        getCommand("superpick").setExecutor(new SuperpickCommand());

        /* Initialize Clan */
        ClanManager.getInstance().cacheClans();

        /* Begin the lootdrop timer */
        LootdropManager.getInstance().initializeLootdropTimer();

        /* Initialize regions */
        RegionsManager.getInstance().cacheRegions();

        // Finish
        getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " has successfully been enabled!");
    }

    @Override
    public void onDisable(){
        saveConfig();
        getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " has successfully been disabled!");
    }

    public static Main getInstance(){
        return instance;
    }

    public String getPrefix(){
        return prefix;
    }

    // Configuration file methods
    private void createConfig(){
        if(!getDataFolder().exists()){
            getDataFolder().mkdirs();
        }
        // Generate default config.yml
        File configuration = new File(getDataFolder(), "config.yml");
        if(!configuration.exists()){
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " is creating the configuration...");
            saveDefaultConfig();
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " configuration has been created!");
        } else {
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " configuration has been loaded.");
        }
        // Generate bundles.yml
        bundlesFile = new File(getDataFolder(), "bundles.yml");
        if(!bundlesFile.exists()){
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " is creating the bundles.yml...");
            saveResource("bundles.yml", false);
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " bundles.yml has been created!");
        } else {
            bundlesConfig = new YamlConfiguration();
            try {
                bundlesConfig.load(bundlesFile);
            } catch (IOException | InvalidConfigurationException e){
                getLogger().log(Level.WARNING, "OinkTownyy bundles.yml could not be loaded!");
                e.printStackTrace();
            }
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " bundles.yml has been loaded.");
        }
        // Generate clans.yml
        clansFile = new File(getDataFolder(), "clans.yml");
        if(!clansFile.exists()){
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " is creating the clans.yml...");
            saveResource("clans.yml", false);
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " clans.yml has been created!");
        } else {
            clansConfig = new YamlConfiguration();
            try {
                clansConfig.load(clansFile);
            } catch (IOException | InvalidConfigurationException e){
                getLogger().log(Level.WARNING, "OinkTowny clans.yml could not be loaded!");
                e.printStackTrace();
            }
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " clans.yml has been loaded.");
        }
        // Generate lootdrops.yml
        lootdropsFile = new File(getDataFolder(), "lootdrops.yml");
        if(!lootdropsFile.exists()){
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " is creating the lootdrops.yml...");
            saveResource("lootdrops.yml", false);
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " lootdrops.yml has been created!");
        } else {
            lootdropsConfig = new YamlConfiguration();
            try {
                lootdropsConfig.load(lootdropsFile);
            } catch (IOException | InvalidConfigurationException e){
                getLogger().log(Level.WARNING, "OinkTowny lootdrops.yml could not be loaded!");
                e.printStackTrace();
            }
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " lootdrops.yml has been loaded.");
        }
        // Generate regions.yml
        regionsFile = new File(getDataFolder(), "regions.yml");
        if(!regionsFile.exists()){
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " is creating the regions.yml...");
            saveResource("regions.yml", false);
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " regions.yml has been created!");
        } else {
            regionsConfig = new YamlConfiguration();
            try {
                regionsConfig.load(regionsFile);
            } catch (IOException | InvalidConfigurationException e){
                getLogger().log(Level.WARNING, "OinkTowny lootdrops.yml could not be loaded!");
                e.printStackTrace();
            }
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " regions.yml has been loaded.");
        }
    }

    // Bundles config file methods
    public void saveBundlesConfig(){
        saveResource("bundles.yml", true);
        try {
            Main.getInstance().getBundlesConfig().save(getBundlesFile());
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public FileConfiguration getBundlesConfig(){
        return this.bundlesConfig;
    }
    private File getBundlesFile() { return this.bundlesFile; }

    // Clans config file methods
    public void saveClansConfig(){
        saveResource("clans.yml", true);
        try {
            Main.getInstance().getClansConfig().save(getClansFile());
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public FileConfiguration getClansConfig(){
        return this.clansConfig;
    }
    private File getClansFile() { return this.clansFile; }

    // Lootdrops config file methods
    public void saveLootdropsConfig(){
        saveResource("lootdrops.yml", true);
        try{
            Main.getInstance().getLootdropsConfig().save(getLootdropsFile());
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public FileConfiguration getLootdropsConfig() {
        return this.lootdropsConfig;
    }
    private File getLootdropsFile(){
        return this.lootdropsFile;
    }

    // Regions config file methods
    public void saveRegionsConfig(){
        saveResource("regions.yml", true);
        try{
            Main.getInstance().getRegionsConfig().save(getRegionsFile());
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public FileConfiguration getRegionsConfig() {
        return this.regionsConfig;
    }
    private File getRegionsFile(){
        return this.regionsFile;
    }

}
