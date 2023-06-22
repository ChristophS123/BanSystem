package de.christoph.backend.commands;

import de.christoph.backend.Constant;
import de.christoph.backend.HWBackend;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BugReportCommand extends Command {

    public ArrayList<CommandSender> bugReportSenders = new ArrayList<>();

    public BugReportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(strings.length > 2) {
            if(bugReportSenders.contains(commandSender)) {
                commandSender.sendMessage(Constant.PREFIX + "§7Du kannst nur jede Minute einen Bug reporten.");
                return;
            }
            bugReportSenders.add(commandSender);
            String category = strings[0];
            String message = "";
            int i = 0;
            for(String word : strings) {
                i++;
                if(i != 1) {
                    message = message + word + " ";
                }
            }
            try {
                PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                  "INSERT INTO `bugs`(`sender`, `category`, `message`) VALUES (?,?,?)"
                );
                preparedStatement.setString(1, commandSender.getName());
                preparedStatement.setString(2, category);
                preparedStatement.setString(3, message);
                preparedStatement.execute();
                commandSender.sendMessage(Constant.PREFIX + "§7Vielen Dank für den Report!");
            } catch (SQLException throwables) {
                commandSender.sendMessage(Constant.PREFIX + "§7Ein Fehler ist aufgetreten. Bitte reporte den Bug auf unserem Discord Server.");
                throwables.printStackTrace();
            }
            BungeeCord.getInstance().getScheduler().schedule(HWBackend.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    bugReportSenders.remove(commandSender);
                }
            }, 60, TimeUnit.SECONDS);
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/bugreport <Spielmodus> <Nachricht>§7.");
    }

}
