package anthony.SuperCraftBrawl.playerdata;

import java.util.HashMap;
import java.util.UUID;

import anthony.SuperCraftBrawl.Game.classes.ClassType;
import anthony.SuperCraftBrawl.ranks.Rank;

public class PlayerData {
	public UUID playerUUID;

	public String playerName, playerIP;

	public int roleID = 0, tokens = 0, wins = 0, kills = 0, deaths = 0, flawlessWins = 0, losses = 0, winstreak = 0,
			votes = 0, mysteryChests = 0, cwm = 0, melon = 0, astronaut = 0, pm = 0, blue = 0, red = 0, green = 0,
			yellow = 0, muted = 0, exp = 0, level = 0, bestTime = 0, magicbroom = 0, points = 0;

	public HashMap<Integer, ClassDetails> playerClasses = new HashMap<>();

	public PlayerData(UUID playerUUID, String playerName, String playerIP, int roleID, int tokens, int wins, int kills,
			int deaths, int flawlessWins, int losses, int winstreak, int cwm, int melon, int astronaut, int pm,
			int votes, int mysteryChests, int blue, int red, int green, int yellow, int muted, int exp, int level, int bestTime, int magicbroom, int points) {
		this(playerUUID, playerName, playerIP);
		this.roleID = roleID;
		this.tokens = tokens;
		this.wins = wins;
		this.kills = kills;
		this.deaths = deaths;
		this.flawlessWins = flawlessWins;
		this.losses = losses;
		this.winstreak = winstreak;
		this.cwm = cwm;
		this.melon = melon;
		this.astronaut = astronaut;
		this.pm = pm;
		this.votes = votes;
		this.mysteryChests = mysteryChests;
		this.blue = blue;
		this.red = red;
		this.green = green;
		this.yellow = yellow;
		this.muted = muted;
		this.exp = exp;
		this.level = level;
		this.bestTime = bestTime;
		this.magicbroom = magicbroom;
		this.points = points;
	}

	public boolean isPurchased(ClassType type) {
		return this.playerClasses.get(type.getID()) != null && this.playerClasses.get(type.getID()).purchased
				|| type.getTokenCost() == 0;
	}

	public PlayerData(UUID playerUUID, String playerName, String playerIP) {
		this.playerUUID = playerUUID;
		this.playerName = playerName;
		this.playerIP = playerIP;

	}

	public Rank getRank() {
		return Rank.getRankFromID(roleID);
	}
}
