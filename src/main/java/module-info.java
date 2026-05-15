module com.gestionhandicap {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.gestionhandicap to javafx.fxml;
    exports com.gestionhandicap;

    opens com.gestionhandicap.view to javafx.fxml;
    exports com.gestionhandicap.view;
}