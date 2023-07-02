/*    */ package de.christoph.bansystem.punishment;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import de.christoph.bansystem.Constant;
/*    */ import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
/*    */ 
/*    */ public class Kick {
/*    */   public String victim;
/*    */   
/*    */   public String reason;
/*    */   
/*    */   public String sender;
/*    */   
/*    */   public Kick(String victim, String reason, String sender) {
/* 16 */     this.victim = victim;
/* 17 */     this.reason = reason;
/* 18 */     this.sender = sender;
/* 19 */     performKick();
/*    */   }
/*    */   
/*    */   public void performKick() {
/* 23 */     ProxyServer.getInstance().getPlayer(UUID.fromString(this.victim))
/* 24 */       .disconnect("§7Du wurdest von HeroWars §4§lGekickt§7. \n\n §7Grund: §4§l" + this.reason);
/* 25 */     informServer();
/*    */   }
/*    */   
/*    */   public void informServer() {
/* 29 */     for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
/* 30 */       if (all.hasPermission(Constant.KICK_INFORM))
/* 31 */         all.sendMessage("§e§lHeroWars §7§l| §7Der Spieler §e" + 
/*    */             
/* 33 */             ProxyServer.getInstance().getPlayer(UUID.fromString(this.victim)).getName() + " §7wurde von §e" + this.sender + " §7wegen §c" + this.reason + " §7gekickt.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\schmi\Desktop\Allgemein\Server\TolerServer\Backup\BanSystem.jar!\de\christoph\backend\punish\Kick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */