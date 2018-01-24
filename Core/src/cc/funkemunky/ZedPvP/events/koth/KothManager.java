package cc.funkemunky.ZedPvP.events.koth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;
import cc.funkemunky.ZedPvP.utils.Cuboid;
import cc.funkemunky.ZedPvP.utils.DurationFormatter;
import cc.funkemunky.ZedPvP.utils.MiscUtils;

public class KothManager {
	
	private List<Koth> koths;
	private List<Koth> activeKoths;

	public KothManager() {
		koths = new ArrayList<Koth>();
		
		int i = 0;
		if(MiscUtils.getConfigKeys("Koths") != null && MiscUtils.getConfigKeys("Koths").size() > 0) {
			for(String path : MiscUtils.getConfigKeys("Koths")) {
				Koth koth = (Koth) Core.getInstance().getConfig().get(path);
				if(koth != null) {
					koths.add(koth);
				}
				i++;
			}
		}
		
		activeKoths = new ArrayList<Koth>();
		
		new BukkitRunnable() {
			public void run() {
				task();
			}
		}.runTaskTimer(Core.getInstance(), 0L, 2L);
	}
	
	public void createKoth(String name, Location one, Location two, long capTime) {
		Faction faction = Factions.getInstance().createFaction();
		faction.setTag(Color.Blue + name);
		Koth koth = new Koth(name, new Cuboid(one, two), faction, capTime);
		koths.add(koth);
		int i = 0;
		for(Koth koth2 : koths) {
			Core.getInstance().getConfig().set("Koths." + i, koth2);
			Core.getInstance().saveConfig();
			i++;
		}
	}
	
	public void removeKoth(Koth koth) {
		Core.getInstance().getConfig().set("Koths." + koths.indexOf(koth), null);
		koths.remove(koth);
		int i = 0;
		for(Koth koth2 : koths) {
			Core.getInstance().getConfig().set("Koths." + i, koth2);
			i++;
		}
		Core.getInstance().saveConfig();
	}
	
	public Koth getKoth(Koth koth) {
		if(koths.contains(koth)) {
			return koth;
		}
		return null;
	}
	
	public Koth getKoth(String string) {
		Iterator<Koth> iterator = koths.iterator();
		while(iterator.hasNext()) {
			Koth koth = iterator.next();
			if(koth.getName().equalsIgnoreCase(string)) {
				return koth;
			}
		}
		return null;
	}
	
	public void updateKothTime(Koth koth, long millis) {
		koth.setTimeLeft(millis);
	}
	
	public void resetKothTime(Koth koth) {
		updateKothTime(koth, koth.getDefaultTime());
	}
	
	public long getTimeLeft(Koth koth) {
		return koth.getTimeLeft();
	}
	
	public long getDefaultTime(Koth koth) {
		return koth.getDefaultTime();
	}
	
	public String getTimeLeftForatted(Koth koth) {	
		return DurationFormatter.getRemaining(koth.getTimeLeft(), true);
	}
	
	public void setCapping(Koth koth, Player player) {
		koth.setCapping(player);
	}
	
	public void updateLocation(Koth koth, Location one, Location two) {
		koth.setCuboid(new Cuboid(one, two));
	}
	
	public void updateLocation(Koth koth, Cuboid cuboid) {
		koth.setCuboid(cuboid);
	}
	
	public void startKoth(Koth koth) {
		if(!activeKoths.contains(koth)) {
			activeKoths.add(koth);
		}
		koth.setTimeLeft(koth.getDefaultTime());
		activeKoths.set(activeKoths.indexOf(koth), koth);
		Bukkit.broadcastMessage(Color.Gold + "[KOTH] " + Color.Blue + koth.getName() + Color.Gray + " koth has been started and can now be contested!");
	}
	
	public void stopKoth(Koth koth) {
		if(activeKoths.contains(koth)) {
			activeKoths.remove(koth);
		}
		
		Bukkit.broadcastMessage(Color.Gold + "[KOTH] " + Color.Blue + koth.getName() + Color.Gray + " koth has been stopped!");
	}
	
	public void task() {
		if(activeKoths.size() > 0) {
			Iterator<Koth> koths = activeKoths.iterator();
			while(koths.hasNext()) {
				Koth koth = koths.next();
				Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();
				
				while(players.hasNext()) {
					Player player = players.next();
					if(player.getWorld().equals(koth.getCuboid().getOne().getWorld())
							&& koth.getCuboid().isInCuboid(player.getLocation())) {
						Cuboid cuboid = koth.getCuboid();
						ArrayList<UUID> playerList = cuboid.getPlayers();
						playerList.add(player.getUniqueId());
						cuboid.setPlayers(playerList);
						koth.setCuboid(cuboid);
						activeKoths.set(activeKoths.indexOf(koth), koth);
					} else if(koth.getCuboid().getPlayers().contains(player.getUniqueId()) && !koth.getCuboid().isInCuboid(player)) {
						Cuboid cuboid = koth.getCuboid();
						ArrayList<UUID> playerList = cuboid.getPlayers();
						playerList.remove(player.getUniqueId());
						cuboid.setPlayers(playerList);
						koth.setCuboid(cuboid);
						activeKoths.set(activeKoths.indexOf(koth), koth);
					}
					if(koth.getCuboid().getPlayers().size() == 1 && koth.getCuboid().getPlayers().contains(player.getUniqueId())
							&& koth.getCapping() == null && koth.getCapping() != player) {
						koth.setCapping(player);
						activeKoths.set(activeKoths.indexOf(koth), koth);
						player.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "You are now capping " + Color.Blue + koth.getName() + Color.Gray + ".");
					} else if(!koth.getCuboid().getPlayers().contains(player.getUniqueId())
							&& koth.getCapping() != null && koth.getCapping() == player) {
						koth.setCapping(null);
						koth.setTimeLeft(koth.getDefaultTime());
						activeKoths.set(activeKoths.indexOf(koth), koth);
						player.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "You are no longer capping " + Color.Blue + koth.getName() + Color.Gray + ".");
					} else if(koth.getCuboid().getPlayers().size() > 1 && !koth.getCuboid().getPlayers().contains(player.getUniqueId()) && koth.getCapping() != null && koth.getCapping() == player) {
						ArrayList<UUID> uuids = koth.getCuboid().getPlayers();
						Random random = new Random();
						int i = random.nextInt();
						koth.setCapping(Bukkit.getPlayer(uuids.get(i)));
						activeKoths.set(activeKoths.indexOf(koth), koth);
						Bukkit.getPlayer(uuids.get(i)).sendMessage(Core.getInstance().getPrefix() + Color.Gray + "You are now capping " + Color.Blue + koth.getName() + Color.Gray + ".");
					}
				}
				if(koth.getCapping() != null) {
					koth.setTimeLeft(koth.getTimeLeft() - 100L);
					activeKoths.set(activeKoths.indexOf(koth), koth);
				}
				if(koth.getTimeLeft() <= 0L) {
					FPlayer fPlayer = FPlayers.getInstance().getByPlayer(koth.getCapping());
					Faction faction = fPlayer.getFaction();
					
					Core.getInstance().getRubyManager().addRubies(koth.getCapping(), 10);
					for(Player player : faction.getOnlinePlayers()) {
						Core.getInstance().getRubyManager().addRubies(player, 2);
					}
					
					stopKoth(koth);
					this.koths.set(this.koths.indexOf(koth), koth);
					int i = 0;
					for(Koth koth2 : this.koths) {
						Core.getInstance().getConfig().set("Koths." + i, koth2);
						i++;
					}
					Core.getInstance().saveConfig();
					Bukkit.broadcastMessage(Color.Gold + "[KOTH] " + Color.Blue + koth.getName() + Color.Gray + " has been successfully capped by " + Color.Green + faction.getTag() + Color.Gray + "!");
				}
			}
		}
	}
	
	public List<Koth> getAllKoths() {
		return koths;
	}
	
	public List<Koth> getAllActiveKoths() {
		return activeKoths;
	}

}
