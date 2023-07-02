/*    */ package de.christoph.bansystem.commands;
/*    */
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.Constant;
import net.md_5.bungee.api.CommandSender;
/*    */ import net.md_5.bungee.api.plugin.Command;
/*    */ 
/*    */ public class BuglistCommand extends Command {
/*    */   public BuglistCommand(String name) {
/* 15 */     super(name);
/*    */   }
/*    */   
/*    */   public void execute(CommandSender commandSender, String[] strings) {
/* 20 */     if (commandSender.hasPermission(Constant.BUGLIST)) {
/* 21 */       if (strings.length == 0) {
/*    */         try {
/* 23 */           commandSender.sendMessage("§e§lBugs");
/* 24 */           commandSender.sendMessage("");
/* 25 */           commandSender.sendMessage("");
/* 26 */           PreparedStatement preparedStatement = (BanSystem.getInstance()).getMySQL().getConnection().prepareStatement("SELECT * FROM `bugs` WHERE 1");
/* 29 */           ResultSet resultSet = preparedStatement.executeQuery();
/* 30 */           while (resultSet.next()) {
/* 31 */             commandSender.sendMessage("§7" + resultSet.getInt("id") + " " + resultSet.getString("category"));
/* 32 */             commandSender.sendMessage("");
/*    */           } 
/* 34 */           commandSender.sendMessage("");
/* 35 */           commandSender.sendMessage("§e§lBugs");
/* 36 */         } catch (SQLException throwables) {
/* 37 */           commandSender.sendMessage("§e§lHeroWars §7§l| §7Ein Fehler ist aufgetreten.");
/* 38 */           throwables.printStackTrace();
/*    */         } 
/* 40 */       } else if (strings.length == 1) {
/* 41 */         if (strings[0].equalsIgnoreCase("help") || strings[0].equalsIgnoreCase("hilfe")) {
/* 42 */           sendHelp(commandSender);
/*    */           return;
/*    */         } 
/* 45 */         int id = 0;
/*    */         try {
/* 47 */           id = Integer.parseInt(strings[0]);
/* 48 */         } catch (NumberFormatException e) {
/* 49 */           commandSender.sendMessage("§e§lHeroWars §7§l| §7Dies ist keine gültige §cID§7.");
/*    */           return;
/*    */         } 
/*    */         try {
/* 53 */           PreparedStatement preparedStatement = (BanSystem.getInstance()).getMySQL().getConnection().prepareStatement("SELECT * FROM `bugs` WHERE `id` = ?");
/* 56 */           preparedStatement.setInt(1, id);
/* 57 */           ResultSet resultSet = preparedStatement.executeQuery();
/* 58 */           commandSender.sendMessage("");
/* 59 */           while (resultSet.next()) {
/* 60 */             commandSender.sendMessage("§7ID: §e" + resultSet.getInt("id"));
/* 61 */             commandSender.sendMessage("§7Kategorie: §e" + resultSet.getString("category"));
/* 62 */             commandSender.sendMessage("§7Reportet von: §e" + resultSet.getString("sender"));
/* 63 */             commandSender.sendMessage("");
/* 64 */             commandSender.sendMessage("§7Nachricht: §e" + resultSet.getString("message"));
/*    */           } 
/* 66 */           commandSender.sendMessage("");
/* 67 */         } catch (SQLException throwables) {
/* 68 */           throwables.printStackTrace();
/* 69 */           commandSender.sendMessage("§e§lHeroWars §7§l| §7Ein Fehler ist aufgetreten.");
/*    */         } 
/* 71 */       } else if (strings.length == 2) {
/* 72 */         if (!strings[0].equalsIgnoreCase("finish") && !strings[0].equalsIgnoreCase("delete")) {
/* 73 */           sendHelp(commandSender);
/*    */           return;
/*    */         } 
/* 76 */         int id = 0;
/*    */         try {
/* 78 */           id = Integer.parseInt(strings[1]);
/* 79 */         } catch (NumberFormatException e) {
/* 80 */           commandSender.sendMessage("§e§lHeroWars §7§l| §7Dies ist keine gültige §cID§7.");
/*    */           return;
/*    */         } 
/*    */         try {
/* 84 */           PreparedStatement preparedStatement = (BanSystem.getInstance()).getMySQL().getConnection().prepareStatement("DELETE FROM `bugs` WHERE `id` = ?");
/* 87 */           preparedStatement.setInt(1, id);
/* 88 */           preparedStatement.execute();
/* 89 */           commandSender.sendMessage("§e§lHeroWars §7§l| §7Der Bug wurde §agelöscht§7.");
/* 90 */         } catch (SQLException throwables) {
/* 91 */           throwables.printStackTrace();
/*    */         } 
/*    */       } 
/*    */     } else {
/* 95 */       commandSender.sendMessage("§e§lHeroWars §7§l| §7Dazu hast du keine §cRechte§7.");
/*    */     } 
/*    */   }
/*    */   
/*    */   private void sendHelp(CommandSender commandSender) {}
/*    */ }


/* Location:              C:\Users\schmi\Desktop\Allgemein\Server\TolerServer\Backup\BanSystem.jar!\de\christoph\backend\commands\BuglistCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */