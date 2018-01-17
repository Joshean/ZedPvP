package anticheat.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffectType;

import anticheat.Keaton;
import anticheat.user.User;
import anticheat.utils.PlayerUtils;

public class EventPlayerVelocity implements Listener {

	@EventHandler
	public void onMove(PlayerVelocityEvent event) {
		Keaton.getAC().getChecks().event(event);
		
		User user = Keaton.getUserManager().getUser(event.getPlayer().getUniqueId());
		if(!PlayerUtils.hasPotionEffect(event.getPlayer(), PotionEffectType.POISON)) {
			user.setTookVelocity(System.currentTimeMillis());
		}
	}

}
