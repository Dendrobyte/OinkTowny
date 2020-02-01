package com.redstoneoinkcraft.oinktowny;

import com.google.common.base.Charsets;
import com.redstoneoinkcraft.oinktowny.arenapvp.*;
import com.redstoneoinkcraft.oinktowny.artifacts.ArtifactsListeners;
import com.redstoneoinkcraft.oinktowny.bettersleep.SleepListener;
import com.redstoneoinkcraft.oinktowny.bundles.PreventItemStealListener;
import com.redstoneoinkcraft.oinktowny.bundles.SignClickListener;
import com.redstoneoinkcraft.oinktowny.clans.ClanChatListener;
import com.redstoneoinkcraft.oinktowny.clans.ClanManager;
import com.redstoneoinkcraft.oinktowny.clans.ClanUpdateUuidListener;
import com.redstoneoinkcraft.oinktowny.customenchants.utils.EnchantAnvilListener;
import com.redstoneoinkcraft.oinktowny.customenchants.utils.EnchantListeners;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
import com.redstoneoinkcraft.oinktowny.economy.TownyBankInvListener;
import com.redstoneoinkcraft.oinktowny.listeners.PlayerDeathListener;
import com.redstoneoinkcraft.oinktowny.listeners.PlayerJoinWorldListener;
import com.redstoneoinkcraft.oinktowny.listeners.TreeFellerBreakListener;
import com.redstoneoinkcraft.oinktowny.lockette.*;
import com.redstoneoinkcraft.oinktowny.lootdrops.LootdropManager;
import com.redstoneoinkcraft.oinktowny.lootdrops.LootdropOpenListener;
import com.redstoneoinkcraft.oinktowny.portals.PortalListener;
import com.redstoneoinkcraft.oinktowny.regions.*;
import com.redstoneoinkcraft.oinktowny.ruins.creation.RuinsChatListener;
import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import com.redstoneoinkcraft.oinktowny.ruins.creation.RuinsSelectionListener;
import com.redstoneoinkcraft.oinktowny.ruins.running.RuinsEntityDeathListener;
import com.redstoneoinkcraft.oinktowny.ruins.running.RuinsPlayerLeaveListeners;
import com.redstoneoinkcraft.oinktowny.ruins.running.RuinsSignClickListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 8/17/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private String prefix = "§8(§3OinkTowny§8)§3 ";
    private String worldName, netherWorldName, endWorldName;

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
    // arenas.yml
    private File arenasFile;
    private FileConfiguration arenasConfig;
    // ruins.yml
    private File ruinsFile;
    private FileConfiguration ruinsConfig;


    @Override
    public void onEnable(){
        // Define instance
        instance = this;

        // Create config
        createConfig();

        // Set the main world name to retrieve from various functions
        worldName = getConfig().getString("world-name");
        netherWorldName = getConfig().getString("world-nether");
        endWorldName = getConfig().getString("world-end");

        // Reset the list for Token UBI
        if (LocalDate.now().getDayOfWeek().toString().equalsIgnoreCase("SUNDAY")){
            Main.getInstance().getConfig().set("players-paid", null);
            Bukkit.getLogger().log(Level.INFO, "UBI Token List Cleared");
            Main.getInstance().saveConfig();
            Main.getInstance().reloadConfig();
        }

        /* Register Events */
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinWorldListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        // Bukkit.getServer().getPluginManager().registerEvents(new TreeFellerBreakListener(), this);
        // Bundle events
        Bukkit.getServer().getPluginManager().registerEvents(new SignClickListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PreventItemStealListener(), this);
        // Region events
        Bukkit.getServer().getPluginManager().registerEvents(new SuperpickListeners(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RegionBlockPlaceBreakListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PreventTntInClaimsListener(), this);
        // Clan events
        Bukkit.getServer().getPluginManager().registerEvents(new ClanChatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ClanUpdateUuidListener(), this);
        // Better sleep events
        Bukkit.getServer().getPluginManager().registerEvents(new SleepListener(), this);
        // PvP Arena events
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaSignListeners(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerQuitListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaClickListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDamageListener(), this);
        // Lootdrops
        Bukkit.getServer().getPluginManager().registerEvents(new LootdropOpenListener(), this);
        // Enchantments
        Bukkit.getServer().getPluginManager().registerEvents(new EnchantListeners(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EnchantAnvilListener(), this);
        // Portals
        Bukkit.getServer().getPluginManager().registerEvents(new PortalListener(), this);
        // Artifacts
        Bukkit.getServer().getPluginManager().registerEvents(new ArtifactsListeners(), this);
        // Economy
        Bukkit.getServer().getPluginManager().registerEvents(new TownyBankInvListener(), this);
        // Ruins
        Bukkit.getServer().getPluginManager().registerEvents(new RuinsChatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RuinsSelectionListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RuinsSignClickListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RuinsEntityDeathListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RuinsPlayerLeaveListeners(), this);
        /* Lockette (Clicking method)
        Bukkit.getServer().getPluginManager().registerEvents(new LocketteChestPlaceBreakListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LocketteChestPrivatedListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LocketteChatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LocketteDoorListener(), this); */

        // Register Commands
        getCommand("oinktowny").setExecutor(new BaseCommand());
        getCommand("superpick").setExecutor(new SuperpickCommand());

        /* Initialize Clan */
        ClanManager.getInstance().cacheClans();

        /* Begin the lootdrop timer */
        LootdropManager.getInstance().initializeLootdropTimer();

        /* Initialize regions */
        RegionsManager.getInstance().cacheRegions();

        /* Load arenas */
        ArenaPVPManager.getInstance().loadArenas();

        /* Rebuild ruins */
        RuinsManager.getInstance().rebuildRuins();

        /* Register enchantments */
        EnchantmentManager.registerEnchants();

        /* Store Lockette chests in memory */
        // LocketteManager.getInstance().loadChests();

        // Finish
        getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " has successfully been enabled!");
    }

    @Override
    public void onDisable(){
        //saveConfig();
        getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " has successfully been disabled!");
    }

    public static Main getInstance(){
        return instance;
    }

    public String getPrefix(){
        return prefix;
    }

    public String getWorldName(){
        return worldName;
    }
    public String getNetherWorldName() {
        return netherWorldName;
    }
    public String getEndWorldName() { return endWorldName; }

    public boolean isTownyWorld(String name) {
        return name.equals(worldName) || name.equals(netherWorldName) || name.equals(endWorldName);
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

        // Generate arenas.yml
        arenasFile = new File(getDataFolder(), "arenas.yml");
        if(!arenasFile.exists()){
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " is creating the arenas.yml...");
            saveResource("arenas.yml", false);
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " arenas.yml has been created!");
        } else {
            arenasConfig = new YamlConfiguration();
            try {
                arenasConfig.load(arenasFile);
            } catch (IOException | InvalidConfigurationException e){
                getLogger().log(Level.WARNING, "OinkTowny arenas.yml could not be loaded!");
                e.printStackTrace();
            }
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " arenas.yml has been loaded.");
        }

        // Generate ruins.yml
        ruinsFile = new File(getDataFolder(), "ruins.yml");
        if(!ruinsFile.exists()){
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " is creating the ruins.yml...");
            saveResource("ruins.yml", false);
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " ruins.yml has been created!");
        } else {
            ruinsConfig = new YamlConfiguration();
            try {
                ruinsConfig.load(ruinsFile);
            } catch (IOException | InvalidConfigurationException e){
                getLogger().log(Level.WARNING, "OInkTowny ruins.yml could not be loaded!");
                e.printStackTrace();
            }
            getLogger().log(Level.INFO, "OinkTowny v" + getDescription().getVersion() + " ruins.yml has been loaded.");
        }
    }

    // Bundles config file methods
    // TODO: Make one method for all creation files and just pass in what changes. oops.
    public void saveBundlesConfig(){
        saveResource("bundles.yml", true);
        try {
            Main.getInstance().getBundlesConfig().save(getBundlesFile());
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reloadBundlesConfig(){
        bundlesFile = new File(getDataFolder(), "bundles.yml");
        bundlesConfig = YamlConfiguration.loadConfiguration(bundlesFile);
        getLogger().log(Level.INFO, "[OinkTowny] Bundles configuration reloaded!");
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

    public void reloadClansConfig(){
        clansFile = new File(getDataFolder(), "clans.yml");
        clansConfig = YamlConfiguration.loadConfiguration(clansFile);
        getLogger().log(Level.INFO, "[OinkTowny] Clans configuration reloaded!");
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

    public void reloadLootdropsConfig(){
        lootdropsFile = new File(getDataFolder(), "lootdrops.yml");
        lootdropsConfig = YamlConfiguration.loadConfiguration(lootdropsFile);
        getLogger().log(Level.INFO, "[OinkTowny] Lootdrops configuration reloaded!");
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

    public void reloadRegionsConfig(){
        regionsFile = new File(getDataFolder(), "regions.yml");
        regionsConfig = YamlConfiguration.loadConfiguration(regionsFile);
        getLogger().log(Level.INFO, "[OinkTowny] Regions configuration reloaded!");
    }

    public FileConfiguration getRegionsConfig() {
        return this.regionsConfig;
    }
    private File getRegionsFile(){
        return this.regionsFile;
    }

    // Arenas config file methods
    public void saveArenasConfig(){
        saveResource("arenas.yml", true);
        try{
            Main.getInstance().getArenasConfig().save(getArenasFile());
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reloadArenasConfig(){
        arenasFile = new File(getDataFolder(), "arenas.yml");
        arenasConfig = YamlConfiguration.loadConfiguration(arenasFile);
        getLogger().log(Level.INFO, "[OinkTowny] Arenas configuration reloaded!");
    }

    public FileConfiguration getArenasConfig() {
        return this.arenasConfig;
    }
    private File getArenasFile(){
        return this.arenasFile;
    }

    // Ruins config file methods
    public void saveRuinsConfig(){
        saveResource("ruins.yml", true);
        try{
            Main.getInstance().getRuinsConfig().save(getRuinsFile());
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reloadRuinsConfig(){
        ruinsFile = new File(getDataFolder(), "ruins.yml");
        ruinsConfig = YamlConfiguration.loadConfiguration(ruinsFile);
        getLogger().log(Level.INFO, "[OinkTowny] Ruins configuration reloaded!");
    }

    public FileConfiguration getRuinsConfig(){
        return this.ruinsConfig;
    }
    public File getRuinsFile(){
        return this.ruinsFile;
    }

    // Reload all configuration files
    public void reloadAllConfigurations(){
        reloadConfig();
        reloadBundlesConfig();
        reloadArenasConfig();
        reloadClansConfig();
        reloadLootdropsConfig();
        reloadRuinsConfig();
        reloadRegionsConfig();
    }



}
