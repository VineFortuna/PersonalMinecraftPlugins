package anthony.SuperCraftBrawl.npcs;

import java.util.Arrays;

import org.bukkit.Location;

import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.GameManager;
import anthony.SuperCraftBrawl.Game.GameState;
import anthony.SuperCraftBrawl.Game.GameType;
import anthony.SuperCraftBrawl.Game.map.Maps;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import net.md_5.bungee.api.ChatColor;

public class MapNPC {

	private NPC npc;
	public GameType gameType;
	private NPCLib npcLib;
	private NPCManager manager;
	private Maps map;

	public MapNPC(NPCManager manager, Maps map, Location loc, Skin skin) {
		this.manager = manager;
		this.npcLib = manager.getNpcLib();
		this.map = map;
		npc = npcLib.createNPC(Arrays.asList(ChatColor.YELLOW + "" + ChatColor.BOLD + map.getName() 
				+ (map.GetInstance().gameType == GameType.FRENZY ? 
						"" + ChatColor.GRAY + ChatColor.ITALIC + " (frenzy)":""), "0 players",
				ChatColor.GREEN + "Waiting for players", "" + ChatColor.AQUA + ChatColor.UNDERLINE + "Click to join!"));
		this.gameType = map.GetInstance().gameType;
		npc.setSkin(skin);
		npc.setLocation(loc);
		npc.create();
	}

	public NPC getNpc() {
		return npc;
	}

	public Maps getMap() {
		return map;
	}

	public void update() {
		GameManager gameManager = manager.getMain().getGameManager();
		GameInstance game = gameManager.getInstanceOfMap(map);
		int players = 0;
		String state = "Waiting for players";
		String join = "Click to join!";
		if (game != null) {
			players = game.players.size();
			state = game.state.getName();
			if (game.state == GameState.WAITING) {
				if (gameType == GameType.NORMAL && players > 4) {
					state = ChatColor.RED + "Game is Full!";
					join = "";
				}
				else if (gameType == GameType.DUEL && players > 1) {
					state = ChatColor.RED + "Game is Full!";
					join = "";
				}
				else if (players >= 2)
					state = "Starting in " + game.getSecondsUntilStart() + "s";
			} else if (game.state == GameState.STARTED) {
				join = "";
			}
		}

		npc.setText(Arrays.asList(ChatColor.YELLOW + "" + ChatColor.BOLD + map.getName() + (map.GetInstance().gameType == GameType.FRENZY ? 
				"" + ChatColor.GRAY + ChatColor.ITALIC + " (frenzy)":""), players + " players",
				ChatColor.GREEN + state, "" + ChatColor.AQUA + ChatColor.UNDERLINE + join));
	}

}
