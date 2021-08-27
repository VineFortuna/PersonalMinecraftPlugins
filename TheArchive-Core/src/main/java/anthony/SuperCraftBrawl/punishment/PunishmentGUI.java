package anthony.SuperCraftBrawl.punishment;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class PunishmentGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;
	private Player target;
	private int time;
	private String banReason;

	public PunishmentGUI(Main main, Player target) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "Punishments").build();
		this.main = main;
		this.target = target;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		// Mute Player
		contents.set(1, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.PAPER), "" + ChatColor.YELLOW
				+ ChatColor.BOLD + "Mute for Permanent " + ChatColor.RESET + target.getName() + "?"), e -> {
					inv.close(player);
					PlayerData data = main.getDataManager().getPlayerData(target);

					if (data.muted == 0) {
						main.getPunishment().setMuted(target);
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have muted "
								+ ChatColor.YELLOW + target.getName() + ChatColor.RESET + " for " + ChatColor.YELLOW
								+ "Permanent " + ChatColor.RESET + "for the reason: " + ChatColor.YELLOW
								+ main.getPunishment().getMutedReason("No reason yet"));
					} else {
						player.sendMessage(
								"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This player is already muted!");
					}
				}));
		// Unmute Player
		contents.set(2, 4,
				ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.PAPER),
						"" + ChatColor.YELLOW + ChatColor.BOLD + "Unmute " + ChatColor.RESET + target.getName() + "?"),
						e -> {
							inv.close(player);
							PlayerData data = main.getDataManager().getPlayerData(target);

							if (data.muted == 1) {
								main.getPunishment().setUnmuted(target);
								player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have unmuted "
										+ ChatColor.YELLOW + target.getName());
							} else {
								player.sendMessage(
										"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This player is not muted!");
							}
						}));
		// Kick Player
		contents.set(1, 6, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.NAME_TAG),
				"" + ChatColor.YELLOW + "Kick " + ChatColor.RESET + target.getName() + "?"), e -> {
					inv.close(player);
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have kicked "
							+ ChatColor.YELLOW + target.getName());
					target.kickPlayer("You have been kicked by " + ChatColor.YELLOW + player.getName());
				}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
