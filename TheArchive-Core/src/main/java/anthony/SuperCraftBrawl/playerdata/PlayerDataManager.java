package anthony.SuperCraftBrawl.playerdata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import anthony.SuperCraftBrawl.Main;

public class PlayerDataManager implements Listener {

	private final Main main;
	private final DatabaseManager manager;
	private HashMap<Player, PlayerData> playerData = new HashMap<>();
	private final HashMap<Player, PermissionAttachment> perms = new HashMap<>();

	public PlayerDataManager(Main main) {
		this.main = main;
		this.manager = this.main.getDatabaseManager();
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		if (!(loadPlayer(event.getPlayer()))) {
			event.getPlayer().kickPlayer("Player Data has not been loaded! Please relog");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		unloadPlayer(event.getPlayer());
	}

	public PlayerData getPlayerData(Player player) {
		return playerData.get(player);
	}

	public boolean loadPlayer(Player player) {
		try {
			System.out.print("Loading data for " + player.getName());
			PlayerData data = getSavedData(player);
			if (data == null)
				data = loadNewData(player);

			playerData.put(player, data);

			PermissionAttachment a = player.addAttachment(main);
			perms.put(player, a);
			a.setPermission("scb." + data.getRank().toString().toLowerCase(), true);

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void unloadPlayer(Player player) {
		PlayerData data = playerData.remove(player);
		saveData(data);
		perms.remove(player);
	}

	public PlayerData getSavedData(Player player) throws SQLException {
		System.out.print("Getting saved data for " + player.getName());
		PlayerData data = null;
		Connection c = manager.getConnection();
		Statement stmt = c.createStatement();
		ResultSet set = stmt
				.executeQuery("SELECT * FROM PlayerData WHERE UUID = '" + player.getUniqueId().toString() + "'");
		while (set.next()) {
			UUID uuid = UUID.fromString(set.getString("UUID"));
			String lastIp = player.getAddress().getAddress().getHostAddress();
			int roleID = set.getInt("RoleID");
			int tokens = set.getInt("Tokens");
			int wins = set.getInt("Wins");
			int kills = set.getInt("Kills");
			int deaths = set.getInt("Deaths");
			int flawlessWins = set.getInt("FlawlessWins");
			int losses = set.getInt("Losses");
			int winstreak = set.getInt("Winstreak");
			int cwm = set.getInt("Cwm");
			int melon = set.getInt("MelonCosmetic");
			int astronaut = set.getInt("AstronautCosmetic");
			int pm = set.getInt("PrivateMessages");
			int votes = set.getInt("Votes");
			int mysteryChests = set.getInt("MysteryChests");
			int blue = set.getInt("Blue");
			int red = set.getInt("Red");
			int green = set.getInt("Green");
			int yellow = set.getInt("Yellow");
			int muted = set.getInt("Muted");
			int exp = set.getInt("Exp");
			int level = set.getInt("Level");
			int bestTime = set.getInt("BestTime");
			int magicbroom = set.getInt("MagicBroom");
			int points = set.getInt("Points");
			data = new PlayerData(uuid, player.getName(), lastIp, roleID, tokens, wins, kills, deaths, flawlessWins,
					losses, winstreak, cwm, melon, astronaut, pm, votes, mysteryChests, blue, red, green, yellow, muted,
					exp, level, bestTime, magicbroom, points);
		}
		set.close();
		stmt.close();
		Statement classState = c.createStatement();
		ResultSet classSet = classState
				.executeQuery("SELECT * FROM PlayerClasses WHERE UUID = '" + player.getUniqueId().toString() + "'");
		while (classSet.next()) {
			int classID = classSet.getInt("ClassID");
			boolean purchased = classSet.getInt("Purchased") == 1;
			int timePurchased = classSet.getInt("TimePurchased");
			int gamesPlayed = classSet.getInt("GamesPlayed");
			data.playerClasses.put(classID, new ClassDetails(purchased, timePurchased, gamesPlayed));
		}
		classSet.close();
		return data;
	}

	public PlayerData loadNewData(Player player) {
		System.out.print("Loading new saved data for " + player.getName());
		String lastIp = player.getAddress().getAddress().getHostAddress();
		PlayerData newData = new PlayerData(player.getUniqueId(), player.getName(), lastIp);

		manager.executeUpdateCommand("INSERT INTO PlayerData (`UUID`, `LastPlayerName`, `LastIP`) VALUES ('"
				+ player.getUniqueId().toString() + "', '" + player.getName() + "', '" + lastIp + "');");

		return newData;
	}

	public void saveData(PlayerData data) {
		System.out.print("Saving data for " + data.playerName);
		manager.executeUpdateCommand("UPDATE PlayerData SET LastPlayerName = '" + data.playerName + "', LastIP = '"
				+ data.playerIP + "', RoleID = " + data.roleID + ", Tokens = " + data.tokens + ", Kills = " + data.kills
				+ ", Losses = " + data.losses + ", Votes = " + data.votes + ", FlawlessWins = " + data.flawlessWins
				+ ", Points = " + data.points + ", MagicBroom = " + data.magicbroom + ", Cwm = " + data.cwm + ", Blue = " + data.blue + ", Red = " + data.red + ", Green = " + data.green
				+ ", Yellow = " + data.yellow + ", MelonCosmetic = " + data.melon + ", PrivateMessages = " + data.pm
				+ ", Muted = " + data.muted + ", MysteryChests = " + data.mysteryChests + ", AstronautCosmetic = "
				+ data.astronaut + ", BestTime = " + data.bestTime + ", Exp = " + data.exp + ", Level = " + data.level
				+ ", Deaths = " + data.deaths + ", Wins = " + data.wins + " WHERE UUID = '" + data.playerUUID.toString()
				+ "';");
		String updateCMD = "INSERT INTO PlayerClasses (UUID, ClassID, TimePurchased, Purchased, GamesPlayed) VALUES ";
		int index = 0;

		for (Entry<Integer, ClassDetails> entry : data.playerClasses.entrySet()) {
			if (entry.getValue().hasUpdated) {
				if (index != 0)
					updateCMD += ", ";
				updateCMD += "('" + data.playerUUID.toString() + "', " + entry.getKey() + ", "
						+ entry.getValue().timePurchased + ", " + (entry.getValue().purchased ? 1 : 0) + ", "
						+ entry.getValue().gamesPlayed + ")";
				index++;
			}
		}

		if (index > 0) {
			updateCMD += "ON DUPLICATE KEY UPDATE TimePurchased = VALUES (TimePurchased), Purchased = VALUES (Purchased), GamesPlayed = VALUES (GamesPlayed);";
			System.out.print("Executing " + updateCMD);
			manager.executeUpdateCommand(updateCMD);
		}
	}

}
