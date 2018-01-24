package anticheat.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.utils.CancelType;
import anticheat.utils.CustomLocation;
import anticheat.utils.Pattern;
import lombok.Getter;
import lombok.Setter;

public class User {

	private Player player;
	private UUID uuid;
	private Map<Checks, Integer> vl;
	private ArrayList<String> logList = new ArrayList<String>();
	private int AirTicks = 0;
	private int GroundTicks = 0;
	private int IceTicks = 0;
	private int inva = 0;
	private int invb = 0;
	private boolean hasSwung = false;
	private boolean hasAlerts = false;
	private CancelType isCancelled = CancelType.NONE;
	private long lastSwing = 0;
	private int swingPackets = 0;
	private int usePackets = 0;
	private long lastPacket = 0L;
	private long teleported = 0L;
	private double lastYawDifference = 1.0D;
	private long lastFlightChange = 0;
	private long loginMillis = 0L;
	private double lastPitchDifferenceAimC;
	private double lastPitchAimC;
	private long lastMove = 0L;
	@Getter @Setter private long lastServerKeepAlive = 0L;
	@Getter @Setter private long lastClientKeepAlive = 0L;
	private long lastMoveTime = 0L;
	private long lastHeal = 0L;
	private long lastPotionSplash = 0L;
	private long isHit = 0L;
	@Getter @Setter private Location lastlocation;
	private long lastKeepAlive = -1L;
	@Getter @Setter private int lastPing = 0;
	@Getter @Setter private int averagePing = 0;
	private int posPackets = 0;
	private double deltaXZ = 0D;
	private double deltaY = 0D;
	private boolean placedBlock = false;
	private long tookVelocity = 0L;
	private Checks lastCheckSetOff;
	@Getter @Setter private Entity lastHitPlayer;
	@Getter @Setter private int blockTicks = 0;
	@Getter @Setter private double realFallDistance = 0D;
	@Getter @Setter private long lastBow = 0L;
	@Getter @Setter private long lastAttack = 0L;
	@Getter @Setter private Location setbackLocation;
	@Getter @Setter private double lastScaffoldYaw = -1D;
	@Getter @Setter private double veryLastScaffoldYaw = 1D;
	@Getter @Setter private double lastScaffoldPitch = -1D;
	@Getter @Setter private double veryLastScaffoldPitch = 1D;
	@Getter @Setter private long lastBlockPlace = 0L;
	@Getter @Setter private Block blockPlaced;
	@Getter @Setter private Block lastBlockPlaced;
	@Getter @Setter private double rotation = 0D;
	private int leftClicks;
	private int rightClicks;
	@Getter @Setter private int packetsFromLag = 0;
	@Getter @Setter private int clickLevel = 0;
	@Getter @Setter private double lastLastYawDif;
	@Getter @Setter private boolean hasAdvancedAlerts = false;
	@Getter @Setter private double lastYaw = 1.0D;
	@Getter @Setter private int clickFreg = 0;
	@Getter @Setter private int clickFreg2 = 0;
	@Getter @Setter private Map<Double, Integer> clickIntervals = new HashMap<Double, Integer>();
	@Getter @Setter private Map<Double, Integer> clickIntervals2 = new HashMap<Double, Integer>();
    @Getter @Setter private int ratioAttackAmount = 0;
    @Getter @Setter private int ratioAttackLevel = 0;
	@Getter @Setter private long attackTime = 0;
	@Getter @Setter private double lastPitch = 1.0D;
	@Getter @Setter private int maxCps = 0;
	@Getter @Setter private int globalClicks = 0;
	@Getter @Setter private boolean bigPitch = false;
	@Getter @Setter private int bodyPositive2 = 0;
	@Getter @Setter private int intervalAmount = 0;
	@Getter @Setter private long swingTime = 0;
	@Getter @Setter private int globalClicksAmount = 0;
	@Getter @Setter private int ratioAttack = 0;
	@Getter @Setter private int lastSum = 0;
	@Getter @Setter private long lastInvClick = 0L;
	@Getter @Setter private long lastInvClickDifference = 0L;
	@Getter @Setter private Pattern pattern;
	@Getter @Setter private double reachVL = 0;
	@Getter @Setter private List<Double> clickDifferences = new ArrayList<Double>();
	@Getter @Setter boolean addDiff = true;
	@Getter @Setter private int highestCps = -1;
	@Getter @Setter private boolean collectingData = false;
	@Getter @Setter private int lowestCps = -1;
	@Getter @Setter private double lastDiff = 0.0;
	@Getter @Setter private int cpsOscillationTime = 0;
	@Getter @Setter private List<CustomLocation> last20MovePackets = new ArrayList<CustomLocation>();
	@Getter @Setter private int cpsOscillationLevel = 0;
	@Getter @Setter private Location getLastLocation;
	@Getter @Setter private long lastAimB;
	@Getter @Setter private boolean inventoryOpen = false;
	@Getter @Setter private int hits;
	@Getter @Setter private double deltaXZ2;
	@Getter @Setter private double lastDifference;
	@Getter @Setter private int swings;
	@Getter @Setter private double deltaY2;
	@Getter @Setter private double lastPitchDifference = 0.0D;
	@Getter @Setter private int clicks = 0;
	@Getter @Setter private Player lastPlayer = null;
	@Getter @Setter private double totalClickTime = 0.0;
	@Getter @Setter private double yawOffset = 0.0D;
	@Getter @Setter private double lastYawOffset = 0.0D;
	@Getter @Setter private long lastPacketHit = 0L;
	@Getter @Setter private Location lastLocation;
	@Getter @Setter private long lastFlyPacket = System.currentTimeMillis();
	@Getter @Setter private long lastPosPacket = System.currentTimeMillis();
	@Getter @Setter private long lastPosLookPacket = System.currentTimeMillis();
	@Getter @Setter private ListMultimap<UUID, CustomLocation> playerCustomLocationListMultiMap = ArrayListMultimap.create();
	public User(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		this.vl = new HashMap<Checks, Integer>();
	}
	
	public long getLastFlyPacket() {
		return lastFlyPacket;
	}

	public void setLastFlyPacket(long lastFlyPacket) {
		this.lastFlyPacket = lastFlyPacket;
	}

	public double getYawOffset() {
		return yawOffset;
	}

	public void setYawOffset(double yawOffset) {
		this.yawOffset = yawOffset;
	}

	public double getLastYawOffset() {
		return lastYawOffset;
	}

	public void setLastYawOffset(double lastYawOffset) {
		this.lastYawOffset = lastYawOffset;
	}
	
	public void setDefenderLocation(Location loc) {
		this.getLastLocation = loc;
	}
	
	public Location getDefenderLocation() {
		return getLastLocation;
	}
	
    public void addPlayerLocation(UUID uuid, CustomLocation customLocation) {
        if (this.playerCustomLocationListMultiMap.containsKey(uuid) && this.playerCustomLocationListMultiMap.get(uuid).size() >= 20) {
            this.playerCustomLocationListMultiMap.get(uuid).remove(0);
        }
        this.playerCustomLocationListMultiMap.put(uuid, customLocation);
    }
	
	public Player getLastPlayer() {
		return lastPlayer;
	}

	public void setLastPlayer(Player lastPlayer) {
		this.lastPlayer = lastPlayer;
	}
	
	public int getSwings() {
		return swings;
	}
	
	public void setSwings(int swings) {
		this.swings = swings;
	}

	public Location getLastlocation() {
		return lastlocation;
	}

	public void setLastlocation(Location lastlocation) {
		this.lastlocation = lastlocation;
	}

	public int getHits() {
		return hits;
	}
	
	public boolean isInventoryOpen() {
		return inventoryOpen;
	}

	public void setInventoryOpen(boolean inventoryOpen) {
		this.inventoryOpen = inventoryOpen;
	}
	
	public long getLastPosPacket() {
		return lastPosPacket;
	}

	public void setLastPosPacket(long lastPosPacket) {
		this.lastPosPacket = lastPosPacket;
	}

	public long getLastPosLookPacket() {
		return lastPosLookPacket;
	}

	public void setLastPosLookPacket(long lastPosLookPacket) {
		this.lastPosLookPacket = lastPosLookPacket;
	}

	public long getLastInvClick() {
		return lastInvClick;
	}

	public void setLastInvClick(long lastInvClick) {
		this.lastInvClick = lastInvClick;
	}

	public long getLastInvClickDifference() {
		return lastInvClickDifference;
	}

	public void setLastInvClickDifference(long lastInvClickDifference) {
		this.lastInvClickDifference = lastInvClickDifference;
	}

	public void addHit() {
		hits++;
	}

	public void resetHits() {
		hits = 0;
	}
	
    public long getLastPacketHit() {
		return lastPacketHit;
	}

	public void setLastPacketHit(long lastPacketHit) {
		this.lastPacketHit = lastPacketHit;
	}

	public CustomLocation getLastPlayerLocation(UUID player, int i) {
        List<CustomLocation> customLocations = this.playerCustomLocationListMultiMap.get(player);
        if (customLocations == null || customLocations.size() <= i - 1) {
            return null;
        }
        return customLocations.get(customLocations.size() - i);
    }
    
    public CustomLocation getLastMovePacket(int i) {
        if (this.last20MovePackets.size() <= i - 1) {
            return null;
        }
        return this.last20MovePackets.get(this.last20MovePackets.size() - i);
    }

	
	public long getMovePacketAverage() {
        ArrayList<Long> diffs = new ArrayList<Long>();
        for (int i = 0; i < this.last20MovePackets.size(); ++i) {
            long FIRST_DIFF = this.last20MovePackets.get(i).getTimeStamp();
            if (this.last20MovePackets.size() == i + 1) continue;
            diffs.add(this.last20MovePackets.get(i + 1).getTimeStamp() - FIRST_DIFF);
            ++i;
        }
        long average = 0;
        Iterator iterator = diffs.iterator();
        while (iterator.hasNext()) {
            long g = (Long)iterator.next();
            average += g;
        }
        return average / (long)this.last20MovePackets.size();
    }

    public void addMovePacket(CustomLocation customLocation) {
        if (this.last20MovePackets.size() >= 20) {
            this.last20MovePackets.remove(0);
        }
        this.last20MovePackets.add(customLocation);
    }

	public int getPacketsFromLag() {
		return packetsFromLag;
	}

	public void setPacketsFromLag(int packetsFromLag) {
		this.packetsFromLag = packetsFromLag;
	}

	public Pattern getPattern() {
		return this.pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public void setClickLevel(int number) {
		this.clickLevel = number;
	}

	public int getClickLevel() {
		return this.clickLevel;
	}
	
	public void setDeltaXZ2(double xz) {
		this.deltaXZ2 = xz;
	}
	
	public double getDeltaXZ2() {
		return this.deltaXZ2;
	}
	
	public void setDeltaY2(double y) {
		this.deltaY2 = y;
	}
	
	public double getDeltaY2() {
		return this.deltaY2;
	}
	
	public void setLastLastYawDifference(double yawdif) {
		this.lastLastYawDif = yawdif;
	}
	
	public double getLastLastYawDifference() {
		return this.lastLastYawDif;
	}

	public long getTookVelocity() {
		return tookVelocity;
	}
	
	public double getLastDifference() {
		return lastDifference;
	}

	public void setLastDifference(double lastDifference) {
		this.lastDifference = lastDifference;
	}
	
	public boolean isCollectingData() {
		return collectingData;
	}

	public void setCollectingData(boolean collectingData) {
		this.collectingData = collectingData;
	}

	public void setGlobalClicks(int number) {
		this.globalClicks = number;
	}

	public int getGlobalClicks() {
		return this.globalClicks;
	}

	public void setGlobalClicksAmount(int number) {
		this.globalClicksAmount = number;
	}

	public int getGlobalClicksAmount() {
		return this.globalClicksAmount;
	}
	
	public long getTeleported() {
		return teleported;
	}
	
	public void setClickFreq(int freg) {
		this.clickFreg = freg;
	}
	
	public void setClickFreq2(int freg) {
		this.clickFreg2 = freg;
	}

	public int getClickFreq() {
		return this.clickFreg;
	}
	
	public int getClickFreq2() {
		return this.clickFreg2;
	}
	public void setBodyPositive(int num) {
		this.bodyPositive2 = num;
	}
	
	public int getBodyPositive() {
		return this.bodyPositive2;
	}

	public void setClicks(int number) {
		this.clicks = number;
	}

	public int getClicks() {
		return this.clicks;
	}

	public void setTotalClickTime(double number) {
		this.totalClickTime = number;
	}

	public double getTotalClickTime() {
		return this.totalClickTime;
	}
	
	public void setReachVL(double vl) {
		reachVL = vl;
	}
	
	public double getReachVL() {
		return reachVL;
	}

	public int getHighestCps() {
		return this.highestCps;
	}

	public void setHighestCps(int highestCps) {
		this.highestCps = highestCps;
	}

	public int getLowestCps() {
		return this.lowestCps;
	}

	public void setLowestCps(int lowestCps) {
		this.lowestCps = lowestCps;
	}

	public void setMaxCps(int number) {
		this.maxCps = number;
	}

	public int getMaxCps() {
		return this.maxCps;
	}
	
	public int getCpsOscillationTime() {
		return this.cpsOscillationTime;
	}

	public void setCpsOscillationTime(int cpsOscillationTime) {
		this.cpsOscillationTime = cpsOscillationTime;
	}
	
    public void setIntervalAmount(int number) {
        this.intervalAmount = number;
    }

    public int getIntervalAmount() {
        return this.intervalAmount;
    }
    
    public void addClickDiff(double number) {
        this.clickDifferences.add(number);
    }

    public List<Double> getClicksDiffs() {
        return this.clickDifferences;
    }

    public void clearClickDiffs() {
        this.clickDifferences.clear();
    }
    
    public void setAddDiff(boolean bool) {
        this.addDiff = bool;
    }

    public boolean getAddDiff() {
        return this.addDiff;
    }

	public int getCpsOscillationLevel() {
		return this.cpsOscillationLevel;
	}
	
    public void setLastSum(int number) {
        this.lastSum = number;
    }

    public int getLastSum() {
        return this.lastSum;
    }

	public Location getLastLocation() {
		if(lastlocation == null) {
			return player.getLocation();
		}
		return lastlocation;
	}

	public void setLastLocation(Location lastlocation) {
		this.lastlocation = lastlocation;
	}

	public void setCpsOscillationLevel(int cpsOscillationLevel) {
		this.cpsOscillationLevel = cpsOscillationLevel;
	}
	
    public void setSwingTime(long number) {
        this.swingTime = number;
    }

    public long getSwingTime() {
        return this.swingTime;
    }
	
    public void setAttackTime(long number) {
        this.attackTime = number;
    }

    public long getAttackTime() {
        return this.attackTime;
    }

	/**
	 * 
	 * @param yawDifference
	 */

	public void setLastYawDifference(double yawDifference) {
		this.lastYawDifference = yawDifference;
	}

	/**
	 * 
	 * @return lastYawDifference
	 */

	public double getLastYawDifference() {
		return this.lastYawDifference;
	}
	
    public void setBigPitch(boolean bool) {
        this.bigPitch = bool;
    }

    public boolean getBigPitch() {
        return this.bigPitch;
    }

	/**
	 * 
	 * @param pitch
	 */

	public void setLastPitch(double pitch) {
		this.lastPitch = pitch;
	}

	/**
	 * 
	 * @return lastPitch
	 */

	public double getLastPitch() {
		return this.lastPitch;
	}
	
    public void setLastDiff(double number) {
        this.lastDiff = number;
    }
    
    public int getRatioAttack() {
        return this.ratioAttack;
    }

    public void setRatioAttack(int ratioAttack) {
        this.ratioAttack = ratioAttack;
    }

    public int getRatioAttackAmount() {
        return this.ratioAttackAmount;
    }

    public void setRatioAttackAmount(int ratioAttackAmount) {
        this.ratioAttackAmount = ratioAttackAmount;
    }

    public int getRatioAttackLevel() {
        return this.ratioAttackLevel;
    }

    public void setRatioAttackLevel(int ratioAttackLevel) {
        this.ratioAttackLevel = ratioAttackLevel;
    }

    public double getLastDiff() {
        return this.lastDiff;
    }
  
    public void addInterval(double key) {
        if (this.clickIntervals.containsKey(key)) {
            this.clickIntervals.put(key, this.clickIntervals.get(key) + 1);
        } else {
            this.clickIntervals.put(key, 1);
        }
    }
    
    public Map<Double, Integer> getIntervals() {
        return this.clickIntervals;
    }

    public void addInterval2(double key) {
        if (this.clickIntervals2.containsKey(key)) {
            this.clickIntervals2.put(key, this.clickIntervals2.get(key) + 1);
        } else {
            this.clickIntervals2.put(key, 1);
        }
    }

    public Map<Double, Integer> getIntervals2() {
        return this.clickIntervals2;
    }

	/**
	 * 
	 * @param pitchDifference
	 */

	public void setLastPitchDifference(double pitchDifference) {
		this.lastPitchDifference = pitchDifference;
	}

	/**
	 * 
	 * @return lastPitchDifference
	 */

	public double getLastPitchDifference() {
		return this.lastPitchDifference;
	}

	/**
	 * 
	 * @param millis
	 */

	public void setLastAimB(long millis) {
		this.lastAimB = millis;
	}

	/**
	 * 
	 * @return lastAimB
	 */

	public long getLastAimB() {
		return this.lastAimB;
	}
	public Player getPlayer() {
		return player;
	}

	public boolean isStaff() {
		if (this.player.hasPermission("Keaton.staff") || this.player.isOp()) {
			return true;
		}
		return false;

	}
	
	/**
	 * 
	 * @param pitch
	 */
	
	public void setLastPitchAimC(double pitch) {
		this.lastPitchAimC = pitch;
	}
	
	/**
	 * 
	 * @return lastPitch
	 */
	
	public double getLastPitchAimC() {
		return this.lastPitchAimC;
	}

	public UUID getUUID() {
		return uuid;
	}

	public int getVL(Checks check) {
		return vl.getOrDefault(check, 0);
	}

	public void setVL(Checks check, int vl) {
		this.vl.put(check, vl);
	}

	public Map<Checks, Integer> getVLs() {
		return this.vl;
	}

	public boolean needBan(Checks check) {
		return getVL(check) > check.getWeight();
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public long getLastAttack() {
		return lastAttack;
	}

	public void setLastAttack(long lastAttack) {
		this.lastAttack = lastAttack;
	}

	public int clearVL(Checks check) {
		return getVLs().put(check, 0);
	}

	public int getAveragePing() {
		return averagePing;
	}

	public void setAveragePing(int averagePing) {
		this.averagePing = averagePing;
	}

	public void clearData() {
		this.player = null;
		this.uuid = null;
		this.vl.clear();
		;
		setAirTicks(0);
		setGroundTicks(0);
		setIceTicks(0);
		setRightClicks(0);
		setLeftClicks(0);
	}
	
	public int getLastPing() {
		return lastPing;
	}

	public void setLastPing(int lastPing) {
		this.lastPing = lastPing;
	}

	public double getLastScaffoldYaw() {
		return lastScaffoldYaw;
	}

	public void setLastScaffoldYaw(double lastScaffoldYaw) {
		this.lastScaffoldYaw = lastScaffoldYaw;
	}

	public double getVeryLastScaffoldYaw() {
		return veryLastScaffoldYaw;
	}

	public void setVeryLastScaffoldYaw(double veryLastScaffoldYaw) {
		this.veryLastScaffoldYaw = veryLastScaffoldYaw;
	}

	public double getLastScaffoldPitch() {
		return lastScaffoldPitch;
	}

	public void setLastScaffoldPitch(double lastScaffoldPitch) {
		this.lastScaffoldPitch = lastScaffoldPitch;
	}

	public double getVeryLastScaffoldPitch() {
		return veryLastScaffoldPitch;
	}

	public void setVeryLastScaffoldPitch(double veryLastScaffoldPitch) {
		this.veryLastScaffoldPitch = veryLastScaffoldPitch;
	}

	public Block getBlockPlaced() {
		return blockPlaced;
	}

	public void setBlockPlaced(Block blockPlaced) {
		this.blockPlaced = blockPlaced;
	}

	public Block getLastBlockPlaced() {
		return lastBlockPlaced;
	}

	public void setLastBlockPlaced(Block lastBlockPlaced) {
		this.lastBlockPlaced = lastBlockPlaced;
	}

	/**
	 * 
	 * @return placedBlock
	 */
	
	public boolean placedBlock() {
		return this.placedBlock;
	}
	
	/**
	 * 
	 * @param placedBlock
	 */
	
	public void setPlacedBlock(boolean placedBlock) {
		this.placedBlock = placedBlock;
	}
	
	public void addUsePackets() {
		usePackets++;
	}
	
	public void resetUsePackets() {
		usePackets = 0;
	}
	
	public long getLastServerKeepAlive() {
		return lastServerKeepAlive;
	}

	public void setLastServerKeepAlive(long lastServerKeepAlive) {
		this.lastServerKeepAlive = lastServerKeepAlive;
	}

	public long getLastClientKeepAlive() {
		return lastClientKeepAlive;
	}

	public void setLastClientKeepAlive(long lastClientKeepAlive) {
		this.lastClientKeepAlive = lastClientKeepAlive;
	}

	public int getUsePackets() {
		return usePackets;
	}
	
	public long getLastPacket() {
		return lastPacket;
	}
	
	public void setLastPacket(long millis) {
		lastPacket = millis;
	}
	
	public void addSwingPackets() {
		swingPackets++;
	}
	
	public void resetSwingPackets() {
		swingPackets = 0;
	}
	
	public int getSwingPackets() {
		return swingPackets;
	}
	
	public double getRealFallDistance() {
		return realFallDistance;
	}

	public void setRealFallDistance(double realFallDistance) {
		this.realFallDistance = realFallDistance;
	}

	public void setLastMove(long millis) {
		lastMove = millis;
	}
	
	public long getLastMove() {
		return lastMove;
	}
	
	public long getLastBlockPlace() {
		return lastBlockPlace;
	}

	public void setLastBlockPlace(long lastBlockPlace) {
		this.lastBlockPlace = lastBlockPlace;
	}

	public Entity getLastHitPlayer() {
		return lastHitPlayer;
	}

	public void setLastHitPlayer(Entity lastHitPlayer) {
		this.lastHitPlayer = lastHitPlayer;
	}

	public void setLastMoveTime(long millis) {
		lastMoveTime = millis;
	}
	
	public long getLastMoveTime() {
		return lastMoveTime;
	}
	
	public long getLastFlightChange() {
		return lastFlightChange;
	}
	
	public void setLastFlightChange(long millis) {
		lastFlightChange = millis;
	}
	
	public int getPosPackets() {
		return posPackets;
	}
	
	public void setPosPacket(int number) {
		posPackets = number;
	}
	
	/**
	 * 
	 * @return Checks
	 */
	
	public Checks getLastCheck() {
		return this.lastCheckSetOff;
	}
	
	/**
	 * 
	 * @param check
	 */
	
	public void setLastCheck(Checks check) {
		this.lastCheckSetOff = check;
	}
	
	public int getBlockTicks() {
		return blockTicks;
	}

	public void setBlockTicks(int blockTicks) {
		this.blockTicks = blockTicks;
	}

	public Location getSetbackLocation() {
		return setbackLocation;
	}

	public void setSetbackLocation(Location setbackLocation) {
		this.setbackLocation = setbackLocation;
	}

	/**
	 * 
	 * @return lastPotionSplash
	 */
	
	public long getLastPotionSplash() {
		return this.lastPotionSplash;
	}
	
	/**
	 * 
	 * @param millis
	 */
	
	public void setLastPotionSplash(long millis) {
		this.lastPotionSplash = millis;
	}

	/**
	 * @return the airTicks
	 */
	public int getAirTicks() {
		return AirTicks;
	}

	/**
	 * @param airTicks
	 *            the airTicks to set
	 */
	public void setAirTicks(int airTicks) {
		AirTicks = airTicks;
	}
	
	/**
	 * 
	 * @param millis
	 */
	
	public void setLoginMillis(long millis) {
		this.loginMillis = millis;
	}
	
	/**
	 * 
	 * @return loginMillis
	 */
	
	public long getLoginMIllis() {
		return this.loginMillis;
	}
	
	public void setDeltaXZ(double offset) {
		deltaXZ = offset;
	}
	
	public double getDeltaXZ() {
		return deltaXZ;
	}
	
	public void setDeltaY(double offset) {
		deltaY = offset;
	}
	
	public double getDeltaY() {
		return deltaY;
	}
	
	/**
	 * 
	 * @param millis
	 */
	
	public void setLastHeal(long millis) {
		this.lastHeal = millis;
	}
	
	/**
	 * 
	 * @return lastHeal
	 */
	public long getLastHeal() {
		return this.lastHeal;
	}
	
	/**
	 * 
	 * @param teleported
	 */
	public void setTeleported(long teleported) {
		this.teleported = teleported;
	}
	
	/**
	 * 
	 * @return teleported
	 */
	
	public long isTeleported()  {
		return this.teleported;
	}
	
	public long getLastBow() {
		return lastBow;
	}

	public void setLastBow(long lastBow) {
		this.lastBow = lastBow;
	}

	/**
	 * 
	 * @return tookVelocity;
	 */
	
	public long isVelocity() {
		return this.tookVelocity;
	}
	
	public void addToList(String string) {
		this.logList.add(string);
	}
	
	public void clearList() {
		this.logList.clear();
	}
	
	public ArrayList<String> getList() {
		return this.logList;
	}
	
	/**
	 * 
	 * @param took
	 */
	
	public void setTookVelocity(long took) {
		this.tookVelocity = took;
	}
	
	/**
	 * 
	 * @param yaw
	 */
	
	public void setLastYaw(double yaw) {
		lastYawDifference = yaw;
	}
	
	/**
	 * 
	 * @return yaw
	 */
	
	public double getLastYaw() {
		return lastYawDifference;
	}
	
	public void setLastKeepAlive(long millis) {
		lastKeepAlive = millis;
	}
	
	public long lastKeepAlive() {
		return lastKeepAlive;
	}
	
	/**
	 * 
	 * @param pitchDifference
	 */
	
	public void setLastPitchDifferenceAimC(double pitchDifference) {
		this.lastPitchDifferenceAimC = pitchDifference;
	}
	
	/**
	 * 
	 * @return lastPitchDifference
	 */
	
	public double getLastPitchDifferenceAimC() {
		return this.lastPitchDifferenceAimC;
	}

	/**
	 * @return the groundTicks
	 */
	public int getGroundTicks() {
		return GroundTicks;
	}
	
	/**
	 * 
	 * @param check
	 * @param cancelled
	 */
	
	public void setCancelled(Checks check, CancelType type) {
		if(check == null) {
			this.isCancelled = type;
			return;
		}
		if(Keaton.getAC().getConfig().getBoolean("checks." + check.getName() + ".cancelled")) {
			this.isCancelled = type;
		}
	}
	
	/**
	 * 
	 * @return isCancelled
	 */
	
	public CancelType isCancelled() {
		return this.isCancelled;
	}
	
	/**
	 * 
	 * @return isHit
	 */
	
	public long isHit() {
		return this.isHit;
	}
	
	/**
	 * 
	 * @param isHit
	 */
	
	public void setIsHit(long isHit) {
		this.isHit = isHit;
	}

	/**
	 * @param groundTicks
	 *            the groundTicks to set
	 */
	public void setGroundTicks(int groundTicks) {
		GroundTicks = groundTicks;
	}

	/**
	 * @return the iceTicks
	 */
	public int getIceTicks() {
		return IceTicks;
	}

	/**
	 * @param iceTicks
	 *            the iceTicks to set
	 */
	public void setIceTicks(int iceTicks) {
		IceTicks = iceTicks;
	}

	/**
	 * @return the hasAlerts
	 */
	public boolean isHasAlerts() {
		return hasAlerts;
	}

	/**
	 * @param hasAlerts
	 *            the hasAlerts to set
	 */
	public void setHasAlerts(boolean hasAlerts) {
		this.hasAlerts = hasAlerts;
	}

	/**
	 * @return the leftClicks
	 */
	public int getLeftClicks() {
		return leftClicks;
	}

	/**
	 * @param leftClicks
	 *            the leftClicks to set
	 */
	public void setLeftClicks(int leftClicks) {
		this.leftClicks = leftClicks;
	}

	/**
	 * @return the rightClicks
	 */
	public int getRightClicks() {
		return rightClicks;
	}

	/**
	 * @param rightClicks
	 *            the rightClicks to set
	 */
	public void setRightClicks(int rightClicks) {
		this.rightClicks = rightClicks;
	}

	/**
	 * @return the inva
	 */
	public int getInva() {
		return inva;
	}

	/**
	 * @param inva the inva to set
	 */
	public void setInva(int inva) {
		this.inva = inva;
	}

	/**
	 * @return the invb
	 */
	public int getInvb() {
		return invb;
	}

	/**
	 * @param invb the invb to set
	 */
	public void setInvb(int invb) {
		this.invb = invb;
	}

	/**
	 * @return the hasSwung
	 */
	public boolean isHasSwung() {
		return hasSwung;
	}

	/**
	 * @param hasSwung the hasSwung to set
	 */
	public void setHasSwung(boolean hasSwung) {
		this.hasSwung = hasSwung;
		
		if(hasSwung) {
			this.lastSwing = System.currentTimeMillis();
		}
	}
	
	public long getLastSwing() {
		return this.lastSwing;
	}


}
