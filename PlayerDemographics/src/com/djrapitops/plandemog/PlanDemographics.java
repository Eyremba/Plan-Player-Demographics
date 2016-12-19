package com.djrapitops.plandemog;

import com.djrapitops.plan.api.API;
import com.djrapitops.plan.Plan;
import com.djrapitops.plandemog.listeners.PlanDChatListener;
import com.djrapitops.plandemog.listeners.PlanDPlayerListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class PlanDemographics extends JavaPlugin {

    private PlanDChatListener chatListener;
    private PlanDPlayerListener playerListener;
    
    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Plan")) {
            logError("Dependency Plan (Player Analytics) not found - Disabling plugin..");
            logToFile("MAIN\nPlan not found. Plugin Disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getDataFolder().mkdirs();

        getConfig().options().copyDefaults(true);

        getConfig().options().header("Plan Demographics Config\n"
                + "debug - Errors are saved in errorlog.txt when they occur\n"
        );

        saveConfig();
        
        registerListeners();
        
        log("Hooking to Plan..");
        // Hook Plan
        API planAPI = getPlugin(Plan.class).getAPI();
        planAPI.addExtraHook("PlanDemographics", new PlanHook(this));
        
        getCommand("plade").setExecutor(new PlanDCommand(this));
        log("Player Demographics enabled.");
        
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        log("Player Demographics Disabled.");
    }

    public void log(String message) {
        getLogger().info(message);
    }

    public void logError(String message) {
        getLogger().severe(message);
    }

    public void logToFile(String message) {
        if (getConfig().getBoolean("debug")) {
            File folder = getDataFolder();
            if (!folder.exists()) {
                folder.mkdir();
            }
            File log = new File(getDataFolder(), "errorlog.txt");
            try {
                if (!log.exists()) {
                    log.createNewFile();
                }
                FileWriter fw = new FileWriter(log, true);
                try (PrintWriter pw = new PrintWriter(fw)) {
                    pw.println(message + "\n");
                    pw.flush();
                }
            } catch (IOException e) {
                logError("Failed to create log.txt file");
            }
        }
    }

    private void registerListeners() {
        chatListener = new PlanDChatListener(this);
        playerListener = new PlanDPlayerListener(this);
    }
}
