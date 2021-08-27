package anthony.SuperCraftBrawl.punishment;

import org.bukkit.entity.Player;

import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.playerdata.PlayerData;

public class Punishment {

	private Main main;
	
	public Punishment(Main main) {
		this.main = main;
	}
	
	//Mute system
	public int setMuted(Player target) {
		PlayerData data = main.getDataManager().getPlayerData(target);
		return data.muted = 1;
	}
	
	public int setUnmuted(Player target) {
		PlayerData data = main.getDataManager().getPlayerData(target);
		return data.muted = 0;
	}
	
	public String getMutedReason(String reason) {
		return reason;
	}
	
	//Ban system
}
