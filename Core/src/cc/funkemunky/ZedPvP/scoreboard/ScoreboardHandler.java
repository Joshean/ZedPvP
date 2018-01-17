package cc.funkemunky.ZedPvP.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.scoreboard.provider.ScoreboardProvider;

public class ScoreboardHandler implements Listener {

    private Map<UUID, PlayerBoard> playerBoards = new HashMap<>();
	private final ScoreboardProvider timerSidebarProvider;

	public ScoreboardHandler() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		this.timerSidebarProvider = new ScoreboardProvider();

		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

		for (Player player : players) {
			this.applyBoard(player).addUpdates(players);
		}

	}

	public PlayerBoard getPlayerBoard(UUID uuid) {
		return this.playerBoards.get(uuid);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Core.getInstance().getScoreboardHandler().applyBoard(event.getPlayer());

        	Core.getInstance().getScoreboardHandler().playerBoards.get(event.getPlayer().getUniqueId()).addUpdate(event.getPlayer());
            new BukkitRunnable() {
                public void run() {
                	Core.getInstance().getScoreboardHandler().playerBoards.get(event.getPlayer().getUniqueId()).addUpdates(Bukkit.getOnlinePlayers());
                }
            }.runTaskLater(Core.getInstance(), 4L);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.playerBoards.remove(event.getPlayer().getUniqueId()).remove();
	}

	public PlayerBoard applyBoard(Player player) {
		PlayerBoard board = new PlayerBoard(player);
		PlayerBoard previous = this.playerBoards.put(player.getUniqueId(), board);

		if (previous != null && previous != board) {
			previous.remove();
		}

		board.setSidebarVisible();
		board.setDefaultSidebar(this.timerSidebarProvider);

		return board;
	}

}