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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;

public class EnderdragonClass extends BaseClass{
	
	private Cooldown boosterCooldown = new Cooldown(10000), shurikenCooldown = new Cooldown(200);
	private Cooldown enderPearlCooldown = new Cooldown (10000);

	public EnderdragonClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.5;
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
		
		meta.setOwner("Ender_dragon");
		meta.setDisplayName("");
		
		playerskull.setItemMeta(meta);
		
		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makeBlack(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeBlack(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeBlack(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.addItem(new ItemStack(Material.STONE_SWORD));
		playerInv.addItem(new ItemStack(Material.ENDER_PEARL, 5));
		player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 999999999, 1));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		
	}
	
	@Override
	public ClassType getType() {
		return ClassType.Enderdragon;
	}
	
	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}


}
