package anthony.Duels.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import anthony.Duels.game.maps.DuelMaps;
import anthony.SuperCraftBrawl.worldgen.VoidGenerator;

public class DuelInstance {

	private DuelManager manager;
	private DuelMaps map;
	private List<Player> players;
	private DuelState state;
	private World mapWorld;

	public DuelInstance(DuelManager manager, DuelMaps map) {
		this.manager = manager;
		this.map = map;
		this.players = new ArrayList<>();
		state = DuelState.WAITING;
		InitialiseMap();
	}
	
	public void InitialiseMap() {
		WorldCreator w = new WorldCreator(map.GetInstance().worldName).environment(World.Environment.NORMAL);
		w.generator(new VoidGenerator());
		mapWorld = Bukkit.getServer().createWorld(w);
	}

	public void AddPlayer(Player player) {
		if (state == DuelState.WAITING) 
			if (!(players.contains(player))) {
				players.add(player);
				player.sendMessage(manager.getMain().color("&6&l(!) &rYou have joined &r&l" + map.toString()));
			} else
				player.sendMessage(manager.getMain().color("&c&l(!) &rYou are already in a game!"));
		else
			player.sendMessage(manager.getMain().color("&c&l(!) &rThis game is already playing!"));
	}

	public boolean HasPlayer(Player player) {
		return players.contains(player);
	}

}
