package anticheat.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Base64;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class MathUtils {

	public static boolean elapsed(long from, long required) {
		return System.currentTimeMillis() - from > required;
	}

	public static long elapsed(long starttime) {
		return System.currentTimeMillis() - starttime;
	}

	public static double trim(int degree, double d) {
		String format = "#.#";
		for (int i = 1; i < degree; ++i) {
			format = String.valueOf(format) + "#";
		}
		DecimalFormat twoDForm = new DecimalFormat(format);
		return Double.valueOf(twoDForm.format(d).replaceAll(",", "."));
	}

	public static double frac(double value) {
		return value % 1.0;
	}

	public static float[] getAimRotations(Player player, Entity entity) {
		double x = entity.getLocation().getX() - player.getLocation().getX();
		double z = entity.getLocation().getZ() - player.getLocation().getZ();
		double y = entity.getLocation().getY() + (2.0 - 0.4D) - player.getLocation().getY()
				+ (player.getEyeHeight() - 0.4D);
		double helper = Math.sqrt(x * x + z * z);

		float newYaw = (float) Math.toDegrees(-Math.atan(x / z));
		float newPitch = (float) -Math.toDegrees(Math.atan(y / helper));
		if ((z < 0.0D) && (x < 0.0D)) {
			newYaw = (float) (90.0D + Math.toDegrees(Math.atan(z / x)));
		} else if ((z < 0.0D) && (x > 0.0D)) {
			newYaw = (float) (-90.0D + Math.toDegrees(Math.atan(z / x)));
		}
		return new float[] { newPitch, newYaw };
	}
	
	  public static float getAimRotation(float currentRotation, float targetRotation, float maxIncrement)
	  {
	    float deltaAngle = yawTo180F(targetRotation - currentRotation);
	    if (deltaAngle > maxIncrement) {
	      deltaAngle = maxIncrement;
	    }
	    if (deltaAngle < -maxIncrement) {
	      deltaAngle = -maxIncrement;
	    }
	    return currentRotation + deltaAngle / 2.0F;
	  }

	public static float[] getRotations(Location one, Location two) {
		double diffX = two.getX() - one.getX();
		double diffZ = two.getZ() - one.getZ();
		double diffY = (two.getY() + 2.0D - 0.4D) - (one.getY() + 2.0D);
		double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

		return new float[] { yaw, pitch };
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public static double roundDown(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.DOWN);
		return bd.doubleValue();
	}

	public static String decrypt(String strEncrypted) {
		String strData = "";

		try {
			byte[] decoded = Base64.getDecoder().decode(strEncrypted);
			strData = (new String(decoded, "UTF-8") + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	public static double getDistanceBetweenAngles(float angle1, float angle2) {
		float distance = Math.abs(angle1 - angle2) % 360.0f;
		if (distance > 180.0f) {
			distance = 360.0f - distance;
		}
		return distance;
	}

	public static double offset(final Vector one, final Vector two) {
		return one.subtract(two).length();
	}

	public static double getHorizontalDistance(Location to, Location from) {
		double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
		double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));

		return Math.sqrt(x * x + z * z);
	}

	public static float[] getRotations(Player player, Entity entity) {
		if (entity == null) {
			return null;
		}
		double diffX = entity.getLocation().getX() - player.getLocation().getX();
		double diffZ = entity.getLocation().getZ() - player.getLocation().getZ();
		double diffY = (entity.getLocation().getY() + 2.0 - 0.4D)
				- (player.getLocation().getY() + player.getEyeHeight());
		double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

		return new float[] { yaw, pitch };
	}

	public static float getYawChangeToEntity(Player player, Entity entity, Location from, Location to) {
		double deltaX = entity.getLocation().getX() - player.getLocation().getX();
		double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
		double yawToEntity;
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			} else {
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
			}
		}
		return Float.valueOf((float) (-(getYawDifference(from, to)) - yawToEntity));
	}

	public static float getPitchChangeToEntity(Player player, Entity entity, Location from, Location to) {
		double deltaX = entity.getLocation().getX() - player.getLocation().getX();
		double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
		double deltaY = player.getLocation().getY() - 1.6D + 2 - 0.4D - entity.getLocation().getY();
		double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));

		return yawTo180F((float) (-(getYawDifference(from, to) - (float) pitchToEntity)));
	}

	public static double fixedXAxis(double x) {
		double touchedX = x;
		double rem = touchedX - Math.round(touchedX) + 0.01D;
		if (rem < 0.3D) {
			touchedX = NumberConversions.floor(x) - 1;
		}
		return touchedX;
	}

	public static Vector calculateAngle(Vector player, Vector otherPlayer) {
		Vector delta = new Vector((player.getX() - otherPlayer.getX()), (player.getY() - otherPlayer.getY()),
				(player.getZ() - otherPlayer.getZ()));
		double hyp = Math.sqrt(delta.getX() * delta.getX() + delta.getY() * delta.getY());

		Vector angle = new Vector(Math.atan((delta.getZ() + 64.06) / hyp) * 57.295779513082,
				Math.atan((delta.getY() / delta.getX())) * 57.295779513082, 0.0D);
		if (delta.getX() >= 0.0) {
			angle.setY(angle.getY() + 180.0);
		}
		return angle;
	}

	public static Vector getRotation(Location one, Location two) {
		double dx = two.getX() - one.getX();
		double dy = two.getY() - one.getY();
		double dz = two.getZ() - one.getZ();
		double distanceXZ = Math.sqrt(dx * dx + dz * dz);
		float yaw = (float) (Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793);
		return new Vector(yaw, pitch, 0.0f);
	}

	public static float[] getAngles(Player player, Entity e, Location from, Location to) {
		return new float[] { (float) (getYawChangeToEntity(player, e, from, to) + getYawDifference(from, to)),
				(float) (getPitchChangeToEntity(player, e, from, to) + getYawDifference(from, to)) };
	}

	public static double getYawDifference(Location one, Location two) {
		return Math.abs(one.getYaw() - two.getYaw());
	}

	public static double getVerticalDistance(Location to, Location from) {
		double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));

		return Math.sqrt(y * y);
	}

	public static double getYDifference(Location to, Location from) {
		return Math.abs(to.getY() - from.getY());
	}

	public static Vector getHorizontalVector(Location loc) {
		Vector vec = loc.toVector();
		vec.setY(0);
		return vec;
	}

	public static Vector getVerticalVector(Location loc) {
		Vector vec = loc.toVector();
		vec.setX(0);
		vec.setZ(0);
		return vec;
	}

	public static float yawTo180F(float flub) {
		flub %= 360.0F;
		if (flub >= 180.0F) {
			flub -= 360.0F;
		}
		if (flub < -180.0F) {
			flub += 360.0F;
		}
		return flub;
	}

	public static double yawTo180D(double dub) {
		dub %= 360.0D;
		if (dub >= 180.0D) {
			dub -= 360.0D;
		}
		if (dub < -180.0D) {
			dub += 360.0D;
		}
		return dub;
	}
}
