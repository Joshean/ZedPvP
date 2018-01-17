package anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.user.User;
import anticheat.utils.MathUtils;

@ChecksListener(events = { ProjectileLaunchEvent.class, PlayerInteractEvent.class, PlayerQuitEvent.class })
public class Fastbow extends Checks {

	public Map<UUID, Integer> verbose;

	public Fastbow() {
		super("Fastbow", ChecksType.COMBAT, Keaton.getAC(), 5, true, true);

		this.verbose = new HashMap<UUID, Integer>();
	}

	@Override
	protected void onEvent(Event event) {
		if (!this.getState()) {
			return;
		}
		if (event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;

			if (verbose.containsKey(e.getPlayer().getUniqueId())) {
				verbose.remove(e.getPlayer().getUniqueId());
			}
		}

		if (event instanceof ProjectileLaunchEvent) {
			ProjectileLaunchEvent e = (ProjectileLaunchEvent) event;
			if (e.getEntity() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getEntity();
				if(!(arrow.getShooter() instanceof Player)) {
					return;
				}
				
				if(arrow.getShooter() == null) {
					return;
				}
				
				Player player = (Player) arrow.getShooter();
				User user = Keaton.getUserManager().getUser(player.getUniqueId());
				long threshold = Math.round(240 * arrow.getVelocity().length());
				int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
				
				if(MathUtils.elapsed(user.getLastBow()) < threshold) {
					verbose++;
					if(Keaton.getAC().getConfig().getBoolean("checks." + getName() + ".cancelled")) {
						e.setCancelled(true);
					}
				} else {
					verbose = verbose > 0 ? verbose - 2 : verbose;
				}
				
				if(verbose > 4) {
					user.setVL(this, user.getVL(this) + 1);
					verbose = 0;
					Alert(player, "*");
				}
				
				this.verbose.put(player.getUniqueId(), verbose);
			}
		}
	}

}
