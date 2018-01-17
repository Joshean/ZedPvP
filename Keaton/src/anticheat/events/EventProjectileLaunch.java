package anticheat.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import anticheat.Keaton;

public class EventProjectileLaunch implements Listener {
	
	@EventHandler
	public void onLaunch(ProjectileLaunchEvent event)  {
		Keaton.getAC().getChecks().event(event);
	}

}
