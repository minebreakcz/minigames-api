package net.graymadness.minigame_api.helper;

import org.bukkit.World;

public class WorldHelper {

    public static boolean isNight(World world) {
        return isNight(world.getTime());
    }

    public static boolean isDay(World world) {
        return isDay(world.getTime());
    }

    public static boolean isNight(long time) {
        return !isDay(time);
    }

    public static boolean isDay(long time) {
        return time <= 12541 || time >= 23458;
    }

    public static String worldTimeToString(World world) {
        long time = world.getTime();

        int hours = (int)((time / 1000 + 8) % 24);
        int minutes = (int)(60 * (time % 1000) / 1000);

        return String.format("%02d:%02d", hours, minutes);
    }

}
