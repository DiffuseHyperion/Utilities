package tk.yjservers.Login;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

import static tk.yjservers.Login.Login.notlogined;
import static tk.yjservers.Utilities.*;

public class Register implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            String pname = p.getName();
            if (!notlogined.contains(p)) {
                if (!passwords.containsKey(pname)) {
                    if (strings.length == 1) {
                        passwords.put(pname, strings[0]);
                        accountConfig.createSection(pname);
                        accountConfig.createSection(pname + ".password");
                        accountConfig.set(pname + ".password", strings[0]);
                        try {
                            accountConfig.save(accountFile);
                            p.sendMessage(ChatColor.GREEN + "Account registered! Your future password is: " + ChatColor.GOLD + strings[0]);
                        } catch (IOException e) {
                            p.sendMessage(ChatColor.GRAY + "Something went wrong while saving your password. Contact this server's admins. Error has been printed to console.");
                            e.printStackTrace();
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Malformed command! Do /register (Your password)");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You already have an account! Do /unregister to remove your account.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Login first before doing this command.");
            }
        } else {
            commandSender.sendMessage("This command can only be executed by a player!");
        }
        return true;
    }
}
