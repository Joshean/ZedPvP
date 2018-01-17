package anticheat.packets.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketKillauraEvent extends Event {
	
	private Player player;
	private Entity entity;
	private static final HandlerList handlers;
	private PacketTypes type;

	static {
		handlers = new HandlerList();
	}

	public PacketKillauraEvent(final Player player, Entity entity, final PacketTypes type) {
		super();
		this.player = player;
		this.type = type;
		this.entity = entity;
	}

	public Player getPlayer() {
		return this.player;
	}

	public PacketTypes getType() {
		return this.type;
	}

	public Entity getEntity() {
		return entity;
	}

	public HandlerList getHandlers() {
		return PacketKillauraEvent.handlers;
	}

	public static HandlerList getHandlerList() {
		return PacketKillauraEvent.handlers;
	}
}
