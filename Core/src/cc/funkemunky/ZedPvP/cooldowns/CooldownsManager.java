package cc.funkemunky.ZedPvP.cooldowns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cc.funkemunky.ZedPvP.Core;

public class CooldownsManager {
	
    private Map<Player, List<Cooldown>> cooldowns;
    private Map<Player, List<CommandCooldown>> commandCooldowns;
	
	public CooldownsManager() {
		cooldowns = new WeakHashMap<Player, List<Cooldown>>();
		commandCooldowns = new WeakHashMap<Player, List<CommandCooldown>>();
		
		runTask();
	}
	
	public Map<Player, List<Cooldown>> getCooldowns() {
		return cooldowns;
	}
	
	public Map<Player, List<CommandCooldown>> getCommandCooldowns() {
		return commandCooldowns;
	}
	
	public CommandCooldown getCommandCooldown(Player player, String string) {
		if(commandCooldowns.containsKey(player)) {
			for(CommandCooldown cmd : commandCooldowns.get(player)) {
				if(cmd.getCommand().equalsIgnoreCase(string)) {
					return cmd;
				}
			}
		}
		return null;
	}
	
	public Cooldown getCooldown(Player player, CooldownType type) {
		if(cooldowns.containsKey(player)) {
			for(Cooldown cmd : cooldowns.get(player)) {
				if(cmd.getType() == type) {
					return cmd;
				}
			}
		}
		return null;
	}
	
	public void addNewCommandCooldown(Player player, String command, long time) {
		if(commandCooldowns.containsKey(player)) {
			List<CommandCooldown> cooldowns = commandCooldowns.get(player);
			
			cooldowns.add(new CommandCooldown(player, command, time));
			commandCooldowns.put(player, cooldowns);
		} else {
			List<CommandCooldown> cooldowns = new ArrayList<CommandCooldown>();
			
			cooldowns.add(new CommandCooldown(player, command, time));
			commandCooldowns.put(player, cooldowns);
		}
	}
	
	public void addNewCooldown(Player player, CooldownType type, long time) {
		if(cooldowns.containsKey(player)) {
			List<Cooldown> cooldowns = this.cooldowns.get(player);
			
			cooldowns.add(new Cooldown(player, type, time));
			this.cooldowns.put(player, cooldowns);
		} else {
			List<Cooldown> cooldowns = new ArrayList<Cooldown>();
			
			cooldowns.add(new Cooldown(player, type, time));
			this.cooldowns.put(player, cooldowns);
		}
	}
	
	public boolean hasCommandCooldown(Player player, String command) {
		if(commandCooldowns.containsKey(player)) {
			for(CommandCooldown cooldown : commandCooldowns.get(player)) {
				if(cooldown.getCommand().contains(command)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasCooldown(Player player, CooldownType type) {
		if(cooldowns.containsKey(player)) {
			for(Cooldown cooldown : cooldowns.get(player)) {
				if(cooldown.getType() == type) {
					return true;
				}
			}
		}
		return false;
	}
	private void runTask() {
		new BukkitRunnable() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(commandCooldowns.containsKey(player)) {
						for(CommandCooldown cooldown : commandCooldowns.get(player)) {
							if(cooldown.getTime() > 0) {
								cooldown.setTime(cooldown.getTime() - 100);
							} else {
								if(commandCooldowns.get(player).size() > 1) {
									commandCooldowns.get(player).remove(cooldown);
								} else {
									commandCooldowns.remove(player);
								}
							}
						}
					}
					if(cooldowns.containsKey(player)) {
						for(Cooldown cooldown : cooldowns.get(player)) {
							if(cooldown.getTime() > 0) {
								cooldown.setTime(cooldown.getTime() - 100L);
							} else {
								if(cooldowns.get(player).size() > 1) {
									cooldowns.get(player).remove(cooldown);
								} else {
									cooldowns.remove(player);
								}
							}
						}
					}
				}
			}
		}.runTaskTimer(Core.getInstance(), 0L, 2L);
	}

}
