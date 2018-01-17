package cc.funkemunky.ZedPvP.events.koth;

import org.bukkit.entity.Player;

import com.massivecraft.factions.Faction;

import cc.funkemunky.ZedPvP.utils.Cuboid;

public class Koth {
	
	private String name;
	private Cuboid cuboid;
	private Faction faction;
	private Player capping;
	private long timeLeft;
	private long defaultTime;
	private long upTime;
	
	public Koth(String name, Cuboid cube, Faction faction, long millis) {
		this.name = name;
		this.cuboid = cube;
		this.faction = faction;
		this.timeLeft = millis;
		this.defaultTime = millis;
		this.upTime = 0L;
	}

	public long getDefaultTime() {
		return defaultTime;
	}

	public void setDefaultTime(long defaultTime) {
		this.defaultTime = defaultTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Cuboid getCuboid() {
		return cuboid;
	}

	public void setCuboid(Cuboid cuboid) {
		this.cuboid = cuboid;
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public Player getCapping() {
		return capping;
	}

	public void setCapping(Player capping) {
		this.capping = capping;
	}

	public long getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(long timeLeft) {
		this.timeLeft = timeLeft;
	}

	public long getUpTime() {
		return upTime;
	}

	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}

}
