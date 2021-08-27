package anthony.SuperCraftBrawl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.Game.classes.ClassType;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class DonorClassesGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;

	public DonorClassesGUI(Main main) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "DONOR CLASSES").build();
		this.main = main;

	}

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.set(0, 0,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.IRON_AXE), "" + ChatColor.ITALIC + "Irongolem",
								"" + ChatColor.GRAY + "Smack your enemies into the air while defending your village!",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.WHITE + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.IronGolem);
							;
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.RESET + ChatColor.GRAY + ChatColor.BOLD + ChatColor.ITALIC
									+ "IronGolem");
							inv.close(player);
						}));
		contents.set(0, 1,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.GHAST_TEAR), "Ghast",
								"" + ChatColor.GRAY + "Burn down your enemies with your sorrows",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.WHITE + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Ghast);
							;
							ItemHelper.setDetails(new ItemStack(Material.GHAST_TEAR), "" + ChatColor.ITALIC + "Ghast",
									"" + ChatColor.GRAY + "Burn down your enemies with your sorrows");
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.WHITE + "Ghast");
							inv.close(player);
						}));
		contents.set(0, 2,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.SLIME_BALL), "Slime",
								"" + ChatColor.GRAY + "Throw sticky grenades and attack enemies!",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.WHITE + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Slime);
							;
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.GREEN + ChatColor.BOLD + ChatColor.ITALIC + "Slime");
							inv.close(player);
						}));
		contents.set(0, 3, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.GOLD_AXE), "ButterGolem", ""
				+ ChatColor.GRAY
				+ "Once a proud member of the Sky Army, the ButterGolem now stands as a relic of a bygone era..",
				"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.RESET + "+ exclusive!"), e -> {
					main.getGameManager().playerSelectClass(player, ClassType.ButterGolem);
					;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
							+ ChatColor.YELLOW + ChatColor.BOLD + ChatColor.ITALIC + "ButterGolem");
					inv.close(player);
				}));
		contents.set(0, 4,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.DRAGON_EGG), "EnderDragon",
								"" + ChatColor.GRAY + "Jump higher than your opponents and teleport around!",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.RESET + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Enderdragon);
							;
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.DARK_PURPLE + ChatColor.BOLD + "Ender" + ChatColor.RESET
									+ ChatColor.BLACK + ChatColor.BOLD + "Dragon");
							inv.close(player);
						}));
		contents.set(0, 5,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.SHEARS), "Bat",
								"" + ChatColor.GRAY + "Dance around your opponents with SUPER high jumps!",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.RESET + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Bat);
							;
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.DARK_GRAY + ChatColor.BOLD + "Bat");
							inv.close(player);
						}));
		contents.set(0, 6,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK), "SethBling",
								"" + ChatColor.GRAY + "The creator of SCB, wanna fight?!?!",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.RESET + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.SethBling);
							;
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.RED + ChatColor.BOLD + ChatColor.ITALIC + "SethBling");
							inv.close(player);
						}));
		contents.set(0, 7,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.MELON), "Melon",
								"" + ChatColor.GRAY + "The Owner of the server in the game?!",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.RESET + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Melon);
							;
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.YELLOW + "Melon");
							inv.close(player);
						}));
		contents.set(0, 8,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.RED_MUSHROOM), "BabyCow",
								"" + ChatColor.GRAY + "moo.. MOO!!",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.RESET + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.BabyCow);
							;
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.RED + ChatColor.ITALIC + ChatColor.BOLD + "BabyCow");
							inv.close(player);
						}));
		contents.set(1, 0,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.DIAMOND), "Herobrine",
								"" + ChatColor.GRAY + "Use your Diamond of Despair to",
								"" + ChatColor.GRAY + "play tricks on your opponents!",
								"" + ChatColor.YELLOW + ChatColor.BOLD + "VIP" + ChatColor.RESET + "+ exclusive!"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Herobrine);
							;
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected "
									+ ChatColor.GRAY + ChatColor.BOLD + "Herobrine");
							inv.close(player);
						}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
