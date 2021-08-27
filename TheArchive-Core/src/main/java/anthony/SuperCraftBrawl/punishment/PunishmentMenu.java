package anthony.SuperCraftBrawl.punishment;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.playerdata.PlayerData;
import fr.minuskube.inv.ClickableItem;
import net.md_5.bungee.api.ChatColor;

public class PunishmentMenu extends InventoryMenu {

	private String reason = "";
	private PunishmentType type;
	private int time; // Time in seconds
	private Player target;

	public PunishmentMenu(Main core, Player target) {
		super(core, "" + "Punish " + ChatColor.YELLOW + target.getName(), true, 4);
		this.target = target;
	}

	@Override
	public void loadMenu() {
		// this.setHeader(new ItemStack(Material.IRON_AXE), "" + "Punish " +
		// ChatColor.YELLOW + target.getName());
		this.setContents(1, 2, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.BARRIER),
				"" + ChatColor.RED + ChatColor.BOLD + "Ban Player"), e -> {
					type = PunishmentType.BAN;
					loadNewPage(this::chooseReason);
				}));
		this.setContents(1, 4, ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.PAPER),
				"" + ChatColor.GREEN + ChatColor.BOLD + "Mute Player"), e -> {
					type = PunishmentType.MUTE;
					loadNewPage(this::chooseReason);
				}));
	}

	private void setReason(int index, String reason, Material mat) {
		int row = 1 + index / 9, col = index % 9;
		this.setContents(row, col, ClickableItem.of(ItemHelper.setDetails(new ItemStack(mat), reason), e -> {
			this.reason = reason;
			loadNewPage(this::chooseTime);
		}));
	}

	public void chooseReason() {
		setBack(this::loadMenu);
		int index = 0;

		if (type == PunishmentType.BAN) {
			setReason(index++, "" + ChatColor.RESET + "Unapproved Modifications (Hacking)", Material.IRON_SWORD);
			setReason(index++, "" + ChatColor.RESET + "Punishment Evading", Material.REDSTONE_BLOCK);
			setReason(index++, "" + ChatColor.RESET + "Bug/Map Exploiting", Material.SEA_LANTERN);
			setReason(index++, "" + ChatColor.RESET + "Inappropriate Username/Skin", Material.NAME_TAG);
			setReason(index++, "" + ChatColor.RESET + "Stat Boosting", Material.SKULL_ITEM);
			setReason(index++, "" + ChatColor.RESET + "Increasing Game Duration", Material.WATCH);
		} else if (type == PunishmentType.MUTE) {
			setReason(index++, "Spamming/Swearing/Flaming", Material.BARRIER);
			setReason(index++, "Advertising", Material.WORKBENCH);
			setReason(index++, "Advertising", Material.WORKBENCH);
			setReason(index++, "Threatening Players", Material.BOW);
		}
	}

	private void setTime(int index, String name, int seconds, Material mat) {
		int row = 1 + index / 9, col = index % 9;
		this.setContents(row, col, ClickableItem.of(ItemHelper.setDetails(new ItemStack(mat), name), e -> {
			this.time = seconds;
			loadNewPage(this::confirm);
		}));
	}

	public void chooseTime() {
		setBack(this::chooseReason);
		int index = 0;
		setTime(index++, "60 bloody seconds", 60, Material.EXP_BOTTLE);
		setTime(index++, "1 hour", 60 * 60, Material.EXP_BOTTLE);
		setTime(index++, "30 days", 60 * 60 * 24 * 30, Material.EXP_BOTTLE);

	}

	public void confirm() {
		setBack(this::chooseTime);
		this.setContents(2, 4,
				ClickableItem.of(ItemHelper.setDetails(new ItemStack(Material.EMERALD_BLOCK), "Confirm"), e -> {
					getPlayer().sendMessage(
							"Punishing player for " + this.reason + " " + this.time + " " + type.toString());
					PlayerData data = getCore().getDataManager().getPlayerData(target);
					data.muted = 1;
					loadNewPage(this::loadMenu);
				}));
	}
}
