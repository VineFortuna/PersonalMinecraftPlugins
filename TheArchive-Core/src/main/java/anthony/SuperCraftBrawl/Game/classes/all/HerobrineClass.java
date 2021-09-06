package anthony.SuperCraftBrawl.Game.classes.all;

import java.util.Random;

import org.bukkit.Color;
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

public class HerobrineClass extends BaseClass {

	private Cooldown boosterCooldown = new Cooldown(10000), shurikenCooldown = new Cooldown(10000);

	public HerobrineClass(GameInstance instance, Player player) {
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

		meta.setOwner("_Herobrine_");
		meta.setDisplayName("");

		playerskull.setItemMeta(meta);

		playerEquip.setHelmet(playerskull);
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999, 1));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(
						ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.GOLD_SWORD),
								ChatColor.GOLD + "Golden Sword"), Enchantment.KNOCKBACK, 2),
						Enchantment.DURABILITY, 10000));
		playerInv.setItem(1, ItemHelper.setDetails(new ItemStack(Material.DIAMOND),
				"" + ChatColor.RESET + ChatColor.BOLD + "Diamond of Despair"));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item.getType() == Material.DIAMOND
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Random rand = new Random();

			int chance = rand.nextInt(3);

			for (Player gamePlayer : instance.players) {

			}

			if (chance == 0) {
				for (Player gamePlayers : instance.players)
					if (gamePlayers != player)
						gamePlayers.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
			} else if (chance == 1) {
				for (Player gamePlayers : instance.players)
					if (gamePlayers != player)
						gamePlayers.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
			} else if (chance == 2) {
				for (Player gamePlayers : instance.players) {
					if (gamePlayers != player) {
						gamePlayers.setFireTicks(100);
						instance.getMapWorld().strikeLightningEffect(gamePlayers.getLocation());
					}
				}
			}
			int amount = item.getAmount();
			if (amount > 0) {
				amount--;
				if (amount == 0)
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
				else
					item.setAmount(amount);
				event.setCancelled(true);
			}
		}
	}

	@Override
	public ClassType getType() {
		return ClassType.Herobrine;
	}

	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub

	}
}
