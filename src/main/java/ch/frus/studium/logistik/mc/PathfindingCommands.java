package ch.frus.studium.logistik.mc;

import ch.frus.studium.logistik.mc.algorithms.AStar;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class PathfindingCommands implements CommandExecutor {

    private final Logger log = getLogger();
    private final Pathfinder pathfinder;

    PathfindingCommands(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        log.info("onCommand");
        log.info(cmd.getName());

        Player player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("astar")){
            return this.pathfinder.astar(player);
        }
        return false;
    }
}
