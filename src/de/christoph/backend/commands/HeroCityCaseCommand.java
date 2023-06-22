package de.christoph.backend.commands;

import de.christoph.backend.HWBackend;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HeroCityCaseCommand extends Command {

    public HeroCityCaseCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission("herowars.admin")) {
            commandSender.sendMessage("§e§lHeroWars §7§l| §7Dazu hast du keine §cRechte§7.");
            return;
        }
        if(strings.length == 4) {
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

    private void sendWrongUsage(CommandSender player) {
        player.sendMessage("§e§lHeroWars §7§l| §7Benutze §e/herocitykiste add/remove/set/get <Spieler> <Anzahl> Normal/Plus§7.");
    }

    private void performAdd(CommandSender sender, String[] strings) throws SQLException {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(strings[1]);
        int amount = Integer.parseInt(strings[2]);
        if(player == null) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
            return;
        }
        if(strings[3].equalsIgnoreCase("normal")) {
            PreparedStatement preparedStatement = null;
            if(isInNormalDatabase(player.getUniqueId().toString())) {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "UPDATE `herocitycases` SET `amount` = ? WHERE `uuid` = ?"
                );
                preparedStatement.setDouble(1, amount + getNormalCaseAmount(player.getUniqueId().toString()));
                preparedStatement.setString(2, player.getUniqueId().toString());
            } else {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "INSERT INTO `herocitycases`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
                );
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setDouble(3, amount + getNormalCaseAmount(player.getUniqueId().toString()));
            }
            preparedStatement.execute();
        } else if(strings[3].equalsIgnoreCase("plus")) {
            PreparedStatement preparedStatement = null;
            if(isInNormalDatabase(player.getUniqueId().toString())) {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "UPDATE `herocitypluscases` SET `amount` = ? WHERE `uuid` = ?"
                );
                preparedStatement.setDouble(1, amount + getPlusCaseAmount(player.getUniqueId().toString()));
                preparedStatement.setString(2, player.getUniqueId().toString());
            } else {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "INSERT INTO `herocitypluscases`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
                );
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setDouble(3, amount + getPlusCaseAmount(player.getUniqueId().toString()));
            }
            preparedStatement.execute();
        } else
            sendWrongUsage(sender);
    }

    private void performRemove(CommandSender sender, String[] strings) throws SQLException {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(strings[1]);
        int amount = Integer.parseInt(strings[2]);
        if(player == null) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
            return;
        }
        if(strings[3].equalsIgnoreCase("normal")) {
            PreparedStatement preparedStatement = null;
            if(isInNormalDatabase(player.getUniqueId().toString())) {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "UPDATE `herocitycases` SET `amount` = ? WHERE `uuid` = ?"
                );
                preparedStatement.setDouble(1, getNormalCaseAmount(player.getUniqueId().toString()) - amount);
                preparedStatement.setString(2, player.getUniqueId().toString());
            } else {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "INSERT INTO `herocitycases`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
                );
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setDouble(3, getNormalCaseAmount(player.getUniqueId().toString()) - amount);
            }
            preparedStatement.execute();
        } else if(strings[3].equalsIgnoreCase("plus")) {
            PreparedStatement preparedStatement = null;
            if(isInNormalDatabase(player.getUniqueId().toString())) {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "UPDATE `herocitypluscases` SET `amount` = ? WHERE `uuid` = ?"
                );
                preparedStatement.setDouble(1, getPlusCaseAmount(player.getUniqueId().toString()) - amount);
                preparedStatement.setString(2, player.getUniqueId().toString());
            } else {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "INSERT INTO `herocitypluscases`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
                );
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setDouble(3, getPlusCaseAmount(player.getUniqueId().toString()) - amount);
            }
            preparedStatement.execute();
        } else
            sendWrongUsage(sender);
    }

    private void performGet(CommandSender sender, String[] strings) throws SQLException {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(strings[1]);
        int amount = Integer.parseInt(strings[2]);
        if(player == null) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
            return;
        }
        if(strings[3].equalsIgnoreCase("normal")) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Lobby Kisten: §e" + getNormalCaseAmount(player.getUniqueId().toString()));
        } else if(strings[3].equalsIgnoreCase("plus")) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Lobby+ Kisten: §e" + getPlusCaseAmount(player.getUniqueId().toString()));
        } else
            sendWrongUsage(sender);
    }

    private int getNormalCaseAmount(String uuid) throws SQLException {
        if(!isInNormalDatabase(uuid))
            return 0;
        PreparedStatement preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                "SELECT `amount` FROM `herocitycases` WHERE `uuid` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getInt("amount");
        return 0;
    }

    private int getPlusCaseAmount(String uuid) throws SQLException {
        if(!isInPlusDatabase(uuid))
            return 0;
        PreparedStatement preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                "SELECT `amount` FROM `herocitypluscases` WHERE `uuid` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return resultSet.getInt("amount");
        return 0;
    }

    private void performSet(CommandSender sender, String[] strings) throws SQLException {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(strings[1]);
        int amount = Integer.parseInt(strings[2]);
        if(player == null) {
            sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
            return;
        }
        if(strings[3].equalsIgnoreCase("normal")) {
            PreparedStatement preparedStatement = null;
            if(isInNormalDatabase(player.getUniqueId().toString())) {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "UPDATE `herocitycases` SET `amount` = ? WHERE `uuid` = ?"
                );
                preparedStatement.setDouble(1, amount);
                preparedStatement.setString(2, player.getUniqueId().toString());
            } else {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "INSERT INTO `herocitycases`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
                );
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setDouble(3, amount);
            }
            preparedStatement.execute();
        } else if(strings[3].equalsIgnoreCase("plus")) {
            PreparedStatement preparedStatement = null;
            if(isInPlusDatabase(player.getUniqueId().toString())) {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "UPDATE `herocitypluscases` SET `amount` = ? WHERE `uuid` = ?"
                );
                preparedStatement.setDouble(1, amount);
                preparedStatement.setString(2, player.getUniqueId().toString());
            } else {
                preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                        "INSERT INTO `herocitypluscases`(`uuid`, `name`, `amount`) VALUES (?,?,?)"
                );
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setDouble(3, amount);
            }
            preparedStatement.execute();
        } else
            sendWrongUsage(sender);
    }

    public boolean isInNormalDatabase(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                "SELECT `uuid` FROM `herocitycases` WHERE `uuid` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return true;
        return false;
    }

    public boolean isInPlusDatabase(String uuid) throws SQLException {
        PreparedStatement preparedStatement = HWBackend.getPlugin().shopMySql.getConnection().prepareStatement(
                "SELECT `uuid` FROM `herocitypluscases` WHERE `uuid` = ?"
        );
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            return true;
        return false;
    }

}
