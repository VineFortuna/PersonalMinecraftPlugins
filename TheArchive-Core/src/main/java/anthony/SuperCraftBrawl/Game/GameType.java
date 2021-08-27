package anthony.SuperCraftBrawl.Game;

public enum GameType {
	NORMAL(5), FRENZY(100), DUEL(2), CTF(0);
	
	private int maxPlayers;
	
	private GameType(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
}
