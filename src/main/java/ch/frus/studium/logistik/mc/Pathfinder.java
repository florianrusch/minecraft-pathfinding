package ch.frus.studium.logistik.mc;

import ch.frus.studium.logistik.mc.algorithms.AStar;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class Pathfinder {
    private final Logger log = getLogger();

    private static HashMap<Player, Block> startSelections = new HashMap<Player, Block>();
    private static HashMap<Player, Block> endSelections = new HashMap<Player, Block>();

    Pathfinder() {
    }

    void addStartSelection(Player p, Block b) {
        startSelections.put(p, b);
    }

    boolean startSelectionsContainsKey(Player p) {
        return startSelections.containsKey(p);
    }

    Block getStartSelection(Player p) {
        return startSelections.get(p);
    }

    void addEndSelection(Player p, Block b) {
        endSelections.put(p, b);
    }

    Block getEndSelection(Player p) {
        return endSelections.get(p);
    }

    void removeEndSelection(Player p) {
        endSelections.remove(p);
    }

    boolean hasPlayerSelections(Player p) {
        return endSelections.containsKey(p) && startSelections.containsKey(p);
    }

    boolean astar(Player p) {
        if (this.hasPlayerSelections(p)) {
            p.sendMessage(ChatColor.GREEN + "Start A* algorithm");

            AStar aStar = new AStar(this.getStartSelection(p), this.getEndSelection(p));
            ArrayList<Block> path = aStar.run();

            path.forEach((Block b) -> log.info(b.toString()));

            return true;
        } else {
            p.sendMessage(ChatColor.RED + "No start/end selection found.");
        }
        return false;
    }
}
