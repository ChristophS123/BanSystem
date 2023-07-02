package de.christoph.bansystem.commands;

import de.christoph.bansystem.punishment.Ban;
import de.christoph.bansystem.punishment.Mute;
import de.christoph.bansystem.punishment.Punishment;
import de.christoph.bansystem.punishment.PunishmentType;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MuteListener implements Listener {

    @EventHandler
    public void onPlayerJoin(ChatEvent event) throws SQLException {
        if(!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }
        if(event.getMessage().charAt(0) == '/') {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        Punishment punishment = Punishment.getPunishment(PunishmentType.MUTE, player.getUniqueId().toString());
        if(punishment != null) {
            if(punishment instanceof Mute) {
                Mute mute = (Mute) punishment;
                Date today = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
                if(mute.getEndingDate().before(today)) {
                    mute.removeFromDatabase();
                    return;
                }
                event.setCancelled(true);
                mute.sendMuteMessage(player);
            }
        }
    }

}
