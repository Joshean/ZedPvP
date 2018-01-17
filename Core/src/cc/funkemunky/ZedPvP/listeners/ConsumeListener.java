package cc.funkemunky.ZedPvP.listeners;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.cooldowns.CooldownType;
import cc.funkemunky.ZedPvP.utils.Color;

public class ConsumeListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onGappleConsume(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		
		if(item.getType() != Material.GOLDEN_APPLE || item.getDurability() != 1) {
			return;
		}
		Player player = e.getPlayer();
		if(!Core.getInstance().getCooldownManager().hasCooldown(player, CooldownType.GAPPLE)) {
			if(!e.isCancelled()) {
				Core.getInstance().getCooldownManager().addNewCooldown(player, CooldownType.GAPPLE, TimeUnit.MINUTES.toMillis(2));
			}
		} else {
			player.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "Gapple Cooldown: " + Color.Red + DurationFormatUtils.formatDurationWords(Core.getInstance().getCooldownManager().getCooldown(player, CooldownType.GAPPLE).getTime(), true, true));
			e.setCancelled(true);
			player.updateInventory();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCrappleConsume(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		if(item.getType() != Material.GOLDEN_APPLE || item.getDurability() != 0) {
			return;
		}
		Player player = e.getPlayer();
		if(!Core.getInstance().getCooldownManager().hasCooldown(player, CooldownType.CRAPPLE)) {
			if(!e.isCancelled()) {
				Core.getInstance().getCooldownManager().addNewCooldown(player, CooldownType.CRAPPLE, TimeUnit.SECONDS.toMillis(15));
			}
		} else {
			player.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "Crapple Cooldown: " + Color.Red + DurationFormatUtils.formatDurationWords(Core.getInstance().getCooldownManager().getCooldown(player, CooldownType.CRAPPLE).getTime(), true, true));
			e.setCancelled(true);
			player.updateInventory();
		}
	}

}
