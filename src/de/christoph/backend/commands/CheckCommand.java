package de.christoph.backend.commands;

import de.christoph.backend.Constant;
import de.christoph.backend.HWBackend;
import de.christoph.backend.punish.Ban;
import de.christoph.backend.punish.Mute;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckCommand extends Command {

    public CheckCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission(Constant.CHECK)) {
            if(strings.length == 1) {
                commandSender.sendMessage("§e§lCheck");
                commandSender.sendMessage("");
                commandSender.sendMessage("§7Name: " + strings[0]);
                //ban
                try {
                    if(Ban.isNameBanned(strings[0]))
                        commandSender.sendMessage("§7Gebannt: §aJa");
                    else
                        commandSender.sendMessage("§7Gebannt: §cNein");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                //mute
                try {
                    if(Mute.isNameMuted(strings[0]))
                        commandSender.sendMessage("§7Gemutet: §aJa");
                    else
                        commandSender.sendMessage("§7Gemutet: §cNein");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                commandSender.sendMessage("");
                commandSender.sendMessage("§7Reports:");
                commandSender.sendMessage("");
                //Report
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                      "SELECT * FROM `reports` WHERE `victimname` = ?"
                    );
                    preparedStatement.setString(1, strings[0]);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        commandSender.sendMessage("§7" + resultSet.getInt("id") + " " + resultSet.getString("reason"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                commandSender.sendMessage("");
                commandSender.sendMessage("§e§lCheck");
            } else
                commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/check <Spieler>§7.");
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
    }

}
