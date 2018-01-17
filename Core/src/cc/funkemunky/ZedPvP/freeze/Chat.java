package cc.funkemunky.ZedPvP.freeze;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Chat {
    public static String c(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String player(String message, Player player)
    {
        return c(message).replaceAll("<player>", player.getName());
    }

    public static String player(String message, OfflinePlayer player)
    {
        return c(message).replaceAll("<player>", player.getName());
    }

    public static String perm() {
        return Chat.c("&cNo permission.");
    }

    public static String disallow() {
        return Chat.c("zedpvp.notallowed");
    }

    public static void bc(String str) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Chat.c(str));
        }
    }

    public static void sendBroadcast(String perm, String str) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(perm)) {
                player.sendMessage(Chat.c(str));
            }
        }
    }
}
