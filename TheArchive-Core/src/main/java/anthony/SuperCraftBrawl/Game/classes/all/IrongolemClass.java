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

public class IrongolemClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public IrongolemClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.1;
	}

	public ItemStack makeGray(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.GRAY);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();

		meta.setOwner("IronGolem");
		meta.setDisplayName("");

		playerskull.setItemMeta(meta);

		playerEquip.setHelmet(playerskull);
		playerEquip.setChestplate(makeGray(new ItemStack(Material.LEATHER_CHESTPLATE)));
		playerEquip.setLeggings(makeGray(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeGray(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(
						ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.IRON_AXE),
								"" + ChatColor.RED + ChatColor.BOLD + "Power Axe"), Enchantment.KNOCKBACK, 2),
						Enchantment.DURABILITY, 10000));
		playerInv.setItem(1, ItemHelper.setDetails(new ItemStack(Material.RED_ROSE),
				"" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + "Rose of Elevation"));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		/*ItemStack item = event.getItem();

		if (item != null && item.getType() == Material.RED_ROSE
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			ItemProjectile proj = new ItemProjectile(instance, player, new ProjectileOnHit() {
				@Override
				public void onHit(Player hit) {
					player.playSound(hit.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
					Location loc = hit.getLocation();
					Vector v = loc.getDirection();
					v.setY(1);
					hit.setVelocity(v);
					for (Player gamePlayer : instance.players)
						gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 1, 1);
				}

			}, new ItemStack(Material.RED_ROSE));
			instance.getManager().getProjManager().shootProjectile(proj, player.getEyeLocation(),
					player.getLocation().getDirection().multiply(2.0D));
		}*/
	}

	@Override
	public ClassType getType() {
		return ClassType.IronGolem;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}

}
