package anthony.SuperCraftBrawl.ranks;

import org.bukkit.ChatColor;

public enum Rank {
	Default(0, ""), Admin(1, "" + ChatColor.RED + ChatColor.BOLD + "ADMIN"),
	Owner(2, "" + ChatColor.RED + ChatColor.BOLD + "OWNER"), Vip(6, "" + ChatColor.YELLOW + ChatColor.BOLD + "VIP"),
	Trainee(3, "" + ChatColor.GOLD + ChatColor.BOLD + "TRAINEE"),
	Moderator(4, "" + ChatColor.BLUE + ChatColor.BOLD + "MOD"),
	Developer(5, "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "DEV"),
	Manager(7, "" + ChatColor.DARK_RED + ChatColor.BOLD + "MANAGER"),
	Captain(8, "" + ChatColor.BLUE + ChatColor.BOLD + "CAPTAIN"), Design(9, "" + ChatColor.YELLOW + ChatColor.BOLD + "DESIGN");

	private int roleID;
	private String tag;

	private Rank(int roleID, String tag) {
		this.roleID = roleID;
		this.tag = tag;
	}

	public int getRoleID() {
		return roleID;
	}

	public String getTag() {
		return tag;
	}

	public String getTagWithSpace() {
		if (this == Default)
			return "";
		else
			return tag + " " + ChatColor.RESET;
	}

	public static Rank getRankFromID(int id) {
		for (Rank rank : Rank.values()) {
			if (rank.getRoleID() == id) {
				return rank;
			}
		}
		return Rank.Default;
	}

	public static Rank getRankFromName(String name) {
		for (Rank rank : Rank.values()) {
			if (rank.toString().equalsIgnoreCase(name)) {
				return rank;
			}
		}
		return Rank.Default;
	}
}
