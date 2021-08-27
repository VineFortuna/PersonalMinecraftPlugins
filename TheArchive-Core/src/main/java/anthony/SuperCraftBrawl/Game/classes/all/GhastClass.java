package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
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
import net.md_5.bungee.api.ChatColor;

public class GhastClass extends BaseClass{
	
	private Cooldown boosterCooldown = new Cooldown(10000), shurikenCooldown = new Cooldown(200);

	public GhastClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.2;
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
		
		meta.setOwner("ghast");
		meta.setDisplayName("");
		
		playerskull.setItemMeta(meta);
		
		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makeWhite(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeWhite(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeWhite(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.GHAST_TEAR), ChatColor.GRAY + "Ghast Tear",
						ChatColor.GRAY + "",
						ChatColor.YELLOW + ""), Enchantment.DAMAGE_ALL, 1), Enchantment.KNOCKBACK, 1));
		playerInv.setItem(1,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.BOW), ChatColor.GRAY + "Ghast Bow",
						ChatColor.GRAY + "",
						ChatColor.YELLOW + ""), Enchantment.ARROW_FIRE, 1), Enchantment.ARROW_INFINITE, 1), Enchantment.DURABILITY, 10000), 
						Enchantment.ARROW_KNOCKBACK, 1));
		playerInv.setItem(2, new ItemStack(Material.ARROW));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {

	}
	
	@Override
	public ClassType getType() {
		return ClassType.Ghast;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}

}
