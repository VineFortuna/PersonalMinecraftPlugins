package anthony.Duels.game.maps;

import org.bukkit.util.Vector;

public enum DuelMaps {

	Duel1("Duel1", new DuelMapInstance("duel1").setLobbyLoc(new Vector(100, 100, 100)));
	
	private DuelMapInstance instance;
	private String mapName;
	
	DuelMaps(String mapName, DuelMapInstance instance) {
		this.mapName = mapName;
		this.instance = instance;
	}
	
	public DuelMapInstance GetInstance() {
		return instance;
	}
	
}
