package tk.yjservers;

import net.minecraft.server.v1_5_R3.NBTTagCompound;
import net.minecraft.server.v1_5_R3.NBTTagString;
import org.bukkit.Bukkit;
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
    public Mutton() {
    }

    @EventHandler
    public void onSheepKilled(EntityDeathEvent e) {
        Bukkit.getLogger().info("entity died");
        if (e instanceof Sheep) {
            Bukkit.getLogger().info("is sheep");
            Sheep sheep = (Sheep)e;
            if (sheep.isAdult()) {
                e.getDrops().add(this.getRawMutton(sheep.getKiller().getItemInHand()));
            }
        }

    }

    @EventHandler
    public void onMuttonSmelt(FurnaceSmeltEvent e) {
        Bukkit.getLogger().info("smelted");
        ItemStack source = e.getSource();
        net.minecraft.server.v1_5_R3.ItemStack nmsSource = CraftItemStack.asNMSCopy(source);
        NBTTagCompound sourcecompound = nmsSource.hasTag() ? nmsSource.getTag() : new NBTTagCompound();

        try {
            if (sourcecompound.getString("Mutton").equals("raw")) {
                e.setResult(this.getCookedMutton(source.getAmount()));
            }
        } catch (NullPointerException ignore) {
        }

    }

    @EventHandler
    public void onMuttonEat(PlayerItemConsumeEvent e) {
        Bukkit.getLogger().info("consumed");
        ItemStack source = e.getItem();
        net.minecraft.server.v1_5_R3.ItemStack nmsSource = CraftItemStack.asNMSCopy(source);
        NBTTagCompound sourcecompound = nmsSource.hasTag() ? nmsSource.getTag() : new NBTTagCompound();
        Player p = e.getPlayer();

        try {
            if (sourcecompound.getString("Mutton").equals("raw")) {
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
    }

    private ItemStack getRawMutton(ItemStack weapon) {
        ItemStack item = new ItemStack(Material.RAW_BEEF);

        int lootingLevel = weapon.getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_MOBS, 0);
        Random random = new Random();
        item.setAmount(random.nextInt(lootingLevel + 2) + 1);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Raw Mutton");
        item.setItemMeta(meta);

        net.minecraft.server.v1_5_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound itemcompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        itemcompound.set("Mutton", new NBTTagString("raw"));
        nmsItem.setTag(itemcompound);

        item = CraftItemStack.asBukkitCopy(nmsItem);
        return item;
    }

    private ItemStack getCookedMutton(int quantity) {
        ItemStack item = new ItemStack(Material.COOKED_BEEF);

        item.setAmount(quantity);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Cooked Mutton");
        item.setItemMeta(meta);

        net.minecraft.server.v1_5_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound itemcompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        itemcompound.set("Mutton", new NBTTagString("cooked"));
        nmsItem.setTag(itemcompound);

        item = CraftItemStack.asBukkitCopy(nmsItem);
        return item;
    }
}