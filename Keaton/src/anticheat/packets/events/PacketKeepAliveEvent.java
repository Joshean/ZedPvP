package anticheat.packets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketKeepAliveEvent extends Event {
	public Player player;
	private static final HandlerList handlers;
	private PacketKeepAliveType type;

	static {
		handlers = new HandlerList();
	}

	public PacketKeepAliveEvent(final Player player, PacketKeepAliveType type) {
		super();
		this.player = player;
		this.type = type;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public PacketKeepAliveType getType() {
		return this.type;
	}

	public HandlerList getHandlers() {
		return PacketKeepAliveEvent.handlers;
	}

	public static HandlerList getHandlerList() {
		return PacketKeepAliveEvent.handlers;
	}
	
	public enum PacketKeepAliveType {
		SERVER, CLIENT;
	}
}