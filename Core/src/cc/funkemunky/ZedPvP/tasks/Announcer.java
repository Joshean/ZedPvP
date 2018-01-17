package cc.funkemunky.ZedPvP.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;
import cc.funkemunky.ZedPvP.utils.JSONMessage;
import cc.funkemunky.ZedPvP.utils.JSONMessage.ClickableType;
import cc.funkemunky.ZedPvP.utils.MiscUtils;

public class Announcer {
	
	public List<String> messages;
	public int i;
	
	public Announcer() {
		messages = new ArrayList<String>();
		i = 0;
		
		for(String string : Core.getInstance().getConfig().getStringList("Announcements.Messages")) {
			messages.add(string);
		}
		
		new BukkitRunnable() {
			public void run() {
				if(messages.size() > 0) {
					if(MiscUtils.getConfigKeys("Announcements.Links").size() > 0) {
						int link = 1;
						for(String s : MiscUtils.getConfigKeys("Announcements.Links")) {
							if(messages.get(i).contains("$LINK:" + s + "$")) {
								JSONMessage msg = new JSONMessage();
								String[] string = messages.get(i).split("$LINK:" + s + "$");
								msg.addText(Color.translate(string[0]));
								if(string[1].contains("$END$")) {
									String[] newString = string[1].split("$end$");
									msg.addText(Color.translate(Core.getInstance().getConfig().getString("Announcements.Links." + s + ".String")))
									.addHoverText(Color.translate(Core.getInstance().getConfig().getString("Announcements.Links." + s + ".Hover"))).setClickEvent(ClickableType.OpenURL, Core.getInstance().getConfig().getString("Announcements.Links." + s + ".Link"));
									msg.addText(newString[1].replaceAll("$end$", ""));
								} else {
									msg.addText(Color.translate(Core.getInstance().getConfig().getString("Announcements.Links." + s + ".String")))
									.addHoverText(Color.translate(Core.getInstance().getConfig().getString("Announcements.Links." + s + ".Hover"))).setClickEvent(ClickableType.OpenURL, Core.getInstance().getConfig().getString("Announcements.Links." + s + ".Link"));
								}
								for(Player player : Bukkit.getOnlinePlayers()) {
									msg.sendToPlayer(player);
								}
								i = i < messages.size() - 1 || i == 0 ? i + 1 : 0;
							} else {
								link++;
							}
						}
						if(link > MiscUtils.getConfigKeys("Announcements.Links").size()) {
							Bukkit.broadcastMessage(Color.translate(messages.get(i)));
							i = i < messages.size() - 1 || i == 0 ? i + 1 : 0;
						}
					} else {
						Bukkit.broadcastMessage(Color.translate(messages.get(i)));
						i = i < messages.size() - 1 || i == 0 ? i + 1 : 0;
					}
				}
			}
		}.runTaskTimer(Core.getInstance(), 0L, 20L * 60L * 2L);
	}
	

}
