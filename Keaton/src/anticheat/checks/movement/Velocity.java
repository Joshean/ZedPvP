package anticheat.checks.movement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerVelocityEvent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.packets.events.PacketEvent;
import anticheat.packets.events.PacketTypes;
import anticheat.user.User;
import anticheat.utils.Color;
import anticheat.utils.MathUtils;
import anticheat.utils.PlayerUtils;

@ChecksListener(events = { PlayerVelocityEvent.class, PacketEvent.class })
public class Velocity extends Checks {

	private Set<UUID> playersTakingVelocity;
	private Multimap<UUID, Double> playerOffsetMultiMap;
	private Map<UUID, Double> playerVelocityY;

	public Velocity() {
		super("Velocity", ChecksType.MOVEMENT, Keaton.getAC(), 10, true, true);

		playersTakingVelocity = new HashSet<UUID>();
		playerVelocityY = new HashMap<UUID, Double>();
		playerOffsetMultiMap = ArrayListMultimap.create();
	}

	@Override
	protected void onEvent(Event event) {
		if (!getState()) {
			return;
		}

		if (event instanceof PlayerVelocityEvent) {
			PlayerVelocityEvent e = (PlayerVelocityEvent) event;

			Player player = e.getPlayer();
			double velocityY = e.getVelocity().getY();
			if (velocityY < 0.02) {
				return;
			}
			if (player.getHealth() <= 0.0) {
				return;
			}
			if (this.playersTakingVelocity.contains(player.getUniqueId())) {
				return;
			}
			if (PlayerUtils.isInWater(player)) {
				return;
			}
			if (PlayerUtils.isInWeb(player)) {
				return;
			}
			if (player.getLocation().clone().add(0.0D, 2.0D, 0.0D).getBlock().getType().isSolid()) {
				return;
			}
			int playerPing = Keaton.getAC().getPing().getPing(player);
			if (playerPing < 0) {
				return;
			}
			this.playersTakingVelocity.add(player.getUniqueId());
		}

		if (event instanceof PlayerVelocityEvent) {
			PlayerVelocityEvent e = (PlayerVelocityEvent) event;
			Player player = e.getPlayer();
			double velocityY = e.getVelocity().getY();
			if (velocityY < 0.02) {
				return;
			}
			if (player.getHealth() <= 0.0) {
				return;
			}
			if (this.playerVelocityY.containsKey(player.getUniqueId())) {
				return;
			}
			if (PlayerUtils.isInWater(player)) {
				return;
			}
			if (PlayerUtils.isInWeb(player)) {
				return;
			}
			if (player.getLocation().clone().add(0.0D, 2.0D, 0.0D).getBlock().getType().isSolid()) {
				return;
			}

			this.playerVelocityY.put(player.getUniqueId(), velocityY);

		}

		if (event instanceof PacketEvent) {
			PacketEvent e = (PacketEvent) event;
			if (e.getType() != PacketTypes.POSITION) {
				return;
			}
			Player player = e.getPlayer();
			
			if(e.getFrom() == null || e.getTo() == null) {
				return;
			}
			if (e.getFrom().getY() >= e.getTo().getY()) {
				return;
			}
			User playerStats = Keaton.getUserManager().getUser(player.getUniqueId());
			Location from = e.getFrom();
			Location to = e.getTo();

			if (this.playerVelocityY.containsKey(player.getUniqueId())) {
				double offsetY = to.getY() - from.getY();
				if (MathUtils.frac(from.getY()) == 0.0 && MathUtils.frac(to.getY()) > 0.0
						&& offsetY < 0.41999998688697815) {
					double velocityY = this.playerVelocityY.get(player.getUniqueId());
					this.playerVelocityY.remove(player.getUniqueId());
					double ratio = offsetY / velocityY;
					double vl = playerStats.getVL(this);
					if (ratio <= 0.98) {
						playerStats.setVL(this, playerStats.getVL(this) + 1);
						if ((vl + 1.0) >= 5.0) {
							int ping = Keaton.getAC().getPing().getPing(player);
							
							Alert(player, Color.Gray + "Reason: " + Color.White + "Modified" + Color.Gray + " Velocity: "
									+ Color.White + (ratio * 100) + "%" + Color.Gray + " Ping: " + Color.White + ping);
						}
					} else {
						playerStats.setVL(this, playerStats.getVL(this) > 0 ? playerStats.getVL(this) - 1 : 0);
					}
				}
			}
		}

		if (event instanceof PacketEvent) {
			PacketEvent e = (PacketEvent) event;
			if (e.getType() != PacketTypes.POSITION) {
				return;
			}

			if (e.getFrom() != null && e.getTo() != null) {
				if (e.getFrom().getY() > e.getTo().getY()) {
					return;
				}
			}
			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			if (this.playersTakingVelocity.contains(player.getUniqueId())) {
				if (user.getPacketsFromLag() > 0) {
					this.playersTakingVelocity.remove(player.getUniqueId());
					this.playerOffsetMultiMap.removeAll(player.getUniqueId());
					return;
				}
				int ping = Keaton.getAC().getPing().getPing(player);
				long average = user.getMovePacketAverage();
				int ticks = this.getFormula(ping, average) * 2;
				double offsetY = e.getTo().getY() - e.getFrom().getY();
				this.playerOffsetMultiMap.put(player.getUniqueId(), offsetY);
				if (this.playerOffsetMultiMap.get(player.getUniqueId()).size() == 5 + ticks) {
					double totalOffset = 0.0;
					Iterator<Double> iterator = this.playerOffsetMultiMap.get(player.getUniqueId()).iterator();
					while (iterator.hasNext()) {
						double offset = iterator.next();
						totalOffset += offset;
					}
					this.playerOffsetMultiMap.removeAll(player.getUniqueId());
					this.playersTakingVelocity.remove(player.getUniqueId());
					if (totalOffset < 0.01) {
						user.setVL(this, user.getVL(this) + 1);

						if ((user.getVL(this) + 1.0) >= 5.0) {
							Alert(player, Color.Gray + "Reason: " + Color.White + "NoVelocity");
						}
					}
				}
			}
		}
	}

	private int getFormula(int ping, long average) {
		int formula = (int) Math.round((double) ((long) (ping / 2) + average / 2) / 50.0);
		if (formula == 0) {
			formula = 1;
		}
		return formula;
	}
}