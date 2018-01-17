package anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksListener;
import anticheat.detections.ChecksType;
import anticheat.packets.events.PacketKillauraEvent;
import anticheat.packets.events.PacketTypes;
import anticheat.user.User;
import anticheat.utils.CustomLocation;
import anticheat.utils.MathUtils;
import anticheat.utils.PlayerUtils;

@ChecksListener(events = {PacketKillauraEvent.class})
public class Range extends Checks {
	
	private Map<UUID, Long> lastReachCheck;
	
    public Range() {
    	    super("Range", ChecksType.COMBAT, Keaton.getAC(), 15, true, false);
    	    
    	    lastReachCheck = new HashMap<UUID, Long>();
    }
    
    @Override
    protected void onEvent(Event event) {
    	    if(!getState()) {
    	    	    return;
    	    }
    	    
    	    if(event instanceof PacketKillauraEvent) {
    	    	    PacketKillauraEvent e = (PacketKillauraEvent) event;
    	    	    
    	    	    if(e.getType() == PacketTypes.USE) {
    	    	    	    return;
    	    	    }
    	    	    
    	    	    Player attacker = e.getPlayer();
    	    	    Player attacked = (Player) e.getEntity();
    	    	    
    	    	    long time;
    	            if (this.lastReachCheck.containsKey(attacker.getUniqueId()) && (time = System.currentTimeMillis() - this.lastReachCheck.get(attacker.getUniqueId())) < 200) {
    	                return;
    	            }
    	            int attackerPing = Keaton.getAC().getPing().getPing(attacker);
    	            int attackedPing = Keaton.getAC().getPing().getPing(attacked);
    	            if (attacker.getGameMode() == GameMode.CREATIVE) {
    	                return;
    	            }
    	            if (attackerPing < 0) {
    	                return;
    	            }
    	            
    	            User attackerStats = Keaton.getUserManager().getUser(attacker.getUniqueId());
    	            User attackedStats = Keaton.getUserManager().getUser(attacked.getUniqueId());
    	            
    	            if (attackerStats.getPacketsFromLag() > 0) {
    	                return;
    	            }
    	            if (attackedStats.getPacketsFromLag() > 0) {
    	                return;
    	            }
    	            long attackerAverageMovePackets = attackerStats.getMovePacketAverage();
    	            long attackedAverageMovePackets = attackedStats.getMovePacketAverage();
    	            CustomLocation attackerLastMove = attackerStats.getLastMovePacket(1);
    	            if (attackerLastMove == null) {
    	                return;
    	            }
    	            if (System.currentTimeMillis() - attackerLastMove.getTimeStamp() > 500) {
    	                return;
    	            }
    	            CustomLocation attackedLastMove = attackerStats.getLastPlayerLocation(attacked.getUniqueId(), this.getFormula(attackerPing, attackerAverageMovePackets) + 1);
    	            if (attackedLastMove == null) {
    	                return;
    	            }
    	            if (System.currentTimeMillis() - attackedLastMove.getTimeStamp() > 500) {
    	                return;
    	            }
    	            double range = Math.hypot(attackerLastMove.getX() - attackedLastMove.getX(), attackerLastMove.getZ() - attackedLastMove.getZ());
    	            if (range > 7.0) {
    	                return;
    	            }
    	            double rangeThreshold = 3.0 + this.getDistancePlayerCanMove(attacker, attackerPing, attackerAverageMovePackets);
    	            if (!attacked.isSprinting() || MathUtils.getDistanceBetweenAngles(attacker.getLocation().getYaw(), attacked.getLocation().getYaw()) < 50.0) {
    	                rangeThreshold += this.getDistancePlayerCanMove(attacked, attackedPing, attackedAverageMovePackets);
    	            }
    	            if (attackedStats.getDeltaXZ() > 12.0) {
    	                rangeThreshold += 0.2;
    	            }
    	            int vl = attackerStats.getVL(this);
    	            if (range >= rangeThreshold) {
    	                if ((vl + 10) >= 50) {
    	                    Alert(attacker, "Type A");
    	                    if (vl >= 100.0) {
    	                        Alert(attacked, "Type B");
    	                    }
    	                }
	    	             attackerStats.setVL(this, attackerStats.getVL(this) + 10);
    	            } else {
    	            	attackerStats.setVL(this, attackerStats.getVL(this) > 0 ? attackerStats.getVL(this) - 1 : 0);
    	            }
    	            this.lastReachCheck.put(attacker.getUniqueId(), System.currentTimeMillis());
    	    }
    }
    
    public double getDistancePlayerCanMove(Player player, int ping, long average) {
        return (double)this.getFormula(ping, average) * this.getDistancePlayerCanMoveInATick(player);
    }

    public int getFormula(int ping, long average) {
        int formula = (int)Math.round((double)((long)(ping / 2) + average) / 50.0);
        if (formula == 0) {
            formula = 1;
        }
        return formula;
    }

    private double getDistancePlayerCanMoveInATick(Player player) {
        double distance;
        
        int speed = 0;
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (!effect.getType().equals(PotionEffectType.SPEED)) continue;
            speed = effect.getAmplifier() + 1;
            break;
        }
        if (PlayerUtils.isOnGround(player.getLocation()) && (MathUtils.frac(player.getLocation().getY()) <= 0.1 || MathUtils.frac(player.getLocation().getY()) == 0.5)) {
            distance = 0.3;
            if (PlayerUtils.isOnStairs(player)) {
                distance = 0.45;
            } else if (PlayerUtils.isOnIce(player)) {
                distance = PlayerUtils.hasBlockAbove(player) ? 1.3 : 0.65;
            } else if (PlayerUtils.hasBlockAbove(player)) {
                distance = 0.7;
            }
            distance += (double)(player.getWalkSpeed() > 0.2f ? player.getWalkSpeed() * 10.0f * 0.33f : 0.0f);
            distance += 0.06 * (double)speed;
        } else {
            distance = 0.36;
            if (PlayerUtils.isOnStairs(player)) {
                distance = 0.45;
            } else if (PlayerUtils.isOnIce(player)) {
                distance = PlayerUtils.hasBlockAbove(player) ? 1.3 : 0.65;
            } else if (PlayerUtils.hasBlockAbove(player)) {
                distance = 0.7;
            }
            distance += (double)(player.getWalkSpeed() > 0.2f ? player.getWalkSpeed() * 10.0f * 0.33f : 0.0f);
            distance += 0.02 * (double)speed;
        }
        return distance;
    }

}
