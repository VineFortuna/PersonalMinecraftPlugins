	package anthony.SuperCraftBrawl.Game.classes.all;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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

public class SkeletonClass extends BaseClass {

	private Cooldown arrowCooldown = new Cooldown(5000), lightningCooldown = new Cooldown(10000);

	public SkeletonClass(GameInstance instance, Player player) {
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
		playerEquip.setHelmet(new ItemStack(Material.SKULL_ITEM));
		playerEquip.setChestplate(makeGray(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeGray(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeGray(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void SetItems(Inventory playerInv) {
		playerInv.setItem(0,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.BONE), ChatColor.GRAY + "Skeleton Bone",
						ChatColor.GRAY + "Bone them to DEATH!",
						ChatColor.YELLOW + "Smack your enemies with this!"), Enchantment.DAMAGE_ALL, 2), Enchantment.KNOCKBACK, 1));
		playerInv.setItem(1,
				ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.addEnchant(ItemHelper.setDetails(new ItemStack(Material.BOW), ChatColor.GRAY + "Skeleton Bow",
						ChatColor.GRAY + "",
						ChatColor.YELLOW + "Snipe your enemies with this!"), Enchantment.ARROW_KNOCKBACK, 1), Enchantment.ARROW_INFINITE, 1), Enchantment.DURABILITY, 10000));
		playerInv.setItem(2, new ItemStack(Material.ARROW));
	}

	@Override
	public void UseItem(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if (item.getType() == Material.BONE
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (arrowCooldown.useAndResetCooldown()) {
				player.launchProjectile(Arrow.class);
				for (Player worldPlayer : player.getWorld().getPlayers())
					worldPlayer.playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
			}
		}
	}

	@Override
	public void TakeDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			Entity attacker = damageEvent.getDamager();
			if (attacker instanceof LivingEntity) {
				/*if (lightningCooldown.useAndResetCooldown()) {
					player.sendMessage("" + ChatColor.GREEN + ChatColor.RESET + ChatColor.BOLD + "(!) "
							+ ChatColor.GREEN + "You have smited " + attacker.getName());
					attacker.sendMessage(ChatColor.RED + "You have been smited by " + player.getName());

					attacker.getWorld().spawnEntity(attacker.getLocation(), EntityType.LIGHTNING);
				}*/

			}
		}
	}
	
	@Override
	public ClassType getType() {
		return ClassType.Skeleton;
	}
	
	@Override
	public void SetNameTag() {
		
	}


}
