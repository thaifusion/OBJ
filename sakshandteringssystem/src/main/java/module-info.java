module com.eksamen2025 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.eksamen2025 to javafx.fxml;
    exports com.eksamen2025;
}
