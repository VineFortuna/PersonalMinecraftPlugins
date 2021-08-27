package anthony.SuperCraftBrawl.Game.classes.all;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
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

public class SheepClass extends BaseClass{
	
	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public SheepClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.2;
	}
	
	public ItemStack makeRed(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.RED);
		armour.setItemMeta(lm);
		return armour;
	}
	
	public ItemStack makeYellow(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.YELLOW);
		armour.setItemMeta(lm);
		return armour;
	}
	
	public ItemStack makeOrange(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.ORANGE);
		armour.setItemMeta(lm);
		return armour;
	}
	
	public ItemStack makeBlue(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.BLUE);
		armour.setItemMeta(lm);
		return armour;
	}
	
	public ItemStack makePurple(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.PURPLE);
		armour.setItemMeta(lm);
		return armour;
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
		
		meta.setOwner("DerpTheSheep");
		meta.setDisplayName("");
		
		playerskull.setItemMeta(meta);
		
		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makeWhite(new ItemStack(Material.LEATHER_CHESTPLATE)));
		playerEquip.setLeggings(makeWhite(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeWhite(new ItemStack(Material.LEATHER_BOOTS)));
	}
	
	public ItemStack getStartWool() {
		player.setDisplayName("" + player.getName() + " " + ChatColor.RESET + ChatColor.BOLD + "Sheep" +
				ChatColor.RESET);
		return ItemHelper.setDetails(new ItemStack(Material.WOOL, 1), "" + ChatColor.RESET + "White Wool",
				ChatColor.YELLOW + "Right click!");
	}
	
	public ItemStack getStartEnchanter() {
		return ItemHelper.setDetails(new ItemStack(Material.ENCHANTMENT_TABLE, 1), "" + ChatColor.BLUE + "Wool Enchanter",
				ChatColor.YELLOW + "Right click!");
	}
	
	@Override
	public void SetItems(Inventory playerInv) {
		ItemStack whiteWool = getStartWool();
		whiteWool.setAmount(1);
		playerInv.setItem(0, whiteWool);
		
		ItemStack enchanter = getStartEnchanter();
		enchanter.setAmount(1);
		playerInv.setItem(1, enchanter);
	}
	
	public void SetRedWool() {
		player.getInventory().setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.WHEAT), "" + ChatColor.BLACK + ChatColor.BOLD + "Magic Broom",
						ChatColor.GRAY + "",
						ChatColor.YELLOW + "Smack your enemies with this!"), Enchantment.DAMAGE_ALL, 1), Enchantment.KNOCKBACK, 1));
	}
	
	public void TestItems() {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		
		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();
		
		meta.setOwner("DerpTheSheep");
		meta.setDisplayName("");
		
		playerskull.setItemMeta(meta);
		
		ItemStack item3 = ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.WOOL, 1, DyeColor.RED.getData()), "" + ChatColor.RED + ChatColor.BOLD + "Red Wool",
				ChatColor.GRAY + "",
				ChatColor.YELLOW + "Smack your enemies with this!"), Enchantment.FIRE_ASPECT, 2), Enchantment.DAMAGE_ALL, 2);
		
		ItemStack item2 = ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.WOOL, 1, DyeColor.ORANGE.getData()), "" + ChatColor.GREEN + ChatColor.BOLD + "Orange Wool",
				ChatColor.GRAY + "",
				ChatColor.YELLOW + "Smack your enemies with this!"), Enchantment.DAMAGE_ALL, 4);
		
		ItemStack item = ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getData()), "" + ChatColor.BLUE + ChatColor.BOLD + "Blue Wool",
				ChatColor.GRAY + "",
				ChatColor.YELLOW + "Smack your enemies with this!"), Enchantment.KNOCKBACK, 2), Enchantment.DAMAGE_ALL, 2);
		
		ItemStack item4 = ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.WOOL, 1, DyeColor.PURPLE.getData()), "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Purple Wool",
				ChatColor.GRAY + "",
				ChatColor.YELLOW + "Smack your enemies with this!"), Enchantment.KNOCKBACK, 6), Enchantment.DAMAGE_ALL, 4);
		
		ItemStack item5 = ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData()), "" + ChatColor.YELLOW + ChatColor.BOLD + "Yellow Wool",
				ChatColor.GRAY + "",
				ChatColor.YELLOW + "Smack your enemies with this!"), Enchantment.DAMAGE_ALL, 3);
		
		ItemStack[] itemList = {item5, item3, item, item4, item3, item, item3, item, item3, item2, item, item3, item2, item,
				item3, item2, item, item3, item2, item, item3, item5, item5, item5, item2, item, item3, item2, item};
		Random rand = new Random();
		int randomNum = rand.nextInt(itemList.length);
		
		if (itemList[randomNum] == item3) {
			player.getInventory().setChestplate(makeRed(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			player.getInventory().setLeggings(makeRed(new ItemStack(Material.LEATHER_LEGGINGS)));
			player.getInventory().setBoots(makeRed(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You were given " + ChatColor.RED + ChatColor.BOLD + 
					"RED WOOL");
			player.setDisplayName("" + player.getName() + " " + ChatColor.RED + ChatColor.BOLD + "Sheep" +
					ChatColor.RESET);
		}
		else if (itemList[randomNum] == item2) {
			player.getInventory().setChestplate(makeOrange(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			player.getInventory().setLeggings(makeOrange(new ItemStack(Material.LEATHER_LEGGINGS)));
			player.getInventory().setBoots(makeOrange(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You were given " + ChatColor.GREEN + ChatColor.BOLD + 
					"ORANGE WOOL");
			player.setDisplayName("" + player.getName() + " " + ChatColor.DARK_GREEN + ChatColor.BOLD + "Sheep" +
					ChatColor.RESET);
		}
		else if (itemList[randomNum] == item) {
			player.getInventory().setChestplate(makeBlue(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			player.getInventory().setLeggings(makeBlue(new ItemStack(Material.LEATHER_LEGGINGS)));
			player.getInventory().setBoots(makeBlue(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You were given " + ChatColor.BLUE + ChatColor.BOLD + 
					"BLUE WOOL");
			player.setDisplayName("" + player.getName() + " " + ChatColor.BLUE + ChatColor.BOLD + "Sheep" +
					ChatColor.RESET);
		}
		else if (itemList[randomNum] == item4) {
			player.getInventory().setChestplate(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			player.getInventory().setLeggings(makePurple(new ItemStack(Material.LEATHER_LEGGINGS)));
			player.getInventory().setBoots(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You were given " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + 
					"PURPLE WOOL");
			player.setDisplayName("" + player.getName() + " " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Sheep" +
					ChatColor.RESET);
		}
		else if (itemList[randomNum] == item5) {
			player.getInventory().setChestplate(makeYellow(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			player.getInventory().setLeggings(makeYellow(new ItemStack(Material.LEATHER_LEGGINGS)));
			player.getInventory().setBoots(makeYellow(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
			player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 999999999, 1));
			
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You were given " + ChatColor.YELLOW + ChatColor.BOLD + 
					"YELLOW WOOL");
			player.setDisplayName("" + player.getName() + " " + ChatColor.YELLOW + ChatColor.BOLD + "Sheep" +
					ChatColor.RESET);
		}
		
		
		player.getInventory().setItem(0, new ItemStack(itemList[randomNum]));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		/*if (item.getType() == Material.WOOD_SWORD
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (boosterCooldown.useAndResetCooldown()) {
				double boosterStrength = 1.5;
				for (Player gamePlayer : instance.players)
					gamePlayer.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 1);
				Vector vel = player.getLocation().getDirection().multiply(boosterStrength);
				player.setVelocity(vel);
			}
	}*/ if (item.getType() == Material.ENCHANTMENT_TABLE && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			//player.getInventory().clear();
		int amount = item.getAmount();
		if (amount > 0) {
			amount--;
			if (amount == 0)
				player.getInventory().clear(player.getInventory().getHeldItemSlot());
			else
				item.setAmount(amount);
			event.setCancelled(true);
			TestItems();
		}
	}
		
	}
	
	@Override
	public ClassType getType() {
		return ClassType.Sheep;
	}
	
	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}

}
