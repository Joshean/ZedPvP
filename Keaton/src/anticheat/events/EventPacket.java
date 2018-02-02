package anticheat.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import anticheat.Keaton;
import anticheat.packets.events.PacketEvent;
import anticheat.packets.events.PacketKeepAliveEvent;
import anticheat.user.User;
import anticheat.utils.MathUtils;
import pw.cinque.packetapi.WrappedPacket;
import pw.cinque.packetapi.event.PacketReceiveEvent;

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
	private void onPacketReceive(PacketReceiveEvent event) {
	    WrappedPacket packet = event.getPacket();
	    Player player = event.getPlayer();
	    if(packet.getType().equals("PacketPlayInFlying")) {
	         //Bukkit.broadcastMessage("Flying: " + MathUtils.elapsed(Keaton.getUserManager().getUser(player.getUniqueId()).getLastFlyPacket()));
	    	     Keaton.getUserManager().getUser(player.getUniqueId()).setLastFlyPacket(System.currentTimeMillis());
	    }
	    if(packet.getType().equalsIgnoreCase("PacketPlayInPositionLook")) {
	         //Bukkit.broadcastMessage("PositionLook: " + MathUtils.elapsed(Keaton.getUserManager().getUser(player.getUniqueId()).getLastPosLookPacket()));
	         Keaton.getUserManager().getUser(player.getUniqueId()).setLastPosLookPacket(System.currentTimeMillis());
	    }
	    if(packet.getType().equalsIgnoreCase("PacketPlayInPosition")) {
	        // Bukkit.broadcastMessage("Position: " + MathUtils.elapsed(Keaton.getUserManager().getUser(player.getUniqueId()).getLastPosPacket()));
	         Keaton.getUserManager().getUser(player.getUniqueId()).setLastPosPacket(System.currentTimeMillis());
	    }
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
