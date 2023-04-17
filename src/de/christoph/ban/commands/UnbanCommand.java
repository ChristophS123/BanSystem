package de.christoph.ban.commands;

import de.christoph.ban.Constant;
import de.christoph.ban.punish.Ban;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class UnbanCommand extends Command {

    public UnbanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission(Constant.UNBAN_PERMISSION)) {
            if(strings.length == 1) {
                try {
                    Ban.unbanPlayer(strings[0], commandSender.getName());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return;
                }

            } else
                commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/unban <Spieler>§7.");
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte");
    }

}
