package anticheat.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import anticheat.Keaton;
import anticheat.user.User;
import anticheat.utils.CancelType;
import anticheat.utils.CustomLocation;
import anticheat.utils.MathUtils;
import anticheat.utils.PlayerUtils;

public class EventPlayerMove implements Listener {
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		Keaton.getAC().getChecks().event(event);
		
		User user = Keaton.getUserManager().getUser(event.getPlayer().getUniqueId());
		if(user == null) {
			return;
		}
		user.setTeleported(System.currentTimeMillis());
	}
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Keaton.getAC().getChecks().event(event);
		
		Player p = event.getPlayer();
		User user = Keaton.getUserManager().getUser(p.getUniqueId());
		
		double horizontal = Math.sqrt(Math.pow(event.getTo().getX() - event.getFrom().getX(), 2.0)
				+ Math.pow(event.getTo().getZ() - event.getFrom().getZ(), 2.0));
		double vertical = Math.sqrt(Math.pow(event.getTo().getY() - event.getFrom().getY(), 2.0));
		user.setDeltaXZ(horizontal);
		user.setDeltaY(vertical);
		if (user.getIceTicks() < 0) {
			user.setIceTicks(0);
		}
		
		if(!PlayerUtils.isOnGround(p.getLocation()) && event.getFrom().getY() > event.getTo().getY()) {
			user.setRealFallDistance(user.getRealFallDistance() + MathUtils.getVerticalDistance(event.getFrom(), event.getTo()));;
		}
		
		if(PlayerUtils.isOnGround(p.getLocation())) {
			user.setRealFallDistance(0.0D);;
		}
		 CustomLocation lastLocation = user.getLastPlayerLocation(p.getUniqueId(), 1);
         if (lastLocation != null) {
             user.addPlayerLocation(p.getUniqueId(), new CustomLocation(lastLocation.getX() + event.getTo().getX(), lastLocation.getY() + event.getTo().getY(), lastLocation.getZ() + event.getTo().getZ(), lastLocation.getYaw(), lastLocation.getPitch()));
         }
		
		Location blockLoc = p.getLocation().subtract(0.0D, 1.0D, 0.0D);
		Location blockLoc2 = p.getLocation().subtract(0.0D, 1.5D, 0.0D);
		if (blockLoc.getBlock().getType() == Material.ICE || blockLoc.getBlock().getType() == Material.PACKED_ICE
				|| blockLoc2.getBlock().getType() == Material.ICE || blockLoc2.getBlock().getType() == Material.PACKED_ICE) {
			user.setIceTicks(user.getIceTicks() + 1);
		} else {
			user.setIceTicks(user.getIceTicks() - 1);
		}
		if(!p.getAllowFlight()) {
			if (PlayerUtils.isReallyOnground(p)) {
				user.setGroundTicks(user.getGroundTicks() + 1);
				user.setAirTicks(0);
			} else {
				user.setGroundTicks(0);
				if(!p.isFlying()) {
					user.setAirTicks(user.getAirTicks() + 1);
				}
			}
		}
		
        Location above = p.getLocation().clone().add(0.0D, 2.0D, 0.0D);
		
		if(above.getBlock().getType().isSolid()) {
			user.setBlockTicks(user.getBlockTicks() + 1);
		} else {
			user.setBlockTicks(user.getBlockTicks() > 0 ? user.getBlockTicks() - 1 : 0);
		}
		
		if(event.isCancelled()) {
			user.setTeleported(System.currentTimeMillis());
		}
		
		if(user.isCancelled() == CancelType.MOVEMENT) {
			if(user.getSetbackLocation() != null) {
				event.setTo(user.getSetbackLocation());
				user.setCancelled(null, CancelType.NONE);
			} else {
				event.setCancelled(true);
			}
		} else {
			if(user.isCancelled() == CancelType.NONE) {
				if(PlayerUtils.isOnGround(p.getLocation())) {
					user.setSetbackLocation(p.getLocation());
				}
			}
		}
		
	}
}