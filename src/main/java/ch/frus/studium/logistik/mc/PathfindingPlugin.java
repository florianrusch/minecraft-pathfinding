package ch.frus.studium.logistik.mc;

import ch.frus.studium.logistik.mc.algorithms.AStar;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class PathfindingPlugin extends JavaPlugin {
//    private final Logger log = getLogger();
    private final Pathfinder pathfinder = new Pathfinder();
    private final PathfindingListener pathfindingListener = new PathfindingListener(pathfinder);
    private final PathfindingCommands pathfindingCommands = new PathfindingCommands(pathfinder);

    @Override
    public void onEnable() {
        getCommand("astar").setExecutor(this.pathfindingCommands);
        getServer().getPluginManager().registerEvents(this.pathfindingListener, this);
        getLogger().info("onEnable has been invoked!");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this.pathfindingListener);
        getLogger().info("onDisable has been invoked!");
    }
}
