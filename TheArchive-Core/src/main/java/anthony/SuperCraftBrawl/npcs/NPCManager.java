package anthony.SuperCraftBrawl.npcs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Game.map.Maps;
import anthony.SuperCraftBrawl.gui.ChooseGameGUI;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import net.jitse.npclib.api.skin.Skin;

public class NPCManager implements Listener {

	private Main main;
	private NPCLib npcLib;
	private NPC gameLobbyNPC;

	private NPC test, pileOfBodies, theend, stronghold, village, netherFortress, mushroom, nightDragon, candyland,
			iceland, dragonsDescent, clockwork, lostTemple, marooned, orbital, apex, atronach, multiverse, aperature;

	private List<MapNPC> npcs = new ArrayList<>();
	private List<RandomGameNPC> npcs2 = new ArrayList<>();

	public NPCManager(Main main) {
		this.main = main;
		this.npcLib = new NPCLib(main);
		load();
		Bukkit.getPluginManager().registerEvents(this, main);

	}

	public Main getMain() {
		return main;
	}

	public NPCLib getNpcLib() {
		return npcLib;
	}

	public void display(Player player) {
		for (MapNPC npc : npcs) {
			npc.getNpc().show(player);
		}
		for (RandomGameNPC npc : npcs2) {
			npc.getNpc().show(player);
		}
	}

	public void addNpcs(MapNPC... npcs) {
		for (MapNPC npc : npcs) {
			this.npcs.add(npc);
		}
	}

	public void addNpcs2(RandomGameNPC... npcs2) {
		for (RandomGameNPC npc : npcs2) {
			this.npcs2.add(npc);
		}
	}

	private void load() {
		Skin skin = new Skin("", "");
		World lw = main.getLobbyWorld();
		
		/*addNpcs(new MapNPC(this, Maps.PileOfBodies, new Location(lw, -13.596, 162, -437.528, 50, 0), skin),
				new MapNPC(this, Maps.TheEnd, new Location(lw, -16.587, 162, -440.550, 50, 0), skin),
				new MapNPC(this, Maps.Stronghold, new Location(lw, -20.541, 162, -444.511, 50, 0), skin),
				new MapNPC(this, Maps.Village, new Location(lw, -21.653, 162, -449.526, 50, 0), skin),
				new MapNPC(this, Maps.NetherFortress, new Location(lw, -19.578, 162, -454.524, 50, 0), skin),
				new MapNPC(this, Maps.Mushroom, new Location(lw, -16.538, 162, -458.523, 50, 0), skin),
				new MapNPC(this, Maps.NightDragon, new Location(lw, -3.494, 162, -437.533, 50, 0), skin),
				new MapNPC(this, Maps.Candyland, new Location(lw, -0.500, 162, -440.518, 50, 0), skin),
				new MapNPC(this, Maps.Iceland, new Location(lw, 2.515, 162, -444.518, 50, 0), skin),
				new MapNPC(this, Maps.DragonsDescent, new Location(lw, 4.520, 162, -449.531, 50, 0), skin),
				new MapNPC(this, Maps.Clockwork, new Location(lw, 2.520, 162, -454.508, 50, 0), skin),
				new MapNPC(this, Maps.LostTemple, new Location(lw, -40.543, 162, -401.510, 50, 0), skin),
				new MapNPC(this, Maps.Marooned, new Location(lw, 22.468, 162, -411.552, 50, 0), skin),
				new MapNPC(this, Maps.Orbital, new Location(lw, 22.475, 162, -401.487, 50, 0), skin),
				new MapNPC(this, Maps.Apex, new Location(lw, 25.478, 162, -398.509, 50, 0), skin),
				new MapNPC(this, Maps.Revenge, new Location(lw, -43.521, 162, -398.475, 50, 0), skin),
				new MapNPC(this, Maps.Multiverse, new Location(lw, 25.469, 162, -414.578, 50, 0), skin),
				new MapNPC(this, Maps.Aperature, new Location(lw, 29.464, 162, -418.554, 50, 0), skin),
				new MapNPC(this, Maps.Atronach, new Location(lw, -40.529, 162, -411.578, 50, 0), skin),
				new MapNPC(this, Maps.Limbo, new Location(lw, -43.526, 162, -414.568, 50, 0), skin),
				new MapNPC(this, Maps.Treehouse, new Location(lw, -47.539, 162, -417.566, 50, 0), skin),
				new MapNPC(this, Maps.Gateway, new Location(lw, -3.507, 162, -375.505, 50, 0), skin),
				new MapNPC(this, Maps.Pokemob, new Location(lw, -13.575, 162, -375.539, 50, 0), skin),
				new MapNPC(this, Maps.Tropical, new Location(lw, 29.487, 162, -395.502, 50, 0), skin),
				new MapNPC(this, Maps.Frigid, new Location(lw, -47.526, 162, -394.467, 50, 0), skin),
				new MapNPC(this, Maps.JungleRiver, new Location(lw, 34.500, 162, -393.475, 50, 0), skin),
				new MapNPC(this, Maps.EggHunt, new Location(lw, 34.502, 162, -419.485, 50, 0), skin),
				new MapNPC(this, Maps.NorthernSeas, new Location(lw, -12.512, 162, -461.551, 50, 0), skin));*/
		addNpcs2(new RandomGameNPC(this, new Location(lw, 10.525, 144, 19.538, 50, 0), skin));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		display(player);
	}

	public void updateNpc(Maps map) {
		for (MapNPC npc : npcs) {
			if (npc.getMap() == map) {
				npc.update();
				return;
			}
		}
	}

	public void updateRandomNpc() {
		for (RandomGameNPC npc : npcs2) {
			npc.update();
			return;
		}
	}

	@EventHandler
	public void onInteract(NPCInteractEvent e) {
		Player player = e.getWhoClicked();
		for (MapNPC npc : npcs)
			if (npc.getNpc() == e.getNPC()) {
				main.getGameManager().JoinMap(e.getWhoClicked(), npc.getMap());
				return;
			}
		for (RandomGameNPC npc : npcs2) {
			if (npc.getNpc() == e.getNPC()) {
				// if (npc.getMap() != null) {
				new ChooseGameGUI(main).inv.open(player);
				// main.getGameManager().JoinMap(e.getWhoClicked(), npc.getMap());
				// } else
				// player.sendMessage("Map was null");
				return;
			}
		}
	}

}
