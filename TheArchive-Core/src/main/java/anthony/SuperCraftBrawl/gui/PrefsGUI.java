package anthony.SuperCraftBrawl.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import anthony.SuperCraftBrawl.ranks.RankManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class PrefsGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;
	private RankManager rm;

	public PrefsGUI(Main main) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "PREFERENCES").build();
		this.main = main;
	}

	public RankManager getRankManager() {
		return rm;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		PlayerData data = main.getDataManager().getPlayerData(player);
		ItemStack wheat = ItemHelper.setDetails(new ItemStack(Material.WHEAT, 1),
				"" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "Magic Broom");

		contents.set(1, 3, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.DIAMOND),
				"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Toggle Custom Win Messages"), e -> {
					if (player.hasPermission("scb.customWin")) {
						if (data.cwm == 1) {
							player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have disabled " + ChatColor.YELLOW + "Custom Win Messages");
							data.cwm = 0;
						} else {
							data.cwm = 1;
							player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have enabled " + ChatColor.YELLOW + "Custom Win Messages");
						}
					} else {
						player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You need a rank in order to use this!");
					}
					inv.close(player);
				}));
		contents.set(1, 5, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.PAPER),
				"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Toggle Private Messages"), e -> {
						if (data.pm == 0) {
							player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have disabled " + ChatColor.YELLOW + "Private Messages");
							data.pm = 1;
						} else {
							data.pm = 0;
							player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have enabled " + ChatColor.YELLOW + "Private Messages");
						}
					inv.close(player);
				}));
		/*contents.set(1, 5,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
								"" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + "Disable Custom Win Messages"),
						e -> {
							if (player.hasPermission("scb.customWin")) {
								if (data.cwm == 1) {
									data.cwm = 0;
									player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
											+ "You have disabled " + ChatColor.YELLOW + "Custom Win Messages");
								} else {
									player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
											+ "You already have this feature disabled!");
								}
							} else {
								player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
										+ "You need a rank in order to use this!");
							}
							inv.close(player);
						}));*/
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
