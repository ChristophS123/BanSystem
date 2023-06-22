package de.christoph.backend.commands;

import de.christoph.backend.HWBackend;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradingCardCommand extends Command {

    public TradingCardCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission("herowars.admin")) {
            commandSender.sendMessage("§e§lHeroWars §7§l| §7Dazu hast du keine §cRechte§7.");
            return;
        }
        if(strings.length == 3) {
            if(strings[0].equalsIgnoreCase("add")) {
                try {
                    performAdd(commandSender, strings);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(strings[0].equalsIgnoreCase("set")) {
                try {
                    performSet(commandSender, strings);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(strings[0].equalsIgnoreCase("remove")) {
                try {
                    performRemove(commandSender, strings);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(strings[0].equalsIgnoreCase("get")) {
                try {
                    performGet(commandSender, strings);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                sendWrongUsage(commandSender);
        } else
            sendWrongUsage(commandSender);
    }

    private void performGet(CommandSender sender, String[] strings) throws SQLException {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(strings[1]);
        int amount = Integer.parseInt(strings[2]);
        if(player == null) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
            return;
        }
        sender.sendMessage("§e§lHeroWars §7§l| §7Sammelkarten Packs Kisten: §e" + getPackAmount(player.getUniqueId().toString()));
    }

    private void performRemove(CommandSender sender, String[] strings) throws SQLException {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(strings[1]);
        int amount = Integer.parseInt(strings[2]);
        if(player == null) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
            return;
        }
        PreparedStatement preparedStatement = null;
        if(isInDatabase(player.getUniqueId().toString())) {
            preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                    "UPDATE `herocitycases` SET `amount` = ? WHERE `uuid` = ?"
            );
            preparedStatement.setDouble(1, getPackAmount(player.getUniqueId().toString()) - amount);
            preparedStatement.setString(2, player.getUniqueId().toString());
        } else {
            preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                    "INSERT INTO `herocitycases`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
            );
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setDouble(3, getPackAmount(player.getUniqueId().toString()) - amount);
        }
        preparedStatement.execute();
    }

    private void performSet(CommandSender sender, String[] strings) throws SQLException {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(strings[1]);
        int amount = Integer.parseInt(strings[2]);
        if(player == null) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
            return;
        }
            PreparedStatement preparedStatement = null;
            if(isInDatabase(player.getUniqueId().toString())) {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "UPDATE `tradingcardpacks` SET `amount` = ? WHERE `uuid` = ?"
                );
                preparedStatement.setDouble(1, amount);
                preparedStatement.setString(2, player.getUniqueId().toString());
            } else {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "INSERT INTO `tradingcardpacks`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
                );
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setDouble(3, amount);
            }
            preparedStatement.execute();
    }

    private void performAdd(CommandSender sender, String[] strings) throws SQLException {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(strings[1]);
        int amount = Integer.parseInt(strings[2]);
        if (player == null) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
            return;
        }
        PreparedStatement preparedStatement = null;
        if (isInDatabase(player.getUniqueId().toString())) {
            preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                    "UPDATE `tradingcardpacks` SET `amount` = ? WHERE `uuid` = ?"
            );
            preparedStatement.setDouble(1, amount + getPackAmount(player.getUniqueId().toString()));
            preparedStatement.setString(2, player.getUniqueId().toString());
        } else {
            preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                    "INSERT INTO `tradingcardpacks`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
            );
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setDouble(3, amount + getPackAmount(player.getUniqueId().toString()));
        }
        preparedStatement.execute();
    }

    private int getPackAmount(String uuid) throws SQLException {
        if(!isInDatabase(uuid))
            return 0;
        PreparedStatement preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                "SELECT `amount` FROM `tradingcardpacks` WHERE `uuid` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getInt("amount");
        return 0;
    }

    private void sendWrongUsage(CommandSender player) {
        player.sendMessage("§e§lHeroWars §7§l| §7Benutze §e/sammelkartenpacks add/remove/set/get <Spieler> <Anzahl>§7.");
    }

    public boolean isInDatabase(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                "SELECT `uuid` FROM `tradingcardpacks` WHERE `uuid` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return true;
        return false;
    }

}
