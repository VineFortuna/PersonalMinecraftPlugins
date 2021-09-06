package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import anthony.SuperCraftBrawl.Game.projectile.ItemProjectile;
import anthony.SuperCraftBrawl.Game.projectile.ProjectileOnHit;
import net.md_5.bungee.api.ChatColor;

/**
 * Example of a class that shows different features
 */
public class NinjaClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(6000), shurikenCooldown = new Cooldown(200);

	public NinjaClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.3;
	}

	public ItemStack makeBlack(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.BLACK);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();

		meta.setOwner("_fergul_");
		meta.setDisplayName("");

		playerskull.setItemMeta(meta);

		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makeBlack(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeBlack(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeBlack(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	public ItemStack getShuriken() {
		return ItemHelper.setDetails(new ItemStack(Material.NETHER_STAR, 1), ChatColor.GRAY + "Shuriken",
				ChatColor.YELLOW + "Right click to throw a deadly star");
	}

	@Override
	public void DoDamage(EntityDamageByEntityEvent event) {
		if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.GHAST_TEAR) {
			if (player.getLocation().distanceSquared(event.getEntity().getLocation()) > 1.0)
				event.setCancelled(true);
			else {
				for (Player gamePlayer : instance.players)
					gamePlayer.playSound(event.getEntity().getLocation(), Sound.BAT_DEATH, 1, 2);
			}
		}
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0, ItemHelper.addEnchant(
				ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.STICK), ChatColor.GRAY + "Katana",
						ChatColor.GRAY + "Slice your enemies to pieces",
						ChatColor.YELLOW + "Right click to boost forwards!"), Enchantment.DAMAGE_ALL, 3),
				Enchantment.KNOCKBACK, 1));
		playerInv.setItem(1,
				ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.GHAST_TEAR),
						ChatColor.GRAY + "Wakizashi", ChatColor.GRAY + "Deals damage class range"),
						Enchantment.DAMAGE_ALL, 8));
		ItemStack shuriken = getShuriken();
		shuriken.setAmount(5);
		playerInv.setItem(2, shuriken);

	}

	private int getNumberOfShurikens() {
		int count = 0;
		for (ItemStack item : player.getInventory().getContents())
			if (item != null && item.getType() == Material.NETHER_STAR)
				count += item.getAmount();
		return count;
	}

	@Override
	public void Tick(int gameTicks) {
		if (gameTicks % 20 == 0) {
			int number = getNumberOfShurikens();
			if (number < 5)
				player.getInventory().addItem(getShuriken());
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item.getType() == Material.STICK
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (boosterCooldown.useAndResetCooldown()) {
				double boosterStrength = 1.5;
				for (Player gamePlayer : instance.players)
					gamePlayer.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 1);
				Vector vel = player.getLocation().getDirection().multiply(boosterStrength);
				player.setVelocity(vel);
			}
		} else if (item.getType() == Material.NETHER_STAR) {
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
								hit.damage(1.5, player);
								for (Player gamePlayer : instance.players)
									gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 2, 1);

							}

						}, new ItemStack(Material.NETHER_STAR));
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
		return ClassType.Ninja;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}

}
