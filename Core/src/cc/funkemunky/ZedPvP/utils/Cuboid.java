package cc.funkemunky.ZedPvP.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Cuboid {
	
	private Location one;
	private Location two;
	private ArrayList<UUID> players;
	
	public Cuboid(Location one, Location two) {
		this.one = one;
		this.two = two;
		
		players = new ArrayList<UUID>();
	}
	
	public boolean isInCuboid(Player player) {
		if(isInCuboid(player.getLocation())) {
			players.add(player.getUniqueId());
		} else if(players.contains(player.getUniqueId())) {
			players.remove(player.getUniqueId());
		}
		return players.contains(player.getUniqueId());
	}
	
	public boolean isInCuboid(Location location) {
		for(int x = Math.min(one.getBlockX(), two.getBlockX()); x < Math.max(one.getBlockX(), two.getBlockX()) ; x++) {
			for(int y = Math.min(one.getBlockY(), two.getBlockY()); y < Math.max(one.getBlockY(), two.getBlockY()) ; y++) {
				for(int z = Math.min(one.getBlockZ(), two.getBlockZ()); z < Math.max(one.getBlockZ(), two.getBlockZ()) ; z++) {
					if(location.getBlockX() == x && location.getBlockY() == y && location.getBlockZ() == z) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Location getOne() {
		return one;
	}

	public void setOne(Location one) {
		this.one = one;
	}

	public Location getTwo() {
		return two;
	}

	public void setTwo(Location two) {
		this.two = two;
	}

	public ArrayList<UUID> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<UUID> players) {
		this.players = players;
	}

}
