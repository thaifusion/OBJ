//Monica A. Johansen
package com.eksamen2025.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Monica og Jørgen
 * Klassen håndterer tilkoblingen til databasen.
 * Den laster inn noen konfigurasjoner fra db.properties-fil og oppretter en forbindelse til databasen.
 */
public class DatabaseUtil {
    private static final String CONFIG_FILE = "sakshandteringssystem/src/main/java/com/eksamen2025/ressurser/db.properties";

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(CONFIG_FILE); 
        props.load(fis);
        
        final String url = "jdbc:mysql://localhost:3306/sakssystem?serverTimezone=UTC";
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        
        Connection databaseKobling = null;
        try {
            databaseKobling = DriverManager.getConnection(url, user, password);
            System.out.println("Databasen er koblet til.");
        } catch (SQLException e) {
            System.out.println("Databasen finnes ikke! Oppretter ny database");
            final String nyDatabaseUrl = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";
            try {
                databaseKobling = DriverManager.getConnection(nyDatabaseUrl, user, password);
                databaseKobling.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS sakssystem");
                databaseKobling = DriverManager.getConnection(url, user, password);
                opprettTabeller(databaseKobling);
                System.out.println("Ny database er opprettet.");
            } catch (SQLException ex) {
                System.out.println("Feil ved oppretting av ny database: " + ex.getMessage());
            }
        }
        return databaseKobling;
    }

    /** @author Jørgen
     * Oppretter tabeller i databasen hvis de ikke allerede finnes.
     * Tabellene som opprettes er: bruker, kategori, prioritet, status og sak.
     * Om databasen ikke finnes, opprettes den også.
     * @param dbKobling Kobling til databasen
     */
    private static void opprettTabeller(Connection dbKobling) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS bruker ( " +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "brukernavn VARCHAR(50), " +
                     "rolle VARCHAR(50));";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO bruker (brukernavn, rolle) " +
              "VALUES ('JorgenUTVIKLER', 'UTVIKLER');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO bruker (brukernavn, rolle) " +
              "VALUES ('VibekeTESTER', 'TESTER');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO bruker (brukernavn, rolle) " +
              "VALUES ('MonicaUTVIKLER', 'UTVIKLER');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO bruker (brukernavn, rolle) " +
              "VALUES ('HenrikLEDER', 'LEDER');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO bruker (brukernavn, rolle) " +
              "VALUES ('MariusTESTER', 'TESTER');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO bruker (brukernavn, rolle) " +
              "VALUES ('SofiaLEDER', 'LEDER');";
        dbKobling.createStatement().executeUpdate(sql);

        /**
         * Oppretter Kategoritabellen og fyller den med noen standardverdier.
         * Kategoriene er: UI-feil, Backend-feil og Funksjonsforespørsel.
         */
        sql = "CREATE TABLE IF NOT EXISTS kategori ( " +
              "id INT AUTO_INCREMENT PRIMARY KEY, " +
              "navn VARCHAR(50));";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO kategori (navn) " +
              "VALUES ('UI-feil');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO kategori (navn) " +
              "VALUES ('Backend-feil');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO kategori (navn) " +
              "VALUES ('Funksjonsforespørsel');";
        dbKobling.createStatement().executeUpdate(sql);

        /**
         * Oppretter Prioritetstabellen og fyller den med noen standardverdier.
         * Prioritetene er: Lav, Middels og Høy.
         */
        sql = "CREATE TABLE IF NOT EXISTS prioritet ( " +
              "id INT AUTO_INCREMENT PRIMARY KEY, " +
              "navn VARCHAR(50));";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO prioritet (navn) " +
              "VALUES ('Lav');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO prioritet (navn) " +
              "VALUES ('Middels');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO prioritet (navn) " +
              "VALUES ('Høy');";
        dbKobling.createStatement().executeUpdate(sql);

        /**
         * Oppretter Statustabellen og fyller den med noen standardverdier.
         * Statusene er: Innsendt, Tildelt, Under arbeid, Fikset, Løst, Testet - Feilet og Lukket.
         */
        sql = "CREATE TABLE IF NOT EXISTS status ( " +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "kode VARCHAR(50), " +
            "etikett VARCHAR(50));";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO status (kode, etikett) " +
              "VALUES ('SUBMITTED', 'Innsendt');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO status (kode, etikett) " +
              "VALUES ('ASSIGNED', 'Tildelt');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO status (kode, etikett) " +
              "VALUES ('IN_PROGRESS', 'Under arbeid');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO status (kode, etikett) " +
              "VALUES ('FIXED', 'Fikset');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO status (kode, etikett) " +
              "VALUES ('RESOLVED', 'Løst');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO status (kode, etikett) " +
              "VALUES ('TEST_FAILED', 'Testet - Feilet');";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "INSERT INTO status (kode, etikett) " +
              "VALUES ('CLOSED', 'Lukket');";
        dbKobling.createStatement().executeUpdate(sql);

        /**
         * Oppretter Sak-tabellen.
         * Sak-tabellen har en del attributter som er relatert til de andre tabellene.
         * Den har også noen attributter som er spesifikke for sakene.
         */
        sql = "CREATE TABLE IF NOT EXISTS sak (" +
        "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
        "tittel VARCHAR(50) DEFAULT NULL, " +
        "beskrivelse VARCHAR(255) DEFAULT NULL, " +
        "prioritet_id INT DEFAULT NULL, " +
        "kategori_id INT DEFAULT NULL, " +
        "status_id INT DEFAULT NULL, " +
        "reporter_id INT DEFAULT NULL, " +
        "mottaker_id INT DEFAULT NULL, " +
        "opprettetTid DATETIME DEFAULT NULL, " +
        "oppdatertTid DATETIME DEFAULT NULL, " +
        "utviklerkommentar VARCHAR(255) DEFAULT NULL, " +
        "testerTilbakemelding VARCHAR(255) DEFAULT NULL" +
        ");";

        /**
         * Legger til foreignkeys i Sak-tabellen.
         * Disse refererer til de andre tabellene.
         */
        dbKobling.createStatement().executeUpdate(sql);
        sql = "ALTER TABLE sak ADD FOREIGN KEY (prioritet_id) REFERENCES prioritet(id);";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "ALTER TABLE sak ADD FOREIGN KEY (kategori_id) REFERENCES kategori(id);";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "ALTER TABLE sak ADD FOREIGN KEY (status_id) REFERENCES status(id);";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "ALTER TABLE sak ADD FOREIGN KEY (reporter_id) REFERENCES bruker(id);";
        dbKobling.createStatement().executeUpdate(sql);
        sql = "ALTER TABLE sak ADD FOREIGN KEY (mottaker_id) REFERENCES bruker(id);";
        dbKobling.createStatement().executeUpdate(sql);
    }
}
