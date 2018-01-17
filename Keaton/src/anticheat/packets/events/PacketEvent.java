package anticheat.packets.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEvent extends Event {
	
	private Player player;
	private float yaw;
	private float pitch;
	private PacketTypes type;
	private Location from;
	private Location to;
	private static final HandlerList handlers;
	
	static {
		handlers = new HandlerList();
	}
	
	public PacketEvent(Player player, Location from, Location to, float yaw, float pitch, PacketTypes type) {
		this.player = player;
		this.yaw = yaw;
		this.pitch = pitch;
		this.type = type;
		this.from = from;
		this.to = to;
	}

	public Player getPlayer() {
		return player;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}
	
	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}

	public PacketTypes getType() {
		return type;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
