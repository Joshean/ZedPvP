package anticheat.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import anticheat.Keaton;

public class Messages {
	
	public String LOG_SAVING = getPrefix("%prefix%&cSaving logs for all online users...");
	public String LOG_SAVED = getPrefix("%prefix%&aSaved!");
	public String RELOADING_CONFIG = getPrefix("%prefix%&cReloading config...");
	public String RELOADING_CHECKS = getPrefix("%prefix%&cReloading checks...");
	public String RELOADING_VIOLATIONS = getPrefix("%prefix%&cResetting violations...");
	public String RELOADING_PLUGIN = getPrefix("%prefix%&cReloading plugin...");
	public String RELOADING_PARTLY_DONE = getPrefix("%prefix%&eDone!");
	public String RELOADING_DONE = getPrefix("%prefix%&aSuccessfully reloaded Keaton!");
	public String NO_PERMISSION = getPrefix("&cNo permission.");
	public String INVALID_ARGUMENT = getPrefix("%prefix%&cInvalid argument(s).");
	public List<String> Keaton_STATUS = new ArrayList<String>(Arrays.asList(new String[] {"", "&8&m----------------------------------", "&6&lKeaton Status:",
			"", "&eTPS: &c%tps%", "", "&eSilent Checks: &7%notbannable%", "&eBannable Checks: &7%bannable%", 
			"&8&m----------------------------------", ""}));
	public String SET_BANNABLE = getPrefix("%prefix%&c%check% bannable state has been set to %state%");
	public String SET_TOGGLE = getPrefix("%prefix%&c%check% state has been set to %state%");
	public String ALERTS_JOIN = getPrefix("%prefix%&7&oToggled your alerts on automatically. Do &a/Keaton alerts &7&oto toggle them.");
	public List<String> VERBOSE_CHECKS = new ArrayList<String>(Arrays.asList(new String[] { "&8&m--------------------------------------------",
			"&6&l%player%'s Violations/Info", "","&7Ping: &f%ping%", "", "&c&lSet off:", "", "%violations%",
			"&8&m--------------------------------------------"}));
	public String VERBOSE_NOCHECKS = getPrefix("&cThis player set off no checks!");
	public String ALERTS_TOGGLE = getPrefix("%prefix%&cAlerts state set to %state%&c.");
	
	public Messages(Keaton Keaton) {	
		LOG_SAVING = getPrefix(Keaton.getMessages().getString("LOG_SAVING"));
		LOG_SAVED = getPrefix(Keaton.getMessages().getString("LOG_SAVED"));
		RELOADING_CONFIG = getPrefix(Keaton.getMessages().getString("RELOADING_CONFIG"));
		RELOADING_CHECKS = getPrefix(Keaton.getMessages().getString("RELOADING_CHECKS"));
		RELOADING_VIOLATIONS = getPrefix(Keaton.getMessages().getString("RELOADING_VIOLATIONS"));
		RELOADING_PLUGIN = getPrefix(Keaton.getMessages().getString("RELOADING_PLUGIN"));
		RELOADING_PARTLY_DONE = getPrefix(Keaton.getMessages().getString("RELOADING_PARTLY_DONE"));
		RELOADING_DONE = getPrefix(Keaton.getMessages().getString("RELOADING_DONE"));
		NO_PERMISSION = getPrefix(Keaton.getMessages().getString("NO_PERMISSION"));
		INVALID_ARGUMENT = getPrefix(Keaton.getMessages().getString("INVALID_ARGUMENT"));
		Keaton_STATUS = Keaton.getMessages().getStringList("Keaton_STATUS");
		SET_BANNABLE = getPrefix(Keaton.getMessages().getString("SET_BANNABLE"));
		SET_TOGGLE = getPrefix(Keaton.getMessages().getString("SET_TOGGLE"));
		ALERTS_JOIN = getPrefix(Keaton.getMessages().getString("ALERTS_JOIN"));
		ALERTS_TOGGLE = getPrefix(Keaton.getMessages().getString("ALERTS_TOGGLE"));
	}
	
	private String getPrefix(String string) {
		return Color.translate(string.replaceAll("%prefix%", Keaton.getAC().getPrefix()));
	}

}
