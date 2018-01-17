package anticheat.checks.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.user.User;
import anticheat.utils.CancelType;
import anticheat.utils.Color;
import anticheat.utils.MathUtils;
import anticheat.utils.PlayerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class Fly extends Checks {

	public Map<UUID, Double> verbose;
	public Map<UUID, Double> glideVerbose;
	public Map<UUID, Double> velocity;
	private boolean isVelocity = true;

	public Fly() {
		super("Fly", ChecksType.MOVEMENT, Keaton.getAC(), 15, true, true);
		verbose = new HashMap<UUID, Double>();
		velocity = new ConcurrentHashMap<UUID, Double>();
		glideVerbose = new HashMap<UUID, Double>();
		
		isVelocity = Keaton.getAC().getConfig().getBoolean("checks.Fly.velocityLeniency");
	}

	@Override
	protected void onEvent(Event event) {
		if (!getState()) {
			return;
		}

		if (event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			Player p = e.getPlayer();
			UUID uuid = p.getUniqueId();

			if (verbose.containsKey(uuid)) {
				verbose.remove(uuid);
			}
			if (velocity.containsKey(uuid)) {
				velocity.remove(uuid);
			}
			if(glideVerbose.containsKey(uuid)) {
				glideVerbose.remove(uuid);
			}
		}

		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			Player player = e.getPlayer();
			if (player.getAllowFlight()) {
				return;
			}
			if (player.getVehicle() != null) {
				return;
			}
			
			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			if(MathUtils.elapsed(user.getLastBlockPlace()) < 2000L) {
				return;
			}
			
			double Speed = MathUtils.offset(MathUtils.getVerticalVector(e.getFrom()),
					MathUtils.getVerticalVector(e.getTo()));
			double glideVerbose = this.glideVerbose.getOrDefault(player.getUniqueId(), 0D);
			
			if(PlayerUtils.isGliding(player)) {
				return;
			}
			
			if((System.currentTimeMillis() - user.isVelocity()) < 2000L) {
				return;
			}
			if (isVelocity && player.getVelocity().length() < velocity.getOrDefault(player.getUniqueId(), -1.0D)) {
				return;
			}
			
			if(!PlayerUtils.hasBlocksNear(player) && !PlayerUtils.hasBlocksNear(player.getLocation().subtract(0.0D, 1.0D, 0.0D)) && PlayerUtils.isAir(player) 
					&& !PlayerUtils.hasBlocksNear(player.getLocation().add(0.0D, 1.0D, 0.0D)) && user.getAirTicks() > 30
					&& (e.getFrom().getY() - e.getTo().getY()) > 0 && Speed < 1.0D) {
				glideVerbose++;
			} else {
				glideVerbose= glideVerbose > -10 ? glideVerbose-- : -10;
			}
			
			if(glideVerbose > 17) {
				user.setVL(this, user.getVL(this) + 1);
				
				Alert(player, Color.Gray + "Reason: " + Color.White + "Fall Speed");
				user.setCancelled(this, CancelType.MOVEMENT);
				
				glideVerbose = 0;
			}
			if (!PlayerUtils.hasBlocksNear(player) && !PlayerUtils.hasBlocksNear(player.getLocation().subtract(0.0D, 1.0D, 0.0D)) && PlayerUtils.isAir(player) && user.getAirTicks() > 24 && Math.abs(e.getFrom().getY() - e.getTo().getY()) < 0.05
					&& user.getGroundTicks() == 0.0 && !player.hasPotionEffect(PotionEffectType.JUMP)) {
				
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT);
				if(user.getVL(this) > 2) {
					Alert(player, Color.Gray + "Reason: " + Color.White + "Hover");
				}
			}
			
			this.glideVerbose.put(player.getUniqueId(), glideVerbose);

			if (e.getFrom().getY() >= e.getTo().getY()) {
				return;
			}

			double TotalBlocks = this.verbose.getOrDefault(player.getUniqueId(), 0.0D);
			
			double OffsetY = MathUtils.offset(MathUtils.getVerticalVector(e.getFrom()),
					MathUtils.getVerticalVector(e.getTo()));
			if (OffsetY > 0.0D) {
				TotalBlocks += OffsetY;
			}
			double Limit = 2.0D;
			
			if(PlayerUtils.isOnGround(player.getLocation()) || PlayerUtils.isOnGround(player.getLocation(), -1.0D) || PlayerUtils.isInWater(player)
					|| MathUtils.elapsed(user.getLastBlockPlace()) <= 1000L) {
				TotalBlocks = 0;
			}
			
			if (player.hasPotionEffect(PotionEffectType.JUMP)) {
				for (PotionEffect effect : player.getActivePotionEffects()) {
					if (effect.getType().equals(PotionEffectType.JUMP)) {
						int level = effect.getAmplifier() + 1;
						Limit += (Math.pow(level + 4.2D, 2.0D) / 16.0D) + 0.3;
						break;
					}
				}
			}
			if (TotalBlocks > Limit) {
					if (velocity.containsKey(player.getUniqueId())) {
						user.setVL(this, user.getVL(this) + 1);
						
						Alert(player,Color.Gray +"Reason: " + Color.White + "Ascension " + Color.Gray + "Flew up " + Color.White + MathUtils.round(TotalBlocks, 2) + " blocks" + Color.Gray + ".");
						user.setCancelled(this, CancelType.MOVEMENT);
				}
			}
			verbose.put(player.getUniqueId(), TotalBlocks);
			if (!PlayerUtils.isOnGround(player.getLocation()) && !PlayerUtils.hasPotionEffect(player, PotionEffectType.POISON)) {
				velocity.put(player.getUniqueId(), player.getVelocity().length());
			} else {
				velocity.put(player.getUniqueId(), -1.0D);
			}
		}
	}
}