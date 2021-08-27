package anthony.SuperCraftBrawl.Game.classes;

import org.bukkit.entity.Player;

public class Cooldown {
	private long lastTime = System.currentTimeMillis() - 100000;
	private long cooldownTime = 1000;
	private Player player;

	public Cooldown(long cooldownTime) {
		this.cooldownTime = cooldownTime;
	}
	
	private Cooldown (Player player) {
		this.player = player;
	}

	public boolean useAndResetCooldown() {

		if (System.currentTimeMillis() - lastTime > cooldownTime) {
			reset();
			return true;
		} else {
			return false;
		}
	}

	public void reset() {
		lastTime = System.currentTimeMillis();
	}
}
