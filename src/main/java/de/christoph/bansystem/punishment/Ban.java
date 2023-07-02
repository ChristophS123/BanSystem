package de.christoph.bansystem.punishment;

import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.Constant;
import de.christoph.bansystem.utils.MinecraftPlayerUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Ban extends Punishment {

    public Ban(String victim, String sender, String reason, Date punishTime, Date endingTime) {
        super(PunishmentType.BAN, victim, sender, reason, punishTime, endingTime);
    }

    @Override
    public void perform() {
        try {
            removeFromDatabase();
            PreparedStatement preparedStatement = BanSystem
                    .getInstance()
                    .getMySQL()
                    .getConnection()
                    .prepareStatement("INSERT INTO `" + punishmentType.getTableName() + "` (`victim`, `victimname`, `sender`, `reason`, `punishDate`, `endingDate`, `punishTime`, `endingTime`) VALUES (?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, victim);
            preparedStatement.setString(2, MinecraftPlayerUtil.getPlayerNameByUUID((victim)));
            preparedStatement.setString(3, sender);
            preparedStatement.setString(4, reason);
            preparedStatement.setDate(5, new java.sql.Date(punishDate.getTime()));
            preparedStatement.setDate(6, new java.sql.Date(endingDate.getTime()));
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String punishTime = formatter.format(punishDate);
            String endingTime = formatter.format(endingDate);
            preparedStatement.setTime(7, Time.valueOf(punishTime));
            preparedStatement.setTime(8, Time.valueOf(endingTime));
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
        UUID uuid = UUID.fromString(victim);
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        kickPlayer(player);
    }

    public void kickPlayer(ProxiedPlayer player) {
        if(player == null) {
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy:HH:mm:ss");
        String endDate = formatter.format(endingDate);
        player.disconnect("§7Du wurdest von HeroWars §4§lAusgeschlossen \n\n §7Grund: §4§l" + reason + "\n §7Ende des Bans: §4§l" + endDate);
    }

    public void inform() {
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if(player.hasPermission(Constant.BAN_INFORM_PERMISSION)) {
                player.sendMessage(Constant.PREFIX + "§e" + ProxyServer.getInstance().getPlayer(UUID.fromString(sender)) + " §7hat den Spieler §c" + ProxyServer.getInstance().getPlayer(UUID.fromString(victim)) + " §7für §c" + reason + " §7gebannt.");
            }
        }
    }

}
