
package com.djrapitops.plandemog;

import com.djrapitops.plandemog.command.CommandType;
import com.djrapitops.plandemog.command.SubCommand;
import com.djrapitops.plandemog.command.commands.HelpCommand;
import com.djrapitops.plandemog.command.commands.OptInCommand;
import com.djrapitops.plandemog.command.commands.OptOutCommand;
import com.djrapitops.plandemog.javaTools.Editor;
import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.entity.Player;

public class PlanDCommand implements CommandExecutor {

    private final List<SubCommand> commands;

    public PlanDCommand(PlanDemographics plugin) {
        commands = new ArrayList<>();

        commands.add(new HelpCommand(plugin, this));
        commands.add(new OptOutCommand(plugin));
        commands.add(new OptInCommand(plugin));
    }

    public List<SubCommand> getCommands() {
        return this.commands;
    }

    public SubCommand getCommand(String name) {
        for (SubCommand command : commands) {
            String[] aliases = command.getName().split(",");

            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(name)) {
                    return command;
                }
            }
        }
        return null;
    }

    private void sendDefaultCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String command = "help";
        if (args.length < 1) {
            command = "help";
        }
        Editor edit = new Editor();
        onCommand(sender, cmd, commandLabel, edit.mergeArrays(new String[]{command}, args));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length < 1) {
            sendDefaultCommand(sender, cmd, commandLabel, args);
            return true;
        }

        SubCommand command = getCommand(args[0]);

        if (command == null) {
            sendDefaultCommand(sender, cmd, commandLabel, args);
            return true;
        }

        boolean console = !(sender instanceof Player);
        
        if (!sender.hasPermission(command.getPermission())) {
//            Phrase.NO_PERMISSION_FOR_COMMAND.sendWithPrefix( sender );
            sender.sendMessage(ChatColor.RED + "[PLADE] You do not have the required permmission.");
            return true;
        }

        if (console && args.length < 2 && command.getCommandType() == CommandType.CONSOLE_WITH_ARGUMENTS) {
//            Phrase.COMMAND_NEEDS_ARGUMENTS.sendWithPrefix( sender );
            sender.sendMessage(ChatColor.RED + "[PLADE] Command requires arguments.");

            return true;
        }

        if (console && command.getCommandType() == CommandType.PLAYER) {
//            Phrase.COMMAND_NOT_CONSOLE.sendWithPrefix( sender, commandLabel );
            sender.sendMessage(ChatColor.RED + "[PLADE] This command can be only used as a player.");

            return true;
        }

        String[] realArgs = new String[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            realArgs[i - 1] = args[i];
        }

        if (!command.onCommand(sender, cmd, commandLabel, realArgs)) {
//            Phrase.TRY_COMMAND.sendWithPrefix( sender, parse( commandLabel, command ) );
        }
        return true;
    }

}


