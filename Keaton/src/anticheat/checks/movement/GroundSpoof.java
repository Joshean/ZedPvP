package anticheat.checks.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
import anticheat.utils.Color;
import anticheat.utils.MathUtils;
import anticheat.utils.PlayerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class GroundSpoof extends Checks {
	

	private Map<UUID, Integer> verbose;
	public Map<UUID, Integer> violations;
	
	public GroundSpoof() {
		super("GroundSpoof", ChecksType.MOVEMENT, Keaton.getAC(), 9, true, true);
		
		verbose = new HashMap<UUID, Integer>();
		violations = new HashMap<UUID, Integer>();
	}

	@Override
	protected void onEvent(Event event) {

		if (!this.getState()) {
			return;
	    }
		
		if(event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			
			UUID uuid = e.getPlayer().getUniqueId();
			
			if(violations.containsKey(uuid)) {
				violations.remove(uuid);
			}
			if(verbose.containsKey(uuid)) {
				verbose.remove(uuid);
			}
		}

		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			Player p = e.getPlayer();

			Location from = e.getFrom().clone();
			Location to = e.getTo().clone();
			
			Location l = p.getLocation();
			double diff = to.toVector().distance(from.toVector());
			int x = l.getBlockX();
			int y = l.getBlockY();
			int z = l.getBlockZ();
			int violations = this.violations.getOrDefault(p.getUniqueId(), 0);
			Location blockLoc = new Location(p.getWorld(), x, y - 1, z);
			if(PlayerUtils.isGliding(p)) {
				return;
			}
			
			int verbose = this.verbose.getOrDefault(p.getUniqueId(), 0);

			if (p.getRemainingAir() == 300 && MathUtils.elapsed(Keaton.getUserManager().getUser(p.getUniqueId()).isHit()) < 1000L) {
				return;
			}

			if (p.getGameMode().equals(GameMode.CREATIVE) || p.getVehicle() != null || p.getAllowFlight() || p.getHealth() <= 0) {
				return;
			}
			
			User user = Keaton.getUserManager().getUser(p.getUniqueId());
			
			if((System.currentTimeMillis() - user.getLastBlockPlace()) < 1500L) {
				return;
			}

			if (p.isOnGround() && diff > 0.8 && blockLoc.getBlock().getType() == Material.AIR) {
				user.setVL(this, user.getVL(this) + 1);
				violations++;
				user.setCancelled(this, CancelType.MOVEMENT);
			} else {
				violations = violations > 0 ? violations-- : violations;
			}
			
			if(violations > 2) {
				Alert(p, Color.Gray + "Reason: " + Color.White + "Spoofed onGround");
				violations = 0;
			}
			if(p.getFallDistance() == 0.0 && user.getRealFallDistance() > 3.0 && PlayerUtils.isAir(p) && from.getY() > to.getY()) {	
				verbose++;
			} else {
				verbose = verbose > 0 ? verbose-- : 0;
			}
			
			if(verbose > 2) {
                user.setVL(this, user.getVL(this) + 1);
				
                verbose = 0;
				Alert(p, Color.Gray + "Reason: " + Color.White + "Spoofed FallDistance");
				user.setCancelled(this, CancelType.MOVEMENT);
			}
			
			this.verbose.put(p.getUniqueId(), verbose);
			this.violations.put(p.getUniqueId(), violations);
		}
	}
}