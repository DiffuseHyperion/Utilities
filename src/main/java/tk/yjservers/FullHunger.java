package tk.yjservers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FullHunger implements Listener {

    @EventHandler
    public void onPlayerLoseHunger(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
            ((Player) e.getEntity()).setFoodLevel(20);
        }
    }
}
