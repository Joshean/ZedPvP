package anticheat.checks.other;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.packets.events.PacketEvent;
import anticheat.user.User;
import anticheat.utils.CancelType;
import anticheat.utils.Color;
import anticheat.utils.MathUtils;
import anticheat.utils.TimerUtils;

@ChecksListener(events = {PacketEvent.class, PlayerQuitEvent.class})
public class Timer extends Checks {
	
	private Map<UUID, Map.Entry<Integer, Long>> packets;
	private Map<UUID, Integer> verbose;
	private List<Player> toCancel;
	
	public Timer() {
		super("Timer", ChecksType.OTHER, Keaton.getAC(), 10, true, false);
		
		packets = new HashMap<UUID, Map.Entry<Integer, Long>>();
		verbose = new HashMap<UUID, Integer>();
		toCancel = new ArrayList<Player>();
	}
	
	protected void onEvent(Event event) {
		if(!getState()) {
			return;
		}
		if(event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			
			if(packets.containsKey(e.getPlayer().getUniqueId())) {
				packets.remove(e.getPlayer().getUniqueId());
			}
			if(verbose.containsKey(e.getPlayer().getUniqueId())) {
				verbose.remove(e.getPlayer().getUniqueId());
			}
			if(toCancel.contains(e.getPlayer())) {
				toCancel.remove(e.getPlayer());
			}
		}
		if(event instanceof PacketEvent) {
			PacketEvent e = (PacketEvent) event;
			
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			int packets = 0;
			long Time = System.currentTimeMillis();
			int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
			
			if (this.packets.containsKey(player.getUniqueId())) {
				packets = this.packets.get(player.getUniqueId()).getKey().intValue();
				Time = this.packets.get(player.getUniqueId()).getValue().longValue();
			}
			
			if((System.currentTimeMillis() - user.getLastPacket()) > 100L) {
				toCancel.add(player);
			}
			double threshold = 11;
			if(TimerUtils.elapsed(Time, 500L)) {
				if(toCancel.remove(player) && packets <= 13) {
					return;
				}
				if(packets > threshold + user.getPosPackets() && user.getPosPackets() < 5) {
					verbose = (packets - threshold) > 10 ? verbose + 2 : verbose + 1;
				} else {
					verbose = 0;
				}
				
				if(verbose > 4) {
					user.setVL(this, user.getVL(this) + 1);
					user.setCancelled(this, CancelType.MOVEMENT);
					if(user.getVL(this) > 1) {
						Alert(player, Color.Gray + "Reason: " + Color.Red + "Experimental " + Color.Gray + "Timer Speed: " + Color.White + MathUtils.round(packets / threshold, 2));
					}
				}
				packets = 0;
				Time = TimerUtils.nowlong();
				user.setPosPacket(0);
			}
			packets++;
			this.packets.put(player.getUniqueId(), new SimpleEntry<Integer, Long>(packets, Time));
			this.verbose.put(player.getUniqueId(), verbose);
		}
	}

}