package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;

public class NotchClass extends BaseClass {

	public NotchClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.0;
	}

	public ItemStack makeGray(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.GRAY);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(new ItemStack(Material.SKULL_ITEM));
		playerEquip.setChestplate(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4));
		playerEquip.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		playerEquip.setBoots(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0, ItemHelper.addEnchant(
				ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.STONE_SWORD),
						"" + ChatColor.BLACK + ChatColor.BOLD + "Notch's Sword"), Enchantment.KNOCKBACK, 1),
				Enchantment.DURABILITY, 10000));
		playerInv.setItem(1, ItemHelper.setDetails(new ItemStack(Material.GRASS),
				"" + ChatColor.GRAY + "Right Click to Teleport Players"));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if (item.getType() == Material.GRASS
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			int amount = item.getAmount();
			for (Player gamePlayer : instance.players) {
				if (player != gamePlayer) {
					if (player.isOnGround()) {
						if (gamePlayer.getGameMode() != GameMode.SPECTATOR) {
							if (amount > 0) {
								amount--;
								if (amount == 0) {
									player.getInventory().clear(player.getInventory().getHeldItemSlot());
								}
								gamePlayer.teleport(player.getLocation());
								player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
										+ "You teleported a random player to your location");
							}
						}
					} else {
						player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You have to be on the ground to use this!");
					}
				}
			}
		}
	}

	@Override
	public ClassType getType() {
		return ClassType.Notch;
	}

	@Override
	public void SetNameTag() {

	}
}
