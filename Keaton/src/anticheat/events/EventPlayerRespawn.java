package anticheat.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import anticheat.Keaton;
import anticheat.user.User;

public class EventPlayerRespawn implements Listener {
	
	@EventHandler
	public void respawn(PlayerRespawnEvent event) {
		Keaton.getAC().getChecks().event(event);
		
		User user = Keaton.getUserManager().getUser(event.getPlayer().getUniqueId());
		if(user != null) {
			user.setLoginMillis(System.currentTimeMillis());
		}
	}

}
