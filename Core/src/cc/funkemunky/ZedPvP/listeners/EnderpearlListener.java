package cc.funkemunky.ZedPvP.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.ProtocolLibrary;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.cooldowns.Cooldown;
import cc.funkemunky.ZedPvP.cooldowns.CooldownType;
import cc.funkemunky.ZedPvP.utils.Color;
import cc.funkemunky.ZedPvP.utils.DurationFormatter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.PlayerInventory;

public class EnderpearlListener implements Listener {

    private static final long REFRESH_DELAY_TICKS = 2L; // time in ticks it will update the remaining time on the Enderpearl.
    private static final long REFRESH_DELAY_TICKS_18 = 20L; // time in ticks it will update the remaining time on the Enderpearl for a 1.8 client.
    private final Map<UUID, PearlNameFaker> itemNameFakes = new HashMap<>();

    public void refund(Player player) {
        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof EnderPearl) {
            EnderPearl enderPearl = (EnderPearl) projectile;
            ProjectileSource source = enderPearl.getShooter();
            if (source instanceof Player) {
                Player shooter = (Player) source;
                if(Core.getInstance().getCooldownManager().hasCooldown(shooter, CooldownType.ENDERPEARL)) {
                	     long remaining = (Core.getInstance().getCooldownManager().getCooldown(shooter, CooldownType.ENDERPEARL).getTime());
                     if (remaining > 0L) {
                         shooter.sendMessage(Color.Gray + "Enderpearl Cooldown: " + Color.Red + DurationFormatUtils.formatDurationWords(remaining, true, true));

                         shooter.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                         event.setCancelled(true);
                         return;
                     }

                } else {
                	    Core.getInstance().getCooldownManager().addNewCooldown(shooter, CooldownType.ENDERPEARL, TimeUnit.SECONDS.toMillis(16));
                	    startDisplaying(shooter);
                }
            }
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PearlNameFaker pearlNameFaker = itemNameFakes.get(player.getUniqueId());
        if (pearlNameFaker != null) {
            int previousSlot = event.getPreviousSlot();
            ItemStack item = player.getInventory().getItem(previousSlot);
            if (item == null)
                return;

           pearlNameFaker.setFakeItem(CraftItemStack.asNMSCopy(player.getItemInHand()), previousSlot);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player) {
            Player player = (Player) humanEntity;
            PearlNameFaker pearlNameFaker = itemNameFakes.get(player.getUniqueId());
            if (pearlNameFaker == null)
                return;
            for (Map.Entry<Integer, ItemStack> entry : event.getNewItems().entrySet()) {
                if (entry.getKey() == player.getInventory().getHeldItemSlot()) {
                    pearlNameFaker.setFakeItem(CraftItemStack.asNMSCopy(player.getItemInHand()), player.getInventory().getHeldItemSlot());
                    break;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player) {
            Player player = (Player) humanEntity;
            PearlNameFaker pearlNameFaker = itemNameFakes.get(player.getUniqueId());
            if (pearlNameFaker == null)
                return;

            // Required to prevent ghost items.
            int heldSlot = player.getInventory().getHeldItemSlot();
            if (event.getSlot() == heldSlot) {
                pearlNameFaker.setFakeItem(CraftItemStack.asNMSCopy(player.getItemInHand()), heldSlot);
            } else if (event.getHotbarButton() == heldSlot) {
                pearlNameFaker.setFakeItem(CraftItemStack.asNMSCopy(event.getCurrentItem()), event.getSlot());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.updateInventory();
                    }
                }.runTask(Core.getInstance());
            }
        }
    }
    
    private long getRemaining(Player player) {
    	    if(Core.getInstance().getCooldownManager().hasCooldown(player, CooldownType.ENDERPEARL)) {
    	    	    return Core.getInstance().getCooldownManager().getCooldown(player, CooldownType.ENDERPEARL).getTime();
    	    }
    	    return 0L;
    }

    /**
     * Starts displaying the remaining Enderpearl cooldown on the hotbar.
     *
     * @param player
     *            the {@link Player} to display for
     */
    public void startDisplaying(Player player) {
        PearlNameFaker pearlNameFaker;
        if (getRemaining(player) > 0L && itemNameFakes.putIfAbsent(player.getUniqueId(), pearlNameFaker = new PearlNameFaker(Core.getInstance().getCooldownManager().getCooldown(player, CooldownType.ENDERPEARL), player)) == null) {
            long ticks = ProtocolLibrary.getProtocolManager().getProtocolVersion(player) >= 47 ? REFRESH_DELAY_TICKS_18 : REFRESH_DELAY_TICKS;
            pearlNameFaker.runTaskTimerAsynchronously(Core.getInstance(), ticks, ticks);
        }
    }

    /**
     * Stop displaying the remaining Enderpearl cooldown on the hotbar.
     *
     * @param player
     *            the {@link Player} to stop for
     */
    public void stopDisplaying(Player player) {
        PearlNameFaker pearlNameFaker = itemNameFakes.remove(player.getUniqueId());
        if (pearlNameFaker != null) {
            pearlNameFaker.cancel();
        }
    }

    /**
     * Runnable to show remaining Enderpearl cooldown on held item.
     */
    public static class PearlNameFaker extends BukkitRunnable {

        private final Cooldown timer;
        private final Player player;

        public PearlNameFaker(Cooldown timer, Player player) {
            this.timer = timer;
            this.player = player;
        }

        @Override
        public void run() {
            ItemStack stack = player.getItemInHand();
            if (stack != null && stack.getType() == Material.ENDER_PEARL) {
                long remaining = timer.getTime();
                net.minecraft.server.v1_8_R3.ItemStack item = CraftItemStack.asNMSCopy(player.getItemInHand());
                if (remaining > 0L) {
                    item = item.cloneItemStack();
                    item.c(Color.Gray + "Enderpearl Cooldown:" + Color.Red + DurationFormatter.getRemaining(remaining, true, true));
                   setFakeItem(item, player.getInventory().getHeldItemSlot());
                } else {
                    cancel();
                }
            }
        }

        @Override
        public synchronized void cancel() throws IllegalStateException {
            super.cancel();
            setFakeItem(CraftItemStack.asNMSCopy(player.getItemInHand()), player.getInventory().getHeldItemSlot()); // show the original item here.
        }

        /**
         * Sends a fake SetSlot packet to a {@link Player}.
         *
         * @param nms
         *            the {@link net.minecraft.server.v1_7_R4.ItemStack} to set at
         * @param index
         *            the inventory index position to set at
         */
        public void setFakeItem(net.minecraft.server.v1_8_R3.ItemStack nms, int index) {
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

            // Taken from CraftInventoryPlayer
           if (index < PlayerInventory.getHotbarSize())
               index = index + 36;
            else if (index > 35)
                index = 8 - (index - 36);

            entityPlayer.playerConnection.sendPacket(new PacketPlayOutSetSlot(entityPlayer.activeContainer.windowId, index, nms));
        }
    }
}
