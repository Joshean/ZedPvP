package pw.cinque.packetapi.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import pw.cinque.packetapi.WrappedPacket;

public class PacketReceiveEvent extends PacketEvent {

    private static final HandlerList handlers = new HandlerList();

    public PacketReceiveEvent(WrappedPacket packet, Player player) {
        super(packet, player);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
