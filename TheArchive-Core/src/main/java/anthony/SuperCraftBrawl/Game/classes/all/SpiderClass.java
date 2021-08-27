package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
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

public class SpiderClass extends BaseClass{
	
	private Cooldown boosterCooldown = new Cooldown(10000), shurikenCooldown = new Cooldown(10000);

	public SpiderClass(GameInstance instance, Player player) {
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
		
		meta.setOwner("Spidarr");
		meta.setDisplayName("");
		
		playerskull.setItemMeta(meta);
		
		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makePurple(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.SPIDER_EYE), "" + ChatColor.RESET + ChatColor.GREEN + "Spider Eye",
						ChatColor.GRAY + "",
						ChatColor.YELLOW + ""), Enchantment.DAMAGE_ALL, 2), Enchantment.KNOCKBACK, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 2));
	}
	
	@Override
	public void DoDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			((LivingEntity)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 0, true));
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item.getType() == Material.SPIDER_EYE
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			event.setCancelled(true);
		}
	}
	
	
	@Override
	public ClassType getType() {
		return ClassType.Spider;
	}
	
	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}
}
