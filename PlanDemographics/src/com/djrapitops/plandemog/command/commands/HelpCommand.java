package com.djrapitops.plandemog.command.commands;

//import com.djrapitops.plan.Phrase;
import com.djrapitops.plandemog.PlanDemographics;
import com.djrapitops.plandemog.PlanDCommand;
import com.djrapitops.plandemog.command.CommandType;
import com.djrapitops.plandemog.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {

    private final PlanDemographics plugin;
    private final PlanDCommand command;

    public HelpCommand(PlanDemographics plugin, PlanDCommand command) {
        super("help,?", "pland.?", "Show command list.", CommandType.CONSOLE);

        this.plugin = plugin;
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        ChatColor operatorColor = ChatColor.DARK_GREEN;

        ChatColor textColor = ChatColor.GRAY;

        sender.sendMessage(textColor + "-- [" + operatorColor + "PLAN - Player Demographics" + textColor + "] --");

        for (SubCommand command : this.command.getCommands()) {
            if (command.getName().equalsIgnoreCase(getName())) {
                continue;
            }

            if (!sender.hasPermission(command.getPermission())) {
                continue;
            }

            if (!(sender instanceof Player) && command.getCommandType() == CommandType.PLAYER) {
                continue;
            }

            sender.sendMessage(operatorColor + "/pland " + command.getFirstName() + textColor + " - " + command.getUsage());
        }

        return true;
    }

}
