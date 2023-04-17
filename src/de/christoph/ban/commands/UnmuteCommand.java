package de.christoph.ban.commands;

import de.christoph.ban.Constant;
import de.christoph.ban.punish.Mute;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class UnmuteCommand extends Command {

    public UnmuteCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission(Constant.UNMUTE_PERMISSION)) {
            if(strings.length == 1) {
                try {
                    Mute.unmutePlayer(strings[0], commandSender.getName());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return;
                }

            } else
                commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/unmute <Spieler>§7.");
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte");
    }

}
