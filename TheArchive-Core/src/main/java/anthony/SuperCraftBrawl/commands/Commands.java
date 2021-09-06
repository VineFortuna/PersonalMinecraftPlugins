package anthony.SuperCraftBrawl.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.Lists;

import anthony.Duels.game.maps.DuelMaps;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.GameState;
import anthony.SuperCraftBrawl.Game.GameType;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.map.Maps;
import anthony.SuperCraftBrawl.playerdata.ClassDetails;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import anthony.SuperCraftBrawl.ranks.Rank;

public class Commands implements CommandExecutor, TabCompleter {

	private final Main main;
	public List<Player> players;

	public Commands(Main main) {
		this.main = main;
	}

	public void TellAll(String message) {
		for (Player player : players) {
			player.sendMessage(message);
		}
	}

	public void removeArmor(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
		player.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
		player.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
		player.getInventory().setBoots(new ItemStack(Material.AIR, 1));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		if (sender instanceof Player) {
			switch (cmd.getName().toLowerCase()) {
			case "test":
				sender.sendMessage("test v1.0");
				break;
			case "purchases":
				if (args.length > 0) {
					switch (args[0].toLowerCase()) {
					case "get":
						PlayerData data = main.getDataManager().getPlayerData(player);
						player.sendMessage("Listing purchases...");

						int size = 0;
						for (Entry<Integer, ClassDetails> entry : data.playerClasses.entrySet()) {
							player.sendMessage(" - " + entry.getKey() + ": " + entry.getValue().toString());
							size++;
						}

						if (size == 0)
							player.sendMessage("You have no Class Stats");
						break;
					case "buy":
						if (args.length > 1) {
							int classID = Integer.parseInt(args[1]);
							PlayerData playerData = main.getDataManager().getPlayerData(player);
							ClassDetails details = playerData.playerClasses.get(classID);

							if (details == null) {
								details = new ClassDetails();
								playerData.playerClasses.put(classID, details);
							}
							details.setPurchased();
							player.sendMessage("Class Purchased!");
						} else
							player.sendMessage("Not enough arguments");

					}
				} else {
					player.sendMessage("Not enough arguments!");
				}
				break;
			case "startgame":
				GameInstance instance2 = main.getGameManager().GetInstanceOfPlayer(player);

				if (instance2 != null) {
					if (player.hasPermission("scb.startGame")) {
						if (instance2.state == GameState.WAITING && instance2.players.size() >= 2) {
							instance2.TellAll("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
									+ ChatColor.RESET + "Game has been force started by " + ChatColor.YELLOW
									+ player.getName());
							instance2.ticksTilStart = 0;
						} else if (instance2.state == GameState.WAITING && !(instance2.players.size() >= 2)) {
							player.sendMessage(
									"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Not enough players to start!");
						} else if (instance2.state == GameState.STARTED) {
							sender.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
									+ ChatColor.RESET + "Game is already in progress!");
						}
					} else {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You do not have permission to use this command!");
					}
				} else {
					player.sendMessage(
							"" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You are not in a game");
				}
				break;

			case "setlives":
				GameInstance instance22 = main.getGameManager().GetInstanceOfPlayer(player);
				BaseClass baseClass = instance22.classes.get(player);

				if (player.hasPermission("scb.setLives")) {
					if (args.length > 0) {
						Player target = Bukkit.getServer().getPlayerExact(args[0]);
						GameInstance i = main.getGameManager().GetInstanceOfPlayer(target);
						BaseClass baseClass2 = i.classes.get(target);
						int num = Integer.parseInt(args[1]);

						if (i.state == GameState.STARTED) {
							if (i != null) {
								if (target != null) {
									baseClass2.lives = num;
									player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD
											+ "(!) " + ChatColor.RESET + "You have set " + ChatColor.YELLOW
											+ target.getName() + ChatColor.RESET + "'s lives to " + ChatColor.YELLOW
											+ num);
									baseClass2.score.setScore(baseClass2.lives);
								} else {
									player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD
											+ "(!) " + ChatColor.RESET + "Please specify a player!");
								}
							} else {
								player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
										+ ChatColor.RESET + "This player is not in a game!");
							}
						} else {
							player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
									+ ChatColor.RESET + "Game must be started in order to use this!");
						}
					}
				}
				break;

			case "setrank":
				if (player.hasPermission("scb.setrank")) {
					if (args.length > 1) {
						Rank rank = Rank.getRankFromName(args[1]);
						Player target = Bukkit.getServer().getPlayerExact(args[0]);

						if (target != null) {
							main.getRankManager().setRank(target, rank);
							String temp = "" + main.getRankManager().getRank(target);
							String temp2 = temp.toUpperCase();
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + target.getName()
									+ "'s rank was set to " + ChatColor.YELLOW + temp2);
							target.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "Your rank has been set to " + ChatColor.YELLOW + temp2);
							target.kickPlayer("Your rank has been set to " + ChatColor.YELLOW + temp2);
						} else {
							player.sendMessage(
									"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This player is not online!");
						}
					} else {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "Incorrect usage! Try doing: " + ChatColor.GREEN + "/setrank <player> <rank>");
					}
				}
				break;
			case "join":

				if (args.length > 0) {
					String mapName = args[0];

					Maps map = null;
					for (Maps maps : Maps.values()) {
						if (maps.toString().equalsIgnoreCase(mapName)) {
							map = maps;
							break;
						}
					}
					if (map != null) {
						main.getGameManager().JoinMap(player, map);

					} else {
						player.sendMessage(ChatColor.BOLD + "(!) " + ChatColor.RESET + "This map does not exist! Use "
								+ ChatColor.YELLOW + "/maplist " + ChatColor.RESET + "for a list of playable maps");
					}

				} else
					player.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ "Incorrect usage! Try doing: " + ChatColor.RESET + ChatColor.GREEN + "/join <mapname>");

				return true;

			case "duel":
				if (args.length > 0) {
					String mapName = args[0];
					DuelMaps map = null;

					for (DuelMaps maps : DuelMaps.values()) {
						if (maps.toString().equalsIgnoreCase(mapName)) {
							map = maps;
							break;
						}
					}

					if (map != null) {
						main.getDuelManager().JoinGame(player, map);
					} else {
						player.sendMessage(ChatColor.BOLD + "(!) " + ChatColor.RESET + "This map does not exist!");
					}
				}
				return true;

			case "spectate":
				if (sender instanceof Player) {
					if (args.length > 0) {
						String mapName = args[0];
						Maps map = null;

						for (Maps maps : Maps.values()) {
							if (maps.toString().equalsIgnoreCase(mapName)) {
								map = maps;
								break;
							}
						}

						if (map != null) {
							main.getGameManager().SpectatorJoinMap(player, map);
						} else {
							player.sendMessage(
									ChatColor.BOLD + "(!) " + ChatColor.RESET + "This map does not exist! Use "
											+ ChatColor.YELLOW + "/maplist " + ChatColor.RESET + "for a list of maps");
						}
					} else {
						player.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "Incorrect usage! Try doing: " + ChatColor.RESET + ChatColor.GREEN
								+ "/spectate <mapname>");
					}
				}
				return true;

			case "class":
				PlayerData playerData = main.getDataManager().getPlayerData(player);
				if (sender instanceof Player) {
					if (args.length > 0) {
						String className = args[0];
						for (ClassType type : ClassType.values())
							if (className.equalsIgnoreCase(type.toString())) {
								if (playerData.playerClasses.get(type.getID()) != null
										&& playerData.playerClasses.get(type.getID()).purchased
										|| type.getTokenCost() == 0) {
									Rank donor = type.getMinRank();

									if (donor == null
											|| sender.hasPermission("scb." + donor.toString().toLowerCase())) {
										GameInstance game = main.getGameManager().GetInstanceOfPlayer((Player) sender);
										if (game != null) {
											if (game.state == GameState.WAITING) {
												if (game.gameType == GameType.FRENZY) {
													player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
															+ "(!) " + ChatColor.RESET
															+ "You cannot select a class in a Frenzy game");
												} else {
													player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
															+ "==============================================");
													player.sendMessage(
															"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
													player.sendMessage(
															"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
													player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
															+ "|| " + ChatColor.RESET + ChatColor.YELLOW
															+ ChatColor.BOLD + "Selected Class: " + type.getTag());
													player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
															+ "|| " + ChatColor.RESET + ChatColor.YELLOW
															+ ChatColor.BOLD + "Class Desc: " + ChatColor.RESET
															+ ChatColor.YELLOW + type.getClassDesc());
													player.sendMessage(
															"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
													player.sendMessage(
															"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "|| ");
													player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD
															+ "==============================================");
													main.getGameManager().playerSelectClass((Player) sender, type);
												}
											} else {
												sender.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN
														+ ChatColor.BOLD + "(!) " + ChatColor.RESET
														+ "You cannot select a class while in a game!");
											}
										} else {
											sender.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN
													+ ChatColor.BOLD + "(!) " + ChatColor.RESET
													+ "You have to be in a game to select a class!");
										}
										return true;
									} else {
										sender.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD
												+ "(!) " + ChatColor.RESET
												+ "Stop tryna cheat the systemmmmm!! You need a rank to use this class");
										return true;
									}
								} else {
									sender.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD
											+ "(!) " + ChatColor.RESET + "You do not have this class unlocked");
									return true;
								}
							} else if (className.equalsIgnoreCase("random")) {
								GameInstance instance20 = main.getGameManager().GetInstanceOfPlayer((Player) sender);
								Random random = new Random();
								ClassType classType = ClassType.values()[random.nextInt(ClassType.values().length)];

								if (playerData.playerClasses.get(classType.getID()) != null
										&& playerData.playerClasses.get(classType.getID()).purchased
										|| classType.getTokenCost() == 0) {
									Rank donor = type.getMinRank();

									if (donor == null
											|| sender.hasPermission("scb." + donor.toString().toLowerCase())) {

										sender.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD
												+ "(!) " + ChatColor.RESET + "You have selected to go a Random class");
										main.getGameManager().playerSelectClass((Player) sender, classType);
										instance20.board.updateLine(4, " " + "Random");
										((Player) sender).setDisplayName("" + sender.getName());
										return true;
									}
								}
							}

						sender.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This class doesn't exist! "
								+ "Use " + ChatColor.GREEN + "/classes " + ChatColor.RESET + "for classes list");
					} else
						sender.sendMessage(
								"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
										+ ChatColor.RESET + ChatColor.GREEN + "/class <classname>");
				} else
					sender.sendMessage(ChatColor.RED + "You must be a player to choose a class");
				return true;

			case "leave":
				GameInstance i = main.getGameManager().GetInstanceOfSpectator(player);
				if (main.getGameManager().RemovePlayerFromAll(player)) {
					main.ResetPlayer(player);
					player.setGameMode(GameMode.ADVENTURE);
					main.LobbyBoard(player);
					player.getInventory().clear();
					main.LobbyItems(player);
					player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
							+ ChatColor.RESET + "You have left your game");
					main.sendScoreboardUpdate(player);

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

					removeArmor(player);
				} else if (i != null && i.spectators.contains(player)) {
					player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
							+ ChatColor.RESET + "You have left " + i.getMap().toString());
					main.ResetPlayer(player);
					player.setGameMode(GameMode.ADVENTURE);
					main.LobbyBoard(player);
					player.getInventory().clear();
					main.LobbyItems(player);
					i.spectators.remove(player);
					player.setDisplayName("" + player.getName());
				} else {
					player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
							+ ChatColor.RESET + "You are not in a game!");
				}
				return true;

			case "give":
				if (args.length > 0 && args.length < 4) {
					Player target = Bukkit.getServer().getPlayerExact(args[0]);
					Material mat = testMaterial(args[1]);
					int amount = Integer.parseInt(args[2]);
					ItemStack item = null;
					if (mat != null) {
						item = new ItemStack(mat, amount);
						target.getInventory().addItem(item);
					} else {
						player.sendMessage(main.color("&c&l(!) &rInvalid item!"));
						return false;
					}
					if (target != player) {
						target.sendMessage(main.color("&e&l(!) &rYou were given &e " + amount + " " + item.getType()));
					} else {
						player.sendMessage(main.color("&e&l(!) &rYou were given &e " + amount + " " + item.getType()));
					}
				} else if (args.length > 3 && args.length < 6) {
					Player target = Bukkit.getServer().getPlayerExact(args[0]);
					Material mat = testMaterial(args[1]);
					int amount = Integer.parseInt(args[2]);
					Enchantment ench = testEnchant(args[3]);
					int level = Integer.parseInt(args[4]);
					ItemStack item = null;

					if (level > 0) {
						if (mat != null) {
							item = new ItemStack(mat, amount);
							enchantments(item, ench, level);
							target.getInventory().addItem(item);
						} else {
							player.sendMessage(main.color("&c&l(!) &rInvalid item!"));
							return false;
						}
						if (target != player) {
							target.sendMessage(
									main.color("&e&l(!) &rYou were given &e " + amount + " " + item.getType()));
						} else {
							player.sendMessage(main.color("&e&l(!) &rYou were given &e " + amount + " " + item.getType()
									+ " &rwith &e " + ench.getName() + " " + level));
						}
					} else {
						player.sendMessage(main.color(
								"&c&l(!) &rPlease enter an Enchantment level higher than 0!"));
					}
				} else {
					player.sendMessage(main.color(
							"&c&l(!) &rIncorrect usage! Try doing: &e/give <player> <item> <amount> <enchantment> <level>"));
				}
				return true;

			case "players":
				GameInstance instance = main.getGameManager().GetInstanceOfPlayer(player);
				if (instance != null) {
					String players = "";
					for (Player gamePlayer : instance.players) {
						if (!players.isEmpty())
							players += ", ";
						players += gamePlayer.getName() + "";
					}
					player.sendMessage(
							"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.GREEN + "Players in your game ("
									+ instance.players.size() + "): " + ChatColor.RESET + ChatColor.WHITE);
					player.sendMessage("" + ChatColor.BOLD + "--> " + ChatColor.RESET + players);
				} else
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You are not in a game");
				return true;
			}
		} else {
			sender.sendMessage("Hey! You can't use this in the terminal!");
		}
		return true;
	}

	private ItemStack enchantments(ItemStack item, Enchantment ench, int level) {
		item.addUnsafeEnchantment(ench, level);
		return item;
	}

	private Material testMaterial(String st) {
		try {
			return Material.getMaterial(st.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	private Enchantment testEnchant(String st) {
		try {
			return Enchantment.getByName(st.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("join")) {
			List<Maps> a = Arrays.asList(Maps.values());
			List<String> f = Lists.newArrayList();
			if (args.length == 1) {
				for (Maps s : a) {
					if (s.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
						f.add(s.getName());
					}
				}
				return f;
			}
		} else if (cmd.getName().equalsIgnoreCase("class")) {
			List<ClassType> a = Arrays.asList(ClassType.values());
			List<String> f = Lists.newArrayList();
			if (args.length == 1) {
				for (ClassType s : a) {
					if (s.name().toLowerCase().startsWith(args[0].toLowerCase())) {
						f.add(s.name());
					}
				}
				return f;
			}
		}
		/*
		 * else if (cmd.getName().equalsIgnoreCase("spectate")) { GameInstance instance
		 * = main.getGameManager().GetInstanceOfPlayer(player); List<Maps> a =
		 * instance.activeMaps; Arrays.asList(a); List<String> f = Lists.newArrayList();
		 * if (args.length == 1) { for (Maps s : a) { if
		 * (s.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
		 * f.add(s.getName()); } } } return f; }
		 */
		return null;
	}

}
