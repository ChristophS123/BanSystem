/*     */ package de.christoph.bansystem.commands;
/*     */
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import de.christoph.bansystem.BanSystem;
/*     */ import net.md_5.bungee.api.CommandSender;
/*     */ import net.md_5.bungee.api.ProxyServer;
          import net.md_5.bungee.api.connection.ProxiedPlayer;
/*     */ import net.md_5.bungee.api.plugin.Command;
/*     */ 
/*     */ public class HeroCityCaseCommand extends Command {
/*     */   public HeroCityCaseCommand(String name) {
/*  16 */     super(name);
/*     */   }
/*     */   
/*     */   public void execute(CommandSender commandSender, String[] strings) {
/*  21 */     if (!commandSender.hasPermission("herowars.admin")) {
/*  22 */       commandSender.sendMessage("§e§lHeroWars §7§l| §7Dazu hast du keine §cRechte§7.");
/*     */       return;
/*     */     } 
/*  25 */     if (strings.length == 4) {
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
/*     */   private void sendWrongUsage(CommandSender player) {
/*  61 */     player.sendMessage("§e§lHeroWars §7§l| §7Benutze §e/herocitykiste add/remove/set/get <Spieler> <Anzahl> Normal/Plus§7.");
/*     */   }
/*     */   
/*     */   private void performAdd(CommandSender sender, String[] strings) throws SQLException {
/*  65 */     ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1]);
/*  66 */     int amount = Integer.parseInt(strings[2]);
/*  67 */     if (player == null) {
/*  68 */       sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
/*     */       return;
/*     */     } 
/*  71 */     if (strings[3].equalsIgnoreCase("normal")) {
/*  72 */       PreparedStatement preparedStatement = null;
/*  73 */       if (isInNormalDatabase(player.getUniqueId().toString())) {
/*  74 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `herocitycases` SET `amount` = ? WHERE `uuid` = ?");
/*  77 */         preparedStatement.setDouble(1, (amount + getNormalCaseAmount(player.getUniqueId().toString())));
/*  78 */         preparedStatement.setString(2, player.getUniqueId().toString());
/*     */       } else {
/*  80 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `herocitycases`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/*  83 */         preparedStatement.setString(1, player.getUniqueId().toString());
/*  84 */         preparedStatement.setString(2, player.getName());
/*  85 */         preparedStatement.setDouble(3, (amount + getNormalCaseAmount(player.getUniqueId().toString())));
/*     */       } 
/*  87 */       preparedStatement.execute();
/*  88 */     } else if (strings[3].equalsIgnoreCase("plus")) {
/*  89 */       PreparedStatement preparedStatement = null;
/*  90 */       if (isInNormalDatabase(player.getUniqueId().toString())) {
/*  91 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `herocitypluscases` SET `amount` = ? WHERE `uuid` = ?");
/*  94 */         preparedStatement.setDouble(1, (amount + getPlusCaseAmount(player.getUniqueId().toString())));
/*  95 */         preparedStatement.setString(2, player.getUniqueId().toString());
/*     */       } else {
/*  97 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `herocitypluscases`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/* 100 */         preparedStatement.setString(1, player.getUniqueId().toString());
/* 101 */         preparedStatement.setString(2, player.getName());
/* 102 */         preparedStatement.setDouble(3, (amount + getPlusCaseAmount(player.getUniqueId().toString())));
/*     */       } 
/* 104 */       preparedStatement.execute();
/*     */     } else {
/* 106 */       sendWrongUsage(sender);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void performRemove(CommandSender sender, String[] strings) throws SQLException {
/* 110 */     ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1]);
/* 111 */     int amount = Integer.parseInt(strings[2]);
/* 112 */     if (player == null) {
/* 113 */       sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
/*     */       return;
/*     */     } 
/* 116 */     if (strings[3].equalsIgnoreCase("normal")) {
/* 117 */       PreparedStatement preparedStatement = null;
/* 118 */       if (isInNormalDatabase(player.getUniqueId().toString())) {
/* 119 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `herocitycases` SET `amount` = ? WHERE `uuid` = ?");
/* 122 */         preparedStatement.setDouble(1, (getNormalCaseAmount(player.getUniqueId().toString()) - amount));
/* 123 */         preparedStatement.setString(2, player.getUniqueId().toString());
/*     */       } else {
/* 125 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `herocitycases`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/* 128 */         preparedStatement.setString(1, player.getUniqueId().toString());
/* 129 */         preparedStatement.setString(2, player.getName());
/* 130 */         preparedStatement.setDouble(3, (getNormalCaseAmount(player.getUniqueId().toString()) - amount));
/*     */       } 
/* 132 */       preparedStatement.execute();
/* 133 */     } else if (strings[3].equalsIgnoreCase("plus")) {
/* 134 */       PreparedStatement preparedStatement = null;
/* 135 */       if (isInNormalDatabase(player.getUniqueId().toString())) {
/* 136 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `herocitypluscases` SET `amount` = ? WHERE `uuid` = ?");
/* 139 */         preparedStatement.setDouble(1, (getPlusCaseAmount(player.getUniqueId().toString()) - amount));
/* 140 */         preparedStatement.setString(2, player.getUniqueId().toString());
/*     */       } else {
/* 142 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `herocitypluscases`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/* 145 */         preparedStatement.setString(1, player.getUniqueId().toString());
/* 146 */         preparedStatement.setString(2, player.getName());
/* 147 */         preparedStatement.setDouble(3, (getPlusCaseAmount(player.getUniqueId().toString()) - amount));
/*     */       } 
/* 149 */       preparedStatement.execute();
/*     */     } else {
/* 151 */       sendWrongUsage(sender);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void performGet(CommandSender sender, String[] strings) throws SQLException {
/* 155 */     ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1]);
/* 156 */     int amount = Integer.parseInt(strings[2]);
/* 157 */     if (player == null) {
/* 158 */       sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
/*     */       return;
/*     */     } 
/* 161 */     if (strings[3].equalsIgnoreCase("normal")) {
/* 162 */       sender.sendMessage("§e§lHeroWars §7§l| §7Lobby Kisten: §e" + getNormalCaseAmount(player.getUniqueId().toString()));
/* 163 */     } else if (strings[3].equalsIgnoreCase("plus")) {
/* 164 */       sender.sendMessage("§e§lHeroWars §7§l| §7Lobby+ Kisten: §e" + getPlusCaseAmount(player.getUniqueId().toString()));
/*     */     } else {
/* 166 */       sendWrongUsage(sender);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getNormalCaseAmount(String uuid) throws SQLException {
/* 170 */     if (!isInNormalDatabase(uuid))
/* 171 */       return 0; 
/* 172 */     PreparedStatement preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("SELECT `amount` FROM `herocitycases` WHERE `uuid` = ?");
/* 175 */     preparedStatement.setString(1, uuid);
/* 176 */     ResultSet resultSet = preparedStatement.executeQuery();
/* 177 */     if (resultSet.next())
/* 178 */       return resultSet.getInt("amount"); 
/* 179 */     return 0;
/*     */   }
/*     */   
/*     */   private int getPlusCaseAmount(String uuid) throws SQLException {
/* 183 */     if (!isInPlusDatabase(uuid))
/* 184 */       return 0; 
/* 185 */     PreparedStatement preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("SELECT `amount` FROM `herocitypluscases` WHERE `uuid` = ?");
/* 188 */     preparedStatement.setString(1, uuid);
/* 189 */     ResultSet resultSet = preparedStatement.executeQuery();
/* 190 */     if (resultSet.next())
/* 191 */       return resultSet.getInt("amount"); 
/* 192 */     return 0;
/*     */   }
/*     */   
/*     */   private void performSet(CommandSender sender, String[] strings) throws SQLException {
/* 196 */     ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1]);
/* 197 */     int amount = Integer.parseInt(strings[2]);
/* 198 */     if (player == null) {
/* 199 */       sender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist §cOffline§7.");
/*     */       return;
/*     */     } 
/* 202 */     if (strings[3].equalsIgnoreCase("normal")) {
/* 203 */       PreparedStatement preparedStatement = null;
/* 204 */       if (isInNormalDatabase(player.getUniqueId().toString())) {
/* 205 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `herocitycases` SET `amount` = ? WHERE `uuid` = ?");
/* 208 */         preparedStatement.setDouble(1, amount);
/* 209 */         preparedStatement.setString(2, player.getUniqueId().toString());
/*     */       } else {
/* 211 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `herocitycases`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/* 214 */         preparedStatement.setString(1, player.getUniqueId().toString());
/* 215 */         preparedStatement.setString(2, player.getName());
/* 216 */         preparedStatement.setDouble(3, amount);
/*     */       } 
/* 218 */       preparedStatement.execute();
/* 219 */     } else if (strings[3].equalsIgnoreCase("plus")) {
/* 220 */       PreparedStatement preparedStatement = null;
/* 221 */       if (isInPlusDatabase(player.getUniqueId().toString())) {
/* 222 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("UPDATE `herocitypluscases` SET `amount` = ? WHERE `uuid` = ?");
/* 225 */         preparedStatement.setDouble(1, amount);
/* 226 */         preparedStatement.setString(2, player.getUniqueId().toString());
/*     */       } else {
/* 228 */         preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("INSERT INTO `herocitypluscases`(`uuid`, `name`, `amount`) VALUES (?,?,?)");
/* 231 */         preparedStatement.setString(1, player.getUniqueId().toString());
/* 232 */         preparedStatement.setString(2, player.getName());
/* 233 */         preparedStatement.setDouble(3, amount);
/*     */       } 
/* 235 */       preparedStatement.execute();
/*     */     } else {
/* 237 */       sendWrongUsage(sender);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isInNormalDatabase(String uuid) throws SQLException {
/* 241 */     PreparedStatement preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("SELECT `uuid` FROM `herocitycases` WHERE `uuid` = ?");
/* 244 */     preparedStatement.setString(1, uuid);
/* 245 */     ResultSet resultSet = preparedStatement.executeQuery();
/* 246 */     if (resultSet.next())
/* 247 */       return true; 
/* 248 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isInPlusDatabase(String uuid) throws SQLException {
/* 252 */     PreparedStatement preparedStatement = (BanSystem.getInstance()).shopMySql.getConnection().prepareStatement("SELECT `uuid` FROM `herocitypluscases` WHERE `uuid` = ?");
/* 255 */     preparedStatement.setString(1, uuid);
/* 256 */     ResultSet resultSet = preparedStatement.executeQuery();
/* 257 */     if (resultSet.next())
/* 258 */       return true; 
/* 259 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\schmi\Desktop\Allgemein\Server\TolerServer\Backup\BanSystem.jar!\de\christoph\backend\commands\HeroCityCaseCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */