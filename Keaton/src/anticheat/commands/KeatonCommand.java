package anticheat.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.comphenix.protocol.ProtocolLibrary;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.packets.PacketListeners;
import anticheat.user.User;
import anticheat.utils.Color;
import anticheat.utils.MathUtils;
import anticheat.utils.Messages;

public class KeatonCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("Keaton.staff")) {
			sender.sendMessage(Color.Red + "No permission.");
			return true;
		}
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Keaton.getAC().getGUIManager().openMainGUI(player);
				player.sendMessage(Color.Green + "Opened GUI.");
			} else {
				sender.sendMessage(Color.Red
						+ "You must be a player to open the GUI. Do /Keaton help for the commands a non-player can do.");
			}
		} else {
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("Keaton.admin")) {
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_CONFIG);
					Keaton.getAC().reloadConfig();
					Keaton.getAC().reloadMessages();
					new Messages(Keaton.getAC());
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_PARTLY_DONE);
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_PLUGIN);
					HandlerList.unregisterAll(Keaton.getAC());
					Keaton.getAC().getServer().getScheduler().cancelAllTasks();
					Keaton.getAC().registerEvents();
					ProtocolLibrary.getProtocolManager().getPacketListeners()
							.forEach(ProtocolLibrary.getProtocolManager()::removePacketListener);
					new PacketListeners();
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_PARTLY_DONE);
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_CHECKS);
					Keaton.getAC().getChecks().getDetections().clear();
					Keaton.getAC().getChecks().init();
					for (Checks check : Keaton.getAC().getChecks().getDetections()) {
						check.setState(Keaton.getAC().getConfig().getBoolean("checks." + check.getName() + ".enabled"));
						check.setBannable(
								Keaton.getAC().getConfig().getBoolean("checks." + check.getName() + ".bannable"));
					}
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_PARTLY_DONE);
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_VIOLATIONS);
					Keaton.getAC().clearVLS();
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_PARTLY_DONE);
					sender.sendMessage(Keaton.getAC().getMessage().RELOADING_DONE);
				} else {
					sender.sendMessage(Keaton.getAC().getMessage().NO_PERMISSION);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("status")) {
				if (sender.hasPermission("Keaton.admin")) {
					ArrayList<String> bannable = new ArrayList<String>();
					for (Checks check1 : Keaton.getAC().getChecks().getDetections()) {
						if (check1.isBannable()) {
							bannable.add(Color.Gray + (check1.getState() ? Color.Green + check1.getName()
									: Color.Red + check1.getName()).toString() + Color.Gray);
						}
					}
					ArrayList<String> notbannable = new ArrayList<String>();
					for (Checks check1 : Keaton.getAC().getChecks().getDetections()) {
						if (!check1.isBannable()) {
							notbannable.add(Color.Gray + (check1.getState() ? Color.Green + check1.getName()
									: Color.Red + check1.getName()).toString() + Color.Gray);
						}
					}
					for (String string : Keaton.getAC().getMessage().Keaton_STATUS) {
						sender.sendMessage(Color.translate(string).replaceAll("%notbannable%", notbannable.toString())
								.replaceAll("%bannable%", bannable.toString()).replaceAll("%tps%",
										String.valueOf(MathUtils.trim(1, Keaton.getAC().getPing().getTPS()))));
					}
				} else {
					sender.sendMessage(Keaton.getAC().getMessage().NO_PERMISSION);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("alerts")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					User user = Keaton.getUserManager().getUser(player.getUniqueId());
					user.setHasAlerts(user.isHasAlerts() ? false : true);
					String state = user.isHasAlerts() ? Color.Green + "true" : Color.Dark_Red + "false";
					player.sendMessage(Keaton.getAC().getMessage().ALERTS_TOGGLE.replaceAll("%state%", state));
				} else {
					sender.sendMessage(Color.Red
							+ "You must be a player to run this command. Do /Keaton help to see a list of commands a non-player can do.");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("bannable")) {
				if (args.length > 1) {
					if (sender.hasPermission("Keaton.admin")) {
						String checkName = args[1];
						Checks check = Keaton.getAC().getChecks().getCheckByName(checkName);
						if (check == null) {
							sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED + " Check ' " + checkName
									+ " ' not found.");
							ArrayList<String> list = new ArrayList<String>();
							for (Checks check1 : Keaton.getAC().getChecks().getDetections()) {
								list.add(Color.Gray + (check1.isBannable() ? Color.Green + check1.getName()
										: Color.Red + check1.getName()).toString() + Color.Gray);
							}
							sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED + " Bannable Status: "
									+ Color.Gray + list.toString());
							return true;
						}
						check.toggleBans();
						Keaton.getAC().getConfig().set("checks." + check.getName() + ".bannable", check.isBannable());
						Keaton.getAC().saveConfig();
						String state = String.valueOf(check.isBannable() ? Color.Green + check.isBannable()
								: Color.Dark_Red + check.isBannable());
						sender.sendMessage(Keaton.getAC().getMessage().SET_BANNABLE
								.replaceAll("%check%", check.getName()).replaceAll("%state%", state));
					} else {
						sender.sendMessage(Keaton.getAC().getMessage().NO_PERMISSION);
					}
				} else {
					sender.sendMessage(Color.Red + "Please specify a check to toggle bans for.");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("verbose")) {
				if (args.length > 1) {
					Player target = Bukkit.getPlayer(args[1]);

					if (target == null) {
						sender.sendMessage(Color.Red + "That player is not online!");
						return true;
					}
					sender.sendMessage(
							Color.Dark_Gray + Color.Strikethrough + "--------------------------------------------");
					User user = Keaton.getUserManager().getUser(target.getUniqueId());
					if (user.getVLs().size() > 0) {
						sender.sendMessage(Color.Gold + Color.Bold + target.getName() + "'s Violations/Info");
						sender.sendMessage("");
						sender.sendMessage(
								Color.Gray + "Ping: " + Color.White + Keaton.getAC().getPing().getPing(target));
						sender.sendMessage("");
						sender.sendMessage(Color.Red + Color.Bold + "Set off:");
						sender.sendMessage("");

						for (Checks check : user.getVLs().keySet()) {
							sender.sendMessage(Color.White + "- " + check.getName() + " VL: " + user.getVL(check));
						}

					} else {
						sender.sendMessage(Color.Red + "This player set off no checks!");
					}
					sender.sendMessage(
							Color.Dark_Gray + Color.Strikethrough + "--------------------------------------------");
				} else {
					sender.sendMessage(Color.Red + "Please specify a player to check violations for.");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(args.length > 1) {
					String checkName = args[1];
					if(sender.hasPermission("Keaton.admin")) {
						Checks check = Keaton.getAC().getChecks().getCheckByName(checkName);
						if (check == null) {
							sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED + " Check ' " + checkName + " ' not found.");
							ArrayList<String> list = new ArrayList<String>();
							for(Checks check1 : Keaton.getAC().getChecks().getDetections()) {
								list.add(Color.Gray + (check1.getState() ? Color.Green + check1.getName() : Color.Red + check1.getName()).toString() + Color.Gray);
							}
							sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED + " Available checks: " + Color.Gray + list.toString());
							return true;
						}
						check.toggle();
						Keaton.getAC().getConfig().set("checks." + check.getName() + ".enabled", check.getState());
						Keaton.getAC().saveConfig();
						String state = String.valueOf(check.getState() ? Color.Green + check.getState() : Color.Dark_Red + check.getState());
						sender.sendMessage(Keaton.getAC().getMessage().SET_TOGGLE.replaceAll("%check%", check.getName()).replaceAll("%state%", state));
					} else {
						sender.sendMessage(Keaton.getAC().getMessage().NO_PERMISSION);
					}
				} else {
					sender.sendMessage(Color.Red + "Please specify a check to toggle on/off.");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED
						+ "Use /Keaton toggle <CheckName> to enable/disable checks.");
				if(sender instanceof Player) {
					sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED
							+ "Use /Keaton Alerts on/off to enable/disable alerts.");
				}
				sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED
						+ "Use /Keaton bannable <CheckName> to make a check bannable/silent.");
				sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED
						+ "Use /Keaton status to check the current Keaton status.");
				sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED
						+ "Use /Keaton verbose <player> to see a player's violations.");
				sender.sendMessage(Keaton.getAC().getPrefix() + ChatColor.RED
						+ "Use /Keaton reload to reload the plugin.");
				return true;
			}
			sender.sendMessage(Color.Red + "Invalid argument(s). Do /Keaton help to see the list of commands you can do.");
		}
		return true;
	}

}
