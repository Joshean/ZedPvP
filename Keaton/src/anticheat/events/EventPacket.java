package anticheat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import anticheat.Keaton;
import anticheat.packets.events.PacketEvent;
import anticheat.packets.events.PacketKeepAliveEvent;
import anticheat.user.User;
import anticheat.utils.MathUtils;

public class EventPacket implements Listener {
	
	
	@EventHandler
	public void packet(PacketEvent e) {
		Keaton.getAC().getChecks().event(e);
		
		User user = Keaton.getUserManager().getUser(e.getPlayer().getUniqueId());
		long time = MathUtils.elapsed(user.getLastPacket());
		
        if (time >= 100) {
            long diff = time - 50;
            user.setPacketsFromLag(user.getPacketsFromLag() + (int)Math.ceil((double)diff / 50.0));
            
        } else if (user.getPacketsFromLag() > 0) {
            user.setPacketsFromLag(user.getPacketsFromLag() - 1);
        }
		user.setLastPacket(System.currentTimeMillis());
	}
	
	@EventHandler
	public void onKeepAlive(PacketKeepAliveEvent e) {
		Keaton.getAC().getChecks().event(e);
	}
	
	@EventHandler
	public void regainHealth(EntityRegainHealthEvent e) {
		Keaton.getAC().getChecks().event(e);
		
		if(e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			user.setLastHeal(System.currentTimeMillis());
		}
	}

}
