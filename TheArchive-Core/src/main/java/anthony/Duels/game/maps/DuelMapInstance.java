package anthony.Duels.game.maps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.Vector;

public class DuelMapInstance {

	public String worldName;
	public List<Vector> spawnPos = new ArrayList<>(); 
	public Vector lobbyLoc = new Vector(100, 100, 100);
	
	public DuelMapInstance(String worldName) {
		this.worldName = worldName;
	}
	
	public DuelMapInstance setSpawnPos(Vector... spawnPos) {
		for (Vector v : spawnPos)
			this.spawnPos.add(v);
		return this;
	}
	
	public DuelMapInstance setLobbyLoc(Vector lobbyLoc) {
		this.lobbyLoc = lobbyLoc;
		return this;
	}
	
}
