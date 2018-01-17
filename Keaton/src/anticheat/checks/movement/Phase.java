package anticheat.checks.movement;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.TrapDoor;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.user.User;
import anticheat.utils.BlockUtils;
import anticheat.utils.MathUtils;

@ChecksListener(events = { PlayerMoveEvent.class })
public class Phase extends Checks {

	public List<Material> allowed;
	public List<Material> semi;
	private boolean silent;

	public Phase() {
		super("Phase", ChecksType.MOVEMENT, Keaton.getAC(), 100, true, false);

		allowed = new ArrayList<Material>();
		semi = new ArrayList<Material>();

		allowed.add(Material.SIGN);
		allowed.add(Material.SIGN_POST);
		allowed.add(Material.WALL_SIGN);
		allowed.add(Material.SUGAR_CANE_BLOCK);
		allowed.add(Material.WHEAT);
		allowed.add(Material.POTATO);
		allowed.add(Material.CARROT);
		allowed.add(Material.STEP);
		allowed.add(Material.AIR);
		allowed.add(Material.WOOD_STEP);
		allowed.add(Material.SOUL_SAND);
		allowed.add(Material.CARPET);
		allowed.add(Material.STONE_PLATE);
		allowed.add(Material.WOOD_PLATE);
		allowed.add(Material.LADDER);
		allowed.add(Material.CHEST);
		allowed.add(Material.WATER);
		allowed.add(Material.STATIONARY_WATER);
		allowed.add(Material.LAVA);
		allowed.add(Material.STATIONARY_LAVA);
		allowed.add(Material.REDSTONE_COMPARATOR);
		allowed.add(Material.REDSTONE_COMPARATOR_OFF);
		allowed.add(Material.REDSTONE_COMPARATOR_ON);
		allowed.add(Material.IRON_PLATE);
		allowed.add(Material.GOLD_PLATE);
		allowed.add(Material.DAYLIGHT_DETECTOR);
		allowed.add(Material.STONE_BUTTON);
		allowed.add(Material.WOOD_BUTTON);
		allowed.add(Material.HOPPER);
		allowed.add(Material.RAILS);
		allowed.add(Material.ACTIVATOR_RAIL);
		allowed.add(Material.DETECTOR_RAIL);
		allowed.add(Material.POWERED_RAIL);
		allowed.add(Material.TRIPWIRE_HOOK);
		allowed.add(Material.TRIPWIRE);
		allowed.add(Material.SNOW_BLOCK);
		allowed.add(Material.REDSTONE_TORCH_OFF);
		allowed.add(Material.REDSTONE_TORCH_ON);
		allowed.add(Material.DIODE_BLOCK_OFF);
		allowed.add(Material.DIODE_BLOCK_ON);
		allowed.add(Material.DIODE);
		allowed.add(Material.SEEDS);
		allowed.add(Material.MELON_SEEDS);
		allowed.add(Material.PUMPKIN_SEEDS);
		allowed.add(Material.DOUBLE_PLANT);
		allowed.add(Material.LONG_GRASS);
		allowed.add(Material.WEB);
		;
		allowed.add(Material.SNOW);
		allowed.add(Material.FLOWER_POT);
		allowed.add(Material.BREWING_STAND);
		allowed.add(Material.CAULDRON);
		allowed.add(Material.CACTUS);
		allowed.add(Material.WATER_LILY);
		allowed.add(Material.RED_ROSE);
		allowed.add(Material.ENCHANTMENT_TABLE);
		allowed.add(Material.ENDER_PORTAL_FRAME);
		allowed.add(Material.PORTAL);
		allowed.add(Material.ENDER_PORTAL);
		allowed.add(Material.ENDER_CHEST);
		allowed.add(Material.NETHER_FENCE);
		allowed.add(Material.NETHER_WARTS);
		allowed.add(Material.REDSTONE_WIRE);
		allowed.add(Material.LEVER);
		allowed.add(Material.YELLOW_FLOWER);
		allowed.add(Material.CROPS);
		allowed.add(Material.WATER);
		allowed.add(Material.LAVA);
		allowed.add(Material.SKULL);
		allowed.add(Material.TRAPPED_CHEST);
		allowed.add(Material.FIRE);
		allowed.add(Material.BROWN_MUSHROOM);
		allowed.add(Material.RED_MUSHROOM);
		allowed.add(Material.DEAD_BUSH);
		allowed.add(Material.SAPLING);
		allowed.add(Material.TORCH);
		allowed.add(Material.MELON_STEM);
		allowed.add(Material.PUMPKIN_STEM);
		allowed.add(Material.COCOA);
		allowed.add(Material.BED);
		allowed.add(Material.BED_BLOCK);
		allowed.add(Material.PISTON_EXTENSION);
		allowed.add(Material.PISTON_MOVING_PIECE);
		semi.add(Material.IRON_FENCE);
		semi.add(Material.THIN_GLASS);
		semi.add(Material.STAINED_GLASS_PANE);
		semi.add(Material.COBBLE_WALL);
		semi.add(Material.FENCE);
		
		silent = Keaton.getAC().getConfig().getBoolean("checks.Phase.silent");
	}

	@Override
	protected void onEvent(Event event) {
		if (!getState()) {
			return;
		}

		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;

			Player player = e.getPlayer();
			User user = Keaton.getUserManager().getUser(player.getUniqueId());

			Location from = e.getFrom();
			Location to = e.getTo();
			
			if(from.getX() == to.getX() && from.getZ() == to.getZ()
					&& from.getY() == to.getY()) {
				return;
			}
			
			if(from.distance(to) > 50) {
				player.kickPlayer("Illegal Stance");
				return;
			}
			if(player.getAllowFlight()) {
				return;
			}

			for (int x = Math.min(from.getBlockX(), to.getBlockX()); x <= Math.max(from.getBlockX(),
					to.getBlockX()); x++) {
				for (int z = Math.min(from.getBlockZ(), to.getBlockZ()); z <= Math.max(from.getBlockZ(),
						to.getBlockZ()); z++) {
					for (int y = Math.min(from.getBlockY(), to.getBlockY()); y <= Math.max(from.getBlockY(),
							to.getBlockY()) + 1; y++) {
						Location loc = new Location(player.getWorld(), x, y, z);
						Block block = loc.getBlock();
						double distance = MathUtils.getHorizontalDistance(from, to);
						if (block.getType().isSolid() && !allowed.contains(block.getType())
								&& !BlockUtils.isDoor(block) && !BlockUtils.isFenceGate(block)
								&& !BlockUtils.isTrapDoor(block) && !BlockUtils.isStair(block)
								&& !semi.contains(block.getType()) && distance > 0.2) {
							e.setTo(e.getFrom());
							user.setTeleported(System.currentTimeMillis());
							user.setVL(this, user.getVL(this) + 1);

							if(!silent) {
								Alert(player, block.getType().name().toUpperCase());
							}
						}
						if (BlockUtils.isDoor(block)) {
							Door door = (Door) block.getType().getNewData(block.getData());
							if (door.isTopHalf()) {
								return;
							}

							BlockFace facing = door.getFacing();
							if (door.isOpen()) {
								Block up = block.getRelative(BlockFace.UP);
								boolean hinge;
								if ((up.getType().equals(Material.IRON_DOOR_BLOCK)) || (up.getType().equals(Material.WOODEN_DOOR))) {
									hinge = (up.getData() & 0x1) == 1;
								} else {
									return;
								}
								if (facing == BlockFace.NORTH) {
									facing = hinge ? BlockFace.WEST : BlockFace.EAST;
								} else if (facing == BlockFace.EAST) {
									facing = hinge ? BlockFace.NORTH : BlockFace.SOUTH;
								} else if (facing == BlockFace.SOUTH) {
									facing = hinge ? BlockFace.EAST : BlockFace.WEST;
								} else {
									facing = hinge ? BlockFace.SOUTH : BlockFace.NORTH;
								}
							}
                             boolean greaterX = from.getX() > to.getX();
							boolean greaterZ = from.getZ() > to.getZ();
								if (facing == BlockFace.EAST) {
									if(greaterX) {
										if (Math.abs(from.getX() - block.getX()) > 0.6) {
											e.setTo(e.getFrom());
											
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										} 
									} else {
										if (Math.abs(to.getX() - block.getX()) > 0.6) {
											e.setTo(e.getFrom());
											
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									}
								}
								if(facing == BlockFace.WEST) {
									if(greaterX) {
										if(Math.abs(to.getX() - block.getX()) < 0.48) {
											e.setTo(e.getFrom());
											
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									} else {
										if(Math.abs(from.getX() - block.getX()) < 0.48) {
											e.setTo(e.getFrom());
											
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									}
								}
								if(facing == BlockFace.SOUTH) {
									if(greaterZ) {
										if(Math.abs(from.getZ() - block.getZ()) > 0.6) {
											e.setTo(e.getFrom());
											
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									} else {
										if(Math.abs(to.getZ() - block.getZ()) > 0.6) {
											e.setTo(e.getFrom());
											
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									}
								}
								if(facing == BlockFace.NORTH) {
									if(greaterZ) {
										if(Math.abs(to.getZ() - block.getZ()) < 0.48) {
											e.setTo(e.getFrom());
											
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									} else {
										if(Math.abs(from.getZ() - block.getZ()) < 0.48) {
											e.setTo(e.getFrom());
											
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									}
								}
							}
						if(BlockUtils.isFenceGate(block)) {
							Gate gate = (Gate) block.getType().getNewData(block.getData());
							
							if(!gate.isOpen()) {
								BlockFace facing = gate.getFacing();
								boolean greaterX = from.getX() > to.getX();
								boolean greaterZ = from.getZ() > to.getZ();
								
								if(facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
									if(greaterX) {
										if((Math.abs(to.getX() - block.getLocation().getX()) <= 0.925) && (Math.abs(from.getX() - block.getLocation().getX()) >= 0.08)) {
											e.setTo(e.getFrom());
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									} else {
										if(Math.abs(to.getX() - block.getLocation().getX()) >= 0.08 && (Math.abs(from.getX() - block.getLocation().getX()) <= 0.925)) {
											e.setTo(e.getFrom());
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									}
								}
								if(facing == BlockFace.EAST || facing == BlockFace.WEST) {
									if(greaterZ) {
										if((Math.abs(from.getZ() - block.getLocation().getZ()) <= 0.925) && (Math.abs(to.getZ() - block.getLocation().getZ()) >= 0.08)) {
											e.setTo(e.getFrom());
											//debug(String.valueOf(Math.abs(from.getZ() - block.getLocation().getX())));
											user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									} else {
										if(Math.abs(to.getZ() - block.getLocation().getZ()) >= 0.08 && (Math.abs(from.getZ() - block.getLocation().getZ()) <= 0.925)) {
											e.setTo(e.getFrom());
											//debug(String.valueOf(Math.abs(from.getZ() - block.getLocation().getX())));
											//user.setTeleported(System.currentTimeMillis());
											user.setVL(this, user.getVL(this) + 1);
											if(!silent) {
												Alert(player, block.getType().name().toUpperCase());
											}
										}
									}
								}
							}
						}
						if(BlockUtils.isTrapDoor(block)) {
							TrapDoor door = (TrapDoor) block.getType().getNewData(block.getData());
							
							if(!door.isOpen()) {
									if(!(Math.abs(to.getY() - block.getY()) >= 1.0) && to.getY() % 0.5 < 0.00000001) {
										e.setTo(e.getFrom());
										user.setTeleported(System.currentTimeMillis());
										user.setVL(this, user.getVL(this) + 1);
										Alert(player, block.getType().name().toUpperCase());
									}
							}
						}
						if(BlockUtils.isStair(block)) {
							if((to.getY() % 1 == 0 && from.getY() % 1 == 0) || (Math.abs(block.getY() - to.getY()) >= 1.0 && Math.abs(block.getY() - from.getY()) >= 1.0)) {
								e.setTo(e.getFrom());
								user.setTeleported(System.currentTimeMillis());
								user.setVL(this, user.getVL(this) + 1);
								Alert(player, block.getType().name().toUpperCase());
							}
						}
						if(semi.contains(block.getType())) {
							if(distance > 0.25 && ((Math.abs(from.getZ() - block.getZ()) > 0.1 && Math.abs(from.getZ() - block.getZ()) < 0.9) || (Math.abs(from.getX() - block.getX()) > 0.1 && Math.abs(from.getX() - block.getX()) < 0.9))) {
								e.setTo(e.getFrom());
								user.setTeleported(System.currentTimeMillis());
								user.setVL(this, user.getVL(this) + 1);
								Alert(player, block.getType().name().toUpperCase());
							}
						}
					}
				}
			}
		}

	}
}