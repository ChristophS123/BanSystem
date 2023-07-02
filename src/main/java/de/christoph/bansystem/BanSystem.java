package de.christoph.bansystem;

import de.christoph.bansystem.commands.*;
import de.christoph.bansystem.listeners.BanListener;
import de.christoph.bansystem.mysql.MySQL;
import de.christoph.bansystem.punishment.reasons.ReasonManager;
import de.christoph.bansystem.support.Support;
import de.christoph.bansystem.support.SupportListener;
import de.christoph.bansystem.utils.YamlConfigBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BanSystem extends Plugin {

    private static BanSystem instance;

    private MySQL mySQL;
    public MySQL shopMySql;
    public ArrayList<Support> supports;
    private ReasonManager reasonManager;
    private YamlConfigBuilder reasonFile;

    @Override
    public void onEnable() {
        instance = this;
        mySQL = new MySQL("78.46.106.254", 3306, "teampanel", "admin", "EULVAq7WfowAv5yRhpPbsJt0c6Wfvx49");
        shopMySql = new MySQL("78.46.106.254", 3306, "shop", "admin", "EULVAq7WfowAv5yRhpPbsJt0c6Wfvx49");
        supports = new ArrayList<>();
        getProxy().getPluginManager().registerCommand(instance, new BanCommand("ban"));
        getProxy().getPluginManager().registerCommand(instance, new UnbanCommand("unban"));
        getProxy().getPluginManager().registerListener(instance, new BanListener());
        getProxy().getPluginManager().registerCommand(instance, new MuteCommand("mute"));
        getProxy().getPluginManager().registerCommand(instance, new UnmuteCommand("unmute"));
        getProxy().getPluginManager().registerCommand(instance, new ReportCommand("report"));
        getProxy().getPluginManager().registerCommand(instance, new ReportsCommand("reports"));
        getProxy().getPluginManager().registerListener(instance, new SupportListener());
        getProxy().getPluginManager().registerListener(instance, new MuteListener());
        getProxy().getPluginManager().registerCommand(instance, new BugReportCommand("bugreport"));
        getProxy().getPluginManager().registerCommand(instance, new BuglistCommand("buglist"));
        getProxy().getPluginManager().registerCommand(instance, new SupportCommand("support"));
        getProxy().getPluginManager().registerCommand(instance, new HeroCityCaseCommand("herocitykiste"));
        getProxy().getPluginManager().registerCommand(instance, new LobbyCaseCommand("lobbykiste"));
        getProxy().getPluginManager().registerCommand(instance, new TradingCardCommand("sammelkartenpacks"));
        getProxy().getPluginManager().registerCommand(instance, new KickCommand("kick"));
        getProxy().getPluginManager().registerCommand(instance, new HistoryCommand("history"));
        reasonFile = new YamlConfigBuilder(getDataFolder().getPath() + "/reasons.yml");
        ProxyServer.getInstance().getScheduler().schedule(instance, new Runnable() {
            @Override
            public void run() {
                reasonManager = new ReasonManager();
            }
        }, 3, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {

    }

    public static BanSystem getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public ReasonManager getReasonManager() {
        return reasonManager;
    }

    public YamlConfigBuilder getReasonFile() {
        return reasonFile;
    }

    // TODO History Command

}
