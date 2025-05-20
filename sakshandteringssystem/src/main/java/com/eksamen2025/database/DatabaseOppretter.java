import java.sql.*;

public class DatabaseOppretter {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/?serverTimezone=UTC&allowMultiQueries=true";
        String dbName = "sakssystem";
        String user = "root"; // change as needed
        String password = "password"; // change as needed

        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = conn.createStatement()) {

            // 1. Create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            stmt.execute("USE " + dbName);
            System.out.println("Database created or already exists.");

            // 2. Create tables
            String createTables = """
                CREATE TABLE IF NOT EXISTS bruker (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    brukernavn VARCHAR(50) NOT NULL,
                    rolle ENUM('TESTER','UTVIKLER','LEDER') NOT NULL
                );

                CREATE TABLE IF NOT EXISTS kategori (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    navn VARCHAR(50)
                );

                CREATE TABLE IF NOT EXISTS prioritet (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    navn VARCHAR(20)
                );

                CREATE TABLE IF NOT EXISTS status (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    kode VARCHAR(20),
                    etikett VARCHAR(50)
                );

                CREATE TABLE IF NOT EXISTS sak (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    tittel VARCHAR(100),
                    beskrivelse TEXT,
                    prioritet_id INT,
                    kategori_id INT,
                    status_id INT,
                    reporter_id INT,
                    mottaker_id INT,
                    opprettetTid DATETIME,
                    oppdatertTid DATETIME,
                    utviklerkommentar TEXT,
                    testerTilbakemelding TEXT,
                    FOREIGN KEY (prioritet_id) REFERENCES prioritet(id),
                    FOREIGN KEY (kategori_id) REFERENCES kategori(id),
                    FOREIGN KEY (status_id) REFERENCES status(id),
                    FOREIGN KEY (reporter_id) REFERENCES bruker(id),
                    FOREIGN KEY (mottaker_id) REFERENCES bruker(id)
                );
            ";

            stmt.executeUpdate(createTables);
            System.out.println("Tables created.");

            // 3. Insert initial data
            String insertData = """
                INSERT INTO bruker (brukernavn, rolle) VALUES
                ('Vibeke', 'TESTER'),
                ('Ranem', 'UTVIKLER'),
                ('Sara', 'LEDER');

                INSERT INTO kategori (navn) VALUES
                ('UI-feil'),
                ('Backend-feil'),
                ('Funksjonsforespørsel');

                INSERT INTO prioritet (navn) VALUES
                ('Lav'),
                ('Middels'),
                ('Høy');

                INSERT INTO status (kode, etikett) VALUES
                ('SUBMITTED','Innsendt'),
                ('ASSIGNED','Tildelt'),
                ('IN_PROGRESS','Pågår'),
                ('FIXED','Rettet'),
                ('RESOLVED','Løst'),
                ('TEST_FAILED','Test mislyktes'),
                ('CLOSED','Lukket');
            """;

            stmt.executeUpdate(insertData);
            System.out.println("Initial data inserted.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Feil under databaseoppsett.");
        }
    }
}
