package anthony.SuperCraftBrawl.Game.projectile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import anthony.SuperCraftBrawl.Game.GameManager;

public class ProjectileManager {
	private static double JUMP_DISTANCE = 0.1;

	private GameManager manager;
	private List<BaseProjectile> shotProjectiles = new ArrayList<>();

	public ProjectileManager(GameManager manager) {
		this.manager = manager;

		BukkitRunnable checkCollisions = new BukkitRunnable() {
			@Override
			public void run() {
				checkAllCollision();
			}
		};
		checkCollisions.runTaskTimer(manager.getMain(), 0, 1);
	}

	public boolean isProjectile(Entity entity) {
		for (BaseProjectile proj : shotProjectiles)
			if (proj.getEntity().equals(entity))
				return true;
		return false;
	}

	public void shootProjectile(ItemProjectile itemProj, Location loc, Vector vel) {
		ItemStack itemStack = itemProj.getItem().clone();
		itemProj.lastLoc = loc;
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(UUID.randomUUID().toString());
		itemStack.setItemMeta(im);
		Item item = loc.getWorld().dropItem(loc, itemStack);
		item.setVelocity(vel);
		itemProj.setEntity(item);
		shotProjectiles.add(itemProj);
	}

	public void checkAllCollision() {
		List<BaseProjectile> projectilesToRemove = new ArrayList<>();
		for (BaseProjectile baseProj : shotProjectiles) {
			if (baseProj.tooOld() || checkCollision(baseProj)) {
				projectilesToRemove.add(baseProj);
				baseProj.destroy();
			} else {
				baseProj.addAge(1);
			}
			baseProj.lastLoc = baseProj.getEntity().getLocation();
		}

		shotProjectiles.removeAll(projectilesToRemove);
	}

	public boolean checkCollision(BaseProjectile proj) {
		Entity entity = proj.getEntity();
		// Starting and end locations
		Location entLoc = entity.getLocation();
		Location lastLoc = proj.lastLoc;
		
		// Calculate direction and length
		Vector dir = entLoc.clone().subtract(lastLoc).toVector();
		double length = dir.length();
		
		// Work out number of jumps to calculate in between
		int numberOfJumps = (int) Math.ceil(length / JUMP_DISTANCE);
		dir.multiply(1.0D / numberOfJumps);
		
		// Loop through each player
		for (Player player : proj.getInstance().players) {
			if (player != proj.getShooter()) {
				// Starting coordinates
				double x = lastLoc.getX(), y = lastLoc.getY(), z = lastLoc.getZ();
				// Player position
				Location playerLoc = player.getLocation();
				double px = playerLoc.getX(), py = playerLoc.getY() + 1, pz = playerLoc.getZ();
				// Player bounds
				double ex = 0.8, ey = 1.5, ez = 0.8;
				
				// Check for collision, by stepping in a certain direction
				boolean collided = false;
				for (int i = 0; i < numberOfJumps; i++) {
					x+= dir.getX(); y += dir.getY(); z+= dir.getZ();
					if (x < px - ex || x > px + ex)
						continue;
					if (z < pz - ez || z > pz + ez)
						continue;
					if (y < py - ey || y > py + ey)
						continue;
					collided = true;
				}
				// Break if there was no collision
				if(!collided)
					break;
				
				// Hit if there was a collision
				proj.getOnHit().onHit(player);
				return true;
			}
		}
		return false;
	}

}
