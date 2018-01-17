package cc.funkemunky.Scoreboard;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.Scoreboard.scoreboard.ScoreboardHandler;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Core extends JavaPlugin {
	
	private static Core instance;
	private Economy eco;
	private Permission perms;
	private ScoreboardHandler scoreboardHandler;
	
	 private boolean setupEconomy()
	    {
	        RegisteredServiceProvider<Economy> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (permissionProvider != null) {
	            eco = permissionProvider.getProvider();
	        }
	        return (eco != null);
	    }
	 private boolean setupPermissions()
	    {
	        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	        if (permissionProvider != null) {
	            perms = permissionProvider.getProvider();
	        }
	        return (perms != null);
	    }
	
	public void onEnable() {
		instance = this;
		setupEconomy();
		setupPermissions();
		
	    scoreboardHandler = new ScoreboardHandler();
	}
	
	public ScoreboardHandler getScoreboardHandler() {
		return scoreboardHandler;
	}
	public Economy getEconomy() {
		return eco;
	}
	
	public Permission getPermissions() {
		return perms;
	}
	
	public static Core getInstance() {
		return instance;
	}

}
