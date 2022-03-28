package tk.yjservers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Mutton implements Listener {

    private ItemStack mutton;

    @EventHandler
    public void onSheepKilled(EntityDeathEvent e) {
        if (e instanceof Sheep) {
            Sheep sheep = (Sheep) e;
            if (sheep.isAdult()) {
                e.getDrops().add(getMutton(sheep.getKiller().getItemInHand()));
            }
        }
    }

    private ItemStack getMutton(ItemStack weapon) {
        int lootingLevel = weapon.getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_MOBS, 0);
        Random random = new Random();
        ItemStack item = new ItemStack(Material.RAW_BEEF);
        // An adult sheep drops 1–2 raw mutton when killed. The maximum amount is increased by 1 per level of Looting, for a maximum of 1–5 with Looting III.
        // https://minecraft.fandom.com/wiki/Raw_Mutton
        item.setAmount(random.nextInt(1 + lootingLevel) + 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Raw Mutton");
        item.setItemMeta(meta);
        return item;
    }
}
