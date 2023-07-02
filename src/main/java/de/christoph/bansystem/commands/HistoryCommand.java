package de.christoph.bansystem.commands;

import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.Constant;
import de.christoph.bansystem.utils.MinecraftPlayerUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryCommand extends Command {

    public HistoryCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission(Constant.HISTORY_PERMISSION)) {
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
            return;
        }
        if(strings.length != 1) {
            commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze: §e/history <Spieler>§7.");
            return;
        }
        String uuid = MinecraftPlayerUtil.getUUIDByName(strings[0]);
        try {
            PreparedStatement preparedStatement = BanSystem.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM `histories` WHERE `victim` = ?");
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            commandSender.sendMessage("§e§l" + strings[0]);
            while (resultSet.next()) {
                commandSender.sendMessage("");
                commandSender.sendMessage("§7Strafe: §e" + resultSet.getString("punishmentType"));
                commandSender.sendMessage("§7Sender: §e" + resultSet.getString("sender"));
                commandSender.sendMessage("§7Grund: §e" + resultSet.getString("reason"));
                commandSender.sendMessage("§7StrafenTag: §e" + resultSet.getDate("punishDate") + " " + resultSet.getTime("punishTime"));
                commandSender.sendMessage("§7Ende: §e" + resultSet.getDate("endingDate") + " " + resultSet.getTime("endingTime"));
            }
            commandSender.sendMessage("");
            commandSender.sendMessage("§e§l" + strings[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
