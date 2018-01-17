package anticheat.checks.movement;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.user.User;
import anticheat.utils.CancelType;
import anticheat.utils.PlayerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class Jesus extends Checks {

	public Map<UUID, Integer> onWater;
	public Map<UUID, Integer> count;

	public Jesus() {
		super("Jesus", ChecksType.MOVEMENT, Keaton.getAC(), 10, true, true);

		this.onWater = new WeakHashMap<UUID, Integer>();
		this.count = new WeakHashMap<UUID, Integer>();
	}

	@Override
	protected void onEvent(Event event) {
		if(!this.getState()) {
			return;
		}
		if(event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			Player p = e.getPlayer();
			
			if(this.onWater.containsKey(p.getUniqueId())) {
				this.onWater.remove(p.getUniqueId());
			}
			if(this.count.containsKey(p.getUniqueId())) {
				this.count.remove(p.getUniqueId());
			}
		}
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			if ((e.getFrom().getX() == e.getTo().getX()) && (e.getFrom().getZ() == e.getTo().getZ())) {
				return;
			}
			Player p = e.getPlayer();
			User user = Keaton.getUserManager().getUser(p.getUniqueId());
			
			if (p.getAllowFlight()) {
				return;
			}
			
			if (!p.getNearbyEntities(1.0D, 1.0D, 1.0D).isEmpty()) {
				return;
			}
			
			if (PlayerUtils.isOnLilyPad(p)) {
				return;
			}
			
			if((System.currentTimeMillis() - user.isTeleported()) < 500L) {
				return;
			}
			
			if(Math.abs(System.currentTimeMillis() - user.isHit()) < 1500) {
				return;
			}

			if (user.placedBlock()) {
				user.setPlacedBlock(false);
				return;
			}
			int Count = 0;
			if (count.containsKey(p.getUniqueId())) {
				Count = count.get(p.getUniqueId());
			}
			if ((PlayerUtils.cantStandAtWater(p.getWorld().getBlockAt(p.getLocation())) ||
					PlayerUtils.cantStandAtLava(p.getWorld().getBlockAt(p.getLocation())))
					&& (PlayerUtils.isHoveringOverWater(p.getLocation()))
					&& (!PlayerUtils.isFullyInWater(p.getLocation()))) {
			    Count+= 2;
			} else {
				Count= Count > 0 ? Count - 1 : Count;
			}

			if (Count > 20) {
				user.setVL(this, user.getVL(this) + 1);
				user.setCancelled(this, CancelType.MOVEMENT);
				this.Alert(p, "*");
				
				Count = 0;
			}
			
			count.put(p.getUniqueId(), Count);
		}
	}

}
