package anthony.SuperCraftBrawl.Game.classes.all;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.Game.classes.Cooldown;
import anthony.SuperCraftBrawl.Game.projectile.ItemProjectile;
import anthony.SuperCraftBrawl.Game.projectile.ProjectileOnHit;
import net.md_5.bungee.api.ChatColor;

public class SethBlingClass extends BaseClass{
	
	private Cooldown boosterCooldown = new Cooldown(3000), shurikenCooldown = new Cooldown(200);

	public SethBlingClass(GameInstance instance, Player player) {
		super(instance, player);
		baseVerticalJump = 1.6;
	}
	
	public ItemStack makeRed(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.RED);
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
		playerEquip.setChestplate(makeRed(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeRed(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeRed(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}
	
	public ItemStack getCmdBlock() {
		return ItemHelper.setDetails(new ItemStack(Material.COMMAND, 1), "" + ChatColor.GRAY + ChatColor.ITALIC + "Right Click",
				ChatColor.YELLOW + "");
	}
	
	public ItemStack getSethsBlock() {
		return ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.REDSTONE_BLOCK, 1), "" + ChatColor.RED + ChatColor.BOLD + "Seth's Block",
				ChatColor.YELLOW + ""), Enchantment.KNOCKBACK, 2), Enchantment.DAMAGE_ALL, 3);
	}
	
	@Override
	public void SetItems(Inventory playerInv) {
		ItemStack sethsBlock = getSethsBlock();
		sethsBlock.setAmount(1);
		playerInv.setItem(0, sethsBlock);
		
		ItemStack commandBlock = getCmdBlock();
		commandBlock.setAmount(1);
		playerInv.setItem(1, commandBlock);

	}
	
	@SuppressWarnings("deprecation")
	public void SetItemsAfter() {
		
		ItemStack test = new ItemStack(Material.APPLE); 

		ItemStack test2 = new ItemStack(Material.ANVIL); 
		
		ItemStack test3 = new ItemStack(Material.COAL); 

		ItemStack test4 = new ItemStack(Material.BLAZE_POWDER);
		
		ItemStack[] items = new ItemStack[]{test, test2, test3, test4};
		
		Random r = new Random();
		int index = r.nextInt(items.length); 
		ItemStack item = items[index]; 
		
		player.getInventory().setItem(1, new ItemStack(index));
	}
	
	public void TestItems() {
		ItemStack item = ItemHelper.setDetails(new ItemStack(Material.POTION, 1), "" + ChatColor.YELLOW + ChatColor.BOLD + "Instant Heal");
		Potion pot = new Potion(1);
		pot.setType(PotionType.INSTANT_HEAL);
		pot.setSplash(true);
		pot.apply(item);
		
		ItemStack item2 = ItemHelper.setDetails(new ItemStack(Material.POTION, 1), "" + ChatColor.GREEN + "Speed Pot");
		Potion pot2 = new Potion(1);
		pot2.setType(PotionType.SPEED);
		pot2.setSplash(true);
		pot2.apply(item2);
		
		ItemStack item6 = ItemHelper.setDetails(new ItemStack(Material.POTION, 1), "" + ChatColor.BLUE + "Weakness Pot");
		Potion pot3 = new Potion(1);
		pot3.setType(PotionType.WEAKNESS);
		pot3.setSplash(true);
		pot3.apply(item6);
		
		ItemStack item7 = ItemHelper.setDetails(new ItemStack(Material.POTION, 1), "" + ChatColor.YELLOW + "Strength Pot");
		Potion pot4 = new Potion(1);
		pot4.setType(PotionType.STRENGTH);
		pot4.setSplash(true);
		pot4.apply(item7);
	
		ItemStack item3 = new ItemStack(Material.GOLDEN_APPLE, 1);
		ItemStack item4 = new ItemStack(Material.TNT);
		ItemStack item5 = ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.IRON_SWORD, 1, (short) 250), "" + ChatColor.YELLOW + ChatColor.BOLD + "HAMMER",
				ChatColor.YELLOW + ""), Enchantment.KNOCKBACK, 10);
		item5.getDurability();
		ItemStack[] itemList = {item, item2, item3, item, item2, item3, item6, item7, item, item, item2};
		Random rand = new Random();
		int randomNum = rand.nextInt(itemList.length);
		ItemStack[] itemList2 = {item4, item5, item, item2, item3, item4, item5, item, item2, item3, item6};
		Random rand2 = new Random();
		int randomNum2 = rand2.nextInt(itemList2.length);
		player.getInventory().addItem(new ItemStack(itemList[randomNum]));
		player.getInventory().addItem(new ItemStack(itemList2[randomNum2]));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		/*if (item.getType() == Material.WOOD_SWORD
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (boosterCooldown.useAndResetCooldown()) {
				double boosterStrength = 1.5;
				for (Player gamePlayer : instance.players)
					gamePlayer.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 1);
				Vector vel = player.getLocation().getDirection().multiply(boosterStrength);
				player.setVelocity(vel);
			}
	}*/ if (item != null && item.getType() == Material.COMMAND && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "Your command block skills rewarded you with special items!");
			int amount = item.getAmount();
			if (amount > 0) {
				amount--;
				if (amount == 0)
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
				else
					item.setAmount(amount);
				event.setCancelled(true);
			TestItems();
			}
		}
	
		if (item != null && item.getType() == Material.IRON_SWORD && (event.getAction() == Action.PHYSICAL)) {
			if (shurikenCooldown.useAndResetCooldown()) {
				int amount2 = item.getAmount();
				if (amount2 > 0) {
					amount2--;
					if (amount2 == 0)
						player.getInventory().clear(player.getInventory().getHeldItemSlot());
					else
						item.setAmount(amount2);
				}
				event.setCancelled(true);
			}
		}
	
		if (item != null && item.getType() == Material.TNT && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
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
							hit.damage(5.5, player);
							for (Player gamePlayer : instance.players) {
								gamePlayer.playSound(hit.getLocation(), Sound.EXPLODE, 1, 1);
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
	
	@Override
	public ClassType getType() {
		return ClassType.SethBling;
	}
	
	@Override
	public void SetNameTag() {
		// TODO Auto-generated method stub
		
	}

}
