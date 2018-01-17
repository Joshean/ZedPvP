package cc.funkemunky.ZedPvP.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;

public class ElevatorListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSneak(PlayerToggleSneakEvent e) {
		Player player = e.getPlayer();
		Block block = player.getLocation().getBlock();

		if (block.getType() == Material.CARPET && block.getData() == 14) {
			signClick(player, block.getLocation(), false);
		}
		if (block.getType() == Material.CARPET && block.getData() == 13) {
			signClick(player, block.getLocation(), true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Block block = e.getBlockPlaced();

		if (block.getType() == Material.CARPET && block.getData() == 14) {
			player.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Successfully placed elevator!");
			Hologram up = HologramsAPI.createHologram(Core.getInstance(), block.getLocation().clone().add(0.5D, 2.0D, 0.5D));
			up.appendTextLine(Color.Gold + "Sneak here to go up.");
		}
		if (block.getType() == Material.CARPET && block.getData() == 13) {
			player.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Successfully placed elevator!");
			Hologram down = HologramsAPI.createHologram(Core.getInstance(), block.getLocation().clone().add(0.5D, 2.0D, 0.5D));
			down.appendTextLine(Color.Gold + "Sneak here to go down.");
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

		if (block.getType() == Material.CARPET && block.getData() == 14) {
			player.sendMessage(Core.getInstance().getPrefix() + Color.Green + "You broke this elevator.");
			for(Hologram hologram : HologramsAPI.getHolograms(Core.getInstance())) {
				if(hologram.getLocation().equals(block.getLocation().clone().add(0.5D, 2.0D, 0.5D))) {
					hologram.delete();
				}
			}
		}
		if (block.getType() == Material.CARPET && block.getData() == 13) {
			player.sendMessage(Core.getInstance().getPrefix() + Color.Green + "You broke this elevator.");
			Hologram down = HologramsAPI.createHologram(Core.getInstance(), block.getLocation().clone().add(0.5D, 1.5, 0.5D));
			for(Hologram hologram : HologramsAPI.getHolograms(Core.getInstance())) {
				if(hologram.getLocation().equals(block.getLocation().clone().add(0.5D, 2.0D, 0.5D))) {
					hologram.delete();
				}
			}
		}
	}

	public Location teleportSpotUp(Location loc, int min, int max) {
		int k = min;
		while (k < max) {
			Material m = new Location(loc.getWorld(), loc.getBlockX(), k - 1, loc.getBlockZ()).getBlock().getType();
			Material m1 = new Location(loc.getWorld(), loc.getBlockX(), k, loc.getBlockZ()).getBlock().getType();
			Material m2 = new Location(loc.getWorld(), loc.getBlockX(), (k + 1), loc.getBlockZ()).getBlock().getType();
			if (m1.equals(Material.AIR) && m2.equals(Material.AIR) && m.isSolid() && !m.equals(Material.CARPET)) {
				return new Location(loc.getWorld(), loc.getBlockX(), k, loc.getBlockZ());
			}
			++k;
		}
		return new Location(loc.getWorld(), loc.getBlockX(),
				loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), loc.getBlockZ());
	}

	public Location teleportSpotDown(Location loc, int min, int max) {
		int k = min;
		while (k > max) {
			Material m = new Location(loc.getWorld(), loc.getBlockX(), k - 1, loc.getBlockZ()).getBlock().getType();
			Material m1 = new Location(loc.getWorld(), loc.getBlockX(), k, loc.getBlockZ()).getBlock().getType();
			Material m2 = new Location(loc.getWorld(), loc.getBlockX(), (k + 1), loc.getBlockZ()).getBlock().getType();
			if (m1.equals(Material.AIR) && m2.equals(Material.AIR) && m.isSolid() && !m.equals(Material.CARPET)) {
				return new Location(loc.getWorld(), loc.getBlockX(), k, loc.getBlockZ());
			}
			--k;
		}
		return new Location(loc.getWorld(), loc.getBlockX(),
				loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), loc.getBlockZ());
	}
    public boolean signClick(Player player, Location signLocation, boolean up) {
        Block block = signLocation.getBlock();
        do {
            if ((block = block.getRelative(up ? BlockFace.UP : BlockFace.DOWN)).getY() <= block.getWorld().getMaxHeight() && block.getY() > 1) continue;
            player.sendMessage(Core.getInstance().getPrefix() + Color.Red + "Could not locate the carpet " + (up ? "above" : "below"));
            return false;
        } while (!block.getType().equals(Material.CARPET));
        boolean underSafe = this.isSafe(block.getRelative(BlockFace.DOWN));
        boolean overSafe = this.isSafe(block.getRelative(BlockFace.UP));
        if (!underSafe && !overSafe) {
            player.sendMessage(Core.getInstance().getPrefix() + Color.Red + "Could not find a place to teleport by the carpet " + (up ? "above" : "below"));
            return false;
        }
        Location location = player.getLocation().clone();
        location.setX((double)block.getX() + 0.5);
        location.setY((double)(block.getY() + (underSafe ? -1 : 0)));
        location.setZ((double)block.getZ() + 0.5);
        location.setPitch(0.0f);
        player.teleport(location);
        player.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Successfully teleported.");
        return true;
    }
    
    public boolean isSafe(Block block) {
        return block != null && !block.getType().isSolid() && block.getType() != Material.GLASS && block.getType() != Material.STAINED_GLASS;
    }

}
