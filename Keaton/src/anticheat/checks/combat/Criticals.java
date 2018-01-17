package anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.user.User;
import anticheat.utils.CancelType;
import anticheat.utils.PlayerUtils;

@ChecksListener(events = { EntityDamageByEntityEvent.class, PlayerQuitEvent.class })
public class Criticals extends Checks {
	
	private Map<UUID, Integer> verbose;

	public Criticals() {
		super("Criticals", ChecksType.COMBAT, Keaton.getAC(), 4, true, true);
		
		verbose = new HashMap<UUID, Integer>();
	}

	@Override
	protected void onEvent(Event event) {
		
		if (!this.getState()) {
			return;
		}
		
		if(event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			
			if(verbose.containsKey(e.getPlayer().getUniqueId())) {
				verbose.remove(e.getPlayer().getUniqueId());
			}
		}
		
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			
			if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
				return;
			}
			
			Player player = (Player) e.getDamager();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
			
			if ((System.currentTimeMillis() - user.isVelocity()) < 1200L) {
				return;
			}
			
			if (PlayerUtils.hasSlabsNear(player.getLocation())) {
				return;
			}
			
			Location l = player.getLocation().clone();
			l.add(0.0, player.getEyeHeight() + 1.0, 0.0);
			
			if (PlayerUtils.hasBlocksNear(l)) {
				return;
			}
			
			if(player.getFallDistance() > 0.0D && user.getRealFallDistance() == 0.0D) {
				verbose++;
			} else {
				verbose = verbose > 0 ? verbose - 1 : 0;
			}
			
			if(verbose > 1) {
				user.setVL(this, user.getVL(this) + 1);
				user.setCancelled(this, CancelType.COMBAT);
				verbose = 0;
				Alert(player, "*");
			}
			
			this.verbose.put(player.getUniqueId(), verbose);
		}
	}

}