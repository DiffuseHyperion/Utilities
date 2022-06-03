package tk.yjservers;

import net.minecraft.server.v1_5_R3.NBTTagCompound;
import net.minecraft.server.v1_5_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Mutton implements Listener {

    private final ItemStack rawMutton;
    public Mutton() {
        ItemStack item = new ItemStack(Material.RAW_BEEF);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Raw Mutton");
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);

        rawMutton = setNBTTag(item, "mutton", "raw");
    }

    @EventHandler
    public void onSheepKilled(EntityDeathEvent e) {
        if (e.getEntity() instanceof Sheep) {
            Sheep sheep = (Sheep) e.getEntity();
            if (sheep.isAdult()) {
                ItemStack mutton = rawMutton;
                setMuttonNumber(sheep.getKiller().getItemInHand(), mutton);
                e.getDrops().add(mutton);
            }
        }

    }

    @EventHandler
    public void onMuttonSmelt(FurnaceSmeltEvent e) {
    }

    @EventHandler
    public void onMuttonEat(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        ItemStack consumed = e.getItem();
        /*
        try {
            if (consumed.getItemMeta().equals(rawMutton.getItemMeta())) {
                e.setCancelled(true);
                p.getInventory().remove(e.getItem());
                p.setFoodLevel(p.getFoodLevel() + 2);
                p.setSaturation((float)((double)p.getSaturation() + 1.2D));
            } else if (sourcecompound.getString("Mutton").equals("cooked")) {
                e.setCancelled(true);
                p.getInventory().remove(e.getItem());
                p.setFoodLevel(p.getFoodLevel() + 6);
                p.setSaturation((float)((double)p.getSaturation() + 9.6D));
            }
        } catch (NullPointerException ignore) {
        }

         */
        if (getNBTTag(consumed, "mutton").equals("raw")) {
            p.sendMessage("eaten raw mutton");
        }
    }

    private void setMuttonNumber(ItemStack weapon, ItemStack item) {
        int lootingLevel = weapon.getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_MOBS, 0);
        Random random = new Random();
        item.setAmount(random.nextInt(lootingLevel + 2) + 1);
    }

    public net.minecraft.server.v1_5_R3.ItemStack getNMSItem(ItemStack item) {
        return CraftItemStack.asNMSCopy(item);
    }

    public NBTTagCompound getCompound(ItemStack item) {
        net.minecraft.server.v1_5_R3.ItemStack nmsItem = getNMSItem(item);
        return (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
    }

    public ItemStack setNBTTag(ItemStack item, String tag, String value) {
        net.minecraft.server.v1_5_R3.ItemStack nmsItem = getNMSItem(item);
        NBTTagCompound itemcompound = getCompound(item);

        itemcompound.set(tag, new NBTTagString(value));
        nmsItem.setTag(itemcompound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public String getNBTTag(ItemStack item, String tag) {
        NBTTagCompound itemcompound = getCompound(item);

        return itemcompound.getString(tag);
    }
}