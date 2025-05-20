module com.eksamen2025 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;
    
    opens com.eksamen2025 to javafx.fxml;
    exports com.eksamen2025;



}
