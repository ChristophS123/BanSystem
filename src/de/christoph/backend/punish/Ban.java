package de.christoph.backend.punish;

import de.christoph.backend.HWBackend;
import de.christoph.backend.Constant;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Ban {

    public String victimUUID;
    public String victimName;
    public String reason;
    public String sender;
    public int day;
    public int month;
    public int year;

    //Online Ban
    public Ban(String victim, String victimName, String reason, String sender) {
        this.victimUUID = victim;
        this.victimName = victimName;
        this.reason = reason;
        this.sender = sender;
        try {
            performOnlineBan();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Offline Ban
    public Ban(String victim, String victimName, String reason) throws SQLException {
        this.victimUUID = victim;
        this.victimName = victimName;
        this.reason = reason;
        performOfflineBan();
    }

    //Temp Ban
    public Ban(String victim, String victimName, String reason, String sender, int day, int month, int year) {
        this.victimUUID = victim;
        this.victimName = victimName;
        this.reason = reason;
        this.sender = sender;
        this.day = day;
        this.month = month;
        this.year = year;
        try {
            performTempban();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void performTempban() throws SQLException {
        PreparedStatement preparedStatement = null;
        if(isInDatabase(victimUUID)) {
            preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                    "UPDATE `bans` SET `victimname` = ?, `sender` = ?, `banned` = ?, `reason` = ?, `day` = ?, `month` = ?, `year` = ? WHERE `victim` = ?"
            );
            preparedStatement.setString(1, victimName);
            preparedStatement.setString(2, sender);
            preparedStatement.setInt(3, 1); // second 1 = true
            preparedStatement.setString(4, victimUUID);
            preparedStatement.setInt(5, day);
            preparedStatement.setInt(6, month);
            preparedStatement.setInt(7, year);
            preparedStatement.setString(8, victimUUID);
        } else {
            preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                    "INSERT INTO `bans`(`victim`, `victimname`, `sender`, `banned`, `reason`, `day`, `month`, `year`) VALUES (?,?,?,?,?,?,?,?)"
            );
            preparedStatement.setString(1, victimUUID);
            preparedStatement.setString(2, victimName);
            preparedStatement.setString(3, sender);
            preparedStatement.setInt(4, 1); //Second 1 = true
            preparedStatement.setString(5, reason);
            preparedStatement.setInt(6, day);
            preparedStatement.setInt(7, month);
            preparedStatement.setInt(8, year);
        }
        preparedStatement.execute();
        BungeeCord.getInstance().getPlayer(UUID.fromString(victimUUID)).
                disconnect("§7Du wurdest von HeroWars §4§lGebannt§7. \n\n §7Grund: §4§l" + reason + "\n§7Ende des Bans: §4§l" + day + "." + month + "." + year);
        informServer();
    }

    private void performOnlineBan() throws SQLException {
        PreparedStatement preparedStatement = null;
        if(isInDatabase(victimUUID)) {
            preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                    "UPDATE `bans` SET `victimname` = ?, `sender` = ?, `banned` = ?, `reason` = ?, `day` = ?, `month` = ?, `year` = ? WHERE `victim` = ?"
            );
            preparedStatement.setString(1, victimName);
            preparedStatement.setString(2, sender);
            preparedStatement.setInt(3, 1); //Second 1 = true
            preparedStatement.setString(4, reason);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7, 0);
            preparedStatement.setString(8, victimUUID);
        } else {
            preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "INSERT INTO `bans`(`victim`, `victimname`, `sender`, `banned`, `reason`, `day`, `month`, `year`) VALUES (?,?,?,?,?,?,?,?)"
            );
            preparedStatement.setString(1, victimUUID);
            preparedStatement.setString(2, victimName);
            preparedStatement.setString(3, sender);
            preparedStatement.setInt(4, 1); //Second 1 = true
            preparedStatement.setString(5, reason);
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7, 0);
            preparedStatement.setInt(8, 0);
        }
        preparedStatement.execute();
        BungeeCord.getInstance().getPlayer(UUID.fromString(victimUUID)).
                disconnect("§7Du wurdest von HeroWars §4§lGebannt§7. \n\n §7Grund: §4§l" + reason + "\n§7Ende des Bans: §4§lPERMANENT");
        informServer();
    }

    private void performOfflineBan() throws SQLException {
        PreparedStatement preparedStatement = null;
        if(isInDatabase(victimUUID)) {
            preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "UPDATE `bans` SET `victimname` = ?, `banned` = ?, `reason` = ?, `day` = ?, `month` = ?, `year` = ? WHERE `victim` = ?"
            );
            preparedStatement.setString(1, victimName);
            preparedStatement.setInt(2, 1); //Second 1 = true
            preparedStatement.setString(3, reason);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            preparedStatement.setString(7, victimUUID);
        } else {
            preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                    "INSERT INTO `bans`(`victim`, `victimname`, `sender`, `banned`, `reason`, `day`, `month`, `year`) VALUES (?,?,?,?,?,?,?,?)"
            );
            preparedStatement.setString(1, victimUUID);
            preparedStatement.setString(2, victimName);
            preparedStatement.setString(3, "offline");
            preparedStatement.setInt(4, 1); //Second 1 = true
            preparedStatement.setString(5, reason);
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7, 0);
            preparedStatement.setInt(8, 0);
        }
        preparedStatement.execute();
    }


    public static boolean isBanned(String uuid) throws SQLException {
        if(!isInDatabase(uuid))
            return false;
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
            "SELECT `banned` FROM `bans` WHERE `victim` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getInt("banned") == 1;
        return false;
    }

    public static boolean isNameBanned(String name) throws SQLException {
        if(!isNameInDatabase(name))
            return false;
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "SELECT `banned` FROM `bans` WHERE `victimname` = ?"
        );
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getInt("banned") == 1;
        return false;
    }

    public static boolean isInDatabase(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
            "SELECT `victim` FROM `bans` WHERE `victim` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return true;
        return false;
    }

    public static boolean isNameInDatabase(String name) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "SELECT `victimname` FROM `bans` WHERE `victimname` = ?"
        );
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return true;
        return false;
    }

    public static String getReason(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
            "SELECT `reason` FROM `bans` WHERE `victim` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getString("reason");
        return "";
    }

    public static String getSender(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "SELECT `sender` FROM `bans` WHERE `victim` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getString("sender");
        return "";
    }

    public static int getDay(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "SELECT `day` FROM `bans` WHERE `victim` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getInt("day");
        return 0;
    }

    public static int getMonth(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "SELECT `month` FROM `bans` WHERE `victim` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getInt("month");
        return 0;
    }

    public static int getYear(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "SELECT `year` FROM `bans` WHERE `victim` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getInt("year");
        return 0;
    }

    public static void unbanPlayer(String name, String sender) throws SQLException {
        if(!Ban.isNameBanned(name))
            return;
        PreparedStatement preparedStatement = HWBackend.getPlugin().banMySql.getConnection().prepareStatement(
                "UPDATE `bans` SET `banned` = ? WHERE `victimname` = ?"
        );
        preparedStatement.setInt(1, 0); //Second 0 = false
        preparedStatement.setString(2, name);
        preparedStatement.execute();
        unbanInformServer(name, sender);
    }

    public void informServer() {
        for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
            if(all.hasPermission(Constant.BAN_INFORM)) {
                all.sendMessage(
                        Constant.PREFIX +
                                "§7Der Spieler §e" + victimName +
                                " §7wurde von §e" + sender +
                                " §7wegen §c" + reason +
                                " §7gebannt."
                );
            }
        }
    }

    public static void unbanInformServer(String victim, String sender) {
        for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
            if(all.hasPermission(Constant.UNBAN_INFORM)) {
                all.sendMessage(
                        Constant.PREFIX +
                                "§7Der Spieler §e" + victim +
                                " §7wurde von §e" + sender +
                                " §7entbannt."
                );
            }
        }
    }

}
