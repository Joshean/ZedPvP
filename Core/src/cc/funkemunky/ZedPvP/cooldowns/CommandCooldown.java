package cc.funkemunky.ZedPvP.cooldowns;

import org.bukkit.entity.Player;

public class CommandCooldown {
	
	private Player player;
	private String command;
	private long time;
	
	public CommandCooldown(Player player, String command, long time) {
		this.player = player;
		this.command = command;
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

	public String getCommand() {
		return command;
	}

}
