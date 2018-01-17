package cc.funkemunky.ZedPvP.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;

public class TestCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.getName().equals("funkemunky")) {
			return false;
		}
		for(Hologram hologram : HologramsAPI.getHolograms(Core.getInstance())) {
			hologram.delete();
		}
		sender.sendMessage(Color.Green + "Removed all holograms!");
		return true;
	}

}
