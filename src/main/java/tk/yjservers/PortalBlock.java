package tk.yjservers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PortalBlock implements CommandExecutor {

    public static ItemStack portal;

    public PortalBlock() {
        init();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            ((Player) commandSender).getInventory().addItem(portal);
        } else {
            commandSender.sendMessage("This command can only be executed by a player!");
        }
        return true;
    }

    private void init() {
        ItemStack item = new ItemStack(Material.ENDER_PORTAL);
        ItemMeta meta = item.getItemMeta();
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GOLD + "looks pretty drippy");
        list.add(ChatColor.GRAY + "Players will teleport to the end if they touch this!");
        meta.setLore(list);
        item.setItemMeta(meta);
        portal = item;
    }


}
