package tk.yjservers.Login;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tk.yjservers.SendMessages;
import tk.yjservers.SetCamAndPos;

import java.util.ArrayList;
import java.util.List;

import static tk.yjservers.Utilities.config;
import static tk.yjservers.Utilities.passwords;

public class Login implements CommandExecutor, Listener{

    public static List<Player> notlogined = new ArrayList<>();
    SetCamAndPos scap = new SetCamAndPos();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String pname = p.getName();
        p.removePotionEffect(PotionEffectType.BLINDNESS);
        p.removePotionEffect(PotionEffectType.JUMP);
        p.removePotionEffect(PotionEffectType.SPEED);
        if (config.getBoolean("Login.required")) {
            notlogined.add(p);
            if (config.getBoolean("Login.blind.beforelogin")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, -1, true));
            }
            scap.tpPlayer(p);
            if (passwords.containsKey(pname)){
                p.sendMessage(ChatColor.RED + "Please login to this account! Type /login (Your password)");
            } else {
                p.sendMessage(ChatColor.RED + "Please register this account! Type /register (Your password), then login with /login (Your password)");
            }
        } else if (passwords.containsKey(pname)){
            notlogined.add(p);
            if (config.getBoolean("Login.blind.beforelogin")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, -1, true));
            }
            p.sendMessage(ChatColor.RED + "Please login to this account! Type /login (Your password)");
            scap.tpPlayer(p);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if (notlogined.contains(p)) {
            scap.tpPlayer(p);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            String pname = p.getName();
            if (notlogined.contains(p)) {
                if (passwords.containsKey(pname)) {
                    if (strings.length == 1) {
                        if (passwords.get(pname).equals(strings[0])) {
                            notlogined.remove(p);
                            if (!config.getBoolean("Login.blind.afterlogin")) {
                                p.removePotionEffect(PotionEffectType.BLINDNESS);
                            }
                            p.sendMessage(ChatColor.GREEN + "You have logged in!");
                            new SendMessages().sendMessages(p);
                        } else {
                            p.sendMessage(ChatColor.RED + "Incorrect password!");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Malformed command! Do /login (Your password)");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You have no account registered! Do /register to create one.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You are already logged in!");
            }
        } else {
            commandSender.sendMessage("This command can only be executed by a player!");
        }
        return true;
    }
}
