package com.djrapitops.plandemog.listeners;

import com.djrapitops.plandemog.PlanDemographics;
import com.djrapitops.plandemog.datautils.FileUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlanDChatListener implements Listener {

    private final PlanDemographics plugin;

    public PlanDChatListener(PlanDemographics plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (FileUtils.playerHasOptOut(player.getUniqueId())) {
            return;
        }
        // Create lists
        String[] triggersA = {"i\\'m", "am", "im"};
        String[] femaleA = {"female", "girl", "gurl", "woman", "gal", "mrs", "she", "miss"};
        String[] maleA = {"male", "boy", "man", "boe", "sir", "mr", "guy", "he"};
        String[] ageA = {"years", "year-old", "old"};
        String[] ignoreA = {"sure", "think", "with", "are"};
        
        Set<String> triggers = new HashSet<>();
        Set<String> female = new HashSet<>();
        Set<String> male = new HashSet<>();
        Set<String> ages = new HashSet<>();
        Set<String> ignore = new HashSet<>();
        
        triggers.addAll(Arrays.asList(triggersA));
        female.addAll(Arrays.asList(femaleA));
        male.addAll(Arrays.asList(maleA));
        ages.addAll(Arrays.asList(ageA));
        ignore.addAll(Arrays.asList(ignoreA));
        // get message
        String message = event.getMessage();
        String[] messageA = message.toLowerCase().split("\\s+");
        
        boolean trigger = false;
        boolean age = false;
        boolean gender = false;
        
        // Does message contain important data?
        for (String string : messageA) {
            if (ignore.contains(string)) {
                trigger = false;
                break;
            }
            if (triggers.contains(string)) {
                trigger = true;
            }
            if (ages.contains(string)) {
                age = true;
            }
            if (female.contains(string) || male.contains(string)) {
                gender = true;
            }
        }
        
        // if not end
        if (!trigger) {
            return;
        }
        
        // Manage important data
        if (age) {
            int ageNum = -1;
            for (String string : messageA) {
                try {
                    ageNum = Integer.parseInt(string);
                    if (ageNum != -1) {
                        break;
                    }
                } catch (Exception e) {

                }
            }
            if (ageNum != -1 && ageNum < 100) {
                FileUtils.ageFound(player.getUniqueId(), ageNum);
            }
        }
        if (gender) {
            for (String string : messageA) {
                if (female.contains(string)) {
                    FileUtils.genderFound(player.getUniqueId(), "Female");
                } else if (male.contains(string)) {
                    FileUtils.genderFound(player.getUniqueId(), "Male");
                }
            }
        }
    }
}

