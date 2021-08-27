package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import anthony.SuperCraftBrawl.Game.projectile.ItemProjectile;
import anthony.SuperCraftBrawl.Game.projectile.ProjectileOnHit;
import net.md_5.bungee.api.ChatColor;

public class SlimeClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(5000);

	public SlimeClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.3;
	}

	public ItemStack makeYellow(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.YELLOW);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();

		meta.setOwner("Slimess");
		meta.setDisplayName("");

		playerskull.setItemMeta(meta);

		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makeYellow(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeYellow(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeYellow(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		
		/*BaseClass baseClass = instance.classes.get(player);
		player.setCustomName("" + baseClass.getType().getTag() + ChatColor.RESET + player.getName());
		player.setCustomNameVisible(true);*/
	}

	public ItemStack getSlimeBall() {
		return ItemHelper.setDetails(new ItemStack(Material.SLIME_BALL, 1),
				"" + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD + "Goey Grenade",
				ChatColor.YELLOW + "Right click to throw DEADLY slime balls!");
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv
				.setItem(0,
						ItemHelper
								.addEnchant(
										ItemHelper.addEnchant(
												ItemHelper.setDetails(new ItemStack(Material.WOOD_SWORD),
														"" + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD
																+ "Goey Sword",
														ChatColor.GRAY + "Slice your enemies to pieces"),
												Enchantment.DAMAGE_ALL, 1),
										Enchantment.DURABILITY, 10000));
		ItemStack slime = getSlimeBall();
		slime.setAmount(1);
		playerInv.setItem(1, slime);

	}

	private int getNumberOfSlime() {
		int count = 0;
		for (ItemStack item : player.getInventory().getContents())
			if (item != null && item.getType() == Material.SLIME_BALL)
				count += item.getAmount();
		return count;
	}

	/*
	 * @Override public void Tick(int gameTicks) { if (gameTicks % 20 == 0) { int
	 * number = getNumberOfTNT(); if (number < 5)
	 * player.getInventory().addItem(getTNT()); } }
	 */

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item.getType() == Material.SLIME_BALL) {
			if (player.getGameMode() != GameMode.SPECTATOR) {
				if (slimeBall.getTime() < 5000) {
					int seconds = (5000 - slimeBall.getTime()) / 1000 + 1;
					event.setCancelled(true);
					player.sendMessage(
							"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Your goo is still regenerating for " + ChatColor.YELLOW
									+ seconds + " more seconds ");
				} else {
					slimeBall.restart();
					ItemProjectile proj = new ItemProjectile(instance, player, new ProjectileOnHit() {
						@Override
						public void onHit(Player hit) {
							player.playSound(hit.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
							hit.damage(4.0, player);
							for (Player gamePlayer : instance.players) {
								gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 2, 1);
								//gamePlayer.getWorld().createExplosion(hit.getLocation().getX(),
										//hit.getLocation().getY(), hit.getLocation().getZ(), 3, false, false);
								gamePlayer.playEffect(hit.getLocation(), Effect.EXPLOSION_LARGE, 1);
							}

						}

					}, new ItemStack(Material.SLIME_BALL));
					instance.getManager().getProjManager().shootProjectile(proj, player.getEyeLocation(),
							player.getLocation().getDirection().multiply(2.0D));
				}
				
				/*if (shurikenCooldown.useAndResetCooldown()) {
					ItemProjectile proj = new ItemProjectile(instance, player, new ProjectileOnHit() {
						@Override
						public void onHit(Player hit) {
							player.playSound(hit.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
							hit.damage(4.0, player);
							for (Player gamePlayer : instance.players) {
								gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 2, 1);
								//gamePlayer.getWorld().createExplosion(hit.getLocation().getX(),
										//hit.getLocation().getY(), hit.getLocation().getZ(), 3, false, false);
							}

						}

					}, new ItemStack(Material.SLIME_BALL));
					instance.getManager().getProjManager().shootProjectile(proj, player.getEyeLocation(),
							player.getLocation().getDirection().multiply(2.0D));
				}*/
				event.setCancelled(true);
			}
		}
	}

	@Override
	public ClassType getType() {
		return ClassType.Slime;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}
}
