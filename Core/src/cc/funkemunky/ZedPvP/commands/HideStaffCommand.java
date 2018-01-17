package cc.funkemunky.ZedPvP.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;

public class HideStaffCommand implements CommandExecutor {
	
	private List<Player> hasStaffHidden;
	
	public HideStaffCommand() {
		hasStaffHidden = new ArrayList<Player>();
		
		new BukkitRunnable() {
			public void run() {
				for(Player player : hasStaffHidden) {
					for(Player online : Bukkit.getOnlinePlayers()) {
						if(online.hasPermission("zedpvp.staff")) {
							if(Core.getInstance().getStaffMode().isInStaffMode(online)) {
								player.hidePlayer(online);
							} else if(!player.canSee(online)) {
								player.showPlayer(online);
							}
						}
					}
				}
			}
		}.runTaskTimer(Core.getInstance(), 0L, 80L);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((!sender.hasPermission("zedpvp.staff") && !sender.hasPermission("zedpvp.hidestaff") && !sender.isOp()) || !(sender instanceof Player)) {
			sender.sendMessage(Color.Red + "No permission.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(!hasStaffHidden.contains(player)) {
			for(UUID uuid : Core.getInstance().getStaffMode().isStaffMode) {
				player.hidePlayer(Bukkit.getPlayer(uuid));
			}
			hasStaffHidden.add(player);
			player.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "Removed all staff members in staffmode from your view.");
		} else {
			for(Player staff : Bukkit.getOnlinePlayers()) {
				if(staff.hasPermission("zedpvp.staff")) {
					if(!player.canSee(staff)) {
						player.showPlayer(staff);	
					}
				}
			}
			hasStaffHidden.remove(player);
			player.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "You can now see all staff members.");
		}
		return true;
	}

}
