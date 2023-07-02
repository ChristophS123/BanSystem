package de.christoph.bansystem.punishment;

public enum PunishmentType {

    BAN("bans"), MUTE("mutes");

    private String tableName;

    PunishmentType(String name) {
        this.tableName = name;
    }

    public String getTableName() {
        return tableName;
    }

    public static PunishmentType convert(String string) {
        if(string == "BAN")
            return PunishmentType.BAN;
        else if(string == "MUTE")
            return PunishmentType.MUTE;
        return null;
    }

}
