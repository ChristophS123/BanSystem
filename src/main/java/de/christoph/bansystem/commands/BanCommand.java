package de.christoph.bansystem.commands;

import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.Constant;
import de.christoph.bansystem.punishment.Ban;
import de.christoph.bansystem.punishment.PunishmentType;
import de.christoph.bansystem.punishment.reasons.Reason;
import de.christoph.bansystem.utils.MinecraftPlayerUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class BanCommand extends Command {

    public BanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission(Constant.BAN_PERMISSION)) {
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
            return;
        }
        if(strings.length == 0) {
            commandSender.sendMessage("");
            commandSender.sendMessage("§0-- §e§lBans §0--");
            for(Reason reason : BanSystem.getInstance().getReasonManager().getReasons()) {
                if(reason.getReasonType() == PunishmentType.BAN) {
                    String endDate = reason.formatDurationDate();
                    commandSender.sendMessage("");
                    commandSender.sendMessage("§e§l" + reason.getId() + " §7" + reason.getName() + " §eBan Ende: " + endDate);
                }
            }
            commandSender.sendMessage("");
            commandSender.sendMessage("§0-- §e§lBans §0--");
            commandSender.sendMessage("");
        }
        if(strings.length != 2) {
            commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/ban <Spieler> <Grund ID>§7.");
            return;
        }
        int reasonID = 0;
        try {
            reasonID = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(Constant.PREFIX + "§7Dies ist keine gültige §cGrund-ID§7.");
            return;
        }
        Reason reason = BanSystem.getInstance().getReasonManager().getReasonByID(reasonID);
        if(reason == null) {
            commandSender.sendMessage(Constant.PREFIX + "§7Dies ist keine gültige §cGrund-ID§7.");
            return;
        }
        if(!reason.isPermitted(commandSender) && !commandSender.hasPermission(Constant.ALL_BAN_REASONS_PERMISSION)) {
            commandSender.sendMessage(Constant.PREFIX + "§7Auf diesen Grund hast du keinen §cZugriff§7.");
            return;
        }
        ProxiedPlayer victim = ProxyServer.getInstance().getPlayer(strings[0]);
        if(victim != null) {
            if(victim.hasPermission(Constant.BAN_BYPASS)) {
                commandSender.sendMessage(Constant.PREFIX + "§7Du darfst diesen Spieler nicht §cbannen§7.");
                return;
            }
            Ban ban = new Ban(victim.getUniqueId().toString(), commandSender.getName(), reason.getName(), Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), reason.getEndDate());
            ban.perform();
            ban.inform();
        } else {
            if(commandSender.hasPermission(Constant.OFFLINE_BAN)) {
                String victimUUID = MinecraftPlayerUtil.getUUIDByName(strings[0]);
                Ban ban = new Ban(victimUUID, commandSender.getName(), reason.getName(), Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), reason.getEndDate());
                ban.perform();
                ban.inform();
            } else {
                commandSender.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht §conline§7.");
                return;
            }
        }
        commandSender.sendMessage(Constant.PREFIX + "§7Der Spieler wurde §agebannt§7.");
    }

}
