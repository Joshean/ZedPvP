package cc.funkemunky.ZedPvP.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;

public class RubiesCommand implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 0) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(Color.Gray + "Rubies: " + Color.Red + Core.getInstance().getRubyManager().getRubies(player));
			} else {
				sender.sendMessage(Color.Red + "You can only do administrative commands in console.");
			}
			return true;
		} else {
			if(args[0].equalsIgnoreCase("shop")) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					Core.getInstance().getRubyGUI().openMainGUI(player);
					player.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Opened the Ruby Shop!");
				} else {
					sender.sendMessage(Color.Red + "You can only do administrative commands in console.");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("give")) {
				if(sender.hasPermission("zedpvp.admin") || sender.hasPermission("zedpvp.rubies.give")) {
					if(args.length == 3) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						int amount = Integer.parseInt(args[2]);
						
						Core.getInstance().getRubyManager().addRubies(target.getPlayer(), amount);
						sender.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Gave " + target.getName() + " " + amount + " rubies!");
						
						if(Bukkit.getPlayer(args[1]) != null) {
							target.getPlayer().sendMessage(Core.getInstance().getPrefix() + Color.Gray + "You were given " + amount + " rubies.");
						}
						return true;
					}
					sender.sendMessage(Color.Red + "Invalid arugments!");
				} else {
					sender.sendMessage(Color.Red + "No permissison.");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("take")) {
				if(sender.hasPermission("zedpvp.admin") || sender.hasPermission("zedpvp.rubies.take")) {
					if(args.length == 3) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						int amount = Integer.parseInt(args[2]);
						
						Core.getInstance().getRubyManager().subtractRubies(target.getPlayer(), amount);
						sender.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Took " + amount + " rubies from " + target.getName() + "!");
						
						if(Bukkit.getPlayer(args[1]) != null) {
							target.getPlayer().sendMessage(Core.getInstance().getPrefix() + Color.Gray +  amount + " rubies were taken from your account.");
						}
						return true;
					}
					sender.sendMessage(Color.Red + "Invalid arugments!");
				} else {
					sender.sendMessage(Color.Red + "No permissison.");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("set")) {
				if(sender.hasPermission("zedpvp.admin") || sender.hasPermission("zedpvp.rubies.set")) {
					if(args.length == 3) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						int amount = Integer.parseInt(args[2]);
						
						Core.getInstance().getRubyManager().setRubies(target.getPlayer(), amount);
						sender.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Set " + target.getName() + "'s ruby balance to " + amount + ".");
						
						if(Bukkit.getPlayer(args[1]) != null) {
							target.getPlayer().sendMessage(Core.getInstance().getPrefix() + Color.Gray + "Your ruby balance was set to " + amount + ".");
						}
					}
				}
			}
		}
		return false;
	}

}
