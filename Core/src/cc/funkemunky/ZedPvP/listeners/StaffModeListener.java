package cc.funkemunky.ZedPvP.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import com.massivecraft.factions.integration.Essentials;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.freeze.FreezeManager;
import cc.funkemunky.ZedPvP.utils.Color;
import cc.funkemunky.ZedPvP.utils.MiscUtils;

public class StaffModeListener implements Listener {
	
	private Map<Player, ItemStack[]> playerContents;
	private Map<Player, ItemStack[]> playerArmor;
	private Map<Player, Location> lastLocation;
	public List<UUID> isStaffMode;
	private Map<Player, Boolean> wasInCreative;
	
	public StaffModeListener() {
		playerContents = new HashMap<Player, ItemStack[]>();
		playerArmor = new HashMap<Player, ItemStack[]>();
		lastLocation = new WeakHashMap<Player, Location>();
		wasInCreative = new WeakHashMap<Player, Boolean>();
		isStaffMode = new ArrayList<UUID>();
	}
	
	public boolean isVanished(Player player) {
		return Essentials.isVanished(player);
	}
	
	public boolean isInStaffMode(Player player) {
		return isStaffMode.contains(player.getUniqueId());
	}

	public void setStaffMode(Player player) {
		if(!isStaffMode.contains(player.getUniqueId())) {
			ItemStack compass = MiscUtils.createItem(Material.COMPASS, 1, Color.Red + "Phase Compass", new String[] {"", Color.White + "Right Click " + Color.Gray + "to teleport through blocks.", Color.White + "Left Click " + Color.Gray + "to jump to the block you're looking at."});
			ItemStack inspectBook = MiscUtils.createItem(Material.BOOK, 1, Color.Red + "Inspection Book", new String[] {"", Color.White + "Interact with a player " + Color.Gray + "to inspect their inventory."});
			ItemStack freezeWand = MiscUtils.createItem(Material.BLAZE_ROD, 1, Color.Red + "Freeze Wand", new String[] {"", Color.White + "Interact with a player " + Color.Gray + "to freeze a player to bring in Teamspeak."});
			ItemStack randomTeleport = MiscUtils.createItem(Material.WATCH, 1, Color.Red + "Teleport Watch", new String[] {"", Color.White + "Right Click " + Color.Gray + "to randomly teleport to an online player."});
			ItemStack vanish = new ItemStack(Material.INK_SACK, 1, (short) (isVanished(player) ? 10 : 8));
			ItemMeta vanishMeta = vanish.getItemMeta();
			vanishMeta.setDisplayName(Color.Red + "Vanish");
			vanish.setItemMeta(vanishMeta);
			ItemStack betterView = MiscUtils.createItem(Material.CARPET, 1, Color.Red + "Better View", new String[] {"", Color.White + "Hold " + Color.Gray + "to have a better view."});
			ItemStack exitStaffMode = MiscUtils.createItem(Material.REDSTONE, 1, Color.Gold + "Exit Staff Mode", new String[] {"", Color.White + "Right Click " + Color.Gray + "to exit staff mode."});
			playerContents.put(player, player.getInventory().getContents());
			playerArmor.put(player, player.getInventory().getArmorContents());
			lastLocation.put(player, player.getLocation());
			player.getInventory().clear();
			player.getInventory().setItem(0, compass);
			player.getInventory().setItem(1, freezeWand);
			player.getInventory().setItem(2, inspectBook);
			player.getInventory().setItem(4, betterView);
			player.getInventory().setItem(6, randomTeleport);
			player.getInventory().setItem(7, vanish);
			player.getInventory().setItem(8, exitStaffMode);
			isStaffMode.add(player.getUniqueId());
			wasInCreative.put(player, player.getGameMode() == GameMode.CREATIVE);
			player.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Successfully went in staff mode!");
		} else {
			player.getInventory().clear();
			if(playerContents.containsKey(player)) {
				player.getInventory().setContents(playerContents.get(player));
				playerContents.remove(player);
			} else {
				player.sendMessage(Core.getInstance().getPrefix() + Color.Red + "An unknown error occurred that resulted in the lost of your inventory contents.");
			}
			if(playerArmor.containsKey(player)) {
				player.getInventory().setArmorContents(playerArmor.get(player));
				playerArmor.remove(player);
			} else {
				player.sendMessage(Core.getInstance().getPrefix() + Color.Red + "An unknown error occurred that resulted in the lost of your inventory armor contents.");
			}
			if(lastLocation.containsKey(player)) {
				player.teleport(lastLocation.get(player));
				lastLocation.remove(player);
			}
			if(wasInCreative.containsKey(player)) {
				if(!wasInCreative.get(player)) player.setGameMode(GameMode.SURVIVAL);
			} else {
				player.setGameMode(GameMode.SURVIVAL);
			}
			isStaffMode.remove(player.getUniqueId());
			player.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Successfully left staff mode!");
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack itemInHand = p.getItemInHand();
		Inventory rm = Bukkit.createInventory(null, 54, Color.Yellow + "Silent Chest");
		
		if(isInStaffMode(p)) {
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(itemInHand.getType().equals(Material.WATCH) && itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(Color.Red + "Teleport Watch")) {
					
					if(Core.getInstance().toTeleportTo.size() > 0) {
						Random r = new Random();
						int index = r.nextInt(Core.getInstance().toTeleportTo.size());
						if(Core.getInstance().toTeleportTo.get(index) != null) {
							p.teleport(Core.getInstance().toTeleportTo.get(index).getLocation());
							p.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "Teleported to " + Color.White + Core.getInstance().toTeleportTo.get(index).getDisplayName());
						} else {
							p.sendMessage(Core.getInstance().getPrefix() + Color.Red + "Cancelled teleportation as the player returned is null.");
						}
					} else {
						p.sendMessage(Core.getInstance().getPrefix() + Color.Red + "Prevented teleportation since there is nobody to teleport to.");
					}
					e.setCancelled(true);
 				}
				
				if(itemInHand.getType().equals(Material.INK_SACK) && itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(Color.Red + "Vanish")) {
					Bukkit.dispatchCommand(p, "vanish");
					e.setCancelled(true);
					if(Essentials.isVanished(p)) {
						itemInHand.setData(new MaterialData(10));
					} else {
						itemInHand.setData(new MaterialData(8));
					}
				}
			}
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getType().equals(Material.CHEST) || e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
					Chest chest = (Chest) e.getClickedBlock().getState();
					p.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "Opened chest silently.");
					rm.setContents(chest.getInventory().getContents());
					p.openInventory(rm);
					e.setCancelled(true);
				}
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(isInStaffMode(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(isInStaffMode(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryDrop(PlayerDropItemEvent e) {
		if(isInStaffMode(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInvPickup(PlayerPickupItemEvent e) {
		if(isInStaffMode(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInvMove(InventoryClickEvent e) {
		if(isInStaffMode((Player) e.getWhoClicked())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			if(isInStaffMode((Player) e.getDamager())) {
				Player p = (Player) e.getDamager();
				e.setCancelled(true);
				p.sendMessage(Core.getInstance().getPrefix() + Color.Red + "Get out of staff mode to pvp!");
				return;
			}
			if(Essentials.isVanished((Player) e.getDamager())) {
				Player p = (Player) e.getDamager();
				e.setCancelled(true);
				p.sendMessage(Core.getInstance().getPrefix() + Color.Red + "Get out of vanish to pvp!");
				return;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack itemInHand = p.getItemInHand();
		if(isInStaffMode(p)) {
			if(itemInHand.getType().equals(Material.IRON_FENCE)) {
				if(e.getRightClicked() instanceof Player) {
					Player clicked = (Player) e.getRightClicked();
					FreezeManager.freeze(clicked.getUniqueId());
					p.sendMessage(Core.getInstance().getPrefix() + Color.Green + "Successfully froze " + clicked.getName() + "!");
				}
			}
			if(itemInHand.getType().equals(Material.BOOK)) {
				if(e.getRightClicked() instanceof Player) {
					Player clicked = (Player) e.getRightClicked();
					Damageable clickedd = clicked;
					Inventory rm = Bukkit.createInventory(null, 54, Color.Gold + "Inspecting: " + Color.Gray + clicked.getName());
					rm.setContents(clicked.getInventory().getContents());
					rm.setItem(36, clicked.getInventory().getArmorContents()[0]);
					rm.setItem(37, clicked.getInventory().getArmorContents()[1]);
					rm.setItem(38, clicked.getInventory().getArmorContents()[2]);
					rm.setItem(39, clicked.getInventory().getArmorContents()[3]);
					for(int i = 40; i < 45 ; i++) {
						rm.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 5));
					}
					ItemStack health = new ItemStack(Material.SPECKLED_MELON, 1);
					ItemMeta healthMeta = health.getItemMeta();
					healthMeta.setDisplayName(Color.Red + clickedd.getHealth() / 2 + "♥");
					health.setItemMeta(healthMeta);
					ItemStack hunger = new ItemStack(Material.COOKED_CHICKEN, 1);
					ItemMeta hungerMeta = health.getItemMeta();
					hungerMeta.setDisplayName(Color.Gold + Integer.valueOf(Math.round(clicked.getFoodLevel())) + " Hunger");
					hunger.setItemMeta(hungerMeta);
					ItemStack potions = new ItemStack(Material.POTION, 1);
					ItemMeta potionsMeta = potions.getItemMeta();
					ArrayList<String> effects = new ArrayList<String>();
					effects.add(" ");
					for(PotionEffect effect : clicked.getActivePotionEffects()) {
						if(clicked.getActivePotionEffects() != null) {
							effects.add(Color.Red + "Effects:");
							effects.add(Color.Dark_Gray + "» " + Color.Gold + effect.getType().getName() + Color.Gray + ": "+ Color.White + effect.getDuration());
						}
					}
					
				    potionsMeta.setLore(effects);
				    potionsMeta.setDisplayName(ChatColor.RED + "Potion Effects");
				    potions.setItemMeta(potionsMeta);
				    
					rm.setItem(45, health);
					rm.setItem(46, hunger);
					rm.setItem(47, potions);
					
					p.openInventory(rm);
				}
			}
		}
		e.setCancelled(true);
	}
}
