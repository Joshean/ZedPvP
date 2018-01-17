package anticheat.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import anticheat.Keaton;

public class EventPacketMoveEvent implements Listener {
	
	Map<Player, Location> locations;
	
	public EventPacketMoveEvent() {
		this.locations = new HashMap<Player, Location>();
		
		new BukkitRunnable() {
			public void run() {
				for(Player online : Bukkit.getOnlinePlayers()) {
					Location location = online.getLocation();
					Location lastLocation = online.getLocation();
					if(EventPacketMoveEvent.this.locations.containsKey(online)) {
						lastLocation = EventPacketMoveEvent.this.locations.get(online);
					} else {
						EventPacketMoveEvent.this.locations.put(online, location);
					}
					
					Bukkit.getPluginManager().callEvent(new PacketedMovementEvent(online, lastLocation, location));
					EventPacketMoveEvent.this.locations.put(online, location);
				}
			}
		}.runTaskTimer(Keaton.getAC(), 0L, 1L);
	}
	
	@EventHandler
	public void onMove(PacketedMovementEvent event) {
		Keaton.getAC().getChecks().event(event);
	}

}
