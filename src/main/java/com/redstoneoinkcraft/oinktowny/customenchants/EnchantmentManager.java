package com.redstoneoinkcraft.oinktowny.customenchants;

import com.redstoneoinkcraft.oinktowny.customenchants.enchants.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class EnchantmentManager {

    private static EnchantmentManager instance = new EnchantmentManager();

    private EnchantmentManager() {}

    public static EnchantmentManager getInstance() {
        return instance;
    }

    public static String prefix = "" + ChatColor.BLACK + ChatColor.BOLD + "(" + ChatColor.DARK_GREEN + "TownyEnchant" + ChatColor.BLACK + ChatColor.BOLD + ")" + ChatColor.GRAY + " ";

    // All available enchantments stored for use within the plugin
    private static ArrayList<EnchantmentFramework> allCustomEnchantments = new ArrayList<>(8);
    public ArrayList<EnchantmentFramework> getAllCustomEnchantments(){
        return allCustomEnchantments;
    }

    /* Constants
     * Note: All constant names are the namespace passed in in the class.
     */
    public static final EnchantmentFramework ARTIFACT = new EnchantArtifact();

    public static final EnchantmentFramework JUMP_BOOST = new EnchantJumpBoost();
    public static final EnchantmentFramework GLOW_STRIKE = new EnchantGlowEntity();
    public static final EnchantmentFramework CONVERSION = new EnchantDmgConversion(); // Converts damage to health
    public static final EnchantmentFramework EXPLOSIVE_ARROWS = new EnchantExplosiveArrow();
    public static final EnchantmentFramework DEFLECT = new EnchantDeflect();
    public static final EnchantmentFramework DOG_MASTER = new EnchantDogMaster();
    public static final EnchantmentFramework NECROMANCER = new EnchantNecromancer();
    public static final EnchantmentFramework RUST = new EnchantRust();

    public static void registerEnchants(){
        registerCustomEnchantment(ARTIFACT);

        registerCustomEnchantment(JUMP_BOOST);
        registerCustomEnchantment(GLOW_STRIKE);
        registerCustomEnchantment(CONVERSION);
        registerCustomEnchantment(EXPLOSIVE_ARROWS);
        registerCustomEnchantment(DEFLECT);
        registerCustomEnchantment(DOG_MASTER);
        registerCustomEnchantment(NECROMANCER);
        registerCustomEnchantment(RUST);
        Bukkit.getLogger().log(Level.INFO, "[OinkTowny] All enchantments have been loaded!");
    }
    /* Register and inject all enchantments to the server, the return is the result of the registration */
    public static void registerCustomEnchantment(EnchantmentFramework enchantment){
        try {
            // Allow registration using Reflection API
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);

            // Actual enchantment registration
            Enchantment.registerEnchantment(enchantment);

            // Go ahead and throw it in the arraylist
            allCustomEnchantments.add(enchantment);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            Bukkit.getLogger().log(Level.WARNING, "[OinKTowny] Enchants: Field value failure! Did you reload the server? If so, you should be fine.");
        } catch (IllegalStateException e) {
            Bukkit.getLogger().log(Level.WARNING, "[OinKTowny] Enchants: Not accepting new values!");
        }
    }

    /* Helper method to add enchantment to the lore */
    public void addCustomEnchantmentLore(ItemStack item, ItemMeta meta, String enchantmentName, int level){
        String loreAddition = "" + ChatColor.YELLOW + ChatColor.ITALIC + enchantmentName + " - Level " + level;
        List<String> lore = meta.getLore();
        if(lore == null){
            lore = new ArrayList<>(1);
        }
        lore.add(loreAddition);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /* To add an enchantment on to an item */
    public void addEnchantmentToItem(ItemStack itemToEnchant, EnchantmentFramework enchantment, int level){
        if(enchantment.canEnchantItem(itemToEnchant)){
            ItemMeta itemMeta = itemToEnchant.getItemMeta();
            assert itemMeta != null;
            if(itemMeta.getEnchants().containsKey(enchantment)){
                itemMeta.removeEnchant(enchantment);
            }
            itemMeta.addEnchant(enchantment, level, true);
            itemToEnchant.setItemMeta(itemMeta);
            addCustomEnchantmentLore(itemToEnchant, itemMeta, enchantment.getCustomName(), level);
        }
    }

    /* Get an Enchantment by name, if possible */
    public EnchantmentFramework getEnchantmentByName(String name){
        for(EnchantmentFramework enchant : allCustomEnchantments){
            if(enchant.getCustomName().toLowerCase().equalsIgnoreCase(name)){
                return enchant;
            }
        }

        return null;
    }

    private HashMap<Player, Inventory> activePlayerCustomEnchantInventories = new HashMap<>(2);
    public HashMap<Player, Inventory> getActivePlayerCustomEnchantInventories(){
        return activePlayerCustomEnchantInventories;
    }

    /* Open the custom enchantment GUI */
    public void openCustomEnchantmentTable(Player player, EnchantmentFramework enchant, ItemStack itemToEnchant, int level){
        if(!enchant.canEnchantItem(itemToEnchant)){
            player.sendMessage(prefix + ChatColor.RED + ChatColor.ITALIC + "Sorry!" + ChatColor.GRAY + " That enchantment can not be applied to this item.");
            return;
        }
        if(itemToEnchant.getEnchantments().containsKey(enchant)){
            player.sendMessage(prefix + ChatColor.RED + ChatColor.ITALIC + "Sorry!" + ChatColor.GRAY + " That item already has that enchantment.");
            return;
        }

        // Inventory setup
        Inventory inv = Bukkit.createInventory(null, InventoryType.WORKBENCH);

        // Top row: Left will be cancel, middle will be current item, right will be confirm
        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName("" + ChatColor.DARK_RED + ChatColor.BOLD + "CANCEL");
        cancel.setItemMeta(cancelMeta);
        inv.setItem(1, cancel);

        ItemStack item = itemToEnchant;
        inv.setItem(2, item);

        ItemStack confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "CONFIRM");
        confirm.setItemMeta(confirmMeta);
        inv.setItem(3, confirm);

        // Middle and bottom row (4 through 8): Available enchantment levels (5 max anyway)
        for (int i = 1; i <= enchant.getMaxLevel(); i++){
            inv.setItem(i + 3, makeEnchantBook(enchant, i, enchant.getCost(i)));
        }

        // Bottom right - Paper with instructions
        ItemStack instructions = new ItemStack(Material.PAPER, 1);
        ItemMeta instructionsMeta = instructions.getItemMeta();
        instructionsMeta.setDisplayName("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "INSTRUCTIONS");
        instructionsMeta.setLore(Arrays.asList("Select the desired enchantment level.", "Your item will update on the right side of the inventory.", "Make sure you have enough levels!"));
        instructions.setItemMeta(instructionsMeta);
        inv.setItem(9, instructions);

        player.openInventory(inv);
        activePlayerCustomEnchantInventories.put(player, inv);

        // The rest is handled in CustomEnchantInvListener -- Enchantment is just added to original item, stored in the top middle, each time a player updates it.
    }

    // Make a book for the enchantment inventory
    public ItemStack makeEnchantBook(EnchantmentFramework enchant, int level, int cost){
        String enchantmentName = enchant.getCustomName();
        ItemStack book = new ItemStack(Material.BOOK, 1);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName("" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "k" + ChatColor.BOLD + ChatColor.GOLD + enchantmentName + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "k");
        bookMeta.setLore(Arrays.asList("Level: " + level, "" + ChatColor.GREEN + cost + " Levels"));
        book.setItemMeta(bookMeta);

        return book;
    }

    /* Enchantment specific methods */

    // Calculate odds for the conversion enchantment
    public boolean calculateConversion(int level){
        Random rand = new Random();
        int odds = rand.nextInt(100);
        if(level == 1){
            return odds < 20;
        }
        if(level == 2){
            return odds < 30;
        }
        if(level == 3){
            return odds < 40;
        }
        return false;
    }
}
