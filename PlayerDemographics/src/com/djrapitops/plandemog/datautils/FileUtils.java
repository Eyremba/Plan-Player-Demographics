// This product includes GeoLite data created by MaxMind, available from http://www.maxmind.com
package com.djrapitops.plandemog.datautils;

import com.djrapitops.plandemog.PlanDemographics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
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
        newFile(userFile);
    }

    private static void newFile(File userFile) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        try {
            if (!userFile.exists()) {
                userFile.createNewFile();
                FileWriter fw = new FileWriter(userFile, false);
                try (PrintWriter pw = new PrintWriter(fw)) {
                    pw.print("opt-out: false\nlocation: Unknown\nage: Unknown\ngender: Unknown\n");
                    pw.flush();
                }
            }
        } catch (IOException e) {
            plugin.logError("Failed to create user file");
        }
    }

    private static void saveFile(UUID uuid, HashMap<String, String> playerdata) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        if (fileExists(uuid)) {
            String fileName = uuid + ".yml";
            try {
                File userFile = new File(plugin.getDataFolder() + File.separator + "users", fileName);
                FileWriter fw = new FileWriter(userFile, false);
                try (PrintWriter pw = new PrintWriter(fw)) {
                    for (String key : playerdata.keySet()) {
                        pw.print(key + ": " + playerdata.get(key) + "\n");
                    }
                    pw.flush();
                }
            } catch (IOException e) {
                plugin.logError("Failed to save user file");
            }
        }
    }

    private static HashMap<String, String> getFileConfig(UUID uuid) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        String fileName = uuid + ".yml";
        File userFile = new File(plugin.getDataFolder() + File.separator + "users", fileName);
        HashMap<String, String> fileData = new HashMap<>();
        if (userFile.exists()) {
            try {
                Scanner reader = new Scanner(userFile, "UTF8");
                while (reader.hasNextLine()) {
                    String[] line = reader.nextLine().split(": ");
                    fileData.put(line[0], line[1]);
                }
            } catch (FileNotFoundException ex) {
                plugin.logToFile("FILE\nFile not found for " + uuid);
            }
        } else {
            newFile(userFile);
            return getFileConfig(uuid);
        }
        return fileData;
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
        HashMap<String, String> playerdata = getFileConfig(uuid);
        playerdata.put("opt-out", "" + true);
        saveFile(uuid, playerdata);
    }

    public static void OptIn(UUID uuid) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
        HashMap<String, String> playerdata = getFileConfig(uuid);
        playerdata.put("opt-out", "" + false);
        saveFile(uuid, playerdata);
    }

    public static boolean playerHasOptOut(UUID uuid) {
        if (!fileExists(uuid)) {
            return false;
        }
        HashMap<String, String> playerdata = getFileConfig(uuid);
        return Boolean.parseBoolean(playerdata.get("opt-out"));
    }

    public static void locate(Player p) {
        locate(p, p.getAddress().getAddress());
    }
    
    public static void locate(Player p, InetAddress adress) {
        PlanDemographics plugin = getPlugin(PlanDemographics.class);
        if (!fileExists(p.getUniqueId())) {
            newFile(p.getUniqueId());
        }
        if (!getGeolocation(p.getUniqueId()).equals("Unknown")) {
            HashMap<String, String> playerdata = getFileConfig(p.getUniqueId());
            try {
                Scanner locationScanner = new Scanner("http://ip-api.com/line/" + adress.getHostAddress());
                List<String> results = new ArrayList<>();
                while (locationScanner.hasNextLine()) {
                    results.add(locationScanner.nextLine());
                }
                if (results.size() >= 2) {
                    playerdata.put("location", results.get(1));
                } else {
                    playerdata.put("location", "Unknown");
                }
                saveFile(p.getUniqueId(), playerdata);
            } catch (Exception e) {
                plugin.logToFile("http://ip-api.com/line/" + adress.getHostAddress());
                plugin.logToFile("" + e);
                plugin.logToFile(adress.toString());
            }

        }
    }

    public static void ageFound(UUID uuid, int ageNum) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
        HashMap<String, String> playerdata = getFileConfig(uuid);
        playerdata.put("age", "" + ageNum);
        saveFile(uuid, playerdata);
    }

    public static void genderFound(UUID uuid, String gender) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
        HashMap<String, String> playerdata = getFileConfig(uuid);
        playerdata.put("gender", "" + gender);
        saveFile(uuid, playerdata);
    }

    public static String getGeolocation(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        HashMap<String, String> playerdata = getFileConfig(uuid);
        if (playerdata.get("location") == null) {
            return "Unknown";
        }
        return (playerdata.get("location"));
    }

    public static String getGender(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        HashMap<String, String> playerdata = getFileConfig(uuid);
        return (playerdata.get("gender"));
    }

    public static String getAge(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        HashMap<String, String> playerdata = getFileConfig(uuid);
        return (playerdata.get("age"));
    }

}
