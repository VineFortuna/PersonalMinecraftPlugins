package anthony.SuperCraftBrawl.gui;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class InventoryGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;

	public InventoryGUI(Main main) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "FREE CLASSES").build();
		this.main = main;

	}

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.set(0, 0,
				ClickableItem.of(
						ItemHelper.setDetails(new ItemStack(Material.SKULL_ITEM), "" + ChatColor.ITALIC + "Skeleton",
								"" + ChatColor.GRAY + "A long range shooter effective at taking down their targets"),
						e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Skeleton);
							;
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
									+ "==============================================");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
									+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: "
									+ ClassType.Skeleton.getTag());
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
									+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET
									+ ChatColor.YELLOW + ClassType.Skeleton.getClassDesc());
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
									+ "==============================================");
							inv.close(player);
						}));

		contents.set(0, 1, ClickableItem.of(
				ItemHelper.setDetails(new ItemStack(Material.ENDER_PEARL), "" + ChatColor.ITALIC + "Enderman",
						"" + ChatColor.GRAY + "Stare into the souls of your enemies whilst teleporting around them"),
				e -> {
					main.getGameManager().playerSelectClass(player, ClassType.Enderman);
					;
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: " + ClassType.Enderman.getTag());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET + ChatColor.YELLOW
							+ ClassType.Enderman.getClassDesc());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					inv.close(player);
				}));
		contents.set(0, 2,
				ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.CACTUS), "" + ChatColor.ITALIC + "Cactus",
						"" + ChatColor.GRAY + "A pricklyyy living thing, made up of thornws & blood.."), e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Cactus);
							;
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
									+ "==============================================");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
									+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: "
									+ ClassType.Cactus.getTag());
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
									+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET
									+ ChatColor.YELLOW + ClassType.Cactus.getClassDesc());
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
									+ "==============================================");
							inv.close(player);
						}));
		contents.set(0, 3,
				ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.LEASH), "" + ChatColor.ITALIC + "Horse",
						"" + ChatColor.GRAY + "Nayyy!! Different effects = different powers!"), e -> {
							main.getGameManager().playerSelectClass(player, ClassType.Horse);
							;
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
									+ "==============================================");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
									+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: "
									+ ClassType.Horse.getTag());
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
									+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET
									+ ChatColor.YELLOW + ClassType.Horse.getClassDesc());
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
									+ "==============================================");
							inv.close(player);
						}));
		contents.set(0, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.INK_SACK),
				"" + ChatColor.ITALIC + "Squid", "" + ChatColor.GRAY + "UNDA DA SEA! UNDA DA SEA!"), e -> {
					main.getGameManager().playerSelectClass(player, ClassType.Squid);
					;
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: " + ClassType.Squid.getTag());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET + ChatColor.YELLOW
							+ ClassType.Squid.getClassDesc());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					inv.close(player);
				}));
		contents.set(0, 5,
				ClickableItem
						.of(ItemHelper.setDetails(new ItemStack(Material.SPIDER_EYE), "" + ChatColor.ITALIC + "Spider",
								"" + ChatColor.GRAY + "Bite and poison your enemies while fighting them!"), e -> {
									main.getGameManager().playerSelectClass(player, ClassType.Spider);
									;
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
											+ "==============================================");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| "
											+ ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: "
											+ ClassType.Spider.getTag());
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| "
											+ ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: "
											+ ChatColor.RESET + ChatColor.YELLOW + ClassType.Spider.getClassDesc());
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
											+ "==============================================");
									inv.close(player);
								}));
		contents.set(0, 6,
				ClickableItem
						.of(ItemHelper.setDetails(new ItemStack(Material.PUMPKIN), "" + ChatColor.ITALIC + "SnowMan",
								"" + ChatColor.GRAY + "This is a SnowMan, not a SnowGolem. Get it right pleb!"), e -> {
									main.getGameManager().playerSelectClass(player, ClassType.Snowman);
									;
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
											+ "==============================================");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| "
											+ ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: "
											+ ClassType.Snowman.getTag());
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| "
											+ ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: "
											+ ChatColor.RESET + ChatColor.YELLOW + ClassType.Snowman.getClassDesc());
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
									player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
											+ "==============================================");
									inv.close(player);
								}));
		contents.set(0, 7, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.LEATHER_CHESTPLATE),
				"" + ChatColor.ITALIC + "Fluxty", "" + ChatColor.GRAY + "We cannot have HATERS in the community..",
				"" + ChatColor.GRAY + "So use your Wood Axe to kick em all out!"), e -> {
					main.getGameManager().playerSelectClass(player, ClassType.Fluxty);
					;
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: " + ClassType.Fluxty.getTag());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET + ChatColor.YELLOW
							+ ClassType.Fluxty.getClassDesc());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					inv.close(player);
				}));
		contents.set(0, 8, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.PORK),
				"" + ChatColor.ITALIC + "Pig", "" + ChatColor.GRAY + "Gain some speed when you get hit"), e -> {
					main.getGameManager().playerSelectClass(player, ClassType.Pig);
					;
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: " + ClassType.Pig.getTag());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET + ChatColor.YELLOW
							+ ClassType.Pig.getClassDesc());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					inv.close(player);
				}));
		contents.set(1, 0, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.BLAZE_ROD),
				"" + ChatColor.ITALIC + "Blaze", "" + ChatColor.GRAY + "ITS A BLAZE LOL!"), e -> {
					main.getGameManager().playerSelectClass(player, ClassType.Blaze);
					;
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: " + ClassType.Blaze.getTag());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET + ChatColor.YELLOW
							+ ClassType.Blaze.getClassDesc());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					inv.close(player);
				}));
		contents.set(1, 1, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.NETHER_STAR),
				"" + ChatColor.ITALIC + "Wither", "" + ChatColor.GRAY + "Utilize your explosive skulls to defeat your enemies!"), e -> {
					main.getGameManager().playerSelectClass(player, ClassType.Wither);
					;
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: " + ClassType.Wither.getTag());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET + ChatColor.YELLOW
							+ ClassType.Wither.getClassDesc());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					inv.close(player);
				}));
		ItemStack creeperHelmet = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.CREEPER.ordinal());
		contents.set(1, 2, ClickableItem.of(ItemHelper.setDetails(creeperHelmet,
				"" + ChatColor.ITALIC + "Creeper", "" + ChatColor.GRAY + "Defeat your opponents with your explosive arsenal"), e -> {
					main.getGameManager().playerSelectClass(player, ClassType.Creeper);
					;
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Selected Class: " + ClassType.Creeper.getTag());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| " + ChatColor.RESET
							+ ChatColor.YELLOW + ChatColor.BOLD + "Class Desc: " + ChatColor.RESET + ChatColor.YELLOW
							+ ClassType.Creeper.getClassDesc());
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
					player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
							+ "==============================================");
					inv.close(player);
				}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}

}
