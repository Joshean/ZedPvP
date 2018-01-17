package anticheat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import anticheat.commands.KeatonCommand;
import anticheat.commands.ViewLogCommand;
import anticheat.detections.Checks;
import anticheat.detections.ChecksManager;
import anticheat.events.EventInventory;
import anticheat.events.EventJoinQuit;
import anticheat.events.EventPacket;
import anticheat.events.EventPacketMoveEvent;
import anticheat.events.EventPacketUse;
import anticheat.events.EventPlayerAttack;
import anticheat.events.EventPlayerInteractEvent;
import anticheat.events.EventPlayerMove;
import anticheat.events.EventPlayerRespawn;
import anticheat.events.EventPlayerVelocity;
import anticheat.events.EventProjectileLaunch;
import anticheat.events.EventTick;
import anticheat.events.TickEvent;
import anticheat.events.TickType;
import anticheat.gui.GUI;
import anticheat.gui.GUIListener;
import anticheat.packets.PacketListeners;
import anticheat.user.User;
import anticheat.user.UserManager;
import anticheat.utils.Color;
import anticheat.utils.Messages;
import anticheat.utils.MySQL;
import anticheat.utils.Ping;
import anticheat.utils.PlayerUtils;
import anticheat.utils.TxtFile;

public class Keaton extends JavaPlugin {

	private static ChecksManager checksmanager;
	private static Keaton Keaton;
	public PacketListeners packet;
	private static UserManager userManager;
	private Ping ping;
	BufferedWriter bw = null;
	public static String hwid;
	private Messages msgs;
	private FileConfiguration messages = null;
	private GUI guiManager;
	private File messagesFile = null;
	private MySQL mySQL;
	public ArrayList<Player> playersBanned = new ArrayList<Player>();
	File file = new File(getDataFolder(), "JD.txt");

	public Ping getPing() {
		return this.ping;
	}

	public static Keaton getAC() {
		return Keaton;
	}

	public ChecksManager getChecks() {
		return checksmanager;
	}

	public static UserManager getUserManager() {
		return userManager;
	}

	public String getPrefix() {
		return Color.translate(getMessages().getString("Prefix"));
	}
	
	public GUI getGUIManager() {
		return guiManager;
	}

	public void onEnable() {
		getServer().getConsoleSender()
				.sendMessage(Color.translate("&d------------------------------------------"));
		Keaton = this;
		userManager = new UserManager();
		ping = new Ping(this);
		getServer().getConsoleSender().sendMessage(Color.translate("&d Keaton &f Loaded Main class!"));
		
		checksmanager = new ChecksManager(this);
		getServer().getConsoleSender().sendMessage(Color.translate("&d Keaton &f Loaded checks!"));
		
		getCommand("Keaton").setExecutor(new KeatonCommand());
		getCommand("viewlog").setExecutor(new ViewLogCommand());
		getServer().getConsoleSender().sendMessage(Color.translate("&d Keaton &f Loaded commands!"));
		
		registerConfig();
		registerBungee();
		getServer().getConsoleSender().sendMessage(Color.translate("&d Keaton &f Loaded Configuration!"));
		
		hwid = getConfig().getString("hwid");
		getServer().getConsoleSender().sendMessage(Color.translate("&d Keaton &f Loaded players data's!"));
		
		checksmanager.init();

		registerEvents();
		packet = new PacketListeners();
		guiManager = new GUI();
		getServer().getConsoleSender().sendMessage(Color.translate("&d Keaton &f Registered events!"));

		mkdirs();
		loadChecks();
		loadUsers();
		registerUtils();
		if(getConfig().getBoolean("MySQL.Enabled")) {
			mySQL = new MySQL(getConfig().getString("MySQL.IP"), getConfig().getString("MySQL.Username"),
					getConfig().getString("MySQL.Password"), getConfig().getString("MySQL.Name"));
			getServer().getConsoleSender().sendMessage(Color.translate("&dMySQL enabled!"));
		} else {
			getServer().getConsoleSender().sendMessage(Color.translate("&dMySQL &cdisabled&d, using FlatFile system."));
		}
		getServer().getConsoleSender().sendMessage(Color.translate("&d Keaton &f Loaded Keaton!"));
		getServer().getConsoleSender()
				.sendMessage(Color.translate("&d------------------------------------------"));

	}
	
	public void registerUtils() {
		new PlayerUtils();
		msgs = new Messages(this);
	}
	
	public void reloadMessages() {
	    if (messagesFile == null) {
	    messagesFile = new File(getDataFolder(), "messages.yml");
	    }
	    messages = YamlConfiguration.loadConfiguration(messagesFile);

	    // Look for defaults in the jar
	    try {
	        Reader defConfigStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
		    if (defConfigStream != null) {
		        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		        messages.setDefaults(defConfig);
		    }
	    } catch(Exception e) {
	    	    e.printStackTrace();
	    }
	}
	
	public MySQL getMySQL() {
		return mySQL;
	}
	
	public FileConfiguration getMessages() {
	    if (messages == null) {
	        reloadMessages();
	    }
	    return messages;
	}
	
	public void saveMessages() {
	    if (messages == null || messagesFile == null) {
	        return;
	    }
	    try {
	        getMessages().save(messagesFile);
	    } catch (IOException ex) {
	        getLogger().log(Level.SEVERE, "Could not save config to " + messagesFile, ex);
	    }
	}
	
	public void createMessages() {
	    if (messagesFile == null) {
	        messagesFile = new File(getDataFolder(), "messages.yml");
	    }
	    if (!messagesFile.exists()) {            
	         saveResource("messages.yml", false);
	     }
	}
	
	public void loadChecks() {
		for (Checks check : getChecks().getDetections()) {
			if (getConfig().get("checks." + check.getName() + ".enabled") != null
					|| getConfig().get("checks." + check.getName() + ".bannable") != null) {
				check.setState(getConfig().getBoolean("checks." + check.getName() + ".enabled"));
				check.setBannable(getConfig().getBoolean("checks." + check.getName() + ".bannable"));
			} else {
				getConfig().set("checks." + check.getName() + ".enabled", check.getState());
				getConfig().set("checks." + check.getName() + ".bannable", check.isBannable());
				if(!check.getName().equalsIgnoreCase("Phase")) {
					getConfig().set("checks." + check.getName() + ".cancelled", false);
				} else {
					getConfig().set("checks." + check.getName() + ".cancelled", true);
				}
				saveConfig();
			}
		}
	}
	
	public void mkdirs() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
			getServer().getConsoleSender().sendMessage(Color.translate("&d Keaton &f Made Keaton file!"));
		}
	}
	
	public void registerBungee() {
		if(getConfig().getBoolean("bungee")) {
			  this.getServer().getMessenger().registerOutgoingPluginChannel(this, "KeatonBans");
			  this.getServer().getMessenger().registerOutgoingPluginChannel(this, "KeatonAlerts");
		}
	}
	
	public void registerConfig() {
		saveDefaultConfig();
		createMessages();
	}
	
	public void writeInLog(User user) {
		if(user.getList() != null) {
			if(user.getList().size() > 0) {
				TxtFile logFile = new TxtFile(this, File.separator + "logs", user.getPlayer().getName());
				for(String string : user.getList()) {
					logFile.addLine(string);
				}
				user.clearList();
				logFile.write();
			}
		}
	}
	
	public void loadUsers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			getUserManager().add(new User(player));
		}
	}
	
	public void removeUsers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(getUserManager().getUser(player.getUniqueId()) != null) {
				getUserManager().remove(getUserManager().getUser(player.getUniqueId()));
			}
		}
	}

	public void clearVLS() {
		for (Player online : Bukkit.getOnlinePlayers()) {
			getUserManager().getUser(online.getUniqueId()).getVLs().clear();
		}
	}
	
	public Messages getMessage() {
		return msgs;
	}

	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventPlayerMove(), this);
		pm.registerEvents(new EventPlayerAttack(), this);
		pm.registerEvents(new EventTick(), this);
		pm.registerEvents(new EventJoinQuit(), this);
		pm.registerEvents(new EventPlayerVelocity(), this);
		pm.registerEvents(new EventPlayerInteractEvent(), this);
		pm.registerEvents(new EventPacketUse(), this);
		pm.registerEvents(new EventPacket(), this);
		pm.registerEvents(new EventPlayerRespawn(), this);
		pm.registerEvents(new EventProjectileLaunch(), this);
		pm.registerEvents(new EventInventory(), this);
		pm.registerEvents(new EventPacketMoveEvent(), this);
		pm.registerEvents(new GUIListener(), this);

		new BukkitRunnable() {
			public void run() {
				clearVLS();
				Bukkit.broadcast(getPrefix() + Color.Green + "Reset violations for all players.", "keaton.admin");
			}
		}.runTaskTimerAsynchronously(this, 0L, 24000L);

		new BukkitRunnable() {
			public void run() {
				getServer().getPluginManager().callEvent(new TickEvent(TickType.FASTEST));

			}
		}.runTaskTimer(this, 0L, 1L);
		new BukkitRunnable() {
			public void run() {
				getServer().getPluginManager().callEvent(new TickEvent(TickType.FAST));

				for (Player player : Bukkit.getOnlinePlayers()) {
					for (Checks check : getChecks().getDetections()) {
						if(player != null && check != null) {
							check.kick(player);
						}
					}
				}
			}
		}.runTaskTimer(this, 0L, 5L);
		new BukkitRunnable() {
			public void run() {
				getServer().getPluginManager().callEvent(new TickEvent(TickType.SECOND));
			}
		}.runTaskTimer(this, 0L, 20L);
		new BukkitRunnable() {
			public void run() {
				if(getConfig().getBoolean("logs.enabled")) {
					getServer().getConsoleSender().sendMessage(getMessage().LOG_SAVING);
					if(getConfig().getBoolean("logs.broadcast")) Bukkit.broadcast(getMessage().LOG_SAVING, "Keaton.admin");
					if(getUserManager().allUsers.size() > 0) {
						for(User user : getUserManager().allUsers) {
							writeInLog(user);
						}
					}
					getServer().getConsoleSender().sendMessage(getMessage().LOG_SAVED);
					if(getConfig().getBoolean("logs.broadcast")) Bukkit.broadcast(getMessage().LOG_SAVED, "Keaton.admin");
				}
			}
		}.runTaskTimerAsynchronously(this, 0L, 20 * 60 * getConfig().getLong("logs.interval"));
	}
}