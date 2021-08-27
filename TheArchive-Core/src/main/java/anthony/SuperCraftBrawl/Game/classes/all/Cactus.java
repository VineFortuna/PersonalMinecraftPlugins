package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import net.md_5.bungee.api.ChatColor;

public class Cactus extends BaseClass{
	
	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public Cactus(GameInstance instance, Player player) {
		super(instance, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		
		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();
		
		meta.setOwner("Cactus");
		meta.setDisplayName("");
		
		playerskull.setItemMeta(meta);
		
		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(ItemHelper.addEnchant(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.THORNS, 1), Enchantment.PROTECTION_ENVIRONMENTAL, 4));
		playerEquip.setLeggings(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_LEGGINGS), Enchantment.THORNS, 1));
		playerEquip.setBoots(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4));
		
	}
	
	public ItemStack getCactus() {
		return ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.CACTUS, 1), "" + ChatColor.RED + ChatColor.BOLD + "Spiker",
				ChatColor.YELLOW + ""), Enchantment.KNOCKBACK, 1);
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.WOOD_SWORD), "" + ChatColor.GREEN + ChatColor.BOLD + "Spikey Sword",
						ChatColor.GRAY + "Chop your enemies to pieces!",
						ChatColor.YELLOW + ""), Enchantment.DAMAGE_ALL, 1), Enchantment.DURABILITY, 10000));
		ItemStack cactus = getCactus();
		cactus.setAmount(1);
		playerInv.setItem(1, cactus);
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		
	}
	
	@Override
	public ClassType getType() {
		return ClassType.Cactus;
	}
	
	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}
}
