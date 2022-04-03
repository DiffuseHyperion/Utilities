package tk.yjservers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static tk.yjservers.Utilities.config;
import static tk.yjservers.Utilities.passwords;

public class SendMessages implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!passwords.containsKey(p.getName())) {
            sendMessages(p);
        }
    }

    public void sendMessages(Player p) {
        for (String s : config.getStringList("Messages.messages")) {
            p.sendMessage(s.replace("&", "§"));
        }
    }
}
