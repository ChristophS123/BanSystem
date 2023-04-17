package de.christoph.ban;

import de.christoph.ban.commands.*;
import de.christoph.ban.listeners.BanListener;
import de.christoph.ban.listeners.MuteListener;
import de.christoph.ban.listeners.SupportListener;
import de.christoph.ban.mysql.MySQL;
import de.christoph.ban.support.Support;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;

public class BanSystem extends Plugin {

    private static BanSystem plugin;
    public ArrayList<Support> supports;
    public MySQL mySQL;

    @Override
    public void onEnable() {
        plugin = this;
        mySQL = new MySQL("78.46.106.254", 3306, "teampanel", "admin", "EULVAq7WfowAv5yRhpPbsJt0c6Wfvx49");
        supports = new ArrayList<>();
        register();
    }

    private void register() {
        getProxy().getPluginManager().registerCommand(this, new KickCommand("kick"));
        getProxy().getPluginManager().registerCommand(this, new BanCommand("ban"));
        getProxy().getPluginManager().registerListener(this, new BanListener());
        getProxy().getPluginManager().registerListener(this, new MuteListener());
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand("unban"));
        getProxy().getPluginManager().registerCommand(this, new OfflineBan("offlineban"));
        getProxy().getPluginManager().registerCommand(this, new TempbanCommand("tempban"));
        getProxy().getPluginManager().registerCommand(this, new TempMuteCommand("tempmute"));
        getProxy().getPluginManager().registerCommand(this, new MuteCommand("mute"));
        getProxy().getPluginManager().registerCommand(this, new OfflineMuteCommand("offlinemute"));
        getProxy().getPluginManager().registerCommand(this, new UnmuteCommand("unmute"));
        getProxy().getPluginManager().registerCommand(this, new ReportCommand("report"));
        getProxy().getPluginManager().registerCommand(this, new ReportsCommand("reports"));
        getProxy().getPluginManager().registerCommand(this, new CheckCommand("check"));
        getProxy().getPluginManager().registerCommand(this, new SupportCommand("support"));
        getProxy().getPluginManager().registerListener(this, new SupportListener());
        getProxy().getPluginManager().registerCommand(this, new BuglistCommand("buglist"));
        getProxy().getPluginManager().registerCommand(this, new BugReportCommand("bugreport"));
    }

    public static BanSystem getPlugin() {
        return plugin;
    }

    //id username password

    @Override
    public void onDisable() {
        mySQL.disconnect();
    }
}
