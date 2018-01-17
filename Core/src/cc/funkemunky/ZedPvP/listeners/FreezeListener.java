package cc.funkemunky.ZedPvP.listeners;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.meta.ItemMeta;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.freeze.Chat;
import cc.funkemunky.ZedPvP.freeze.FreezeManager;
import cc.funkemunky.ZedPvP.freeze.GUIManager;
import cc.funkemunky.ZedPvP.utils.Color;
import cc.funkemunky.ZedPvP.utils.JSONMessage;
import cc.funkemunky.ZedPvP.utils.JSONMessage.ClickableType;


public class FreezeListener implements Listener {
	
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (FreezeManager.isFrozen(uuid)) {
            Location movingFrom = event.getFrom();

            //Stops movement
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();

        if (FreezeManager.isFrozen(player.getUniqueId())) {
            player.sendMessage(Chat.disallow());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();

        if (FreezeManager.isFrozen(player.getUniqueId())) {
            player.sendMessage(Chat.disallow());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();

        if (FreezeManager.isFrozen(player.getUniqueId())) {
            player.sendMessage(Chat.disallow());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();

        if (FreezeManager.isFrozen(player.getUniqueId()))
        {
            player.sendMessage(Chat.disallow());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event)
    {
        Entity damager = event.getDamager();

        if (FreezeManager.isFrozen(damager.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        Entity player = event.getEntity();
        if (FreezeManager.isFrozen(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event)
    {
        Player player = event.getPlayer();

        if (FreezeManager.isFrozen(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if (FreezeManager.isFrozen(player.getUniqueId())) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.hasPermission("zedpvp.staff")) {
                	    JSONMessage message = new JSONMessage();
                	    message.addText(Chat.c("&7[&c!&8] &a" + player.getName() + " has logged out while frozen! "));
                    message.addText(Color.Gray + Color.Italics + "(Click to Ban)")
                    .addHoverText(Color.Green + "/ban " + player.getName() + " Logged out while Frozen [Factions]")
                    .setClickEvent(ClickableType.RunCommand, "ban " + player.getName() + " Logged out while Frozen [Factions]");
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	    Player player = event.getPlayer();
    	    if(FreezeManager.isFrozen(player.getUniqueId())) {
    	    	        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (p.hasPermission("zedpvp.staff")) {
                    	    JSONMessage message = new JSONMessage();
                    	    message.addText(Chat.c("&7[&c!&8] &a" + player.getName() + " logged in but is still frozen! "));
                        message.addText(Color.Gray + Color.Italics + "(Punish)")
                        .addHoverText(Color.Green + "/ban " + player.getName() + " Logged out while Frozen [Factions]")
                        .setClickEvent(ClickableType.RunCommand, "ban " + player.getName() + " Logged out while Frozen [Factions]");
                        message.addText(Color.Gray + Color.Italics + "(Unfreeze)")
                        .addHoverText(Color.Green + "/freeze " + player.getName())
                        .setClickEvent(ClickableType.RunCommand, "/freeze " + player.getName());
                    }
                }
    	    }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();
        if (FreezeManager.isFrozen(player.getUniqueId())) {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) && Core.getInstance().getConfig().getBoolean("options.freeze.enderpearling")) {
                player.sendMessage(Chat.disallow());
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onInvClose(InventoryCloseEvent event)
    {
        Player target = (Player) event.getPlayer();

        if (!(target instanceof Player)) {
            return;
        }

        if (!(FreezeManager.isFrozen(target.getUniqueId()))) {
            return;
        }

        if (!(Core.getInstance().getInvLock().contains(target.getUniqueId()))) {
            return;
        }

        if (!(event.getInventory().getName().equals(GUIManager.getInv().getName()))) {
            return;
        }

        Core.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {
            public void run() {
                GUIManager.openInv(target);
            }
        }, 1L);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if(event.getClickedInventory() != null) {
            if (FreezeManager.isFrozen(player.getUniqueId())) {
                event.setCancelled(true);
        		if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Admit")) {
        			ItemMeta meta = event.getCurrentItem().getItemMeta();
        			meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Admitted!");
        			meta.setLore(null);
        			event.getCurrentItem().setItemMeta(meta);
        			for(Player online : Bukkit.getServer().getOnlinePlayers()) {
        				if(online.hasPermission("zedpvp.staff")) {
        					online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "!" + ChatColor.DARK_GRAY + "] "+ ChatColor.GREEN + event.getWhoClicked().getName() + " has admitted while being frozen!");
        					online.playSound(player.getLocation(), Sound.NOTE_PLING, 40L, 1L);
        				}
        			}
        		}
            }
        }
    }

    @EventHandler
    public void preventPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasPermission("zedpvp.freeze.bypass"))) {
            Location loc = player.getLocation();

            List<UUID> fPlayers = Core.getInstance().getFrozen();

            for (UUID uuid : fPlayers) {
                if (loc.distance(Bukkit.getPlayer(uuid).getLocation()) <= 3.0D) {
                    player.sendMessage(Chat.c("&cYou may not place blocks near this player."));
                    event.setBuild(false);
                }
            }
        }
    }

    @EventHandler
    public void preventBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasPermission("zedpvp.freeze.bypass"))) {
            Location loc = player.getLocation();

            List<UUID> fPlayers = Core.getInstance().getFrozen();

            for (UUID uuid : fPlayers) {
                if (loc.distance(Bukkit.getPlayer(uuid).getLocation()) <= 3.0D) {
                    player.sendMessage(Chat.c("&cYou may not break blocks near this player."));
                    event.setCancelled(true);
                }
            }
        }
    }
}