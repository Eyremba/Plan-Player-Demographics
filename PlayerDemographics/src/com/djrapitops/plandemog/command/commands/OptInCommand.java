
package com.djrapitops.plandemog.command.commands;

import com.djrapitops.plandemog.PlanDemographics;
import com.djrapitops.plandemog.command.CommandType;
import com.djrapitops.plandemog.command.SubCommand;
import com.djrapitops.plandemog.datautils.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OptInCommand extends SubCommand {
    private final PlanDemographics plugin;

    public OptInCommand(PlanDemographics plugin) {
        super("optin,opt-in,in", "plade.in", "Opt-in demographics logging /plade optIn", CommandType.PLAYER);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            FileUtils.OptIn(((Player) sender).getUniqueId());
            sender.sendMessage(ChatColor.DARK_GREEN+"Player Demographic Opt-In Successful.");
        } else {
            sender.sendMessage(ChatColor.RED + "[PLADE] This command can be only used as a player.");
        }
        return true;
    }
}
