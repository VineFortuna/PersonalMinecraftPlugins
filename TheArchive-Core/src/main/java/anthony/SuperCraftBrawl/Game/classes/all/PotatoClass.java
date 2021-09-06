package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import anthony.SuperCraftBrawl.Game.classes.ClassType;

public class PotatoClass extends BaseClass{

	private ItemStack potato = new ItemStack(Material.POTATO_ITEM);
	private ItemStack bakedPotato = new ItemStack(Material.BAKED_POTATO);
	private int sharpness = 3;
	public boolean bp = false;

	public PotatoClass(GameInstance instance, Player player) {
		super(instance, player);
	}

	public Main getMain() {
		return instance.getManager().getMain();
	}

	@Override
	public ClassType getType() {
		return ClassType.Potato;
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
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0));
	}

	@Override
	public void SetNameTag() {

	}

	@Override
	public void SetItems(Inventory playerInv) {
		this.sharpness = 3; // Default sharpness when spawn
		playerInv.setItem(0, ItemHelper.addEnchant(potato, Enchantment.DAMAGE_ALL, sharpness));
	}

	@Override
	public void TakeDamage(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
			if (player.getFireTicks() > 0) {
				if (!(player.getInventory().contains(bakedPotato))) {
					player.getInventory().setItem(0,
							ItemHelper.addEnchant(ItemHelper.addEnchant(bakedPotato, Enchantment.DAMAGE_ALL, sharpness),
									Enchantment.FIRE_ASPECT, 1));
					player.sendMessage(instance.getManager().getMain()
							.color("&r&l(!) &rYou recieved a baked potato for being on fire"));
				}
			}
		}
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getItem();

		if (item != null) {
			if (item.getType() == Material.POTATO_ITEM
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				if (sharpness <= 3 && sharpness >= 2) {
					sharpness--;
					player.getInventory().removeItem(potato);
					player.removePotionEffect(PotionEffectType.SPEED);

					new BukkitRunnable() {

						@Override
						public void run() {
							player.getInventory().setItem(0, ItemHelper.addEnchant(new ItemStack(Material.POTATO_ITEM),
									Enchantment.DAMAGE_ALL, sharpness));
							player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 3 - sharpness));
						}
					}.runTaskLater(instance.getManager().getMain(), 1);

					player.sendMessage(instance.getManager().getMain()
							.color("&6&l(!) &rYou gave up a level of Sharpness for some speed"));
				}
			} else if (item.getType() == Material.BAKED_POTATO
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				if (sharpness <= 3 && sharpness >= 2) {
					sharpness--;
					player.getInventory().removeItem(bakedPotato);
					player.removePotionEffect(PotionEffectType.SPEED);

					new BukkitRunnable() {

						@Override
						public void run() {
							player.getInventory()
									.setItem(0,
											ItemHelper.addEnchant(
													ItemHelper.addEnchant(new ItemStack(Material.BAKED_POTATO),
															Enchantment.DAMAGE_ALL, sharpness),
													Enchantment.FIRE_ASPECT, 1));
							player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 3 - sharpness));
						}
					}.runTaskLater(instance.getManager().getMain(), 1);

					player.sendMessage(instance.getManager().getMain()
							.color("&6&l(!) &rYou gave up a level of Sharpness for some speed"));
				}
			}
		}
	}

}
