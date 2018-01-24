package cc.funkemunky.ZedPvP;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.ZedPvP.cooldowns.CooldownsManager;
import cc.funkemunky.ZedPvP.economy.RubiesGUI;
import cc.funkemunky.ZedPvP.economy.RubiesManager;
import cc.funkemunky.ZedPvP.events.EventManager;
import cc.funkemunky.ZedPvP.events.koth.KothManager;
import cc.funkemunky.ZedPvP.listeners.FreezeListener;
import cc.funkemunky.ZedPvP.listeners.StaffModeListener;
import cc.funkemunky.ZedPvP.scoreboard.ScoreboardHandler;
import cc.funkemunky.ZedPvP.utils.Loadup;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import subside.plugins.koth.KothPlugin;

public class Core extends JavaPlugin {
	
    private static Core instance; 
	private Economy eco;
	private Permission perms;
    private CooldownsManager cooldownManager;
    private ScoreboardHandler scoreboardHandler;
    private KothPlugin koth;
    private FreezeListener freezeListener;
    private List<UUID> frozen;
    private List<UUID> invLock;
    private EventManager eventManager;
    public List<Player> toTeleportTo;
    private StaffModeListener staffMode;
    private RubiesGUI rubyGui;
    private RubiesManager rubyManager;
    private KothManager kothManager;
    
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
		cooldownManager = new CooldownsManager();
		freezeListener = new FreezeListener();
		scoreboardHandler = new ScoreboardHandler();
		staffMode = new StaffModeListener();
		eventManager = new EventManager();
		rubyGui = new RubiesGUI();
		rubyManager = new RubiesManager();
		kothManager = new KothManager();
		
		frozen = new ArrayList<UUID>();
		invLock = new ArrayList<UUID>();
		toTeleportTo = new ArrayList<Player>();
		
		koth = (KothPlugin) Bukkit.getPluginManager().getPlugin("KoTH");
		
		setupEconomy();
		setupPermissions();
		
		new Loadup();
	}

	public KothManager getKothManager() {
		return kothManager;
	}
	
	public RubiesManager getRubyManager() {
		return rubyManager;
	}
	
	public RubiesGUI getRubyGUI() {
		return rubyGui;
	}
	public EventManager getEventManager() {
		return eventManager;
	}
	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}
	public KothPlugin getKoth() {
		return koth;
	}
	
	public StaffModeListener getStaffMode() {
		return staffMode;
	}
	
	public void addToTeleport() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(!toTeleportTo.contains(player) && !player.hasPermission("zedpvp.staff")) {
				toTeleportTo.add(player);
			}
		}
		for(Player player : toTeleportTo) {
			if(player == null) {
				toTeleportTo.remove(player);
			}
		}
	}
	
	public List<UUID> getFrozen() {
		return frozen;
	}
	
	public List<UUID> getInvLock() {
		return invLock;
	}
	
	public FreezeListener getFreezeListener() {
		return freezeListener;
	}
	
	public static Core getInstance() {
		return instance;
	}
	
	public CooldownsManager getCooldownManager() {
		return cooldownManager;
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

	public String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', "&6&lZedPvP &8> &7");
	}
	

}
