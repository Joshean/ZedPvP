package cc.funkemunky.ZedPvP.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;

public class StaffmodeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((!sender.hasPermission("zedpvp.staff") && !sender.isOp()) || !(sender instanceof Player)) {
			sender.sendMessage(Color.Red + "No permission.");
			return true;
		}
		if(args.length == 0) {
			Player p = (Player) sender;

			Core.getInstance().getStaffMode().setStaffMode(p);
		} else {
			if(sender.hasPermission("zedpvp.admin") || sender.isOp()) {
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					sender.sendMessage(Core.getInstance().getPrefix() + Color.Red + "The player '" + args[0] + "' is currently not online.");
					return true;
				}
				
				if(target.hasPermission("zedpvp.staff") && !target.hasPermission("zedpvp.admin") && !target.isOp()) {
					Core.getInstance().getStaffMode().setStaffMode(target);
					sender.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Successfully toggled the staffmode of " + target.getName() + (Core.getInstance().getStaffMode().isInStaffMode(target) ? "on" : "off") + "!");
				} else if(!target.hasPermission("zedpvp.staff")) {
					sender.sendMessage(Core.getInstance().getPrefix() + Color.Red + "You cannot toggle staffmode for nonstaff.");
				} else {
					sender.sendMessage(Core.getInstance().getPrefix() + Color.Red + "You cannot toggle the staffmode for this person.");
				}
			}
		}
		return true;
	}

}
