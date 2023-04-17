package de.christoph.ban.listeners;

import de.christoph.ban.Constant;
import de.christoph.ban.BanSystem;
import de.christoph.ban.support.Support;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SupportListener implements Listener {

    @EventHandler
    public void onPlayerChat(net.md_5.bungee.api.event.ChatEvent event) {
        if(!(event.getSender() instanceof ProxiedPlayer))
            return;
        if(event.getMessage().charAt(0) == '/')
            return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        Support support = null;
        for(Support i : BanSystem.getPlugin().supports) {
            if(i.getPlayer() == player || i.getSupporter() == player) {
                support = i;
            }
        }
        if(support == null)
            return;
        event.setCancelled(true);
        support.getPlayer().sendMessage("§e" + player.getName() + "§7» " + event.getMessage());
        support.getSupporter().sendMessage("§e" + player.getName() + "§7» " + event.getMessage());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        for(Support i : BanSystem.getPlugin().supports) {
            if(i.getPlayer() == player ) {
                i.finishSupport();
                player.sendMessage(Constant.PREFIX + "§7Der Supporter hat das Netzwerk verlassen. ");
            }
        }
    }

}
