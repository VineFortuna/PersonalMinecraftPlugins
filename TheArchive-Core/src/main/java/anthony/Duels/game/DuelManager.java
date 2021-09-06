package anthony.Duels.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import anthony.Duels.game.maps.DuelMaps;
import anthony.SuperCraftBrawl.Main;

public class DuelManager implements Listener{

	private Main main;
	
	public DuelManager(Main main) {
		this.main = main;
	}
	
	public Main getMain() {
		return main;
	}
	
	public void JoinGame(Player player, DuelMaps map) {
		
	}
	
}
