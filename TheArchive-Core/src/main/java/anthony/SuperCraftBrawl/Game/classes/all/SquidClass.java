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

public class SquidClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(10000), shurikenCooldown = new Cooldown(10000);

	public SquidClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.1;
	}

	public ItemStack makePurple(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.PURPLE);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();

		meta.setOwner("Squid");
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
		playerInv
				.setItem(0,
						ItemHelper.addEnchant(ItemHelper.addEnchant(
								ItemHelper.setDetails(new ItemStack(Material.INK_SACK),
										"" + ChatColor.RESET + "Ink Sack", ChatColor.GRAY + "", ChatColor.YELLOW + ""),
								Enchantment.DAMAGE_ALL, 3), Enchantment.KNOCKBACK, 2));

	}

	@Override
	public void DoDamage(EntityDamageByEntityEvent event) {
		Random rand = new Random();
		int chance = rand.nextInt(9);

		if (chance == 5) {
			if (event.getEntity() instanceof LivingEntity) {
				((LivingEntity) event.getEntity())
						.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 2, true));
			}
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
	}

	@Override
	public ClassType getType() {
		return ClassType.Squid;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}
}
