package anticheat.utils;

public class CustomLocation {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private long timeStamp;

    public CustomLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.timeStamp = System.currentTimeMillis();
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }
}


