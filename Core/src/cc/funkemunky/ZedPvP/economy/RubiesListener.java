package cc.funkemunky.ZedPvP.economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;

public class RubiesListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}

		Player player = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();

		if (e.getClickedInventory().getName().equals(Color.Gold + "Ruby Shop")) {
			if (Core.getInstance().getRubyGUI().hasSameName(item, Color.Yellow + "Key Shop")) {
				Core.getInstance().getRubyGUI().openKeysGUI(player);
			}
			if (Core.getInstance().getRubyGUI().hasSameName(item, Color.Yellow + "Money Shop")) {
				Core.getInstance().getRubyGUI().openMoneyGUI(player);
			}
			e.setCancelled(true);
		}
		if (e.getClickedInventory().getName().equals(Color.Gold + "Ruby Money Shop")) {
			if (item.getType().equals(Material.EMERALD)) {
				int amount = Integer.parseInt(Color.strip(item.getItemMeta().getDisplayName()));
				int price = amount / 500;

				if (Core.getInstance().getRubyManager().getRubies(player) > price) {
					Core.getInstance().getRubyManager().subtractRubies(player, price);
					Core.getInstance().getEconomy().depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()),
							amount);
				} else {
					ItemStack lastItem = item;
					ItemStack newItem = Core.getInstance().getRubyGUI().createItem(Material.BARRIER, 1,
							Color.Red + "No enough rubies!");
					e.setCurrentItem(newItem);

					if (!lastItem.getType().equals(Material.BARRIER)) {
						new BukkitRunnable() {
							public void run() {
								e.setCurrentItem(lastItem);
							}
						}.runTaskLater(Core.getInstance(), 40L);
					}
				}
			}
			if (Core.getInstance().getRubyGUI().hasSameName(item, Color.Red + "Go back")) {
				player.closeInventory();
				Core.getInstance().getRubyGUI().openMainGUI(player);
			}
			e.setCancelled(true);
		}
		if (e.getClickedInventory().getName().equals(Color.Gold + "Ruby Key Shop")) {
			if (Core.getInstance().getRubyGUI().hasSameName(item, Color.Red + "Go back")) {
				player.closeInventory();
				Core.getInstance().getRubyGUI().openMainGUI(player);
			}
			e.setCancelled(true);
		}
	}

}
