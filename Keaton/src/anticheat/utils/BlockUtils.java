package anticheat.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockUtils {
	public static boolean isLiquid(Block block) {
		if((block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER
				|| block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA)) {
			return true;
		}
		return false;
	}
	
	public static boolean isDoor(Block block) {
		String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
		if (block.getType().equals(Material.IRON_DOOR) || block.getType().equals(Material.IRON_DOOR_BLOCK)
				|| block.getType().equals(Material.WOOD_DOOR) || block.getType().equals(Material.WOODEN_DOOR)) {
			return true;
		}
		if(!bukkitversion.contains("1_7")) {
			if(block.getType().equals(Material.ACACIA_DOOR) || block.getType().equals(Material.BIRCH_DOOR)
					|| block.getType().equals(Material.DARK_OAK_DOOR) || block.getType().equals(Material.IRON_DOOR)
					|| block.getType().equals(Material.JUNGLE_DOOR) || block.getType().equals(Material.SPRUCE_DOOR)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isTrapDoor(Block block) {
		String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
		if(block.getType().equals(Material.TRAP_DOOR)) {
			return true;
		}
		if(!bukkitversion.contains("1_7")) {
			if(block.getType().equals(Material.IRON_TRAPDOOR)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFenceGate(Block block) {
		String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
		if(block.getType().equals(Material.FENCE_GATE)) {
			return true;
		}
		if(!bukkitversion.contains("1_7")) {
			if(block.getType().equals(Material.ACACIA_FENCE_GATE) || block.getType().equals(Material.BIRCH_FENCE_GATE)
					|| block.getType().equals(Material.DARK_OAK_FENCE_GATE) || block.getType().equals(Material.JUNGLE_FENCE_GATE)
					|| block.getType().equals(Material.SPRUCE_FENCE_GATE)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isStair(Block block) {
	    if(block.getType().equals(Material.ACACIA_STAIRS) || block.getType().equals(Material.BIRCH_WOOD_STAIRS)
	    		|| block.getType().equals(Material.BRICK_STAIRS) || block.getType().equals(Material.COBBLESTONE_STAIRS)
	    		|| block.getType().equals(Material.DARK_OAK_STAIRS) || block.getType().equals(Material.NETHER_BRICK_STAIRS)
	    		|| block.getType().equals(Material.JUNGLE_WOOD_STAIRS) || block.getType().equals(Material.QUARTZ_STAIRS)
	    		|| block.getType().equals(Material.SMOOTH_STAIRS) || block.getType().equals(Material.WOOD_STAIRS)
	    		|| block.getType().equals(Material.SANDSTONE_STAIRS) || block.getType().equals(Material.SPRUCE_WOOD_STAIRS)) {
	    	return true;
	    }
	    return false;
	}
	
	public static boolean isSlab(Block block) {
		if(block.getType().equals(Material.STEP) || block.getType().equals(Material.WOOD_STEP)) {
			return true;
		}
		
		return false;
	}
	
	public static ArrayList<Block> getSurroundingB(Block block) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (double x = -0.5; x <= 0.5; x += 0.5) {
			for (double y = -0.5; y <= 0.5; y += 0.5) {
				for (double z = -0.5; z <= 0.5; z += 0.5) {
					if ((x != 0) || (y != 0) || (z != 0)) {
						blocks.add(block.getLocation().add(x, y, z).getBlock());
					}
				}
			}
		}
		return blocks;
	}

	public static ArrayList<Block> getSurroundingXZ(Block block) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		blocks.add(block.getRelative(BlockFace.NORTH));
		blocks.add(block.getRelative(BlockFace.NORTH_EAST));
		blocks.add(block.getRelative(BlockFace.NORTH_WEST));
		blocks.add(block.getRelative(BlockFace.SOUTH));
		blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
		blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
		blocks.add(block.getRelative(BlockFace.EAST));
		blocks.add(block.getRelative(BlockFace.WEST));

		return blocks;
	}

	public static ArrayList<Block> getSurroundingXZ(Block block, boolean diagonals) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		if (diagonals) {
			blocks.add(block.getRelative(BlockFace.NORTH));
			blocks.add(block.getRelative(BlockFace.NORTH_EAST));
			blocks.add(block.getRelative(BlockFace.NORTH_WEST));
			blocks.add(block.getRelative(BlockFace.SOUTH));
			blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
			blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
			blocks.add(block.getRelative(BlockFace.EAST));
			blocks.add(block.getRelative(BlockFace.WEST));
		} else {
			blocks.add(block.getRelative(BlockFace.NORTH));
			blocks.add(block.getRelative(BlockFace.SOUTH));
			blocks.add(block.getRelative(BlockFace.EAST));
			blocks.add(block.getRelative(BlockFace.WEST));
		}

		return blocks;
	}
}
