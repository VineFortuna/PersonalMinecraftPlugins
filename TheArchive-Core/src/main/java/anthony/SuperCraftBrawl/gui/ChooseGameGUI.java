package anthony.SuperCraftBrawl.gui;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.GameManager;
import anthony.SuperCraftBrawl.Game.GameType;
import anthony.SuperCraftBrawl.Game.map.Maps;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class ChooseGameGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;
	private HashMap<Player, BukkitRunnable> borderRunnables = new HashMap<>();

	public ChooseGameGUI(Main main) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "Choose a Game").build();
		this.main = main;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		PlayerData data = main.getDataManager().getPlayerData(player);
		GameManager gameManager = main.getGameManager();
		GameInstance game = gameManager.getWaitingMap();

		if (player.hasPermission("scb.cooldown")) {
			contents.set(1, 1, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.WHEAT),
					"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Click to join SCB Frenzy!", "",
					"" + ChatColor.RESET + ChatColor.GRAY + "An unlimited amount of players",
					"" + ChatColor.RESET + ChatColor.GRAY + "and randomly selected classes",
					"" + ChatColor.RESET + ChatColor.GRAY + "each life", "", "" + ChatColor.YELLOW + "There are "
							+ gameManager.getNumOfGamesFrenzy() + ChatColor.YELLOW + " Games Playing"),
					e -> {
						// GameManager gameManager = main.getGameManager();
						// GameInstance game = gameManager.getWaitingMap();

						/*if (game != null && game.getMap().GetInstance().gameType == GameType.FRENZY) {
							for (Maps map : Maps.values()) {
								if (game.getMap().GetInstance().gameType == GameType.FRENZY) {
									main.getGameManager().JoinMap(player, game.getMap());
									return;
								}
							}
						} else {
							for (Maps map : Maps.values()) {
								Random random = new Random();
								Maps maps = Maps.values()[random.nextInt(Maps.values().length)];
								if (maps.GetInstance().gameType == GameType.FRENZY) {
									main.getGameManager().JoinMap(player, maps);
									return;
								}
							}
						}*/
						
						main.getGameManager().joinRandomGame(player, GameType.FRENZY);
						
						inv.close(player);
					}));
			contents.set(2, 1,
					ClickableItem.of(
							ItemHelper.setDetails(new ItemStack(Material.BOOK_AND_QUILL),
									"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Introduction to Frenzy",
									"", "" + ChatColor.RESET + ChatColor.GRAY + "Click to get an Intro to Frenzy!"),
							e -> {
								main.frenzyIntro(player);
								inv.close(player);
							}));
			contents.set(0, 4,
					ClickableItem.of(
							ItemHelper.setDetails(new ItemStack(Material.ENDER_CHEST),
									"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "SuperCraftBlocks Lobby",
								"", "" + ChatColor.RESET + ChatColor.GRAY + "Click to teleport to SCB Lobby!"),
							e -> {
								player.teleport(main.getSCBLoc());
								inv.close(player);
							}));
			contents.set(1, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.NETHER_STAR),
					"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Click to join SCB Normal!", "",
					"" + ChatColor.RESET + ChatColor.GRAY + "Choose a class, fight your opponents",
					"" + ChatColor.RESET + ChatColor.GRAY + "with a certain class and claim",
					"" + ChatColor.RESET + ChatColor.GRAY + "the #1 spot!", "", "" + ChatColor.YELLOW + "There are "
							+ gameManager.getNumOfGamesNormal() + ChatColor.YELLOW + " Games Playing"),
					e -> {
						// GameManager gameManager = main.getGameManager();
						// GameInstance game = gameManager.getWaitingMap();

						/*if (game != null && game.getMap().GetInstance().gameType == GameType.NORMAL) {
							for (Maps map : Maps.values()) {
								if (game.getMap().GetInstance().gameType == GameType.NORMAL) {
									main.getGameManager().JoinMap(player, game.getMap());
									return;
								}
							}
						} else {
							for (Maps map : Maps.values()) {
								Random random = new Random();
								Maps maps = Maps.values()[random.nextInt(Maps.values().length)];
								if (maps.GetInstance().gameType == GameType.NORMAL) {
									main.getGameManager().JoinMap(player, maps);
									return;
								}
							}
						}*/
						
						main.getGameManager().joinRandomGame(player, GameType.NORMAL);
						inv.close(player);
					}));
			contents.set(1, 7, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.IRON_SWORD),
					"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Click to join SCB Duel!", "",
					"" + ChatColor.RESET + ChatColor.GRAY + "Two players competing 1 vs. 1", "", "" + ChatColor.YELLOW
							+ "There are " + gameManager.getNumOfGamesDuel() + ChatColor.YELLOW + " Games Playing"),
					e -> {
						/*if (game != null && game.getMap().GetInstance().gameType == GameType.DUEL) {
							for (Maps map : Maps.values()) {
								if (game.getMap().GetInstance().gameType == GameType.DUEL) {
									main.getGameManager().JoinMap(player, game.getMap());
									return;
								}
							}
						} else {
							for (Maps map : Maps.values()) {
								Random random = new Random();
								Maps maps = Maps.values()[random.nextInt(Maps.values().length)];
								if (maps.GetInstance().gameType == GameType.DUEL) {
									main.getGameManager().JoinMap(player, maps);
									return;
								}
							}
						}*/
						
						main.getGameManager().joinRandomGame(player, GameType.DUEL);
						inv.close(player);
					}));
		} else {
			BukkitRunnable runnable = borderRunnables.get(player);
			if (runnable == null) {
				runnable = new BukkitRunnable() {
					int ticks = 5;

					@Override
					public void run() {
						if (ticks == 5) {
							contents.set(1, 2,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 3,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 4,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK, 5),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 5,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 6,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
						} else if (ticks == 4) {
							contents.set(1, 2,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
							contents.set(1, 3,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 4,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK, 4),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 5,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 6,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
						} else if (ticks == 3) {
							contents.set(1, 2,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
							contents.set(1, 3,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 4,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK, 3),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 5,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 6,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
						} else if (ticks == 2) {
							contents.set(1, 2,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
							contents.set(1, 3,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 4,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK, 2),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 5,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
							contents.set(1, 6,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
						} else if (ticks == 1) {
							contents.set(1, 2,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
							contents.set(1, 3,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
							contents.set(1, 4,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK),
											"" + ChatColor.RESET + ChatColor.BOLD + "Purchase a rank at:",
											"" + ChatColor.RESET + ChatColor.YELLOW + "minezone.tebex.io "
													+ ChatColor.RESET + ChatColor.BOLD + "to bypass",
											"" + ChatColor.RESET + ChatColor.BOLD + "this restriction!"), e -> {

											}));
							contents.set(1, 5,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
							contents.set(1, 6,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
						} else if (ticks == 0) {
							contents.set(1, 4,
									ClickableItem.of(new ItemStack(Material.AIR), e -> {

											}));
							
							borderRunnables.remove(player);
							this.cancel();

							contents.set(1, 1,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.WHEAT),
											"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD
													+ "Click to join SCB Frenzy!",
											"",
											"" + ChatColor.RESET + ChatColor.GRAY + "An unlimited amount of players",
											"" + ChatColor.RESET + ChatColor.GRAY + "and randomly selected classes",
											"" + ChatColor.RESET + ChatColor.GRAY + "each life", "",
											"" + ChatColor.YELLOW + "There are " + gameManager.getNumOfGamesFrenzy()
													+ ChatColor.YELLOW + " Games Playing"),
											e -> {
												// GameManager gameManager = main.getGameManager();
												// GameInstance game = gameManager.getWaitingMap();

												if (game != null
														&& game.getMap().GetInstance().gameType == GameType.FRENZY) {
													for (Maps map : Maps.values()) {
														if (game.getMap().GetInstance().gameType == GameType.FRENZY) {
															main.getGameManager().JoinMap(player, game.getMap());
															return;
														}
													}
												} else {
													for (Maps map : Maps.values()) {
														Random random = new Random();
														Maps maps = Maps.values()[random.nextInt(Maps.values().length)];
														if (maps.GetInstance().gameType == GameType.FRENZY) {
															main.getGameManager().JoinMap(player, maps);
															return;
														}
													}
												}
												inv.close(player);
											}));
							contents.set(2, 1, ClickableItem.of(
									ItemHelper.setDetails(new ItemStack(Material.BOOK_AND_QUILL),
											"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD
													+ "Introduction to Frenzy",
											"",
											"" + ChatColor.RESET + ChatColor.GRAY + "Click to get an Intro to Frenzy!"),
									e -> {
										main.frenzyIntro(player);
										inv.close(player);
									}));
							contents.set(1, 4, ClickableItem.of(ItemHelper.setDetails(
									new ItemStack(Material.NETHER_STAR),
									"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD
											+ "Click to join SCB Normal!",
									"", "" + ChatColor.RESET + ChatColor.GRAY + "Choose a class, fight your opponents",
									"" + ChatColor.RESET + ChatColor.GRAY + "with a certain class and claim",
									"" + ChatColor.RESET + ChatColor.GRAY + "the #1 spot!", "",
									"" + ChatColor.YELLOW + "There are " + gameManager.getNumOfGamesNormal()
											+ ChatColor.YELLOW + " Games Playing"),
									e -> {
										// GameManager gameManager = main.getGameManager();
										// GameInstance game = gameManager.getWaitingMap();

										if (game != null && game.getMap().GetInstance().gameType == GameType.NORMAL) {
											for (Maps map : Maps.values()) {
												if (game.getMap().GetInstance().gameType == GameType.NORMAL) {
													main.getGameManager().JoinMap(player, game.getMap());
													return;
												}
											}
										} else {
											for (Maps map : Maps.values()) {
												Random random = new Random();
												Maps maps = Maps.values()[random.nextInt(Maps.values().length)];
												if (maps.GetInstance().gameType == GameType.NORMAL) {
													main.getGameManager().JoinMap(player, maps);
													return;
												}
											}
										}
										inv.close(player);
									}));
							contents.set(1, 7,
									ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.IRON_SWORD),
											"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD
													+ "Click to join SCB Duel!",
											"", "" + ChatColor.RESET + ChatColor.GRAY + "Two players competing 1 vs. 1",
											"", "" + ChatColor.YELLOW + "There are " + gameManager.getNumOfGamesDuel()
													+ ChatColor.YELLOW + " Games Playing"),
											e -> {
												if (game != null
														&& game.getMap().GetInstance().gameType == GameType.DUEL) {
													for (Maps map : Maps.values()) {
														if (game.getMap().GetInstance().gameType == GameType.DUEL) {
															main.getGameManager().JoinMap(player, game.getMap());
															return;
														}
													}
												} else {
													for (Maps map : Maps.values()) {
														Random random = new Random();
														Maps maps = Maps.values()[random.nextInt(Maps.values().length)];
														if (maps.GetInstance().gameType == GameType.DUEL) {
															main.getGameManager().JoinMap(player, maps);
															return;
														}
													}
												}
												inv.close(player);
											}));
						}

						ticks--;
					}

				};
				runnable.runTaskTimer(main, 0, 20);
				borderRunnables.put(player, runnable);
			}
		}
	}

	public void menuCooldown(Player player) {

	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
