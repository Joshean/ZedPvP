package cc.funkemunky.ZedPvP.cooldowns;

import org.bukkit.entity.Player;

public class Cooldown {
	
	private Player player;
	private CooldownType type;
	private long time;
	
	public Cooldown(Player player, CooldownType type, long time) {
		this.player = player;
		this.type = type;
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Player getPlayer() {
		return player;
	}

	public CooldownType getType() {
		return type;
	}

}
