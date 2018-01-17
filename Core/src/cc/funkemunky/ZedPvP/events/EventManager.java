package cc.funkemunky.ZedPvP.events;

import cc.funkemunky.ZedPvP.events.koth.KothManager;

public class EventManager {
	
	KothManager kothManager;
	
	public EventManager() {
		kothManager = new KothManager();
	}
	
	public KothManager getKothManager() {
		return kothManager;
	}

}
