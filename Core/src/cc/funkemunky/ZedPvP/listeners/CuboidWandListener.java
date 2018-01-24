package cc.funkemunky.ZedPvP.listeners;

import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import cc.funkemunky.ZedPvP.utils.Color;
import cc.funkemunky.ZedPvP.utils.Cuboid;
import cc.funkemunky.ZedPvP.utils.MiscUtils;

public class CuboidWandListener implements Listener {
	
	public static Map<Player, Cuboid> cuboids;
	
	public CuboidWandListener() {
		cuboids = new WeakHashMap<Player, Cuboid>();
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if(!player.getItemInHand().equals(MiscUtils.WAND)) {
			return;
		}
		
		Cuboid cuboid = cuboids.getOrDefault(player, new Cuboid(null, null));
		
		if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Location location = e.getClickedBlock().getLocation();
			
			cuboid = new Cuboid(location, cuboid.getTwo());
			cuboids.put(player, cuboid);
			player.sendMessage(Color.Green + "Saved location one!");
		}
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Location location = e.getClickedBlock().getLocation();
			
			cuboid = new Cuboid(cuboid.getOne(), location);
			
			cuboids.put(player, cuboid);
			player.sendMessage(Color.Green + "Saved location twp!");
		}
		if(e.getAction() == Action.LEFT_CLICK_AIR) {
			cuboid = new Cuboid(null, null);
			cuboids.put(player, cuboid);
			player.sendMessage(Color.Green + "Cleared selection!");
		}
		
	}

}
