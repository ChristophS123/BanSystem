package de.christoph.ban.listeners;

import de.christoph.ban.punish.Ban;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class BanListener implements Listener {

    @EventHandler
    public void onPreJoin(PostLoginEvent event) throws SQLException {
        ProxiedPlayer player = event.getPlayer();
        if(Ban.isBanned(player.getUniqueId().toString())) {
            if(Ban.getDay(player.getUniqueId().toString()) == 0) { // Perma Ban
                player.disconnect(
                        "§7Du wurdest von HeroWars §4§lGebannt§7." +
                        " \n\n §7Grund: §4§l" + Ban.getReason(player.getUniqueId().toString()) +
                        "\n§7Ende des Bans: §4§lPERMANENT");
            } else { // Temp Ban
                if(shouldBeUnban(player.getUniqueId().toString())) {
                    Ban.unbanPlayer(player.getName(), "Ban abgelaufen");
                } else {
                    player.disconnect(
                            "§7Du wurdest von HeroWars §4§lGebannt§7." +
                                    " \n\n §7Grund: §4§l" + Ban.getReason(player.getUniqueId().toString()) +
                                    "\n§7Ende des Bans: §4§l" + Ban.getDay(player.getUniqueId().toString()) + "." + Ban.getMonth(player.getUniqueId().toString()) + "." + Ban.getYear(player.getUniqueId().toString()));
                }
            }
        }
    }

    private boolean shouldBeUnban(String uuid) throws SQLException {
        if(Ban.getYear(uuid) != LocalDateTime.now().getYear())
            return Ban.getYear(uuid) < LocalDateTime.now().getYear();
        if(Ban.getMonth(uuid) != LocalDateTime.now().getMonthValue())
            return Ban.getMonth(uuid) < LocalDateTime.now().getMonthValue();
        if(Ban.getDay(uuid) <= LocalDateTime.now().getDayOfMonth())
            return true;
        return false;
    }

}
