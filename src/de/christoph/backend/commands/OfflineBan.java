package de.christoph.backend.commands;

import de.christoph.backend.Constant;
import de.christoph.backend.punish.Ban;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class OfflineBan extends Command {

    public OfflineBan(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission(Constant.OFFLINE_BAN_PERMISSION)) {
            if(strings.length == 3) {
                try {
                    new Ban(strings[0], strings[1], strings[2]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                commandSender.sendMessage(Constant.PREFIX + "§7Der Spieler wurde §agebannt§7.");
            } else {
                commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/offlineban <UUID> <Spieler> <Grund>§7.");
                commandSender.sendMessage(Constant.PREFIX + "§7UUID Website: §ehttps://mcuuid.net/");
            }
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
    }

}
