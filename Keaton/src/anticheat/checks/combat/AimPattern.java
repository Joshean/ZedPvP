package anticheat.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.packets.events.PacketEvent;
import anticheat.packets.events.PacketTypes;
import anticheat.user.User;
import anticheat.utils.Color;
import anticheat.utils.MathUtils;
import anticheat.utils.TimerUtils;

@ChecksListener(events = {PacketEvent.class, PlayerQuitEvent.class})
public class AimPattern extends Checks {
	
	public Map<UUID, Map.Entry<Integer, Long>> aimAVerbose;
	public Map<UUID, Integer> aimABVerbose;
	public Map<UUID, Map.Entry<Integer, Long>> aimBVerbose;
	public Map<UUID, Integer> aimCVerbose;
	public Map<UUID, Integer> aimDVerbose;
	private Map<UUID, Integer> verbose;
	
	public AimPattern() {
		super("AimPattern", ChecksType.COMBAT, Keaton.getAC(), 5, true, false);
		
		this.verbose = new HashMap<UUID, Integer>();
		this.aimAVerbose = new HashMap<UUID, Map.Entry<Integer, Long>>();
		this.aimABVerbose = new HashMap<UUID, Integer>();
		this.aimBVerbose = new HashMap<UUID, Map.Entry<Integer, Long>>();
		this.aimCVerbose = new HashMap<UUID, Integer>();
		this.aimDVerbose = new HashMap<UUID, Integer>();
	}
	
	@Override
	protected void onEvent(Event event) {
		if(!getState()) {
			return;
		}
		if(event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;

			UUID uuid = e.getPlayer().getUniqueId();
			
			if(aimAVerbose.containsKey(uuid)) {
				aimAVerbose.remove(uuid);
			}
			if(aimABVerbose.containsKey(uuid)) {
				aimABVerbose.remove(uuid);
			}
			if(aimBVerbose.containsKey(uuid)) {
				aimBVerbose.remove(uuid);
			}
			if(aimCVerbose.containsKey(uuid)) {
				aimCVerbose.remove(uuid);
			}
			if(verbose.containsKey(uuid)) {
				verbose.remove(uuid);
			}
		}
		if(event instanceof PacketEvent) {
			PacketEvent e = (PacketEvent) event;
			
			if(e.getType() != PacketTypes.POSLOOK) {
				return;
			}
			
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			if(Math.abs(user.getLastYaw() - e.getYaw()) == 0) {
				return;
			}
			
			Entity entity = user.getLastHitPlayer();
			
			if(entity == null) {
				return;
			}
			
			float speed = (float) MathUtils.round(MathUtils.getYawDifference(new Location(player.getWorld(), 0, 0 , 0, e.getYaw(), 0), new Location(player.getWorld(), 0, 0, 0, (float)user.getLastYaw(), 0)) % 10, 0);
			
			debug("Calc: " + MathUtils.getAimRotation((float)user.getLastYaw(), MathUtils.getAimRotations(player, entity)[1], (float) Math.abs(user.getLastYaw() - e.getYaw())) + " Yaw: " + e.getYaw() + " yawDif: " + speed);
			user.setLastYaw(e.getYaw());
		}
		if(event instanceof PacketEvent) {
			PacketEvent e = (PacketEvent) event;
			
			if(e.getType() != PacketTypes.POSLOOK) {
				return;
			}
			
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			int verboseA = 0;
			int verboseB = this.aimABVerbose.getOrDefault(player.getUniqueId(), 0);
			
			long Time = TimerUtils.nowlong();
			if (this.aimAVerbose.containsKey(player.getUniqueId())) {
				verboseA = this.aimAVerbose.get(player.getUniqueId()).getKey().intValue();
				Time = this.aimAVerbose.get(player.getUniqueId()).getValue().longValue();
			}
			
			if(TimerUtils.elapsed(Time, 12000L)) {
				verboseA = 0;
				Time = TimerUtils.nowlong();
				
			}
			double yawDif = Math.abs(user.getLastYaw() - e.getYaw());
			if(yawDif > 1.0D && yawDif == user.getLastYawDifference() && user.getLastLastYawDifference() != user.getLastYawDifference() && yawDif != 0.0D) {
				verboseA++;
				//debug("Player: " + player.getName() + " Verbose(+1): " + verboseA + " YawDif: " + Math.abs(user.getLastYaw() - e.getYaw()) + " LastYawDif: " + user.getLastYawDifference());
			}
			
			if(user.getLastYawDifference() == Math.abs(user.getLastYaw() - e.getYaw()) && (Math.abs(user.getLastYaw() - e.getYaw()) != 0.0D || user.getLastYawDifference() != 0.0D)) {
				verboseB+= 1;
				//debug("Player: " + player.getName() + " Verbose(+1): " + verboseB + " YawDif: " + Math.abs(user.getLastYaw() - e.getYaw()) + " LastYawDif: " + user.getLastYawDifference());
			} else {
				verboseB = 0;
			}
			
			if((verboseA > 25 && (System.currentTimeMillis() - user.getAttackTime()) > 1000) || (verboseA > 15 && (System.currentTimeMillis() - user.getAttackTime()) < 1000)) {
				user.setVL(this, user.getVL(this) + 1);
				
				Alert(player, Color.Gray + "Reason: " + Color.White + "Yaw Patterns " + Color.Gray + " Type: " + Color.White + "Overall");
				verboseA = 0;
			}
			if(verboseB > 9) {
				user.setVL(this, user.getVL(this) + 1);
				
				Alert(player, Color.Gray + "Reason: " + Color.White + "Yaw Patterns " + Color.Gray + " Type: " + Color.White + "Short");
				verboseB = 0;
			}
				int verboseD = this.aimDVerbose.getOrDefault(player.getUniqueId(), 0);
				double outcome = Math.sqrt((player.getEyeLocation().getYaw() * player.getEyeLocation().getYaw()) + (player.getEyeLocation().getPitch() * player.getEyeLocation().getPitch()));
				//debug("Pitch Difference: " + user.getLastPitchDifference() + " Yaw Difference: " + user.getLastYawDifference());
				if(outcome != user.getLastYawOffset()) {
					if(MathUtils.round(Math.abs(outcome - user.getLastYawOffset()), 0) == user.getLastDifference()) {
						verboseD++;
						//debug("Verbose (+1): " + verboseD);
					}
				} else {
					verboseD = 0;
				}
				user.setLastDifference(MathUtils.round(Math.abs(outcome - user.getLastYawOffset()), 0));
				user.setLastYawOffset(outcome);
				this.aimDVerbose.put(player.getUniqueId(), verboseD);
			user.setLastLastYawDifference(user.getLastYawDifference());
			user.setLastYawDifference(Math.abs(user.getLastYaw() - e.getYaw()));
			user.setLastYaw(e.getYaw());
			
			this.aimAVerbose.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(verboseA, Time));
			this.aimABVerbose.put(player.getUniqueId(), verboseB);
		}
		if(event instanceof PacketEvent) {
            PacketEvent e = (PacketEvent) event;
			
			if(e.getType() != PacketTypes.POSLOOK) {
				return;
			}
			
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			int verbose = 0;
			long Time = TimerUtils.nowlong();
			if (this.aimBVerbose.containsKey(player.getUniqueId())) {
				verbose = this.aimBVerbose.get(player.getUniqueId()).getKey().intValue();
				Time = this.aimBVerbose.get(player.getUniqueId()).getValue().longValue();
			}
			
			if(TimerUtils.elapsed(Time, 17000L)) {
				verbose = 0;
				//debug("Reset");
				Time = TimerUtils.nowlong();
				
			}
			//debug("yaw: " + player.getEyeLocation().getYaw() % 3 + " pitch: " + player.getEyeLocation().getPitch() % 2);
			if(Math.abs(e.getPitch() - user.getLastPitch()) < 0.1  // If a player's pitch difference this less than 0.1.
					&& MathUtils.elapsed(user.getLastAimB()) > 500L  //If their last flag is greater than 0.5 seconds to dissern flags from Cinematic Mode.
					&& user.getLastYawDifference() / 2 > user.getLastPitchDifference() / 3 //This checks the ratio of yaw and pitch that AimAssist similar to Vapes use.
					&& Math.abs(user.getLastYawDifference() - Math.abs(e.getYaw() - user.getLastYaw())) > 1.0D  //If their yawDifference is greater than 1.0 to prevent false flags.
					&& user.getLastPitchDifference() != Math.abs(user.getLastPitch() - e.getPitch()) //If the pitch isn't a duplicate, which can be caused by lag or position packets being sent to the player.
					&& Math.abs(user.getLastPitch() - e.getPitch()) > 0.004D //Checks if the pitch difference is greater than 0.004D as thats the lowest pitch randomization gets.
					&& Math.abs(user.getLastPitchDifference() - Math.abs(e.getPitch() - user.getLastPitch())) < 0.008) //Checks the differences between the last difference and this difference
				{
                verbose++;
                //debug("Player: " + player.getName() + " Verbose(+1): " + verbose + " LastPitchDif: " + user.getLastPitchDifference() + " PitchDif: " + Math.abs(e.getPitch() - user.getLastPitch()) + " YawDif: " + Math.abs(e.getYaw() - user.getLastYaw()) + " LastYawDif: " + user.getLastYawDifference());
                user.setLastAimB(System.currentTimeMillis());
            } else if((MathUtils.elapsed(user.getLastAimB()) <= 500L) && user.getLastYawDifference() / 2 > user.getLastPitchDifference() / 3 && Math.abs(user.getLastYawDifference() - Math.abs(e.getYaw() - user.getLastYaw())) > 1.0D && user.getLastPitchDifference() != Math.abs(user.getLastPitch() - e.getPitch()) && Math.abs(user.getLastPitch() - e.getPitch()) > 0.004D && Math.abs(user.getLastPitchDifference() - Math.abs(e.getPitch() - user.getLastPitch())) < 0.008) {
                user.setLastAimB(System.currentTimeMillis());
            }
	        
			
			if(verbose > 8) {
				Alert(player, Color.Gray + "Reason: " + Color.White + "Pitch Patterns");
				verbose = 0;
			}
			
			user.setLastPitchDifference(Math.abs(user.getLastPitch() - e.getPitch()));
			user.setLastPitch(e.getPitch());
			
			this.aimBVerbose.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(verbose, Time));
		}
		if(event instanceof PacketEvent) {
			PacketEvent e = (PacketEvent) event;
			
			if(e.getType() != PacketTypes.POSLOOK) {
				return;
			}
			
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			int verbose = this.aimCVerbose.getOrDefault(player.getUniqueId(), 0);
			double pitchDifference = Math.abs(e.getPitch() - user.getLastPitchAimC());
			
			if(e.getPitch() > 80 || e.getPitch() < -80) {
				return;
			}
			
			if(Math.abs(pitchDifference - user.getLastPitchDifferenceAimC()) < 0.000009 && user.getLastYawDifference() >= 1.0) {
				verbose++;
			} else {
				if(user.getLastYawDifference() > 1.0 && Math.abs(pitchDifference - user.getLastPitchDifferenceAimC()) > 0.000009) {
					verbose = 0;
				}
					
			}
			
			if(verbose > 15) {
				user.setVL(this, user.getVL(this) + 1);
				
				verbose = 0;
				
				Alert(player, Color.Gray + "Reason: " + Color.White + "Impossible Pitch Movement");
			}
			
			this.aimCVerbose.put(player.getUniqueId(), verbose);
			user.setLastPitchAimC(e.getPitch());
			user.setLastPitchDifferenceAimC(pitchDifference);
		}
	}

}
