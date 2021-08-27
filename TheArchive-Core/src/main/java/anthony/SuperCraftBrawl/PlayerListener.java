package anthony.SuperCraftBrawl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.GameState;
import anthony.SuperCraftBrawl.gui.CosmeticsGUI;
import anthony.SuperCraftBrawl.gui.HubGUI;
import anthony.SuperCraftBrawl.gui.PrefsGUI;
import anthony.SuperCraftBrawl.gui.StatsGUI;
import anthony.SuperCraftBrawl.gui.TokenClassesGUI;
import anthony.SuperCraftBrawl.playerdata.PlayerData;

public class PlayerListener implements Listener {

	private final Main main;
	public GameState state;

	public PlayerListener(Main main) {
		this.main = main;
		this.main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		main.ResetPlayer(event.getPlayer());
	}

	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event) {
		GameInstance instance = main.getGameManager().GetInstanceOfPlayer(event.getPlayer());

		if (instance != null) {
			main.getGameManager().RemovePlayerFromAll(event.getPlayer());
		}
	}

	@EventHandler
	public void waterNoFlow(BlockFromToEvent e) {
		e.setCancelled(true);
	}

	/*
	 * @EventHandler(priority = EventPriority.LOWEST) public void
	 * onBlockPlace(BlockPlaceEvent event) { Player player = event.getPlayer();
	 * GameInstance instance = main.getGameManager().GetInstanceOfPlayer(player);
	 * 
	 * if (instance != null) { if (player.isOp()) { event.setCancelled(false); }
	 * else { event.setCancelled(true); } } else { if (player.isOp()) {
	 * event.setCancelled(false); } else { event.setCancelled(true); } } }
	 */

	/*
	 * @EventHandler public void onPlayerMove(PlayerMoveEvent event) { // Get the
	 * player's location. Location loc = event.getPlayer().getLocation(); // Sets
	 * loc to five above where it used to be. Note that this doesn't change the
	 * player's position. loc.setY(loc.getY() + 5); // Gets the block at the new
	 * location. Block b = loc.getBlock(); // Sets the block to type id 1 (stone).
	 * b.setType(Material.STONE); }
	 */

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if (player.isOp()) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		if (!(player.isOp())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFall(EntityDamageEvent e) {
		if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onClick(InventoryClickEvent event) {
		if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
			event.setCancelled(true);
		}
		// event.setCancelled(true);
	}

	@EventHandler
	public void tokenClassGUI(PlayerInteractEvent e) {
		ItemStack emerald = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta meta = emerald.getItemMeta();
		Player player = e.getPlayer();

		if (e.getItem() != null && e.getItem().getType() == Material.ENCHANTED_BOOK) {
			e.setCancelled(true);
			new TokenClassesGUI(main).inv.open(player);
		}
	}

	@EventHandler
	public void cosmeticsGUI(PlayerInteractEvent e) {
		ItemStack emerald = new ItemStack(Material.CHEST);
		ItemMeta meta = emerald.getItemMeta();
		Player player = e.getPlayer();

		if (e.getItem() != null && e.getItem().getType() == Material.CHEST) {
			new CosmeticsGUI(main).inv.open(player);
		}
	}

	@EventHandler
	public void prefsGUI(PlayerInteractEvent e) {
		ItemStack emerald = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta meta = emerald.getItemMeta();
		Player player = e.getPlayer();

		if (e.getItem() != null && e.getItem().getType() == Material.REDSTONE_COMPARATOR) {
			new PrefsGUI(main).inv.open(player);
		}
	}

	@EventHandler
	public void joinItem(PlayerInteractEvent e) {
		ItemStack compass = new ItemStack(Material.WATCH);
		ItemMeta meta = compass.getItemMeta();

		if (e.getItem() != null && e.getItem().getType() == Material.WATCH) {
			new HubGUI(main).inv.open(e.getPlayer());
		}
	}

	@EventHandler
	public void manipulate(PlayerArmorStandManipulateEvent e) {
		if (!e.getRightClicked().isVisible()) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void statsItem(PlayerInteractEvent e) {
		ItemStack compass = new ItemStack(Material.SKULL_ITEM);
		ItemMeta meta = compass.getItemMeta();

		if (e.getItem() != null && e.getItem().getType() == Material.SKULL_ITEM) {
			new StatsGUI(main).inv.open(e.getPlayer());
			// player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This
			// feature has been disabled due to an issue");
		}
	}

	@EventHandler
	public void containerInteract(PlayerInteractEvent e) {
		List<Material> list = new ArrayList<>(Arrays.asList(Material.FURNACE, Material.HOPPER, Material.ANVIL,
				Material.ENCHANTMENT_TABLE, Material.ANVIL, Material.WORKBENCH, Material.BREWING_STAND,
				Material.TRAPPED_CHEST, Material.ENDER_CHEST));
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && list.contains(e.getClickedBlock().getType())) {
			Player player = e.getPlayer();
			if (!player.isOp()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		Player foodChangedPlayer = (Player) e.getEntity();
		e.setCancelled(true);
	}

	@EventHandler
	public void profileStats(PlayerInteractEvent e) {
		ItemStack sk = new ItemStack(Material.SKULL_ITEM);
		ItemMeta meta = sk.getItemMeta();

		if (e.getItem() != null && e.getItem().getType() == Material.SKULL_ITEM) {
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		// StaffChat
		if (main.staffchat.contains(event.getPlayer())) {
			String tag = main.getRankManager().getRank(event.getPlayer()).getTagWithSpace();
			String message = tag + event.getPlayer().getDisplayName() + ": " + event.getMessage();

			for (Player staff : main.staffchat) {
				TellAll(message);
				event.setCancelled(true);
				return;
			}
		} else {
			// Chat filter
			List<String> words = new ArrayList<>(Arrays.asList("nibba", "nigga", "porn", "pornhub", "cum", "nexly",
					"n e x l y", "fuck you", "fuckyou", "fuck", "shit", "sh!t", "bitch", "pussy", "fucker",
					"motherfucker", "celestepvp", "celeste", "bitch", "kys"));
			String[] msg = event.getMessage().split(" ");
			Boolean censored = false;
			PlayerData data = main.getDataManager().getPlayerData(event.getPlayer());
			String tag = main.getRankManager().getRank(event.getPlayer()).getTagWithSpace();
			String message = "";

			if (event.getPlayer().hasPermission("scb.chat"))
				message = "" + ChatColor.YELLOW + "[" + ChatColor.YELLOW + ChatColor.BOLD + data.level + ChatColor.RESET
						+ ChatColor.YELLOW + "] " + tag + event.getPlayer().getDisplayName() + ChatColor.RESET + ": "
						+ event.getMessage();
			else
				message = "" + ChatColor.YELLOW + "[" + ChatColor.YELLOW + ChatColor.BOLD + data.level + ChatColor.RESET
						+ ChatColor.YELLOW + "] " + tag + ChatColor.GRAY + event.getPlayer().getDisplayName() + ": "
						+ event.getMessage();

			if (data != null) {
				if (data.blue == 1) {
					message = "" + ChatColor.YELLOW + "[" + ChatColor.YELLOW + ChatColor.BOLD + data.level
							+ ChatColor.RESET + ChatColor.YELLOW + "] " + tag + ChatColor.BLUE
							+ event.getPlayer().getDisplayName() + ChatColor.RESET + ": " + event.getMessage();
				} else if (data.red == 1) {
					message = "" + ChatColor.YELLOW + "[" + ChatColor.YELLOW + ChatColor.BOLD + data.level
							+ ChatColor.RESET + ChatColor.YELLOW + "] " + tag + ChatColor.RED
							+ event.getPlayer().getDisplayName() + ChatColor.RESET + ": " + event.getMessage();
				} else if (data.green == 1) {
					message = "" + ChatColor.YELLOW + "[" + ChatColor.YELLOW + ChatColor.BOLD + data.level
							+ ChatColor.RESET + ChatColor.YELLOW + "] " + tag + ChatColor.GREEN
							+ event.getPlayer().getDisplayName() + ChatColor.RESET + ": " + event.getMessage();
				} else if (data.yellow == 1) {
					message = "" + ChatColor.YELLOW + "[" + ChatColor.YELLOW + ChatColor.BOLD + data.level
							+ ChatColor.RESET + ChatColor.YELLOW + "] " + tag + ChatColor.YELLOW
							+ event.getPlayer().getDisplayName() + ChatColor.RESET + ": " + event.getMessage();
				}
			}

			event.setCancelled(true);

			for (String s : msg) {
				for (String word : words) {
					if (s.toLowerCase().contains(word.toLowerCase())) {
						censored = true;
					}
				}
			}
			if (!censored) {
				if (data.muted == 0)
					Bukkit.broadcastMessage(message);
				else
					event.getPlayer()
							.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ ChatColor.RED + "You are muted for " + ChatColor.YELLOW + "Permanent "
									+ ChatColor.RED + "for: " + ChatColor.YELLOW + "No reason yet");
			}
			if (censored) {
				event.getPlayer().sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.GRAY
						+ "Hey! You can't say that! :(");
			}
		}
	}

	public void TellAll(String message) {
		for (Player staff : main.staffchat) {
			staff.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "StaffChat> " + ChatColor.RESET + message);
		}
	}

	/*
	 * @EventHandler public void onChat(AsyncPlayerChatEvent event) { for
	 * (Iterator<Player> itr = event.getRecipients().iterator(); itr.hasNext();) {
	 * if (!itr.next().getWorld().equals(event.getPlayer().getWorld())) {
	 * itr.remove(); } } }
	 */
}
