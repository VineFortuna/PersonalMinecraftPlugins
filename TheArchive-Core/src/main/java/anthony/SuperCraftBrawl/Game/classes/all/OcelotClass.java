package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
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
import net.md_5.bungee.api.ChatColor;

public class OcelotClass extends BaseClass {

	public OcelotClass(GameInstance instance, Player player) {
		super(instance, player);
	}

	@Override
	public ClassType getType() {
		return ClassType.Ocelot;
	}

	public ItemStack makeYellow(ItemStack armor) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armor.getItemMeta();
		lm.setColor(Color.YELLOW);
		armor.setItemMeta(lm);
		return armor;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(makeYellow(new ItemStack(Material.LEATHER_HELMET)));
		playerEquip.setChestplate(makeYellow(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeYellow(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeYellow(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));

	}

	@Override
	public void SetNameTag() {

	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(
						ItemHelper.addEnchant(new ItemStack(Material.RAW_FISH), Enchantment.DAMAGE_ALL, 3),
						Enchantment.KNOCKBACK, 2));
		playerInv.setItem(1,
				ItemHelper.setDetails(new ItemStack(Material.DIAMOND),
						instance.getManager().getMain().color("&7&lPurr Attack"),
						"" + ChatColor.RESET + "Right click to give slowness"));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();

		if (item != null) {
			if (item.getType() == Material.DIAMOND
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				int amount = item.getAmount();
				player.sendMessage(instance.getManager().getMain()
						.color("&r&l(!) &rYou attacked all players with &7&lPurr Attack"));
				for (Player gamePlayer : instance.players) {
					if (player != gamePlayer) {
						if (amount > 0) {
							gamePlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0));
							gamePlayer.sendMessage(instance.getManager().getMain()
									.color("&r&l(!) &rYou were attacked by &7&lPurr Attack"));
						}
					}
				}
				amount--;
				if (amount == 0)
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
			}
		}
	}

}
