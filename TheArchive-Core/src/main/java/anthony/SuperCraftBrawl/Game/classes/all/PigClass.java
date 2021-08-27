package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;

public class PigClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public PigClass(GameInstance instance, Player player) {
		super(instance, player);
	}

	public ItemStack makePink(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.fromRGB(255, 105, 180));
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(makePink(new ItemStack(Material.LEATHER_HELMET)));
		playerEquip.setChestplate(makePink(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makePink(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makePink(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(new ItemStack(Material.PORK), Enchantment.DAMAGE_ALL, 4),
						Enchantment.KNOCKBACK, 1));
	}

	@Override
	public void TakeDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2, true));
			event.getEntity().sendMessage(instance.getManager().getMain().color("&6&l(!) &rOuch! You gained some speed"));
			for (Player gamePlayer : instance.players) {
				gamePlayer.playSound(player.getLocation(), Sound.PIG_DEATH, 1, 1);
			}
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {

	}

	@Override
	public ClassType getType() {
		return ClassType.Pig;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}
}
