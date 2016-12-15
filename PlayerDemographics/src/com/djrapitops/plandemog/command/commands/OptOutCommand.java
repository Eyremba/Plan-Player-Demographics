package com.djrapitops.plandemog.command.commands;

import com.djrapitops.plandemog.PlanDemographics;
import com.djrapitops.plandemog.command.CommandType;
import com.djrapitops.plandemog.command.SubCommand;
import com.djrapitops.plandemog.datautils.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OptOutCommand extends SubCommand {

    private final PlanDemographics plugin;

    public OptOutCommand(PlanDemographics plugin) {
        super("optout,opt-out,out", "plade.out", "Opt-out of demographics logging /plade optOut", CommandType.PLAYER);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            FileUtils.OptOut(((Player) sender).getUniqueId());
            sender.sendMessage(ChatColor.DARK_GREEN + "Player Demographic Opt-Out Successful.");
        } else {
            sender.sendMessage(ChatColor.RED + "[PLADE] This command can be only used as a player.");
        }
        return true;
    }
}
