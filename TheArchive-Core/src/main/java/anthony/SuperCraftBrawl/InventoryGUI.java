package anthony.SuperCraftBrawl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.Game.classes.ClassType;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

public class InventoryGUI implements InventoryProvider{
	
	public Main main;
	public SmartInventory inv;
	
	public InventoryGUI(Main main) {
		inv = SmartInventory.builder()
	            .id("myInventory")
	            .provider(this)
	            .size(3, 9)
	            .title("" + ChatColor.YELLOW + ChatColor.BOLD + "FREE CLASSES")
	            .build();
		this.main = main;

	}
	


	@Override
	public void init(Player player, InventoryContents contents) {
		contents.set(0, 0, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.SKULL_ITEM), "" + ChatColor.ITALIC + "Skeleton", "" + ChatColor.GRAY + "A long range shooter effective at taking down their targets"), e->{ 
			main.getGameManager().playerSelectClass(player, ClassType.Skeleton);;
			player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.RESET + ChatColor.GRAY + ChatColor.ITALIC + "Skeleton");
			inv.close(player);
		}));
			
			contents.set(0, 1, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.STICK), "" + ChatColor.ITALIC + "Ninja", "" + ChatColor.GRAY + "Ninja 2.0 (idk xD)"), e->{
				main.getGameManager().playerSelectClass(player, ClassType.Ninja);;
				player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.RESET + ChatColor.BLACK + ChatColor.BOLD + "Ninja");
				inv.close(player);
			}));
				contents.set(0, 2, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.ENDER_PEARL), "" + ChatColor.ITALIC + "Enderman", "" + ChatColor.GRAY + "Stare into the souls of your enemies whilst teleporting around them"), e->{
					main.getGameManager().playerSelectClass(player, ClassType.Enderman);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.RESET + ChatColor.BLACK + "Enderman");
					inv.close(player);
				}));
				contents.set(0, 3, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.TNT), "" + ChatColor.RED + ChatColor.BOLD + "T" + ChatColor.RESET + ChatColor.BOLD + "N" + ChatColor.RED + ChatColor.BOLD + "T", "" + ChatColor.GRAY + "Blow up your enemies with TNT!"), e->{
					main.getGameManager().playerSelectClass(player, ClassType.TNT);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.RED + ChatColor.BOLD + "T" + ChatColor.RESET + ChatColor.BOLD + "N" + ChatColor.RED + ChatColor.BOLD + "T");
					inv.close(player);
				}));
				contents.set(0, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.EGG), "" + ChatColor.ITALIC + "Chicken", "" + ChatColor.GRAY + "Bock bock backaaack! One of the best classes hehe tip"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Chicken);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.YELLOW + ChatColor.BOLD + "Chicken");
					inv.close(player);
				}));
				contents.set(0, 5, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.COAL_BLOCK), "" + ChatColor.ITALIC + "DarkSethBling", "" + ChatColor.GRAY + "The evil counterpart to the redstone King"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.DarkSethBling);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.DARK_GRAY + ChatColor.BOLD + ChatColor.ITALIC + "DarkSethBling");
					inv.close(player);
				}));
				contents.set(0, 6, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.WHEAT), "" + ChatColor.ITALIC + "Witch", "" + ChatColor.GRAY + "She lives in daydreams with me! (She)"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Witch);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Witch");
					inv.close(player);
				}));
				contents.set(0, 7, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.CACTUS), "" + ChatColor.ITALIC + "Cactus", "" + ChatColor.GRAY + "A pricklyyy living thing, made up of thornws & blood.."), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Cactus);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.DARK_GREEN + "Cactus");
					inv.close(player);
				}));
				contents.set(0, 8, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.WOOL), "" + ChatColor.ITALIC + "Sheep", "" + ChatColor.GRAY + "Different colors of wool gives you different powers!"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Sheep);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.RESET + ChatColor.BOLD + "Sheep");
					inv.close(player);
				}));
				contents.set(1, 0, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.LEASH), "" + ChatColor.ITALIC + "Horse", "" + ChatColor.GRAY + "Nayyy!! Different effects = different powers!"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Horse);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.DARK_GREEN + ChatColor.ITALIC + "Horse");
					inv.close(player);
				}));
				/*contents.set(1, 1, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.RABBIT_FOOT), "" + ChatColor.ITALIC + "Rabbit", "" + ChatColor.GRAY + "Hop around the whole map with speed!"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Rabbit);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.GREEN + ChatColor.ITALIC + "Rabbit");
					inv.close(player);
				}));*/
				contents.set(1, 2, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.SNOW_BALL), "" + ChatColor.ITALIC + "SnowGolem", "" + ChatColor.GRAY + "This is a SnowGolem, not a Snowman. Get it right pleb!"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.SnowGolem);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.RESET + ChatColor.BOLD + "SnowGolem");
					inv.close(player);
				}));
				contents.set(1, 3, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.INK_SACK), "" + ChatColor.ITALIC + "Squid", "" + ChatColor.GRAY + "UNDA DA SEA! UNDA DA SEA!"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Squid);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.DARK_BLUE + ChatColor.ITALIC + "Squid");
					inv.close(player);
				}));
				contents.set(1, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.SPIDER_EYE), "" + ChatColor.ITALIC + "Spider", "" + ChatColor.GRAY + "Bite and poison your enemies while fighting them!"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Spider);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.RED + ChatColor.ITALIC + "Spider");
					inv.close(player);
				}));
				contents.set(1, 5, ClickableItem.of(ItemHelper.setDetails(new ItemStack (Material.GOLDEN_CARROT), "" + ChatColor.ITALIC + "Bunny", "" + ChatColor.GRAY + "Easter Bunny is coming to town!"), e-> {
					main.getGameManager().playerSelectClass(player, ClassType.Bunny);;
					player.sendMessage("" + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have selected " + ChatColor.RESET + ChatColor.YELLOW + ChatColor.ITALIC + ChatColor.BOLD + "Bunny");
					inv.close(player);
				}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
	}
	
}
