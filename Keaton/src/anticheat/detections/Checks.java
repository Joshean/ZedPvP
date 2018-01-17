package anticheat.detections;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import anticheat.Keaton;
import anticheat.utils.Color;
import anticheat.utils.JsonMessage;

public class Checks {

	public static Keaton ac;
	public ChecksType type;
	private String name;
	private boolean state;
	private boolean bannable;
	public static ArrayList<String> playersToBan = new ArrayList<>();
	private long delay = -1;
	private long interval = 1000;

	private int weight;

	public Checks(String name, ChecksType type, Keaton ac, Integer weight, boolean state, boolean bannable) {
		this.name = name;
		Checks.ac = ac;
		this.type = type;
		this.weight = weight;
		this.bannable = bannable;
		this.state = state;
		ac.getChecks();
		Keaton.getAC().getChecks().getDetections().add(this);
	}

	public int getWeight() {
		return weight;
	}

	public boolean isBannable() {
		return this.bannable;
	}

	public void setBannable(boolean bannable) {
		this.bannable = bannable;
	}

	public void debug(String string) {
		Bukkit.broadcastMessage(Color.Aqua + "DEBUG: " + string);
	}

	public ChecksType getType() {
		return type;
	}

	public boolean getState() {
		return this.state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void toggle() {
		this.setState(!this.state);
	}

	public void toggleBans() {
		this.setBannable(!this.bannable);
	}

	public String getName() {
		return name;
	}

	protected void onEvent(Event event) {
		// Nothing here for a reason.
	}

	public void Alert(Player p, String value) {
		if (!Keaton.getAC().getConfig().getBoolean("debug")) {
			long l = System.currentTimeMillis() - this.delay;
			if (l > this.interval) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (Keaton.getUserManager().getUser(player.getUniqueId()).isHasAlerts()
							&& (player.isOp() || player.hasPermission("Keaton.staff"))) {
						JsonMessage msg = new JsonMessage();
						Keaton.getAC();
						msg.addText(Color.translate(Keaton.getAC().getMessages().getString("Alert_Message")
								.replaceAll("%prefix%", Keaton.getAC().getPrefix()).replaceAll("%player%", p.getName())
								.replaceAll("%check%", getName().toUpperCase()).replaceAll("%info%", value)
								.replaceAll("%violations%",
										String.valueOf(Keaton.getUserManager().getUser(p.getUniqueId()).getVL(this)))))
								.addHoverText(Color.Gray + "Teleport to " + p.getName() + "?")
								.setClickEvent(JsonMessage.ClickableType.RunCommand, "/tp " + p.getName());
						if (!Keaton.getAC().getConfig().getBoolean("bungee")) {
							msg.sendToPlayer(player);
						} else {
							ByteArrayOutputStream b = new ByteArrayOutputStream();
							DataOutputStream out = new DataOutputStream(b);
							try {
								out.writeUTF(player.getName());
								out.writeUTF(this.getName());
								out.writeUTF(value);
								out.writeUTF(String.valueOf(Keaton.getUserManager().getUser(p.getUniqueId()).getVL(this)));
							} catch (IOException e) {
								e.printStackTrace();
							}

							Bukkit.getServer().sendPluginMessage(Keaton.getAC(), "KeatonAlerts", b.toByteArray());
						}
					}
				}
				this.delay = System.currentTimeMillis();

			}
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (Keaton.getUserManager().getUser(player.getUniqueId()).isHasAlerts()
						&& (player.isOp() || player.hasPermission("Keaton.staff"))) {
					JsonMessage msg = new JsonMessage();
					Keaton.getAC();
					msg.addText(Color.translate(Keaton.getAC().getMessages().getString("Alert_Message")
							.replaceAll("%prefix%", Keaton.getAC().getPrefix()).replaceAll("%player%", p.getName())
							.replaceAll("%check%", getName().toUpperCase()).replaceAll("%info%", value)
							.replaceAll("%violations%",
									String.valueOf(Keaton.getUserManager().getUser(p.getUniqueId()).getVL(this)))))
							.addHoverText(Color.Gray + "Teleport to " + p.getName() + "?")
							.setClickEvent(JsonMessage.ClickableType.RunCommand, "/tp " + p.getName());
					if (!Keaton.getAC().getConfig().getBoolean("bungee")) {
						msg.sendToPlayer(player);
					} else {
						ByteArrayOutputStream b = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(b);
						try {
							out.writeUTF(player.getName());
							out.writeUTF(this.getName());
							out.writeUTF(value);
							out.writeUTF(String.valueOf(Keaton.getUserManager().getUser(p.getUniqueId()).getVL(this)));
						} catch (IOException e) {
							e.printStackTrace();
						}

						Bukkit.getServer().sendPluginMessage(Keaton.getAC(), "KeatonAlerts", b.toByteArray());
					}
				}
			}
		}
		if (Keaton.getAC().getConfig().getBoolean("logs.enabled")) {
			if(!Keaton.getAC().getConfig().getBoolean("MySQL.Enabled")) {
				Keaton.getUserManager().getUser(p.getUniqueId())
				.addToList(p.getName() + " set off check: " + this.getName() + "(" + value + ")" + " ["
						+ Keaton.getUserManager().getUser(p.getUniqueId()).getVL(this) + " VL] TPS: "
						+ Keaton.getAC().getPing().getTPS() + " Ping: " + Keaton.getAC().getPing().getPing(p));
			} else {
				Keaton.getAC().getMySQL().addLog(p, p.getName() + " set off check: " + this.getName() + "(" + value + ")" + " ["
						+ Keaton.getUserManager().getUser(p.getUniqueId()).getVL(this) + " VL] TPS: "
						+ Keaton.getAC().getPing().getTPS() + " Ping: " + Keaton.getAC().getPing().getPing(p));
			}
		}
	}

	public void kick(Player p) {
		if (Keaton.getUserManager().getUser(p.getUniqueId()).needBan(this) && this.isBannable() && !p.isOp()
				&& !Keaton.getAC().playersBanned.contains(p)) {
			if (Keaton.getAC().getConfig().getBoolean("Punish_Broadcast.Enabled")) {
				Bukkit.broadcastMessage(Color.translate(Keaton.getAC().getMessages()
						.getString("Punish_Broadcast.Message").replaceAll("%player%", p.getName())
						.replaceAll("%check%", this.getName().toUpperCase())));
			}
			Keaton.getAC().playersBanned.add(p);
			if (Keaton.getAC().getConfig().getBoolean("logs.enabled")) {
				Keaton.getUserManager().getUser(p.getUniqueId()).addToList(p.getName() + " has been banned for: "
						+ this.getName() + " (" + Keaton.getUserManager().getUser(p.getUniqueId()).getVL(this) + " VL)");
				Keaton.getAC().writeInLog(Keaton.getUserManager().getUser(p.getUniqueId()));
			}
			Keaton.getAC().getServer().dispatchCommand(Keaton.getAC().getServer().getConsoleSender(),
					Color.translate(Keaton.getAC().getConfig().getString("Punish_Cmd")
							.replaceAll("%player%", p.getName()).replaceAll("%check%", this.getName().toUpperCase())));
		}
	}
}