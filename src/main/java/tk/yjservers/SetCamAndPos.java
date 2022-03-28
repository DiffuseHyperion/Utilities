package tk.yjservers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static tk.yjservers.Utilities.config;

public class SetCamAndPos implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        tpPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        // prevent any wack conflict between these, login takes precedence, cuz thats arguably more important
        if (!config.getBoolean("Login.freezeatcampos")) {
            if (config.getString("SetPlayerCameraAndPos.tponjoin").equals("all")) {
                tpPlayer(p);
            } else if (config.getString("SetPlayerCameraAndPos.tponjoin").equals("newcomer") && !p.hasPlayedBefore()) {
                tpPlayer(p);
            }
        }
    }

    public void tpPlayer(Player p) {
        double x = config.getDouble("SetPlayerCameraAndPos.location.x");
        double y = config.getDouble("SetPlayerCameraAndPos.location.y");
        double z = config.getDouble("SetPlayerCameraAndPos.location.z");
        float pitch = Float.parseFloat(config.getString("SetPlayerCameraAndPos.location.pitch"));
        float yaw = Float.parseFloat(config.getString("SetPlayerCameraAndPos.location.yaw"));
        Location loc =  new Location(p.getWorld(), x + 0.50, y, z + 0.50, yaw, pitch);
        p.teleport(loc);
    }
}
