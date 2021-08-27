package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;

public class WitherSkeletonClass extends BaseClass {

	public WitherSkeletonClass(GameInstance instance, Player player) {
		super(instance, player);
	}

	@Override
	public ClassType getType() {
		return null;
	}

	public ItemStack makeBlack(ItemStack armor) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armor.getItemMeta();
		lm.setColor(Color.ORANGE);
		armor.setItemMeta(lm);
		return armor;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(makeBlack(new ItemStack(Material.LEATHER_HELMET)));
		playerEquip.setChestplate(makeBlack(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeBlack(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeBlack(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetNameTag() {

	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0, new ItemStack(Material.EYE_OF_ENDER));
		playerInv.setItem(1,
				ItemHelper.addEnchant(ItemHelper.addEnchant(new ItemStack(Material.BOW), Enchantment.DURABILITY, 1000),
						Enchantment.ARROW_INFINITE, 1));
		playerInv.setItem(2, new ItemStack(Material.ARROW));
	}
	
	@Override
	public void ProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntityType() == EntityType.ARROW) {
			event.setCancelled(true);
			//Firework firework = player.launchProjectile(Firework.class);
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		
	}

}
