package cc.funkemunky.ZedPvP.events;

import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.events.koth.Koth;
import cc.funkemunky.ZedPvP.listeners.CuboidWandListener;
import cc.funkemunky.ZedPvP.utils.Color;
import cc.funkemunky.ZedPvP.utils.Cuboid;
import cc.funkemunky.ZedPvP.utils.MiscUtils;

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
			sender.sendMessage(Color.White + "/event create <name> <delay in seconds>");
			sender.sendMessage(Color.White + "/event wand");
			sender.sendMessage(Color.White + "/event remove <name>");
			sender.sendMessage(Color.Dark_Gray + Color.Strikethrough + "----------------------------");
			return true;
		} else {
			if(args[0].equalsIgnoreCase("start") && args.length > 1) {
				Koth koth = Core.getInstance().getKothManager().getKoth(args[1]);
				
				if(koth == null || !Core.getInstance().getKothManager().getAllKoths().contains(koth)) {
					sender.sendMessage(Color.Red + "That Koth does not exist!");
					return true;
				}
				
				if(!Core.getInstance().getKothManager().getAllActiveKoths().contains(koth)) {
					Core.getInstance().getKothManager().startKoth(koth);
					sender.sendMessage(Color.Green + "Started " + koth.getName() + " koth!");
				} else {
					sender.sendMessage(Color.Red + "That Koth is already started! Do /event stop <name> to stop it.");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("stop") && args.length > 1) {
				Koth koth = Core.getInstance().getKothManager().getKoth(args[1]);
				
				if(koth == null || !Core.getInstance().getKothManager().getAllKoths().contains(koth)) {
					sender.sendMessage(Color.Red + "That Koth does not exist!");
					return true;
				}
				
				if(Core.getInstance().getKothManager().getAllActiveKoths().contains(koth)) {
					Core.getInstance().getKothManager().stopKoth(koth);
					sender.sendMessage(Color.Green + "Stopped " + koth.getName() + " koth!");
				} else {
					sender.sendMessage(Color.Red + "That Koth is not on! Do /event start <name> to start it.");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("setcapdelay") && args.length > 2) {
				long time = TimeUnit.SECONDS.toMillis(Long.parseLong(args[2]));
				Koth koth = Core.getInstance().getKothManager().getKoth(args[1]);
				
				if(koth == null || !Core.getInstance().getKothManager().getAllKoths().contains(koth)) {
					sender.sendMessage(Color.Red + "That Koth does not exist!");
					return true;
				}
				
				if(Core.getInstance().getKothManager().getAllActiveKoths().contains(koth)) {
					koth.setTimeLeft(time);
				} else {
					sender.sendMessage(Color.Red + "That koth isn't enabled!");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("wand")) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					player.getInventory().addItem(MiscUtils.WAND);
					player.updateInventory();
					player.sendMessage(Color.Green + "Added selection wand to your inventory!");
				} else {
					sender.sendMessage(Color.Red + "Players only!");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("create") && args.length > 2) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					String kothName = args[1];
					long delay = TimeUnit.SECONDS.toMillis(Long.parseLong(args[2]));
					Cuboid cuboid = CuboidWandListener.cuboids.get(player);
					
					if(cuboid.getOne() == null || cuboid.getTwo() == null) {
						player.sendMessage(Color.Red + "You need to select a capzone! /event wand");
						return true;
					}
					
					Core.getInstance().getKothManager().createKoth(kothName, cuboid.getOne(), cuboid.getTwo(), delay);
					player.sendMessage(Color.Green + "Successfully created koth " + kothName + "!");
				} else {
					sender.sendMessage(Color.Red + "Players only!");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("remove") && args.length > 1) {
				Koth koth = Core.getInstance().getKothManager().getKoth(args[1]);
				
				if(koth != null && Core.getInstance().getKothManager().getAllActiveKoths().contains(koth)) {
					Core.getInstance().getKothManager().removeKoth(koth);
					sender.sendMessage(Color.Green + "Successfully deleted koth " + koth.getName() + "!");
				} else {
					sender.sendMessage(Color.Red + "That koth does not exist!");
				}
				return true;
			}
			sender.sendMessage(Color.Red + "Invalid arguments!");
			return true;
		}
	}

}