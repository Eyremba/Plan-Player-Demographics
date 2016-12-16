package com.djrapitops.plandemog.listeners;

import com.djrapitops.plandemog.PlanDemographics;
import com.djrapitops.plandemog.datautils.FileUtils;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlanDPlayerListener implements Listener {

    private final PlanDemographics plugin;

    public PlanDPlayerListener(PlanDemographics plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!FileUtils.fileExists(uuid)) {
            FileUtils.newFile(uuid);
        }
        if (FileUtils.playerHasOptOut(uuid)) {
            return;
        }
        FileUtils.locate(player, event.getAddress());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
    }
}
