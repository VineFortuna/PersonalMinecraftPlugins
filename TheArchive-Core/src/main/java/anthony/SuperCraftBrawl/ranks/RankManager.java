package anthony.SuperCraftBrawl.ranks;

import org.bukkit.entity.Player;

import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.playerdata.PlayerData;

public class RankManager {
	
	private final Main main;
	
	public RankManager(Main main) {
		this.main = main;
	}
	
	public void setRank(Player player, Rank rank) {
		PlayerData data = main.getDataManager().getPlayerData(player);
		data.roleID = rank.getRoleID();
		updateRank(player, rank);
	}
	
	public Rank getRank(Player player) {
		PlayerData data = main.getDataManager().getPlayerData(player);
		return Rank.getRankFromID(data.roleID);
	}
	
	public void updateRank(Player player, Rank rank) {
		
	}
	
}
