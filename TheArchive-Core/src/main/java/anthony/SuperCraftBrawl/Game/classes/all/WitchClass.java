package anthony.SuperCraftBrawl.Game.classes.all;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;

public class WitchClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(10000), shurikenCooldown = new Cooldown(200);

	public WitchClass(GameInstance instance, Player player) {
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
		playerEquip.setChestplate(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makePurple(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makePurple(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		ItemStack item3 = new ItemStack(Material.POTION, 3);
		Potion pot2 = new Potion(1);
		pot2.setType(PotionType.WEAKNESS);
		pot2.setSplash(true);
		pot2.apply(item3);
		
		ItemStack item2 = new ItemStack(Material.POTION, 5);
		Potion pot3 = new Potion(1);
		pot3.setType(PotionType.INSTANT_DAMAGE);
		pot3.setSplash(true);
		pot3.apply(item2);
		
		ItemStack item = new ItemStack(Material.POTION, 3);
		Potion pot = new Potion(1);
		pot.setType(PotionType.POISON);
		pot.setSplash(true);
		pot.apply(item);
		
		ItemStack item5 = ItemHelper.setDetails(new ItemStack(Material.POTION, 2), "" + ChatColor.RED + ChatColor.BOLD + "Slowness", "");
		Potion pot5 = new Potion(3);
		pot5.setType(PotionType.SLOWNESS);
		pot5.setSplash(true);
		pot5.apply(item5);
		
		ItemStack[] itemList = {item3, item, item5, item2, item2, item3, item3, item3, item, item3, item2, item, item3, item2, item, item5,
				item3, item2, item5, item, item3, item2, item, item5, item3, item2, item3, item2, item, item5};
		Random rand = new Random();
		int randomNum = rand.nextInt(itemList.length);
		
		
		player.getInventory().addItem(new ItemStack(itemList[randomNum]));
		
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.WHEAT), "" + ChatColor.BLACK + ChatColor.BOLD + "Magic Broom",
						ChatColor.GRAY + "",
						ChatColor.YELLOW + "Smack your enemies with this!"), Enchantment.DAMAGE_ALL, 2), Enchantment.KNOCKBACK, 1));
		
		player.getInventory().addItem(new ItemStack(itemList[randomNum]));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item.getType() == Material.WHEAT
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (broom.getTime() < 10000) {
				int seconds = (10000 - broom.getTime()) / 1000 + 1;
				event.setCancelled(true);
				player.sendMessage(
						"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have to wait " + ChatColor.YELLOW
								+ seconds + " seconds " + ChatColor.RESET + "to use this item again");
			} else {
				broom.restart();
				double boosterStrength = 2.1;
				for (Player gamePlayer : instance.players)
					gamePlayer.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 1);
				Vector vel = player.getLocation().getDirection().multiply(boosterStrength);
				player.setVelocity(vel);
				player.sendMessage("Juuuuuumppp!!!");  
			}
			/*if (boosterCooldown.useAndResetCooldown()) {
				double boosterStrength = 2.1;
				for (Player gamePlayer : instance.players)
					gamePlayer.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 1);
				Vector vel = player.getLocation().getDirection().multiply(boosterStrength);
				player.setVelocity(vel);
				player.sendMessage("Juuuuuumppp!!!");  
				
			}*/
			/*if (cooldownTicks <= 10 && cooldownTicks != 0){
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You cannot use this for another " + cooldownTicks);
			}*/
			
		}
	}
	
	@Override
	public ClassType getType() {
		return ClassType.Witch;
	}
	
	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}

}
