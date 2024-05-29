module au.edu.cqu.jhle.client {
    requires javafx.controls;
    requires javafx.fxml;

    opens au.edu.cqu.jhle.client to javafx.fxml;
    exports au.edu.cqu.jhle.client;
}
