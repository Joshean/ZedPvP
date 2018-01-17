package cc.funkemunky.ZedPvP.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import cc.funkemunky.ZedPvP.utils.Color;

public class EventCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("zedpvp.admin") && !sender.hasPermission("zedpvp.event")) {
			sender.sendMessage(Color.Red + "No permission.");
			return true;
		}
		
		if(args.length == 0) {
			sender.sendMessage(Color.Dark_Gray + Color.Strikethrough + "----------------------------");
			sender.sendMessage(Color.Gold + "Event Manager Help");
			sender.sendMessage("");
			sender.sendMessage(Color.White + "/event start <name>");
			sender.sendMessage(Color.White + "/event stop <name>");
			sender.sendMessage(Color.White + "/event setcapdelay <name> <seconds>");
			sender.sendMessage(Color.White + "/event create <name> <faction> <delay>");
			sender.sendMessage(Color.White + "/event wand");
			sender.sendMessage(Color.White + "/event remove <name>");
		}
		
		return false;
	}

}
