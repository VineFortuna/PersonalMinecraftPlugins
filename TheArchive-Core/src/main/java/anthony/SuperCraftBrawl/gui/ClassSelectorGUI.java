package anthony.SuperCraftBrawl.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class ClassSelectorGUI implements InventoryProvider{
	
	public Main main;
	public SmartInventory inv;
	
	public ClassSelectorGUI(Main main) {
		inv = SmartInventory.builder()
	            .id("myInventory")
	            .provider(this)
	            .size(3, 9)
	            .title("" + ChatColor.YELLOW + ChatColor.BOLD + "CLASS SELECTOR")
	            .build();
		this.main = main;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.set(1, 2, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.DIAMOND), "" + ChatColor.YELLOW + ChatColor.BOLD + "DONOR CLASSES", "" + ChatColor.GRAY + "Purchase a rank to access Donor Classes"), e->{ 
			inv.close(player);
			new DonorClassesGUI(main).inv.open(player);
		}));
			
			contents.set(1, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.ENCHANTED_BOOK), "" + ChatColor.YELLOW + ChatColor.BOLD + "FREE CLASSES", "" + ChatColor.GRAY + "All the free classes!"), e->{
				inv.close(player);
				new InventoryGUI(main).inv.open(player);
			}));
				
				contents.set(1, 6, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.EMERALD), "" + ChatColor.YELLOW + ChatColor.BOLD + "TOKEN CLASSES", "" + ChatColor.GRAY + "You can buy these classes with coins!"), e->{
					inv.close(player);
					new TokenClassesGUI(main).inv.open(player);
				}));
				contents.set(2, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.NETHER_STAR), "" + ChatColor.BLUE + ChatColor.BOLD + "LEVEL CLASSES", "" + ChatColor.GRAY + "Classes that can be unlocked with Levels"), e->{
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "Not implemented yet!");
					inv.close(player);
				}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
	}
}
