package anticheat.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import anticheat.Keaton;

public class EventInventory implements Listener {

	@EventHandler
	public void onclose(InventoryCloseEvent e) {
		Keaton.getAC().getChecks().event(e);
	}

	@EventHandler
	public void onopen(InventoryOpenEvent e) {
		Keaton.getAC().getChecks().event(e);
	}

}