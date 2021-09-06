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

public class SnowGolemClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public SnowGolemClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.3;
	}

	public ItemStack makeWhite(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.WHITE);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();

		meta.setOwner("SnowGolem");
		meta.setDisplayName("");

		playerskull.setItemMeta(meta);

		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makeWhite(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeWhite(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeWhite(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	public ItemStack getTNT() {
		return ItemHelper.setDetails(new ItemStack(Material.SNOW_BALL, 1), ChatColor.GREEN + "Snowballs");
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv
				.setItem(0,
						ItemHelper.addEnchant(
								ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.STICK),
										ChatColor.GREEN + "Map Knocker"), Enchantment.DAMAGE_ALL, 3),
								Enchantment.KNOCKBACK, 2));
		ItemStack tnt = getTNT();
		tnt.setAmount(12);
		playerInv.setItem(1, tnt);
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		/*
		 * if (item.getType() == Material.STICK && (event.getAction() ==
		 * Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
		 * if (boosterCooldown.useAndResetCooldown()) { double boosterStrength = 1.5;
		 * for (Player gamePlayer : instance.players)
		 * gamePlayer.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 1); Vector
		 * vel = player.getLocation().getDirection().multiply(boosterStrength);
		 * player.setVelocity(vel); } }
		 */ if (item.getType() == Material.SNOW_BALL) {
			if (player.getGameMode() != GameMode.SPECTATOR) {
				if (shurikenCooldown.useAndResetCooldown()) {
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
								hit.damage(3.8, player);
								for (Player gamePlayer : instance.players) {
									gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 1, 1);
									// gamePlayer.getWorld().createExplosion(hit.getLocation().getX(),
									// hit.getLocation().getY(), hit.getLocation().getZ(), 3, false, false);
									gamePlayer.playEffect(hit.getLocation(), Effect.EXPLOSION_HUGE, 1);
								}

							}

						}, new ItemStack(Material.SNOW_BALL));
						instance.getManager().getProjManager().shootProjectile(proj, player.getEyeLocation(),
								player.getLocation().getDirection().multiply(2.0D));
					}
					event.setCancelled(true);
				}
			}
		}
	}

	@Override
	public ClassType getType() {
		return ClassType.SnowGolem;
	}

	@Override
	public void SetNameTag() {

	}

}
