package anthony.SuperCraftBrawl.anticheat;

import org.bukkit.event.Listener;

public class AntiCheat implements Listener {

	/*private Main main;
	private BukkitRunnable flyDetect;
	private BukkitRunnable speedDetect;
	private int ticks = 0;
	private int speedTicks = 0;
	private BukkitRunnable noSlowDetect;
	private List<Player> checkPlayers;
	private HashMap<Player, BukkitRunnable> flyRunnable = new HashMap<>();

	public AntiCheat(Main main) {
		this.main = main;
		this.checkPlayers = new ArrayList<Player>();
		this.main.getServer().getPluginManager().registerEvents(this, main);
	}

	public Main getMain() {
		return main;
	}

	public void TellAll(String message) {
		for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
			onlinePlayers.sendMessage(message);
		}
	}

	public void warnMac(Player player, String message) {
		checkPlayers.add(player);
	}

	@EventHandler
	public void FlyDetect(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PlayerData data = main.getDataManager().getPlayerData(player);
		int ping = ((CraftPlayer) player).getHandle().ping;

		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return;

		if (data.magicbroom == 0) {
			if (event.getTo().getY() - event.getFrom().getY() >= 1.5) {
				if (ping <= 60) {
					if (checkPlayers.contains(player)) {
						BukkitRunnable runnable = flyRunnable.get(player);
						if (runnable == null) {
							runnable = new BukkitRunnable() {
								int ticks = this.ticks;
								int count = 0;

								@Override
								public void run() {
									if (player.isOnGround()) {
										flyRunnable.remove(player);
										this.cancel();
									}
									if (count == 3) {
										int newPing = ((CraftPlayer) player).getHandle().ping;
										if (newPing <= 60) {
											player.kickPlayer(
													"You were removed from the game for: Suspicious Activity");
											TellAll(main.color("&9MAC: &e" + player.getName()
													+ " &7was removed due to suspicious activity"));
											warnMac(player, "" + player.getName()
													+ " was kicked due to Suspicious Activity. No longer spectating player");

											flyRunnable.remove(player);
											this.cancel();
										} else {
											// FIX LATER TO CHECK PING
										}
									}

									// Add ban system after 3-4 warnings

									if (ticks % 3 == 0) {
										for (Player staff : Bukkit.getOnlinePlayers()) {
											if (staff.hasPermission("scb.anticheat")) {
												staff.sendMessage(main.color("&9MAC: &e" + player.getName()
														+ " &7detected of &cFly Hacking (&e" + ping
														+ "ms&c)&7. Please investigate"));
											}
										}
										count++;
									}
									ticks++;
								}
							};
							runnable.runTaskTimer(main, 0, 20);
							flyRunnable.put(player, runnable);
						}
					} else {
						warnMac(player, "" + player.getName() + " suspected of possibly cheating. Now spectating..");
					}
			}
		}
	}

	@EventHandler
	public void ReachDetect(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Entity player = event.getEntity();
			Player damager = (Player) event.getDamager();
			int ping = ((CraftPlayer) damager).getHandle().ping;

			if (ping <= 50) {
				if (damager.getLocation().getX() - player.getLocation().getBlockX() > 3) {
					for (Player staff : Bukkit.getOnlinePlayers()) {
						if (staff.hasPermission("scb.anticheat")) {
							staff.sendMessage(main.color("&9MAC: &e" + damager.getName() + " &7detected of &cReach (&e"
									+ ping + "ms&c)&7. Please investigate"));
						}
					}
				}
			} else {
				if (damager.getLocation().getX() - player.getLocation().getBlockX() > 3) {
					for (Player staff : Bukkit.getOnlinePlayers()) {
						if (staff.hasPermission("scb.anticheat")) {
							staff.sendMessage(main.color(
									"&9MAC: &e" + damager.getName() + " &7detected of &cReach&7. Please investigate"));
							staff.sendMessage(main.color(
									"&9MAC: &e" + damager.getName() + "&7's ping is &e" + ping + "ms &c&l(HIGH)"));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void NoSlowDetect(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (item != null && item.getType() == Material.BOW && event.getAction() == Action.RIGHT_CLICK_AIR) {
			if (player.getWalkSpeed() < 0.2F) {
				return;
			} else {
				if (noSlowDetect == null) {
					noSlowDetect = new BukkitRunnable() {
						int ticks = 0;

						@Override
						public void run() {
							if (ticks == 1) {
								if (player.getWalkSpeed() >= 0.2F) {
									for (Player staff : Bukkit.getOnlinePlayers()) {
										if (staff.hasPermission("scb.anticheat")) {
											staff.sendMessage(main.color("&9MAC: &e" + player.getName()
													+ " &7detected of &cNo Slowdown&7. Please investigate"));
										}
									}
								}
								noSlowDetect = null;
								this.cancel();
							}

							ticks++;
						}

					};
					noSlowDetect.runTaskTimer(main, 0, 20);
				}
			}
		}
	}

	@EventHandler
	public void Broom(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PlayerData data = main.getDataManager().getPlayerData(player);

		if (data.magicbroom == 1) {
			if (player.isOnGround()) {
				data.magicbroom = 0;
			}
		}
	}

	public static void doDamageEvent(EntityDamageByEntityEvent event, Player damager) {
		if (!(event.getDamager() instanceof Player) || event.getCause() != DamageCause.ENTITY_ATTACK)
			return;
		Player player = (Player) event.getDamager();
		if (isCritical(player)) {
			if ((player.getLocation().getY() % 1.0 == 0 || player.getLocation().getY() % 0.5 == 0)
					&& player.getLocation().clone().subtract(0, 1.0, 0).getBlock().getType().isSolid()) {
				event.setCancelled(true);
				for (Player staff : Bukkit.getOnlinePlayers()) {
					if (staff.hasPermission("scb.anticheat")) {
						staff.sendMessage("" + ChatColor.BLUE + "MAC: " + ChatColor.YELLOW + damager.getName()
								+ ChatColor.GRAY + " detected of " + ChatColor.RED + "Criticals" + ChatColor.GRAY
								+ ". Please investigate");
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static boolean isCritical(Player player) {
		return player.getFallDistance() > 0.0f && !player.isOnGround() && !player.isInsideVehicle()
				&& !player.hasPotionEffect(PotionEffectType.BLINDNESS)
				&& player.getEyeLocation().getBlock().getType() != Material.LADDER;
	}*/
}
