package de.christoph.backend.commands;

import de.christoph.backend.Constant;
import de.christoph.backend.HWBackend;
import de.christoph.backend.support.Support;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SupportCommand extends Command {

    public SupportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (strings.length == 0) {
                for (Support all : HWBackend.getPlugin().supports) {
                    if (all.getPlayer() == player) {
                        player.sendMessage(Constant.PREFIX + "§7Du hast bereits einen §cSupport §7eröffnet.");
                        return;
                    }
                }
                new Support(player);
                player.sendMessage(Constant.PREFIX + "§7Du hast einen Support eröffnet.");
                for (ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                    if (all.hasPermission("support.supporter"))
                        all.sendMessage(Constant.PREFIX + "§e§l" + player.getName() + " §7hat einen Support eröffnet. Benutze §e/support annehmen " + player.getName() + "§7.");
                }
            } else if(strings.length == 1) {
                if(strings[0].equalsIgnoreCase("beenden")) {
                    Support support = null;
                    for(Support all : HWBackend.getPlugin().supports) {
                        if(all.getPlayer() == player || all.getSupporter() == player) {
                            support = all;
                        }
                    }
                    if(support == null) {
                        player.sendMessage(Constant.PREFIX + "§7Du befindest dich in keinem Support");
                        return;
                    }
                    support.finishSupport();
                }
            } else if(strings[0].equalsIgnoreCase("annehmen")) {
                if(! player.hasPermission("support.supporter")) {
                    player.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
                    return;
                }
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(strings[1]);
                if(target == null) {
                    player.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht auf dem §cServer§7.");
                    return;
                }
                Support support = null;
                for (Support all : HWBackend.getPlugin().supports) {
                    if (all.getPlayer() == target) {
                        support = all;
                    }
                }
                if(support == null) {
                    player.sendMessage(Constant.PREFIX + "§7Dieser Spieler hat keinen §cSupport§7.");
                    return;
                }
                support.supporterAccept(player);
            }
        }
    }

}
