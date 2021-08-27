package anthony.SuperCraftBrawl.Game.classes.all;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import net.md_5.bungee.api.ChatColor;

public class BunnyClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public BunnyClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.2;
	}

	public ItemStack makeRed(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.GRAY);
		armour.setItemMeta(lm);
		return armour;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setChestplate(makeRed(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeRed(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeRed(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.CARROT_ITEM), "" + ChatColor.RESET + "Carrot",
						ChatColor.GRAY + "",
						ChatColor.YELLOW + ""), Enchantment.DAMAGE_ALL, 3), Enchantment.KNOCKBACK, 1));
		playerInv.setItem(1,
				ItemHelper.setDetails(new ItemStack(Material.GOLDEN_CARROT), ChatColor.GREEN + "Golden Carrot"));

			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 2));
	}

	private void carrotEffect() {
		instance.bunnyClassEvent(player);
	}

	public static void delay(int time) {
		try {
			Thread.sleep(time);
		}

		catch (InterruptedException anthony) {
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();

		if (item.getType() == Material.GOLDEN_CARROT
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			player.playSound(player.getLocation(), Sound.EAT, 1, 2);
			if (shurikenCooldown.useAndResetCooldown()) {
				int amount = item.getAmount();
				if (amount > 0) {
					amount--;
					if (amount == 0)
						player.getInventory().clear(player.getInventory().getHeldItemSlot());
					else
						item.setAmount(amount);
				}
				event.setCancelled(true);
			}
			carrotEffect();
		}
	}

	@Override
	public ClassType getType() {
		return ClassType.Bunny;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}
}
