package de.christoph.backend.punish;

import de.christoph.backend.Constant;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class Kick {

    public String victim;
    public String reason;
    public String sender;

    public Kick(String victim, String reason, String sender) {
        this.victim = victim;
        this.reason = reason;
        this.sender = sender;
        performKick();
    }

    public void performKick() {
        BungeeCord.getInstance().getPlayer(UUID.fromString(victim)).
                disconnect("§7Du wurdest von HeroWars §4§lGekickt§7. \n\n §7Grund: §4§l" + reason);
        informServer();
    }

    public void informServer() {
        for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
            if(all.hasPermission(Constant.KICK_INFORM)) {
                all.sendMessage(
                        Constant.PREFIX +
                                "§7Der Spieler §e" + BungeeCord.getInstance().getPlayer(UUID.fromString(victim)).getName() +
                                " §7wurde von §e" + sender +
                                " §7wegen §c" + reason +
                                " §7gekickt."
                );
            }
        }
    }

}
