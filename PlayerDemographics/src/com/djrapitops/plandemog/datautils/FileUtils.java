// This product includes GeoLite data created by MaxMind, available from http://www.maxmind.com
package com.djrapitops.plandemog.datautils;

import com.djrapitops.plandemog.PlanDemographics;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.bukkit.Bukkit.getPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class FileUtils {

    public static void newFile(UUID uuid) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        String fileName = uuid + ".yml";
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        File usersFolder = new File(plugin.getDataFolder() + File.separator + "users");
        File userFile = new File(plugin.getDataFolder() + File.separator + "users", fileName);
//        try {
        usersFolder.mkdirs();
//            userFile.createNewFile();
//        } catch (IOException e) {
//            plugin.logError("NEWFILE\n" + e+"\n"+plugin.getDataFolder()+File.separator+"users");
//        }
        reloadFile(userFile);
    }

    private static void reloadFile(File userFile) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(userFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("player.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            fileConfiguration.setDefaults(defConfig);
        }
        try {
            fileConfiguration.save(userFile);
        } catch (IOException ex) {
            plugin.logError("FILE-RELOAD\n" + ex);
        }
    }

    private static FileConfiguration getFileConfig(UUID uuid) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        String fileName = uuid + ".yml";
        File userFile = new File(plugin.getDataFolder() + File.separator + "users", fileName);
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(userFile);
        return fileConfiguration;
    }

    public static boolean fileExists(UUID uuid) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        String fileName = uuid + ".yml";
        File configFile = new File(plugin.getDataFolder() + File.separator + "users", fileName);
        return configFile.exists();
    }

    public static void OptOut(UUID uuid) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
        getFileConfig(uuid).set("opt-out", true);
    }

    public static void OptIn(UUID uuid) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
        getFileConfig(uuid).set("opt-out", false);
    }

    public static boolean playerHasOptOut(UUID uuid) {
        if (!fileExists(uuid)) {
            return false;
        }
        return getFileConfig(uuid).getBoolean("opt-out");
    }

    public static void locate(Player p) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        if (!fileExists(p.getUniqueId())) {
            newFile(p.getUniqueId());
        }
//        if (!getGeolocation(uuid).equals("Unknown")) {

        try {
            Scanner locationScanner = new Scanner("http://ip-api.com/line/" + p.getAddress());
            if (locationScanner.nextLine().contains("success")) {
                getFileConfig(p.getUniqueId()).set("location", locationScanner.nextLine());
            } else {
                getFileConfig(p.getUniqueId()).set("location", "Unknown");
            }
        } catch (Exception e) {

        }
//        plugin.log("http://ip-api.com/line/" + p.getAddress().getAddress().getHostAddress() + "\n");

//    }
    }

    public static void ageFound(UUID uuid, int ageNum) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
        getFileConfig(uuid).set("age", "" + ageNum);
    }

    public static void sexFound(UUID uuid, String sex) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
        getFileConfig(uuid).set("sex", "" + sex);
    }

    public static String getGeolocation(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        return getFileConfig(uuid).getString("location");
    }

    public static String getSex(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        return getFileConfig(uuid).getString("sex");
    }

    public static String getAge(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        return getFileConfig(uuid).getString("age");
    }

}
