
package tk.yjservers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Barrier implements Listener {

    List<Location> list;

    public Barrier(List<Location> blocklist) {
        list = blocklist;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (Location b : list) {
            if (!(b.getBlock().getType() == Material.AIR)) {
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().sendBlockChange(b, Material.AIR, (byte) 0);
                    }
                };
                task.runTaskLater(Bukkit.getPluginManager().getPlugin("Utilities"), 2);
            }
        }
    }

    @EventHandler
    public void onPlayerRightClickBarrier(PlayerInteractEvent e) {
        if (list.contains(e.getClickedBlock().getLocation())) {
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().sendBlockChange(e.getClickedBlock().getLocation(), Material.AIR, (byte) 0);
                }
            };
            task.runTaskLater(Bukkit.getPluginManager().getPlugin("Utilities"), 2);
        }
    }
}
