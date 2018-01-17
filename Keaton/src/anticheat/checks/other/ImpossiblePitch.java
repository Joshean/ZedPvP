package anticheat.checks.other;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.packets.events.PacketEvent;
import anticheat.packets.events.PacketTypes;
import anticheat.user.User;
import anticheat.utils.CancelType;
import anticheat.utils.Color;
import anticheat.utils.MathUtils;

@ChecksListener(events = {PacketEvent.class})
public class ImpossiblePitch extends Checks {
	
	public ImpossiblePitch() {
		super("ImpossiblePitch", ChecksType.OTHER, Keaton.getAC(), 6, true, true);
	}
	
	@Override
	protected void onEvent(Event event) {
		if(!getState()) {
			return;
		}
		
		if(event instanceof PacketEvent) {
			PacketEvent e = (PacketEvent) event;
			
			if(e.getType() != PacketTypes.LOOK) {
				return;
			}
			
			Player player = e.getPlayer();
			
			if(e.getPitch() > 90.1F || e.getPitch() < -90.1F) {
				User user = Keaton.getUserManager().getUser(player.getUniqueId());
				
				user.setVL(this, user.getVL(this) + 1);
				user.setCancelled(this, CancelType.MOVEMENT);
				Alert(player, Color.Gray + "Pitch: " + Color.White + MathUtils.round(e.getPitch(), 4));
			}
		}
	}

}
