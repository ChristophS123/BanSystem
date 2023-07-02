package de.christoph.bansystem.punishment.reasons;

import de.christoph.bansystem.Constant;
import de.christoph.bansystem.punishment.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Reason {

    private int id;
    private String name;
    private long duration; // Duration in seconds
    private PunishmentType punishmentType;

    public Reason(int id, String name, long duration, PunishmentType punishmentType) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.punishmentType = punishmentType;
    }

    public boolean isPermitted(CommandSender player) {
        if(punishmentType == PunishmentType.BAN)
            return (player.hasPermission(Constant.BAN_REASON_PERMISSION + id));
        else
            return (player.hasPermission(Constant.MUTE_REASON_PERMISSION + id));
    }

    public Date getEndDate() {
        Duration duration = Duration.ofSeconds(this.duration);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plus(duration);
        return Date.from(future.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String formatDurationDate() {
        long seconds = duration;
        Duration duration = Duration.ofSeconds(seconds);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plus(duration);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH:mm:ss");
        String endDate = future.format(formatter);
        return endDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDuration() {
        return duration;
    }

    public PunishmentType getReasonType() {
        return punishmentType;
    }

    // TODO Ban ending Date wird falsch angezeigt und deshalb wird spieler nicht enbannt


}
