package ch.frus.studium.logistik.mc;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;
import java.util.logging.Logger;

import static ch.frus.studium.logistik.mc.Utils.getLocationString;
import static org.bukkit.Bukkit.getLogger;

public class PathfindingListener implements Listener {

    private final Logger log = getLogger();
    private final Pathfinder pathfinder;

    PathfindingListener(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    /**
     * Called when a player interacts
     *
     * @param event Relevant event details
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.DENY || event.getHand() == EquipmentSlot.OFF_HAND) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (Objects.requireNonNull(event.getItem()).getType().equals(Material.STICK)) {
                Player player = event.getPlayer();

                if (this.pathfinder.hasSelection(player)) {
                    player.sendMessage("Selection reseted");
                    log.info("Selection reseted for player \"" + player.getDisplayName() + "\"");
                    this.pathfinder.removeSelection(player);

                    event.setCancelled(true);
                    return;
                }

                if (this.pathfinder.hasStartSelection(player)) {
                    Block b = Objects.requireNonNull(event.getClickedBlock()).getRelative(0,1,0);
                    player.sendMessage("End block selected: " + getLocationString(b.getLocation()));
                    log.info("Selection update for player \"" + player.getDisplayName() + "\"");
                    log.info("End: " + getLocationString(b.getLocation()));
                    this.pathfinder.addEndSelection(player, b);

                    event.setCancelled(true);
                    return;
                }

                Block b = Objects.requireNonNull(event.getClickedBlock()).getRelative(0,1,0);
                player.sendMessage("Start block selected: " + getLocationString(b.getLocation()));
                log.info("Selection update for player \"" + player.getDisplayName() + "\"");
                log.info("Start: " + getLocationString(b.getLocation()));
                this.pathfinder.addStartSelection(player, b);

                event.setCancelled(true);
            }
        }
    }
}
