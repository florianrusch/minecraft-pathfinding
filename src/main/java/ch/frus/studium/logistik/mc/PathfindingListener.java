package ch.frus.studium.logistik.mc;

import org.bukkit.Material;
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
        log.info("onPlayerInteract");

        if (event.useItemInHand() == Event.Result.DENY || event.getHand() == EquipmentSlot.OFF_HAND) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (Objects.requireNonNull(event.getItem()).getType().equals(Material.STICK)) {
                Player player = event.getPlayer();

                if (this.pathfinder.hasPlayerSelections(player)) {
                    this.pathfinder.removeEndSelection(player);
                } else if (this.pathfinder.startSelectionsContainsKey(player)) {
                    this.pathfinder.addEndSelection(player, Objects.requireNonNull(event.getClickedBlock()).getRelative(0,1,0));
                } else {
                    this.pathfinder.addStartSelection(player, Objects.requireNonNull(event.getClickedBlock()).getRelative(0,1,0));
                }

                log.info("Selection update - " + player.getDisplayName() + "(" + this.pathfinder.getStartSelection(player) + "<->" + this.pathfinder.getEndSelection(player) + ")");
                event.setCancelled(true);
            }
        }
    }
}
