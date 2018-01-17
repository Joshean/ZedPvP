package anticheat.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import anticheat.Keaton;
import anticheat.user.User;
import anticheat.utils.CancelType;

public class EventPlayerInteractEvent implements Listener {

	@EventHandler
	public void onMove(PlayerInteractEvent event) {
		Keaton.getAC().getChecks().event(event);
		Player p = (Player) event.getPlayer();
		User user = Keaton.getUserManager().getUser(p.getUniqueId());
		if (Keaton.getAC().getPing().getTPS() < 16) {
			return;
		}
		if(event.getItem() != null && event.getItem().getType().equals(Material.BOW)) {
			user.setLastBow(System.currentTimeMillis());
		}
		
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			user.setLeftClicks(user.getLeftClicks() + 1);

		} else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			user.setRightClicks(user.getRightClicks() + 1);
		}
		
		if(user.isCancelled() == CancelType.INTERACT) {
			event.setCancelled(true);
			user.setCancelled(null, CancelType.NONE);
		}
	}
	
	@EventHandler
	public void potionSplash(EntityRegainHealthEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		
		if(event.getRegainReason() != RegainReason.MAGIC) {
			return;
		}
		
		Player player = (Player) event.getEntity();
		User user = Keaton.getUserManager().getUser(player.getUniqueId());
		
		user.setLastPotionSplash(System.currentTimeMillis());
	}
	
	@EventHandler
	public void inventoryInteract(InventoryInteractEvent event) {
		Keaton.getAC().getChecks().event(event);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Keaton.getAC().getChecks().event(event);
		
		User user = Keaton.getUserManager().getUser(event.getPlayer().getUniqueId());
		if(user != null) {
			if(user.isCancelled() == CancelType.BLOCK) {
				event.setCancelled(true);
				user.setCancelled(null, CancelType.NONE);
				return;
			}
			user.setLastBlockPlaced(user.getBlockPlaced());
			user.setBlockPlaced(event.getBlockPlaced());
			user.setLastBlockPlace(System.currentTimeMillis());
		}
		if(event.isCancelled()) {
			event.getPlayer().setVelocity(new Vector(0, -1, 0));
		}
		if (event.getBlockReplacedState().getBlock().getType() == Material.WATER) {
			
			if(user != null) {
				user.setPlacedBlock(true);
			}
		}
	}
}
