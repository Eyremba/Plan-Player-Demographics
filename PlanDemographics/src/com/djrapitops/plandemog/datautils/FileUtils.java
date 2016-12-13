
package com.djrapitops.plandemog.datautils;

import java.util.UUID;

public class FileUtils {

    public static void newFile(UUID uuid) {
        
    }
    
    public static boolean fileExists(UUID uuid) {
        return false;
    }
    
    public static void OptOut(UUID uuid) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
    }

    public static void OptIn(UUID uuid) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
    }

    public static boolean playerHasOptOut(UUID uuid) {
        if (!fileExists(uuid)) {
            return false;
        }
        return false;
    }

    public static void locate(UUID uuid) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
    }
    
    public static void ageFound(UUID uuid, int ageNum) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
    }

    public static void sexFound(UUID uuid, String female) {
        if (!fileExists(uuid)) {
            newFile(uuid);
        }
    }
    
    public static String getGeolocation(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        return "Null";
    }

    public static String getSex(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        return "Null";
    }

    public static String getAge(UUID uuid) {
        if (!fileExists(uuid)) {
            return "Unknown";
        }
        return "Null";
    }

}
