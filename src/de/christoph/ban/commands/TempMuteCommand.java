package de.christoph.ban.commands;

import de.christoph.ban.Constant;
import de.christoph.ban.punish.Ban;
import de.christoph.ban.punish.Mute;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.time.LocalDateTime;
import java.util.Calendar;

public class TempMuteCommand extends Command {

    public TempMuteCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission(Constant.MUTE_PERMISSION)) {
            if(strings.length == 5) {
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(strings[0]);
                if(target != null) {
                    if (target.hasPermission(Constant.MUTE_BYPASS) && !commandSender.hasPermission(Constant.BAN_ADMIN)) {
                        target.sendMessage(Constant.PREFIX + "§7Der Spieler §e" + commandSender.getName() + " §7hat versucht dich zu muten.");
                        return;
                    }
                    int day = 0;
                    int month = 0;
                    int year = 0;
                    try {
                        day = Integer.parseInt(strings[2]);
                        month = Integer.parseInt(strings[3]);
                        year = Integer.parseInt(strings[4]);
                    } catch (NumberFormatException e) {
                        commandSender.sendMessage(Constant.PREFIX + "§7Dies ist kein gültiges §cDatum§7.");
                        return;
                    }
                    new Mute(target.getUniqueId().toString(), target.getName(), strings[1], commandSender.getName(), day, month, year);
                } else
                    commandSender.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht auf dem §cNetzwerk§7.");
            } else {
                commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/tempmute <Spieler> <Grund> <Tag> <Monat> <Jahr>");
                commandSender.sendMessage(Constant.PREFIX + "§7z.B. §e/tempmute Steve Hacking 12 2 2023");
            }

        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
    }

}
