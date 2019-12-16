package ch.frus.studium.logistik.mc;

import ch.frus.studium.logistik.mc.algorithms.AStar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ch.frus.studium.logistik.mc.Utils.getLocationString;
import static org.bukkit.Bukkit.getLogger;

public class Pathfinder {
    private final Logger log = getLogger();

    private static HashMap<Player, Block> startSelections = new HashMap<>();
    private static HashMap<Player, Block> endSelections = new HashMap<>();

    private final Material pathFirstMaterial = Material.GOLD_BLOCK;
    private final Material pathMaterial = Material.LAPIS_BLOCK;
    private final Material pathLastMaterial = Material.COAL_BLOCK;

    Pathfinder() {
    }

    void addStartSelection(Player p, Block b) {
        startSelections.put(p, b);
    }

    boolean hasStartSelection(Player p) {
        return startSelections.containsKey(p);
    }

    private Block getStartSelection(Player p) {
        return startSelections.get(p);
    }

    private void removeStartSelection(Player p) {
        startSelections.remove(p);
    }

    void addEndSelection(Player p, Block b) {
        endSelections.put(p, b);
    }

    private Block getEndSelection(Player p) {
        return endSelections.get(p);
    }

    private void removeEndSelection(Player p) {
        endSelections.remove(p);
    }

    boolean hasSelection(Player p) {
        return endSelections.containsKey(p) && startSelections.containsKey(p);
    }

    void removeSelection(Player p) {
        this.removeStartSelection(p);
        this.removeEndSelection(p);
    }

    boolean astar(Player p) throws InterruptedException {
        if (this.hasSelection(p)) {
            p.sendMessage(ChatColor.GREEN + "Start A* algorithm");

            AStar aStar = new AStar(this.getStartSelection(p), this.getEndSelection(p));
            ArrayList<Block> path = null;

            try {
                path = aStar.run();
            } catch (Exception e) {
                p.sendMessage(ChatColor.RED + e.getMessage());
                this.log.log(Level.SEVERE, e.getMessage(), e);
            }

            assert path != null;
            path.forEach((Block b) -> {
                this.log.info(getLocationString(b.getLocation()));
                this.printPath(b);
            });

//            printFirstPath(path.get(0));
//            printLastPath(path.get(path.size()-1));

            return true;
        } else {
            p.sendMessage(ChatColor.RED + "No start/end selection found.");
        }
        return false;
    }

    private void printFirstPath(Block b) {
        b.setType(pathFirstMaterial);
    }

    private void printPath(Block b) {
        b.setType(pathMaterial);
    }

    private void printLastPath(Block b) {
        b.setType(pathLastMaterial);
    }
}
