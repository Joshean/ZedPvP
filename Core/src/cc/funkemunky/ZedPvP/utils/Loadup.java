package cc.funkemunky.ZedPvP.utils;

import com.comphenix.protocol.ProtocolLibrary;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.commands.FreezeCommand;
import cc.funkemunky.ZedPvP.commands.HideStaffCommand;
import cc.funkemunky.ZedPvP.commands.RawcastCommand;
import cc.funkemunky.ZedPvP.commands.Rename;
import cc.funkemunky.ZedPvP.commands.StaffmodeCommand;
import cc.funkemunky.ZedPvP.commands.TestCommand;
import cc.funkemunky.ZedPvP.listeners.CommandBlockerListener;
import cc.funkemunky.ZedPvP.listeners.ConsumeListener;
import cc.funkemunky.ZedPvP.listeners.ElevatorListener;
import cc.funkemunky.ZedPvP.listeners.EnderpearlListener;
import cc.funkemunky.ZedPvP.listeners.FreezeListener;
import cc.funkemunky.ZedPvP.listeners.StaffModeListener;
import cc.funkemunky.ZedPvP.tasks.Announcer;

public class Loadup {
	
	public Loadup() {
		registerCommands();
		registerTasks();
		registerListeners();
		Core.getInstance().saveDefaultConfig();
	}
	
	public void registerCommands() {
		Core.getInstance().getCommand("rename").setExecutor(new Rename());
		Core.getInstance().getCommand("rawcast").setExecutor(new RawcastCommand());
		Core.getInstance().getCommand("funkemunky").setExecutor(new TestCommand());
		Core.getInstance().getCommand("freeze").setExecutor(new FreezeCommand());
		Core.getInstance().getCommand("staffmode").setExecutor(new StaffmodeCommand());
		Core.getInstance().getCommand("hidestaff").setExecutor(new HideStaffCommand());
	}
	
	public void registerTasks() {
		new Announcer();
	}
	
	public void registerListeners() {
		//Bukkit
		Core.getInstance().getServer().getPluginManager().registerEvents(new CommandBlockerListener(Core.getInstance()), Core.getInstance());
		Core.getInstance().getServer().getPluginManager().registerEvents(new ConsumeListener(), Core.getInstance());
		Core.getInstance().getServer().getPluginManager().registerEvents(new EnderpearlListener(), Core.getInstance());
		Core.getInstance().getServer().getPluginManager().registerEvents(new ElevatorListener(), Core.getInstance());
		Core.getInstance().getServer().getPluginManager().registerEvents(new FreezeListener(), Core.getInstance());
		Core.getInstance().getServer().getPluginManager().registerEvents(new StaffModeListener(), Core.getInstance());
		//ProtocolLib
		ProtocolLibrary.getProtocolManager().addPacketListener(new CommandBlockerListener(Core.getInstance()));
	}

}
