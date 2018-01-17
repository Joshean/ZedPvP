package cc.funkemunky.ZedPvP.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.funkemunky.ZedPvP.freeze.Chat;
import cc.funkemunky.ZedPvP.freeze.FreezeManager;
import cc.funkemunky.ZedPvP.freeze.GUIManager;
import cc.funkemunky.ZedPvP.utils.Color;

public class FreezeCommand implements CommandExecutor {
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.Red + "Only players are allowed to use this.");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("zedpvp.staff")) {
            player.sendMessage(Chat.perm());
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Chat.c("&cUsage: /freeze <player>"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Chat.c("&cThat player is not online."));
            return false;
        }

        if (target == player) {
            player.sendMessage(Chat.c("&cYou cannot freeze yourself!"));
            return false;
        }

        if (FreezeManager.isFrozen(target.getUniqueId())) {
            if (GUIManager.isLocked(target)) {
                GUIManager.removeLock(target);

                player.sendMessage(Chat.c("&cRemoved inventory lock from " + target.getName() + "."));
                return false;
            }

            FreezeManager.unfreeze(target.getUniqueId());
            GUIManager.removeLock(target);
            player.sendMessage(Chat.c("&aSuccessfully unfroze " + target.getName() + "."));
            Bukkit.broadcast(Chat.c("&7[" + player.getName() + " has unfrozen " + target.getName() + "]"), "zedpvp.staff");
            target.sendMessage(Chat.c("&aYou have been unfrozen. Sorry for any inconvenience caused!"));
            return false;
        } else {
            FreezeManager.freeze(target.getUniqueId());
            GUIManager.addLock(target);
            target.sendMessage(" ");
            target.sendMessage("&f\u2588\u2588\u2588\u2588&c\u2588&f\u2588\u2588\u2588\u2588");
            target.sendMessage("&f\u2588\u2588\u2588&c\u2588&6\u2588&c\u2588&f\u2588\u2588\u2588");
            target.sendMessage("&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588 &4&lATTENTION");
            target.sendMessage("&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588 &eYou have been frozen");
            target.sendMessage("&f\u2588&c\u2588&6\u2588\u2588&0\u2588&6\u2588\u2588&c\u2588&f\u2588 &fRead the instructions in the GUI!");
            target.sendMessage("&f\u2588&c\u2588&6\u2588\u2588\u2588\u2588\u2588&c\u2588&f\u2588 " + "http://teamspeak.com");
            target.sendMessage("&c\u2588&6\u2588\u2588\u2588&0\u2588&6\u2588\u2588\u2588&c\u2588");
            target.sendMessage("&c\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            target.sendMessage(" ");
            player.sendMessage(Chat.c("&aSuccesfully froze " + target.getName() + "."));
            Bukkit.broadcast(Chat.c("&7[" + player.getName() + " has frozen " + target.getName() + "]"), "hcf.command.freeze");
        }
        return false;
    }
}