package anticheat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.user.User;

public class EventJoinQuit implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Keaton.getUserManager().add(new User(p));
		User user = Keaton.getUserManager().getUser(p.getUniqueId());
		user.setLoginMillis(System.currentTimeMillis());
		if(user.isStaff()) {
			if(!user.isHasAlerts()) {
				user.setHasAlerts(true);
				p.sendMessage(Keaton.getAC().getMessage().ALERTS_JOIN);
			}
		}
		Keaton.getAC().getChecks().event(e);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Keaton.getUserManager().remove(Keaton.getUserManager().getUser(p.getUniqueId()));
		Keaton.getAC().getChecks().event(e);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		Keaton.getUserManager().remove(Keaton.getUserManager().getUser(p.getUniqueId()));
		Keaton.getAC().getChecks().event(e);
	}
}
