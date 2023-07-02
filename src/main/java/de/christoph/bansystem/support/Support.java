package de.christoph.bansystem.support;

import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.Constant;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Support {
    private ProxiedPlayer player;

    private ProxiedPlayer supporter;

    public Support(ProxiedPlayer player) {
        this.player = player;
        this.supporter = null;
        (BanSystem.getInstance()).supports.add(this);
    }

    public void supporterAccept(ProxiedPlayer supporter) {
        this.supporter = supporter;
        this.player.sendMessage("");
        this.player.sendMessage(Constant.PREFIX + "§7Der/Die Supporter/Supporterin §e" + supporter.getName() + " §7hat deinen Support angenommen. Du kannst jetzt mit ihm schreiben.");
        this.player.sendMessage(Constant.PREFIX + "§7Benutze §e/support beenden §7um den Support zu beenden.");
        this.player.sendMessage("");
        supporter.sendMessage("");
        supporter.sendMessage(Constant.PREFIX + "§7Du hast den Support von §e" + this.player.getName() + " §7angenommen. Du kannst nun mit ihm/ihr schreiben.");
        supporter.sendMessage(Constant.PREFIX + "§7Benutze §e/support beenden §7um den Support zu beenden.");
        supporter.sendMessage("");
    }

    public void finishSupport() {
        (BanSystem.getInstance()).supports.remove(this);
        this.player.sendMessage(Constant.PREFIX + "§7Der Support wurde §cbeendet§7.");
        this.supporter.sendMessage(Constant.PREFIX + "§7Der Support wurde §cbeendet§7.");
    }

    public ProxiedPlayer getPlayer() {
        return this.player;
    }

    public ProxiedPlayer getSupporter() {
        return this.supporter;
    }
}