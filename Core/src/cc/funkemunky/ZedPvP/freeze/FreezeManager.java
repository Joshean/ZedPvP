package cc.funkemunky.ZedPvP.freeze;

import java.util.UUID;

import cc.funkemunky.ZedPvP.Core;

public class FreezeManager {
    public static void unfreeze(UUID uuid) {
        Core.getInstance().getFrozen().remove(uuid);
    }

    public static void freeze(UUID uuid) {
        Core.getInstance().getFrozen().add(uuid);
    }

    public static boolean isFrozen(UUID uuid) {
        return Core.getInstance().getFrozen().contains(uuid);
    }
} 