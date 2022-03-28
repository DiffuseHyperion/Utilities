package tk.yjservers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tk.yjservers.Utilities.*;

public class SetWorldSpawn implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                Pattern pa = Pattern.compile("[0-9|~]+");
                for (String s : args) {
                    Matcher m = pa.matcher(s);
                    if (m.matches()) {
                        p.sendMessage(ChatColor.RED + "Unexpected character found! You can only use numbers, or tildes.");
                        return true;
                    }
                }
                int x;
                int y;
                int z;
                switch (args.length) {
                    case 3:
                        if (Objects.equals(args[0], "~")) {
                            x = (p).getLocation().getBlockX();
                        } else {
                            x = Integer.parseInt(args[0]);
                        }
                        if (Objects.equals(args[1], "~")) {
                            y = (p).getLocation().getBlockY();
                        } else {
                            y = Integer.parseInt(args[1]);
                        }
                        if (Objects.equals(args[2], "~")) {
                            z = (p).getLocation().getBlockZ();
                        } else {
                            z = Integer.parseInt(args[2]);
                        }
                        break;
                    case 0:
                        x = (p).getLocation().getBlockX();
                        y = (p).getLocation().getBlockY();
                        z = (p).getLocation().getBlockZ();
                        break;
                    default:
                        p.sendMessage(ChatColor.RED + "Not enough arguments! Either enter 3 integers or no arguments to use your location.");
                        return true;
                }

                Location loc = new Location(p.getWorld(), x, y, z);
                String worldname = loc.getWorld().getName();

                if (spawns.contains(loc)) {
                    p.sendMessage(ChatColor.YELLOW + "There is already a world spawn for this world. Overwriting the previous spawn.");
                } else {
                    spawnConfig.createSection(loc.getWorld().getName());
                    spawnConfig.createSection(worldname + ".x");
                    spawnConfig.createSection(worldname + ".y");
                    spawnConfig.createSection(worldname + ".z");

                }
                spawnConfig.set(worldname + ".x", loc.getBlockX());
                spawnConfig.set(worldname + ".y", loc.getBlockY());
                spawnConfig.set(worldname + ".z", loc.getBlockZ());

                spawns.add(loc);
                try {
                    spawnConfig.save(spawnFile);
                    p.sendMessage("Set this world spawn point to " + x + ", " + y + ", " + z);
                } catch (IOException e) {
                    e.printStackTrace();
                    p.sendMessage(ChatColor.RED + "Something went wrong while saving the file! The stack trace has been printed to the console.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You can only use this command in the overworld!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command is only executable by a player!");
        }
        return true;
    }

    @EventHandler
    private void onNewcomerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setBedSpawnLocation(getLoc(p.getWorld()));

        if (config.getString("teleport").equals("all")) {
            tpPlayer(p);
        } else {
            if (!p.hasPlayedBefore()) {
                //weird glitch that newcomers teleport to the void, this is a good enough workaround lmfao
                tpPlayer(p);
            }
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent e) {
        // doing this to allow players to spawn in the middle of a block instead of a corner
        if (!e.isBedSpawn()) {
            tpPlayer(e.getPlayer());
        }
    }

    private void tpPlayer(Player p) {
        Location loc = getLoc(p.getWorld());
        if (loc == null) {
            return;
        }

        p.teleport(new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    private Location getLoc(World w) {
        for (Location l : spawns) {
            if (l.getWorld().equals(w)) {
                return l;
            }
        }
        return null;
    }
}
