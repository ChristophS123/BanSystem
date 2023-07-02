package de.christoph.bansystem.commands;

import de.christoph.bansystem.Constant;
import de.christoph.bansystem.punishment.Ban;
import de.christoph.bansystem.punishment.Punishment;
import de.christoph.bansystem.punishment.PunishmentType;
import de.christoph.bansystem.utils.MinecraftPlayerUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class UnbanCommand extends Command {

    public UnbanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission(Constant.UNBAN_PERMISSION)) {
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
            return;
        }
        if(strings.length != 1) {
            commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/unban <Spieler>§7.");
            return;
        }
        try {
            String uuid = MinecraftPlayerUtil.getUUIDByName(strings[0]);
            System.out.println(uuid);
            Ban ban  = (Ban) Punishment.getPunishment(PunishmentType.BAN, uuid);
            if(ban == null) {
                commandSender.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht §cgebannt§7.");
                return;
            }
            ban.removeFromDatabase();
            commandSender.sendMessage(Constant.PREFIX + "§7Der Spieler wurde entbannt.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

