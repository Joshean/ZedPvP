package anticheat.checks.movement;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.user.User;
import anticheat.utils.PlayerUtils;
import anticheat.utils.TimerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class Vclip extends Checks {
	public Map<UUID, Location> flag;
	public Location location;
	public TimerUtils t = new TimerUtils();

	public Vclip() {
		super("Vclip", ChecksType.MOVEMENT, Keaton.getAC(), 5, true, false);

		this.flag = new WeakHashMap<UUID, Location>();
	}

	@Override
	protected void onEvent(Event event) {
		if (!this.getState()) {
			return;
		}

		if (event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			Player p = e.getPlayer();

			if (flag.containsKey(p.getUniqueId())) {
				flag.remove(p.getUniqueId());
			}
		}

		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			Player p = e.getPlayer();
			if (p.getAllowFlight() || p.getVehicle() != null) {
				return;
			}

			if (e.getTo().getY() <= 0 || e.getTo().getY() >= p.getWorld().getMaxHeight()) {
				return;
			}

			if ((p.getLocation().getY() <= 0.0D) || (p.getLocation().getY() >= p.getWorld().getMaxHeight())) {
				return;
			}

			if (!PlayerUtils.hasBlocksNear(p)) {
				return;
			}

			if (PlayerUtils.isReallyOnground(p) && t.hasReached(location == null ? 500L : 2500L)) {
				flag.put(p.getUniqueId(), p.getLocation());
				location = p.getLocation();
				t.reset();
			}
			User user = Keaton.getUserManager().getUser(p.getUniqueId());
			int vl = user.getVL(this);

			double diff = Math.abs(e.getTo().getY() - e.getFrom().getY());
			if (diff >= 2.0 && !PlayerUtils.isAir(p)) {
				Alert(p, "*");
				flag(p, flag.get(p.getUniqueId()));
				user.setVL(this, vl + 1);

			}
		}
	}

	public void flag(Player p, Location l) {
		if (l != null) {
			p.teleport(l);
		}
	}
}
