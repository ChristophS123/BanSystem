/*    */ package de.christoph.bansystem.commands;
/*    */
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import de.christoph.bansystem.BanSystem;
import net.md_5.bungee.api.CommandSender;
/*    */ import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
/*    */ import net.md_5.bungee.api.plugin.Plugin;
/*    */ 
/*    */ public class BugReportCommand extends Command {
/* 16 */   public ArrayList<CommandSender> bugReportSenders = new ArrayList<>();
/*    */   
/*    */   public BugReportCommand(String name) {
/* 19 */     super(name);
/*    */   }
/*    */   
/*    */   public void execute(final CommandSender commandSender, String[] strings) {
/* 24 */     if (strings.length > 2) {
/* 25 */       if (this.bugReportSenders.contains(commandSender)) {
/* 26 */         commandSender.sendMessage("§e§lHeroWars §7§l| §7Du kannst nur jede Minute einen Bug reporten.");
/*    */         return;
/*    */       } 
/* 29 */       this.bugReportSenders.add(commandSender);
/* 30 */       String category = strings[0];
/* 31 */       String message = "";
/* 32 */       int i = 0;
/* 33 */       for (String word : strings) {
/* 34 */         i++;
/* 35 */         if (i != 1)
/* 36 */           message = message + word + " "; 
/*    */       } 
/*    */       try {
/* 40 */         PreparedStatement preparedStatement = (BanSystem.getInstance()).getMySQL().getConnection().prepareStatement("INSERT INTO `bugs`(`sender`, `category`, `message`) VALUES (?,?,?)");
/* 43 */         preparedStatement.setString(1, commandSender.getName());
/* 44 */         preparedStatement.setString(2, category);
/* 45 */         preparedStatement.setString(3, message);
/* 46 */         preparedStatement.execute();
/* 47 */         commandSender.sendMessage("§e§lHeroWars §7§l| §7Vielen Dank für den Report!");
/* 48 */       } catch (SQLException throwables) {
/* 49 */         commandSender.sendMessage("§e§lHeroWars §7§l| §7Ein Fehler ist aufgetreten. Bitte reporte den Bug auf unserem Discord Server.");
/* 50 */         throwables.printStackTrace();
/*    */       } 
/* 52 */       ProxyServer.getInstance().getScheduler().schedule((Plugin)BanSystem.getInstance(), new Runnable() {
/*    */             public void run() {
/* 55 */               BugReportCommand.this.bugReportSenders.remove(commandSender);
/*    */             }
/*    */           },  60L, TimeUnit.SECONDS);
/*    */     } else {
/* 59 */       commandSender.sendMessage("§e§lHeroWars §7§l| §7Bitte benutze §e/bugreport <Spielmodus> <Nachricht>§7.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\schmi\Desktop\Allgemein\Server\TolerServer\Backup\BanSystem.jar!\de\christoph\backend\commands\BugReportCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */