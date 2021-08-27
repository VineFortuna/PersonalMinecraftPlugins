package anthony.SuperCraftBrawl.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Game.SmmManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class SMMGUI implements InventoryProvider {

	public Main main;
	public SmartInventory inv;

	public SMMGUI(Main main) {
		inv = SmartInventory.builder().id("myInventory").provider(this).size(3, 9)
				.title("" + ChatColor.YELLOW + ChatColor.BOLD + "Super Minezone Maker").build();
		this.main = main;

	}

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.set(0, 0,
				ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.MONSTER_EGG),
						"" + ChatColor.RESET + ChatColor.BOLD + ChatColor.UNDERLINE + "Epic Minecraft Quiz", "",
						"" + ChatColor.GRAY + "Click to begin!"), e -> {
							inv.close(player);
							SmmManager sm = new SmmManager(main);
							sm.SendPlayerToQuiz(player);
							player.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(!) " + ChatColor.RESET
									+ "Teleporting to game..");
						}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}
}
