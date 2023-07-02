package de.christoph.bansystem.commands;

import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.Constant;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ReportCommand extends Command {
    private ArrayList<ProxiedPlayer> reportPlayer = new ArrayList<>();

    public ReportCommand(String name) {
        super(name);
    }

    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer))
            return;
        final ProxiedPlayer player = (ProxiedPlayer)commandSender;
        if (strings.length == 2) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[0]);
            if (target != null) {
                if (!this.reportPlayer.contains(player)) {
                    this.reportPlayer.add(player);
                    try {
                        PreparedStatement preparedStatement = (BanSystem.getInstance()).getMySQL().getConnection().prepareStatement("INSERT INTO `reports`(`victim`, `victimname`, `sender`, `reason`, `finished`) VALUES (?,?,?,?,?)");
                        preparedStatement.setString(1, target.getUniqueId().toString());
                        preparedStatement.setString(2, target.getName());
                        preparedStatement.setString(3, player.getName());
                        preparedStatement.setString(4, strings[1]);
                        preparedStatement.setInt(5, 0);
                        preparedStatement.execute();
                        player.sendMessage(Constant.PREFIX + "Vielen Dank für deinen Report! Wir werden uns bei Neuigkeiten bei dir melden.");
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            if (all.hasPermission(Constant.REPORT_INFORM_PERMISSION)) {
                                TextComponent textComponent = new TextComponent("§a§l(KLICKE HIER)");
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports teleport " + target.getName()));
                                all.sendMessage("");
                                all.sendMessage("");
                                all.sendMessage("§7Der Spieler §e§l" + target.getName() + " §7wurde von §e§l" + player.getName() + " §7wegen §e§l" + strings[1] + " §7reportet");
                                all.sendMessage("");
                                all.sendMessage((BaseComponent)textComponent);
                                all.sendMessage("");
                                all.sendMessage("");
                            }
                        }
                        ProxyServer.getInstance().getScheduler().schedule((Plugin)BanSystem.getInstance(), new Runnable() {
                            public void run() {
                                ReportCommand.this.reportPlayer.remove(player);
                            }
                        },  60L, TimeUnit.SECONDS);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        player.sendMessage(Constant.PREFIX + "§7Ein Fehler ist aufgetreten. Bitte melde dich auf unserem §9Discord Server§7.");
                    }
                } else {
                    commandSender.sendMessage(Constant.PREFIX + "§7Du kannst nur alle §c60 Sekunden §7reporten.");
                }
            } else {
                commandSender.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht auf dem §cServer§7.");
            }
        } else {
            commandSender.sendMessage(Constant.PREFIX + "§7Benutze: §e/report <Spieler> <Grund>§7.");
        }
    }
}
