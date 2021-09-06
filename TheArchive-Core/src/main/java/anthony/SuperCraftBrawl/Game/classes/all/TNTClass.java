package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import anthony.SuperCraftBrawl.Game.projectile.ItemProjectile;
import anthony.SuperCraftBrawl.Game.projectile.ProjectileOnHit;

public class TNTClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public TNTClass(GameInstance instance, Player player) {
		super(instance, player);
	}

	public ItemStack makeRed(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.RED);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(new ItemStack(Material.TNT));
		playerEquip.setChestplate(makeRed(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeRed(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeRed(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	public ItemStack getTNT() {
		return new ItemStack(Material.TNT, 1);
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(
						ItemHelper.addEnchant(new ItemStack(Material.WOOD_SWORD), Enchantment.KNOCKBACK, 1),
						Enchantment.DURABILITY, 10000));
		ItemStack tnt = getTNT();
		tnt.setAmount(5);
		playerInv.setItem(1, tnt);
	}

	public void onPlayerDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item.getType() == Material.TNT
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (player.getGameMode() != GameMode.SPECTATOR) {
				if (shurikenCooldown.useAndResetCooldown()) {
					int amount = item.getAmount();
					if (amount > 0) {
						amount--;
						if (amount == 0)
							player.getInventory().clear(player.getInventory().getHeldItemSlot());
						else
							item.setAmount(amount);
						ItemProjectile proj = new ItemProjectile(instance, player, new ProjectileOnHit() {
							@Override
							public void onHit(Player hit) {
								player.playSound(hit.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
								hit.damage(4.5, player);
								for (Player gamePlayer : instance.players) {
									gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 1, 1);
									// gamePlayer.getWorld().createExplosion(hit.getLocation().getX(),
									// hit.getLocation().getY(), hit.getLocation().getZ(), 3, false, false);
									gamePlayer.playEffect(hit.getLocation(), Effect.EXPLOSION_HUGE, 1);
								}

							}
						}, new ItemStack(Material.TNT));
						instance.getManager().getProjManager().shootProjectile(proj, player.getEyeLocation(),
								player.getLocation().getDirection().multiply(2.0D));
					}
					event.setCancelled(true);
				}
			}

		}

	}

	@Override
	public ClassType getType() {
		return ClassType.TNT;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
	}

}
