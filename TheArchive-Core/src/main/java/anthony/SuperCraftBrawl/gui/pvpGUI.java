package anthony.SuperCraftBrawl.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class pvpGUI implements InventoryProvider{
	
	public Main main;
	public SmartInventory inv;
	
	public pvpGUI(Main main) {
		inv = SmartInventory.builder()
	            .id("myInventory")
	            .provider(this)
	            .size(3, 9)
	            .title("" + ChatColor.BLACK + ChatColor.BOLD + "KIT SELECTOR")
	            .build();
		this.main = main;

	}
	


	@Override
	public void init(Player player, InventoryContents contents) {
		contents.set(1, 1, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.IRON_SWORD), "" + ChatColor.RESET + ChatColor.GREEN + "Warrior", "" + ChatColor.GRAY + "Click to select Kit Warrior!"), e->{ 
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected the Warrior Kit");
			inv.close(player);
			main.SendPlayerToDuel(player);
			player.getInventory().setItem(0, ItemHelper.addEnchant(new ItemStack(Material.IRON_SWORD), Enchantment.DAMAGE_ALL, 3));
			player.getInventory().setItem(1, ItemHelper.setDetails(new ItemStack(Material.GOLDEN_APPLE, 16), "" + ChatColor.GOLD + ChatColor.BOLD + "Golden Apple"));
			player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
		}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
	}
}
