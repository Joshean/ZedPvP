package cc.funkemunky.ZedPvP.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RawcastCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("zedpvp.rawcast") && !sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments. Put in a message!");
			return true;
		}

		String message = "";
		for (int i = 0; i != args.length; i++)
			message += args[i] + " ";

		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
		return true;
	}

}
