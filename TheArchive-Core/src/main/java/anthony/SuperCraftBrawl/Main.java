package anthony.SuperCraftBrawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import anthony.Duels.game.DuelManager;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.GameManager;
import anthony.SuperCraftBrawl.Game.GameState;
import anthony.SuperCraftBrawl.Game.SmmManager;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import anthony.SuperCraftBrawl.Game.map.Maps;
import anthony.SuperCraftBrawl.anticheat.AntiCheat;
import anthony.SuperCraftBrawl.commands.Commands;
import anthony.SuperCraftBrawl.doublejump.DoubleJumpManager;
import anthony.SuperCraftBrawl.gui.ActiveGamesGUI;
import anthony.SuperCraftBrawl.gui.ClassSelectorGUI;
import anthony.SuperCraftBrawl.gui.DonorClassesGUI;
import anthony.SuperCraftBrawl.gui.HubGUI;
import anthony.SuperCraftBrawl.gui.InventoryGUI;
import anthony.SuperCraftBrawl.gui.QuitGUI;
import anthony.SuperCraftBrawl.gui.SMMGUI;
import anthony.SuperCraftBrawl.gui.StatsGUI;
import anthony.SuperCraftBrawl.gui.StatsTargetGUI;
import anthony.SuperCraftBrawl.gui.pvpGUI;
import anthony.SuperCraftBrawl.npcs.NPCManager;
import anthony.SuperCraftBrawl.playerdata.DatabaseManager;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import anthony.SuperCraftBrawl.playerdata.PlayerDataManager;
import anthony.SuperCraftBrawl.punishment.Punishment;
import anthony.SuperCraftBrawl.punishment.PunishmentMenu;
import anthony.SuperCraftBrawl.ranks.Rank;
import anthony.SuperCraftBrawl.ranks.RankManager;
import fr.mrmicky.fastboard.FastBoard;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {

	static Plugin plugin;

	private GameManager gameManager;
	private InventoryGUI inventoryGUI;
	private DonorClassesGUI donorGUI;
	private HubGUI hubGUI;
	private Commands commands;
	private World lobbyWorld;
	private PlayerListener listener;
	private SmmManager smmmanager;
	private DoubleJumpManager djManager;
	protected final Cooldown cooldownTime = null;
	private RankManager rankManager;
	public List<Player> staffchat;
	public List<Player> globalchat;
	private PlayerDataManager dataManager;
	private DatabaseManager databaseManager;
	private NPCManager npcManager;
	private Punishment punishment;
	private ActiveGamesGUI ag;
	public boolean tournament = false;
	private DuelManager dm;
	public HashMap<Player, Boolean> ao = new HashMap<>();

	public Main() {
		this.staffchat = new ArrayList<Player>();
		this.globalchat = new ArrayList<Player>();
	}

	public void TellAll(String message) {
		for (Player staff : staffchat) {
			staff.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "StaffChat> " + ChatColor.RESET + message);
		}
	}

	public PlayerDataManager getDataManager() {
		return dataManager;
	}

	public String color(String c) {
		return ChatColor.translateAlternateColorCodes('&', c);
	}

	public NPCManager getNPCManager() {
		return npcManager;
	}
	
	public DuelManager getDuelManager() {
		return dm;
	}

	public ActiveGamesGUI getActiveGames() {
		return ag;
	}

	public SmmManager getSmmManager() {
		return smmmanager;
	}

	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	@EventHandler
	public void signClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block clickedBlock = event.getClickedBlock();
		if (clickedBlock != null) {
			BlockState state = clickedBlock.getState();

			if (state instanceof Sign) {
				Sign sign = (Sign) state;
				String line1 = sign.getLine(0);
				if (line1.equalsIgnoreCase("[Lobby]")) {
					sign.setLine(0, ChatColor.BLACK + "---------------");
					sign.update();
					player.setGameMode(GameMode.CREATIVE);
					player.sendMessage("Test");
				}
			}
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[LobbyMap]")) {
			e.setLine(0, ChatColor.BLACK + "---------------");
			e.setLine(1, "" + ChatColor.BLACK + ChatColor.UNDERLINE + ChatColor.BOLD + "CHOOSE A");
			e.setLine(2, "" + ChatColor.BLACK + ChatColor.UNDERLINE + ChatColor.BOLD + "CLASS!");
			e.setLine(3, "" + ChatColor.BLACK + "---------------");
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(0).equalsIgnoreCase("---------------")) {
				// instance.AddPlayer(player);
				new ClassSelectorGUI(this).inv.open(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void onSignChange5(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[LobbyMap2]")) {
			e.setLine(0, ChatColor.BLACK + "---------------");
			e.setLine(1, "" + ChatColor.BLACK + ChatColor.UNDERLINE + ChatColor.BLUE + ChatColor.BOLD + "DONOR");
			e.setLine(2, "" + ChatColor.BLACK + ChatColor.UNDERLINE + ChatColor.BLUE + ChatColor.BOLD + "CLASSES");
			e.setLine(3, "" + ChatColor.BLACK + "---------------");
		}
	}

	@EventHandler
	public void onPlayerInteract5(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(1).equalsIgnoreCase("DONOR")) {
				// instance.AddPlayer(player);
				new DonorClassesGUI(this).inv.open(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void onSignChange6(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[LobbyMap3]")) {
			e.setLine(0, ChatColor.BLACK + "---------------");
			e.setLine(1, "" + ChatColor.BLACK + ChatColor.UNDERLINE + ChatColor.GREEN + ChatColor.BOLD + "TOKEN");
			e.setLine(2, "" + ChatColor.BLACK + ChatColor.UNDERLINE + ChatColor.GREEN + ChatColor.BOLD + "CLASSES");
			e.setLine(3, "" + ChatColor.BLACK + "---------------");
		}
	}

	@EventHandler
	public void onPlayerInteract6(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(1).equalsIgnoreCase("TOKEN")) {
				// instance.AddPlayer(player);
				player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "Not implemented yet!");
			}
		}
	}

	@EventHandler
	public void onSignChange2(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[SCB]")) {
			e.setLine(0, ChatColor.BLACK + "SCB");
			e.setLine(1, "" + ChatColor.BLACK + ChatColor.BOLD + "WELCOME TO ");
			e.setLine(2, "" + ChatColor.RED + ChatColor.BOLD + "SUPER CRAFT");
			e.setLine(3, "" + ChatColor.RED + ChatColor.BOLD + "BLOCKS!");
		}
	}

	@EventHandler
	public void onPlayerInteract2(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(0).equalsIgnoreCase("SCB")) {
				new ClassSelectorGUI(this).inv.open(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void onSignChange30(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[HallOfFame]")) {
			e.setLine(0, ChatColor.BLACK + "=X=");
			e.setLine(1, "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "SCB Hall");
			e.setLine(2, "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "of Fame!");
			e.setLine(3, ChatColor.BLACK + "=X=");
		}
	}

	@EventHandler
	public void onPlayerInteract30(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
		}
	}

	@EventHandler
	public void onSignChange3(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[MapList]")) {
			e.setLine(0, ChatColor.BLACK + "Maps");
			e.setLine(1, "" + ChatColor.BLACK + ChatColor.BOLD + "Not all maps ");
			e.setLine(2, "" + ChatColor.BLACK + ChatColor.BOLD + "are listed!");
			e.setLine(3, "" + ChatColor.BLACK + "Use " + ChatColor.BLACK + ChatColor.BOLD + "/maplist");
		}
	}

	@EventHandler
	public void onPlayerInteract3(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(0).equalsIgnoreCase("Maps")) {
			}
		}
	}

	@EventHandler
	public void onSignChange4(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[Leave]")) {
			e.setLine(0, ChatColor.BLACK + "Leave");
			e.setLine(1, "" + ChatColor.BLACK + ChatColor.BOLD + "Click to go ");
			e.setLine(2, "" + ChatColor.BLACK + ChatColor.BOLD + "to Lobby");
		}
	}

	@EventHandler
	public void onPlayerInteract4(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(0).equalsIgnoreCase("Leave")) {
				new QuitGUI(this).invQuit.open(player);
			}
		}
	}

	@EventHandler
	public void onSignChange20(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[Frenzy]")) {
			e.setLine(0, "" + ChatColor.RESET + "Frenzy");
			e.setLine(1, "" + ChatColor.BLACK + ChatColor.BOLD + "Click for an");
			e.setLine(2, "" + ChatColor.BLACK + ChatColor.BOLD + "Intro to Frenzy!");
			e.setLine(3, "" + ChatColor.RED + ChatColor.BOLD + "");
		}
	}

	@EventHandler
	public void onPlayerInteract20(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(0).equalsIgnoreCase("Frenzy")) {
				frenzyIntro(player);
			}
		}
	}

	public Location GetFrenzyIntroLoc() {
		return new Location(lobbyWorld, -51.595, 168, -406.523);
	}

	List<BukkitRunnable> runnables = new ArrayList<>();

	public void frenzyIntro(Player player) {
		BukkitRunnable runTimer = new BukkitRunnable() {

			int ticks = 60;

			@Override
			public void run() {
				if (!(player.getWorld() == lobbyWorld)) {
					this.cancel();
					player.setGameMode(GameMode.ADVENTURE);
					player.setAllowFlight(true);
				}

				FastBoard board = new FastBoard(player);
				board.updateTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "INTRO TO FRENZY");
				board.updateLines("", "" + ChatColor.RESET + ChatColor.BOLD + "Ending In:", " " + ticks + "s");

				if (ticks == 0) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Sending you back to Hub..");
					SendPlayerToHub(player);
					LobbyItems(player);
					LobbyBoard(player);
					player.setGameMode(GameMode.ADVENTURE);
					player.setHealth(20.0);
					player.setAllowFlight(true);
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
					this.cancel();
				} else if (ticks == 60) {
					player.teleport(GetFrenzyIntroLoc());
					player.setGameMode(GameMode.ADVENTURE);
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1));
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ ChatColor.BOLD + "Welcome to Frenzy!");
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ "This will be a tutorial guiding you how the Frenzy gamemode works");
				} else if (ticks == 52) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Let's get started now...");
				} else if (ticks == 50) {
					player.teleport(new Location(lobbyWorld, 1071.700, 211, -980.443));
					player.sendMessage("");
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ "Frenzy games are very similar to Normal SCB games you would play, except this mode allows an infinite amount of "
							+ "players to be able to join, unlike regular games");
				} else if (ticks == 45) {
					player.teleport(new Location(lobbyWorld, 997.493, 213, -1002.499));
				} else if (ticks == 40) {
					player.teleport(new Location(lobbyWorld, -1019.524, 179, 461.498));
					player.sendMessage("");
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ "When you join a Frenzy game, you are not able to choose a class and a class will be randomly selected for you");
				} else if (ticks == 35) {
					player.teleport(new Location(lobbyWorld, -958.700, 158, 485.519));
				} else if (ticks == 30) {
					player.teleport(new Location(lobbyWorld, 532.365, 182, 507.496));
					player.sendMessage("");
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ "Something cool about Frenzy and what makes it fun is that each life, you will be given a new class. "
							+ "Even if you do not have all classes unlocked, the game will give you any class, even ones not unlocked or bought");
				} else if (ticks == 25) {
					player.teleport(new Location(lobbyWorld, 469.488, 136, 506.369));
				} else if (ticks == 20) {
					player.teleport(new Location(lobbyWorld, 1499.700, 188, 11000.354));
					player.sendMessage("");
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ "And that's basically all there is to know about Frenzy games! All other stuff still remains the same, you have 5 "
							+ "lives, and just be the last man standing to claim that #1 spot!");
				} else if (ticks == 15) {
					player.teleport(new Location(lobbyWorld, -51.700, 168, -406.487));
					player.sendMessage("");
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ "Hope you guys enjoyed this Intro to Frenzy games! If you'd like to provide feedback on this tutorial, do so on our "
							+ "Discord Server: https://discord.gg/B9eHKg7");
				}
				ticks--;
			}
		};
		runTimer.runTaskTimer(this, 0, 20);
		runnables.add(runTimer);
	}

	public Location GetFactionLobbyLoc() {
		return new Location(lobbyWorld, 0, 100, 0);
	}

	@EventHandler
	public void SendPlayerToFaction(Player player) {
		player.teleport(GetFactionLobbyLoc());
	}

	public Location getParkourLoc() {
		return new Location(lobbyWorld, 6.496, 82, 499.497);
	}

	@EventHandler
	public void SendPlayerToParkour(Player player) {
		player.teleport(getParkourLoc());
	}

	public Location getSCBLoc() {
		return new Location(lobbyWorld, -8.531, 161, -406.493);
	}

	@EventHandler
	public void SendPlayerToSCB(Player player) {
		player.teleport(getSCBLoc());
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public HubGUI getHubGUI() {
		return hubGUI;
	}

	public InventoryGUI getInventoryGUI() {
		return inventoryGUI;
	}

	public DonorClassesGUI getDonorGUI() {
		return donorGUI;
	}

	public Cooldown getCooldown() {
		return cooldownTime;
	}

	public PlayerListener getListener() {
		return listener;
	}

	public Commands getCommands() {
		return commands;
	}

	public String format(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public RankManager getRankManager() {
		return rankManager;
	}

	public Punishment getPunishment() {
		return punishment;
	}

	private AntiCheat cheat;

	public AntiCheat getAntiCheat() {
		return cheat;
	}

	@Override
	public void onEnable() {
		plugin = this;
		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		getLogger().info("(!) You have enabled Minezone-Core");
		lobbyWorld = this.getServer().getWorlds().get(0);

		for (Player onlinePlayer : this.getServer().getOnlinePlayers())
			this.ResetPlayer(onlinePlayer);

		listener = new PlayerListener(this);
		smmmanager = new SmmManager(this);
		gameManager = new GameManager(this);
		commands = new Commands(this);
		djManager = new DoubleJumpManager(this);
		databaseManager = new DatabaseManager();
		dataManager = new PlayerDataManager(this);
		npcManager = new NPCManager(this);
		rankManager = new RankManager(this);
		punishment = new Punishment(this);
		ag = new ActiveGamesGUI(this);
		dm = new DuelManager(this);
		// cheat = new AntiCheat(this);

		String[] commandTypes = { "join", "leave", "players", "class", "test", "spectate", "setrank", "startgame",
				"setlives", "purchases", "give" };

		for (String command : commandTypes) {
			PluginCommand pluginCommand = this.getCommand(command);
			if (pluginCommand != null) {
				pluginCommand.setExecutor(commands);
				pluginCommand.setTabCompleter(commands);
			} else {
				System.out.print(command + " was null!");
			}
		}

	}

	public Location GetLobbyLoc() {
		return new Location(lobbyWorld, -5.533, 143, 19.468);
	}

	@EventHandler
	public void SendPlayerToMap(Player player) {
		player.teleport(GetLobbyLoc());
	}

	public Location GetStaffLoc() {
		return new Location(lobbyWorld, 953.529, 177, 1036.495);
	}

	@EventHandler
	public void SendPlayerToStaff(Player player) {
		player.teleport(GetStaffLoc());
	}

	public Location GetHubLoc() {
		// return new Location(lobbyWorld, -199, 86, -7);
		// return new Location(lobbyWorld, -5.457, 143, 19.522);
		// return new Location(lobbyWorld, 288.507, 119, 2346.529);
		return new Location(lobbyWorld, -5.529, 143, 19.474);
	}

	@EventHandler
	public void SendPlayerToHub(Player player) {
		player.teleport(GetHubLoc());
	}

	public String staffhelp = "";
	public String staffhelpReply = "";

	@SuppressWarnings("null")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		List<Player> playerss = null;

		if (cmd.getName().equalsIgnoreCase("smmtest")) {
			new SMMGUI(this).inv.open(player);
		}

		if (cmd.getName().equalsIgnoreCase("sh")) {
			if (args.length == 0) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
						+ ChatColor.GREEN + "/sh <message>");
			} else {
				staffhelp = "";

				for (int i = 0; i < args.length; i++) {
					staffhelp += args[i] + " ";
				}
				player.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "StaffHelp> " + ChatColor.RESET
						+ getRankManager().getRank(player).getTagWithSpace() + ChatColor.RESET + player.getName() + ": "
						+ ChatColor.LIGHT_PURPLE + staffhelp);
				player.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "If any staff is online, you will recieve a reply shortly");

				for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
					if (onlinePlayers.hasPermission("scb.staffhelp")) {
						onlinePlayers.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "StaffHelp> "
								+ ChatColor.RESET + getRankManager().getRank(player).getTagWithSpace() + ChatColor.RESET
								+ player.getName() + ": " + ChatColor.LIGHT_PURPLE + staffhelp);
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("shr")) {
			if (player.hasPermission("scb.staffhelpreply")) {
				if (args.length == 0) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/shr <player> <message>");
				} else if (args.length == 1) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/shr <player> <message>");
				} else {
					Player target = Bukkit.getServer().getPlayerExact(args[0]);
					staffhelpReply = "";

					if (target != null) {
						for (int i = 1; i < args.length; i++) {
							staffhelpReply += args[i] + " ";
						}
						player.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "StaffHelp REPLY> "
								+ ChatColor.RESET + getRankManager().getRank(player).getTagWithSpace() + ChatColor.RESET
								+ player.getName() + ": " + ChatColor.LIGHT_PURPLE + staffhelpReply);
						target.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "StaffHelp REPLY> "
								+ ChatColor.RESET + getRankManager().getRank(player).getTagWithSpace() + ChatColor.RESET
								+ player.getName() + ": " + ChatColor.LIGHT_PURPLE + staffhelpReply);
					} else {
						player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "Please specify a player!");
					}
				}
			} else {
				player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "You need the rank " + ChatColor.GOLD + ChatColor.BOLD + "TRAINEE " + ChatColor.RESET
						+ "to use this command");
			}
		}

		if (cmd.getName().equalsIgnoreCase("broadcast")) {
			if (player.hasPermission("scb.broadcast")) {
				if (args.length == 0) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/broadcast <message>");
					return true;
				} else {
					String message = "";

					for (int i = 0; i < args.length; i++) {
						message += args[i] + " ";
					}

					for (Player allPlayers : Bukkit.getOnlinePlayers()) {
						if (args.length != 0) {
							allPlayers.sendTitle("" + ChatColor.GREEN + "Announcement", "" + ChatColor.RESET + message
									+ " - " + ChatColor.YELLOW + player.getName().substring(0, 3));
							allPlayers.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ message + " - " + ChatColor.YELLOW + player.getName());
						}
					}
				}
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank " + ChatColor.RED
						+ ChatColor.BOLD + "ADMIN " + ChatColor.RESET + "to use this command!");
			}
		}

		if (cmd.getName().equalsIgnoreCase("scbp")) {
			if (player.hasPermission("scb.practicetest")) {
				// new PunishmentMenu(this).open(player);
			}
		}

		if (cmd.getName().equalsIgnoreCase("punish")) {
			if (sender.hasPermission("scb.punish")) {
				if (args.length == 0) {
					sender.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.YELLOW + "/punish <player>");
				} else if (args.length == 1) {
					Player target = Bukkit.getServer().getPlayerExact(args[0]);

					if (target != null) {
						player.sendMessage(
								"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Opening punishment menu..");
						new PunishmentMenu(this, target).open(player);
					} else {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Please enter a player!");
					}
				}
			} else {
				player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "You need the rank " + ChatColor.GOLD + ChatColor.BOLD + "TRAINEE " + ChatColor.RESET
						+ "to use this command");
			}
		}

		if (cmd.getName().equalsIgnoreCase("me")) {
			player.sendMessage("Disabled!");
		}

		if (cmd.getName().equalsIgnoreCase("kick")) {
			player.sendMessage("Disabled!");
		}

		if (cmd.getName().equalsIgnoreCase("sc") && sender instanceof Player) {
			if (player.hasPermission("scb.staffchat")) {
				if (!(staffchat.contains(player))) {
					staffchat.add(player);
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have enabled "
							+ ChatColor.YELLOW + "StaffChat");
				} else {
					staffchat.remove(player);
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have disabled "
							+ ChatColor.YELLOW + "StaffChat");
				}
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank "
						+ ChatColor.GOLD + ChatColor.BOLD + "TRAINEE " + ChatColor.RESET + "to use this command");
			}
		}

		if (cmd.getName().equalsIgnoreCase("list")) {
			String players = "";
			int count = 0;
			int totalPlayers = Bukkit.getOnlinePlayers().size();
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There are " + ChatColor.YELLOW
					+ totalPlayers + ChatColor.RESET + " players online:");

			for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
				count++;
				players += "" + ChatColor.YELLOW + onlinePlayers.getName() + "";

				if (count < totalPlayers) {
					players += "" + ChatColor.RESET + ", ";
				}
			}

			player.sendMessage(players);
		}

		if (cmd.getName().equalsIgnoreCase("survival") && sender instanceof Player) {
			if (sender.hasPermission("survival.survival")) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Sending you to " + ChatColor.GREEN
						+ "Survival");
				SendPlayerToFaction(player);
				player.setGameMode(GameMode.SURVIVAL);
			} else {
				player.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.RED
						+ "You don't have permission to join the " + ChatColor.RESET + ChatColor.GREEN
						+ "Survival Server");
			}
		}

		if (cmd.getName().equalsIgnoreCase("online")) {
			int online = Bukkit.getOnlinePlayers().size();
			player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There are "
					+ ChatColor.YELLOW + online + ChatColor.RESET + " players online");
		}

		if (cmd.getName().equalsIgnoreCase("testscblobby")) {
			SendPlayerToSCB(player);
		}

		if (cmd.getName().equalsIgnoreCase("melons")) {
			if (player.hasPermission("sch.melons")) {
				PlayerData data = this.getDataManager().getPlayerData(player);
				data.melon += 100;
			}
		}

		if (cmd.getName().equalsIgnoreCase("cwm") && sender instanceof Player) {
			PlayerData data = this.getDataManager().getPlayerData(player);
			if (player.hasPermission("scb.customWin")) {
				if (args.length == 0) {
					player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ "Incorrect usage! Try doing: " + ChatColor.YELLOW + "/cwm on/off");
				}
				if (args[0].equals("on")) {
					data.cwm = 1;
					player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ "You have enabled " + ChatColor.YELLOW + "Custom Win Messages");
				} else if (args[0].equals("off")) {
					data.cwm = 0;
					player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ "You have disabled " + ChatColor.YELLOW + "Custom Win Messages");
				}
			} else {
				player.sendMessage("" + ChatColor.RESET + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "You need a rank in order to use this command!");
			}
		}

		if (cmd.getName().equalsIgnoreCase("discord") && sender instanceof Player) {
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Join our discord server here: "
					+ ChatColor.GREEN + "discord.gg/rQdaCZXaHF");
		}

		if (cmd.getName().equalsIgnoreCase("vanish") && sender instanceof Player) {
			if (sender.hasPermission("scb.vanish")) {
				player.sendMessage(
						"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.GREEN + "You are now in vanish");
				player.setGameMode(GameMode.SPECTATOR);
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.RED + "You need the rank "
						+ ChatColor.RED + ChatColor.BOLD + "ADMIN " + ChatColor.RESET + ChatColor.RED
						+ "to use this command");
			}
		}

		if (cmd.getName().equalsIgnoreCase("unvanish") && sender instanceof Player) {
			if (sender.hasPermission("scb.unvanish")) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.GREEN + "You are now "
						+ ChatColor.RESET + ChatColor.RED + "unvanished");
				player.setGameMode(GameMode.SURVIVAL);
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.RED + "You need the rank "
						+ ChatColor.RED + ChatColor.BOLD + "ADMIN " + ChatColor.RESET + ChatColor.RED
						+ "to use this command");
			}
		}

		if (cmd.getName().equalsIgnoreCase("server") && sender instanceof Player) {
			int players = Bukkit.getOnlinePlayers().size();
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You are currently connected to "
					+ ChatColor.UNDERLINE + "Main-1");

			if (players == 1) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There is " + ChatColor.YELLOW
						+ players + ChatColor.RESET + " player here");
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There are " + ChatColor.YELLOW
						+ players + ChatColor.RESET + " players here");
			}
		}
		if (cmd.getName().equalsIgnoreCase("rules") && sender instanceof Player) {

			player.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.WHITE
					+ "The rules can be found at, " + ChatColor.RESET + ChatColor.GREEN + "discord.gg/B9eHKg7");
		}
		if (cmd.getName().equalsIgnoreCase("staff") && sender instanceof Player) {
			if (sender.hasPermission("scb.staff")) {
				GameInstance instance = this.getGameManager().GetInstanceOfPlayer(player);

				if (instance != null) {
					player.sendMessage(color("&r&l(!) &rYou cannot teleport to &eStaff &rwhile in a game!"));
				} else {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Sending you to "
							+ ChatColor.GREEN + "Staff");
					SendPlayerToStaff(player);
				}
			} else {
				player.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.RED
						+ "You don't have permission to join the " + ChatColor.RESET + ChatColor.GREEN
						+ "Staff Server");
			}

		}
		if (cmd.getName().equalsIgnoreCase("parkour") && sender instanceof Player) {
			GameInstance instance = this.getGameManager().GetInstanceOfPlayer(player);

			if (instance != null) {
				player.sendMessage(color("&r&l(!) &rYou cannot teleport to &eParkour &rwhile in a game!"));
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Sending you to " + ChatColor.GREEN
						+ "Parkour");
				SendPlayerToParkour(player);
				player.setAllowFlight(false);
			}
		}
		if (cmd.getName().equalsIgnoreCase("gmc") && sender instanceof Player) {
			if (sender.hasPermission("scb.gmc")) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Your gamemode has been updated to "
						+ ChatColor.RESET + ChatColor.GREEN + "Creative!");
				player.setGameMode(GameMode.CREATIVE);
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank " + ChatColor.RED
						+ ChatColor.BOLD + "ADMIN " + ChatColor.RESET + "to perform this command!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("gms") && sender instanceof Player) {
			if (sender.hasPermission("scb.gms")) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Your gamemode has been updated to "
						+ ChatColor.RESET + ChatColor.GREEN + "Survival!");
				player.setGameMode(GameMode.SURVIVAL);
				player.setAllowFlight(true);
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank " + ChatColor.RED
						+ ChatColor.BOLD + "ADMIN " + ChatColor.RESET + "to perform this command!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("gmsp") && sender instanceof Player) {
			if (sender.hasPermission("scb.gmsp")) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Your gamemode has been updated to "
						+ ChatColor.RESET + ChatColor.GREEN + "Spectator!");
				player.setGameMode(GameMode.SPECTATOR);
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank " + ChatColor.RED
						+ ChatColor.BOLD + "ADMIN " + ChatColor.RESET + "to perform this command!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("gma") && sender instanceof Player) {
			if (sender.hasPermission("scb.gma")) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Your gamemode has been updated to "
						+ ChatColor.RESET + ChatColor.GREEN + "Adventure!");
				player.setGameMode(GameMode.ADVENTURE);
				player.setAllowFlight(true);
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank " + ChatColor.RED
						+ ChatColor.BOLD + "ADMIN " + ChatColor.RESET + "to perform this command!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("gm") && sender instanceof Player) {
			if (sender.hasPermission("scb.gm")) {
				player.sendMessage(
						"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.RED + "Incorrect usage! Try doing: "
								+ ChatColor.RESET + ChatColor.GREEN + "/gms, /gmc, /gmsp or /gma");
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank " + ChatColor.RED
						+ ChatColor.BOLD + "ADMIN " + ChatColor.RESET + "to perform this command!");
			}
		}

		if (cmd.getName().equalsIgnoreCase("help") && sender instanceof Player) {
			player.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.AQUA
					+ "Need help? Go to our Discord Server for Help!");
			player.sendMessage(
					"- " + ChatColor.RED + ChatColor.BOLD + "Discord: " + ChatColor.GREEN + "discord.gg/B9eHKg7");
		}
		if (cmd.getName().equalsIgnoreCase("classes") && sender instanceof Player) {
			player.sendMessage(color("&r&l(!) &eThe following classes are available to play:"));
			String dClasses = "";
			String tClasses = "";
			String rClasses = "";
			int count = 0;
			for (ClassType type : ClassType.values()) {
				if (type.getTokenCost() == 0 && type.getMinRank() != Rank.Vip)
					dClasses += type.getTag() + " ";
				else if (type.getTokenCost() > 0)
					tClasses += type.getTag() + " ";
				else if (type.getMinRank() == Rank.Vip)
					rClasses += type.getTag() + " ";
			}
			player.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + "DEFAULT CLASSES");
			player.sendMessage(dClasses);
			player.sendMessage("");
			player.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + "TOKEN CLASSES");
			player.sendMessage(tClasses);
			player.sendMessage("");
			player.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + "DONOR CLASSES");
			player.sendMessage(rClasses);
		}
		if (cmd.getName().equalsIgnoreCase("scb") && sender instanceof Player) {
			player.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "[SUPER CRAFT BLOCKS]");
			player.sendMessage("" + ChatColor.GREEN + "Custom coded plugin by: VineFortuna & CowNecromancer");
			player.sendMessage("" + ChatColor.GREEN + "Version: 1.0");
			player.sendMessage("" + ChatColor.GREEN + "Type " + ChatColor.WHITE + "/scbhelp " + ChatColor.GREEN
					+ "for more information");
		}
		if (cmd.getName().equalsIgnoreCase("scbhelp") && sender instanceof Player) {
			player.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "GENERAL SCB COMMANDS");
			player.sendMessage("" + ChatColor.WHITE + "/join -> " + ChatColor.GREEN + "Join a game");
			player.sendMessage("" + ChatColor.WHITE + "/leave -> " + ChatColor.GREEN + "Leave your game");
			player.sendMessage("" + ChatColor.WHITE + "/classes -> " + ChatColor.GREEN + "Lists all available classes");
			player.sendMessage("" + ChatColor.WHITE + "/class -> " + ChatColor.GREEN + "Choose a class");
			player.sendMessage("" + ChatColor.WHITE + "/spectate -> " + ChatColor.GREEN + "Spectate a game");
			player.sendMessage("" + ChatColor.WHITE + "/maplist -> " + ChatColor.GREEN + "List of all available maps");
		}
		if (cmd.getName().equalsIgnoreCase("maplist") && sender instanceof Player) {
			String list = "";
			int count = 1;

			player.sendMessage(
					"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "The following maps are available to play:");
			for (Maps map : Maps.values()) {
				list += "" + ChatColor.YELLOW + map.toString() + ChatColor.RESET;
				if (count < Maps.values().length) {
					list += ", ";
				}
				count++;
			}
			player.sendMessage(list);
		}

		if (cmd.getName().equalsIgnoreCase("maps")) {
			int count = 0;
			for (int i = 0; i < Maps.values().length; i++) {
				count++;
			}
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There are " + ChatColor.YELLOW + count
					+ ChatColor.RESET + " available maps to play");
		}
		if (cmd.getName().equalsIgnoreCase("hub") && sender instanceof Player) {
			if (player.getWorld() != lobbyWorld) {
				this.getGameManager().RemovePlayerFromAll(player);
				this.ResetPlayer(player);
				this.getGameManager().RemovePlayerFromMap(player, null, player);
				SendPlayerToHub(player);
				player.setAllowFlight(true);
				this.sendScoreboardUpdate(player);

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
				player.setAllowFlight(true);

				player.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
				player.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
				player.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
				player.getInventory().setBoots(new ItemStack(Material.AIR, 1));

				LobbyBoard(player);

				player.setGameMode(GameMode.ADVENTURE);
				player.setHealth(20);
				player.setFoodLevel(20);
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have been sent to the Hub");

				LobbyItems(player);
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You are already in the Hub");
			}
		}

		if (cmd.getName().equalsIgnoreCase("exp")) {
			if (player.hasPermission("scb.exp")) {
				if (args.length == 0) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/exp <amount>");
				} else if (args.length == 1) {
					int num = Integer.parseInt(args[0]);
					PlayerData data = this.getDataManager().getPlayerData(player);
					data.exp += num;
					player.sendMessage("Added " + num + " to your account");

					if (data.exp >= 5000) {
						data.level++;
						data.exp -= 5000;
						player.sendMessage("Level upgraded to " + data.level + "!");
					}
				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("invite")) {
			GameInstance instance = this.getGameManager().GetInstanceOfPlayer(player);
			// Cooldown inviteCooldown = new Cooldown(60000);

			if (player.hasPermission("scb.invite")) {
				// if (inviteCooldown.useAndResetCooldown()) {
				if (instance != null) {
					if (instance.state == GameState.WAITING) {
						/*
						 * for (Player lobbyPlayers : Bukkit.getOnlinePlayers()) { if
						 * (lobbyPlayers.getWorld() == getLobbyWorld()) { lobbyPlayers.sendMessage("" +
						 * ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " +
						 * getRankManager().getRank(player).getTagWithSpace() + ChatColor.YELLOW +
						 * player.getName() + " " + ChatColor.RESET +
						 * "invited all players in the Lobby to join " + ChatColor.YELLOW +
						 * instance.getMap()); lobbyPlayers.sendMessage("" + ChatColor.RESET + "Use " +
						 * ChatColor.GREEN + "/join " + instance.getMap()); player.sendMessage("" +
						 * ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " +
						 * getRankManager().getRank(player).getTagWithSpace() + ChatColor.YELLOW +
						 * player.getName() + " " + ChatColor.RESET +
						 * "invited all players in the Lobby to join " + ChatColor.YELLOW +
						 * instance.getMap()); player.sendMessage("" + ChatColor.RESET + "Use " +
						 * ChatColor.GREEN + "/join " + instance.getMap()); } }
						 */
						Bukkit.broadcastMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
								+ getRankManager().getRank(player).getTagWithSpace() + ChatColor.YELLOW
								+ player.getName() + " " + ChatColor.RESET + "invited all players in the Lobby to join "
								+ ChatColor.YELLOW + instance.getMap());
						Bukkit.broadcastMessage(
								"" + ChatColor.RESET + "Use " + ChatColor.GREEN + "/join " + instance.getMap());
					} else {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You must be in a Waiting Lobby to use this command");
					}
				} else {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ "You need to be in a game to use this command");
				}
				// } else {
				// player.sendMessage("Cooldown!");
				// }
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank "
						+ ChatColor.BLUE + ChatColor.BOLD + "CAPTAIN " + ChatColor.RESET + "to use this command");
			}
		}

		if (cmd.getName().equalsIgnoreCase("pvp") && sender instanceof Player) {
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
					+ "KitPvP is down at the moment and will resume soon!");
			/*
			 * player.setAllowFlight(false);
			 * 
			 * this.getGameManager().RemovePlayerFromAll(player);
			 * 
			 * ScoreboardManager m = Bukkit.getScoreboardManager(); Scoreboard c =
			 * m.getNewScoreboard();
			 * 
			 * Objective o = c.registerNewObjective("Gold", "");
			 * o.setDisplaySlot(DisplaySlot.SIDEBAR); o.setDisplayName("" + ChatColor.YELLOW
			 * + ChatColor.BOLD + "PvP");
			 * 
			 * Score gold0 = o.getScore(""); // Score gold = o.getScore(ChatColor.WHITE +
			 * "Gold: " + ChatColor.GOLD + // "20,000"); Score gold1 = o.getScore("" +
			 * ChatColor.YELLOW + ChatColor.BOLD + "Kills: "); Score gold2 = o.getScore("" +
			 * ChatColor.WHITE + player.getStatistic(Statistic.PLAYER_KILLS)); Score gold3 =
			 * o.getScore("" + ChatColor.YELLOW + ChatColor.BOLD + "Deaths: "); Score gold4
			 * = o.getScore("" + ChatColor.RESET + player.getStatistic(Statistic.DEATHS));
			 * Score gold5 = o.getScore("" + ChatColor.WHITE + ChatColor.BOLD + ""); Score
			 * gold6 = o.getScore("" + ChatColor.WHITE + ChatColor.BOLD +
			 * "Crate Drop In: "); Score gold7 = o.getScore("" + ChatColor.WHITE +
			 * "Coming Soon..."); Score gold110 = o.getScore(""); Score gold111 =
			 * o.getScore("");
			 * 
			 * // gold11.setScore(11); // gold12.setScore(10); gold0.setScore(9);
			 * gold1.setScore(8); gold2.setScore(7); gold110.setScore(6); gold3.setScore(5);
			 * gold4.setScore(4); gold5.setScore(3); gold6.setScore(2); gold7.setScore(1);
			 * 
			 * player.setScoreboard(c);
			 * 
			 * ChooseKit(player);
			 */
		}
		if (cmd.getName().equalsIgnoreCase("store")) {
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
					+ "Want to help support the server? Purchase a rank at " + ChatColor.GREEN
					+ "https://minezone.tebex.io/");
		}

		if (cmd.getName().equalsIgnoreCase("color")) {
			PlayerData data = this.getDataManager().getPlayerData(player);

			if (player.hasPermission("scb.color")) {
				if (data != null) {
					if (args.length == 0) {
						player.sendMessage(
								"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
										+ ChatColor.GREEN + "/color blue/green/red/yellow/reset");
					} else if (args[0].equalsIgnoreCase("blue")) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Changed your prefix to "
								+ ChatColor.BLUE + player.getName());
						data.blue = 1;
						data.red = 0;
						data.green = 0;
						data.yellow = 0;
					} else if (args[0].equalsIgnoreCase("red")) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Changed your prefix to "
								+ ChatColor.RED + player.getName());
						data.red = 1;
						data.blue = 0;
						data.green = 0;
						data.yellow = 0;
					} else if (args[0].equalsIgnoreCase("green")) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Changed your prefix to "
								+ ChatColor.GREEN + player.getName());
						data.green = 1;
						data.red = 0;
						data.blue = 0;
						data.yellow = 0;
					} else if (args[0].equalsIgnoreCase("yellow")) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Changed your prefix to "
								+ ChatColor.YELLOW + player.getName());
						data.green = 0;
						data.red = 0;
						data.blue = 0;
						data.yellow = 1;
					} else if (args[0].equalsIgnoreCase("reset")) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Reset your name color");
						data.green = 0;
						data.red = 0;
						data.blue = 0;
						data.yellow = 0;
					}
				}
			} else {
				player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "You need the rank " + ChatColor.BLUE + ChatColor.BOLD + "CAPTAIN " + ChatColor.RESET
						+ "to use this command");
			}
		}

		if (cmd.getName().equalsIgnoreCase("token") && sender instanceof Player) {
			if (player.hasPermission("scb.giveTokens")) {
				if (args.length == 0) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/token add <player> <amount>");
				} else if (args[0].equalsIgnoreCase("add")) {
					if (args.length == 1) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "Incorrect usage! Try doing: " + ChatColor.GREEN + "/token add <player> <amount>");
					} else if (args.length == 2) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "Incorrect usage! Try doing: " + ChatColor.GREEN + "/token add <player> <amount>");
					} else if (args.length == 3) {
						Player target = Bukkit.getServer().getPlayerExact(args[1]);
						try {
							int num = Integer.parseInt(args[2]);

							PlayerData data = this.getDataManager().getPlayerData(target);
							if (target != null) {
								data.tokens += num;

								player.sendMessage(
										"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You gave " + ChatColor.GREEN
												+ target.getName() + ChatColor.RESET + " " + num + " Tokens!");
								target.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You were given "
										+ num + " Tokens!");
								LobbyBoard(target);
							} else {
								player.sendMessage(
										"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Please specify a player!");
							}
						} catch (Exception e) {
							player.sendMessage(
									"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Please enter a number!");
						}
					}
				} else {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/token add <player> <amount>");
				}
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank " + ChatColor.RED
						+ ChatColor.BOLD + "ADMIN " + ChatColor.RESET + "to use this command!");
			}
		}

		if (cmd.getName().equalsIgnoreCase("tp")) {
			if (player.hasPermission("scb.tp")) {
				if (args.length == 0) {
					player.sendMessage(color("&r&l(!) &rList of Teleport Commands:"));
					player.sendMessage(color("&r- &e/tp <player>"));
					player.sendMessage(color("&r- &e/tp <X> <Y> <Z>"));
				} else if (args.length == 1) {
					Player target = Bukkit.getServer().getPlayerExact(args[0]);

					if (target != null) {
						player.teleport(target.getLocation());
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Teleporting to "
								+ ChatColor.YELLOW + target.getName());
					} else {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Please enter a player!");
					}
				} else if (args.length == 2) {
					player.sendMessage(color("&r&l(!) &rList of Teleport Commands:"));
					player.sendMessage(color("&r- &e/tp <player>"));
					player.sendMessage(color("&r- &e/tp <X> <Y> <Z>"));
				} else if (args.length == 3) {
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					int z = Integer.parseInt(args[2]);

					player.teleport(new Location(player.getWorld(), x, y, z));
					player.sendMessage(color("&r&l(!) &rTeleporting to &e" + x + "&r, &e" + y + "&r, &e" + z));
				}
			} else {
				player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "You need the rank " + ChatColor.GOLD + ChatColor.BOLD + "TRAINEE " + ChatColor.RESET
						+ "to use this command");
			}
		}

		if (cmd.getName().equalsIgnoreCase("nick")) {
			GameInstance instance = this.getGameManager().GetInstanceOfPlayer(player);
			if (instance == null) {
				if (player.hasPermission("scb.nickname.use")) {
					if (args.length == 0 || args[0].equals(player.getName())) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
								+ "Your nickname has been reset!");
						player.setDisplayName("" + player.getName());
						return true;
					}

					String nick = "";
					if (args[0].matches("^[a-zA-Z0-9_]*$")) {
					} else {
						player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "Please enter a name with only alphanumeric characters!");
						return true;
					}
					if (Bukkit.getPlayerExact(args[0]) == null) {
					} else {
						player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You cannot name yourself as another player!");
						return true;
					}
					if (args[0].length() <= 16) {
						nick += args[0] + " ";
					} else {
						player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "Please enter a name up to 16 characters!");
						return true;
					}

					nick = nick.substring(0, nick.length() - 1);

					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You changed your name to "
							+ ChatColor.YELLOW + nick);
					player.setDisplayName("" + nick);
				} else {
					player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_RED + ChatColor.BOLD + "(!) "
							+ ChatColor.RESET + "You need a " + ChatColor.YELLOW + ChatColor.BOLD + "DONOR "
							+ ChatColor.RESET + "rank to access this command!");
				}
			} else {
				player.sendMessage(
						"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You cannot use this while in a game");
			}
		}
		if (cmd.getName().equalsIgnoreCase("tell") || cmd.getName().equalsIgnoreCase("msg")) {
			if (args.length == 0) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
						+ ChatColor.GREEN + "/tell <player> <message>");
				return true;
			}
			Player target = Bukkit.getServer().getPlayerExact(args[0]);
			PlayerData data = this.getDataManager().getPlayerData(target);

			if (target != null) {
				if (data.pm == 0) {
					String message = "";

					for (int i = 1; i != args.length; i++) {
						message += args[i] + " ";
					}

					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.GRAY + "You --> "
							+ target.getName() + ChatColor.RESET + ": " + ChatColor.RESET + message);
					target.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.GRAY
							+ player.getName() + " --> You" + ChatColor.RESET + ": " + ChatColor.RESET + message);
				} else if (data.pm == 1) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
							+ target.getName() + ChatColor.LIGHT_PURPLE + " has private messaging disabled!");
				}

			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Please specify a player!");
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("activegames")) {

			int num = 0;
			for (Entry<Maps, GameInstance> entry : getGameManager().gameMap.entrySet()) {
				num++;
				player.sendMessage("" + ChatColor.YELLOW + "===-" + ChatColor.RESET + ChatColor.BOLD
						+ entry.getValue().getMap() + ChatColor.RESET + ChatColor.YELLOW + "-===");
				player.sendMessage("" + ChatColor.YELLOW + "|| " + ChatColor.RESET + ChatColor.BOLD + "Game Type: "
						+ ChatColor.GREEN + ChatColor.BOLD + ChatColor.ITALIC + entry.getValue().gameType.toString());
				player.sendMessage("" + ChatColor.YELLOW + "|| " + ChatColor.RESET + ChatColor.BOLD + "Game State: "
						+ ChatColor.GREEN + ChatColor.BOLD + ChatColor.ITALIC + entry.getValue().state.getName());
				player.sendMessage("" + ChatColor.YELLOW + "|| " + ChatColor.RESET + ChatColor.BOLD + "Players: "
						+ ChatColor.GREEN + ChatColor.BOLD + entry.getValue().players.size() + "/"
						+ entry.getValue().gameType.getMaxPlayers());
				player.sendMessage("" + ChatColor.YELLOW + "|| " + ChatColor.RESET + ChatColor.BOLD + "Alive Players: "
						+ ChatColor.GREEN + ChatColor.BOLD + entry.getValue().alivePlayers + "/"
						+ entry.getValue().players.size());
				player.sendMessage("");

				new ActiveGamesGUI(this).inv.open(player);
			}

			if (num == 0) {
				player.sendMessage(
						"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There are no games running at this time");
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There are " + ChatColor.YELLOW
						+ num + ChatColor.RESET + " games waiting/running right now");
			}

		}

		if (cmd.getName().equalsIgnoreCase("tournamentmodetoggle")) {
			if (player.hasPermission("scb.toggleTournament")) {
				if (tournament == true) {
					tournament = false;
					player.sendMessage(color("&e&l(!) &eTournament mode disabled!"));
					for (Player onlinePlayers : Bukkit.getOnlinePlayers())
						LobbyBoard(onlinePlayers);
				} else {
					tournament = true;
					player.sendMessage(color("&e&l(!) &eTournament mode now enabled!"));
					for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
						PlayerData data = this.getDataManager().getPlayerData(onlinePlayers);
						data.points = 0;
						LobbyBoard(onlinePlayers);
					}
				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("points")) {
			if (player.hasPermission("scb.points")) {
				if (args.length == 0) {
					player.sendMessage(color("&r&l(!) &rIncorrect usage! Try doing: &e/points <player>"));
				} else {
					Player target = Bukkit.getServer().getPlayerExact(args[0]);
					PlayerData data = this.getDataManager().getPlayerData(target);

					if (target != null) {
						if (data != null) {
							player.sendMessage(color("&r&l(!) &e" + target.getName() + "'s points: " + data.points));
						}
					} else {
						player.sendMessage(color("&r&l(!) &rPlease specify a player!"));
					}
				}
			} else {
				player.sendMessage(color("&r&l(!) &rYou need the rank &c&lOWNER &rto use this command!"));
			}
		}

		if (cmd.getName().equalsIgnoreCase("kill")) {
			player.sendMessage("Disabled!");
		}
		if (cmd.getName().equalsIgnoreCase("setchests")) {
			if (player.hasPermission("scb.test")) {
				PlayerData data = this.getDataManager().getPlayerData(player);
				if (data != null)
					data.mysteryChests++;
			}
		}
		if (cmd.getName().equalsIgnoreCase("stats")) {
			if (args.length == 0) {
				new StatsGUI(this).inv.open(player);
			} else if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayerExact(args[0]);
				if (target != null) {
					new StatsTargetGUI(this, target).inv.open(player);
					player.sendMessage(
							"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Opening "
									+ ChatColor.YELLOW + target.getName() + "'s" + ChatColor.RESET + " statistics");
				} else {
					player.sendMessage(
							"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "The specified target is not online!");
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("pay")) {
			if (args.length == 0) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
						+ ChatColor.GREEN + "/pay tokens <player> <amount>");
			} else if (args[0].equalsIgnoreCase("tokens")) {
				if (args.length == 1) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/pay tokens <player> <amount>");
				} else if (args.length == 2) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/pay tokens <player> <amount>");
				} else if (args.length == 3) {
					Player target = Bukkit.getServer().getPlayerExact(args[1]);
					try {
						int num = Integer.parseInt(args[2]);
						PlayerData data = this.getDataManager().getPlayerData(target);
						PlayerData data2 = this.getDataManager().getPlayerData(player);
						if (target != null) {
							if (data2.tokens >= num) {
								data.tokens += num;
								data2.tokens -= num;

								player.sendMessage(
										"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You paid " + ChatColor.GREEN
												+ target.getName() + ChatColor.RESET + " " + num + " Tokens!");
								target.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You were paid "
										+ num + " Tokens!");
							} else {
								player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_RED + ChatColor.BOLD + "(!) "
										+ ChatColor.RESET + "Not enough funds!");
							}
						} else {
							player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "This player does not exist or " + "is not online!");
						}

					} catch (Exception e) {
						player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Please enter a number!");
					}
				}

			}
		}
		if (cmd.getName().equalsIgnoreCase("seen")) {
			if (args.length == 0) {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
						+ ChatColor.GREEN + "/seen <player>");
				return true;
			}
			OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
			if (target != null && target.hasPlayedBefore()) {
				long t = System.currentTimeMillis() - target.getLastPlayed();
				long h = TimeUnit.MILLISECONDS.toHours(t);
				long m = TimeUnit.MILLISECONDS.toMinutes(t);
				long s = TimeUnit.MILLISECONDS.toSeconds(t);
				if (!target.isOnline()) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + target.getName()
							+ " was last online " + ChatColor.GREEN + h + " hours, " + (m - h * 60) + " minutes, and "
							+ (s - m * 60) + " seconds ago");
				} else {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + target.getName()
							+ " was last online " + ChatColor.GREEN + "now");
				}
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Please specify a player!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("ignite")) {
			if (player.hasPermission("scb.ignite")) {
				if (args.length == 0) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Incorrect usage! Try doing: "
							+ ChatColor.GREEN + "/ignite <player>");
					return true;
				}
				Player target = Bukkit.getServer().getPlayerExact(args[0]);

				if (target != null) {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have ignited "
							+ ChatColor.YELLOW + target.getName());
					target.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You were ignited by "
							+ ChatColor.YELLOW + player.getName());
					target.setFireTicks(1000);
				} else {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Please specify a player!");
					return false;
				}
			} else {
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You need the rank " + ChatColor.RED
						+ ChatColor.BOLD + "ADMIN " + ChatColor.RESET + "to perform this command!");
			}
		}

		return false;

	}

	public Location GetDuelLoc() {
		return new Location(lobbyWorld, 287.956, 100, 2308.510);
	}

	@EventHandler
	public void SendPlayerToDuel(Player player) {
		player.teleport(GetDuelLoc());
	}

	public Location GetDuelLoc2() {
		return new Location(lobbyWorld, 288.405, 123, 2307.472);
	}

	@EventHandler
	public void SendPlayerToDuel2(Player player) {
		player.teleport(GetDuelLoc2());
	}

	public void ChooseKit(Player player) {
		SendPlayerToDuel2(player);
		new pvpGUI(this).inv.open(player);
	}

	public void StartDuel(Player player) {
		SendPlayerToDuel(player);
		player.setAllowFlight(false);
	}

	public void LobbyScoreboard(Player player) {

		LobbyBoard(player);

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
            team.setPrefix(this.getRankManager().getRank(player).getTagWithSpace());
        }
    }

	@SuppressWarnings("deprecation")
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String pname = p.getName();
		p.setGameMode(GameMode.ADVENTURE);
		sendScoreboardUpdate(p);

		// Message to send the server on join
		e.setJoinMessage("" + ChatColor.BOLD + "[" + ChatColor.YELLOW + ChatColor.BOLD + "+" + ChatColor.RESET
				+ ChatColor.BOLD + "] " + ChatColor.RESET + getRankManager().getRank(p).getTagWithSpace() + ""
				+ ChatColor.AQUA + pname + ChatColor.RESET + ChatColor.GRAY + " has joined");

		// Set rank on tablist
		String owner = "";
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (getRankManager().getRank(players) == Rank.Owner) {
				owner += "" + getRankManager().getRank(p).getTagWithSpace() + ChatColor.RESET + p.getName();
			}
		}
		p.setPlayerListName("" + getRankManager().getRank(p).getTagWithSpace() + ChatColor.RESET + p.getName());

		p.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
		p.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
		p.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
		p.getInventory().setBoots(new ItemStack(Material.AIR, 1));

		p.sendMessage("----------------------------------------------");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "          WELCOME TO THE MINEZONE SERVER!");
		p.sendMessage("" + "     Be sure to join our Discord Server with " + ChatColor.GREEN + "/discord");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("----------------------------------------------");

		p.sendTitle("" + ChatColor.GREEN + ChatColor.BOLD + "MINEZONE",
				"" + ChatColor.RESET + ChatColor.YELLOW + "New classes released! Check out /classes");

		Player player = e.getPlayer();
		player.getInventory().clear();
		// Use a for loop to remove potion effects player.getActivePotionEffects()
		/*
		 * for (PotionEffect potType : player.getActivePotionEffects()) { potType. }
		 */
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

		player.getInventory().setItem(0, ItemHelper.setDetails(new ItemStack(Material.COMPASS),
				"" + ChatColor.GREEN + ChatColor.BOLD + "Game Selector"));
	}

	@EventHandler
	public void leave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		String pname = player.getName();
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

		PlayerData data = this.getDataManager().getPlayerData(player);
		GameInstance instance = this.getGameManager().GetInstanceOfPlayer(player);

		if (data.votes == 1) {
			if (instance != null) {
				instance.totalVotes--;
				data.votes = 0;
			}
		}

		e.setQuitMessage("" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "-" + ChatColor.RESET
				+ ChatColor.BOLD + "] " + ChatColor.RESET + getRankManager().getRank(player).getTagWithSpace()
				+ ChatColor.AQUA + pname + ChatColor.GRAY + " has left");
	}

	public Location hologramLoc(Player player) {
		return new Location(lobbyWorld, 288.557, 114, 2362.646);
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		player.setGameMode(GameMode.ADVENTURE);

		LobbyBoard(player);
		player.setGameMode(GameMode.ADVENTURE);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.getInventory().setItem(0, ItemHelper.setDetails(new ItemStack(Material.COMPASS),
				"" + ChatColor.GREEN + ChatColor.BOLD + "Game Selector"));
	}

	public Location LobbyLoc() {
		// return new Location(lobbyWorld, -199.517, 89.98466, -7.519);
		// return new Location(lobbyWorld, -5.457, 143, 19.522);
		// return new Location(lobbyWorld, 288.507, 119, 2346.529);
		return new Location(lobbyWorld, -5.529, 143, 19.474);
	}

	public HashMap<Player, FastBoard> board = new HashMap<>();

	public void LobbyBoard(Player player) {
		FastBoard board = new FastBoard(player);
		PlayerData data = this.getDataManager().getPlayerData(player);
		this.board.put(player, board);

		if (tournament == false) {
			board.updateTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "MINEZONE");
			if (data != null) {
				board.updateLines("" + ChatColor.YELLOW + ChatColor.BOLD + "Tokens:",
						"" + ChatColor.RESET + data.tokens, "",
						"" + ChatColor.RESET + ChatColor.BOLD + "Level: " + ChatColor.YELLOW + ChatColor.BOLD
								+ data.level,
						"" + ChatColor.RED + "[" + ChatColor.YELLOW + data.exp + "/5000 EXP" + ChatColor.RED + "]", "",
						"" + ChatColor.GREEN + ChatColor.BOLD + "Rank:",
						"" + ChatColor.RESET + getRankManager().getRank(player), "",
						"" + ChatColor.RED + ChatColor.BOLD + "Discord:", "" + ChatColor.RESET + "discord.gg/B9eHKg7",
						"", "----------------", "" + ChatColor.AQUA + "MINEZONE.APEXMC.CO");
			}
		} else {
			board.updateTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "MINEZONE");
			if (data != null) {
				board.updateLines("" + ChatColor.YELLOW + ChatColor.BOLD + "Tokens:",
						"" + ChatColor.RESET + data.tokens, "", color("&r&lPoints: &r" + data.points), "",
						"" + ChatColor.RESET + ChatColor.BOLD + "Level: " + ChatColor.YELLOW + ChatColor.BOLD
								+ data.level,
						"" + ChatColor.RED + "[" + ChatColor.YELLOW + data.exp + "/5000 EXP" + ChatColor.RED + "]", "",
						"" + ChatColor.GREEN + ChatColor.BOLD + "Rank:",
						"" + ChatColor.RESET + getRankManager().getRank(player), "", "----------------",
						"" + ChatColor.AQUA + "MINEZONE.APEXMC.CO");
			}
		}
	}

	public World getLobbyWorld() {
		return lobbyWorld;
	}

	public void LobbyItems(Player player) {
		player.getInventory().setItem(0, ItemHelper.setDetails(new ItemStack(Material.COMPASS),
				"" + ChatColor.GREEN + ChatColor.BOLD + "Game Selector"));
		player.getInventory().setItem(1, ItemHelper.setDetails(new ItemStack(Material.EYE_OF_ENDER),
				"" + ChatColor.BLUE + ChatColor.BOLD + "Active Games"));
		player.getInventory().setItem(3, ItemHelper.setDetails(new ItemStack(Material.ENCHANTED_BOOK),
				"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Token Classes"));
		player.getInventory().setItem(4, ItemHelper.setDetails(new ItemStack(Material.CHEST),
				"" + ChatColor.RESET + ChatColor.BOLD + "Cosmetics"));

		ItemStack stats = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta statsMeta = (SkullMeta) stats.getItemMeta();
		statsMeta.setOwner(player.getName());
		stats.setItemMeta(statsMeta);

		player.getInventory().setItem(8,
				ItemHelper.setDetails(stats, "" + ChatColor.RESET + ChatColor.BOLD + "Profile"));
		player.getInventory().setItem(7, ItemHelper.setDetails(new ItemStack(Material.REDSTONE_COMPARATOR),
				"" + ChatColor.RESET + ChatColor.BOLD + "Preferences"));
		player.setAllowFlight(true);
	}

	public void ResetPlayer(Player player) {
		player.teleport(LobbyLoc());
		player.setHealth(20.0f);
		player.getInventory().clear();
		player.setAllowFlight(true);
		LobbyItems(player);
	}

	public Location GetSpawnLocation() {
		// return new Location(lobbyWorld, -199.517, 89.98466, -7.519);
		// return new Location(lobbyWorld, 288.507, 119, 2346.529);
		return new Location(lobbyWorld, -5.529, 143, 19.474);
	}

	/*
	 * public boolean isInBounds(Location loc) { Vector v = new Vector(-5.500, 172,
	 * 19.500); Location centre = new Location(lobbyWorld, v.getX(), v.getY(),
	 * v.getZ()); double boundsX = 150, boundsZ = 220;
	 * 
	 * if (Math.abs(centre.getX() - loc.getX()) > boundsX) return false; if
	 * (Math.abs(centre.getZ() - loc.getZ()) > boundsZ) return false; return true; }
	 */

	@Override
	public void onDisable() {
		getLogger().info("(!) You have disabled Minezone-Core");
	}
}
