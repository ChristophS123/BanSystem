package de.christoph.backend.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Connection connection;
    private int port;
    private String host;
    private String database;
    private String user;
    private String password;

    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        connect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
            System.out.println("HeroWarsBans: MySQL Verbunden");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("HeroWarsBans: MySQL Verbindung fehlgeschlagen.");
        }
    }

    public void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("HeroWarsBans: MySQL Verbindung konnte nicht geschlossen werden.");
        }
    }

    public boolean hasConnection() {
        if(this.connection != null) {
            return true;
        }
        return false;
    }

    public Connection getConnection() {
        return connection;
    }
}
