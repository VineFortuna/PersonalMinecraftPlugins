package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import net.md_5.bungee.api.ChatColor;

public class CreeperClass extends BaseClass {

	public CreeperClass(GameInstance instance, Player player) {
		super(instance, player);
	}

	@Override
	public ClassType getType() {
		return ClassType.Creeper;
	}

	public ItemStack makeGreen(ItemStack armor) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armor.getItemMeta();
		lm.setColor(Color.ORANGE);
		armor.setItemMeta(lm);
		return armor;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack creeperHelmet = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.CREEPER.ordinal());
		playerEquip.setHelmet(creeperHelmet);
		playerEquip.setChestplate(makeGreen(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeGreen(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeGreen(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetNameTag() {

	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0, ItemHelper.addEnchant(
				ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.SULPHUR),
						"" + ChatColor.YELLOW + ChatColor.BOLD + "Creeper Essence"), Enchantment.DAMAGE_ALL, 2),
				Enchantment.KNOCKBACK, 1));
		ItemStack item = new ItemStack(Material.POTION, 5);
		Potion pot3 = new Potion(1);
		pot3.setType(PotionType.INSTANT_DAMAGE);
		pot3.setSplash(true);
		pot3.apply(item);
		playerInv.setItem(1, item);
		playerInv.setItem(2, ItemHelper.setDetails(new ItemStack(Material.TNT),
				"" + ChatColor.RED + ChatColor.BOLD + "Destructionators"));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();

		if (item != null) {
			if (item.getType() == Material.POTION
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				int amount = item.getAmount();

				if (amount == 0) {
					ItemStack item2 = new ItemStack(Material.POTION, 1);
					Potion pot3 = new Potion(1);
					pot3.setType(PotionType.INSTANT_DAMAGE);
					pot3.setSplash(true);
					pot3.apply(item2);
					player.getInventory().setItem(1, item2);
				}
			} else if (item.getType() == Material.TNT
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				if (tnt.getTime() < 10000) {
					int seconds = (10000 - tnt.getTime()) / 1000 + 1;
					event.setCancelled(true);
					player.sendMessage(
							"" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "You have to wait " + ChatColor.YELLOW
									+ seconds + " seconds " + ChatColor.RESET + "to use this item again");
				} else {
					tnt.restart();
					TNTPrimed tnt = player.getWorld().spawn(player.getLocation().add(0, 1, 0), TNTPrimed.class);
					tnt.setFuseTicks(65);
				}
			}
		}
	}

}
