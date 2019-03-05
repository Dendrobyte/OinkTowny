package com.redstoneoinkcraft.oinktowny.regions;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.clans.ClanManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RegionBlockPlaceBreakListener implements Listener {

    Main mainInstance = Main.getInstance();
    String prefix = mainInstance.getPrefix();

    RegionsManager rm = RegionsManager.getInstance();
    ClanManager cm = ClanManager.getInstance();

    @EventHandler
    public void onBreakInRegion(BlockBreakEvent event){

    }

    @EventHandler
    public void onPlaceInRegion(BlockPlaceEvent event){

    }


}
