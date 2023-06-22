package de.christoph.backend.commands;

import de.christoph.backend.Constant;
import de.christoph.backend.HWBackend;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuglistCommand extends Command {

    public BuglistCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission(Constant.BUG_ADMIN)) {
            if(strings.length == 0) {
                try {
                    commandSender.sendMessage("§e§lBugs");
                    commandSender.sendMessage("");
                    commandSender.sendMessage("");
                    PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                      "SELECT * FROM `bugs` WHERE 1"
                    );
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        commandSender.sendMessage("§7" + resultSet.getInt("id") + " " + resultSet.getString("category"));
                        commandSender.sendMessage("");
                    }
                    commandSender.sendMessage("");
                    commandSender.sendMessage("§e§lBugs");
                } catch (SQLException throwables) {
                    commandSender.sendMessage(Constant.PREFIX + "§7Ein Fehler ist aufgetreten.");
                    throwables.printStackTrace();
                }
            } else if(strings.length == 1) {
                if(strings[0].equalsIgnoreCase("help") || strings[0].equalsIgnoreCase("hilfe")) {
                    sendHelp(commandSender);
                    return;
                }
                int id = 0;
                try {
                    id = Integer.parseInt(strings[0]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(Constant.PREFIX + "§7Dies ist keine gültige §cID§7.");
                    return;
                }
                try {
                    PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                      "SELECT * FROM `bugs` WHERE `id` = ?"
                    );
                    preparedStatement.setInt(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    commandSender.sendMessage("");
                    while (resultSet.next()) {
                        commandSender.sendMessage("§7ID: §e" + resultSet.getInt("id"));
                        commandSender.sendMessage("§7Kategorie: §e" + resultSet.getString("category"));
                        commandSender.sendMessage("§7Reportet von: §e" + resultSet.getString("sender"));
                        commandSender.sendMessage("");
                        commandSender.sendMessage("§7Nachricht: §e" + resultSet.getString("message"));
                    }
                    commandSender.sendMessage("");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    commandSender.sendMessage(Constant.PREFIX + "§7Ein Fehler ist aufgetreten.");
                }
            } else if(strings.length == 2) {
                if(!strings[0].equalsIgnoreCase("finish") && !strings[0].equalsIgnoreCase("delete")) {
                    sendHelp(commandSender);
                    return;
                }
                int id = 0;
                try {
                    id = Integer.parseInt(strings[1]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(Constant.PREFIX + "§7Dies ist keine gültige §cID§7.");
                    return;
                }
                try {
                    PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                      "DELETE FROM `bugs` WHERE `id` = ?"
                    );
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                    commandSender.sendMessage(Constant.PREFIX + "§7Der Bug wurde §agelöscht§7.");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
    }

    private void sendHelp(CommandSender commandSender) {

    }

}
