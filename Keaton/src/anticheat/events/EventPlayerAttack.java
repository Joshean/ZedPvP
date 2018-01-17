package anticheat.events;

import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import anticheat.Keaton;
import anticheat.user.User;
import anticheat.utils.CancelType;

public class EventPlayerAttack implements Listener {

	private Map<Player, Long> lastHit = new WeakHashMap<Player, Long>();
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		Keaton.getAC().getChecks().event(e);
		
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			if (user != null) {
				user.setIsHit(System.currentTimeMillis());
			}
		}
		if(e.getDamager() instanceof Player) {
			Player player = (Player) e.getDamager();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			user.setLastHitPlayer(e.getEntity());

			if ((System.currentTimeMillis() - lastHit.getOrDefault((Player) e.getDamager(), 0L)) > 2500L) {
				user.resetHits();
			} else {
				user.addHit();
			}
			if(user.isCancelled() == CancelType.COMBAT) {
				e.setCancelled(true);
				user.setCancelled(null, CancelType.NONE);
			}
			
			lastHit.put((Player) e.getDamager(), System.currentTimeMillis());
			user.setAttackTime(System.currentTimeMillis());
		}
	}
}
