package anticheat.checks.movement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
import anticheat.utils.BlockUtils;
import anticheat.utils.CancelType;
import anticheat.utils.Color;
import anticheat.utils.MathUtils;
import anticheat.utils.PlayerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class Speed extends Checks {

	private Map<UUID, Integer> verbose;
	public Location location;
	private boolean normalMovements = true;
	private boolean limit = true;
	private List<Double> normalValues;
	

	public Speed() {
		super("Speed", ChecksType.MOVEMENT, Keaton.getAC(), 15, true, true);
		
		verbose = new HashMap<UUID, Integer>();
		normalValues = new ArrayList<Double>();
		
		normalMovements = Keaton.getAC().getConfig().getBoolean("checks.Speed.NormalMovements");
		limit = Keaton.getAC().getConfig().getBoolean("checks.Speed.Limit");
		
		normalValues.add(0.41999998688697815);
		normalValues.add(0.33319999363422426);
		normalValues.add(0.1568672884460831);
		normalValues.add(0.4044491418477924);
		normalValues.add(0.4044449141847757);
		normalValues.add(0.40444491418477746);
		normalValues.add(0.24813599859094637);
		normalValues.add(0.19123230896968835);
		normalValues.add(0.1647732812606676);
		normalValues.add(0.24006865856430082);
		normalValues.add(0.20000004768370516);
		normalValues.add(0.10900766491188207);
		normalValues.add(0.20000004768371227);
		normalValues.add(0.40444491418477924);
		normalValues.add(0.0030162615090425504);
		normalValues.add(0.05999999821186108);
		normalValues.add(0.05199999886751172);
		normalValues.add(0.06159999881982792);
		normalValues.add(0.06927999889612124);
		normalValues.add(0.07542399904870933);
		normalValues.add(0.07532994414328797);
		normalValues.add(0.08033919924402255);
		normalValues.add(0.5);
		normalValues.add(0.08427135945886555);
		normalValues.add(0.340000110268593);
		normalValues.add(0.30000001192092896);
		normalValues.add(0.3955758986732967);
		normalValues.add(0.019999999105930755);
		normalValues.add(0.21560001587867816);
		normalValues.add(0.13283301814746876);
		normalValues.add(0.05193025879327907);
		normalValues.add(0.1875);
		normalValues.add(0.375);
		normalValues.add(0.08307781780646728);
		normalValues.add(0.125);
		normalValues.add(0.25);
		normalValues.add(0.01250004768371582);
		normalValues.add(0.1176000022888175);
		normalValues.add(0.0625);
		normalValues.add(0.20000004768371582);
		normalValues.add(0.4044448882341385);
		normalValues.add(0.40444491418477835);
		normalValues.add(0.019999999105934307);
		normalValues.add(0.4375);
		normalValues.add(0.36510663985490055);
		normalValues.add(0.4641593749554431);
		normalValues.add(0.3841593618424213);
		normalValues.add(0.2000000476837016);
		normalValues.add(0.011929668006757765);
		normalValues.add(0.4053654548823289);
		normalValues.add(0.07840000152587834);
		normalValues.add(0.40444491418477213);
		normalValues.add(0.019999999105920097);
		normalValues.add(0.36537445639108057);
		normalValues.add(0.3955758986732931);
		normalValues.add(0.395575898673286);
		normalValues.add(0.39557589867329757);
		normalValues.add(0.39557589867330023);
		normalValues.add(0.200000047683627);
		normalValues.add(0.019999999105927202);
		normalValues.add(0.36537445639108057);
		normalValues.add(0.36537445162271354);
		normalValues.add(0.3434175188903765);
		normalValues.add(0.3884175057773547);
		normalValues.add(0.38497228309773845);
		normalValues.add(0.44091960879393355);
		normalValues.add(0.3125);
		normalValues.add(0.2000000476835737);
	}

	@Override
	protected void onEvent(Event event) {
		if (!this.getState()) {
			return;
		}

		if (event instanceof PlayerMoveEvent) {
			
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			Player player = e.getPlayer();
			
			if(e.getTo().getX() == e.getFrom().getX() && e.getTo().getY() == e.getFrom().getY()
					&& e.getTo().getZ() == e.getFrom().getZ()) {
				return;
			}

			User user = Keaton.getUserManager().getUser(player.getUniqueId());
			
			double vector = MathUtils.offset(MathUtils.getHorizontalVector(e.getFrom()),
					MathUtils.getHorizontalVector(e.getTo()));
			double maxSpeed = 0;
			long lastHitDiff = Math.abs(System.currentTimeMillis() - user.isHit());
			
			int speed = getPotionEffectLevel(player, PotionEffectType.SPEED);
			
			if(player.getAllowFlight()) {
				return;
			}
			
			if(Math.abs(System.currentTimeMillis() - user.getLoginMIllis()) < 1250L) {
				return;
			}
			
			if (player.getVehicle() != null) {
				return;
			}
			
			if(Math.abs(System.currentTimeMillis() - user.isTeleported()) < 400L) {
				return;
			}
			
			if(!limit) {
				return;
			}
			
			if(lastHitDiff < 1800L) {
				return;
			}
			
			if((System.currentTimeMillis() - user.isVelocity()) < 2000L) {
				return;
			}
			
			Location below = player.getLocation().clone().subtract(0.0D, 1.0D, 0.0D);
			
			if(user.getGroundTicks() > 0) {
				if(user.getGroundTicks() == 1) {
					maxSpeed = 0.361;
					if(speed == 1) {
						maxSpeed = 0.379;
					} else if(speed == 2) {
						maxSpeed = 0.396;
					} else if(speed == 3) {
						maxSpeed += 0.413;
					} else if(speed >= 4) {
						maxSpeed += 0.43 + (speed - 4) * 0.25;
					}
				} else if(user.getGroundTicks() == 2) {
					maxSpeed = 0.325;
					if(speed == 1) {
						maxSpeed = 0.365;
					} else if(speed == 2) {
						maxSpeed = 0.416;
					} else if(speed == 3) {
						maxSpeed += 0.4685;
					} else if(speed >= 4) {
						maxSpeed += 0.518 + (speed - 4) * 0.6;
					}
				} else if(user.getGroundTicks() == 3) {
					maxSpeed = 0.312;
					
					if(speed == 1) {
						maxSpeed = 0.354;
					} else if(speed == 2) {
						maxSpeed += 0.406;
					} else if(speed == 3) {
						maxSpeed += 0.457;
					} else if(speed == 4) {
						maxSpeed = 0.513 + (speed - 4) * 0.62;
					}
				} else if(user.getGroundTicks() == 4) {
					maxSpeed = 0.31;
					if(speed == 1) {
						maxSpeed = 0.347;
					} else if(speed == 2) {
						maxSpeed = 0.4;
					} else if(speed == 3) {
						maxSpeed = 0.455;
					} else if(speed == 4) {
						maxSpeed = 0.5077 + (speed - 4) * 0.61;
					}
				} else if(user.getGroundTicks() >= 5) {
					maxSpeed = 0.31;
					if(speed == 1) {
						maxSpeed = 0.342;
					} else if(speed == 2) {
						maxSpeed += 0.4;
					} else if(speed == 3) {
						maxSpeed = 0.454;
					} else if(speed == 4) {
						maxSpeed = 0.51 + (speed - 4) * 0.62;
					}
				}
				if(BlockUtils.isStair(below.getBlock())) {
					maxSpeed += 0.2;
				}
			} else {
				if(user.getAirTicks() == 1) {
					maxSpeed = 0.36;
					
					if(user.getBlockTicks() > 0) {
						maxSpeed += 0.034;
					}
					if(speed == 1) {
						maxSpeed = 0.37;
					} else if(speed == 2) {
						maxSpeed = 0.388;
					} else if(speed == 3) {
						maxSpeed = 0.407;
					} else if(speed == 4) {
						maxSpeed = 0.418 + (speed - 4) * 0.02;
					}
				} else if(user.getAirTicks() == 2) {
					maxSpeed = 0.348;
					
					if(user.getBlockTicks() > 0) {
						maxSpeed += 0.31;
					}
					
					if(speed == 1) {
						maxSpeed = 0.364;
					} else if(speed == 2) {
						maxSpeed = 0.381;
					} else if(speed == 3) {
						maxSpeed = 0.391;
					} else if(speed == 4) {
						maxSpeed = 0.408 + (speed - 4) * 0.021;
					}
				} else if(user.getAirTicks() == 3) {
					maxSpeed = 0.354;
					
					
					if(speed == 1) {
						maxSpeed = 0.364;
					} else if(speed == 2) {
						maxSpeed = 0.381;
					} else if(speed == 3) {
						maxSpeed = 0.391;
					} else if(speed == 4) {
						maxSpeed = 0.408 + (speed - 4) * 0.021;
					}
				} else if(user.getAirTicks() == 4) {
					maxSpeed = 0.403;
					
					if(speed > 0) {
						maxSpeed += 0.016 * speed;
					}
				} else if(user.getAirTicks() >= 5) {
					maxSpeed = 0.352;
					
					if(speed > 0) {
						maxSpeed += 0.016 * speed;
					}
				}
			}
			
			if(PlayerUtils.isInWater(player)) {
				maxSpeed -= 0.1;
			}
			
			maxSpeed += player.getWalkSpeed() > 0.2 ? (player.getWalkSpeed() - 0.2) * 3 : 0;
			
			if(user.getIceTicks() > 0) {
				maxSpeed += 0.25;
			}
			
			if(user.getBlockTicks() > 0) {
				maxSpeed += 0.3;
			}
			
			if(user.getIceTicks() > 0 && user.getBlockTicks() > 0) {
				maxSpeed += 0.15;
				if(Math.abs(user.getIceTicks() - user.getBlockTicks()) > 0) {
					maxSpeed += 0.18;
				}
			}
			
			
			if(BlockUtils.isStair(player.getLocation().clone().subtract(0.0D, 1.0D, 0.0D).getBlock())) {
				maxSpeed += 0.35;
			}
			
			int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
			
			if(vector > maxSpeed && speed < 7) {
				//debug("Verbose (+2): " + verbose + " Vector: " + vector + " MaxSpeed: " + maxSpeed);
				verbose+= 2;
			} else {
				verbose = verbose > -2 ? verbose - 1 : verbose;
			}
			
			if(verbose > 7) {
				user.setVL(this, user.getVL(this) + 1);
				user.setCancelled(this, CancelType.MOVEMENT);
				verbose = 0;
				Alert(player, Color.Gray + "Reason: " + Color.Green + "Limit " + Color.Gray + "Speed: " + Color.Green + vector + Color.Gray + " > " + Color.Green + maxSpeed);
			}
			
			this.verbose.put(player.getUniqueId(), verbose);
		}
		if (event instanceof PlayerMoveEvent) {

			Location from = ((PlayerMoveEvent) event).getFrom().clone();
			Location to = ((PlayerMoveEvent) event).getTo().clone();
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			if ((e.getTo().getX() == e.getFrom().getX()) && (e.getTo().getZ() == e.getFrom().getZ())
					&& (e.getTo().getY() == e.getFrom().getY())) {
				return;
			}
			Player p = e.getPlayer();

			User user = Keaton.getUserManager().getUser(p.getUniqueId());

			Location l = p.getLocation();
			int x = l.getBlockX();
			int y = l.getBlockY();
			int z = l.getBlockZ();
			Location loc2 = new Location(p.getWorld(), x, y + 1, z);
			Location above = new Location(p.getWorld(), x, y + 2, z);
			long lastHitDiff = Math.abs(System.currentTimeMillis() - user.isHit());
			long loginDiff = Math.abs(System.currentTimeMillis() - user.getLoginMIllis());
			
			if(!normalMovements) {
				return;
			}
			
			if (lastHitDiff < 1500L) {
				return;
			}
			
			if(PlayerUtils.isGliding(p)) {
				return;
			}
			
			if(loginDiff < 1250L) {
				return;
			}
			
			if((System.currentTimeMillis() - user.isTeleported()) < 250L) {
				return;
			}
			
			if (p.getVehicle() != null) {
				return;
			}

			if (p.getGameMode().equals(GameMode.CREATIVE)) {
				return;
			}

			if (p.getAllowFlight()) {
				return;
			}
			
			double onGroundDiff = (to.getY() - from.getY());
			int vl = user.getVL(Speed.this);
			

			/** MOTION Y RELEATED HACKS **/
			if(Keaton.getAC().getPing().getTPS() > 18.2) {
				if (PlayerUtils.isReallyOnground(p) && !e.isCancelled() && !PlayerUtils.isOnClimbable(p, 0) && !PlayerUtils.isOnClimbable(p, -1) && (System.currentTimeMillis() - user.isTeleported()) > 1200L && !p.hasPotionEffect(PotionEffectType.JUMP)
						&& above.getBlock().getType() == Material.AIR && loc2.getBlock().getType() == Material.AIR
						&& onGroundDiff > 0 && onGroundDiff != 0 && !normalValues.contains(onGroundDiff)) {
					user.setVL(Speed.this, vl + 1);
					user.setCancelled(this, CancelType.MOVEMENT);
					Alert(p, Color.Gray + "Reason: " + Color.Green + "NormalMovements " + Color.Gray + "Illegal Value: " + Color.Green + onGroundDiff);
				}
			}
		}
		if (event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			
			if (verbose.containsKey(e.getPlayer().getUniqueId())) {
				verbose.remove(e.getPlayer().getUniqueId());
			}
		}
	}


	private int getPotionEffectLevel(Player p, PotionEffectType pet) {
		for (PotionEffect pe : p.getActivePotionEffects()) {
			if (pe.getType().getName().equals(pet.getName())) {
				return pe.getAmplifier() + 1;
			}
		}
		return 0;
	}

}