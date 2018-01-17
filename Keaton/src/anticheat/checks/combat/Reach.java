package anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.packets.events.PacketKillauraEvent;
import anticheat.packets.events.PacketTypes;
import anticheat.user.User;
import anticheat.utils.Color;
import anticheat.utils.CustomLocation;
import anticheat.utils.MathUtils;

@ChecksListener(events = { PlayerMoveEvent.class, EntityDamageByEntityEvent.class,
		PlayerQuitEvent.class, PacketKillauraEvent.class })
public class Reach extends Checks {

	public Map<UUID, Integer> count;
	public Map<UUID, Integer> reachBVerbose;

	public Reach() {
		super("Reach", ChecksType.COMBAT, Keaton.getAC(), 15, true, true);

		this.count = new HashMap<UUID, Integer>();
		this.reachBVerbose = new HashMap<UUID, Integer>();
	}

	@Override
	protected void onEvent(Event event) {
		if (!this.getState()) {
			return;
		}
		if(event instanceof PacketKillauraEvent) {
			PacketKillauraEvent e = (PacketKillauraEvent) event;
			if(e.getType() != PacketTypes.USE) {
				return;
			}
			
			if(!(e.getEntity() instanceof Player)) {
				return;
			}
			
			Player player = e.getPlayer();
			Player attacked = (Player) e.getEntity();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			if(MathUtils.elapsed(user.getLastPacketHit()) < 1200L) {
				user.setLastPacketHit(System.currentTimeMillis());
				return;
			}

	        double distance = MathUtils.trim(3, MathUtils.getHorizontalDistance(player.getEyeLocation(), attacked.getEyeLocation()) - 0.35);
	        double maxReach = 3.1;
	        double attackerPing = Keaton.getAC().getPing().getPing(player);
	        
			double velocityCombined = Math.abs(player.getVelocity().length()) + Math.abs(attacked.getVelocity().length());
			maxReach += velocityCombined * 0.32;
			maxReach += !player.isSprinting() ? user.getDeltaXZ() * 1.2 : 0;
			maxReach += attackerPing * 0.0021;
			
			//debug("Reach: " + distance + " maxReach: " + maxReach + " attackerPing: " + attackerPing 
			//		+ " velocityComb :" + velocityCombined * 0.32 + " delta: " + (!player.isSprinting() ? user.getDeltaXZ() * 1.2 : 0));
			if(distance > maxReach) {
				user.setVL(this, user.getVL(this) + 1);
				
        	        if(user.getVL(this) > 5) {
        	        	Alert(player,
        						Color.Gray + "Reason: " + Color.White + "First-Hit " + Color.Gray + "Ping: " + Color.White
        								+ attackerPing + Color.Gray + " Reach: " + Color.White
        								+ distance + Color.Gray + " > " + Color.White + maxReach);
        	        }
			}
			
			
			user.setLastPacketHit(System.currentTimeMillis());
		}
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
	        
			if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
				return;
			}

	        Player attacker = (Player) e.getDamager();

	        Player attacked = (Player) e.getEntity();
	        if (attacker.getGameMode() == GameMode.CREATIVE) return;

	        float attackerPing = Keaton.getAC().getPing().getPing(attacker);
	        float attackedPing = Keaton.getAC().getPing().getPing(attacked);

	        double distance = MathUtils.trim(3, attacker.getLocation().distance(attacked.getLocation()) - 0.35 - Math.abs(attacker.getLocation().getY() - attacked.getLocation().getY()));

	        double YawDifference = Math.abs(180 - Math.abs(attacker.getLocation().getYaw() - attacked.getLocation().getYaw()));

	        float max = 3.0F;


	        max += ((attackedPing + attackerPing) / 2) * 0.0024;

	        User attackerData = Keaton.getUserManager().getUser(attacker.getUniqueId());
	        User attackedData = Keaton.getUserManager().getUser(attacked.getUniqueId());

	        double deltaXCombined = attackedData != null ? Math.abs(attackedData.getDeltaXZ2()) + Math.abs(attackerData.getDeltaXZ2()) : Math.abs(attackerData.getDeltaXZ2());
	        double deltaY = attackedData != null ? attackedData.getDeltaY2() : 0;
	        double combinedDeltas = (deltaXCombined + deltaY) * 2.0;

	        max += combinedDeltas * 2.0;
	        max += YawDifference > 100  && deltaY < 0.1 ? YawDifference * 0.01 : YawDifference * 0.001;
	        max += attacked.getVelocity().length() * 5.0;
	        max += Math.abs(deltaXCombined) > 0.0 ? (attackerData.getHits() < 7 ? (attackerData.getHits() <= 2 && attackerData.getHits() > 0 ? attackerData.getHits() * 0.5 : attackerData.getHits() * 0.35) : 7 * 0.35) : 0;

	        for (PotionEffect potionEffect : attacked.getActivePotionEffects()) {
	            if (potionEffect.getType() == PotionEffectType.SPEED) {
	                int amplifier = potionEffect.getAmplifier() + 1;
	                max += 0.15 * amplifier;
	                break;
	            }
	        }

	        for (PotionEffect potionEffect : attacker.getActivePotionEffects()) {
	            if (potionEffect.getType()== PotionEffectType.SPEED) {
	                int amplifier = potionEffect.getAmplifier() + 1;
	                max += 0.15 * amplifier;
	                break;
	            }
	        }
	        attackerData.setReachVL(distance >= max ? attackerData.getReachVL() + 1 : attackerData.getReachVL() > 0 ? (distance >= 3.0 ? attackerData.getReachVL() - 0.25D : attackerData.getReachVL()) : attackerData.getReachVL());
	        //if(distance >= max) { 
	        	   // debug("Damager: " + attacker.getName() + " Count: " + attackerData.getReachVL() + " Hits: " + attackerData.getHits() + " Reach: " + distance + " Max: " + max);
	       // }
	        if(attackerData.getReachVL() > 5) {
	        	    attackerData.setVL(this, attackerData.getVL(this) + 1);
	        	    attackerData.setReachVL(0);
	        	    if(attackerData.getVL(this) > 8) {
		        	    Alert(attacker,
								Color.Gray + "Reason: " + Color.White + "Counted " + Color.Gray + "Ping: " + Color.White
										+ attackerPing + Color.Gray + " Reach: " + Color.White
										+ distance + Color.Gray + " > " + Color.White + max);
	        	    }
	        }
		}
		if(event instanceof PacketKillauraEvent) {
			PacketKillauraEvent e = (PacketKillauraEvent) event;
			if(e.getType() != PacketTypes.USE) {
				return;
			}
			
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			Entity entity = e.getEntity();
			
			if(player.getAllowFlight()) {
				return;
			}
			
			if(Keaton.getAC().getPing().getTPS() < 17) {
				return;
			}
			
			double reach = MathUtils.getHorizontalDistance(player.getLocation(), entity.getLocation()) - MathUtils.getYDifference(player.getLocation(), entity.getLocation());
			double maxReach = 3.4;
			double playerPing = Keaton.getAC().getPing().getPing(player);
			double entityPing = entity instanceof Player ? Keaton.getAC().getPing().getPing((Player) entity) : 0;
			double deltaXZ = user.getDeltaXZ();
			double entityDeltaXZ = entity.getVelocity().length() > 0 ? entity.getVelocity().length() : 0;
			int verbose = this.reachBVerbose.getOrDefault(player.getUniqueId(), 0);
			
			maxReach += (playerPing + entityPing) * 0.004;
			maxReach += (deltaXZ + entityDeltaXZ) * 0.9;
			maxReach += Math.abs(player.getEyeLocation().getYaw() - entity.getLocation().getYaw()) * 0.004;
			
			if(reach > maxReach) {
				verbose++;
				//debug("Verbose(+1): " + verbose + " Reach: " + reach + " MaxReach: " + maxReach + " pDelta: " + deltaXZ + " eDelta: " + entityDeltaXZ + " playerPing: " + playerPing);
			} else {
				verbose = verbose > -2 ? verbose - 1 : verbose;
			}
			
			if(verbose > 7) {
				user.setVL(this, user.getVL(this) + 1);
				verbose = 0;
				if(user.getVL(this) > 5) {
					Alert(player, Color.Gray + "Reason: " + Color.White + "UseEntity " + Color.Gray + "Ping: " + Color.White + Keaton.getAC().getPing().getPing(player) +  Color.Gray + " Reach: " + Color.White
							+ reach + Color.Gray + " > " + Color.White + maxReach);
				}
			}
			
			reachBVerbose.put(player.getUniqueId(), verbose);
			
		}
		if (event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			
			if (count.containsKey(e.getPlayer().getUniqueId())) {
				count.remove(e.getPlayer().getUniqueId());
			}
			if (reachBVerbose.containsKey(e.getPlayer().getUniqueId())) {
				reachBVerbose.remove(e.getPlayer().getUniqueId());
			}
		}
	}

}
