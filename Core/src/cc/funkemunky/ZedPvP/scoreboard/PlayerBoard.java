package cc.funkemunky.ZedPvP.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Iterables;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.scoreboard.provider.ScoreboardProvider;
import cc.funkemunky.ZedPvP.utils.Color;

public class PlayerBoard {

	private AtomicBoolean removed = new AtomicBoolean(false);
	private Team players;
	private BufferedObjective bufferedObjective;
	private Scoreboard scoreboard;
	private Player player;
	private SidebarProvider defaultProvider;
	private BukkitRunnable runnable;

	public PlayerBoard(Player player) {
		this.player = player;

		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.bufferedObjective = new BufferedObjective(scoreboard);
		defaultProvider = new ScoreboardProvider();

		this.players = scoreboard.registerNewTeam("players");
		this.players.setPrefix(Color.White);

		player.setScoreboard(scoreboard);
	}

	public void remove() {
		if (!this.removed.getAndSet(true) && scoreboard != null) {
			for (Team team : scoreboard.getTeams()) {
				team.unregister();
			}

			for (Objective objective : scoreboard.getObjectives()) {
				objective.unregister();
			}
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}

	public void setSidebarVisible() {
		this.bufferedObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	public void setDefaultSidebar(SidebarProvider provider) {
		if (provider != this.defaultProvider) {
			this.defaultProvider = provider;

			if (this.runnable != null) {
				this.runnable.cancel();
			}

			if (provider == null) {
				this.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
				return;
			}

			(this.runnable = new BukkitRunnable() {
				@Override
				public void run() {
					if (removed.get()) {
						cancel();
						return;
					}

					if (provider == defaultProvider) {
						updateObjective();
					}
				}
			}).runTaskTimerAsynchronously(Core.getInstance(), 2L, 2L);
		}
	}

	private void updateObjective() {
		if (this.removed.get()) {
			throw new IllegalStateException("Cannot update whilst board is removed");
		}

		SidebarProvider provider = this.defaultProvider;

		if (provider == null) {
			this.bufferedObjective.setVisible(false);
		}
		else {
			this.bufferedObjective.setTitle(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "ZedPvP");
			this.bufferedObjective.setAllLines(provider.getLines(player));
			this.bufferedObjective.flip();
		}
	}

	public void addUpdate(Player target) {
		this.addUpdates(Collections.singleton(target));
	}

	public void addUpdates(Iterable<? extends Player> updates) {
		if (Iterables.size(updates) == 0) {
			return;
		}

		if (this.removed.get()) {
			throw new IllegalStateException("Cannot update whilst board is removed.");
		}


		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player update : updates) {
					if (update == null || !update.isOnline()) {
						continue;
					}

					List<Team> removeFrom = new ArrayList<>();

					for (Team team : scoreboard.getTeams()) {
						if (team.hasPlayer(update)) {
							removeFrom.add(team);
						}
					}

					for (Team team : removeFrom) {
						team.removePlayer(update);
					}

					if (player.equals(update)) {
						if (!players.hasPlayer(update)) {
							players.addPlayer(update);
						}
					}

				}
			}
		}.runTask(Core.getInstance());
	}

}