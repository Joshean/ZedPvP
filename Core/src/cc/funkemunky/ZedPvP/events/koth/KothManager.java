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

public class KothManager {
	
	private List<Koth> koths;
	private List<Koth> activeKoths;
	
	@SuppressWarnings("unchecked")
	public KothManager() {
		koths = Core.getInstance().getConfig().getList("Koths") != null ? (List<Koth>) Core.getInstance().getConfig().getList("Koths") : new ArrayList<Koth>();
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
		Core.getInstance().getConfig().set("Koths", koths);
		Core.getInstance().saveConfig();
	}
	
	public void removeKoth(Koth koth) {
		koths.remove(koth);
		Core.getInstance().getConfig().set("Koths", koths);
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
							&& player.getLocation().distance(koth.getCuboid().getOne()) < 100
							&& koth.getCuboid().isInCuboid(player)) {
						Cuboid cuboid = koth.getCuboid();
						ArrayList<UUID> playerList = cuboid.getPlayers();
						playerList.add(player.getUniqueId());
						cuboid.setPlayers(playerList);
						koth.setCuboid(cuboid);
					} else if(koth.getCuboid().getPlayers().contains(player.getUniqueId()) && !koth.getCuboid().isInCuboid(player)) {
						Cuboid cuboid = koth.getCuboid();
						ArrayList<UUID> playerList = cuboid.getPlayers();
						playerList.remove(player.getUniqueId());
						cuboid.setPlayers(playerList);
						koth.setCuboid(cuboid);
					}
					if(koth.getCuboid().getPlayers().size() == 1 && koth.getCuboid().getPlayers().contains(player.getUniqueId())
							&& koth.getCapping() == null && koth.getCapping() != player) {
						koth.setCapping(player);
						player.sendMessage(Core.getInstance().getPrefix() + Color.Gray + "You are now capping " + Color.Blue + koth.getName() + Color.Gray + ".");
					} else if(koth.getCuboid().getPlayers().size() <= 1 && !koth.getCuboid().getPlayers().contains(player.getUniqueId())
							&& koth.getCapping() != null && koth.getCapping() == player) {
						koth.setCapping(null);
					} else if(koth.getCuboid().getPlayers().size() > 1 && !koth.getCuboid().getPlayers().contains(player.getUniqueId()) && koth.getCapping() != null && koth.getCapping() == player) {
						ArrayList<UUID> uuids = koth.getCuboid().getPlayers();
						Random random = new Random();
						int i = random.nextInt();
						koth.setCapping(Bukkit.getPlayer(uuids.get(i)));
						Bukkit.getPlayer(uuids.get(i)).sendMessage(Core.getInstance().getPrefix() + Color.Gray + "You are now capping " + Color.Blue + koth.getName() + Color.Gray + ".");
					}
				}
				if(koth.getCapping() != null) {
					koth.setTimeLeft(koth.getTimeLeft() - 100L);
				}
				if(koth.getTimeLeft() <= 0L) {
					FPlayer fPlayer = FPlayers.getInstance().getByPlayer(koth.getCapping());
					Faction faction = fPlayer.getFaction();
					
					Core.getInstance().getRubyManager().addRubies(koth.getCapping(), 10);
					for(Player player : faction.getOnlinePlayers()) {
						Core.getInstance().getRubyManager().addRubies(player, 2);
					}
					
					stopKoth(koth);
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
