package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

public class RabbitClass extends BaseClass{
	
	private Cooldown boosterCooldown = new Cooldown(10000), shurikenCooldown = new Cooldown(10000);

	public RabbitClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.1;
	}

	public ItemStack makePurple(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.GRAY);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setChestplate(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makePurple(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.RABBIT_FOOT), "" + ChatColor.RESET + "Rabbit Foot",
						ChatColor.GRAY + "",
						ChatColor.YELLOW + ""), Enchantment.DAMAGE_ALL, 2), Enchantment.KNOCKBACK, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 2));
	
	}
	
	@Override
	public void DoDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			((LivingEntity)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 35, 2, true));
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
	}
	
	
	@Override
	public ClassType getType() {
		return ClassType.Melon;
	}
	
	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}
}
