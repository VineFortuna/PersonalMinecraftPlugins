package anthony.SuperCraftBrawl.Game.classes.all;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import net.md_5.bungee.api.ChatColor;

public class BabyCowClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(10000), shurikenCooldown = new Cooldown(10000);

	public BabyCowClass(GameInstance instance, Player player) {
		super(instance, player);
		// TODO Auto-generated constructor stub
	}

	public ItemStack makePurple(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.RED);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();

		meta.setOwner("BabyCow");
		meta.setDisplayName("");

		playerskull.setItemMeta(meta);

		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makePurple(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makePurple(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(
						ItemHelper.setDetails(new ItemStack(Material.RED_MUSHROOM),
								"" + ChatColor.RESET + "Red Mushoom", ChatColor.GRAY + "", ChatColor.YELLOW + ""),
						Enchantment.DAMAGE_ALL, 3));
		playerInv.setItem(1,
				ItemHelper.addEnchant(
						ItemHelper.setDetails(new ItemStack(Material.BROWN_MUSHROOM),
								"" + ChatColor.RESET + "Brown Mushroom", ChatColor.GRAY + "", ChatColor.YELLOW + ""),
						Enchantment.KNOCKBACK, 2));
		playerInv.setItem(3, new ItemStack(Material.MILK_BUCKET));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));

	}

	@Override
	public void DoDamage(EntityDamageByEntityEvent event) {
		Random rand = new Random();
		int chance = rand.nextInt(9);

		if (chance == 5) {
			if (event.getEntity() instanceof LivingEntity) {
				((LivingEntity) event.getEntity())
						.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 2, true));
			}
		} else if (chance == 3) {
			if (event.getEntity() instanceof LivingEntity) {
				((LivingEntity) event.getEntity())
						.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 2, true));
			}
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		/*
		 * ItemStack item = event.getItem(); if (item.getType() == Material.MILK_BUCKET
		 * && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() ==
		 * Action.RIGHT_CLICK_BLOCK)) { player.playSound(player.getLocation(),
		 * Sound.DRINK, 2, 1); player.sendMessage("" + ChatColor.BOLD + "(!) " +
		 * ChatColor.RESET + "You feel refreshed!"); for (PotionEffect type :
		 * player.getActivePotionEffects()) { player.removePotionEffect(type.getType());
		 * } player.removePotionEffect(PotionEffectType.WEAKNESS);
		 * player.removePotionEffect(PotionEffectType.POISON);
		 * player.removePotionEffect(PotionEffectType.ABSORPTION);
		 * player.removePotionEffect(PotionEffectType.BLINDNESS);
		 * player.removePotionEffect(PotionEffectType.CONFUSION);
		 * player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		 * player.removePotionEffect(PotionEffectType.FAST_DIGGING);
		 * player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		 * player.removePotionEffect(PotionEffectType.HARM);
		 * player.removePotionEffect(PotionEffectType.HEAL);
		 * player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
		 * player.removePotionEffect(PotionEffectType.HUNGER);
		 * player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		 * player.removePotionEffect(PotionEffectType.INVISIBILITY);
		 * player.removePotionEffect(PotionEffectType.JUMP);
		 * player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		 * player.removePotionEffect(PotionEffectType.REGENERATION);
		 * player.removePotionEffect(PotionEffectType.SATURATION);
		 * player.removePotionEffect(PotionEffectType.SLOW);
		 * player.removePotionEffect(PotionEffectType.WATER_BREATHING);
		 * player.removePotionEffect(PotionEffectType.WITHER); if
		 * (shurikenCooldown.useAndResetCooldown()) { int amount = item.getAmount(); if
		 * (amount > 0) { amount--; if (amount == 0)
		 * player.getInventory().clear(player.getInventory().getHeldItemSlot()); else
		 * item.setAmount(amount); } event.setCancelled(true); } }
		 */
	}

	@Override
	public ClassType getType() {
		return ClassType.BabyCow;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}
}
