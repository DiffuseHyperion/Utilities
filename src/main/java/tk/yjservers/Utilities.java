package tk.yjservers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import tk.yjservers.Login.Login;
import tk.yjservers.Login.Register;
import tk.yjservers.Login.Unregister;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utilities extends JavaPlugin{

    public static FileConfiguration config;

    public static FileConfiguration accountConfig;
    public static File accountFile;
    public static HashMap<String, String> passwords = new HashMap<>();

    public static FileConfiguration spawnConfig;
    public static File spawnFile;
    public static List<Location> spawns = new ArrayList<>();


    public void onEnable() {
        setConfig();
        init();
    }

    public void onDisable() {

    }

    private void setConfig() {
        this.saveDefaultConfig();
        config = getConfig();
    }

    private void init() {
        getCommand("endportal").setExecutor(new PortalBlock());
        if (config.getBoolean("SetPlayerCameraAndPos.enabled")) {
            getLogger().info("SetPlayerCameraAndPos has been enabled!");
            getServer().getPluginManager().registerEvents(new SetCamAndPos(), this);
        }
        if (config.getBoolean("StopBreakBlock.enabled")) {
            getLogger().info("StopBreakBlock has been enabled!");
            getServer().getPluginManager().registerEvents(new StopBreakBlock(), this);
        }
        if (config.getBoolean("FullHunger.enabled")) {
            getLogger().info("FullHunger has been enabled!");
            getServer().getPluginManager().registerEvents(new FullHunger(), this);
        }
        if (config.getBoolean("Login.enabled")) {
            getLogger().info("Login has been enabled!");
            getServer().getPluginManager().registerEvents(new Login(), this);
            getCommand("login").setExecutor(new Login());
            getCommand("register").setExecutor(new Register());
            getCommand("unregister").setExecutor(new Unregister());
        }
        if (config.getBoolean("Barrier.enabled")) {
            getLogger().info("Barrier has been enabled!");
            List <Location> list = new ArrayList<>();
            for (String s : config.getStringList("Barrier.list")) {
                String[] strlist = s.split(", ");
                int x = Integer.parseInt(strlist[0]);
                int y = Integer.parseInt(strlist[1]);
                int z = Integer.parseInt(strlist[2]);
                World w = Bukkit.getWorld(strlist[3]);
                if (w != null) {
                    list.add(new Location(w, x, y, z));
                } else {
                    getLogger().warning("A block in Barrier.list has an invalid world name! Its coordinates are: " + x + ", " + y + ", " + z);
                }
            }
            getServer().getPluginManager().registerEvents(new Barrier(list), this);
        }
        if (config.getBoolean("Messages.enabled")) {
            getLogger().info("Messages has been enabled!");
            getServer().getPluginManager().registerEvents(new SendMessages(), this);
        }

        accountFile = getFile("accounts.yml");
        accountConfig = getConfig(accountFile);
        if (accountConfig.getKeys(false).isEmpty()) {
            Bukkit.getLogger().warning("No accounts found in accounts.yml, this may be an error!");
        } else {
            for (String s : accountConfig.getKeys(false)) {
                String password = accountConfig.getString(s + ".password");
                passwords.put(s, password);
                getLogger().info("Registering: " + s);
            }
            getLogger().info("Accounts loaded!");
        }

        // KEEP THIS AT THE END OF THE METHOD
        spawnFile = getFile("spawns.yml");
        spawnConfig = getConfig(spawnFile);
        if (spawnConfig.getKeys(false).isEmpty()) {
            Bukkit.getLogger().warning("No signs found in spawns.yml, this may be an error!");
        } else {
            for (String s: spawnConfig.getKeys(false)) {
                if (getServer().getWorld(s) == null) {
                    getLogger().severe("Something went wrong while reading data.yml, was it changed manually?");
                    getLogger().severe("Check if there is a section called: " + s + ", by using CTRL+F with your editor.");
                    getLogger().severe("SetWorldSpawn section will not enable, to prevent further errors.");
                    return;
                }
                World w = getServer().getWorld(s);
                int x = spawnConfig.getInt(s + ".x");
                int y = spawnConfig.getInt(s + ".y");
                int z = spawnConfig.getInt(s + ".z");

                spawns.add(new Location(w, x, y, z));
            }
        }
        getCommand("setworldspawn").setExecutor(new SetWorldSpawn());
        getServer().getPluginManager().registerEvents(new SetWorldSpawn(), this);
    }

    private FileConfiguration getConfig(File datafile) {
        //load dataFile
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(datafile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }
    
    private File getFile(String filename) {
        File file = new File(getDataFolder(), filename);
        if (file.exists()) {
            file.getParentFile().mkdirs();
            saveResource(filename, true);
        } 
        return file;
    }
}
