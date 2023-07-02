package de.christoph.bansystem.commands;

import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.Constant;
import de.christoph.bansystem.punishment.Ban;
import de.christoph.bansystem.punishment.Mute;
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

public class MuteCommand extends Command {

    public MuteCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission(Constant.MUTE_PERMISSION)) {
            commandSender.sendMessage(Constant.PREFIX + "§7Dazu hast du keine §cRechte§7.");
            return;
        }
        if(strings.length == 0) {
            commandSender.sendMessage("");
            commandSender.sendMessage("§0-- §e§lMutes §0--");
            for(Reason reason : BanSystem.getInstance().getReasonManager().getReasons()) {
                if(reason.getReasonType() == PunishmentType.MUTE) {
                    String endDate = reason.formatDurationDate();
                    commandSender.sendMessage("");
                    commandSender.sendMessage("§e§l" + reason.getId() + " §7" + reason.getName() + " §eMute Ende: " + endDate);
                }
            }
            commandSender.sendMessage("");
            commandSender.sendMessage("§0-- §e§lMutes §0--");
            commandSender.sendMessage("");
        }
        if(strings.length != 2) {
            commandSender.sendMessage(Constant.PREFIX + "§7Bitte benutze §e/mute <Spieler> <Grund ID>§7.");
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
        if(!reason.isPermitted(commandSender) && !commandSender.hasPermission(Constant.ALL_MUTE_REASONS_PERMISSION)) {
            commandSender.sendMessage(Constant.PREFIX + "§7Auf diesen Grund hast du keinen §cZugriff§7.");
            return;
        }
        ProxiedPlayer victim = ProxyServer.getInstance().getPlayer(strings[0]);
        if(victim != null) {
            if(victim.hasPermission(Constant.MUTE_BYPASS)) {
                commandSender.sendMessage(Constant.PREFIX + "§7Du darfst diesen Spieler nicht §cmuten§7.");
                return;
            }
            Mute mute = new Mute(victim.getUniqueId().toString(), commandSender.getName(), reason.getName(), Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), reason.getEndDate());
            mute.perform();
            mute.inform();
        } else {
            if(commandSender.hasPermission(Constant.OFFLINE_MUTE)) {
                String victimUUID = MinecraftPlayerUtil.getUUIDByName(strings[0]);
                Mute mute = new Mute(victimUUID, commandSender.getName(), reason.getName(), Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), reason.getEndDate());
                mute.perform();
                mute.inform();
            } else {
                commandSender.sendMessage(Constant.PREFIX + "§7Dieser Spieler ist nicht §conline§7.");
                return;
            }
        }
        commandSender.sendMessage(Constant.PREFIX + "§7Der Spieler wurde §agemutet§7.");
    }

}
