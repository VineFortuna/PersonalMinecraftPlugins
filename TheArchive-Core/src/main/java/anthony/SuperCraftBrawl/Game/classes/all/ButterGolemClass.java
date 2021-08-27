package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
import net.md_5.bungee.api.ChatColor;

public class ButterGolemClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public ButterGolemClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.1;
	}

	public ItemStack makeYellow(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.YELLOW);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(makeYellow(new ItemStack(Material.LEATHER_HELMET)));
		playerEquip.setChestplate(makeYellow(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeYellow(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeYellow(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	public ItemStack getButterBall() {
		return ItemHelper.setDetails(new ItemStack(Material.GOLD_BLOCK, 1), ChatColor.GREEN + "Butter Balls",
				ChatColor.YELLOW + "Right click to throw DEADLY butter balls!");
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.GOLD_AXE),
						ChatColor.YELLOW + "Butter Axe", ChatColor.GRAY + "Slice your enemies to pieces"),
						Enchantment.KNOCKBACK, 3), Enchantment.DURABILITY, 10000));
		ItemStack butter = getButterBall();
		butter.setAmount(8);
		playerInv.setItem(1, butter);

	}

	private int getNumberOfButter() {
		int count = 0;
		for (ItemStack item : player.getInventory().getContents())
			if (item != null && item.getType() == Material.GOLD_BLOCK)
				count += item.getAmount();
		return count;
	}

	/*
	 * @Override public void Tick(int gameTicks) { if (gameTicks % 20 == 0) { int
	 * number = getNumberOfTNT(); if (number < 5)
	 * player.getInventory().addItem(getTNT()); } }
	 */

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item.getType() == Material.GOLD_BLOCK) {
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
								hit.damage(4.0, player);
								for (Player gamePlayer : instance.players) {
									gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 2, 1);
									gamePlayer.playEffect(hit.getLocation(), Effect.EXPLOSION_HUGE, 1);
								}

							}

						}, new ItemStack(Material.GOLD_BLOCK));
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
		return ClassType.ButterGolem;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}

}
