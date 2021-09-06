package anthony.SuperCraftBrawl.gui;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class MysteryChestsGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;

	public MysteryChestsGUI(Main main) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "Mystery Chest").build();
		this.main = main;
	}

	public boolean isInBounds(Location loc) {
		Vector v = new Vector(-16.506, 140, 41.448);
		Location centre = new Location(main.getLobbyWorld(), v.getX(), v.getY(), v.getZ());
		double boundsX = 3, boundsZ = 3;

		if (Math.abs(centre.getX() - loc.getX()) > boundsX)
			return false;
		if (Math.abs(centre.getZ() - loc.getZ()) > boundsZ)
			return false;
		return true;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		PlayerData playerData = main.getDataManager().getPlayerData(player);

		if (playerData != null) {
			if (playerData.mysteryChests > 0) {
				contents.set(0, 0,
						ClickableItem
								.of(ItemHelper.setDetails(new ItemStack(Material.ENDER_CHEST, playerData.mysteryChests),
										"" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "MysteryChest",
										"", "" + ChatColor.RESET + ChatColor.YELLOW + "You have "
												+ playerData.mysteryChests + " Chests to Open!"),
										e -> {
											inv.close(player);
											playerData.mysteryChests--;
											main.getGameManager().chestCanOpen = true;

											BukkitRunnable runTimer = new BukkitRunnable() {

												int ticks = 10;

												@Override
												public void run() {
													if (!(player.getWorld() == main.getLobbyWorld())) {
														main.getGameManager().chestCanOpen = false;
														Block block = main.getLobbyWorld().getBlockAt(-16, 139, 40);
														block.setType(Material.SMOOTH_BRICK);
														Block block2 = main.getLobbyWorld().getBlockAt(-16, 139, 42);
														block2.setType(Material.SMOOTH_BRICK);
														Block block3 = main.getLobbyWorld().getBlockAt(-18, 139, 40);
														block3.setType(Material.SMOOTH_BRICK);
														Block block4 = main.getLobbyWorld().getBlockAt(-18, 139, 42);
														block4.setType(Material.SMOOTH_BRICK);
														Block block5 = main.getLobbyWorld().getBlockAt(-16, 143, 42);
														block5.setType(Material.AIR);
														Block block6 = main.getLobbyWorld().getBlockAt(-17, 143, 42);
														block6.setType(Material.AIR);
														Block block7 = main.getLobbyWorld().getBlockAt(-18, 143, 42);
														block7.setType(Material.AIR);
														Block block8 = main.getLobbyWorld().getBlockAt(-18, 143, 41);
														block8.setType(Material.AIR);
														Block block9 = main.getLobbyWorld().getBlockAt(-18, 143, 40);
														block9.setType(Material.AIR);
														Block block10 = main.getLobbyWorld().getBlockAt(-17, 143, 40);
														block10.setType(Material.AIR);
														Block block11 = main.getLobbyWorld().getBlockAt(-16, 143, 40);
														block11.setType(Material.AIR);
														Block block12 = main.getLobbyWorld().getBlockAt(-16, 143, 41);
														block12.setType(Material.AIR);
														Block block13 = main.getLobbyWorld().getBlockAt(-17, 143, 41);
														block13.setType(Material.AIR);
														Block block1200 = main.getLobbyWorld().getBlockAt(-16, 143, 40);
														block1200.setType(Material.AIR);

														Block block60 = main.getLobbyWorld().getBlockAt(-17, 139, 42);
														block60.setType(Material.SMOOTH_BRICK);
														Block block80 = main.getLobbyWorld().getBlockAt(-18, 139, 41);
														block80.setType(Material.SMOOTH_BRICK);
														Block block100 = main.getLobbyWorld().getBlockAt(-17, 139, 40);
														block100.setType(Material.SMOOTH_BRICK);
														Block block120 = main.getLobbyWorld().getBlockAt(-16, 139, 41);
														block120.setType(Material.SMOOTH_BRICK);
														this.cancel();
													}

													if (main.getGameManager().chestCanOpen == true) {
														if (!(isInBounds(player.getLocation()))) {
															player.teleport(new Location(main.getLobbyWorld(), -16.497,
																	140, 40.082));
														}
													}

													if (ticks == 10) {
														player.sendTitle(
																"" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD
																		+ "Mystery Chests",
																"" + ChatColor.YELLOW + "Opening mystery chest..");
														player.teleport(new Location(main.getLobbyWorld(), -16.497, 140,
																40.082));

														Block block = main.getLobbyWorld().getBlockAt(-16, 139, 40);
														block.setType(Material.EMERALD_BLOCK);
														Block block2 = main.getLobbyWorld().getBlockAt(-16, 139, 42);
														block2.setType(Material.EMERALD_BLOCK);
														Block block3 = main.getLobbyWorld().getBlockAt(-18, 139, 40);
														block3.setType(Material.EMERALD_BLOCK);
														Block block4 = main.getLobbyWorld().getBlockAt(-18, 139, 42);
														block4.setType(Material.EMERALD_BLOCK);
														Block block5 = main.getLobbyWorld().getBlockAt(-16, 143, 42);
														block5.setType(Material.EMERALD_BLOCK);
														Block block7 = main.getLobbyWorld().getBlockAt(-18, 143, 42);
														block7.setType(Material.EMERALD_BLOCK);
														Block block9 = main.getLobbyWorld().getBlockAt(-18, 143, 40);
														block9.setType(Material.EMERALD_BLOCK);
														Block block21 = main.getLobbyWorld().getBlockAt(-16, 143, 40);
														block21.setType(Material.EMERALD_BLOCK);
													} else if (ticks == 9) {
														Block block6 = main.getLobbyWorld().getBlockAt(-17, 143, 42);
														block6.setType(Material.EMERALD_BLOCK);
														Block block8 = main.getLobbyWorld().getBlockAt(-18, 143, 41);
														block8.setType(Material.EMERALD_BLOCK);
														Block block10 = main.getLobbyWorld().getBlockAt(-17, 143, 40);
														block10.setType(Material.EMERALD_BLOCK);
														Block block12 = main.getLobbyWorld().getBlockAt(-16, 143, 41);
														block12.setType(Material.EMERALD_BLOCK);
														Block block12000 = main.getLobbyWorld().getBlockAt(-16, 139, 41);
														block12000.setType(Material.EMERALD_BLOCK);

														Block block60 = main.getLobbyWorld().getBlockAt(-17, 139, 42);
														block60.setType(Material.EMERALD_BLOCK);
														Block block80 = main.getLobbyWorld().getBlockAt(-18, 139, 41);
														block80.setType(Material.EMERALD_BLOCK);
														Block block100 = main.getLobbyWorld().getBlockAt(-17, 139, 40);
														block100.setType(Material.EMERALD_BLOCK);
														Block block120 = main.getLobbyWorld().getBlockAt(-16, 143, 40);
														block120.setType(Material.EMERALD_BLOCK);
														Block block1200 = main.getLobbyWorld().getBlockAt(-16, 143, 41);
														block1200.setType(Material.EMERALD_BLOCK);
													} else if (ticks == 8) {
														Block block13 = main.getLobbyWorld().getBlockAt(-17, 143, 41);
														block13.setType(Material.SEA_LANTERN);
													} else if (ticks == 3) {
														Random rand = new Random();
														int chance = rand.nextInt(25);
														if (chance >= 0 && chance < 22) {
															playerData.melon += 14;
															player.sendMessage("" + ChatColor.LIGHT_PURPLE
																	+ ChatColor.BOLD + "(!) " + ChatColor.RESET
																	+ ChatColor.YELLOW
																	+ "You unlocked 14 Cosmetic Melons!");
															player.sendTitle("", "" + ChatColor.YELLOW
																	+ "You unlcoked 14 Cosmetic Melons!");
														} else if (chance > 21 && chance < 25) {
															player.sendMessage("" + ChatColor.LIGHT_PURPLE
																	+ ChatColor.BOLD + "(!) " + ChatColor.RESET
																	+ ChatColor.YELLOW + "You found "
																	+ ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "RARE "
																	+ ChatColor.RESET + ChatColor.YELLOW
																	+ "Astronaut Outfit!");
															player.sendTitle("",
																	"" + ChatColor.YELLOW + "You found "
																			+ ChatColor.LIGHT_PURPLE + ChatColor.BOLD
																			+ "RARE " + ChatColor.RESET
																			+ ChatColor.YELLOW + "Astronaut Outfit!");

															if (playerData.astronaut == 1) {
																player.sendMessage("" + ChatColor.LIGHT_PURPLE
																		+ ChatColor.BOLD + "(!) " + ChatColor.RESET
																		+ "You already have this cosmetic!");
															}
															playerData.astronaut = 1;
														}
													} else if (ticks == 0) {
														main.getGameManager().chestCanOpen = false;
														Block block = main.getLobbyWorld().getBlockAt(-16, 139, 40);
														block.setType(Material.SMOOTH_BRICK);
														Block block2 = main.getLobbyWorld().getBlockAt(-16, 139, 42);
														block2.setType(Material.SMOOTH_BRICK);
														Block block3 = main.getLobbyWorld().getBlockAt(-18, 139, 40);
														block3.setType(Material.SMOOTH_BRICK);
														Block block4 = main.getLobbyWorld().getBlockAt(-18, 139, 42);
														block4.setType(Material.SMOOTH_BRICK);
														Block block5 = main.getLobbyWorld().getBlockAt(-16, 143, 42);
														block5.setType(Material.AIR);
														Block block6 = main.getLobbyWorld().getBlockAt(-17, 143, 42);
														block6.setType(Material.AIR);
														Block block7 = main.getLobbyWorld().getBlockAt(-18, 143, 42);
														block7.setType(Material.AIR);
														Block block8 = main.getLobbyWorld().getBlockAt(-18, 143, 41);
														block8.setType(Material.AIR);
														Block block9 = main.getLobbyWorld().getBlockAt(-18, 143, 40);
														block9.setType(Material.AIR);
														Block block10 = main.getLobbyWorld().getBlockAt(-17, 143, 40);
														block10.setType(Material.AIR);
														Block block11 = main.getLobbyWorld().getBlockAt(-16, 143, 40);
														block11.setType(Material.AIR);
														Block block12 = main.getLobbyWorld().getBlockAt(-16, 143, 41);
														block12.setType(Material.AIR);
														Block block13 = main.getLobbyWorld().getBlockAt(-17, 143, 41);
														block13.setType(Material.AIR);
														Block block1200 = main.getLobbyWorld().getBlockAt(-16, 143, 40);
														block1200.setType(Material.AIR);

														Block block60 = main.getLobbyWorld().getBlockAt(-17, 139, 42);
														block60.setType(Material.SMOOTH_BRICK);
														Block block80 = main.getLobbyWorld().getBlockAt(-18, 139, 41);
														block80.setType(Material.SMOOTH_BRICK);
														Block block100 = main.getLobbyWorld().getBlockAt(-17, 139, 40);
														block100.setType(Material.SMOOTH_BRICK);
														Block block120 = main.getLobbyWorld().getBlockAt(-16, 139, 41);
														block120.setType(Material.SMOOTH_BRICK);
														this.cancel();
													}

													ticks--;
												}
											};
											runTimer.runTaskTimer(main, 0, 20);
										}));
				
				
				contents.set(1, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.WORKBENCH),
						main.color("&e&lClick here to craft a &9&lMystery Chest")),
						e -> {
							new MysteryChestCraftGUI(main).inv.open(player);
						}));
			} else {
				contents.set(1, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.BARRIER),
						"" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + "You do not have any Mystery Chests"),
						e -> {
						}));
			}
		}
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}

}
