package anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.packets.events.PacketKillauraEvent;
import anticheat.packets.events.PacketTypes;
import anticheat.user.User;
import anticheat.utils.CancelType;
import anticheat.utils.Color;
import anticheat.utils.MathUtils;
import anticheat.utils.PlayerUtils;
import anticheat.utils.TimerUtils;

@ChecksListener(events = {EntityDamageByEntityEvent.class, PacketKillauraEvent.class, PlayerMoveEvent.class, PlayerQuitEvent.class})
public class KillAuraA extends Checks {
	
	public Map<UUID, Player> lastHit;
	public Map<UUID, Integer> count;
	public Map<UUID, Integer> aimVerbose;
	private Map<UUID, Long> directionHit;
	public Map<UUID, Double> yawDif;
	private Map<UUID, Location> location;
	private Map<UUID, Integer> autismVerbose;
	private Map<UUID, Integer> directionVerbose;

	public KillAuraA() {
		super("KillAura", ChecksType.COMBAT,  Keaton.getAC(), 14, true, false);

		lastHit = new ConcurrentHashMap<UUID, Player>();
		count = new HashMap<UUID, Integer>();
		aimVerbose = new HashMap<UUID, Integer>();
		yawDif = new ConcurrentHashMap<UUID, Double>();
		location = new HashMap<UUID, Location>();
		directionHit = new HashMap<UUID, Long>();
		autismVerbose = new HashMap<UUID, Integer>();
		directionVerbose = new HashMap<UUID, Integer>();

	}

	@Override
	protected void onEvent(Event event) {
		if (!this.getState()) {
			return;
		}
		if(event instanceof PacketKillauraEvent) {
			PacketKillauraEvent e = (PacketKillauraEvent) event;
			
			if(Keaton.getAC().getPing().getTPS() < 17) {
				return;
			}
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			if(e.getType() == PacketTypes.SWING) {
				user.addSwingPackets();
			}
			if(e.getType() == PacketTypes.USE) {
				user.addUsePackets();
			}
			
			if(user.getUsePackets() > user.getSwingPackets() && user.getSwingPackets() == 0) {
				user.setVL(this, user.getVL(this) + 1);
				user.setCancelled(this, CancelType.COMBAT);
				Alert(player, Color.Gray + "Reason: " + Color.White + "Invalid Swing " + Color.Gray + "Ping: " + Keaton.getAC().getPing().getPing(player));
			}
			if(user.getSwingPackets() > 0 && user.getUsePackets() > 0) {
				user.resetSwingPackets();
				user.resetUsePackets();
			}
		}
		if(event instanceof PacketKillauraEvent) {
			PacketKillauraEvent e = (PacketKillauraEvent) event;
			if(e.getType() != PacketTypes.USE) {
				return;
			}
			
			if(Keaton.getAC().getPing().getTPS() < 17) {
				return;
			}
			
			Player player = e.getPlayer();
			Entity entity = e.getEntity();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			Location location = this.location.getOrDefault(player.getUniqueId(), player.getLocation());
			int verbose = aimVerbose.getOrDefault(player.getUniqueId(), 0);
			
			if((Math.abs(MathUtils.getRotations(location, entity.getLocation())[0] - location.getYaw()) < 2D && MathUtils.getYawDifference(player.getLocation(), location) > 9) 
					|| (Math.abs(MathUtils.getRotations(location, entity.getLocation())[0] - location.getYaw()) < 4.0D && MathUtils.getYawDifference(player.getLocation(), location) > 40) 
					|| (Math.abs(entity.getVelocity().length()) > 0.1 && user.getDeltaXZ() > 0.2 && Math.abs(MathUtils.getRotations(location, entity.getLocation())[0] - location.getYaw()) < 2 && MathUtils.getYawDifference(player.getLocation(), location) > 1.4)) {
				verbose = Math.abs(MathUtils.getRotations(location, entity.getLocation())[0] - location.getYaw()) < 1.2 || (Math.abs(entity.getVelocity().length()) > 0.1 && user.getDeltaXZ() > 0.2) ? verbose + 2 : verbose + 1;
				//debug("Verbse (+1): " + verbose + " Rotation: " + MathUtils.getRotations(location, entity.getLocation())[0] + " Yaw: " + location.getYaw());
			} else {
				verbose = verbose > 0 ? verbose - 1 : verbose;
			}
			
			if(verbose > 7) {
				user.setVL(this, user.getVL(this) + 1);
				verbose = 0;
				Alert(player, Color.Gray + "Reason: " + Color.White + "Heuristic");
			}
			this.location.put(player.getUniqueId(), player.getLocation());
			this.aimVerbose.put(player.getUniqueId(), verbose);
		}
		//if(event instanceof PacketKillauraEvent) {
			//PacketKillauraEvent e = (PacketKillauraEvent) event;
			//if(e.getType() != PacketTypes.USE) {
			//	return;
			//}
			
			//Player player = e.getPlayer();
			//User user = Keaton.getUserManager().getUser(player.getUniqueId());
		   //int verbose = autismVerbose.getOrDefault(player.getUniqueId(), 0);
		    
		    //if(MathUtils.elapsed(user.getLastFlyPacket(), 51L) && Math.abs(MathUtils.elapsed(user.getLastPosPacket()) - 100) < 2) {
		    	 //   verbose++;
		    //} else {
		    //	    verbose = 0;
		   // }
		    
		    //if(verbose > 3) {
		    	//    user.setVL(this, user.getVL(this) + 1);
		    //	    verbose = 0;
		    //    Alert(player, Color.Gray + "Reason: " + Color.White + "Stupidly Simple " + Color.Gray + "Ping: " + Keaton.getAC().getPing().getPing(player));
		    //}
		    //autismVerbose.put(player.getUniqueId(), verbose);
		//}
		if(event instanceof PacketKillauraEvent) {
			PacketKillauraEvent e = (PacketKillauraEvent) event;
			if(e.getType() != PacketTypes.USE) {
				return;
			}
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			if(!TimerUtils.elapsed(directionHit.getOrDefault(player.getUniqueId(), 0L), 51L) && e.getEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize().dot(player.getLocation().getDirection()) > 0.97) {
				user.setVL(this, user.getVL(this) + 1);
				user.setCancelled(this, CancelType.COMBAT);
				user.setCancelled(this, CancelType.MOVEMENT);
				Alert(player, Color.Gray + "Reason: " + Color.White + "Direction " + Color.Gray + "Ping: " + Keaton.getAC().getPing().getPing(player));
			}
		}
		if(event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			if(e.getFrom().getYaw() == e.getTo().getYaw()) {
				return;
			}
			if(getYawDifference(e.getFrom(), e.getTo()) < 77.5) {
				return;
			}
			
			directionHit.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
		}
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if (e.getCause() != DamageCause.ENTITY_ATTACK) {
				return;
			}
			if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
				return;
			}
			Player player = (Player) e.getDamager();
			Player attacked = (Player) e.getEntity();
			
			if(Keaton.getAC().getPing().getPing(player) > 500 || Keaton.getAC().getPing().getPing(attacked) > 1000) {
				return;
			}

			int Count = 0;
			double yawDif = 0;
			Player lastPlayer = attacked;

			if (this.lastHit.containsKey(player.getUniqueId())) {
				lastPlayer = this.lastHit.get(player.getUniqueId());
			}

			if (count.containsKey(player.getUniqueId())) {
				Count = count.get(player.getUniqueId());
			}
			if (this.yawDif.containsKey(player.getUniqueId())) {
				yawDif = this.yawDif.get(player.getUniqueId());
			}

			if (lastPlayer == attacked) {
				double offset = PlayerUtils.getOffsetOffCursor(player, attacked);
				double Limit = 101D;
				double distance = player.getLocation().distance(attacked.getLocation());
				Limit += distance > 4.0 ? distance * 90 : distance * 40;
				Limit += (attacked.getVelocity().length() + player.getVelocity().length()) * 66;
				Limit += Math.abs(yawDif - Math.abs(player.getLocation().getY() - attacked.getLocation().getY())) * 10;
				Limit += (Keaton.getAC().getPing().getPing(player) + Keaton.getAC().getPing().getPing(attacked)) * 0.28;

				if (offset > Limit) {
					Count+= 2;
				} else {
					Count = Count >= -3 ? Count-- : 0;
				}

				if (Count > 13) {
					User user = Keaton.getUserManager().getUser(player.getUniqueId());
					user.setVL(this, user.getVL(this) + 1);
					user.setCancelled(this, CancelType.COMBAT);
					Count = 0;
					Alert(player, Color.Gray + "Reason: " + Color.White + "Hitboxes " + Color.Gray + "Ping: " + Keaton.getAC().getPing().getPing(player));
				}

				this.count.put(player.getUniqueId(), Count);
				this.lastHit.put(player.getUniqueId(), attacked);
			}
		}
		if(event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			double yawDif = Math.abs(e.getFrom().getYaw() - e.getTo().getYaw()); 
			this.yawDif.put(e.getPlayer().getUniqueId(), yawDif);
		}
		if (event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			Player p = e.getPlayer();
			UUID uuid = p.getUniqueId();

			if(this.count.containsKey(uuid)) {
				this.count.remove(uuid);
			}
			if(this.directionHit.containsKey(uuid)) {
				this.directionHit.remove(uuid);
			}
			if(this.yawDif.containsKey(uuid)) {
				this.yawDif.remove(uuid);
			}
			if(this.lastHit.containsKey(uuid)) {
				this.lastHit.remove(uuid);
			}
			if(this.autismVerbose.containsKey(uuid)) {
				this.autismVerbose.remove(uuid);
			}
			if(aimVerbose.containsKey(uuid)) {
				aimVerbose.remove(uuid);
			}
			if(location.containsKey(uuid)) {
				location.remove(uuid);
			}
		}
	}
	
    public static float getYawDifference(Location location, Location location2) {
        float f = getYaw(location);
        float f2 = getYaw(location2);
        float f3 = Math.abs(f - f2);
        if (f < 90.0f && f2 > 270.0f || f2 < 90.0f && f > 270.0f) {
            f3 -= 360.0f;
        }
        return Math.abs(f3);
    }
    
    public static float getYaw(Location location) {
        float f = (location.getYaw() - 90.0f) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }
}