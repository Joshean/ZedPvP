package anticheat.detections;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;

import anticheat.Keaton;
import anticheat.checks.combat.AimPattern;
import anticheat.checks.combat.AutoClicker;
import anticheat.checks.combat.Criticals;
import anticheat.checks.combat.Fastbow;
import anticheat.checks.combat.KillAuraA;
import anticheat.checks.combat.Reach;
import anticheat.checks.combat.Regen;
import anticheat.checks.movement.Fly;
import anticheat.checks.movement.GroundSpoof;
import anticheat.checks.movement.Jesus;
import anticheat.checks.movement.Phase;
import anticheat.checks.movement.Speed;
import anticheat.checks.movement.Vclip;
import anticheat.checks.movement.Velocity;
import anticheat.checks.other.Exploit;
import anticheat.checks.other.ImpossiblePitch;
import anticheat.checks.other.Timer;

public class ChecksManager {

	public static List<Checks> detections = new ArrayList<>();

	public ChecksManager(Keaton ac) {
	}

	public List<Checks> getDetections() {
		return detections;
	}

	public Checks getCheckByName(String name) {
		for (Checks check : getDetections()) {
			if (check.getName().equalsIgnoreCase(name)) {
				return check;
			}
		}
		return null;
	}

	// TODO: Init all your checks here.
	public void init() {
		new Reach();
		new Speed();
		new KillAuraA();
		new Vclip();
		new Jesus();
		new Criticals();
		new Fly();
		new Exploit();
		new GroundSpoof();
		new Fastbow();
		new Regen();
		new Phase();
		new ImpossiblePitch();
		new Timer();
		new AutoClicker();
		new Velocity();
		new AimPattern();
	}

	public void event(Event event) {
		for (int i = 0; i < detections.size(); i++) {
			Checks detection = detections.get(i);
			Class<? extends Checks> clazz = detection.getClass();
			if (clazz.isAnnotationPresent(ChecksListener.class)) {
				Annotation annotation = clazz.getAnnotation(ChecksListener.class);
				ChecksListener handler = (ChecksListener) annotation;
				for (Class<?> type : handler.events()) {
					if (type == event.getClass()) {
						detection.onEvent(event);
					}
				}
			}
		}
	}
}
