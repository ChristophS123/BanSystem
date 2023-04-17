package de.christoph.ban.listeners;

import de.christoph.ban.punish.Mute;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class MuteListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) throws SQLException {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if(event.getMessage().charAt(0) == '/')
            return;
        if(Mute.isMuted(player.getUniqueId().toString())) {
            if(Mute.getDay(player.getUniqueId().toString()) == 0) { // Perma Mute
                player.sendMessage(
                        "§7Du wurdest §4§lGemutet§7." +
                                " \n\n §7Grund: §4§l" + Mute.getReason(player.getUniqueId().toString()) +
                                "\n§7Ende des Mutes: §4§lPERMANENT");
                event.setCancelled(true);
            } else { // Temp Mute
                if(shouldBeUnMute(player.getUniqueId().toString())) {
                    Mute.unmutePlayer(player.getName(), "Mute abgelaufen");
                } else {
                    player.sendMessage(
                            "§7Du wurdest §4§lGemutet§7." +
                                    " \n\n §7Grund: §4§l" + Mute.getReason(player.getUniqueId().toString()) +
                                    "\n§7Ende des Mutes: §4§l" + Mute.getDay(player.getUniqueId().toString()) + "." + Mute.getMonth(player.getUniqueId().toString()) + "." + Mute.getYear(player.getUniqueId().toString()));
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean shouldBeUnMute(String uuid) throws SQLException {
        if(Mute.getYear(uuid) != LocalDateTime.now().getYear())
            return Mute.getYear(uuid) < LocalDateTime.now().getYear();
        if(Mute.getMonth(uuid) != LocalDateTime.now().getMonthValue())
            return Mute.getMonth(uuid) < LocalDateTime.now().getMonthValue();
        if(Mute.getDay(uuid) <= LocalDateTime.now().getDayOfMonth())
            return true;
        return false;
    }

}
