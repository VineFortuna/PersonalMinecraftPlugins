package anthony.SuperCraftBrawl.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Timer;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import anthony.SuperCraftBrawl.Game.map.Maps;
import anthony.SuperCraftBrawl.Game.projectile.ItemProjectile;
import anthony.SuperCraftBrawl.Game.projectile.ProjectileManager;
import anthony.SuperCraftBrawl.Game.projectile.ProjectileOnHit;
import anthony.SuperCraftBrawl.gui.ActiveGamesGUI;
import anthony.SuperCraftBrawl.gui.ClassSelectorGUI;
import anthony.SuperCraftBrawl.gui.HubGUI;
import anthony.SuperCraftBrawl.gui.MysteryChestsGUI;
import anthony.SuperCraftBrawl.gui.QuitGUI;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class GameManager implements Listener {
	public HashMap<Maps, GameInstance> gameMap;

	private final ProjectileManager projManager;
	private final Main main;
	public GameState state;
	public GameType gameType;

	public GameManager(Main main) {
		this.main = main;
		this.gameMap = new HashMap<Maps, GameInstance>();
		this.main.getServer().getPluginManager().registerEvents(this, main);
		this.projManager = new ProjectileManager(this);
	}

	public GameInstance getGameInstance(Player player) {
		for (Entry<Maps, GameInstance> entry : gameMap.entrySet()) {
			if (entry.getValue().HasPlayer(player)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/*
	 * @EventHandler public void ThrowableEggs(PlayerEggThrowEvent event) { Egg e =
	 * event.getEgg(); Player player = event.getPlayer(); GameInstance instance =
	 * this.GetInstanceOfPlayer(player);
	 * 
	 * if (e.getType() == EntityType.EGG.CHICKEN) { ItemProjectile proj = new
	 * ItemProjectile(instance, player, new ProjectileOnHit() {
	 * 
	 * @Override public void onHit(Player hit) { player.playSound(hit.getLocation(),
	 * Sound.SUCCESSFUL_HIT, 1, 1);
	 * 
	 * }
	 * 
	 * }, new ItemStack(Material.SNOW_BALL));
	 * instance.getManager().getProjManager().shootProjectile(proj,
	 * player.getEyeLocation(), player.getLocation().getDirection().multiply(2.0D));
	 * } }
	 */

	@EventHandler
	public void onTestEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.getWorld() == main.getLobbyWorld()) {
				event.setCancelled(true);
			} else {
				GameInstance instance = getGameInstance(player);

				if (instance != null) {
					if (instance.state == GameState.STARTED) {
						event.setCancelled(false);
					} else {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	public ProjectileManager getProjManager() {
		return projManager;
	}

	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event) {
		for (Entry<Maps, GameInstance> game : gameMap.entrySet()) {
			if (game.getValue().PlayerInteract(event))
				return;
		}
	}

	@EventHandler
	public void onEntityDamage(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		GameInstance instance = getGameInstance(player);
		BaseClass baseClass = null;
		if (instance != null)
			baseClass = instance.classes.get(player);

		if (instance != null && baseClass != null)
			if (event.getCause() == TeleportCause.ENDER_PEARL) {
				event.setCancelled(true);
				if (instance.isInBounds(event.getTo())) {
					if (baseClass.isDead == true) {
						if (event.getTo() == null || event.getTo() != null) {
							
						}
					} else {
						player.teleport(event.getTo());
						player.damage(1.7);
					}
				} else {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You cannot teleport there!");
					player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
				}
			}
	}

	public int getNumOfGames() {
		int num = 0;
		for (Entry<Maps, GameInstance> entry : gameMap.entrySet()) {
			if (entry.getValue().state == GameState.STARTED) {
				num++;
			}
		}
		return num;
	}

	public int getNumOfGamesFrenzy() {
		int num = 0;
		for (Entry<Maps, GameInstance> entry : gameMap.entrySet()) {
			if (entry.getValue().state == GameState.STARTED && entry.getValue().gameType == GameType.FRENZY) {
				num++;
			}
		}
		return num;
	}

	public int getNumOfGamesNormal() {
		int num = 0;
		for (Entry<Maps, GameInstance> entry : gameMap.entrySet()) {
			if (entry.getValue().state == GameState.STARTED && entry.getValue().gameType == GameType.NORMAL) {
				num++;
			}
		}
		return num;
	}

	public int getNumOfGamesDuel() {
		int num = 0;
		for (Entry<Maps, GameInstance> entry : gameMap.entrySet()) {
			if (entry.getValue().state == GameState.STARTED && entry.getValue().gameType == GameType.DUEL) {
				num++;
			}
		}
		return num;
	}

	public String mapName = "";

	public int getActiveGames() {
		int count = 0;
		for (Entry<Maps, GameInstance> games : gameMap.entrySet()) {
			if (games.getValue().state == GameState.WAITING || games.getValue().state == GameState.STARTED) {
				count++;
				mapName = games.getValue().getMap().toString();
			}
		}
		return count;
	}

	public String getActiveMapName() {
		String mapName = "";
		for (Entry<Maps, GameInstance> games : gameMap.entrySet()) {
			if (games.getValue().state == GameState.WAITING || games.getValue().state == GameState.STARTED) {
				mapName = games.getValue().getMap().toString();
			}
		}

		return mapName;
	}

	@EventHandler
	public void MobBurn(EntityCombustEvent event) {
		if (event.getEntityType() == EntityType.ZOMBIE || event.getEntityType() == EntityType.SKELETON)
			event.setCancelled(true);
	}

	private HashMap<Player, BukkitRunnable> borderRunnables = new HashMap<>();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		GameInstance instance = getGameInstance(player);

		if (player.getWorld() == main.getLobbyWorld()) {
			if (e.getPlayer().getLocation().getY() < 50) {
				main.SendPlayerToHub(player);
			}
		}
		if (instance != null) {
			if (instance.state == GameState.STARTED) {
				if (e.getPlayer().getLocation().getY() < 50 && e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
					EntityDamageEvent damageEvent = new EntityDamageEvent(e.getPlayer(), DamageCause.VOID, 1000);
					main.getServer().getPluginManager().callEvent(damageEvent);
				}
				if (!(instance.isInBounds(e.getPlayer().getLocation()))
						&& e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
					BukkitRunnable runnable = borderRunnables.get(player);
					if (runnable == null) {
						runnable = new BukkitRunnable() {
							int ticks = 7;

							@Override
							public void run() {

								if (player.getGameMode() == GameMode.SPECTATOR) {
									borderRunnables.remove(player);
									this.cancel();
								}

								if (instance.state == GameState.ENDED) {
									borderRunnables.remove(player);
									this.cancel();
									player.setGameMode(GameMode.ADVENTURE);
									player.setAllowFlight(true);
								}

								if (ticks == 0) {
									player.setHealth(20.0);
									player.setAllowFlight(true);
									if (player.getGameMode() != GameMode.SPECTATOR) {
										EntityDamageEvent damageEvent = new EntityDamageEvent(e.getPlayer(),
												DamageCause.VOID, 1000);
										// for (Player gamePlayer : instance.players)
										// gamePlayer.getWorld().createExplosion(player.getLocation().getX(),
										// player.getLocation().getY(), player.getLocation().getZ(), 3, false,
										// false);
										main.getServer().getPluginManager().callEvent(damageEvent);
										borderRunnables.remove(player);
										this.cancel();
									} else {
										borderRunnables.remove(player);
										this.cancel();
									}
								} else if (ticks <= 5 && ticks > 0 && instance.state == GameState.STARTED) {
									if (instance.isInBounds(e.getPlayer().getLocation())) {
										player.sendTitle("", "");
										borderRunnables.remove(player);
										this.cancel();
									} else {
										player.sendTitle("" + ChatColor.RED + "Warning!", "" + ChatColor.RESET
												+ "Return to Safety in " + ChatColor.YELLOW + ticks);
										player.damage(2.0);
									}
								}
								ticks--;
							}
						};
						runnable.runTaskTimer(getMain(), 0, 20);
						borderRunnables.put(player, runnable);
					}
				}
			}
		}
	}

	public boolean chestCanOpen = false;

	@EventHandler
	public void mysteryChest(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Player player = e.getPlayer();
		List<Material> list = new ArrayList<>(Arrays.asList(Material.CHEST));

		if (player.getWorld() == main.getLobbyWorld()) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && list.contains(e.getClickedBlock().getType())) {
				if (chestCanOpen == false) {
					e.setCancelled(true);
					new MysteryChestsGUI(main).inv.open(player);
				} else {
					e.setCancelled(true);
					player.sendMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ "This mystery chest is already in use!");
				}
			}
		} else {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && list.contains(e.getClickedBlock().getType())) {
				e.setCancelled(true);
			}
		}
	}

	private Cooldown boosterCooldown = new Cooldown(0), shurikenCooldown = new Cooldown(3000);

	@EventHandler
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		Player player = event.getPlayer();
		PlayerData data = main.getDataManager().getPlayerData(player);

		if (player.getWorld() == main.getLobbyWorld()) {
			if (item != null && item.getType() == Material.WHEAT
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				double boosterStrength = 2.0;
				Vector vel = player.getLocation().getDirection().multiply(boosterStrength);
				player.setVelocity(vel);
				data.magicbroom = 1;
			}
		}
	}

	@EventHandler
	public void EntityDeathEvent​(EntityDeathEvent entity) {
		if (entity.getEntityType() == EntityType.ZOMBIE || entity.getEntityType() == EntityType.SKELETON) {
			entity.getDrops().clear();
			entity.setDroppedExp(0);
		}
	}

	@EventHandler
	public void blooper(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		GameInstance i = this.GetInstanceOfPlayer(player);

		if (item != null && item.getType() == Material.RABBIT_FOOT
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			int amount = item.getAmount();
			for (int x = 0; x < 100; x++) {
				int index = (int) (Math.random() * i.players.size());
				Player target = i.players.get(index);

				if (target != player) {
					target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 2, true));
					player.sendMessage(main.color("&6&l(!) &rYou blooped &e" + target.getName()));
					target.sendMessage(main.color("&6&l(!) &rYou were blooped by &e" + player.getName()));
					amount--;

					if (amount == 0)
						player.getInventory().clear(player.getInventory().getHeldItemSlot());
					else
						item.setAmount(amount);

					x = 100;
					return;
				}
			}
		}
	}

	@EventHandler
	public void votePaper(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		Player player = event.getPlayer();
		GameInstance instance = getGameInstance(player);
		PlayerData data = main.getDataManager().getPlayerData(player);

		if (instance != null) {
			if (instance.state == GameState.WAITING && instance.players.size() >= 2) {
				if (item != null && item.getType() == Material.PAPER && (event.getAction() == Action.RIGHT_CLICK_AIR
						|| event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
					if (data.votes == 0) {
						instance.totalVotes++;
						data.votes = 1;
						instance.TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + ChatColor.BOLD
								+ " has voted for game to start " + ChatColor.RED + "(" + ChatColor.GREEN
								+ instance.totalVotes + "/" + instance.players.size() + ChatColor.RED + ")");
					} else if (data.votes == 1) {
						player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You have already voted for the game to start!");
					}
				}
			}
		}
	}

	@EventHandler
	public void extraLife(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		Player player = event.getPlayer();
		GameInstance instance = getGameInstance(player);

		if (instance != null) {
			if (instance.state == GameState.STARTED) {
				if (item != null && item.getType() == Material.PRISMARINE_SHARD
						&& (event.getAction() == Action.RIGHT_CLICK_AIR
								|| event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
					BaseClass baseClass = instance.classes.get(player);
					int amount = item.getAmount();
					baseClass.lives += 1;
					baseClass.score.setScore(baseClass.lives);
					baseClass.TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ ChatColor.YELLOW + player.getName() + ChatColor.RESET + " used an extra life!");
					if (amount > 0) {
						amount--;
						if (amount == 0)
							player.getInventory().clear(player.getInventory().getHeldItemSlot());
						else
							item.setAmount(amount);
					}
				}
			}
		}
	}

	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if (boosterCooldown.useAndResetCooldown()) {
			if (e.getState() == State.IN_GROUND) {
				e.getPlayer().setVelocity(
						new Vector(e.getPlayer().getVelocity().getX(), 2.5, e.getPlayer().getVelocity().getY()));
			}
		}
	}

	@EventHandler
	public void cosmeticMelon(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		PlayerData data = main.getDataManager().getPlayerData(player);

		if (player.getWorld() == main.getLobbyWorld()) {
			if (item != null && item.getType() == Material.MELON) {
				if (player.getGameMode() != GameMode.SPECTATOR) {
					if (shurikenCooldown.useAndResetCooldown()) {
						if (data.melon > 0) {
							data.melon--;
							String msg = main.color("&9&l(!) &rYou have &e" + data.melon + " melons");
							PacketPlayOutChat packet = new PacketPlayOutChat(
									ChatSerializer.a("{\"text\":\"" + msg + "\"}"), (byte) 2);
							CraftPlayer craft = (CraftPlayer) player;
							craft.getHandle().playerConnection.sendPacket(packet);
							player.playSound(player.getLocation(), Sound.EAT, 2, 1);
							player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 110, 2));
							if (data.melon == 0)
								player.getInventory().clear(player.getInventory().getHeldItemSlot());
						}
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onInv(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		GameInstance instance = getGameInstance(player);

		if (instance != null) {
			/*
			 * if (e.getCurrentItem().getType() == Material.COAL) { e.setCancelled(true); }
			 * else if (e.getCurrentItem().getType() == Material.IRON_INGOT) {
			 * e.setCancelled(true); } else if (e.getCurrentItem().getType() ==
			 * Material.GOLD_INGOT) { e.setCancelled(true); } else if
			 * (e.getCurrentItem().getType() == Material.DIAMOND) { e.setCancelled(true); }
			 */
			if (!(player.isOp()))
				e.setCancelled(true);
		}
	}

	public Timer bazooka = new Timer();

	@EventHandler
	public void bazooka(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		GameInstance instance = getGameInstance(player);

		if (instance != null && instance.state == GameState.STARTED) {
			if (item != null && item.getType() == Material.DIAMOND_HOE) {
				if (player.getGameMode() != GameMode.SPECTATOR) {
					if (bazooka.getTime() < 3000) {
						int seconds = (3000 - bazooka.getTime()) / 1000 + 1;
						e.setCancelled(true);
						player.sendMessage(
								"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have to wait " + ChatColor.YELLOW
										+ seconds + " seconds " + ChatColor.RESET + "to use this item again");
					} else {
						bazooka.restart();
						int amount = item.getAmount();
						if (amount > 0) {
							amount--;
							if (amount == 0)
								player.getInventory().clear(player.getInventory().getHeldItemSlot());
							else
								item.setAmount(amount);
							ItemProjectile proj = new ItemProjectile(instance, player, new ProjectileOnHit() {
								@Override
								public void onHit(Player hit) {
									player.playSound(hit.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
									hit.damage(6.5, player);
									for (Player gamePlayer : instance.players) {
										gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 2, 1);
										// gamePlayer.getWorld().createExplosion(hit.getLocation().getX(),
										// hit.getLocation().getY(), hit.getLocation().getZ(), 3, false, false);
										gamePlayer.playEffect(hit.getLocation(), Effect.EXPLOSION_HUGE, 1);
									}

								}

							}, new ItemStack(Material.TNT));
							instance.getManager().getProjManager().shootProjectile(proj, player.getEyeLocation(),
									player.getLocation().getDirection().multiply(2.0D));
						}

						/*
						 * if (shurikenCooldown.useAndResetCooldown()) { int amount = item.getAmount();
						 * if (amount > 0) { amount--; if (amount == 0)
						 * player.getInventory().clear(player.getInventory().getHeldItemSlot()); else
						 * item.setAmount(amount); ItemProjectile proj = new ItemProjectile(instance,
						 * player, new ProjectileOnHit() {
						 * 
						 * @Override public void onHit(Player hit) { player.playSound(hit.getLocation(),
						 * Sound.SUCCESSFUL_HIT, 1, 1); hit.damage(6.5, player); for (Player gamePlayer
						 * : instance.players) { gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE,
						 * 2, 1); // gamePlayer.getWorld().createExplosion(hit.getLocation().getX(), //
						 * hit.getLocation().getY(), hit.getLocation().getZ(), 3, false, false); }
						 * 
						 * }
						 * 
						 * }, new ItemStack(Material.TNT));
						 * instance.getManager().getProjManager().shootProjectile(proj,
						 * player.getEyeLocation(), player.getLocation().getDirection().multiply(2.0D));
						 * }
						 */
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void milk(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		GameInstance instance = getGameInstance(player);

		if (instance != null && instance.state == GameState.STARTED) {
			if (item != null && item.getType() == Material.MILK_BUCKET) {
				if (player.getGameMode() != GameMode.SPECTATOR) {
					player.removePotionEffect(PotionEffectType.POISON);
					player.setFireTicks(0);
					player.sendMessage("" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) "
							+ ChatColor.RESET + "You feel refreshed!");
					int amount = item.getAmount();
					if (amount > 0) {
						amount--;
						if (amount == 0)
							player.getInventory().clear(player.getInventory().getHeldItemSlot());
						else
							item.setAmount(amount);
					}
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void classSelector(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemMeta meta = compass.getItemMeta();
		GameInstance instance = getGameInstance(player);

		if (e.getItem() != null && e.getItem().getType() == Material.COMPASS) {
			if (instance != null)
				if (instance.state == GameState.WAITING)
					new ClassSelectorGUI(main).inv.open(e.getPlayer());

			if (player.getWorld() == main.getLobbyWorld())
				new HubGUI(main).inv.open(e.getPlayer());
		}
	}

	@EventHandler
	public void activeGames(PlayerInteractEvent e) {

		Player player = e.getPlayer();
		GameInstance i = this.GetInstanceOfPlayer(player);

		ItemStack eye = new ItemStack(Material.EYE_OF_ENDER);
		ItemMeta meta = eye.getItemMeta();

		if (e.getItem() != null && e.getItem().getType() == Material.EYE_OF_ENDER) {
			if (player.getWorld() == main.getLobbyWorld()) {
				e.setCancelled(true);
				new ActiveGamesGUI(getMain()).inv.open(player);
				int num = 0;
				for (Entry<Maps, GameInstance> entry : gameMap.entrySet()) {
					num++;
					player.sendMessage("" + ChatColor.YELLOW + "===-" + ChatColor.RESET + ChatColor.BOLD
							+ entry.getValue().getMap() + ChatColor.RESET + ChatColor.YELLOW + "-===");
					player.sendMessage("" + ChatColor.YELLOW + "|| " + ChatColor.RESET + ChatColor.BOLD + "Game Type: "
							+ ChatColor.GREEN + ChatColor.BOLD + ChatColor.ITALIC
							+ entry.getValue().gameType.toString());
					player.sendMessage("" + ChatColor.YELLOW + "|| " + ChatColor.RESET + ChatColor.BOLD + "Game State: "
							+ ChatColor.GREEN + ChatColor.BOLD + ChatColor.ITALIC + entry.getValue().state.getName());
					player.sendMessage("" + ChatColor.YELLOW + "|| " + ChatColor.RESET + ChatColor.BOLD + "Players: "
							+ ChatColor.GREEN + ChatColor.BOLD + entry.getValue().players.size() + "/"
							+ entry.getValue().gameType.getMaxPlayers());
					player.sendMessage("" + ChatColor.YELLOW + "|| " + ChatColor.RESET + ChatColor.BOLD
							+ "Alive Players: " + ChatColor.GREEN + ChatColor.BOLD + entry.getValue().alivePlayers + "/"
							+ entry.getValue().players.size());
					player.sendMessage("");
				}

				if (num == 0) {
					player.sendMessage(
							"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There are no games running at this time");
				} else {
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "There are " + ChatColor.YELLOW
							+ num + ChatColor.RESET + " games waiting/running right now");
				}
			} else if (i != null) {
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void OnPickupItem(PlayerPickupItemEvent event) {
		Item item = event.getItem();
		if (projManager.isProjectile(item)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void leaveGame(PlayerInteractEvent e) {
		ItemStack star = new ItemStack(Material.BARRIER);
		ItemMeta meta = star.getItemMeta();
		GameInstance instance = GetInstanceOfPlayer(e.getPlayer());

		if (e.getItem() != null && e.getItem().getType() == Material.BARRIER) {
			if (instance != null)
				if (instance.state == GameState.WAITING)
					new QuitGUI(main).invQuit.open(e.getPlayer());
		}
	}

	public void joinRandomGame(Player player, GameType type) {
		// Loop through each map & find map that is open
		List<Maps> existingMaps = new ArrayList<>();

		for (Entry<Maps, GameInstance> entry : this.gameMap.entrySet()) {
			if (entry.getKey().GetInstance().gameType == type) {
				if (entry.getValue().isOpen()) {
					this.JoinMap(player, entry.getKey());
					return;
				}
				existingMaps.add(entry.getKey());
			}
		}

		List<Maps> maps = Maps.getGameType(type);
		maps.removeAll(existingMaps);
		if (maps.size() > 0) {
			Maps map = maps.get(new Random().nextInt(maps.size()));
			this.JoinMap(player, map);
		} else {
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET
					+ "All games are full! Please contact CowNecromancer (lol)");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			GameInstance instance = GetInstanceOfPlayer(player);
			if (instance != null) {
				if (event instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
					if (damageEvent.getDamager() instanceof Player) {
						Player damager = (Player) damageEvent.getDamager();
						BaseClass baseClass = instance.classes.get(damager);
						baseClass.DoDamage(damageEvent);
					} else if (damageEvent.getDamager() instanceof WitherSkull) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 0, true));
					} /*
						 * else if (damageEvent.getDamager() instanceof TNTPrimed) {
						 * player.getWorld().createExplosion(player.getLocation().getX(),
						 * player.getLocation().getY(), player.getLocation().getZ(), 3, false, false); }
						 */
				}
				if (!event.isCancelled() && event.getFinalDamage() >= player.getHealth() - 0.2) {
					event.setCancelled(true);
					instance.PlayerDeath(player);
					return;
				} else
					instance.classes.get(player).TakeDamage(event);
			}
		}
	}

	/*
	 * @EventHandler public void StrongShield(PlayerMoveEvent event) { Player player
	 * = event.getPlayer(); GameInstance instance =
	 * this.GetInstanceOfPlayer(player);
	 * 
	 * if (instance != null) { if (player.getItemInHand().getType() ==
	 * Material.IRON_DOOR) { player.addPotionEffect(new
	 * PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999999, 2)); } else {
	 * player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE); } } }
	 */

	@EventHandler(priority = EventPriority.HIGHEST)
	public void FireballDamage(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			GameInstance instance = this.GetInstanceOfPlayer(player);
			if (instance != null) {
				BaseClass baseClass = instance.classes.get(player);
				baseClass.ProjectileLaunch(event);
			}
		}
	}

	public Main getMain() {
		return main;
	}

	public void playerSelectClass(Player player, ClassType type) {
		GameInstance instance = this.GetInstanceOfPlayer(player);
		if (instance != null)
			instance.setClass(player, type);
	}

	public GameInstance getWaitingMap() {
		for (Entry<Maps, GameInstance> entry : this.gameMap.entrySet()) {
			if (entry.getValue().state == GameState.WAITING && entry.getValue().players.size() > 0)
				// if (entry.getValue().gameType == GameType.NORMAL &&
				// entry.getValue().players.size() == 5)
				// return null;
				/* else */ if (entry.getValue().gameType == GameType.NORMAL && entry.getValue().players.size() < 5)
					return entry.getValue();
				// else if (entry.getValue().gameType == GameType.DUEL &&
				// entry.getValue().players.size() == 1)
				// return null;
				else if (entry.getValue().gameType == GameType.DUEL && entry.getValue().players.size() < 2)
					return entry.getValue();
				else if (entry.getValue().gameType == GameType.FRENZY)
					return entry.getValue();
		}
		return null;
	}

	public void JoinMap(Player player, Maps map) {
		GameReason result = main.getGameManager().AddPlayerToMap(player, map);
		GameInstance instance = this.GetInstanceOfPlayer(player);
		switch (result) {
		case SUCCESS:
			if (instance.gameType == GameType.CTF) {
				player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "You have joined " + ChatColor.RESET + ChatColor.WHITE + ChatColor.BOLD + map.toString());
				player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
			}

			if (instance.gameType == GameType.FRENZY) {
				player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "You have joined a Frenzy game, your class will be randomly selected each life");
			}

			player.setGameMode(GameMode.ADVENTURE);
			player.setAllowFlight(true);

			if (player.getWorld() != main.getLobbyWorld()) {
				player.getInventory().clear();
				if (instance.gameType != GameType.FRENZY) {
					ItemStack classItem = ItemHelper.setDetails(new ItemStack(Material.COMPASS),
							"" + ChatColor.GREEN + ChatColor.BOLD + "Class Selector",
							ChatColor.GRAY + "Click to choose a class!");
					player.getInventory().setItem(0, classItem);
				}

				ItemStack stats = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				SkullMeta statsMeta = (SkullMeta) stats.getItemMeta();
				statsMeta.setOwner(player.getName());
				stats.setItemMeta(statsMeta);

				player.getInventory().setItem(4,
						ItemHelper.setDetails(stats, "" + ChatColor.RESET + ChatColor.BOLD + "Profile"));

				ItemStack leaveItem = ItemHelper.setDetails(new ItemStack(Material.BARRIER),
						"" + ChatColor.RED + ChatColor.BOLD + "Leave Game",
						ChatColor.GRAY + "Click to leave your game");
				player.getInventory().setItem(8, leaveItem);
			}

			break;
		case ALREADY_IN:

			player.sendMessage(
					"" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You are already in a map!");

			break;

		case IN_ANOTHER:
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You are already in a game!");
			break;

		case ALREADYPLAYING:
			player.sendMessage(
					"" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This game is already playing!");
			break;
		}
	}

	public void SpectatorJoinMap(Player player, Maps map) {
		GameReason result = main.getGameManager().AddSpectatorToMap(player, map);
		GameInstance instance = getGameInstance(player);

		switch (result) {
		case SPECTATOR:
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You are now spectating " + ""
					+ ChatColor.GREEN + map.toString() + "." + ChatColor.RESET + " Use " + ChatColor.GREEN + "/leave "
					+ ChatColor.RESET + "to leave");
			player.setGameMode(GameMode.SPECTATOR);
			// player.setDisplayName("" + player.getName() + " " + ChatColor.RESET +
			// ChatColor.GRAY + ChatColor.ITALIC
			// + "Spectator" + ChatColor.RESET);
			// instance.GameScoreboard();
			break;

		case ALREADY_IN:
			player.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET
					+ "You have to leave your game to Spectate");
			break;

		case FAIL:
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "This game is not playing!");
			break;
		}
	}

	public GameReason AddSpectatorToMap(Player player, Maps map) {
		GameInstance instance = null;

		if (GetInstanceOfPlayer(player) != null) {
			player.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "(!) " + ChatColor.RESET
					+ "You have to leave your game to Spectate");
			return GameReason.IN_ANOTHER;
		}

		if (gameMap.containsKey(map))
			instance = gameMap.get(map);
		else {
			instance = new GameInstance(this, map);
			gameMap.put(map, instance);
		}

		return instance.AddSpectator(player);
	}

	public GameReason AddPlayerToMap(Player player, Maps map) {
		GameInstance instance = null;

		if (GetInstanceOfPlayer(player) != null) {
			return GameReason.IN_ANOTHER;
		}
		if (gameMap.containsKey(map))
			instance = gameMap.get(map);
		else {
			instance = new GameInstance(this, map);
			gameMap.put(map, instance);
		}

		GameReason reason = instance.AddPlayer(player);
		main.getNPCManager().updateNpc(map);

		return reason;
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		if ((event.getAction() == Action.PHYSICAL) && (event.getClickedBlock().getType() == Material.SOIL)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void FireballDamage(EntityExplodeEvent event) {
		if (event.getEntity() instanceof SmallFireball) {
			event.setCancelled(true);
			event.blockList().clear();
		} else if (event.getEntity() instanceof WitherSkull) {
			event.setCancelled(true);
			event.blockList().clear();
		} else if (event.getEntity() instanceof TNTPrimed) {
			event.setCancelled(true);
			event.getEntity().getWorld().playEffect(event.getEntity().getLocation(), Effect.EXPLOSION_HUGE, 1);
			event.blockList().clear();
		}
	}

	public GameInstance GetInstanceOfPlayer(Player player) {
		for (Entry<Maps, GameInstance> games : gameMap.entrySet())
			if (games.getValue().HasPlayer(player))
				return games.getValue();
		return null;
	}

	public GameInstance GetInstanceOfSpectator(Player spectator) {
		for (Entry<Maps, GameInstance> games : gameMap.entrySet())
			if (games.getValue().HasSpectator(spectator))
				return games.getValue();
		return null;
	}

	public GameInstance getInstanceOfMap(Maps map) {
		return gameMap.get(map);
	}

	public void RemovePlayerFromMap(Player player, Maps map, Player player2) {
		if (gameMap.containsKey(map)) {
			gameMap.get(map).RemovePlayer(player);
			main.getNPCManager().updateNpc(map);
		}
	}

	public boolean RemovePlayerFromAll(Player player) {
		PlayerData data = main.getDataManager().getPlayerData(player);
		GameInstance instance = main.getGameManager().GetInstanceOfPlayer(player);

		if (data.votes == 1) {
			if (instance != null) {
				instance.totalVotes--;
				data.votes = 0;
			}
		}

		List<Maps> toRemove = new ArrayList<>();
		System.out.println("Removing player from all..");
		boolean found = false;

		for (Entry<Maps, GameInstance> games : gameMap.entrySet())
			if (games.getValue().RemovePlayer(player)) {
				main.getNPCManager().updateNpc(games.getKey());
				System.out.println("Removed player from game");

				if (games.getValue().players.size() == 0) {
					toRemove.add(games.getKey());
				}
				found = true;
			}

		for (Maps maps : toRemove) {
			gameMap.remove(maps);
		}

		return found;
	}

	public void RemoveMap(Maps maps) {
		gameMap.remove(maps);
		main.getNPCManager().updateNpc(maps);
	}
}
