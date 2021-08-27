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

public class HubGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;

	public HubGUI(Main main) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "GAME SELECTOR").build();
		this.main = main;

	}

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.set(1, 3,
				ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.NETHER_STAR),
						"" + ChatColor.RESET + ChatColor.GREEN + "SuperCraftBlocks",
						"" + ChatColor.GRAY + "Choose a class, fight, & ", ChatColor.GRAY + "claim the #1 spot!"),
						e -> {
							inv.close(player);
							new ChooseGameGUI(main).inv.open(player);
						}));
		contents.set(1, 5,
				ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.BEACON),
						"" + ChatColor.RESET + ChatColor.GREEN + "Super Minezone Maker",
						"" + ChatColor.GRAY + "Coming Soon..."), e -> {
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW + "Coming Soon...");
							player.setAllowFlight(true);
							inv.close(player);
						}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
