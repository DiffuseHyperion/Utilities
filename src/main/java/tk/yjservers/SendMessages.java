package tk.yjservers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static tk.yjservers.Utilities.config;

public class SendMessages implements Listener {

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Bukkit.getLogger().info("sendBefMessages: " + p.getName());
        if (config.getBoolean("Messages.befenabled")) {
            for (String s : config.getStringList("Messages.befmessages")) {
                p.sendMessage(s);
            }
        }
    }

    public void sendAftMessages(Player p) {
        Bukkit.getLogger().info("sendAftMessages: " + p.getName());
        if (config.getBoolean("Messages.aftenabled")) {
            for (String s : config.getStringList("Messages.aftmessages")) {
                p.sendMessage(s);
            }
        }
    }
}
