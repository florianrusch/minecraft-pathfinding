package ch.frus.studium.logistik.mc;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Utils {
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static String getLocationString(Location loc) {
        return String.format(
                "(%s, %s, %s)",
                loc.getX(),
                loc.getY(),
                loc.getZ()
        );
    }

    public static String getLocationString(Block b) {
        return String.format(
                "(%s, %s, %s)",
                b.getLocation().getX(),
                b.getLocation().getY(),
                b.getLocation().getZ()
        );
    }
}
