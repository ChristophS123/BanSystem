package de.christoph.bansystem.commands;

import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.Constant;
import de.christoph.bansystem.punishment.Ban;
import de.christoph.bansystem.support.Support;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SupportCommand extends Command {
    public SupportCommand(String name) {
        super(name);
    }

    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer)commandSender;
            if (strings.length == 0) {
                for (Support all : (BanSystem.getInstance()).supports) {
                    if (all.getPlayer() == player) {
                        player.sendMessage(Constant.PREFIX + "§7Du hast bereits einen §cSupport eröffnet§7.");
                        return;
                    }
                }
                new Support(player);
                player.sendMessage(Constant.PREFIX + "§7Du hast einen Support eröffnet.");
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    if (all.hasPermission(Constant.SUPPORTER_PERMISSION))
                        all.sendMessage(Constant.PREFIX + "§e" + player.getName() + " §7hat einen Support eröffnet. Benutze §e/support annehmen " + player.getName() + "§7.");
                }
            } else if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("beenden")) {
                    Support support = null;
                    for (Support all : (BanSystem.getInstance()).supports) {
                        if (all.getPlayer() == player || all.getSupporter() == player)
                            support = all;
                    }
                    if (support == null) {
                        player.sendMessage(Constant.PREFIX + "§7Du befindest dich in keinem Support.");
                        return;
                    }
                    if(support.getSupporter() == null) {
                        player.sendMessage(Constant.PREFIX + "§7Dein Support wurde noch nicht angenommen.");
                        return;
                    }
                    support.finishSupport();
                }
            } else if (strings[0].equalsIgnoreCase("annehmen")) {
                if (!player.hasPermission(Constant.SUPPORTER_PERMISSION)) {
                    player.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
                    return;
                }
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[1]);
                if (target == null) {
                    player.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht auf dem §cNetzwerk§7.");
                    return;
                }
                Support support = null;
                for (Support all : (BanSystem.getInstance()).supports) {
                    if (all.getPlayer() == target)
                        support = all;
                }
                if (support == null) {
                    player.sendMessage(Constant.PREFIX + "§7Dieser Spieler braucht keinen §cSupport§7.");
                    return;
                }
                support.supporterAccept(player);
            }
        }
    }
}
