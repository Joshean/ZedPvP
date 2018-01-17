package anticheat.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class PlayerUtils {

	public static final double PLAYER_WIDTH = .6;
	private static ImmutableSet<Material> ground = Sets.immutableEnumSet(Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK,
			Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH,
			Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT,
			Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS,
			Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL,
			Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF,
			Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
			Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST,
			Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE,
			Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN,
			Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER);

	static {
		String bukkit = Bukkit.getServer().getClass().getPackage().getName().substring(23);
		if (bukkit.contains("1_8") || bukkit.contains("1_9") | bukkit.contains("1_1")) {
			ground = Sets.immutableEnumSet(Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK,
					Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH,
					Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT,
					Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS,
					Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL,
					Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF,
					Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
					Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST,
					Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE,
					Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN,
					Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER,
					Material.getMaterial("ARMOR_STAND"), Material.getMaterial("BANNER"), Material.getMaterial("STANDING_BANNER")
					, Material.getMaterial("WALL_BANNER"));
		}
	}

	public static boolean isGround(Material material) {
		return ground.contains(material);
	}

	public static boolean isOnGround(Location loc) {
		double diff = .3;

		return !isGround(loc.clone().add(0, -.1, 0).getBlock().getType())
				|| !isGround(loc.clone().add(diff, -.1, 0).getBlock().getType())
				|| !isGround(loc.clone().add(-diff, -.1, 0).getBlock().getType())
				|| !isGround(loc.clone().add(0, -.1, diff).getBlock().getType())
				|| !isGround(loc.clone().add(0, -.1, -diff).getBlock().getType())
				|| !isGround(loc.clone().add(diff, -.1, diff).getBlock().getType())
				|| !isGround(loc.clone().add(diff, -.1, -diff).getBlock().getType())
				|| !isGround(loc.clone().add(-diff, -.1, diff).getBlock().getType())
				|| !isGround(loc.clone().add(-diff, -.1, -diff).getBlock().getType());
	}

	public static void kick(Player p, String reason) {
		if (p.isOnline()) {
			p.kickPlayer("§8[§c§l" + "Keaton" + "§8]:\n§c" + reason);
			Bukkit.broadcastMessage(
					"§8[§c§l" + "Keaton" + "§8]" + " §7kicked §0" + p.getDisplayName() + " §7for §a" + reason);
		}
	}
	
	public static boolean cantStandAtWater(Block block) {
		Block otherBlock = block.getRelative(BlockFace.DOWN);

		boolean isHover = block.getType() == Material.AIR;
		boolean n = (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.WATER)
				|| (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER);
		boolean s = (otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.WATER)
				|| (otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER);
		boolean e = (otherBlock.getRelative(BlockFace.EAST).getType() == Material.WATER)
				|| (otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER);
		boolean w = (otherBlock.getRelative(BlockFace.WEST).getType() == Material.WATER)
				|| (otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER);
		boolean ne = (otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER)
				|| (otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER);
		boolean nw = (otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER)
				|| (otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER);
		boolean se = (otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER)
				|| (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER);
		boolean sw = (otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER)
				|| (otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER);

		return (n) && (s) && (e) && (w) && (ne) && (nw) && (se) && (sw) && (isHover);
	}
	
	public static boolean cantStandAtLava(Block block) {
		Block otherBlock = block.getRelative(BlockFace.DOWN);

		boolean isHover = block.getType() == Material.AIR;
		boolean n = (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.LAVA)
				|| (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_LAVA);
		boolean s = (otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.LAVA)
				|| (otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_LAVA);
		boolean e = (otherBlock.getRelative(BlockFace.EAST).getType() == Material.LAVA)
				|| (otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_LAVA);
		boolean w = (otherBlock.getRelative(BlockFace.WEST).getType() == Material.LAVA)
				|| (otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_LAVA);
		boolean ne = (otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.LAVA)
				|| (otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_LAVA);
		boolean nw = (otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.LAVA)
				|| (otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_LAVA);
		boolean se = (otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.LAVA)
				|| (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_LAVA);
		boolean sw = (otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.LAVA)
				|| (otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_LAVA);

		return (n) && (s) && (e) && (w) && (ne) && (nw) && (se) && (sw) && (isHover);
	}
	
	public static boolean isOnLilyPad(Player player) {
		Block block = player.getLocation().getBlock();
		Material lily = Material.WATER_LILY;

		return (block.getType() == lily) || (block.getRelative(BlockFace.NORTH).getType() == lily)
				|| (block.getRelative(BlockFace.SOUTH).getType() == lily)
				|| (block.getRelative(BlockFace.EAST).getType() == lily)
				|| (block.getRelative(BlockFace.WEST).getType() == lily);
	}
	

	public static boolean isHoveringOverWater(Location player, int blocks) {
		for (int i = player.getBlockY(); i > player.getBlockY() - blocks; i--) {
			Block newloc = new Location(player.getWorld(), player.getBlockX(), i, player.getBlockZ()).getBlock();
			if (newloc.getType() != Material.AIR) {
				return newloc.isLiquid();
			}
		}
		return false;
	}
	
	public static boolean isOnIce(Player player) {
		boolean ice = false;
		for(Block block : BlockUtils.getSurroundingB(player.getLocation().clone().subtract(0.0D, 1.0D, 0.0D).getBlock())) {
			if(block.getType().equals(Material.ICE) || block.getType().equals(Material.PACKED_ICE)) {
				ice = true;
			}
		}
		return ice;
	}
	
	public static boolean hasBlockAbove(Player player) {
		boolean has = false;
		for(Block block : BlockUtils.getSurroundingB(player.getLocation().clone().add(0.0D, 2.0D, 0.0D).getBlock())) {
			if(block.getType().isSolid()) {
				has = true;
			}
		}
		return has;
	}
	
	public static boolean isOnStairs(Player player) {
		if(BlockUtils.isStair(player.getLocation().clone().subtract(0.0D, 1.0D, 0.0D).getBlock())) {
			return true;
		}
		return false;
	}

	public static boolean isHoveringOverWater(Location player) {
		return isHoveringOverWater(player, 25);
	}
	
	public static boolean isFullyInWater(Location player) {
		double touchedX = MathUtils.fixedXAxis(player.getX());

		return (new Location(player.getWorld(), touchedX, player.getY(), player.getBlockZ()).getBlock().isLiquid())
				&& (new Location(player.getWorld(), touchedX, Math.round(player.getY()), player.getBlockZ()).getBlock()
						.isLiquid());
	}
	
	public static boolean hasSlabsNear(Location location) {
		for(Block block : BlockUtils.getSurroundingXZ(location.getBlock(), true)) {
			if(BlockUtils.isSlab(block)) {
				return true;
			}
		}
		return false;
	}

	
	public static boolean hasBlocksNear(Player player) {
		for(Block block : BlockUtils.getSurroundingXZ(player.getLocation().getBlock())) {
			if(block.getType().isSolid()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasBlocksNear(Location location) {
		for(Block block : BlockUtils.getSurroundingXZ(location.getBlock())) {
			if(block.getType().isSolid()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOnClimbable(Player player, int blocks) {
		if (blocks == 0) {
			for (Block block : getSurrounding(player.getLocation().getBlock(), false)) {
				if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
					return true;
				}
			}
		} else {
			for (Block block : getSurrounding(player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock(),
					false)) {
				if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
					return true;
				}
			}
		}
		return player.getLocation().getBlock().getType() == Material.LADDER
				|| player.getLocation().getBlock().getType() == Material.VINE;
	}
	 
    public static boolean isInWeb(Player player) {
        if (player.getLocation().getBlock().getType() != Material.WEB && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.WEB && player.getLocation().getBlock().getRelative(BlockFace.UP).getType() != Material.WEB) {
            return false;
        }
        return true;
    }
	
	public static ArrayList<Block> getSurrounding(Block block, boolean diagonals) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		if (diagonals) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						if ((x != 0) || (y != 0) || (z != 0)) {
							blocks.add(block.getRelative(x, y, z));
						}
					}
				}
			}
		} else {
			blocks.add(block.getRelative(BlockFace.UP));
			blocks.add(block.getRelative(BlockFace.DOWN));
			blocks.add(block.getRelative(BlockFace.NORTH));
			blocks.add(block.getRelative(BlockFace.SOUTH));
			blocks.add(block.getRelative(BlockFace.EAST));
			blocks.add(block.getRelative(BlockFace.WEST));
		}
		return blocks;
	}

	public static boolean hasPotionEffect(Player player, PotionEffectType type) {
		if (player.getActivePotionEffects().size() > 0) {
			for (PotionEffect effect : player.getActivePotionEffects()) {
				if (effect.getType() == type) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isInWater(Player player) {
		final Material m = player.getLocation().getBlock().getType();
		return m == Material.STATIONARY_WATER || m == Material.WATER || m == Material.LAVA || m == Material.STATIONARY_LAVA;
	}

	public static Location getEyeLocation(final Player player) {
		final Location eye = player.getLocation();
		eye.setY(eye.getY() + player.getEyeHeight());
		return eye;
	}

	public static boolean isOnGround(Location loc, Double ydiff) {
		double diff = .3;

		return !isGround(loc.clone().add(0, ydiff, 0).getBlock().getType())
				|| !isGround(loc.clone().add(diff, ydiff, 0).getBlock().getType())
				|| !isGround(loc.clone().add(-diff, ydiff, 0).getBlock().getType())
				|| !isGround(loc.clone().add(0, ydiff, diff).getBlock().getType())
				|| !isGround(loc.clone().add(0, ydiff, -diff).getBlock().getType())
				|| !isGround(loc.clone().add(diff, ydiff, diff).getBlock().getType())
				|| !isGround(loc.clone().add(diff, ydiff, -diff).getBlock().getType())
				|| !isGround(loc.clone().add(-diff, ydiff, diff).getBlock().getType())
				|| !isGround(loc.clone().add(-diff, ydiff, -diff).getBlock().getType());
	}

	public static double offset(Vector a, Vector b) {
		return a.subtract(b).length();
	}

	public static boolean isReallyOnground(Player p) {
		Location l = p.getLocation();
		int x = l.getBlockX();
		int y = l.getBlockY();
		int z = l.getBlockZ();
		Location b = new Location(p.getWorld(), x, y - 1, z);

		if (p.isOnGround() && b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB
				&& !b.getBlock().isLiquid()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean flaggyStuffNear(Location loc) {
		boolean nearBlocks = false;
		for (Block bl : getSurrounding(loc.getBlock(), true)) {
			if ((bl.getType().equals(Material.STEP)) || (bl.getType().equals(Material.DOUBLE_STEP))
					|| (bl.getType().equals(Material.BED)) || (bl.getType().equals(Material.WOOD_DOUBLE_STEP))
					|| (bl.getType().equals(Material.WOOD_STEP))) {
				nearBlocks = true;
				break;
			}
		}
		for (Block bl : getSurrounding(loc.getBlock(), false)) {
			if ((bl.getType().equals(Material.STEP)) || (bl.getType().equals(Material.DOUBLE_STEP))
					|| (bl.getType().equals(Material.BED)) || (bl.getType().equals(Material.WOOD_DOUBLE_STEP))
					|| (bl.getType().equals(Material.WOOD_STEP))) {
				nearBlocks = true;
				break;
			}
		}
		if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.STEP, Material.BED,
				Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP })) {
			nearBlocks = true;
		}
		return nearBlocks;
	}

	public static boolean isBlock(Block block, Material[] materials) {
		Material type = block.getType();
		Material[] arrayOfMaterial;
		int j = (arrayOfMaterial = materials).length;
		for (int i = 0; i < j; i++) {
			Material m = arrayOfMaterial[i];
			if (m == type) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAir(final Player player) {
		final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		return b.getType().equals((Object) Material.AIR)
				&& b.getRelative(BlockFace.WEST).getType().equals((Object) Material.AIR)
				&& b.getRelative(BlockFace.NORTH).getType().equals((Object) Material.AIR)
				&& b.getRelative(BlockFace.EAST).getType().equals((Object) Material.AIR)
				&& b.getRelative(BlockFace.SOUTH).getType().equals((Object) Material.AIR);
	}
	
	public static Vector getRotation(Location one, Location two) {
		double dx = two.getX() - one.getX();
		double dy = two.getY() - one.getY();
		double dz = two.getZ() - one.getZ();
		double distanceXZ = Math.sqrt(dx * dx + dz * dz);
		float yaw = (float) (Math.atan2(dz, dx) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(dy, distanceXZ) * 180.0D / 3.141592653589793D);
		return new Vector(yaw, pitch, 0.0F);
	}
	
	public static double clamp180(double dub) {
		dub %= 360.0D;
		if (dub >= 180.0D) {
			dub -= 360.0D;
		}
		if (dub < -180.0D) {
			dub += 360.0D;
		}
		return dub;
	}
	
	public static double getDistance3D(Location one, Location two) {
		double toReturn = 0.0D;
		double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
		double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
		double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
		double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
		toReturn = Math.abs(sqrt);
		return toReturn;
	}
	
	public static double[] getOffsetsOffCursor(Player player, LivingEntity entity) {
		Location entityLoc = entity.getLocation().add(0.0D, entity.getEyeHeight(), 0.0D);
		Location playerLoc = player.getLocation().add(0.0D, player.getEyeHeight(), 0.0D);

		Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0F);
		Vector expectedRotation = getRotation(playerLoc, entityLoc);

		double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
		double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());

		double horizontalDistance = MathUtils.getHorizontalDistance(playerLoc, entityLoc);
		double distance = getDistance3D(playerLoc, entityLoc);

		double offsetX = deltaYaw * horizontalDistance * distance;
		double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;

		return new double[] { Math.abs(offsetX), Math.abs(offsetY) };
	}

	public static double getOffsetOffCursor(Player player, LivingEntity entity) {
		double offset = 0.0D;
		double[] offsets = getOffsetsOffCursor(player, entity);

		offset += offsets[0];
		offset += offsets[1];

		return offset;
	}
	
	public static boolean isGliding(Player p) {
		if(!ServerUtils.isBukkitVerison("1_9") && !ServerUtils.isBukkitVerison("1_1")) {
			return false;
		}
		
		boolean isGliding = false;
		try {
			isGliding = (boolean) p.getClass().getMethod("isGliding").invoke(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isGliding;
	
	}
}
