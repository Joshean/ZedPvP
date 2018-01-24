package pw.cinque.packetapi.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pw.cinque.packetapi.WrappedPacket;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class PacketEvent extends Event implements Cancellable {

    private final WrappedPacket packet;
    private boolean cancelled;
    private Player player;
    
	public PacketEvent(WrappedPacket packet, Player player) {
		this.packet = packet;
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public WrappedPacket getPacket() {
		return packet;
	}

}
