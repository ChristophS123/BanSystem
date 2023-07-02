package de.christoph.bansystem.punishment;

import com.google.gson.stream.JsonReader;
import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.utils.MinecraftPlayerUtil;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


public abstract class Punishment {

    protected String sender;
    protected String victim;
    protected String reason;
    protected Date punishDate;
    protected Date endingDate;
    protected PunishmentType punishmentType;

    public Punishment(PunishmentType punishmentType, String victim, String sender, String reason, Date punishDate, Date endingDate) {
        this.sender = sender;
        this.victim = victim;
        this.reason = reason;
        this.punishDate = punishDate;
        this.endingDate = endingDate;
        this.punishmentType = punishmentType;
    }

    public abstract void perform();

    public void removeFromDatabase() throws SQLException {
        Punishment punishment = getPunishment(punishmentType, victim);
        if(punishment != null)
            punishment.addToHistory();
        PreparedStatement preparedStatement = BanSystem
                .getInstance()
                .getMySQL()
                .getConnection()
                .prepareStatement("DELETE FROM `" + punishmentType.getTableName() + "` WHERE `victim` = ?");
        preparedStatement.setString(1, victim);
        preparedStatement.execute();
    }

    public void addToHistory() throws SQLException {
        PreparedStatement preparedStatement = BanSystem
                .getInstance()
                .getMySQL()
                .getConnection()
                .prepareStatement("INSERT INTO `histories` (`punishmentType`, `victim`, `victimname`, `sender`, `reason`, `punishDate`, `endingDate`, `punishTime`, `endingTime`) VALUES (?,?,?,?,?,?,?,?,?)");
        preparedStatement.setString(1, punishmentType.getTableName());
        preparedStatement.setString(2, victim);
        preparedStatement.setString(3, MinecraftPlayerUtil.getPlayerNameByUUID((victim)));
        preparedStatement.setString(4, sender);
        preparedStatement.setString(5, reason);
        preparedStatement.setDate(6, new java.sql.Date(punishDate.getTime()));
        preparedStatement.setDate(7, new java.sql.Date(endingDate.getTime()));
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String punishTime = formatter.format(punishDate);
        String endingTime = formatter.format(endingDate);
        preparedStatement.setTime(8, Time.valueOf(punishTime));
        preparedStatement.setTime(9, Time.valueOf(endingTime));
        preparedStatement.execute();
    }

    public static boolean containsInDatabase(PunishmentType punishmentType, String victim) throws SQLException {
        PreparedStatement preparedStatement = BanSystem
                .getInstance().
                getMySQL().
                getConnection().
                prepareStatement("SELECT * FROM `" + punishmentType.getTableName() + "` WHERE `victim` = ?");
        preparedStatement.setString(1, victim);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return true;
        return false;
    }

    public static Punishment getPunishment(PunishmentType punishmentType, String victim) throws SQLException {
        PreparedStatement preparedStatement = BanSystem
                .getInstance()
                .getMySQL()
                .getConnection()
                .prepareStatement("SELECT * FROM `" + punishmentType.getTableName() + "` WHERE `victim` = ?");
        preparedStatement.setString(1, victim);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            if(punishmentType == PunishmentType.BAN) {
                LocalDate punishDate = resultSet.getDate("punishDate").toLocalDate();
                LocalTime punishTime = resultSet.getTime("punishTime").toLocalTime();
                LocalDate endingDate = resultSet.getDate("endingDate").toLocalDate();
                LocalTime endingTime = resultSet.getTime("endingTime").toLocalTime();
                Date punishDateTime = Date.from(punishDate.atTime(punishTime).atZone(ZoneId.systemDefault()).toInstant());
                Date endingDateTime = Date.from(endingDate.atTime(endingTime).atZone(ZoneId.systemDefault()).toInstant());
                return new Ban(
                        resultSet.getString("victim"),
                        resultSet.getString("sender"),
                        resultSet.getString("reason"),
                        punishDateTime,
                        endingDateTime
                );
            } else if(punishmentType == PunishmentType.MUTE) {
                LocalDate punishDate = resultSet.getDate("punishDate").toLocalDate();
                LocalTime punishTime = resultSet.getTime("punishTime").toLocalTime();
                LocalDate endingDate = resultSet.getDate("endingDate").toLocalDate();
                LocalTime endingTime = resultSet.getTime("endingTime").toLocalTime();
                Date punishDateTime = Date.from(punishDate.atTime(punishTime).atZone(ZoneId.systemDefault()).toInstant());
                Date endingDateTime = Date.from(endingDate.atTime(endingTime).atZone(ZoneId.systemDefault()).toInstant());
                return new Mute(
                        resultSet.getString("victim"),
                        resultSet.getString("sender"),
                        resultSet.getString("reason"),
                        punishDateTime,
                        endingDateTime
                );
            }
        }
        return null;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public Date getEndingDate() {
        return endingDate;
    }

}
