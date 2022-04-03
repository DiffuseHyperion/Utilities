package tk.yjservers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Bed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static tk.yjservers.Utilities.config;

public class DaytimeSleep implements Listener {

    public boolean isBed(Block b) {
        return b.getType().equals(Material.BED_BLOCK);
    }

    public boolean anyHostileNearby(Player p) {
        List<Entity> list = p.getNearbyEntities(4, 2.5, 4);
        ArrayList<EntityType> typelist = new ArrayList<>();
        for (Entity e : list) {
            typelist.add(e.getType());
        }
        EntityType[] hostilelist = {EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.PIG_ZOMBIE, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SPIDER, EntityType.WITCH, EntityType.WITHER};
        return !Collections.disjoint(typelist, Arrays.asList(hostilelist));
    }

    // i know there is loc.add and loc.subtract, but they all feel wonky af
    public Location changeLoc(Location target, int x, int y, int z){
        return new Location(target.getWorld(), target.getBlockX() + x, target.getBlockY() + y, target.getBlockZ() + z);
    }

    @EventHandler
    public void onPlayerInteractBed(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();
        Block b = e.getClickedBlock();
        if (a.equals(Action.RIGHT_CLICK_BLOCK)
                && isBed(b)
                && b.getLocation().distanceSquared(p.getLocation()) < config.getInt("DaytimeSleep.maxdistancefrombed")
                && b.getWorld().getEnvironment().equals(World.Environment.NORMAL)
                && !anyHostileNearby(p)) {

            Bed bed = (Bed) b.getState().getData();
            Location headloc;
            Location tailloc;

            if (bed.isHeadOfBed()) {
                headloc = b.getLocation();
                tailloc = b.getRelative(bed.getFacing().getOppositeFace()).getLocation();
            } else {
                tailloc = b.getLocation();
                headloc = b.getRelative(bed.getFacing()).getLocation();
            }


            // number represents the order of spawning
            ArrayList<Location> locs = new ArrayList<>();
            // adjacent to headloc (0-6)
            locs.add(changeLoc(headloc, 1, 0, -1));
            locs.add(changeLoc(headloc, -1, 0, 0));
            locs.add(changeLoc(headloc, -1, 0, 1));
            locs.add(changeLoc(headloc, 0, 0, -1));
            locs.add(changeLoc(headloc, 1, 0, -1));
            locs.add(changeLoc(headloc, 1, 0, 0));
            locs.add(changeLoc(headloc, 1, 0, 1));

            // adjacent to feetloc (7-9)
            locs.add(changeLoc(tailloc, -1, 0, 1));
            locs.add(changeLoc(tailloc, 0, 0, 1));
            locs.add(changeLoc(tailloc, 1, 0, 1));

            // on bed (10-11)
            locs.add(changeLoc(headloc, 0, 1, 0));
            locs.add(changeLoc(tailloc, 0, 1, 0));

            for (Location loc : locs) {
                Material below = changeLoc(loc, 0, -1, 0).getBlock().getType();
                Material above = changeLoc(loc, 0, 1, 0).getBlock().getType();
                Material origin = loc.getBlock().getType();
                if (locs.indexOf(loc) < 10) {
                    if (below.isSolid() && origin.equals(Material.AIR) && above.equals(Material.AIR)) {
                        p.setBedSpawnLocation(loc);
                        p.sendMessage("Your spawn has been set to " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                        return;
                    }
                } else if (locs.indexOf(loc) == 10){
                    if ((below.isSolid() ||  below.equals(Material.BED_BLOCK)) && origin.equals(Material.AIR) && above.equals(Material.AIR)) {
                        p.setBedSpawnLocation(loc);
                        p.sendMessage("Your spawn has been set to " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                        return;
                    }
                } else {
                    // if all else fails, default to loc 11
                    p.setBedSpawnLocation(loc);
                    p.sendMessage("Your spawn has been set to " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                }
            }
        }
    }
}
