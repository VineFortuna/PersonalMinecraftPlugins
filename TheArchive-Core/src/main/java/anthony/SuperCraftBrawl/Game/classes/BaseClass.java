package anthony.SuperCraftBrawl.Game.classes;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Score;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Timer;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.GameType;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import net.md_5.bungee.api.ChatColor;

public abstract class BaseClass {

	protected final GameInstance instance;
	protected final Player player;
	public int lives = 5;
	public boolean isDead = false;
	public int tokens = 0;
	public Score score;
	public int totalTokens = 0;
	public int totalKills = 0;
	public int totalExp = 0;
	public double baseVerticalJump = 1.0;
	public Timer pearlTimer = new Timer();
	public Timer tnt = new Timer();
	public Timer broom = new Timer();
	public Timer slimeBall = new Timer();

	public BaseClass(GameInstance instance, Player player) {
		this.instance = instance;
		this.player = player;
	}

	public int getLives() {
		return lives;
	}

	public int getTokens() {
		return tokens;
	}

	public abstract ClassType getType();

	public abstract void SetArmour(EntityEquipment playerEquip);

	public abstract void SetNameTag();

	public abstract void SetItems(Inventory playerInv);

	public abstract void UseItem(PlayerInteractEvent event);

	public void TakeDamage(EntityDamageEvent event) {
	}; // To override

	public void ProjectileLaunch(ProjectileLaunchEvent event) {
	}; // To override

	public void DoDamage(EntityDamageByEntityEvent event) {
	} // To override

	public void Tick(int gameTicks) {
	} // To override

	public void LoadPlayer() {
		Inventory inv = player.getInventory();
		SetArmour(player.getEquipment());
		SetItems(inv);
	}

	public void LoadArmor(Player player) {
		SetArmour(player.getEquipment());
	}

	public void Death(PlayerDeathEvent e) {
		for (Player player : instance.players) {
			if (player.getName() != null) {
				PlayerDeath();
			}
		}
	}

	public void PlayerDeath() {
		if (player.getGameMode() != GameMode.SPECTATOR) {
			lives--;
			score.setScore(lives);

			Player killer = player.getKiller();
			Player p = player.getPlayer();

			PlayerData data2 = instance.getManager().getMain().getDataManager().getPlayerData(p);
			data2.deaths += 1;

			if (killer != null && killer != p) {
				PlayerData data = instance.getManager().getMain().getDataManager().getPlayerData(killer);
				BaseClass baseClass = instance.classes.get(killer);
				data.tokens += 1;
				data.kills += 1;
				data.exp += 16;
				baseClass.totalExp += 16;

				if (instance.getManager().getMain().tournament == true)
					data.points++;

				killer.playSound(killer.getLocation(), Sound.SUCCESSFUL_HIT, 2, 1);

				if (baseClass != null) {
					baseClass.totalTokens += 1;
					baseClass.totalKills++;
				}
			} else {
				p.teleport(instance.GetSpecLoc());
			}

			p.removePotionEffect(PotionEffectType.SPEED);
			p.removePotionEffect(PotionEffectType.WEAKNESS);
			p.removePotionEffect(PotionEffectType.POISON);
			p.removePotionEffect(PotionEffectType.ABSORPTION);
			p.removePotionEffect(PotionEffectType.BLINDNESS);
			p.removePotionEffect(PotionEffectType.CONFUSION);
			p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			p.removePotionEffect(PotionEffectType.FAST_DIGGING);
			p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			p.removePotionEffect(PotionEffectType.HARM);
			p.removePotionEffect(PotionEffectType.HEAL);
			p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			p.removePotionEffect(PotionEffectType.HUNGER);
			p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			p.removePotionEffect(PotionEffectType.JUMP);
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			p.removePotionEffect(PotionEffectType.REGENERATION);
			p.removePotionEffect(PotionEffectType.SLOW);
			p.removePotionEffect(PotionEffectType.WATER_BREATHING);
			p.removePotionEffect(PotionEffectType.WITHER);
			p.setFireTicks(0);
			p.setLastDamage(0);

			data2.winstreak = 0;

			BaseClass baseClass = instance.classes.get(killer);
			BaseClass baseClass2 = instance.classes.get(p);

			if (killer != null && player.getKiller() instanceof Player) {
				if (killer != p) {
					Random rand = new Random();
					int chance = rand.nextInt(2);

					if (chance == 0) {
						killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
						killer.sendMessage(
								"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
										+ "You killed " + ChatColor.RESET + p.getPlayer().getName() + ChatColor.YELLOW
										+ " and got rewarded a " + ChatColor.GOLD + ChatColor.BOLD + "Golden Apple");
					} else {
						ItemStack item = ItemHelper.setDetails(new ItemStack(Material.POTION, 1),
								"" + ChatColor.YELLOW + ChatColor.BOLD + "Health Pot");
						Potion pot = new Potion(1);
						pot.setType(PotionType.INSTANT_HEAL);
						pot.setSplash(true);
						pot.apply(item);

						killer.getInventory().addItem(item);
						killer.sendMessage(
								"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + ChatColor.YELLOW
										+ "You killed " + ChatColor.RESET + p.getPlayer().getName() + ChatColor.YELLOW
										+ " and got rewarded a " + ChatColor.YELLOW + ChatColor.BOLD + "Health Pot");
					}

					if (instance.gameType == GameType.FRENZY) {
						TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ instance.testDisplay(p) + p.getPlayer().getName() + " "
								+ /* baseClass2.getType().getTag() */instance.oldBaseClass.getType().getTag()
								+ ChatColor.RED + " was killed by " + ChatColor.WHITE + instance.testDisplay(killer)
								+ killer.getName() + " " + baseClass.getType().getTag());
						p.teleport(killer);
					} else {
						TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ instance.testDisplay(p) + p.getPlayer().getName() + " "
								+ baseClass2.getType().getTag() + ChatColor.RED + " was killed by " + ChatColor.WHITE
								+ instance.testDisplay(killer) + killer.getName() + " " + baseClass.getType().getTag());
						p.teleport(killer);
					}
				} else {
					if (instance.gameType == GameType.FRENZY) {
						TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ instance.testDisplay(p) + p.getPlayer().getName() + " "
								+ /* baseClass2.getType().getTag() */instance.oldBaseClass.getType().getTag()
								+ ChatColor.RED + " committed suicide");
						p.teleport(killer);
					} else {
						TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ instance.testDisplay(p) + p.getPlayer().getName() + " "
								+ baseClass2.getType().getTag() + ChatColor.RED + " committed suicide");
						p.teleport(killer);
					}
				}
			}
			/*
			 * else if (DamageCause.LAVA != null) { TellAll("" + ChatColor.DARK_GREEN +
			 * ChatColor.BOLD + "(!) " + ChatColor.RESET + p.getPlayer().getName() +
			 * ChatColor.RED + " burnt to a crisp in lava"); }
			 */
			else if (DamageCause.VOID != null) {
				if (instance.gameType == GameType.FRENZY) {
					TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ instance.testDisplay(p) + p.getPlayer().getName() + " "
							+ /* baseClass2.getType().getTag() */instance.oldBaseClass.getType().getTag()
							+ ChatColor.RED + " just died SO badly");
					p.getPlayer().setFireTicks(0);
				} else {
					TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ instance.testDisplay(p) + p.getPlayer().getName() + " " + baseClass2.getType().getTag()
							+ ChatColor.RED + " just died SO badly");
					p.getPlayer().setFireTicks(0);
				}
			} else if (DamageCause.SUICIDE != null) {
				if (instance.gameType == GameType.FRENZY) {
					TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ instance.testDisplay(p) + p.getPlayer().getName() + " "
							+ /* baseClass2.getType().getTag() */instance.oldBaseClass.getType().getTag()
							+ ChatColor.RED + " committed suicide");
					p.getPlayer().setFireTicks(0);
				} else {
					TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ instance.testDisplay(p) + p.getPlayer().getName() + " " + baseClass2.getType().getTag()
							+ ChatColor.RED + " committed suicide");
					p.getPlayer().setFireTicks(0);
				}
			} else if (killer == null && !(player.getKiller() instanceof Player) && DamageCause.LAVA != null
					|| DamageCause.FIRE != null || DamageCause.FIRE_TICK != null) {
				TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + p.getPlayer().getName()
						+ ChatColor.RED + " just burnt to death");
			}

			if (lives == 0) {
				p.setDisplayName("" + p.getName() + " " + ChatColor.RESET + ChatColor.GRAY + ChatColor.ITALIC
						+ "Spectator" + ChatColor.RESET);
				if (instance.gameType == GameType.FRENZY) {
					TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ instance.testDisplay(p) + p.getPlayer().getName() + " "
							+ instance.oldBaseClass.getType().getTag() + ChatColor.RED + " has been eliminated!");
				} else {
					TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ instance.testDisplay(p) + p.getPlayer().getName() + " " + baseClass2.getType().getTag()
							+ ChatColor.RED + " has been eliminated!");
				}
				PlayerData data = instance.getManager().getMain().getDataManager().getPlayerData(p);
				Random rand = new Random();
				int chance = rand.nextInt(15);

				if (chance == 10) {
					data.mysteryChests++;
					player.sendMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "(!) " + ChatColor.RESET
							+ "You have " + ChatColor.YELLOW + "1 " + ChatColor.RESET + "new Mystery Chest available!");
				} else if (chance == 12) {
					data.mysteryChests += 2;
					player.sendMessage(
							"" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have "
									+ ChatColor.YELLOW + "2 " + ChatColor.RESET + "new Mystery Chests available!");
				}

				BaseClass baseClass3 = instance.classes.get(p);
				p.sendMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "(!) " + ChatColor.RESET
						+ "You have gained " + ChatColor.YELLOW + baseClass3.totalExp + " EXP!");
				PlayerData data3 = instance.getManager().getMain().getDataManager().getPlayerData(p);

				if (data3.exp >= 5000) {
					data3.level++;
					data3.exp -= 5000;
					p.sendMessage("Level upgraded to " + data3.level + "!");
				}

			} else if (lives == 1) {
				TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + instance.testDisplay(p)
						+ p.getPlayer().getName() + " " + baseClass2.getType().getTag() + ChatColor.RED + " has "
						+ lives + " life left");
			} else {

				TellAll("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET + instance.testDisplay(p)
						+ p.getPlayer().getName() + " " + baseClass2.getType().getTag() + ChatColor.RED + " has "
						+ lives + " lives left");

				return;
			}
		}
	}

	public void TellAll(String message) {
		for (Player player : instance.players) {
			player.sendMessage(message);
		}
		for (Player spectator : instance.spectators) {
			spectator.sendMessage(message);
		}
	}
}
