package anticheat.commands;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import anticheat.Keaton;
import anticheat.utils.Color;

public class ViewLogCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("keaton.admin") && !sender.hasPermission("keaton.logs") &&
				!sender.isOp()) {
			sender.sendMessage(Keaton.getAC().getMessage().NO_PERMISSION);
			return true;
		}
		
		if(args.length != 2) {
			sender.sendMessage(Color.Red + "Invalid arugments.");
			return true;
		}
		
		String player = args[0];
		
		int page = Integer.parseInt(args[1]);
		if(!Keaton.getAC().getConfig().getBoolean("MySQL.Enabled")) {
			try {
				String path = Keaton.getAC().getDataFolder() + File.separator + "logs" + File.separator + player + ".txt";
				
				File log = new File(path);
				if(!log.exists()) {
					sender.sendMessage(Color.Red + player + "'s log doesn't exist! Either the player doesn't have a log or you typed the name wrong.");
					return true;
				}
				List<String> lines = FileUtils.readLines(log);
				
				if(lines.size() / (page * 1) < 1) {
					sender.sendMessage(Color.Red + player + "'s log doesn't have a page " + page + "!");
					return true;
				}
				
				sender.sendMessage(Color.Dark_Gray + Color.Strikethrough + "----------------------------------------------------");
				sender.sendMessage(Color.Gold + Color.Bold + player + "'s log" + Color.Gray +  " : " + Color.Gold + "Page [" + Color.Red + page + Color.Gold  + "]");
				sender.sendMessage("");
			    for(int i = (page * 10) - 10 ; page < page * 10 ; i++) {
					if(i < lines.size()) {
						sender.sendMessage(Color.White + lines.get(i));
					}
			    }
				sender.sendMessage(Color.Dark_Gray + Color.Strikethrough + "----------------------------------------------------");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Player target = Bukkit.getPlayer(player);
			if(target != null) {
				List<String> lines = Keaton.getAC().getMySQL().getLogs(target);
				
				if(lines == null || lines.size() == 0) {
					sender.sendMessage(Color.Red + player + "'s log doesn't exist! Either the player doesn't have a log or you typed the name wrong.");
					return true;
				}
				sender.sendMessage(Color.Dark_Gray + Color.Strikethrough + "----------------------------------------------------");
				sender.sendMessage(Color.Gold + Color.Bold + player + "'s log" + Color.Gray +  " : " + Color.Gold + "Page [" + Color.Red + page + Color.Gold  + "]");
				sender.sendMessage("");
			    for(int i = (page * 10) - 10 ; page < page * 10 ; i++) {
					if(i < lines.size()) {
						sender.sendMessage(Color.White + lines.get(i));
					}
			    }
				sender.sendMessage(Color.Dark_Gray + Color.Strikethrough + "----------------------------------------------------");
			} else {
				
			}
		}
		return true;
	}

}
