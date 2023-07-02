package de.christoph.bansystem.listeners;

import de.christoph.bansystem.punishment.Ban;
import de.christoph.bansystem.punishment.Punishment;
import de.christoph.bansystem.punishment.PunishmentType;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class BanListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) throws SQLException {
        Punishment punishment = Punishment.getPunishment(PunishmentType.BAN, event.getPlayer().getUniqueId().toString());
        if(punishment != null) {
            if(punishment instanceof Ban) {
                Ban ban = (Ban) punishment;
                Date today = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
                if(ban.getEndingDate().before(today)) {
                    ban.removeFromDatabase();
                    return;
                }
                ban.kickPlayer(event.getPlayer());
            }
        }
    }

}
