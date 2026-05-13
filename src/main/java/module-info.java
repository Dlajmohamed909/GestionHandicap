module com.example.gestionhandicap {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gestionhandicap to javafx.fxml;
    exports com.gestionhandicap;
}