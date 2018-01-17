package cc.funkemunky.Scoreboard.scoreboard.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.sapphiremade.neptunex.NeptuneX;

import cc.funkemunky.Scoreboard.Core;
import cc.funkemunky.Scoreboard.scoreboard.SidebarEntry;
import cc.funkemunky.Scoreboard.scoreboard.SidebarProvider;
import cc.funkemunky.Scoreboard.utils.Color;
import cc.funkemunky.ZedPvP.cooldowns.CooldownType;

public class ScoreboardProvider implements SidebarProvider {
	
    public SidebarEntry entry(String s){
    	
    	    if(s.length() <= 16) {
    	    	    return new SidebarEntry(s);
    	    }
        if(s.length() > 16 && s.length() <= 32){
            return new SidebarEntry(s.substring(0, 10), s.substring(10, s.length()), "");
        }

        if(s.length() > 32 && s.length() <= 48){
            return new SidebarEntry(s.substring(0, 10), s.substring(10, 20), s.substring(20, s.length()));
        }

        return null;
    }
    public List<SidebarEntry> getLines(Player player) {
        List<SidebarEntry> lines = new ArrayList<>();
        
        lines.add(entry(Color.Dark_Gray + Color.Strikethrough + "----------------"));
        lines.add(entry(Color.Red + "Rank: " + Color.White + Core.getInstance().getPermissions().getPrimaryGroup(player)));
        lines.add(entry(Color.Red + "Balance: " + Color.White + Core.getInstance().getEconomy().getBalance(player)));
        lines.add(entry(Color.Red + "Kills: " + Color.White + player.getStatistic(Statistic.PLAYER_KILLS)));
        lines.add(entry(Color.Red + "Deaths: " + Color.White + player.getStatistic(Statistic.DEATHS)));
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        if(fPlayer.getFaction().getId().equalsIgnoreCase("Wilderness")) {
        	    lines.add(entry(Color.Red + "Faction: " + Color.White + "None"));
        } else {
           	lines.add(entry(Color.Red + "Faction: " + Color.White + fPlayer.getFaction().getTag()));
           	lines.add(entry(Color.Red + "Power: " + Color.White + round(fPlayer.getFaction().getPower(), 3)));
        }
        
        if(cc.funkemunky.ZedPvP.Core.getInstance().getCooldownManager().hasCooldown(player, CooldownType.ENDERPEARL)) {
        	    lines.add(entry(Color.Red + "Enderpearl: " + Color.White + cc.funkemunky.ZedPvP.Core.getInstance().getCooldownManager().getCooldown(player, CooldownType.ENDERPEARL).getTime()));;
        }
        if(NeptuneX.getInstance().godGappleCooldown.containsKey(player.getUniqueId())) {
        	    lines.add(entry(Color.Red + "Gapple: " + Color.White + NeptuneX.getInstance().godGappleCooldown.get(player.getUniqueId())));
        }
        if(NeptuneX.getInstance().normalGappleCooldown.containsKey(player.getUniqueId())) {
    	    lines.add(entry(Color.Red + "Apple: " + Color.White + NeptuneX.getInstance().normalGappleCooldown.get(player.getUniqueId())));
        }
        lines.add(entry(Color.White + Color.Dark_Gray + Color.Strikethrough + "----------------"));
        return lines;
    }
    
	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
