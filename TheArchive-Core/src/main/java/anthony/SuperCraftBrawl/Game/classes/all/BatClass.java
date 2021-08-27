package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
import net.md_5.bungee.api.ChatColor;

public class BatClass extends BaseClass{
	
	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public BatClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.6;
	}
	
	public ItemStack makeRed(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.RED);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(new ItemStack(Material.SKULL_ITEM));
		playerEquip.setChestplate(new ItemStack(Material.AIR));
		playerEquip.setLeggings(new ItemStack(Material.AIR));
		playerEquip.setBoots(new ItemStack(Material.AIR));
	}
	
	/*public ItemStack getTNT() {
		return ItemHelper.setDetails(new ItemStack(Material.TNT, 1), ChatColor.GRAY + "TNT",
				ChatColor.YELLOW + "Right click to throw a deadly TNT");
	}*/
	
	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.SHEARS), ChatColor.GREEN + "Shears",
						ChatColor.GRAY + "Beat your enemies to peices!",
						ChatColor.YELLOW + ""), Enchantment.KNOCKBACK, 1), Enchantment.DAMAGE_ALL, 4), Enchantment.DURABILITY, 10000));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0));
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999, 1));

	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		
	}

	@Override
	public ClassType getType() {
		return ClassType.Bat;
	}
	
	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}

}
