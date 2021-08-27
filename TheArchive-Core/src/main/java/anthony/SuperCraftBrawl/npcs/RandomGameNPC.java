package anthony.SuperCraftBrawl.npcs;

import java.util.Arrays;

import org.bukkit.Location;

import anthony.SuperCraftBrawl.Game.GameManager;
import anthony.SuperCraftBrawl.Game.GameType;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import net.md_5.bungee.api.ChatColor;

public class RandomGameNPC {
	private NPC npc;
	public GameType gameType;
	private NPCLib npcLib;
	private NPCManager manager;

	public RandomGameNPC(NPCManager manager, Location loc, Skin skin) {
		this.manager = manager;
		this.npcLib = manager.getNpcLib();
		npc = npcLib.createNPC(Arrays.asList(ChatColor.YELLOW + "" + ChatColor.BOLD + "Super Craft Blocks", "0 Games Playing",
				"" + ChatColor.AQUA + ChatColor.UNDERLINE + "Click to Join!"));
		npc.setSkin(skin);
		npc.setLocation(loc);
		npc.create();
	}

	public NPC getNpc() {
		return npc;
	}

	//public Maps getMap() {
		//GameManager gameManager = manager.getMain().getGameManager();
		//GameInstance game = gameManager.getWaitingMap();
		
		//new ChooseGameGUI(gameManager.getMain()).inv.open(player);
		/*if (game != null) {
			return game.getMap();
		} else {
			Random random = new Random();
			Maps maps = Maps.values()[random.nextInt(Maps.values().length)];
			return maps;
		}*/
		//return;
	//}

	public void update() {
		GameManager gameManager = manager.getMain().getGameManager();

		npc.setText(Arrays.asList(ChatColor.YELLOW + "" + ChatColor.BOLD + "Super Craft Blocks",
				gameManager.getNumOfGames() + " Games Playing",
				"" + ChatColor.AQUA + ChatColor.UNDERLINE + "Click to Join!"));
	}
}
