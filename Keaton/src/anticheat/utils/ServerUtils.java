package anticheat.utils;

import org.bukkit.Bukkit;

public class ServerUtils {
	
	public static boolean isBukkitVerison(String version) {
		String bukkit = Bukkit.getServer().getClass().getPackage().getName().substring(23);
		
		return bukkit.contains(version);
	}

}
