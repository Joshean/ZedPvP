package anticheat.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.ProtocolLibrary;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksType;
import anticheat.packets.PacketListeners;
import anticheat.utils.Color;
import anticheat.utils.Messages;

public class GUIListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		GUI gui = Keaton.getAC().getGUIManager();
		Player player = (Player) event.getWhoClicked();
		ItemStack clickedItem = event.getCurrentItem();

		if(event.getCurrentItem() == null) {
			return;
		}
		if (event.getInventory().getName().equals(Color.Gold + Color.Bold + "Keaton AntiCheat")) {
			if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Color.Gold + "Reload Keaton")) {
				ItemStack item = event.getCurrentItem();
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(Color.Red + Color.Italics + "Working...");
				item.setItemMeta(meta);
				Keaton.getAC().reloadConfig();
				Keaton.getAC().reloadMessages();
				new Messages(Keaton.getAC());
				HandlerList.unregisterAll(Keaton.getAC());
				Keaton.getAC().getServer().getScheduler().cancelAllTasks();
				Keaton.getAC().registerEvents();
				ProtocolLibrary.getProtocolManager().getPacketListeners().forEach(ProtocolLibrary.getProtocolManager()::removePacketListener);
				new PacketListeners();
				Keaton.getAC().getChecks().getDetections().clear();
				Keaton.getAC().getChecks().init();
				for(Checks check : Keaton.getAC().getChecks().getDetections()) {
					check.setState(Keaton.getAC().getConfig().getBoolean("checks." + check.getName() + ".enabled"));
					check.setBannable(Keaton.getAC().getConfig().getBoolean("checks." + check.getName() + ".bannable"));
				}
				Keaton.getAC().clearVLS();
				meta.setDisplayName(Color.Green + "Reloaded!");
				item.setItemMeta(meta);
				new BukkitRunnable() {
					public void run() {
						meta.setDisplayName(Color.Gold + "Reload Keaton");
						item.setItemMeta(meta);
					}
				}.runTaskLaterAsynchronously(Keaton.getAC(), 30L);
			}
			if (gui.hasSameName(clickedItem, Color.Gold + "Reset Violations")) {
				ItemStack item = event.getCurrentItem();
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(Color.Red + Color.Italics + "Working...");
				item.setItemMeta(meta);
				Keaton.getAC().clearVLS();
				meta.setDisplayName(Color.Green + "Successfully Reset Violations!");
				item.setItemMeta(meta);
				new BukkitRunnable() {
					public void run() {
						meta.setDisplayName(Color.Gold + "Reset Violations");
						item.setItemMeta(meta);
					}
				}.runTaskLaterAsynchronously(Keaton.getAC(), 30L);
			}
			if (gui.hasSameName(clickedItem, Color.Gold + "Toggle Checks")) {
				gui.openChecksToggleGUI(player);
			}
			if (gui.hasSameName(clickedItem, Color.Gold + "Toggle Bannable Checks")) {
				gui.openChecksBannableGUI(player);
			}
			if (gui.hasSameName(clickedItem, Color.Red + "Keaton Info")
					&& event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				  player.closeInventory();
				  player.sendMessage("");
				  player.sendMessage(Color.Gray + "Discord: " + Color.Green + "https://discord.gg/wpHSp5t");
				  player.sendMessage("");
			}
			event.setCancelled(true);
		}
		if(event.getInventory().equals(gui.checksBannableGUI)) {
			if(gui.hasSameName(clickedItem, Color.Red + "Combat")) {
				gui.openBannableChecks(player, ChecksType.COMBAT);
			}
			if(gui.hasSameName(clickedItem, Color.Red + "Movement")) {
				gui.openBannableChecks(player, ChecksType.MOVEMENT);
			}
			if(gui.hasSameName(clickedItem, Color.Red + "Miscellaneous")) {
				gui.openBannableChecks(player, ChecksType.OTHER);
			}
			if(gui.hasSameName(clickedItem, Color.Red + "Back")) {
				gui.openMainGUI(player);
			}
			event.setCancelled(true);
		}
		if(event.getInventory().equals(gui.checksToggleGUI)) {
			if(gui.hasSameName(clickedItem,  Color.Red + "Combat")) {
				gui.openToggleChecks(player, ChecksType.COMBAT);
			}
			if(gui.hasSameName(clickedItem, Color.Red + "Movement")) {
				gui.openToggleChecks(player, ChecksType.MOVEMENT);
			}
			if(gui.hasSameName(clickedItem, Color.Red  + "Miscellaneous")) {
				gui.openToggleChecks(player, ChecksType.OTHER);
			}
			if(gui.hasSameName(clickedItem, Color.Red + "Back")) {
				gui.openMainGUI(player);
			}
			event.setCancelled(true);
		}
		if(event.getInventory().getName().contains(Color.Dark_Gray + "Toggle Checks for:")) {
			if(gui.hasSameName(clickedItem, Color.Red + "Back")) {
				gui.openChecksToggleGUI(player);
			} else {
				Checks check = Keaton.getAC().getChecks().getCheckByName(Color.strip(clickedItem.getItemMeta().getDisplayName()));
				if(check != null) {
					check.toggle();
					Keaton.getAC().getConfig().set("checks." + check.getName() + ".enabled", check.getState());
					Keaton.getAC().saveConfig();
				    event.setCurrentItem(gui.createItem(check.getState() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, event.getCurrentItem().getAmount(), check.getState() ? Color.Green + check.getName() : Color.Red + check.getName()));
				}
			}
			event.setCancelled(true);
		}
		if(event.getInventory().getName().contains(Color.Dark_Gray + "Toggle Bans for:")) {
			if(gui.hasSameName(clickedItem, Color.Red + "Back")) {
				gui.openChecksBannableGUI(player);
			} else {
				Checks check = Keaton.getAC().getChecks().getCheckByName(Color.strip(clickedItem.getItemMeta().getDisplayName()));
				if(check != null) {
					check.toggleBans();
					Keaton.getAC().getConfig().set("checks." + check.getName() + ".bannable", check.isBannable());
					Keaton.getAC().saveConfig();
				    event.setCurrentItem(gui.createItem(check.isBannable() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, event.getCurrentItem().getAmount(), check.isBannable() ? Color.Green + check.getName() : Color.Red + check.getName()));
				}
			}
			event.setCancelled(true);
		}
	}

}