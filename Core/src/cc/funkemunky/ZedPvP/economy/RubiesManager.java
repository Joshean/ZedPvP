package cc.funkemunky.ZedPvP.economy;

import org.bukkit.entity.Player;

import cc.funkemunky.ZedPvP.Core;

public class RubiesManager {
	
	public double getRubies(Player player) {
		if(hasRubiesInConfig(player)) {
			return Core.getInstance().getConfig().getInt("Players." + player.getUniqueId() + ".Rubies");
		}
		Core.getInstance().getConfig().set("Players." + player.getUniqueId() + ".Rubies", 0);
		Core.getInstance().saveConfig();
		return 0.0D;
	}
	
	public void setRubies(Player player, int amount) {
		if(hasRubiesInConfig(player)) {
			Core.getInstance().getConfig().set("Players." + player.getUniqueId() + ".Rubies", amount);
			Core.getInstance().saveConfig();
		} else {
			Core.getInstance().getConfig().set("Players." + player.getUniqueId() + ".Rubies", 0);
			Core.getInstance().saveConfig();
		}
	}
	
	public void addRubies(Player player, int amount) {
		if(hasRubiesInConfig(player)) {
			Core.getInstance().getConfig().set("Players." + player.getUniqueId() + ".Rubies", getRubies(player) + amount);
			Core.getInstance().saveConfig();
		} else {
			Core.getInstance().getConfig().set("Players." + player.getUniqueId() + ".Rubies", 0);
			Core.getInstance().saveConfig();
		}
	}
	
	public void subtractRubies(Player player, int amount) {
		if(hasRubiesInConfig(player)) {
			Core.getInstance().getConfig().set("Players." + player.getUniqueId() + ".Rubies", getRubies(player) - amount);
			Core.getInstance().saveConfig();
		} else {
			Core.getInstance().getConfig().set("Players." + player.getUniqueId() + ".Rubies", 0);
			Core.getInstance().saveConfig();
		}
	}
	
	public boolean hasRubiesInConfig(Player player) {
		return Core.getInstance().getConfig().get("Players." + player.getUniqueId() + ".Rubies") != null;
	}

}
