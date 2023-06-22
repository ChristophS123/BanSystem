package de.christoph.backend.commands;

import de.christoph.backend.Constant;
import de.christoph.backend.HWBackend;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ReportCommand extends Command {

    private ArrayList<ProxiedPlayer> reportPlayer = new ArrayList<>(); //sender

    public ReportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if(strings.length == 2) {
            ProxiedPlayer target = BungeeCord.getInstance().getPlayer(strings[0]);
            if(target != null) {
                if(!reportPlayer.contains(player)) {
                    reportPlayer.add(player);
                    try {
                        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                            "INSERT INTO `reports`(`victim`, `victimname`, `sender`, `reason`, `finished`) VALUES (?,?,?,?,?)"
                        );
                        preparedStatement.setString(1, target.getUniqueId().toString());
                        preparedStatement.setString(2, target.getName());
                        preparedStatement.setString(3, player.getName());
                        preparedStatement.setString(4, strings[1]);
                        preparedStatement.setInt(5, 0);
                        preparedStatement.execute();
                        player.sendMessage(Constant.PREFIX + "§7Vielen Dank für deinen Report! Wir werden uns bei Neuigkeiten bei dir melden.");
                        for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                            if(all.hasPermission(Constant.REPORT_ADMIN_PERMISSION)) {
                                TextComponent textComponent = new TextComponent("§a§l(KLICKE HIER)");
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports teleport " + target.getName()));
                                all.sendMessage("");
                                all.sendMessage("");
                                all.sendMessage("§7Der Spieler §e§l" + target.getName() + " §7wurde von §e§l" + player.getName() + " §7wegen §e§l" + strings[1] + " §7reportet.");
                                all.sendMessage("");
                                all.sendMessage(textComponent);
                                all.sendMessage("");
                                all.sendMessage("");
                            }
                        }
                        BungeeCord.getInstance().getScheduler().schedule(HWBackend.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                reportPlayer.remove(player);
                            }
                        }, 60, TimeUnit.SECONDS);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        player.sendMessage(Constant.PREFIX + "§7Leider ist ein Fehler aufgetreten. Bitte Reporte den Spieler in einem Ticket auf unserem §9Discord Server§7.");
                    }
                } else
                    commandSender.sendMessage(Constant.PREFIX + "§7Du kannst nur alle §c60 Sekunden §7reporten.");
            } else
                commandSender.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht auf dem §cServer§7.");
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/report <Spieler> <Grund>§7.");
    }

}
