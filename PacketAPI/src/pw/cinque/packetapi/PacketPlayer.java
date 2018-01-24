package pw.cinque.packetapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Queue;

@Getter
@RequiredArgsConstructor
class PacketPlayer {

    private final Player player;
    private final Queue<?> incomingQueue;
    private final Queue<?> outgoingQueue;
    
	public PacketPlayer(Player player, Queue<?> incomingQueue, Queue<?> outgoingQueue) {
		this.player = player;
		this.incomingQueue = incomingQueue;
		this.outgoingQueue = outgoingQueue;
	}

	public Player getPlayer() {
		return player;
	}

	public Queue<?> getIncomingQueue() {
		return incomingQueue;
	}

	public Queue<?> getOutgoingQueue() {
		return outgoingQueue;
	}

}
