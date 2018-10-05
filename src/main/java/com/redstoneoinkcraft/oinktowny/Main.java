package com.redstoneoinkcraft.oinktowny;

import com.redstoneoinkcraft.oinktowny.economy.InventoryPurchaseListener;
import com.redstoneoinkcraft.oinktowny.economy.SignClickListener;
import com.redstoneoinkcraft.oinktowny.listeners.PlayerJoinWorldListener;
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
    private String prefix = "§8(§3OinkTown-y§8)§3 ";

    /* Custom configurations */
    // bundlesFile.yml
    private File bundlesFile;
    private FileConfiguration bundlesConfig;


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
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryPurchaseListener(), this);

        // Register Commands
        getCommand("oinktowny").setExecutor(new BaseCommand());

        getLogger().log(Level.INFO, "OinkTown-y v" + getDescription().getVersion() + " has successfully been enabled!");
    }

    @Override
    public void onDisable(){
        saveConfig();
        saveBundlesConfig();
        getLogger().log(Level.INFO, "OinkTowny-y v" + getDescription().getVersion() + " has successfully been disabled!");
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
            getLogger().log(Level.INFO, "OinkTown-y v" + getDescription().getVersion() + " is creating the configuration...");
            saveDefaultConfig();
            getLogger().log(Level.INFO, "OinkTown-y v" + getDescription().getVersion() + " configuration has been created!");
        } else {
            getLogger().log(Level.INFO, "OinkTown-y v" + getDescription().getVersion() + " configuration has been loaded.");
        }
        // Generate bundlesFile.yml
        bundlesFile = new File(getDataFolder(), "bundles.yml");
        if(!bundlesFile.exists()){
            getLogger().log(Level.INFO, "OinkTown-y v" + getDescription().getVersion() + " is creating the bundles.yml...");
            saveResource("bundles.yml", false);
            getLogger().log(Level.INFO, "OinkTown-y v" + getDescription().getVersion() + " bundles.yml has been created!");
        } else {
            bundlesConfig = new YamlConfiguration();
            try {
                bundlesConfig.load(bundlesFile);
            } catch (IOException | InvalidConfigurationException e){
                getLogger().log(Level.WARNING, "OinkTowny-y bundles.yml could not be loaded!");
                e.printStackTrace();
            }
            getLogger().log(Level.INFO, "OinkTown-y v" + getDescription().getVersion() + " bundles.yml has been loaded.");
        }
    }

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
    public File getBundlesFile() { return this.bundlesFile; }

}
