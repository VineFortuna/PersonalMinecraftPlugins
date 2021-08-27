package anthony.SuperCraftBrawl.gui;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class QuitGUI implements InventoryProvider {

	public Main main;
	public SmartInventory invQuit;
	public GameInstance instance;

	public GameInstance getGameInstance() {
		return instance;

	}

	public QuitGUI(Main main) {
		invQuit = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.RED + ChatColor.BOLD + "LEAVE GAME").build();
		this.main = main;

	}

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.set(1, 2, ClickableItem.of(
				ItemHelper.setDetails(new ItemStack(Material.EMERALD_BLOCK), "" + ChatColor.YELLOW + "Confirm"), e -> {
					PlayerData data = main.getDataManager().getPlayerData(player);
					GameInstance instance = main.getGameManager().GetInstanceOfPlayer(player);
					if (data.votes == 1) {
						if (instance != null) {
							instance.totalVotes--;
							data.votes = 0;
						}
					}	
					main.getGameManager().RemovePlayerFromAll(player);
					main.ResetPlayer(player);
					main.getGameManager().RemovePlayerFromMap(player, null, player);
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have left your game");

					invQuit.close(player);
					main.LobbyBoard(player);
					main.LobbyItems(player);
					player.setGameMode(GameMode.ADVENTURE);
					player.setHealth(20);
					player.setFoodLevel(20);
				}));

		contents.set(1, 6, ClickableItem.of(
				ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK), "" + ChatColor.GRAY + "Cancel"), e -> {
					invQuit.close(player);
				}));

		contents.set(0, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.BEACON),
				"" + ChatColor.WHITE + "Are you sure you want to leave?"), e -> {

				}));

		/*
		 * contents.set(0, 1, ClickableItem.of(new ItemStack (Material.STICK), e->{
		 * main.getGameManager().playerSelectClass(player, ClassType.Ninja);;
		 * player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN +
		 * "You have selected " + ChatColor.RESET + ChatColor.BLACK + ChatColor.BOLD +
		 * "Ninja"); invQuit.close(player); }));
		 */
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
