package tk.yjservers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DropSkull implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        // https://bukkit.org/threads/how-to-make-a-custom-player-head-into-a-custom-item.489590/

        ItemStack skull = new ItemStack(Material.SKULL); // Create a new ItemStack of the Player Head type.
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
        skullMeta.setOwner(e.getEntity().getDisplayName()); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
        skull.setItemMeta(skullMeta); // Apply the modified meta to the initial created item

        e.getEntity().getInventory().addItem(skull);
    }
}
