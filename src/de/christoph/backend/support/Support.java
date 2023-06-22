package de.christoph.backend.support;

import de.christoph.backend.Constant;
import de.christoph.backend.HWBackend;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Support {

    private ProxiedPlayer player;
    private ProxiedPlayer supporter;

    public Support(ProxiedPlayer player) {
        this.player = player;
        supporter = null;
        HWBackend.getPlugin().supports.add(this);
    }

    public void supporterAccept(ProxiedPlayer supporter) {
        this.supporter = supporter;
        player.sendMessage("");
        player.sendMessage(Constant.PREFIX + "§7Der/Die Supporter/Supporterin §e" + supporter.getName() + " §7hat deinen Support angenommen. Du kannst jetzt mit ihm/ihr schreiben.");
        player.sendMessage(Constant.PREFIX + "§7Benutze §e/support beenden §7um deinen Support zu beenden.");
        player.sendMessage("");
        supporter.sendMessage("");
        supporter.sendMessage(Constant.PREFIX + "§7Du hast den Support von §e" + player.getName() + " §7angenommen. Du kannst jetzt mit ihm/ihr schreiben.");
        supporter.sendMessage(Constant.PREFIX + "§7Benutze §e/support beenden §7um deinen Support zu beenden.");
        supporter.sendMessage("");
    }

    public void finishSupport() {
        HWBackend.getPlugin().supports.remove(this);
        player.sendMessage(Constant.PREFIX + "§7Der Support wurde §cbeendet§7.");
        supporter.sendMessage(Constant.PREFIX + "§7Der Support wurde §cbeendet§7.");
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public ProxiedPlayer getSupporter() {
        return supporter;
    }

}
