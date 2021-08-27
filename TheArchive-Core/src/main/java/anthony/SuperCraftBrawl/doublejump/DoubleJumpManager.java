package anthony.SuperCraftBrawl.doublejump;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import anthony.SuperCraftBrawl.Main;
import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.BaseClass;
import net.md_5.bungee.api.ChatColor;

public class DoubleJumpManager implements Listener {
	private final Main main;
	private final HashMap<Player, DoubleJumpData> data = new HashMap<>();

	public DoubleJumpManager(Main main) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	public double getVerticalVel(Player player) {
		GameInstance instance = main.getGameManager().GetInstanceOfPlayer(player);
		if (instance != null) {
			BaseClass bc = instance.classes.get(player);
			if (bc != null) {
				return bc.baseVerticalJump;
			}
		}
		
		return 1.0;
	}

	public DoubleJumpData getJumpData(Player player) {
		DoubleJumpData jumpData = data.get(player);
		if (jumpData == null) {
			jumpData = new DoubleJumpData();
			data.put(player, jumpData);
		}
		return jumpData;
	}
	
	@EventHandler
	public void OnToggleGameMode(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		
		if (event.getNewGameMode() == GameMode.ADVENTURE || event.getNewGameMode() == GameMode.SURVIVAL)
			event.getPlayer().setAllowFlight(true);
	}

	@EventHandler
	public void OnPlayerLeave(PlayerQuitEvent event) {
		data.remove(event.getPlayer()); // Remove data after player has left
	}

	@EventHandler
	public void OnToggleJump(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();

		if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
			try {
				DoubleJumpData jumpData = getJumpData(player);

				// Check if can double jump and then apply velocity
				if (jumpData.canDoubleJump) {
					jumpData.canDoubleJump = false;
					double forwardsFactor = 0.0, upFactor = getVerticalVel(player);
					Vector forwards = player.getLocation().getDirection().setY(0);
					double forwardsStrength = forwards.length();
					forwards.normalize().multiply(forwardsFactor + forwardsStrength / 5.0D);
					player.setVelocity(new Vector(forwards.getX(), upFactor, forwards.getZ()));
					for(Player worldPlayer : player.getWorld().getPlayers())
						worldPlayer.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 5);
				}
				// If can't double jump
				else {
					player.setVelocity(jumpData.lastVelocity);
				}
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Error when double jumping!");
				e.printStackTrace();
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void OnMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		DoubleJumpData jumpData = getJumpData(player);

		// If player is on ground, reset jump
		if (player.isOnGround())
			jumpData.canDoubleJump = true;

		jumpData.lastVelocity = player.getVelocity();

	}

}
