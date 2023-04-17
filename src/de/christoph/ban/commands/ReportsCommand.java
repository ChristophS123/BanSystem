package de.christoph.ban.commands;

import de.christoph.ban.Constant;
import de.christoph.ban.BanSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsCommand extends Command {

    public ReportsCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission(Constant.REPORT_ADMIN_PERMISSION)) {
            commandSender.sendMessage(Constant.PREFIX + "§7Benutze §e/report <Spieler> <Name>§7.");
            return;
        }
        if(strings.length == 0) {
            //Get all reports
            try {
                PreparedStatement preparedStatement = BanSystem.getPlugin().mySQL.getConnection().prepareStatement(
                    "SELECT * FROM `reports` WHERE `finished` = ?"
                );
                preparedStatement.setInt(1, 0);
                ResultSet resultSet = preparedStatement.executeQuery();
                commandSender.sendMessage("§e§lReports");
                commandSender.sendMessage("");
                while (resultSet.next()) {
                    commandSender.sendMessage("§7" + resultSet.getInt("id") + " §e" + resultSet.getString("victimname"));
                    commandSender.sendMessage("");
                }
                commandSender.sendMessage("§e§lReports");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                commandSender.sendMessage(Constant.PREFIX + "§7Ein Fehler ist aufgetreten.");
            }
        } else if(strings.length == 1) {
            if(strings[0].equalsIgnoreCase("hilfe") || strings[0].equalsIgnoreCase("help")) {
                sendReportsHelp(commandSender);
                return;
            }
            //Get specific report
            int id = 0;
            try {
                id = Integer.parseInt(strings[0]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(Constant.PREFIX + "§7Benutze §e/reports <id>§7.");
                return;
            }
            try {
                if(!isInDatabase(id)) {
                    commandSender.sendMessage(Constant.PREFIX + "§7Diese ID existiert nicht.");
                    return;
                }
                PreparedStatement preparedStatement = BanSystem.getPlugin().mySQL.getConnection().prepareStatement(
                    "SELECT * FROM `reports` WHERE `id` = ?"
                );
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                commandSender.sendMessage("");
                while (resultSet.next()) {
                    commandSender.sendMessage("§7ID: §e" + resultSet.getInt("id"));
                    commandSender.sendMessage("§7Name: §e" + resultSet.getString("victimname"));
                    commandSender.sendMessage("§7Reportet von: §e" + resultSet.getString("sender"));
                    commandSender.sendMessage("§7Grund von: §e" + resultSet.getString("reason"));
                }
                commandSender.sendMessage("");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if(strings.length == 2) {
            if(strings[0].equalsIgnoreCase("finish")) {
                int id = 0;
                try {
                    id = Integer.parseInt(strings[1]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(Constant.PREFIX + "§7Dies ist keine gültige ID.");
                    return;
                }
                try {
                    PreparedStatement preparedStatement = BanSystem.getPlugin().mySQL.getConnection().prepareStatement(
                        "UPDATE `reports` SET `finished` = ? WHERE `id` = ?"
                    );
                    preparedStatement.setInt(1, 1);
                    preparedStatement.setInt(2, id);
                    preparedStatement.execute();
                    commandSender.sendMessage(Constant.PREFIX + "§7Der Report wurde als bearbeitet gekennzeichnet.");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    commandSender.sendMessage(Constant.PREFIX + "§7Etwas ist schief gelaufen.");
                }
                return;
            }
            if(strings[0].equalsIgnoreCase("teleport")) {
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(strings[1]);
                if(target != null) {
                    if(commandSender instanceof ProxiedPlayer) {
                        ProxiedPlayer player = (ProxiedPlayer) commandSender;
                        player.connect(target.getServer().getInfo());
                        player.sendMessage(Constant.PREFIX + "§7Du wurdest zum Server von §e" + target.getName() + " §7teleportiert.");
                    }
                } else
                    commandSender.sendMessage(Constant.PREFIX + "§7Der Spieler ist nicht mehr auf dem §cServer §7.");
                return;
            }
            sendReportsHelp(commandSender);
        }
    }

    private void sendReportsHelp(CommandSender commandSender) {
        commandSender.sendMessage("§e§lReports");
        commandSender.sendMessage("");
        commandSender.sendMessage("§e/reports §7- Sehe alle Reports");
        commandSender.sendMessage("§e/reports <id> §7- Sehe einen bestimmten Report");
        commandSender.sendMessage("§e/reports finish <id> §7- Markiere einen Report als beendet");
        commandSender.sendMessage("§e/reports teleport <name> §7- Teleportiere dich auf den Server vom Reporteten");
        commandSender.sendMessage("");
        commandSender.sendMessage("§e§lReports");
    }

    public boolean isInDatabase(int id) throws SQLException {
        PreparedStatement preparedStatement = BanSystem.getPlugin().mySQL.getConnection().prepareStatement(
                "SELECT `id` FROM `reports` WHERE `id` = ?"
        );
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return true;
        return false;
    }

}
