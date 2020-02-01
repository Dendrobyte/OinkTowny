package com.redstoneoinkcraft.oinktowny;

import com.redstoneoinkcraft.oinktowny.arenapvp.ArenaPVPManager;
import com.redstoneoinkcraft.oinktowny.artifacts.ArtifactManager;
import com.redstoneoinkcraft.oinktowny.bundles.BundleManager;
import com.redstoneoinkcraft.oinktowny.clans.ClanManager;
import com.redstoneoinkcraft.oinktowny.customenchants.EnchantmentManager;
import com.redstoneoinkcraft.oinktowny.economy.TownyTokenManager;
import com.redstoneoinkcraft.oinktowny.lootdrops.LootdropManager;
import com.redstoneoinkcraft.oinktowny.regions.RegionsManager;
import com.redstoneoinkcraft.oinktowny.ruins.RuinsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * OinkTowny Features created/started by Mark Bacon (Mobkinz78 or ByteKangaroo) on 9/1/2018
 * Please do not use or edit without permission! (Being on GitHub counts as permission)
 * If you have any questions, reach out to me on Twitter! @Mobkinz78
 * §
 */
public class BaseCommand implements CommandExecutor {

    String prefix = Main.getInstance().getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage(prefix + "Sorry, you have to be a player to run these commands!");
            sender.sendMessage(prefix + "We use player inventories and, well, you don't have one... =(");
            return true;
        }
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("oinktowny")){
            if(args.length == 0){
                player.sendMessage(prefix + "No arguments provided! " + ChatColor.GOLD + "/oinktowny help");
                return true;
            }
            /* HELP STUFF */
            if(args[0].equalsIgnoreCase("help")){
                player.sendMessage(prefix + ChatColor.GOLD + ChatColor.BOLD + "-+OinkTowny Commands+-");
                player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny bundle");
                player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny token");
                player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny claim");
                player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny clan");
                player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny lootdrop" + ChatColor.DARK_RED + ChatColor.BOLD + " ADMIN COMMAND");
                player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny arena" + ChatColor.DARK_RED + ChatColor.BOLD + " ADMIN COMMAND");
                player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny artifact" + ChatColor.DARK_RED + ChatColor.BOLD + " ADMIN COMMAND");
                player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny ruins" + ChatColor.DARK_RED + ChatColor.BOLD + " ADMIN COMMAND");
                player.sendMessage(ChatColor.GOLD + "To chat with your clan, start your message with " + ChatColor.GOLD + ChatColor.BOLD
                        + ChatColor.DARK_AQUA + "%");
                return true;
            }

            /* BUNDLE STUFF */
            if(args[0].equalsIgnoreCase("bundle")){
                BundleManager bundleManager = BundleManager.getInstance();
                if(!player.hasPermission("oinktowny.bundle")){
                    player.sendMessage(prefix + "Sorry, you don't have access to do this." + ChatColor.RED + "oinktowny.bundle");
                    return true;
                }
                /* Command structure: /oinktowny bundle ... */
                if(args.length == 1){
                    player.sendMessage(prefix + ChatColor.GOLD + ChatColor.BOLD + "-+OinkTowny Bundle Commands+-");
                    player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny bundle ...");
                    player.sendMessage(ChatColor.GOLD + "- create <name>" + ChatColor.DARK_AQUA + " - Create a new bundle");
                    player.sendMessage(ChatColor.GOLD + "- override <name>" + ChatColor.DARK_AQUA + " - Override an existing bundle");
                    player.sendMessage(ChatColor.GOLD + "- remove <name>" + ChatColor.DARK_AQUA + " - Remove an existing bundle");
                    player.sendMessage(ChatColor.GOLD + "- give <name>" + ChatColor.DARK_AQUA + " - Give a bundle to yourself");
                    player.sendMessage(ChatColor.GOLD + "- give <bundle> <player>" + ChatColor.DARK_AQUA + " - Give a bundle to a player");
                    player.sendMessage(ChatColor.GOLD + "- reload" + ChatColor.DARK_AQUA + " - Reload the bundle configuration");
                    return true;
                }
                if(args[1].equalsIgnoreCase("create")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please provide a bundle name!");
                        return true;
                    }
                    String bundleName = args[2];
                    bundleManager.createBundleItems(player, bundleName, false);
                    return true;
                }
                if(args[1].equalsIgnoreCase("override")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please provide a bundle name!");
                        player.sendMessage(bundleManager.listBundles());
                        return true;
                    }
                    String bundleName = args[2];
                    if(bundleManager.bundleExists(bundleName)) {
                        bundleManager.createBundleItems(player, bundleName, true);
                        player.sendMessage(prefix + ChatColor.GREEN + "(Bundle override successful)");
                    } else {
                        player.sendMessage(prefix + ChatColor.AQUA + "That bundle doesn't exist. Please use " + ChatColor.GOLD + "/ot bundle create <name>");
                    }
                    return true;
                }
                if(args[1].equalsIgnoreCase("give")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please provide a bundle name!");
                        player.sendMessage(bundleManager.listBundles());
                        return true;
                    }
                    String bundleName = args[2];
                    if(args.length == 3){
                        bundleManager.giveBundle(bundleName, player);
                        return true;
                    }
                    if(args.length == 4){
                        String playerName = args[3];
                        Player otherPlayer = Bukkit.getPlayer(playerName);
                        bundleManager.giveBundle(bundleName, otherPlayer);
                        otherPlayer.sendMessage(prefix + "Successfully gave the " + ChatColor.GOLD + ChatColor.BOLD + bundleName + ChatColor.getLastColors(prefix) + " bundle to " + otherPlayer.getName());
                        return true;
                    }
                    return true;
                }
                if(args[1].equalsIgnoreCase("remove")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please provide a bundle name!");
                        player.sendMessage(bundleManager.listBundles());
                        return true;
                    }
                    String bundleName = args[2];
                    if(bundleManager.bundleExists(bundleName)) {
                        bundleManager.removeBundle(bundleName, player);
                        player.sendMessage(prefix + ChatColor.GREEN + "(Bundle has been eradicated)");
                    } else {
                        player.sendMessage(prefix + ChatColor.AQUA + "That bundle doesn't exist.");
                    }
                    return true;
                }
                if(args[1].equalsIgnoreCase("list")){
                    player.sendMessage(bundleManager.listBundles());
                    return true;
                }
                if(args[1].equalsIgnoreCase("reload")){
                    player.sendMessage(prefix + "Bundle configuration reload command not yet implemented");
                }
                else {
                    player.sendMessage(prefix + "Unrecognized argument! \n" + prefix + "To see usages, type " + ChatColor.GOLD + "/ot bundle");
                    return true;
                }
            }

            /* TOWNYTOKEN STUFF */
            TownyTokenManager ttm = TownyTokenManager.getInstance();
            if(args[0].equalsIgnoreCase("townybank")) {
                // TODO: Implement args for 'bank' and 'box'
                ttm.openPlayerBox(player);
                return true;
            }
            /* For admin usage
            if(args[0].equalsIgnoreCase("token")){
                TownyTokenManager ttManager = TownyTokenManager.getInstance();
                int amt = Integer.parseInt(args[1]);
                player.getInventory().setItem(0, ttManager.createToken(amt));
                return true;
            } */

            /* CLAN/CLAN CHAT STUFF */
            if(args[0].equalsIgnoreCase("clan")){
                ClanManager cm = ClanManager.getInstance();
                if(!player.hasPermission("oinktowny.clans")){
                    player.sendMessage(prefix + "Sorry, you don't have access to do this." + ChatColor.RED + "oinktowny.clans");
                    return true;
                }
                /* Command structure: /oinktowny clan ... */
                if(args.length == 1){
                    player.sendMessage(prefix + ChatColor.GOLD + ChatColor.BOLD + "-+OinkTowny Clan Commands+-");
                    player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny clan ...");
                    player.sendMessage(ChatColor.GOLD + "- create");
                    player.sendMessage(ChatColor.GOLD + "- disband");
                    player.sendMessage(ChatColor.GOLD + "- invite");
                    player.sendMessage(ChatColor.GOLD + "- kick");
                    player.sendMessage(ChatColor.GOLD + "- leave");
                    player.sendMessage(ChatColor.GOLD + "- list");
                    player.sendMessage(ChatColor.GOLD + "To chat with your clan, start your message with " + ChatColor.GOLD + ChatColor.BOLD
                            + ChatColor.DARK_AQUA + "%");
                    return true;
                }

                if(args[1].equalsIgnoreCase("create")){
                    cm.createClan(player);
                    return true;
                }
                if(args[1].equalsIgnoreCase("disband")){
                    cm.disbandClan(player);
                    return true;
                }
                if(args[1].equalsIgnoreCase("invite")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please enter a player's name!");
                        return true;
                    }
                    String name = args[2];
                    Player invitee = Bukkit.getServer().getPlayer(name);
                    if(invitee == null){
                        player.sendMessage(prefix + "The player " + name + " does not appear to be online.");
                        return true;
                    }
                    cm.inviteToClan(player, invitee);
                    return true;
                }
                if(args[1].equalsIgnoreCase("accept")){
                    cm.acceptClanInvite(player);
                    return true;
                }
                if(args[1].equalsIgnoreCase("deny")){
                    cm.denyClanInvite(player);
                    return true;
                }
                if(args[1].equalsIgnoreCase("kick")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please enter a player's name!");
                        return true;
                    }
                    String name = args[2];
                    Player kicked = Bukkit.getServer().getPlayer(name);
                    if(kicked == null){
                        player.sendMessage(prefix + "The player " + name + " does not appear to be online.");
                        return true;
                    }
                    // TODO: Make it so the player doesn't have to be online
                    cm.kickPlayer(player, kicked);
                    return true;
                }
                if(args[1].equalsIgnoreCase("leave")){
                    cm.leaveClan(player);
                    return true;
                }
                // TODO: Toggle on/off permanently
                // TODO: Check members of someone else's clan for 10 seconds
                if(args[1].equalsIgnoreCase("list")){
                    if(!cm.playerHasClan(player)){
                        player.sendMessage(prefix + "You're not in a clan.");
                        return true;
                    }
                    cm.createClanListScoreboard(player);
                    player.sendMessage(prefix + "Scoreboard will disappear in 10 seconds.");
                    return true;
                }
            }

            /* LOOT DROP STUFF */
            if(args[0].equalsIgnoreCase("lootdrop")){
                LootdropManager lm = LootdropManager.getInstance();
                if(!player.hasPermission("oinktowny.lootdrop")) {
                    player.sendMessage(prefix + "This is an admin-only command!");
                    return true;
                }
                if(args.length < 2){
                    player.sendMessage(prefix + "Please specify drop location. " + ChatColor.GOLD + "/ot lootdrop [random/here]");
                    return true;
                }
                if(args[1].equalsIgnoreCase("here")){
                    lm.dropLootChestPredictably(player);
                    return true;
                }
                if(args[1].equalsIgnoreCase("random")){
                    Location loc = lm.dropLootChestRandom();
                    player.sendMessage(prefix + "Dropping loot at random location...");
                    player.sendMessage(prefix + ChatColor.RED + ChatColor.BOLD + "ALPHA TEST: " + ChatColor.GOLD + "Teleporting you to that location...");
                    player.teleport(loc);
                    return true;
                }
                else {
                    player.sendMessage(prefix + "Argument for lootdrop location not recognized. " + ChatColor.GOLD + "/ot lootdrop [random/here]");
                    return true;
                }
            }

            RegionsManager regM = RegionsManager.getInstance();
            /* REGION CLAIMING STUFF */
            if(args[0].equalsIgnoreCase("claim")){
                regM.claimChunk(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("unclaim")){
                regM.unclaimChunk(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("unclaimall")){
                if(args.length == 1) {
                    player.sendMessage(prefix + ChatColor.RED + ChatColor.BOLD + "This command will unclaim all of your towny region claims. " +
                            ChatColor.GRAY + ChatColor.ITALIC + "To confirm this action, type " + ChatColor.YELLOW + ChatColor.ITALIC + "/ot unclaimall confirm");
                    return true;
                } else if (args.length == 2){
                    regM.unclaimAllChunks(player);
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("claimlist")){
                regM.listClaims(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("claimmap")){
                player.sendMessage(prefix + "Bug Mobkinz78 to get this working! :)");
                return true;
            }
            if(args[0].equalsIgnoreCase("adminclaim")){
                if(!player.hasPermission("oinktowny.claims.admin")) {
                    player.sendMessage(prefix + ChatColor.RED + ChatColor.BOLD + "Sorry!" + ChatColor.GRAY + " This is an admin only command.");
                    return true;
                }
                regM.addAdminRegionClaim(player, player.getLocation().getChunk());
                return true;
            }
            if(args[0].equalsIgnoreCase("bypassclaims")){
                if(!player.hasPermission("oinktowny.claims.admin")) {
                    player.sendMessage(prefix + ChatColor.RED + ChatColor.BOLD + "Sorry!" + ChatColor.GRAY + " This is an admin only command.");
                    return true;
                }
                regM.enableBypassForAdmin(player);
                return true;
            }

            /* ARENAS STUFF */
            if(args[0].equalsIgnoreCase("arena")){
                ArenaPVPManager apm = ArenaPVPManager.getInstance();
                if(!player.hasPermission("oinktowny.create")){
                    player.sendMessage(prefix + "Sorry, this is an admin-only command.");
                    return true;
                }
                if(!player.getWorld().getName().equalsIgnoreCase(Main.getInstance().getWorldName())){
                    player.sendMessage(prefix + "Arenas can not be created in this world.");
                    return true;
                }
                /* Command structure: /oinktowny arena ... */
                if(args.length == 1){
                    player.sendMessage(prefix + ChatColor.GOLD + ChatColor.BOLD + "-+OinkTowny Arena Commands+-");
                    player.sendMessage(prefix + ChatColor.GOLD + "/oinktowny arena ...");
                    player.sendMessage(ChatColor.GOLD + "- create <name>" + ChatColor.DARK_AQUA + " - Create a new arena");
                    player.sendMessage(ChatColor.GOLD + "- quit" + ChatColor.DARK_AQUA + " - Quit arena creation");
                    player.sendMessage(ChatColor.GOLD + "- list" + ChatColor.DARK_AQUA + " - See list of existing arenas");
                    player.sendMessage(ChatColor.GOLD + "- remove <name>" + ChatColor.DARK_AQUA + " - Remove an existing arena");
                    player.sendMessage(ChatColor.GOLD + "- leave" + ChatColor.DARK_AQUA + " - Leave the arena you are in");
                    player.sendMessage(ChatColor.GOLD + "- teleport <name> " + ChatColor.DARK_AQUA + " - Teleport to an arena");
                    return true;
                }
                if(args[1].equalsIgnoreCase("create")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Please enter an arena name!");
                        return true;
                    }
                    apm.initiateArenaCreation(player, args[2]);
                    return true;
                }
                if(args[1].equalsIgnoreCase("quit")){
                    if(!apm.isCreating(player)){
                        player.sendMessage(prefix + "You aren't in arena creation.");
                        return true;
                    }
                    apm.quitCreation(player);
                    return true;
                }
                if(args[1].equalsIgnoreCase("leave")){ // Relatively safe to assume they are the only ones in
                    if(!apm.isPlayerInArena(player)) {
                        player.sendMessage(prefix + "You need to be in an arena to leave one...");
                        return true;
                    }
                    apm.prematurelyEndArena(player, "You have left the arena!");
                    return true;

                }
                if(args[1].equalsIgnoreCase("list")){
                    player.sendMessage(prefix + "Existing arenas: " + apm.getExistingArenas().toString());
                    return true;
                }
                if(args[1].equalsIgnoreCase("teleport")){
                    if(args[2] == null){
                        player.sendMessage(prefix + "Please provide an arena name!");
                        return true;
                    }
                    apm.teleportPlayer(player, args[2]);
                    return true;
                }
                if(args[1].equalsIgnoreCase("remove")){
                    player.sendMessage(prefix + "not working yet, sorry");
                    // TODO: Arena removal method
                    return true;
                }
                else {
                    player.sendMessage(prefix + "Unrecognized argument! \n" + prefix + "To see usages, type " + ChatColor.GOLD + "/ot arena");
                    return true;
                }
            }

            /* ENCHANTMENT STUFF
            if(args[0].equalsIgnoreCase("enchant")){
                EnchantmentManager em = EnchantmentManager.getInstance();
                if(args.length < 3) {
                    player.sendMessage(prefix + "You need 3 args");
                    return true;
                }
                if(args[1].equalsIgnoreCase("jump")){
                    em.enchantItem(player, EnchantmentManager.JUMP_BOOST, Integer.parseInt(args[2]));
                }
                if(args[1].equalsIgnoreCase("glow")){
                    em.enchantItem(player, EnchantmentManager.GLOW_STRIKE, Integer.parseInt(args[2]));
                }
                if(args[1].equalsIgnoreCase("convert")){
                    em.enchantItem(player, EnchantmentManager.CONVERSION, Integer.parseInt(args[2]));
                }
                if(args[1].equalsIgnoreCase("explode")){
                    em.enchantItem(player, EnchantmentManager.EXPLOSIVE_ARROWS, Integer.parseInt(args[2]));
                }
                if(args[1].equalsIgnoreCase("dogmaster")){
                    em.enchantItem(player, EnchantmentManager.DOG_MASTER, Integer.parseInt(args[2]));
                }
                if(args[1].equalsIgnoreCase("necromancer")){
                    em.enchantItem(player, EnchantmentManager.NECROMANCER, Integer.parseInt(args[2]));
                }
                if(args[1].equalsIgnoreCase("deflect")){
                    em.enchantItem(player, EnchantmentManager.DEFLECT, Integer.parseInt(args[2]));
                }
                return true;
            } */

            /* ARTIFACT STUFF
            if(args[0].equalsIgnoreCase("artifact")){
                ArtifactManager am = ArtifactManager.getInstance();
                if(args.length == 1){
                    player.sendMessage(prefix + "Add the name of an artifact.");
                    return true;
                }
                if(args[1].equalsIgnoreCase("jackhammer")){
                    player.getInventory().setItem(0, am.createJackhammer());
                }
                if(args[1].equalsIgnoreCase("gravityshift")){
                    player.getInventory().setItem(0, am.createGravityShifter());
                }
                if(args[1].equalsIgnoreCase("healthshift")){
                    player.getInventory().setItem(0, am.createHealthShifter());
                }
                if(args[1].equalsIgnoreCase("explode")){
                    player.getInventory().setItem(0, am.createDestructoid());
                }
                if(args[1].equalsIgnoreCase("headlamp")){
                    player.getInventory().setItem(0, am.createHeadlamp());
                }
                if(args[1].equalsIgnoreCase("telepoof")){
                    player.getInventory().setItem(0, am.createTelepoof());
                }
                if(args[1].equalsIgnoreCase("luckyhoe")){
                    player.getInventory().setItem(0, am.createLuckyHoe());
                }
                player.sendMessage(prefix + "Here ya go, mate...");
                return true;
            } */

            /* RUINS STUFF */
            if(args[0].equalsIgnoreCase("ruins")){
                RuinsManager rm = RuinsManager.getInstance();
                if(!player.hasPermission("oinktowny.ruins")){
                    player.sendMessage(prefix + "Sorry, this is an admin-only command.");
                    return true;
                }
                if(args.length <= 1){
                    player.sendMessage(prefix + "Use " + ChatColor.GOLD + "/ot ruins create <name>");
                    player.sendMessage(prefix + "To delete ruins: " + ChatColor.GOLD + "/ot ruins destroy <name>");
                    return true;
                }
                if(args[1].equalsIgnoreCase("create")){
                    if(player.getWorld().getName().equalsIgnoreCase(Main.getInstance().getWorldName())){
                        player.sendMessage(prefix + "Ruins can not be created in this world.");
                        return true;
                    }
                    if(args.length == 2){
                        player.sendMessage(prefix + ChatColor.RED + "Please provide a name for the ruins!");
                        return true;
                    }
                    player.sendMessage(prefix + "Now entering ruins creation mode...");
                    rm.initiateRuinsCreation(player, args[2]);
                    return true;
                }
                if(args[1].equalsIgnoreCase("destroy")){
                    if(args.length == 2){
                        player.sendMessage(prefix + "Provide the name of the ruins you'd like to delete." + ChatColor.DARK_RED + ChatColor.BOLD + "[THIS IS PERMANENT]");
                        return true;
                    }
                    player.sendMessage(prefix + "Destroying the " + args[2] + " ruins...");
                    rm.destroyRuins(args[2], player);
                    return true;
                }
            }

            if(args[0].equalsIgnoreCase("reload")){
                if(player.hasPermission("oinktowny.admin")) {
                    player.sendMessage(prefix + "Reloading all configuration files...");
                    Main.getInstance().reloadAllConfigurations();
                    player.sendMessage(prefix + "All configuration files reloaded!");
                    return true;
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Sorry, but that's an admin only command!");
                    return true;
                }
            }

            /* DEFAULT MESSAGE */
            player.sendMessage(prefix + ChatColor.RED + "Argument not recognized! Please see " + ChatColor.GOLD + "/ot help");
            return true;
        }
        return true;
    }

}
