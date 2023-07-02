/*    */ package de.christoph.bansystem.commands;
/*    */
/*    */ import de.christoph.bansystem.Constant;
import de.christoph.bansystem.punishment.Kick;
/*    */ import net.md_5.bungee.api.CommandSender;
/*    */ import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
/*    */ import net.md_5.bungee.api.plugin.Command;
/*    */ 
/*    */ public class KickCommand extends Command {
/*    */   public KickCommand(String name) {
/* 13 */     super(name);
/*    */   }
/*    */   
/*    */   public void execute(CommandSender commandSender, String[] strings) {
/* 18 */     if (commandSender.hasPermission(Constant.KICK)) {
/* 19 */       if (strings.length == 2) {
/* 20 */         ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[0]);
/* 21 */         if (target != null) {
/* 22 */           if (target.hasPermission(Constant.KICK_BYPASS) && !commandSender.hasPermission("herowars.admin")) {
/* 23 */             target.sendMessage("§e§lHeroWars §7§l| §7Der Spieler §e" + commandSender.getName() + " §7hat versucht dich zu kicken.");
/*    */             return;
/*    */           } 
/* 26 */           new Kick(target.getUniqueId().toString(), strings[1], commandSender.getName());
/*    */         } else {
/* 28 */           commandSender.sendMessage("§e§lHeroWars §7§l| §7Dieser Spieler ist nicht auf dem §cNetzwerk§7.");
/*    */         } 
/*    */       } else {
/* 30 */         commandSender.sendMessage("§e§lHeroWars §7§l| §7Bitte benutze §e/kick <Spieler> <Grund>§7.");
/*    */       } 
/*    */     } else {
/* 32 */       commandSender.sendMessage("§e§lHeroWars §7§l| §7Dazu hast du keine §cRechte§7.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\schmi\Desktop\Allgemein\Server\TolerServer\Backup\BanSystem.jar!\de\christoph\backend\commands\KickCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */