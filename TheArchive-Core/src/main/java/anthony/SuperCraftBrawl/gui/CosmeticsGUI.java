package anthony.SuperCraftBrawl.gui;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import anthony.SuperCraftBrawl.ranks.RankManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class CosmeticsGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;
	private RankManager rm;
	private boolean isEquipped = false;

	public CosmeticsGUI(Main main) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "COSMETICS").build();
		this.main = main;
	}

	public RankManager getRankManager() {
		return rm;
	}

	public ItemStack makeYellow(ItemStack armour) {
		LeatherArmorMeta lm = (LeatherArmorMeta) armour.getItemMeta();
		lm.setColor(Color.WHITE);
		armour.setItemMeta(lm);
		return armour;
	}

	public void SetArmour(EntityEquipment playerEquip) {
		playerEquip.setHelmet(makeYellow(new ItemStack(Material.LEATHER_HELMET)));
		playerEquip.setChestplate(makeYellow(ItemHelper.addEnchant(new ItemStack(Material.LEATHER_CHESTPLATE),
				Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
		playerEquip.setLeggings(makeYellow(new ItemStack(Material.LEATHER_LEGGINGS)));
		playerEquip.setBoots(makeYellow(
				ItemHelper.addEnchant(new ItemStack(Material.LEATHER_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 4)));
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		PlayerData data = main.getDataManager().getPlayerData(player);

		ItemStack wheat = ItemHelper.setDetails(new ItemStack(Material.WHEAT),
				"" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + "Magic Broom");
		ItemStack melon = ItemHelper.setDetails(new ItemStack(Material.MELON, data.melon),
				"" + ChatColor.RESET + ChatColor.YELLOW + "Melons");

		contents.set(1, 3, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.WHEAT),
				"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Magic Broom", "",
				"" + ChatColor.RESET + ChatColor.BLUE + ChatColor.BOLD + "CAPTAIN" + ChatColor.RESET + "+ exclusive!"),
				e -> {
					if (player.hasPermission("scb.wheat")) {
						if (!(player.getInventory().contains(wheat))) {
							player.getInventory().setItem(5, wheat);
							player.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have equipped " + ChatColor.DARK_GREEN + ChatColor.BOLD + "Magic Broom");
							inv.close(player);
						} else if (player.getInventory().contains(wheat)) {
							player.getInventory().remove(wheat);
							player.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have unequipped " + ChatColor.DARK_GREEN + ChatColor.BOLD + "Magic Broom");
							inv.close(player);
						}
					} else {
						player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You need the rank " + ChatColor.BLUE + ChatColor.BOLD + "CAPTAIN " + ChatColor.RESET
								+ "to use this item!");
					}
				}));

		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();

		meta.setOwner("SethBling");
		meta.setDisplayName("");

		playerskull.setItemMeta(meta);

		contents.set(2, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.LEATHER_CHESTPLATE),
				"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Astronaut Outfit"), e -> {
					if (data.astronaut == 1) {
						if (this.isEquipped == false) {
							this.isEquipped = true;
							player.getInventory().setHelmet(new ItemStack(Material.GLASS));
							player.getInventory().setChestplate(makeYellow(new ItemStack(Material.LEATHER_CHESTPLATE)));
							player.getInventory().setLeggings(makeYellow(new ItemStack(Material.LEATHER_LEGGINGS)));
							player.getInventory().setBoots(makeYellow(new ItemStack(Material.LEATHER_BOOTS)));
							player.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have equipped " + ChatColor.YELLOW + "Astronaut Outfit");
						} /*else {
							player.getInventory().setHelmet(new ItemStack(Material.AIR));
							player.getInventory().setChestplate(new ItemStack(Material.AIR));
							player.getInventory().setLeggings(new ItemStack(Material.AIR));
							player.getInventory().setBoots(new ItemStack(Material.AIR));
							player.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have unequipped " + ChatColor.YELLOW + "Astronaut Outfit");
						}*/
					} else {
						player.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You have not unlocked this item yet!");
					}
					inv.close(player);
				}));
		contents.set(1, 5, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.MELON),
				"" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "Melons", "",
				"" + ChatColor.RESET + "You have " + ChatColor.YELLOW + data.melon + ChatColor.RESET + " Melons"), e ->

		{
					if (data.melon > 0) {
						if (!(player.getInventory().contains(melon))) {
							player.getInventory().setItem(5, melon);
							player.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have equipped " + ChatColor.YELLOW + "Melons");
							inv.close(player);
						} else if (player.getInventory().contains(melon)) {
							player.getInventory().remove(melon);
							player.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "You have unequipped " + ChatColor.YELLOW + "Melons");
						}
					} else {
						player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "(!) " + ChatColor.RESET
								+ "You do not have enough melons!");
					}
					inv.close(player);
				}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
