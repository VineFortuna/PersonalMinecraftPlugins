package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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
import net.md_5.bungee.api.ChatColor;

public class DarkSethBlingClass extends BaseClass implements Listener {

	public DarkSethBlingClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.5;
		instance.getManager().getMain().getServer().getPluginManager().registerEvents(this,
				instance.getManager().getMain());
	}

	public ItemStack makeNavy(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.NAVY);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();

		meta.setOwner("SethBling");
		meta.setDisplayName("");

		playerskull.setItemMeta(meta);

		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makeNavy(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeNavy(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeNavy(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(
						ItemHelper.setDetails(new ItemStack(Material.COAL_BLOCK),
								"" + ChatColor.DARK_GRAY + "Dark Command Block",
								ChatColor.GRAY + "Knockback your enemies with this!", ChatColor.YELLOW + ""),
						Enchantment.KNOCKBACK, 2), Enchantment.DAMAGE_ALL, 3));
	}

	/*@EventHandler
	public void Block(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		GameInstance i = instance.getManager().GetInstanceOfPlayer(player);
		BaseClass bc = instance.classes.get(player);

		if (i != null && i.state == GameState.STARTED) {
			if (bc != null && bc.getType() == ClassType.DarkSethBling) {
				if (player.getFireTicks() > 0) {
					player.getInventory().setItem(0,
							ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.addEnchant(
									ItemHelper.setDetails(new ItemStack(Material.COAL_BLOCK),
											"" + ChatColor.DARK_GRAY + "Dark Command Block",
											ChatColor.GRAY + "Knockback your enemies with this!", ChatColor.YELLOW + ""),
									Enchantment.KNOCKBACK, 2), Enchantment.DAMAGE_ALL, 3), Enchantment.FIRE_ASPECT, 1));
				} else {
					player.getInventory().setItem(0,
							ItemHelper.addEnchant(ItemHelper.addEnchant(
									ItemHelper.setDetails(new ItemStack(Material.COAL_BLOCK),
											"" + ChatColor.DARK_GRAY + "Dark Command Block",
											ChatColor.GRAY + "Knockback your enemies with this!", ChatColor.YELLOW + ""),
									Enchantment.KNOCKBACK, 2), Enchantment.DAMAGE_ALL, 3));
				}
			}
		}
	}*/

	@Override
	public void UseItem(PlayerInteractEvent event) {
	}

	@Override
	public ClassType getType() {
		//return ClassType.DarkSethBling;
		return null;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}

}