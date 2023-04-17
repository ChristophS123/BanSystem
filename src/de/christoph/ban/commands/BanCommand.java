package de.christoph.ban.commands;

import de.christoph.ban.Constant;
import de.christoph.ban.punish.Ban;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

    public BanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission(Constant.BAN_PERMISSION)) {
            if(strings.length == 2) {
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(strings[0]);
                if(target != null) {
                    if (target.hasPermission(Constant.BAN_BYPASS) && !commandSender.hasPermission(Constant.BAN_ADMIN)) {
                        target.sendMessage(Constant.PREFIX + "§7Der Spieler §e" + commandSender.getName() + " §7hat versucht dich zu bannen.");
                        return;
                    }
                    new Ban(target.getUniqueId().toString(), target.getName(), strings[1], commandSender.getName());
                } else
                    commandSender.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht auf dem §cNetzwerk§7.");
            } else
                commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/ban <Spieler> <Grund>§7.");
        } else
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
    }

}
