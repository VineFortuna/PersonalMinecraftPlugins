package anthony.SuperCraftBrawl.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.map.MapInstance;
import anthony.SuperCraftBrawl.Game.map.Maps;
import anthony.SuperCraftBrawl.gui.CraftableItemsGUI;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import anthony.SuperCraftBrawl.ranks.Rank;
import anthony.SuperCraftBrawl.worldgen.VoidGenerator;
import fr.mrmicky.fastboard.FastBoard;
import net.md_5.bungee.api.ChatColor;

public class GameInstance {

	// Static variables
	public static int MIN_STARTING_PLAYERS = 2, MIN_PLAYERS = 2;
	static Plugin plugin;

	// Variables
	private final GameManager gameManager;
	private Objective livesObjective;
	public GameType gameType;
	private Random random = new Random();
	private final Maps map;
	private World mapWorld;
	private int gameTicks = 0;
	public GameState state;
	public List<Player> players;
	public List<Player> spectators;
	public HashMap<Player, BaseClass> classes;
	public HashMap<Player, BaseClass> oldClasses;
	public List<Player> playerPosition = new ArrayList<>();
	public HashMap<Player, FastBoard> boards = new HashMap();
	private HashMap<Player, ClassType> classSelection = new HashMap<>();
	public BukkitRunnable gameStartTime;
	public int ticksTilStart = 30;
	List<BukkitRunnable> runnables = new ArrayList<>();
	public int totalVotes = 0;
	public int blindness = 0;
	public ItemStack paper = ItemHelper.setDetails(new ItemStack(Material.PAPER),
			"" + ChatColor.YELLOW + ChatColor.BOLD + "Vote for Game Start");
	public int alivePlayers = 0;

	// Constructor
	public GameInstance(GameManager gameManager, Maps map) {
		this.gameManager = gameManager;
		this.map = map;
		this.state = GameState.WAITING; // Default game state
		this.gameType = map.GetInstance().gameType;
		this.players = new ArrayList<Player>();
		this.spectators = new ArrayList<Player>();
		classes = new HashMap<>();
		oldClasses = new HashMap<>();
		InitialiseMap();
	}

	public GameManager getManager() {
		return gameManager;
	}

	public Maps getMap() {
		return map;
	}

	public World getMapWorld() {
		return mapWorld;
	}

	public void InitialiseMap() {
		WorldCreator w = new WorldCreator(map.GetInstance().worldName).environment(World.Environment.NORMAL);
		w.generator(new VoidGenerator());
		mapWorld = Bukkit.getServer().createWorld(w);
	}

	public Location GetLobbyLoc() {
		MapInstance mapInstance = map.GetInstance();
		Vector v = mapInstance.lobbyLoc;

		return new Location(mapWorld, v.getX(), v.getY(), v.getZ());
	}

	public boolean isOpen() {
		return state == GameState.WAITING && this.players.size() < gameType.getMaxPlayers();
	}

	@EventHandler
	public void SendPlayerToMap(Player player) {
		player.teleport(GetLobbyLoc());
	}

	public void bunnyClassEvent(Player player) {
		BukkitRunnable runTimer = new BukkitRunnable() {

			int ticks = 5;

			@Override
			public void run() {
				if (ticks == 5) {
					player.removePotionEffect(PotionEffectType.SPEED);
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 110, 4));
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 110, 1));
				} else if (ticks == 0) {
					player.removePotionEffect(PotionEffectType.SPEED);
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 2));
					this.cancel();
				}

				ticks--;
			}
		};
		runTimer.runTaskTimer(gameManager.getMain(), 0, 20);
	}

	public void steveClassEvent(Player player) {
		new CraftableItemsGUI(gameManager.getMain()).inv.open(player);
	}

	public void snowmanEvent(Player player) {
		for (Player gamePlayer : players) {
			BaseClass baseClass = classes.get(gamePlayer);
			if (player != gamePlayer) {
				BukkitRunnable runTimer = new BukkitRunnable() {

					int ticks = 10;

					@Override
					public void run() {
						if (ticks == 10) {
							if (gamePlayer.getGameMode() != GameMode.SPECTATOR)
								gamePlayer.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
						} else if (ticks == 0) {
							baseClass.LoadArmor(gamePlayer);
							this.cancel();
						}

						ticks--;
					}
				};
				runTimer.runTaskTimer(gameManager.getMain(), 0, 20);
			}
		}
	}

	public GameReason AddSpectator(Player player) {
		if (state == GameState.STARTED) {
			if (!players.contains(player)) {
				spectators.add(player);
				player.getInventory().clear();
				player.teleport(GetSpecLoc());
				gameManager.getMain().board.get(player).delete();
				setGameScore(player);
				player.setDisplayName("" + player.getName() + " " + ChatColor.RESET + ChatColor.GRAY + ChatColor.ITALIC
						+ "Spectator" + ChatColor.RESET);

				return GameReason.SPECTATOR;
			} else
				return GameReason.ALREADY_IN;
		} else
			return GameReason.FAIL;
	}

	public void SetWaitingScoreboard(Player player) {
		FastBoard board = new FastBoard(player);
		boards.put(player, board);
		ClassType selectedClass = classSelection.get(player);

		board.updateTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "SUPER CRAFT BLOCKS");
		board.updateLines("" + ChatColor.RESET + ChatColor.BOLD + "Map:", " " + ChatColor.RESET + map.toString()
				+ (map.GetInstance().gameType == GameType.FRENZY ? "" + ChatColor.GRAY + ChatColor.ITALIC + " (frenzy)"
						: ""), // Empty
								// line
				"", "" + ChatColor.RESET + ChatColor.BOLD + "Class:", " " + ChatColor.RESET + "Random", "",
				"" + ChatColor.RESET + ChatColor.BOLD + "Players:",
				" " + ChatColor.RESET
						+ (map.GetInstance().gameType == GameType.FRENZY
								? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
								: "")
						+ (map.GetInstance().gameType == GameType.NORMAL
								? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
								: "")
						+ (map.GetInstance().gameType == GameType.DUEL
								? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
								: "")
						+ (map.GetInstance().gameType == GameType.CTF ? "" + ChatColor.RESET + players.size() + "/12"
								: ""),
				"", "" + ChatColor.RESET + ChatColor.BOLD + "Status:",
				"" + ChatColor.RESET + ChatColor.ITALIC + " Waiting..");

		if (gameType == GameType.CTF) {
			boards.get(player).updateTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "CAPTURE THE FLAG");
		} else {
			boards.get(player).updateTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "SUPER CRAFT BLOCKS");
		}

	}

	public void removeArmor(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
		player.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
		player.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
		player.getInventory().setBoots(new ItemStack(Material.AIR, 1));
	}

	public GameReason AddPlayer(Player player) {
		if (state == GameState.WAITING) {
			if (!players.contains(player)) {

				if (!(player.hasPermission("scb.bypassFull"))) {
					if (gameType == GameType.NORMAL && players.size() >= 5) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This game is full!");
						return GameReason.FULL;
					} else if (gameType == GameType.DUEL && players.size() >= 2) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This game is full!");
						return GameReason.FULL;
					} else if (gameType == GameType.CTF && players.size() > 0) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This game is full!");
						return GameReason.FULL;
					}
				}

				players.add(player);
				if (player.hasPermission("scb.bypassQueue")) {
					player.sendMessage(
							"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Game found on map "
									+ ChatColor.YELLOW + getMap() + "! " + ChatColor.RESET + "Teleporting to game..");

					if (players.size() == 1) {
						SendPlayerToMap(player);
						CheckForGameStart(player);
						SetWaitingScoreboard(player);
					} else if (players.size() > 1) {
						for (Player gamePlayer : players) {
							if (gamePlayer.getWorld() != mapWorld) {
								SendPlayerToMap(gamePlayer);
								CheckForGameStart(gamePlayer);
								SetWaitingScoreboard(gamePlayer);
							}
						}
					}

					for (Player gamePlayer : players) {
						if (gamePlayer != player) {
							boards.get(gamePlayer)
									.updateLine(7, " "
											+ (map.GetInstance().gameType == GameType.FRENZY
													? "" + ChatColor.RESET + players.size() + "/"
															+ gameType.getMaxPlayers()
													: "")
											+ (map.GetInstance().gameType == GameType.NORMAL
													? "" + ChatColor.RESET + players.size() + "/"
															+ gameType.getMaxPlayers()
													: "")
											+ (map.GetInstance().gameType == GameType.DUEL
													? "" + ChatColor.RESET + players.size() + "/"
															+ gameType.getMaxPlayers()
													: "")
											+ (map.GetInstance().gameType == GameType.CTF
													? "" + ChatColor.RESET + players.size() + "/12"
													: ""));
							if (gameType == GameType.CTF) {
								boards.get(gamePlayer).updateTitle("" + (map.GetInstance().gameType == GameType.CTF
										? "" + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD + "CAPTURE THE FLAG"
										: ""));
							} else {
								boards.get(gamePlayer)
										.updateTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "SUPER CRAFT BLOCKS");
							}
						}
					}

					if (gameType != GameType.DUEL) {
						TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + player.getName()
								+ ChatColor.GREEN + " joined " + ChatColor.RED + "(" + ChatColor.GREEN
								+ (map.GetInstance().gameType == GameType.FRENZY
										? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
										: "")
								+ (map.GetInstance().gameType == GameType.NORMAL
										? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
										: "")
								+ (map.GetInstance().gameType == GameType.DUEL
										? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
										: "")
								+ (map.GetInstance().gameType == GameType.CTF
										? "" + ChatColor.RESET + players.size() + "/12"
										: "")
								+ ChatColor.RED + ")");
					}

					removeArmor(player);

					if (gameType == GameType.FRENZY) {
						player.sendTitle(
								"" + ChatColor.BOLD + "You have joined " + ChatColor.YELLOW + ChatColor.BOLD
										+ map.toString(),
								"" + ChatColor.GREEN + "Your class will be randomly selected!");
					} else {
						player.sendTitle("" + ChatColor.BOLD + "You have joined " + ChatColor.YELLOW + ChatColor.BOLD
								+ map.toString(), "" + ChatColor.GREEN + "Choose your class!");
					}

				} else {
					if (gameType != GameType.CTF) {
						if (players.size() == 2) {
							for (Player gamePlayer : players) {
								if (gamePlayer.getWorld() != mapWorld) {
									gamePlayer.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
											+ ChatColor.RESET + "Game found on map " + ChatColor.YELLOW + getMap()
											+ "! " + ChatColor.RESET + "Teleporting to game..");
									SendPlayerToMap(gamePlayer);
									CheckForGameStart(gamePlayer);
									SetWaitingScoreboard(gamePlayer);
								}

								if (gamePlayer != player) {
									boards.get(gamePlayer)
											.updateLine(7, " "
													+ (map.GetInstance().gameType == GameType.FRENZY
															? "" + ChatColor.RESET + players.size() + "/"
																	+ gameType.getMaxPlayers()
															: "")
													+ (map.GetInstance().gameType == GameType.NORMAL
															? "" + ChatColor.RESET + players.size() + "/"
																	+ gameType.getMaxPlayers()
															: "")
													+ (map.GetInstance().gameType == GameType.DUEL
															? "" + ChatColor.RESET + players.size() + "/"
																	+ gameType.getMaxPlayers()
															: "")
													+ (map.GetInstance().gameType == GameType.CTF
															? "" + ChatColor.RESET + players.size() + "/12"
															: ""));
									if (gameType == GameType.CTF) {
										boards.get(gamePlayer)
												.updateTitle(""
														+ (map.GetInstance().gameType == GameType.CTF
																? "" + ChatColor.RESET + ChatColor.GREEN
																		+ ChatColor.BOLD + "CAPTURE THE FLAG"
																: ""));
									} else {
										boards.get(gamePlayer).updateTitle(
												"" + ChatColor.YELLOW + ChatColor.BOLD + "SUPER CRAFT BLOCKS");
									}
								}

								/*
								 * if (gameType != GameType.DUEL) { TellAll("" + ChatColor.DARK_GREEN +
								 * ChatColor.BOLD + "(!) " + ChatColor.RESET + player.getName() +
								 * ChatColor.GREEN + " joined " + ChatColor.RED + "(" + ChatColor.GREEN +
								 * (map.GetInstance().gameType == GameType.FRENZY ? "" + ChatColor.RESET +
								 * players.size() + "/" + gameType.getMaxPlayers() : "") +
								 * (map.GetInstance().gameType == GameType.NORMAL ? "" + ChatColor.RESET +
								 * players.size() + "/" + gameType.getMaxPlayers() : "") +
								 * (map.GetInstance().gameType == GameType.DUEL ? "" + ChatColor.RESET +
								 * players.size() + "/" + gameType.getMaxPlayers() : "") +
								 * (map.GetInstance().gameType == GameType.CTF ? "" + ChatColor.RESET +
								 * players.size() + "/12" : "") + ChatColor.RED + ")"); }
								 */

								removeArmor(player);

								if (gameType == GameType.FRENZY) {
									player.sendTitle(
											"" + ChatColor.BOLD + "You have joined " + ChatColor.YELLOW + ChatColor.BOLD
													+ map.toString(),
											"" + ChatColor.GREEN + "Your class will be randomly selected!");
								} else {
									player.sendTitle("" + ChatColor.BOLD + "You have joined " + ChatColor.YELLOW
											+ ChatColor.BOLD + map.toString(),
											"" + ChatColor.GREEN + "Choose your class!");
								}

							}
						} else if (players.size() > 2) {
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "Game found on map " + ChatColor.YELLOW + getMap() + "! " + ChatColor.RESET
									+ "Teleporting to game..");
							SendPlayerToMap(player);
							CheckForGameStart(player);
							SetWaitingScoreboard(player);

							for (Player gamePlayer : players) {
								if (gamePlayer != player) {
									boards.get(gamePlayer)
											.updateLine(7, " "
													+ (map.GetInstance().gameType == GameType.FRENZY
															? "" + ChatColor.RESET + players.size() + "/"
																	+ gameType.getMaxPlayers()
															: "")
													+ (map.GetInstance().gameType == GameType.NORMAL
															? "" + ChatColor.RESET + players.size() + "/"
																	+ gameType.getMaxPlayers()
															: "")
													+ (map.GetInstance().gameType == GameType.DUEL
															? "" + ChatColor.RESET + players.size() + "/"
																	+ gameType.getMaxPlayers()
															: "")
													+ (map.GetInstance().gameType == GameType.CTF
															? "" + ChatColor.RESET + players.size() + "/12"
															: ""));
									if (gameType == GameType.CTF) {
										boards.get(gamePlayer)
												.updateTitle(""
														+ (map.GetInstance().gameType == GameType.CTF
																? "" + ChatColor.RESET + ChatColor.GREEN
																		+ ChatColor.BOLD + "CAPTURE THE FLAG"
																: ""));
									} else {
										boards.get(gamePlayer).updateTitle(
												"" + ChatColor.YELLOW + ChatColor.BOLD + "SUPER CRAFT BLOCKS");
									}
								}
							}

							if (gameType != GameType.DUEL) {
								TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
										+ player.getName() + ChatColor.GREEN + " joined " + ChatColor.RED + "("
										+ ChatColor.GREEN
										+ (map.GetInstance().gameType == GameType.FRENZY
												? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
												: "")
										+ (map.GetInstance().gameType == GameType.NORMAL
												? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
												: "")
										+ (map.GetInstance().gameType == GameType.DUEL
												? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
												: "")
										+ (map.GetInstance().gameType == GameType.CTF
												? "" + ChatColor.RESET + players.size() + "/12"
												: "")
										+ ChatColor.RED + ")");
							}

							removeArmor(player);

							if (gameType == GameType.FRENZY) {
								player.sendTitle(
										"" + ChatColor.BOLD + "You have joined " + ChatColor.YELLOW + ChatColor.BOLD
												+ map.toString(),
										"" + ChatColor.GREEN + "Your class will be randomly selected!");
							} else {
								player.sendTitle("" + ChatColor.BOLD + "You have joined " + ChatColor.YELLOW
										+ ChatColor.BOLD + map.toString(), "" + ChatColor.GREEN + "Choose your class!");
							}
						} else {
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You are now queueing on map " + ChatColor.YELLOW + getMap() + ChatColor.GREEN
									+ " (1/" + gameType.getMaxPlayers() + ")");
							player.sendTitle("" + ChatColor.YELLOW + "You are queueing for",
									"" + getMap() + " --> (1/" + gameType.getMaxPlayers() + ")");
						}
					} else {
						SendPlayerToMap(player);
						CheckForGameStart(player);
						SetWaitingScoreboard(player);

						for (Player gamePlayer : players) {
							if (gamePlayer != player) {
								boards.get(gamePlayer)
										.updateLine(7, " "
												+ (map.GetInstance().gameType == GameType.FRENZY
														? "" + ChatColor.RESET + players.size() + "/"
																+ gameType.getMaxPlayers()
														: "")
												+ (map.GetInstance().gameType == GameType.NORMAL
														? "" + ChatColor.RESET + players.size() + "/"
																+ gameType.getMaxPlayers()
														: "")
												+ (map.GetInstance().gameType == GameType.DUEL
														? "" + ChatColor.RESET + players.size() + "/"
																+ gameType.getMaxPlayers()
														: "")
												+ (map.GetInstance().gameType == GameType.CTF
														? "" + ChatColor.RESET + players.size() + "/12"
														: ""));
								if (gameType == GameType.CTF) {
									boards.get(gamePlayer)
											.updateTitle(""
													+ (map.GetInstance().gameType == GameType.CTF
															? "" + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD
																	+ "CAPTURE THE FLAG"
															: ""));
								} else {
									boards.get(gamePlayer)
											.updateTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "SUPER CRAFT BLOCKS");
								}
							}
						}

						TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + player.getName()
								+ ChatColor.GREEN + " joined " + ChatColor.RED + "(" + ChatColor.GREEN
								+ (map.GetInstance().gameType == GameType.FRENZY
										? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
										: "")
								+ (map.GetInstance().gameType == GameType.NORMAL
										? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
										: "")
								+ (map.GetInstance().gameType == GameType.DUEL
										? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
										: "")
								+ (map.GetInstance().gameType == GameType.CTF
										? "" + ChatColor.RESET + players.size() + "/12"
										: "")
								+ ChatColor.RED + ")");

						removeArmor(player);

						if (gameType == GameType.FRENZY) {
							player.sendTitle(
									"" + ChatColor.BOLD + "You have joined " + ChatColor.YELLOW + ChatColor.BOLD
											+ map.toString(),
									"" + ChatColor.GREEN + "Your class will be randomly selected!");
						} else {
							player.sendTitle("" + ChatColor.BOLD + "You have joined " + ChatColor.YELLOW
									+ ChatColor.BOLD + map.toString(), "" + ChatColor.GREEN + "Choose your class!");
						}
					}
				}
				// CheckForGameStart(player);
				// SetWaitingScoreboard(player);

				/*
				 * for (Player gamePlayer : players) { if (gamePlayer != player) {
				 * boards.get(gamePlayer) .updateLine( 7, " " + (map.GetInstance().gameType ==
				 * GameType.FRENZY ? "" + ChatColor.RESET + players.size() + "/" +
				 * gameType.getMaxPlayers() : "") + (map.GetInstance().gameType ==
				 * GameType.NORMAL ? "" + ChatColor.RESET + players.size() + "/" +
				 * gameType.getMaxPlayers() : "") + (map.GetInstance().gameType == GameType.DUEL
				 * ? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers() :
				 * "") + (map.GetInstance().gameType == GameType.CTF ? "" + ChatColor.RESET +
				 * players.size() + "/12" : "")); if (gameType == GameType.CTF) {
				 * boards.get(gamePlayer).updateTitle("" + (map.GetInstance().gameType ==
				 * GameType.CTF ? "" + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD +
				 * "CAPTURE THE FLAG" : "")); } else { boards.get(gamePlayer) .updateTitle("" +
				 * ChatColor.YELLOW + ChatColor.BOLD + "SUPER CRAFT BLOCKS"); } } }
				 * 
				 * TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
				 * + player.getName() + ChatColor.GREEN + " joined " + ChatColor.RED + "(" +
				 * ChatColor.GREEN + (map.GetInstance().gameType == GameType.FRENZY ? "" +
				 * ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers() : "") +
				 * (map.GetInstance().gameType == GameType.NORMAL ? "" + ChatColor.RESET +
				 * players.size() + "/" + gameType.getMaxPlayers() : "") +
				 * (map.GetInstance().gameType == GameType.DUEL ? "" + ChatColor.RESET +
				 * players.size() + "/" + gameType.getMaxPlayers() : "") +
				 * (map.GetInstance().gameType == GameType.CTF ? "" + ChatColor.RESET +
				 * players.size() + "/12" : "") + ChatColor.RED + ")");
				 * 
				 * removeArmor(player);
				 * 
				 * if (gameType == GameType.FRENZY) { player.sendTitle(map.toString(), "" +
				 * ChatColor.GREEN + "Your class will be randomly selected!"); } else {
				 * player.sendTitle(map.toString(), "" + ChatColor.GREEN +
				 * "Choose your class!"); }
				 */

				return GameReason.SUCCESS;
			} else
				return GameReason.ALREADY_IN;

		} else
			return GameReason.ALREADYPLAYING;
	}

	public FastBoard board;

	public void setClass(Player player, ClassType type) {
		classSelection.put(player, type);
		if (gameType != GameType.FRENZY) {
			board = boards.get(player);
			board.updateLine(4, " " + type.getTag());

			if (player.hasPermission("scb.chat"))
				player.setDisplayName("" + player.getName() + " " + type.getTag());
			else
				player.setDisplayName("" + player.getName() + " " + type.getTag() + ChatColor.GRAY);
		}
	}

	public void CheckForGameStart(Player player) {
		// if (gameType == GameType.CTF && players.size() == CTF_MIN_PLAYERS)
		// StartGameTimer(player);
		if (gameType != GameType.CTF && players.size() == MIN_PLAYERS)
			StartGameTimer(player);
	}

	public int getSecondsUntilStart() {
		if (gameManager.getMain().tournament == false)
			return ticksTilStart = 30;
		return ticksTilStart = 60;
	}

	@EventHandler
	public void StartGameTimer(Player player) {
		if (gameStartTime == null) {
			ticksTilStart = getSecondsUntilStart();
			gameStartTime = new BukkitRunnable() {

				@Override
				public void run() {
					gameManager.getMain().getNPCManager().updateNpc(map);
					int ticks = ticksTilStart;
					if (ticks == 0) {
						StartGame();
						GameScoreboard();
						gameStartTime = null;
						this.cancel();
					} else if (ticks == 60) {
						if (gameManager.getMain().tournament == true) {
							TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "The game is now starting..");
						}
					} else if (ticks == 30) {
						for (Player gamePlayer : players) {
							if (!(gamePlayer.getInventory().contains(paper)))
								gamePlayer.getInventory().setItem(1, paper);
						}
						if (gameManager.getMain().tournament == false) {
							if (gameType == GameType.FRENZY) {
								Bukkit.broadcastMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
										+ ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD + "A " + ChatColor.RESET
										+ ChatColor.GRAY + ChatColor.ITALIC + "Frenzy " + ChatColor.GREEN
										+ ChatColor.BOLD + "game on " + ChatColor.RESET + ChatColor.BOLD
										+ map.toString() + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD
										+ " is starting in 30 seconds.");
								Bukkit.broadcastMessage("" + "    " + ChatColor.GREEN + ChatColor.BOLD + " Use "
										+ ChatColor.RESET + "/join " + map.toString() + ChatColor.GREEN + ChatColor.BOLD
										+ " to join!");
							} else if (gameType == GameType.NORMAL || gameType == GameType.CTF) {
								Bukkit.broadcastMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
										+ ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD + "A game on "
										+ ChatColor.RESET + ChatColor.BOLD + map.toString() + ChatColor.RESET
										+ ChatColor.GREEN + ChatColor.BOLD + " is starting in 30 seconds.");
								Bukkit.broadcastMessage("" + "    " + ChatColor.GREEN + ChatColor.BOLD + " Use "
										+ ChatColor.RESET + "/join " + map.toString() + ChatColor.GREEN + ChatColor.BOLD
										+ " to join!");
							}
							TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "All players have joined. Game is now starting!");
						}
						for (Player player : players)
							player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
					} else if (ticks == 25 || ticks == 20 || ticks == 15 || ticks == 10)
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);

					else if (ticks == 5 || ticks == 4 || ticks == 3 || ticks == 2) {
						for (Player player : players)
							player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
					} else if (ticks == 1)
						for (Player player : players)
							player.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 7);

					else if (ticks == 18) {
						Random rand = new Random();
						int chance = rand.nextInt(2);

						if (chance == 0) {
							TellAll("" + ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Tip" + ChatColor.DARK_GREEN
									+ "] " + ChatColor.RESET + ChatColor.LIGHT_PURPLE
									+ "Execute double jump by tapping the space bar twice!");
						} else if (chance == 1) {
							TellAll("" + ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Tip" + ChatColor.DARK_GREEN
									+ "] " + ChatColor.RESET + ChatColor.LIGHT_PURPLE
									+ "Consider purchasing a rank at our /store for more SCB features!");
						} else if (chance == 2) {
							TellAll("" + ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Tip" + ChatColor.DARK_GREEN
									+ "] " + ChatColor.RESET + ChatColor.LIGHT_PURPLE
									+ "Be sure to select a class by using the sign or compass!");
						}
						for (Player player : players)
							player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
					}

					for (Player player : players)
						// player.setLevel(ticks);
						if (ticks <= 5 && ticks >= 1)
							player.sendTitle("" + ChatColor.GREEN + ticks, "");
					if (ticks <= 60 && ticks >= 1) {
						if (players.size() >= 2) {
							for (Player player : players) {
								FastBoard board = boards.get(player);
								board.updateLine(10, " " + ticksTilStart + "s");
								board.updateLine(9, "" + ChatColor.RESET + ChatColor.BOLD + "Starting In:");
								if (players.size() >= 2)
									if (!(player.getInventory().contains(paper)))
										player.getInventory().addItem(paper);
							}
						}
					}

					for (Player gamePlayer : players) {
						if (ticks <= 60) {
							if (totalVotes == players.size()) {
								this.cancel();
								StartGame();
								gamePlayer.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
										+ ChatColor.RESET + "Game is now starting");
							}
						}
					}

					ticksTilStart--;
				}
			};
			gameStartTime.runTaskTimer(gameManager.getMain(), 0, 20);
		}
	}

	public Location GetRespawnLoc() {
		// Respawn location for each map
		MapInstance mapInstance = map.GetInstance();

		if (mapInstance.spawnPos.size() == 0)
			return GetLobbyLoc().add(new Vector(42, 2, 2.5));
		else {
			Vector spawnPos = mapInstance.spawnPos.get(random.nextInt(mapInstance.spawnPos.size()));
			return new Location(mapWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
		}
	}

	public Location GetSpecLoc() {
		// This is the spectator location for each map
		MapInstance mapInstance = map.GetInstance();
		Vector v = mapInstance.specLoc;

		return new Location(mapWorld, v.getX(), v.getY(), v.getZ());
	}

	public double boundsX, boundsZ;
	public BukkitRunnable moveBar;
	public int resetBoundsX = 0;
	public int resetBoundsZ = 0;

	public void moveBarrier() {
		if (moveBar == null) {
			moveBar = new BukkitRunnable() {
				int ticks = 0;

				@Override
				public void run() {
					if (state == GameState.ENDED) {
						MapInstance instance = map.GetInstance();
						if (resetBoundsX != 0)
							instance.boundsX = resetBoundsX + instance.boundsX;
						if (resetBoundsZ != 0)
							instance.boundsZ = resetBoundsZ + instance.boundsZ;
						moveBar = null;
						this.cancel();
					}

					if (ticks == 600) {
						for (Player gamePlayer : players) {
							int minutes = (ticks / 120);
							gamePlayer.sendTitle(
									gameManager.getMain().color("&9Barrier will begin moving in &r" + minutes + "m"),
									"");
							gamePlayer.sendMessage(
									gameManager.getMain().color("&9Barrier will begin moving in &r" + minutes + "m"));
						}
					} else if (ticks == 840) {
						for (Player gamePlayer : players) {
							int seconds = (ticks + 20) - 800;
							gamePlayer.sendTitle(
									gameManager.getMain().color("&9Barrier will begin moving in &r" + seconds + "s"),
									"");
							gamePlayer.sendMessage(
									gameManager.getMain().color("&9Barrier will begin moving in &r" + seconds + "s"));
						}
					} else if (ticks == 900) {
						for (Player gamePlayer : players) {
							gamePlayer.sendTitle(gameManager.getMain().color("&9Barrier moving is now active!"), "");
							gamePlayer.sendMessage(gameManager.getMain().color("&9Barrier moving is now active!"));
							MapInstance mapInstance = map.GetInstance();
							mapInstance.boundsX -= 3;
							resetBoundsX += 3;
							mapInstance.boundsZ -= 3;
							resetBoundsZ += 3;
						}
					} else if (ticks == 915) {
						for (Player gamePlayer : players) {
							ticks = 480;
							int seconds = (ticks / 60);
							// isInBounds(gamePlayer.getLocation());
							gamePlayer.sendTitle(gameManager.getMain().color("&9Barrier has been moved &r3 blocks"),
									gameManager.getMain().color("&eNext barrier move will be in &r2m"));
							gamePlayer.sendMessage(gameManager.getMain().color("&9Barrier has been moved &r3 blocks"));
							gamePlayer.sendMessage(gameManager.getMain().color("&eNext barrier move will be in &r2m"));
						}
					}

					ticks++;
				}
			};
			moveBar.runTaskTimer(gameManager.getMain(), 0, 20);
		}
	}

	public boolean isInBounds(Location loc) {
		MapInstance mapInstance = map.GetInstance();
		Vector v = mapInstance.center;
		Location centre = new Location(mapWorld, v.getX(), v.getY(), v.getZ());
		boundsX = mapInstance.boundsX;
		boundsZ = mapInstance.boundsZ;

		if (Math.abs(centre.getX() - loc.getX()) > boundsX)
			return false;
		if (Math.abs(centre.getZ() - loc.getZ()) > boundsZ)
			return false;
		return true;
	}

	public void StartGame() {
		for (Player gamePlayer : players) {
			PlayerData data = gameManager.getMain().getDataManager().getPlayerData(gamePlayer);
			data.votes = 0;
		}
		totalVotes = 0;
		startLightningDropsTimer();
		for (Player player : players) {
			player.getInventory().clear();
			player.teleport(GetRespawnLoc());
		}

		TellAll("" + ChatColor.BOLD + "===============================");
		TellAll("" + ChatColor.BOLD + "||");
		TellAll("" + ChatColor.BOLD + "||");
		TellAll("" + ChatColor.BOLD + "||");
		TellAll("" + ChatColor.BOLD + "|| " + "        " + ChatColor.YELLOW + ChatColor.BOLD + "  GAME STARTED");
		if (gameType == GameType.DUEL) {
			String playersInGame = "";
			for (Player gamePlayer : players) {
				playersInGame += gamePlayer.getName() + "";
				if (!players.isEmpty())
					playersInGame += ", ";
			}
			TellAll("" + ChatColor.BOLD + "|| " + "    " + ChatColor.RED + ChatColor.BOLD + " Players: "
					+ ChatColor.RESET + playersInGame);
		}
		TellAll("" + ChatColor.BOLD + "||");
		TellAll("" + ChatColor.BOLD + "||");
		TellAll("" + ChatColor.BOLD + "||");
		TellAll("" + ChatColor.BOLD + "===============================");
		state = GameState.STARTED;
		LoadClasses();
		gameManager.getMain().getNPCManager().updateRandomNpc();
		ItemStack item5 = ItemHelper.setDetails(new ItemStack(Material.POTION, 1),
				"" + ChatColor.RED + ChatColor.BOLD + "Slowness Pot", "");
		Potion pot5 = new Potion(3);
		pot5.setType(PotionType.SLOWNESS);
		pot5.setSplash(true);
		pot5.apply(item5);

		ItemStack item = ItemHelper.setDetails(new ItemStack(Material.POTION, 1),
				"" + ChatColor.YELLOW + ChatColor.BOLD + "Health Pot", "");
		Potion pot = new Potion(3);
		pot.setType(PotionType.INSTANT_HEAL);
		pot.setSplash(true);
		pot.apply(item);

		ItemStack item2 = ItemHelper.setDetails(new ItemStack(Material.GOLDEN_APPLE, 1),
				"" + ChatColor.GOLD + ChatColor.BOLD + "Golden Apple", "");

		ItemStack item3 = ItemHelper.addEnchant(
				ItemHelper.setDetails(new ItemStack(Material.IRON_SWORD, 1, (short) 250),
						"" + ChatColor.YELLOW + ChatColor.BOLD + "HAMMER", ChatColor.YELLOW + ""),
				Enchantment.KNOCKBACK, 10);
		item3.getDurability();

		ItemStack item4 = ItemHelper.setDetails(new ItemStack(Material.POTION, 1),
				"" + ChatColor.BLUE + ChatColor.BOLD + "Weakness Pot", "");
		Potion pot4 = new Potion(3);
		pot4.setType(PotionType.WEAKNESS);
		pot4.setSplash(true);
		pot4.apply(item4);

		ItemStack[] itemList = { item5, item, item2, item4, item3, item5, item, item2, item4, item5, item, item2,
				item4 };
		Random rand = new Random();
		int randomNum = rand.nextInt(itemList.length);

		for (Player player2 : players)
			player2.getInventory().addItem(new ItemStack(itemList[randomNum]));

		GameScoreboard();
		alivePlayers = players.size();

		for (Player player : players) {
			player.setGameMode(GameMode.ADVENTURE);
			BaseClass baseClass = classes.get(player);
			Random random = new Random();
			int chance = rand.nextInt(6);

			if (chance == 0) {
				player.sendTitle("" + baseClass.getType().getTag(), "Remember what Sensei said.. Fight smart");
			} else if (chance == 1) {
				player.sendTitle("" + baseClass.getType().getTag(), "Get out there and go savage bro");
			} else if (chance == 2) {
				player.sendTitle("" + baseClass.getType().getTag(), "Get that Vic Roy");
			} else if (chance == 3) {
				player.sendTitle("" + baseClass.getType().getTag(), "Never underestimate your enemy");
			} else if (chance == 4) {
				player.sendTitle("" + baseClass.getType().getTag(), "No Mercy...");
			} else if (chance == 5) {
				player.sendTitle("" + baseClass.getType().getTag(), "Only the strong will survive..");
			} 
		}

		BukkitRunnable runnable = new BukkitRunnable() {

			@Override
			public void run() {
				for (Entry<Player, BaseClass> playerClass : classes.entrySet())
					playerClass.getValue().Tick(gameTicks);

				gameTicks++;
			}

		};
		runnable.runTaskTimer(gameManager.getMain(), 0, 1);
		runnables.add(runnable);
		gameManager.getMain().getNPCManager().updateNpc(map);
		moveBarrier();
		Events();
		for (Player gamePlayer : players)
			sendScoreboardUpdate(gamePlayer);
	}
	
	private String shortenString(String msg, int length) {
		if (msg.length() <= length)
			return msg;
		else
			return msg.substring(0, length);
	}
	
	@SuppressWarnings("deprecation")
    public void sendScoreboardUpdate(Player player) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            Scoreboard board = pl.getScoreboard();
            Team team = board.getTeam(player.getName());
            if (team == null) {
                team = board.registerNewTeam(player.getName());
                team.addPlayer(player);
            }
            BaseClass baseClass = classes.get(player);
            String type = shortenString(baseClass.getType().getTag(), 16);
            int length = player.getName().length() + type.length();
            team.setPrefix(type);
        }
    }

	public Location getItemSpawnLoc() {
		Random rand = new Random();
		int attempts = 0;
		Location respawnLoc = GetRespawnLoc();
		while (true) {
			Location loc = respawnLoc.clone().add(rand.nextFloat() * 50 - 25, 10, rand.nextFloat() * 50 - 25);
			while (true) {
				loc.setY(loc.getY() - 1);
				Material mat = loc.getBlock().getType();
				if (mat.isSolid()) {
					return loc.add(0, 1, 0);
				}
				if (loc.getY() < 40) // Too low down without finding block
					break;
			}
			if (attempts > 500)
				return respawnLoc;
			attempts++;
		}
	}

	public void startLightningDropsTimer() {
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				Location loc = getItemSpawnLoc();
				Item item = mapWorld.dropItem(loc, getItemToDrop());
				item.setVelocity(new Vector(0, 0, 0));
				mapWorld.strikeLightningEffect(loc);

				int x = (int) loc.getX();
				int y = (int) loc.getY();
				int z = (int) loc.getZ();

				ItemStack extraLife = ItemHelper.setDetails(new ItemStack(Material.PRISMARINE_SHARD),
						"" + ChatColor.RESET + ChatColor.BOLD + "Extra Life");

				if (getItemToDrop() == extraLife) {
					TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ "An Extra Life just spawned");
				} else {
					TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ "An item just spawned at " + ChatColor.YELLOW + ChatColor.BOLD + x + ", " + y + ", " + z);
				}
			}
		};
		runnable.runTaskTimer(gameManager.getMain(), 20 * 45, 20 * 45);
		runnables.add(runnable);
	}

	public ItemStack getItemToDrop() {

		ItemStack item5 = ItemHelper.setDetails(new ItemStack(Material.POTION, 1),
				"" + ChatColor.RED + ChatColor.BOLD + "Slowness", "");
		Potion pot5 = new Potion(3);
		pot5.setType(PotionType.SLOWNESS);
		pot5.setSplash(true);
		pot5.apply(item5);

		ItemStack item = ItemHelper.setDetails(new ItemStack(Material.POTION, 1),
				"" + ChatColor.YELLOW + ChatColor.BOLD + "Instant Heal");
		Potion pot = new Potion(1);
		pot.setType(PotionType.INSTANT_HEAL);
		pot.setSplash(true);
		pot.apply(item);

		ItemStack item2 = ItemHelper.setDetails(new ItemStack(Material.POTION, 1), "" + ChatColor.GREEN + "Speed Pot");
		Potion pot2 = new Potion(1);
		pot2.setType(PotionType.SPEED);
		pot2.setSplash(true);
		pot2.apply(item2);

		ItemStack item6 = ItemHelper.addEnchant(
				ItemHelper.setDetails(new ItemStack(Material.IRON_SWORD, 1, (short) 250),
						"" + ChatColor.YELLOW + ChatColor.BOLD + "HAMMER", ChatColor.YELLOW + ""),
				Enchantment.KNOCKBACK, 10);
		item5.getDurability();

		ItemStack item7 = ItemHelper.setDetails(new ItemStack(Material.DIAMOND_HOE, 3, (short) 250),
				"" + ChatColor.LIGHT_PURPLE + "Bazooka", ChatColor.YELLOW + "");
		item5.getDurability();

		ItemStack extraLife = ItemHelper.setDetails(new ItemStack(Material.PRISMARINE_SHARD),
				"" + ChatColor.RESET + ChatColor.BOLD + "Extra Life");

		ItemStack item8 = ItemHelper.setDetails(new ItemStack(Material.POTION, 1),
				"" + ChatColor.RED + ChatColor.BOLD + "Bomb");
		Potion pot8 = new Potion(1);
		pot8.setType(PotionType.INSTANT_DAMAGE);
		pot8.setSplash(true);
		pot8.apply(item8);

		ItemStack blooper = ItemHelper.setDetails(new ItemStack(Material.RABBIT_FOOT),
				gameManager.getMain().color("&6&lBlooper"));

		List<ItemStack> items = Arrays.asList(new ItemStack(Material.GOLDEN_APPLE),
				ItemHelper.setDetails(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1),
						"" + ChatColor.BLACK + ChatColor.BOLD + "Notch Apple"),
				new ItemStack(Material.STONE), item5, item7, item7, item, item2, item5, item5, item2, item7,
				new ItemStack(Material.GOLDEN_APPLE), item6, item, extraLife, item, new ItemStack(Material.MILK_BUCKET),
				new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.MILK_BUCKET), blooper, blooper, blooper,
				blooper);
		return items.get(random.nextInt(items.size()));
	}

	public String truncateString(String string, int length) {
		if (string.length() <= length)
			return string;
		else
			return string.substring(0, length);
	}

	private Scoreboard c;
	private BukkitRunnable aBoard;
	private BukkitRunnable eventRunnable;

	public void Events() {
		if (eventRunnable == null) {
			eventRunnable = new BukkitRunnable() {
				int ticks = 0;

				@Override
				public void run() {
					if (state == GameState.STARTED) {
						//For removing arrows on ground:
						for (Entity entity : mapWorld.getEntities())
							if (entity instanceof Arrow)
								if (entity.isOnGround())
									entity.remove();
						
						//For TNT event & mob spawning
						if (ticks == 180) {
							MapInstance instance = map.GetInstance();
							for (Vector vec : instance.spawnPos) {
								TNTPrimed tnt = mapWorld.spawn(
										new Location(mapWorld, vec.getX(), vec.getY() + 30, vec.getZ()).add(0, 1, 0),
										TNTPrimed.class);
								tnt.setFuseTicks(65);
							}
							TellAll(gameManager.getMain()
									.color("&c&lHerobrine> &cMaybe you'll like my TNT instead? :)"));
							ticks = -120;
						} else if (ticks == 120) {
							MapInstance instance = map.GetInstance();
							TellAll(gameManager.getMain()
									.color("&c&lHerobrine> &cMuahaha.. Enjoy my army of Zombies & Skeletons :)"));
							for (Vector vec : instance.spawnPos) {
								Random rand = new Random();
								int chance = rand.nextInt(2);

								if (chance == 0)
									mapWorld.spawnEntity(
											new Location(mapWorld, vec.getX(), vec.getY() + 10, vec.getZ()),
											EntityType.ZOMBIE);
								else if (chance == 1)
									mapWorld.spawnEntity(
											new Location(mapWorld, vec.getX(), vec.getY() + 10, vec.getZ()),
											EntityType.SKELETON);
							}
						}
					} else {
						eventRunnable = null;
						this.cancel();
					}

					ticks++;
				}
			};
			eventRunnable.runTaskTimer(getManager().getMain(), 0, 20);
		}
	}

	public void GameScoreboard() {

		if (gameType == GameType.CTF) {
			try {

				ScoreboardManager m = Bukkit.getScoreboardManager();
				Scoreboard c = m.getNewScoreboard();

				Objective o = c.registerNewObjective("Gold", "");
				livesObjective = o;
				o.setDisplaySlot(DisplaySlot.SIDEBAR);
				o.setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + map.toString() + " " + ChatColor.RESET
						+ ChatColor.ITALIC + "CTF");

				Score gold0 = o.getScore("");

				for (Player player : players) {

					BaseClass playerClass = classes.get(player);

					// ClassType selectedClass = classSelection.get(player);
					Score livesScore = o.getScore(truncateString(
							playerClass.getType().getTag() + " " + ChatColor.WHITE + player.getName() + "", 40));
					livesScore.setScore(5);
					playerClass.score = livesScore;
				}
				for (Player player3 : players)
					player3.setScoreboard(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			try {

				ScoreboardManager m = Bukkit.getScoreboardManager();
				c = m.getNewScoreboard();

				Objective o = c.registerNewObjective("" + ChatColor.BOLD + map.toString(), "");
				livesObjective = o;
				o.setDisplaySlot(DisplaySlot.SIDEBAR);

				if (aBoard == null) {
					aBoard = new BukkitRunnable() {
						int ticks = 0;

						@Override
						public void run() {
							if (state == GameState.STARTED) {
								// WRITE SOME DEBUG MSGS TO TEST
								if (ticks == 0) {
									o.setDisplayName(
											truncateString("" + ChatColor.RESET + ChatColor.BOLD + map.toString()
													+ (gameType == GameType.FRENZY
															? "" + ChatColor.GRAY + ChatColor.ITALIC + " (frenzy)"
															: ""),
													32));
								} else if (ticks == 1) {
									o.setDisplayName(
											truncateString("" + ChatColor.YELLOW + ChatColor.BOLD + map.toString()
													+ (gameType == GameType.FRENZY
															? "" + ChatColor.GRAY + ChatColor.ITALIC + " (frenzy)"
															: ""),
													32));
								} else if (ticks == 2) {
									o.setDisplayName(
											truncateString("" + ChatColor.BLUE + ChatColor.BOLD + map.toString()
													+ (gameType == GameType.FRENZY
															? "" + ChatColor.GRAY + ChatColor.ITALIC + " (frenzy)"
															: ""),
													32));
								} else if (ticks == 3) {
									o.setDisplayName(truncateString("" + ChatColor.RED + ChatColor.BOLD + map.toString()
											+ (gameType == GameType.FRENZY
													? "" + ChatColor.GRAY + ChatColor.ITALIC + " (frenzy)"
													: ""),
											32));
								} else if (ticks == 4) {
									o.setDisplayName(
											truncateString("" + ChatColor.RESET + ChatColor.BOLD + map.toString()
													+ (gameType == GameType.FRENZY
															? "" + ChatColor.GRAY + ChatColor.ITALIC + " (frenzy)"
															: ""),
													32));
									ticks = 0;
								}
							} else {
								aBoard = null;
								this.cancel();
							}

							ticks++;
						}
					};
					aBoard.runTaskTimer(getManager().getMain(), 0, 20);
				}
				Score gold0 = o.getScore("");

				for (Player player : players) {
					BaseClass playerClass = classes.get(player);
					PlayerData data = gameManager.getMain().getDataManager().getPlayerData(player);

					if (data != null) {
						if (data.blue == 1) {
							Score livesScore = o.getScore(truncateString(
									playerClass.getType().getTag() + " " + ChatColor.BLUE + player.getName() + "", 40));
							livesScore.setScore(5);
							playerClass.score = livesScore;
						} else if (data.red == 1) {
							Score livesScore = o.getScore(truncateString(
									playerClass.getType().getTag() + " " + ChatColor.RED + player.getName() + "", 40));
							livesScore.setScore(5);
							playerClass.score = livesScore;
						} else if (data.green == 1) {
							Score livesScore = o.getScore(truncateString(
									playerClass.getType().getTag() + " " + ChatColor.GREEN + player.getName() + "",
									40));
							livesScore.setScore(5);
							playerClass.score = livesScore;
						} else if (data.yellow == 1) {
							Score livesScore = o.getScore(truncateString(
									playerClass.getType().getTag() + " " + ChatColor.YELLOW + player.getName() + "",
									40));
							livesScore.setScore(5);
							playerClass.score = livesScore;
						} else {
							Score livesScore = o.getScore(truncateString(
									playerClass.getType().getTag() + " " + ChatColor.WHITE + player.getName() + "",
									40));
							livesScore.setScore(5);
							playerClass.score = livesScore;
						}
					}
				}
				for (Player player3 : players)
					player3.setScoreboard(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setGameScore(Player player) {
		// this.GameScoreboard();
		player.setScoreboard(c);
	}

	public String testDisplay(Player player) {
		String tag = "";
		Player p = player.getPlayer();

		return tag = gameManager.getMain().getRankManager().getRank(p).getTagWithSpace();
	}

	public int aliveGamePlayers = 0;

	public void CheckForWin() {
		int alivePlayers = aliveGamePlayers;
		Player alivePlayer = null;
		for (Entry<Player, BaseClass> entry : classes.entrySet())
			if (entry.getValue().getLives() > 0) {
				alivePlayers++;
				alivePlayer = entry.getKey();
				// gameManager.getMain().getNPCManager().updateNpc(map);
			}
		if (alivePlayers == 1 && alivePlayer != null) {
			WinGame(alivePlayer);
		} else if (alivePlayers <= 0)
			EndGame();

	}

	public void PlayerDeath(Player player) {
		this.blindness = 0;
		if (gameType == GameType.FRENZY) {
			rerandomizeClass(player);
		}
		BaseClass baseClass = classes.get(player);
		if (baseClass != null) {
			try {
				player.getInventory().clear();
				baseClass.PlayerDeath();

				BukkitRunnable runTimer = new BukkitRunnable() {

					int ticks = 3;

					@Override
					public void run() {
						if (state == GameState.ENDED) {
							this.cancel();
							player.setGameMode(GameMode.ADVENTURE);
							player.setAllowFlight(true);
						}

						if (baseClass.getLives() > 0) {
							if (ticks == 0) {
								player.teleport(GetRespawnLoc());
								player.setGameMode(GameMode.ADVENTURE);
								player.setHealth(20.0);
								player.setAllowFlight(true);
								if (gameType == GameType.FRENZY) {
									player.sendTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "Respawned",
											"" + ChatColor.RESET + "Your new class for this life is "
													+ baseClass.getType().getTag());
								} else {
									player.sendTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "Respawned", "");
								}
								baseClass.isDead = false;
								this.cancel();
							} else if (ticks <= 3 && state == GameState.STARTED) {
								player.sendTitle("", "" + ChatColor.RED + ticks);
								player.setGameMode(GameMode.SPECTATOR);
							}

							if (ticks == 3) {
								baseClass.isDead = true;
							}
						}
						ticks--;
					}
				};
				runTimer.runTaskTimer(gameManager.getMain(), 0, 20);
				runnables.add(runTimer);

				if (player.getLastDamageCause() != null && player.getLastDamageCause().getCause() == DamageCause.VOID) {
					if (player.getKiller() != null) {
						player.teleport(player.getKiller());
					} else {
						player.teleport(GetRespawnLoc());
					}
				}

				player.setHealth(20.0);
				player.setAllowFlight(true);
				player.setGameMode(GameMode.ADVENTURE);
				if (baseClass.getLives() == 0) {
					playerPosition.add(player);
					if (players.size() > 2) {
						player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You are now spectating on " + ChatColor.GREEN + map.toString());
						player.sendTitle("" + ChatColor.RED + "You have died!",
								"" + ChatColor.RESET + "You are now a Spectator");
						player.teleport(GetSpecLoc());
					}
					player.getPlayer().setGameMode(GameMode.SPECTATOR);
					alivePlayers--;
					try {
						baseClass.score.getScoreboard().resetScores(baseClass.score.getEntry());
					} catch (Exception e) {
						e.printStackTrace();
					}
					CheckForWin();
				} else
					baseClass.LoadPlayer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void EndGameAnimation(Player winner) {
		for (Player player : players) {
			player.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "GAME LOST",
					"" + winner.getName() + ChatColor.GREEN + " won the game!");
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
			player.setDisplayName("" + player.getName());
			winner.setDisplayName("" + winner.getName());
		}
		winner.sendTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "VICTORY",
				"" + ChatColor.GREEN + "You won the game!");
	}

	public void EndGame() {
		state = GameState.ENDED;
		gameManager.getMain().getNPCManager().updateRandomNpc();
		for (Entity en : mapWorld.getEntities()) {
			if (!(en instanceof Player)) {
				en.remove();
			}
		}

		for (Player spectator : spectators) {
			gameManager.getMain().LobbyBoard(spectator);
			gameManager.getMain().LobbyItems(spectator);
			gameManager.getMain().SendPlayerToHub(spectator);
			spectator.setGameMode(GameMode.ADVENTURE);
			spectator.setAllowFlight(true);
			spectator.setDisplayName("" + spectator.getName());
		}
		TellSpec(
				"" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + "The game on "
						+ ChatColor.BOLD + map.toString() + ChatColor.RESET + " has ended. Moving you back to spawn");

		for (Player player : players) {
			player.teleport(gameManager.getMain().GetSpawnLocation());
			SetLobbyScoreboard();
			player.setHealth(20.0);
			player.setGameMode(GameMode.ADVENTURE);
			player.getInventory().clear();
			player.removePotionEffect(PotionEffectType.SPEED);
			player.removePotionEffect(PotionEffectType.WEAKNESS);
			player.removePotionEffect(PotionEffectType.POISON);
			player.removePotionEffect(PotionEffectType.ABSORPTION);
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			player.removePotionEffect(PotionEffectType.FAST_DIGGING);
			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			player.removePotionEffect(PotionEffectType.HARM);
			player.removePotionEffect(PotionEffectType.HEAL);
			player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			player.removePotionEffect(PotionEffectType.HUNGER);
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.removePotionEffect(PotionEffectType.JUMP);
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player.removePotionEffect(PotionEffectType.REGENERATION);
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.WATER_BREATHING);
			player.removePotionEffect(PotionEffectType.WITHER);

			removeArmor(player);
			gameManager.getMain().LobbyItems(player);
		}

		for (BukkitRunnable runnable : runnables)
			runnable.cancel();
		gameManager.RemoveMap(this.map);
	}

	public void SetLobbyScoreboard() {
		for (Player player : players) {
			gameManager.getMain().LobbyBoard(player);
		}
	}

	public void givePositionTokens(int position, int tokens) {
		if (playerPosition.size() >= position) {
			Player player = playerPosition.get(playerPosition.size() - position);
			PlayerData data = gameManager.getMain().getDataManager().getPlayerData(player);
			BaseClass baseClass = classes.get(player);

			if (baseClass == null) {
				player.sendMessage("Your base class is fackin null");
			} else if (data != null) {
				data.tokens += tokens;
				baseClass.totalTokens += tokens;
			}
		}
	}

	public void givePositionNum(int position, String message) {
		if (playerPosition.size() >= position) {
			Player player = playerPosition.get(playerPosition.size() - position);
			player.sendMessage("" + ChatColor.BOLD + message);
		}
	}

	public void givePoints(int position, int points) {
		if (playerPosition.size() >= position) {
			Player player = playerPosition.get(playerPosition.size() - position);
			PlayerData data = gameManager.getMain().getDataManager().getPlayerData(player);

			if (data != null) {
				data.points += points;
			}
		}
	}

	public void WinGame(Player winner) {
		playerPosition.add(winner);
		int posNum = 1;
		PlayerData data3 = gameManager.getMain().getDataManager().getPlayerData(winner);

		if (data3.exp >= 5000) {
			data3.level++;
			data3.exp -= 5000;
			winner.sendMessage("Level upgraded to " + data3.level + "!");
		}

		for (Player gamePlayer : players) {
			if (gamePlayer.hasPermission("scb.bonusTokens")) {
				PlayerData data = gameManager.getMain().getDataManager().getPlayerData(gamePlayer);
				data.tokens += 10;
				BaseClass baseClass = classes.get(gamePlayer);
				baseClass.totalTokens += 10;
			}
		}

		givePositionTokens(1, 10);
		givePositionTokens(2, 7);
		givePositionTokens(3, 5);
		givePositionTokens(4, 3);
		givePositionTokens(5, 1);

		if (getManager().getMain().tournament == true) {
			givePoints(1, 10);
			givePoints(2, 7);
			givePoints(3, 5);
			givePoints(4, 1);
		}

		winner.getInventory().clear();
		winner.setGameMode(GameMode.ADVENTURE);
		for (Player player : players) {
			gameManager.getMain().sendScoreboardUpdate(player);
			if (player == winner) {
				BaseClass baseClass = classes.get(winner);
				if (baseClass != null) {
					if (baseClass.getLives() < 5) {
						PlayerData data = gameManager.getMain().getDataManager().getPlayerData(winner);
						data.wins += 1;
						data.winstreak += 1;
						data.exp += 113;
						baseClass.totalExp += 113;

						winner.sendMessage("" + ChatColor.BOLD + "=====================");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "|| " + "        " + ChatColor.YELLOW + ChatColor.BOLD
								+ "  GAME WON");
						givePositionNum(1, "        " + "  1st Place: 10 Tokens");

						if (baseClass.totalKills >= 0)
							player.sendMessage("" + ChatColor.BOLD + "|| " + "        " + ChatColor.BLUE
									+ ChatColor.BOLD + "  " + baseClass.totalKills + " Kills: " + ChatColor.RESET
									+ ChatColor.YELLOW + baseClass.totalKills + " Tokens");

						if (winner.hasPermission("scb.rankBonusOne")) {
							winner.sendMessage(
									"" + ChatColor.BOLD + "|| " + "        " + ChatColor.BLUE + ChatColor.BOLD
											+ "  RANK BONUS: " + ChatColor.RESET + ChatColor.YELLOW + "10 Tokens");
						}
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "=====================");
					} else {
						PlayerData data = gameManager.getMain().getDataManager().getPlayerData(winner);
						data.tokens += 10;
						baseClass.totalTokens += 10;
						data.wins += 1;
						data.flawlessWins += 1;
						data.winstreak += 1;
						data.exp += 133;
						baseClass.totalExp += 133;

						winner.sendMessage("" + ChatColor.BOLD + "=====================");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "|| " + "        " + ChatColor.YELLOW + ChatColor.BOLD
								+ "  GAME WON");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						givePositionNum(1, "        " + "  1st Place: 10 Tokens");

						if (baseClass.totalKills >= 0)
							player.sendMessage("" + ChatColor.BOLD + "|| " + "        " + ChatColor.BLUE
									+ ChatColor.BOLD + "  " + baseClass.totalKills + " Kills: " + ChatColor.RESET
									+ ChatColor.YELLOW + baseClass.totalKills + " Tokens");

						winner.sendMessage("" + ChatColor.BOLD + "|| " + "        " + ChatColor.BOLD
								+ "  Flawless Win: " + ChatColor.RESET + ChatColor.YELLOW + "10 Tokens");
						if (winner.hasPermission("scb.rankBonus")) {
							winner.sendMessage(
									"" + ChatColor.BOLD + "|| " + "        " + ChatColor.BLUE + ChatColor.BOLD
											+ "  RANK BONUS: " + ChatColor.RESET + ChatColor.YELLOW + "10 Tokens");
						}
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "||");
						winner.sendMessage("" + ChatColor.BOLD + "=====================");

						winner.getInventory().clear();
						winner.setGameMode(GameMode.ADVENTURE);
					}
				}
			} else {
				int pos = playerPosition.size() - playerPosition.indexOf(player);
				PlayerData data = gameManager.getMain().getDataManager().getPlayerData(player);
				data.losses += 1;
				data.winstreak = 0;

				player.sendMessage("" + ChatColor.BOLD + "=====================");
				player.sendMessage("" + ChatColor.BOLD + "||");
				player.sendMessage("" + ChatColor.BOLD + "||");
				player.sendMessage(
						"" + ChatColor.BOLD + "|| " + "        " + ChatColor.RED + ChatColor.BOLD + "  GAME LOST");
				player.sendMessage("" + ChatColor.BOLD + "||");

				/*
				 * if (posNum == 1) { posNum++; givePositionNum(posNum, "        " +
				 * "  2nd Place: 7 Tokens"); } if (posNum == 2) { posNum++;
				 * givePositionNum(posNum, "        " + "  3rd Place: 5 Tokens"); } if (posNum
				 * == 3) { posNum++; givePositionNum(posNum, "        " +
				 * "  4th Place: 3 Tokens"); } if (posNum == 4) { posNum++;
				 * givePositionNum(posNum, "        " + "  5th Place: 1 Token"); } if (posNum ==
				 * 5) { posNum++; givePositionNum(posNum, "        " +
				 * "  Last Place: 0 Tokens"); }
				 */

				givePositionNum(2, "        " + "  2nd Place: 7 Tokens");
				givePositionNum(3, "        " + "  3rd Place: 5 Tokens");
				givePositionNum(4, "        " + "  4th Place: 3 Tokens");
				givePositionNum(5, "        " + "  5th Place: 1 Token");
				givePositionNum(6, "        " + "  Last Place: 0 Tokens");

				BaseClass baseClass = classes.get(player);

				if (baseClass.totalKills >= 0)
					player.sendMessage("" + ChatColor.BOLD + "|| " + "        " + ChatColor.BLUE + ChatColor.BOLD + "  "
							+ baseClass.totalKills + " Kills: " + ChatColor.RESET + ChatColor.YELLOW
							+ baseClass.totalKills + " Tokens");

				if (player.hasPermission("scb.rankBonusOne")) {
					player.sendMessage("" + ChatColor.BOLD + "|| " + "        " + ChatColor.BLUE + ChatColor.BOLD
							+ "  RANK BONUS: " + ChatColor.RESET + ChatColor.YELLOW + "10 Tokens");
				}
				player.sendMessage("" + ChatColor.BOLD + "||");
				player.sendMessage("" + ChatColor.BOLD + "||");
				player.sendMessage("" + ChatColor.BOLD + "||");
				player.sendMessage("" + ChatColor.BOLD + "=====================");
			}

			BaseClass baseClass = classes.get(player);
			BaseClass baseClass2 = classes.get(winner);

			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have earned "
					+ baseClass.totalTokens + " Tokens!");

			player.getInventory().clear();
			player.removePotionEffect(PotionEffectType.SPEED);
			player.removePotionEffect(PotionEffectType.WEAKNESS);
			player.removePotionEffect(PotionEffectType.POISON);
			player.removePotionEffect(PotionEffectType.ABSORPTION);
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			player.removePotionEffect(PotionEffectType.FAST_DIGGING);
			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			player.removePotionEffect(PotionEffectType.HARM);
			player.removePotionEffect(PotionEffectType.HEAL);
			player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			player.removePotionEffect(PotionEffectType.HUNGER);
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.removePotionEffect(PotionEffectType.JUMP);
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player.removePotionEffect(PotionEffectType.REGENERATION);
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.WATER_BREATHING);
			player.removePotionEffect(PotionEffectType.WITHER);
			player.setGameMode(GameMode.ADVENTURE);
		}
		EndGameAnimation(winner);
		EndGame();
		SetLobbyScoreboard();

		for (Player player2 : players) {
			player2.getInventory().clear();
			player2.removePotionEffect(PotionEffectType.SPEED);
			player2.removePotionEffect(PotionEffectType.WEAKNESS);
			player2.removePotionEffect(PotionEffectType.POISON);
			player2.removePotionEffect(PotionEffectType.ABSORPTION);
			player2.removePotionEffect(PotionEffectType.BLINDNESS);
			player2.removePotionEffect(PotionEffectType.CONFUSION);
			player2.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			player2.removePotionEffect(PotionEffectType.FAST_DIGGING);
			player2.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			player2.removePotionEffect(PotionEffectType.HARM);
			player2.removePotionEffect(PotionEffectType.HEAL);
			player2.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			player2.removePotionEffect(PotionEffectType.HUNGER);
			player2.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player2.removePotionEffect(PotionEffectType.INVISIBILITY);
			player2.removePotionEffect(PotionEffectType.JUMP);
			player2.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player2.removePotionEffect(PotionEffectType.REGENERATION);
			player2.removePotionEffect(PotionEffectType.SLOW);
			player2.removePotionEffect(PotionEffectType.WATER_BREATHING);
			player2.removePotionEffect(PotionEffectType.WITHER);

			player2.setGameMode(GameMode.ADVENTURE);
		}
		EndGameAnimation(winner);
		EndGame();
		SetLobbyScoreboard();

		BaseClass baseClass = classes.get(winner);
		String tag = gameManager.getMain().getRankManager().getRank(winner).getTagWithSpace();
		PlayerData data = gameManager.getMain().getDataManager().getPlayerData(winner);

		if (baseClass.getLives() >= 5) {
			if (winner.hasPermission("scb.customWin")) {
				if (data.cwm == 1) {
					customFlawWinMsg(winner);
				} else {
					Bukkit.broadcastMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW
							+ winner.getName() + ChatColor.WHITE + " just " + ChatColor.BOLD + "FLAWLESSLY "
							+ ChatColor.RESET + "won on " + ChatColor.BOLD + ChatColor.WHITE + ChatColor.YELLOW
							+ ChatColor.BOLD + map.toString());
				}
			} else {
				Bukkit.broadcastMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW
						+ winner.getName() + ChatColor.WHITE + " just " + ChatColor.BOLD + "FLAWLESSLY "
						+ ChatColor.RESET + "won on " + ChatColor.BOLD + ChatColor.WHITE + ChatColor.YELLOW
						+ ChatColor.BOLD + map.toString());
			}
		} else {
			if (winner.hasPermission("scb.customWin")) {
				if (data.cwm == 1) {
					customWinMsg(winner);
				} else {
					Bukkit.broadcastMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW
							+ winner.getName() + ChatColor.WHITE + " just won on " + ChatColor.BOLD + ChatColor.WHITE
							+ ChatColor.YELLOW + ChatColor.BOLD + map.toString());
				}
			} else {
				Bukkit.broadcastMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW
						+ winner.getName() + ChatColor.WHITE + " just won on " + ChatColor.BOLD + ChatColor.WHITE
						+ ChatColor.YELLOW + ChatColor.BOLD + map.toString());
			}
		}

	}

	private void customWinMsg(Player winner) {
		Random rand = new Random();
		int chance = rand.nextInt(3);
		String tag = gameManager.getMain().getRankManager().getRank(winner).getTagWithSpace();

		if (chance == 0) {
			Bukkit.broadcastMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW
					+ winner.getName() + ChatColor.WHITE + " got a Victory Royale on " + ChatColor.BOLD
					+ ChatColor.WHITE + ChatColor.YELLOW + ChatColor.BOLD + map.toString());
		} else if (chance == 1) {
			Bukkit.broadcastMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW
					+ winner.getName() + ChatColor.WHITE + " just showed the entire lobby who's boss on "
					+ ChatColor.BOLD + ChatColor.WHITE + ChatColor.YELLOW + ChatColor.BOLD + map.toString());
		} else if (chance == 2) {
			Bukkit.broadcastMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW
					+ winner.getName() + ChatColor.WHITE + " just won on " + ChatColor.BOLD + ChatColor.WHITE
					+ ChatColor.YELLOW + ChatColor.BOLD + map.toString());
		}
	}

	private void customFlawWinMsg(Player winner) {
		Random rand = new Random();
		int chance = rand.nextInt(2);
		String tag = gameManager.getMain().getRankManager().getRank(winner).getTagWithSpace();

		if (chance == 0) {
			Bukkit.broadcastMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW
					+ winner.getName() + ChatColor.WHITE + " just " + ChatColor.BOLD + "ABSOLUTELY DESTROYED "
					+ ChatColor.RESET + "everyone on " + ChatColor.BOLD + ChatColor.WHITE + ChatColor.YELLOW
					+ ChatColor.BOLD + map.toString());
		} else if (chance == 1) {
			Bukkit.broadcastMessage(
					"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + tag + ChatColor.YELLOW + winner.getName()
							+ ChatColor.WHITE + " just " + ChatColor.BOLD + "FLAWLESSLY " + ChatColor.RESET + "won on "
							+ ChatColor.BOLD + ChatColor.WHITE + ChatColor.YELLOW + ChatColor.BOLD + map.toString());
		}
	}

	// public BaseClass oldBaseClass; // Public because we want to use this variable
	// in BaseClass.java

	private void rerandomizeClass(Player player) {
		BaseClass baseClass = classes.get(player);

		if (baseClass.getLives() > 1) {
			ClassType classType = ClassType.values()[random.nextInt(ClassType.values().length)];
			BaseClass newBaseClass = classType.GetClassInstance(this, player);
			BaseClass oldBaseClass = classes.get(player);
			oldClasses.put(player, oldBaseClass);

			Score newScore = livesObjective
					.getScore(truncateString(classType.getTag() + " " + ChatColor.WHITE + player.getName() + "", 40));
			newBaseClass.lives = oldBaseClass.lives;
			newBaseClass.tokens = oldBaseClass.tokens;
			newBaseClass.score = newScore;

			oldBaseClass.score.getScoreboard().resetScores(oldBaseClass.score.getEntry());

			classes.put(player, newBaseClass);

			player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
					+ "Your class has been randomly selected to " + classType.getTag());

			if (player.hasPermission("scb.chat"))
				player.setDisplayName("" + player.getName() + " " + classType.getTag());
			else
				player.setDisplayName("" + player.getName() + " " + classType.getTag() + ChatColor.GRAY);
		}
	}

	private void LoadClasses() {
		Random rand = new Random(); // for random class;
		int attempts = 0;

		for (Player player : players) {
			PlayerData playerData = gameManager.getMain().getDataManager().getPlayerData(player);
			ClassType selectedClass = gameType != GameType.FRENZY ? classSelection.get(player) : null;
			if (selectedClass == null) {
				selectedClass = ClassType.values()[rand.nextInt(ClassType.values().length)];
				if (gameType != GameType.FRENZY) {
					while (attempts <= 100) {
						attempts++;
						ClassType classType = ClassType.values()[rand.nextInt(ClassType.values().length)];
						Rank donor = classType.getMinRank();

						if (playerData.playerClasses.get(classType.getID()) != null
								&& playerData.playerClasses.get(classType.getID()).purchased
								|| classType.getTokenCost() == 0) {
							if (donor == null || player.hasPermission("scb." + donor.toString().toLowerCase())) {
								selectedClass = classType;
								break;
							}
						}
					}
				}
			}

			BaseClass baseClass = selectedClass.GetClassInstance(this, player);

			if (player.hasPermission("scb.chat"))
				player.setDisplayName("" + player.getName() + " " + selectedClass.getTag());
			else
				player.setDisplayName("" + player.getName() + " " + selectedClass.getTag() + ChatColor.GRAY);
			classes.put(player, baseClass);
			player.setHealth(20.0);
			player.setFoodLevel(20);
			player.setGameMode(GameMode.ADVENTURE);
			player.setAllowFlight(true);
			baseClass.LoadPlayer();
		}
	}

	// Sends msg to all players (inside the ArrayList)
	public void TellAll(String message) {
		for (Player player : players) {
			player.sendMessage(message);
		}
	}

	// Sends msg to all spectators (inside the ArrayList)
	public void TellSpec(String message) {
		for (Player spectator : spectators) {
			spectator.sendMessage(message);
		}
	}

	public boolean HasPlayer(Player player) {
		return players.contains(player);
	}

	public boolean HasSpectator(Player spectator) {
		return spectators.contains(spectator);
	}

	public boolean RemovePlayer(Player player) {
		BaseClass baseClass = classes.remove(player);
		playerPosition.remove(player);
		// spectators.remove(player);
		if (players.remove(player)) {
			player.setDisplayName("" + player.getName());
			try {
				if (baseClass != null) {
					baseClass.score.getScoreboard().resetScores(baseClass.score.getEntry());
					PlayerData data = gameManager.getMain().getDataManager().getPlayerData(player);
					data.losses += 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Stop timer if out of players whilst waiting
			if (state == GameState.WAITING) {
				for (Player gamePlayer : players) {
					FastBoard board = boards.get(gamePlayer);
					board.updateLine(7,
							" " + (map.GetInstance().gameType == GameType.FRENZY
									? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
									: "")
									+ (map.GetInstance().gameType == GameType.NORMAL
											? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
											: "")
									+ (map.GetInstance().gameType == GameType.DUEL
											? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
											: "")
									+ (map.GetInstance().gameType == GameType.CTF
											? "" + ChatColor.RESET + players.size() + "/12"
											: ""));
				}

				TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + player.getName()
						+ ChatColor.RED + " left " + ChatColor.RED + "(" + ChatColor.GREEN
						+ (map.GetInstance().gameType == GameType.FRENZY
								? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
								: "")
						+ (map.GetInstance().gameType == GameType.NORMAL
								? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
								: "")
						+ (map.GetInstance().gameType == GameType.DUEL
								? "" + ChatColor.RESET + players.size() + "/" + gameType.getMaxPlayers()
								: "")
						+ (map.GetInstance().gameType == GameType.CTF ? "" + ChatColor.RESET + players.size() + "/12"
								: "")
						+ ChatColor.RED + ")");

				if (players.size() < MIN_STARTING_PLAYERS) {
					state = GameState.WAITING;
					if (gameStartTime != null) {
						gameStartTime.cancel();
						gameStartTime = null;
						TellAll("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "Game start cancelled, not enough players!");

						for (Player gamePlayer : players) {
							if (gamePlayer.getInventory().contains(paper))
								gamePlayer.getInventory().remove(paper);

							FastBoard board = boards.get(gamePlayer);
							board.updateLine(
									7, " "
											+ (map.GetInstance().gameType == GameType.FRENZY
													? "" + ChatColor.RESET + players.size() + "/"
															+ gameType.getMaxPlayers()
													: "")
											+ (map.GetInstance().gameType == GameType.NORMAL
													? "" + ChatColor.RESET + players.size() + "/"
															+ gameType.getMaxPlayers()
													: "")
											+ (map.GetInstance().gameType == GameType.DUEL
													? "" + ChatColor.RESET + players.size() + "/"
															+ gameType.getMaxPlayers()
													: "")
											+ (map.GetInstance().gameType == GameType.CTF
													? "" + ChatColor.RESET + players.size() + "/12"
													: ""));
							board.updateLine(10, " " + ChatColor.ITALIC + "Waiting..");
						}

					}
				}
			}

			// If game has started stop game instead
			if (state == GameState.STARTED && players.size() < MIN_PLAYERS) {
				TellAll("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.GREEN + player.getName()
						+ ChatColor.RESET + ChatColor.RED + " has left the game!");
				try {
					baseClass.score.getScoreboard().resetScores(baseClass.score.getEntry());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (players.size() == 1) {
					WinGame(players.get(0));
				} else
					EndGame();
				SetLobbyScoreboard();
				RemovePlayer(player);

			}

			/*
			 * if (state == GameState.STARTED) { PlayerData data =
			 * gameManager.getMain().getDataManager().getPlayerData(player); data.winstreak
			 * = 0; }
			 */

			return true;
		}

		return false;

	}

	public boolean PlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (players.contains(player)) {
			if (classes.containsKey(player)) {
				BaseClass baseClass = classes.get(player);
				if (event.getPlayer().getItemInHand().getType() == Material.ENDER_PEARL
						&& (event.getAction() == Action.RIGHT_CLICK_AIR
								|| event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
					if (baseClass.pearlTimer.getTime() < 5000) {
						int seconds = (5000 - baseClass.pearlTimer.getTime()) / 1000 + 1;
						event.setCancelled(true);
						player.sendMessage(
								"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have to wait " + ChatColor.YELLOW
										+ seconds + " seconds " + ChatColor.RESET + "to use this item again");
					} else {
						baseClass.pearlTimer.restart();
					}
				}
				baseClass.UseItem(event);
			}
			return true;
		}
		return false;
	}
}
