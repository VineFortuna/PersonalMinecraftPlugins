package anthony.SuperCraftBrawl.Game.classes.all;

import java.util.HashMap;

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

public class NoteblockClass extends BaseClass {

	private boolean aNote = false, bNote = false, cNote = false, dNote = false;
	private ItemStack redstone = new ItemStack(Material.REDSTONE);
	private HashMap<Integer, String> notes = new HashMap<>();
	int count = 0;

	public NoteblockClass(GameInstance instance, Player player) {
		super(instance, player);
	}

	@Override
	public ClassType getType() {
		return ClassType.Noteblock;
	}

	public ItemStack makeGreen(ItemStack armor) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armor.getItemMeta();
		lm.setColor(Color.ORANGE);
		armor.setItemMeta(lm);
		return armor;
	}

	@Override
	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(makeGreen(new ItemStack(Material.LEATHER_HELMET)));
		playerEquip.setChestplate(makeGreen(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeGreen(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeGreen(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		player.removePotionEffect(PotionEffectType.SPEED);
		player.removePotionEffect(PotionEffectType.JUMP);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 0));
	}

	@Override
	public void SetNameTag() {

	}

	@Override
	public void SetItems(Inventory playerInv) {
		player.getInventory().setItem(0,
				ItemHelper.addEnchant(new ItemStack(Material.REDSTONE), Enchantment.DAMAGE_ALL, 2));
		noteItems();
	}

	private void noteItems() {
		ItemStack a = new ItemStack(Material.NOTE_BLOCK);
		a.setDurability((short) 1);
		ItemStack b = new ItemStack(Material.NOTE_BLOCK);
		b.setDurability((short) 2);
		ItemStack c = new ItemStack(Material.NOTE_BLOCK);
		c.setDurability((short) 3);
		ItemStack d = new ItemStack(Material.NOTE_BLOCK);
		d.setDurability((short) 4);
		count = 0; // To reset count

		player.getInventory().setItem(1, ItemHelper.setDetails(a, instance.getManager().getMain().color("&eA")));
		player.getInventory().setItem(2, ItemHelper.setDetails(b, instance.getManager().getMain().color("&eB")));
		player.getInventory().setItem(3, ItemHelper.setDetails(c, instance.getManager().getMain().color("&eC")));
		player.getInventory().setItem(4, ItemHelper.setDetails(d, instance.getManager().getMain().color("&eD")));
		player.getInventory().setItem(5, ItemHelper.setDetails(new ItemStack(Material.BUCKET),
				instance.getManager().getMain().color("&2&lErase Your Work")));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();

		if (item != null && item.getType() == Material.NOTE_BLOCK && player.getInventory().getHeldItemSlot() == 1
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			aNote = true;
			notes.put(count, "A");
			count++;
		} else if (item != null && item.getType() == Material.NOTE_BLOCK && player.getInventory().getHeldItemSlot() == 2
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			bNote = true;
			notes.put(count, "B");
			count++;
		} else if (item != null && item.getType() == Material.NOTE_BLOCK && player.getInventory().getHeldItemSlot() == 3
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			cNote = true;
			notes.put(count, "C");
			count++;
		} else if (item != null && item.getType() == Material.NOTE_BLOCK && player.getInventory().getHeldItemSlot() == 4
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			dNote = true;
			notes.put(count, "D");
			count++;
		} else if (item != null && item.getType() == Material.BUCKET
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			aNote = false;
			bNote = false;
			cNote = false;
			dNote = false;
			notes.remove(0);
			notes.remove(1);
			notes.remove(2);
			count = 0;
			player.getInventory().setItem(0,
					ItemHelper.addEnchant(new ItemStack(Material.REDSTONE), Enchantment.DAMAGE_ALL, 2));
			player.removePotionEffect(PotionEffectType.SPEED);
			player.removePotionEffect(PotionEffectType.JUMP);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 0));
			player.sendMessage(
					instance.getManager().getMain().color("&r&l(!) &rYou have reset all of your work. Rip :("));
			noteItems();
		}

		if (notes.get(0).equals("A")) {
			player.getInventory().setItem(1, ItemHelper.setDetails(new ItemStack(Material.WOOL),
					instance.getManager().getMain().color("&e&lFirst Note")));
			if (notes.get(1).equals("B")) {
				player.getInventory().setItem(2, ItemHelper.setDetails(new ItemStack(Material.WOOL),
						instance.getManager().getMain().color("&e&lSecond Note")));
				if (notes.get(2).equals("D")) {
					player.getInventory().setItem(3, ItemHelper.setDetails(new ItemStack(Material.WOOL),
							instance.getManager().getMain().color("&e&lThird Note")));
					player.getInventory().setItem(0, ItemHelper.addEnchant(redstone, Enchantment.DAMAGE_ALL, 3));
					player.sendMessage(instance.getManager().getMain()
							.color("&r&l(!) &rYour song skills rewarded you with &eSharpness 3 &ron your weapon"));
					noteItems();
					aNote = false;
					bNote = false;
					cNote = false;
					dNote = false;
					notes.remove(0);
					notes.remove(1);
					notes.remove(2);
					count = 0;
				} else {
					incorrectSong();
				}
			} else if (notes.get(1).equals("C")) {
				player.getInventory().setItem(3, ItemHelper.setDetails(new ItemStack(Material.WOOL),
						instance.getManager().getMain().color("&e&lSecond Note")));
				if (notes.get(2).equals("B")) {
					player.getInventory().setItem(3, ItemHelper.setDetails(new ItemStack(Material.WOOL),
							instance.getManager().getMain().color("&e&lThird Note")));
					player.getInventory().setItem(0, ItemHelper.addEnchant(redstone, Enchantment.FIRE_ASPECT, 1));
					if (!(redstone.containsEnchantment(Enchantment.DAMAGE_ALL)))
						player.getInventory().setItem(0,
								ItemHelper.addEnchant(ItemHelper.addEnchant(redstone, Enchantment.FIRE_ASPECT, 1),
										Enchantment.DAMAGE_ALL, 2));
					player.sendMessage(instance.getManager().getMain()
							.color("&r&l(!) &rYour song skills rewarded you with &eFire Aspect 1 &ron your weapon"));
					noteItems();
					aNote = false;
					bNote = false;
					cNote = false;
					dNote = false;
					notes.remove(0);
					notes.remove(1);
					notes.remove(2);
					count = 0;
				} else {
					incorrectSong();
				}
			} else if (notes.get(1).equals("D")) {
				player.getInventory().setItem(4, ItemHelper.setDetails(new ItemStack(Material.WOOL),
						instance.getManager().getMain().color("&e&lSecond Note")));
				if (notes.get(2).equals("B")) {
					incorrectSong();
				} else {
					incorrectSong();
				}
			}
		} else if (notes.get(0).equals("B")) {
			player.getInventory().setItem(2, ItemHelper.setDetails(new ItemStack(Material.WOOL),
					instance.getManager().getMain().color("&e&lFirst Note")));
			if (notes.get(1).equals("C")) {
				player.getInventory().setItem(3, ItemHelper.setDetails(new ItemStack(Material.WOOL),
						instance.getManager().getMain().color("&e&lSecond Note")));
				if (notes.get(2).equals("D")) {
					player.getInventory().setItem(4, ItemHelper.setDetails(new ItemStack(Material.WOOL),
							instance.getManager().getMain().color("&e&lThird Note")));
					player.getInventory().setItem(0, ItemHelper.addEnchant(redstone, Enchantment.KNOCKBACK, 2));
					if (!(redstone.containsEnchantment(Enchantment.DAMAGE_ALL)))
						player.getInventory().setItem(0,
								ItemHelper.addEnchant(ItemHelper.addEnchant(redstone, Enchantment.DAMAGE_ALL, 2),
										Enchantment.DAMAGE_ALL, 2));
					player.sendMessage(instance.getManager().getMain()
							.color("&r&l(!) &rYour song skills rewarded you with &eKnockback 2 &ron your weapon"));
					noteItems();
					aNote = false;
					bNote = false;
					cNote = false;
					dNote = false;
					notes.remove(0);
					notes.remove(1);
					notes.remove(2);
					count = 0;
				} else {
					incorrectSong();
				}
			} else {
				incorrectSong();
			}
		} else if (notes.get(0).equals("C")) {
			player.getInventory().setItem(3, ItemHelper.setDetails(new ItemStack(Material.WOOL),
					instance.getManager().getMain().color("&e&lFirst Note")));
			if (notes.get(1).equals("A")) {
				player.getInventory().setItem(1, ItemHelper.setDetails(new ItemStack(Material.WOOL),
						instance.getManager().getMain().color("&e&lSecond Note")));
				if (notes.get(2).equals("D")) {
					player.getInventory().setItem(4, ItemHelper.setDetails(new ItemStack(Material.WOOL),
							instance.getManager().getMain().color("&e&lThird Note")));
					incorrectSong();
				} else {
					incorrectSong();
				}
			} else {
				incorrectSong();
			}
		} else if (notes.get(0).equals("D")) {
			player.getInventory().setItem(4, ItemHelper.setDetails(new ItemStack(Material.WOOL),
					instance.getManager().getMain().color("&e&lFirst Note")));
			if (notes.get(1).equals("A")) {
				player.getInventory().setItem(1, ItemHelper.setDetails(new ItemStack(Material.WOOL),
						instance.getManager().getMain().color("&e&lSecond Note")));
				if (notes.get(2).equals("C")) {
					player.getInventory().setItem(3, ItemHelper.setDetails(new ItemStack(Material.WOOL),
							instance.getManager().getMain().color("&e&lThird Note")));
					aNote = false;
					bNote = false;
					cNote = false;
					dNote = false;
					notes.remove(0);
					notes.remove(1);
					notes.remove(2);
					count = 0;
					noteItems();
					player.removePotionEffect(PotionEffectType.SPEED);
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));
					player.sendMessage(instance.getManager().getMain()
							.color("&r&l(!) &rYour song skills rewarded you with &eSpeed 2"));
				} else {
					incorrectSong();
				}
			} else {
				incorrectSong();
			}
		}
	}

	private void incorrectSong() {
		player.sendMessage(instance.getManager().getMain().color("&r&l(!) &rWow, you're not that good are you?"));
		aNote = false;
		bNote = false;
		cNote = false;
		dNote = false;
		notes.remove(0);
		notes.remove(1);
		notes.remove(2);
		count = 0;
		noteItems();
	}

}
