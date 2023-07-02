/*     */ package de.christoph.bansystem.commands;
/*     */
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import de.christoph.bansystem.BanSystem;
import net.md_5.bungee.api.CommandSender;
/*     */ import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
/*     */ import net.md_5.bungee.api.plugin.Command;
/*     */ 
/*     */ public class TradingCardCommand extends Command {
/*     */   public TradingCardCommand(String name) {
/*  16 */     super(name);
/*     */   }
/*     */   
/*     */   public void execute(CommandSender commandSender, String[] strings) {
/*  21 */     if (!commandSender.hasPermission("herowars.admin")) {
/*  22 */       commandSender.sendMessage("§e§lHeroWars §7§l| §7Dazu hast du keine §cRechte§7.");
/*     */       return;
/*     */     } 
/*  25 */     if (strings.length == 3) {
/*  26 */       if (strings[0].equalsIgnoreCase("add")) {
/*     */         try {
/*  28 */           performAdd(commandSender, strings);
/*  29 */         } catch (SQLException e) {
/*  30 */           throw new RuntimeException(e);
/*     */         } 
/*  33 */       } else if (strings[0].equalsIgnoreCase("set")) {
/*     */         try {
/*  35 */           performSet(commandSender, strings);
/*  36 */         } catch (SQLException e) {
/*  37 */           throw new RuntimeException(e);
/*     */         } 
/*  40 */       } else if (strings[0].equalsIgnoreCase("remove")) {
/*     */         try {
/*  42 */           performRemove(commandSender, strings);
/*  43 */         } catch (SQLException e) {
/*  44 */           throw new RuntimeException(e);
/*     */         } 
/*  47 */       } else if (strings[0].equalsIgnoreCase("get")) {
/*     */         try {
/*  49 */           performGet(commandSender, strings);
/*  50 */         } catch (SQLException e) {
/*  51 */           throw new RuntimeException(e);
/*     */         } 
/*     */       } else {
/*  55 */         sendWrongUsage(commandSender);
/*     */       } 
/*     */     } else {
/*  57 */       sendWrongUsage(commandSender);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void performGet(CommandSender sender, String[] strings) throws SQLException {
/*  61 */     ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1]);
/*  62 */     int amount = Integer.parseInt(strings[2]);
/*  63 */     if (player == null) {
/*  64 */       sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
/*     */       return;
/*     */     } 
/*  67 */     sender.sendMessage("§e§lHeroWars §7§l| §7Sammelkarten Packs Kisten: §e" + getPackAmount(player.getUniqueId().toString()));
/*     */   }
/*     */   
/*     */   private void performRemove(CommandSender sender, String[] strings) throws SQLException {
/*  71 */     ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1]);
/*  72 */     int amount = Integer.parseInt(strings[2]);
/*  73 */     if (player == null) {
/*  74 */       sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
/*     */       return;
/*     */     } 
/*  77 */     PreparedStatement preparedStatement = null;
/*  78 */     if (isInDatabase(player.getUniqueId().toString())) {
/*  79 */       preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `herocitycases` SET `amount` = ? WHERE `uuid` = ?");
/*  82 */       preparedStatement.setDouble(1, (getPackAmount(player.getUniqueId().toString()) - amount));
/*  83 */       preparedStatement.setString(2, player.getUniqueId().toString());
/*     */     } else {
/*  85 */       preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `herocitycases`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/*  88 */       preparedStatement.setString(1, player.getUniqueId().toString());
/*  89 */       preparedStatement.setString(2, player.getName());
/*  90 */       preparedStatement.setDouble(3, (getPackAmount(player.getUniqueId().toString()) - amount));
/*     */     } 
/*  92 */     preparedStatement.execute();
/*     */   }
/*     */   
/*     */   private void performSet(CommandSender sender, String[] strings) throws SQLException {
/*  96 */     ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1]);
/*  97 */     int amount = Integer.parseInt(strings[2]);
/*  98 */     if (player == null) {
/*  99 */       sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
/*     */       return;
/*     */     } 
/* 102 */     PreparedStatement preparedStatement = null;
/* 103 */     if (isInDatabase(player.getUniqueId().toString())) {
/* 104 */       preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `tradingcardpacks` SET `amount` = ? WHERE `uuid` = ?");
/* 107 */       preparedStatement.setDouble(1, amount);
/* 108 */       preparedStatement.setString(2, player.getUniqueId().toString());
/*     */     } else {
/* 110 */       preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `tradingcardpacks`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/* 113 */       preparedStatement.setString(1, player.getUniqueId().toString());
/* 114 */       preparedStatement.setString(2, player.getName());
/* 115 */       preparedStatement.setDouble(3, amount);
/*     */     } 
/* 117 */     preparedStatement.execute();
/*     */   }
/*     */   
/*     */   private void performAdd(CommandSender sender, String[] strings) throws SQLException {
/* 121 */     ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1]);
/* 122 */     int amount = Integer.parseInt(strings[2]);
/* 123 */     if (player == null) {
/* 124 */       sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
/*     */       return;
/*     */     } 
/* 127 */     PreparedStatement preparedStatement = null;
/* 128 */     if (isInDatabase(player.getUniqueId().toString())) {
/* 129 */       preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `tradingcardpacks` SET `amount` = ? WHERE `uuid` = ?");
/* 132 */       preparedStatement.setDouble(1, (amount + getPackAmount(player.getUniqueId().toString())));
/* 133 */       preparedStatement.setString(2, player.getUniqueId().toString());
/*     */     } else {
/* 135 */       preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `tradingcardpacks`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/* 138 */       preparedStatement.setString(1, player.getUniqueId().toString());
/* 139 */       preparedStatement.setString(2, player.getName());
/* 140 */       preparedStatement.setDouble(3, (amount + getPackAmount(player.getUniqueId().toString())));
/*     */     } 
/* 142 */     preparedStatement.execute();
/*     */   }
/*     */   
/*     */   private int getPackAmount(String uuid) throws SQLException {
/* 146 */     if (!isInDatabase(uuid))
/* 147 */       return 0; 
/* 148 */     PreparedStatement preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("SELECT `amount` FROM `tradingcardpacks` WHERE `uuid` = ?");
/* 151 */     preparedStatement.setString(1, uuid);
/* 152 */     ResultSet resultSet = preparedStatement.executeQuery();
/* 153 */     if (resultSet.next())
/* 154 */       return resultSet.getInt("amount"); 
/* 155 */     return 0;
/*     */   }
/*     */   
/*     */   private void sendWrongUsage(CommandSender player) {
/* 159 */     player.sendMessage("§e§lHeroWars §7§l| §7Benutze §e/sammelkartenpacks add/remove/set/get <Spieler> <Anzahl>§7.");
/*     */   }
/*     */   
/*     */   public boolean isInDatabase(String uuid) throws SQLException {
/* 163 */     PreparedStatement preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("SELECT `uuid` FROM `tradingcardpacks` WHERE `uuid` = ?");
/* 166 */     preparedStatement.setString(1, uuid);
/* 167 */     ResultSet resultSet = preparedStatement.executeQuery();
/* 168 */     if (resultSet.next())
/* 169 */       return true; 
/* 170 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\schmi\Desktop\Allgemein\Server\TolerServer\Backup\BanSystem.jar!\de\christoph\backend\commands\TradingCardCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */