package anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.user.User;

@ChecksListener(events = { PlayerQuitEvent.class, EntityRegainHealthEvent.class })
public class Regen extends Checks {

	public Map<UUID, Integer> verbose;

	public Regen() {
		super("Regen", ChecksType.COMBAT, Keaton.getAC(), 9, true, true);

		verbose = new HashMap<UUID, Integer>();
	}

	@Override
	protected void onEvent(Event event) {
		if (!this.getState()) {
			return;
		}

		if (event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;

			Player player = e.getPlayer();
			if (this.verbose.containsKey(player.getUniqueId())) {
				this.verbose.remove(player.getUniqueId());
			}
		}

		if (event instanceof EntityRegainHealthEvent) {
			EntityRegainHealthEvent e = (EntityRegainHealthEvent) event;
			if (!e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
				return;
			}
			if (!(e.getEntity() instanceof Player)) {
				return;
			}
			if (Keaton.getAC().getPing().getTPS() < 17) {
				return;
			}
			Player player = (Player) e.getEntity();
			
			if (player.getWorld().getDifficulty().equals(Difficulty.PEACEFUL)) {
				return;
			}
			
			if(player.getFoodLevel() < 20) {
				return;
			}
			
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			if((System.currentTimeMillis() - user.getLastPotionSplash()) < 215L) {
				return;
			}
			int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
			
			if (user.getLastHeal() != 0) {
				long l = user.getLastHeal();
				if (System.currentTimeMillis() - l < 2800L) {
					verbose++;
					if(Keaton.getAC().getConfig().getBoolean("checks." + getName() + ".cancelled")) {
						e.setCancelled(true);
					}
				}
			}
			if (verbose > 4) {
				if (user.getPosPackets() < 2) {
					user.setVL(this, user.getVL(this) == 0 ? 1 : user.getVL(this) + 1);
					if(user.getVL(this) > 2) {
						Alert(player, "*");
					}
					if(Keaton.getAC().getConfig().getBoolean("checks." + getName() + ".cancelled")) {
						e.setCancelled(true);
					}
				}
				verbose = 0;
			}
			this.verbose.put(player.getUniqueId(), verbose);
		}
	}

}