package cc.funkemunky.ZedPvP.commands;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;

public class Rename implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Color.Red + "You must be a player to use this command.");
			return true;
		}
		Player p = (Player) sender;
		if (!p.hasPermission("zedpvp.rename") && !p.isOp()) {
			p.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}
		if (args.length < 1) {
			p.sendMessage(Core.getInstance().getPrefix() + Color.Red + "Invaild usage. Do /rename <name>.");
			return true;
		}
		if (!Core.getInstance().getCooldownManager().hasCommandCooldown(p, "/rename")) {
			ItemStack hand = new ItemStack(Material.NAME_TAG, 1);
			String name = "";
			for (int i = 0; i != args.length; i++)
				name += args[i] + " ";

			ItemMeta meta = hand.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r" + name));
			hand.setItemMeta(meta);
			p.updateInventory();

			p.sendMessage(Core.getInstance().getPrefix() + "Added Nametag to your inventory with the name '"
					+ hand.getItemMeta().getDisplayName() + Color.Gray + "'.");
			Core.getInstance().getCooldownManager().addNewCommandCooldown(p, "/rename", TimeUnit.MINUTES.toMillis(5));
		} else {
			p.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "Rename Cooldown: " + Color.Red + DurationFormatUtils.formatDurationWords(Core.getInstance().getCooldownManager().getCommandCooldown(p, "/rename").getTime(), true, true));
		}
		return true;
	}
}
