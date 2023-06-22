package de.christoph.backend.commands;

import de.christoph.backend.Constant;
import de.christoph.backend.punish.Mute;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

    public MuteCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission(Constant.MUTE_PERMISSION)) {
            if(strings.length == 2) {
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(strings[0]);
                if(target != null) {
                    if (target.hasPermission(Constant.MUTE_BYPASS) && !commandSender.hasPermission(Constant.BAN_ADMIN)) {
                        target.sendMessage(Constant.PREFIX + "§7Der Spieler §e" + commandSender.getName() + " §7hat versucht dich zu muten.");
                        return;
                    }
                    new Mute(target.getUniqueId().toString(), target.getName(), strings[1], commandSender.getName());
                } else
                    commandSender.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht auf dem §cNetzwerk§7.");
            } else
                commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/mute <Spieler> <Grund>§7.");
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
    }

}
