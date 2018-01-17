package anticheat.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import anticheat.Keaton;
import anticheat.packets.events.PacketKillauraEvent;
import anticheat.packets.events.PacketTypes;
import anticheat.user.User;

public class EventPacketUse implements Listener {
	
	@EventHandler
	public void onUse(PacketKillauraEvent e) {
		Keaton.getAC().getChecks().event(e);
		
		User user = Keaton.getUserManager().getUser(e.getPlayer().getUniqueId());
		
		if(user != null && e.getType() == PacketTypes.USE) {
			user.setLastAttack(System.currentTimeMillis());
		}
	}

}
