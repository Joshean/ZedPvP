package cc.funkemunky.ZedPvP.listeners;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Sets;

public class CommandBlockerListener extends PacketAdapter implements Listener {
	
    public CommandBlockerListener(Plugin plugin) {
        super(plugin, new PacketType[]{PacketType.Play.Client.TAB_COMPLETE});
    }

	private HashSet<String> blockedCmds = Sets.newHashSet("ver", "version", "about", "plugins", "bukkit", "pl", "calc",
			"bukkit:", "minecraft:");

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCmd(PlayerCommandPreprocessEvent event) {
		if (!event.getPlayer().isOp()) {
			for (String cmd : blockedCmds) {
				if (event.getMessage().startsWith("/" + cmd)) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "No permission.");
				}
			}
			if(event.getMessage().startsWith("/litebans")) {
				event.setCancelled(true);
			}
		}
	}

	public void onPacketReceiving(PacketEvent event) {
		if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
			PacketContainer packetContainer = event.getPacket();
			String tab = (String) packetContainer.getStrings().read(0);
			if (!event.getPlayer().isOp()) {
				for (String cmd : blockedCmds) {
					if ((tab.startsWith("/" + cmd) || tab.equals("/"))) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
