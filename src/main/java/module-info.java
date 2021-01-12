module umpmax {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.jfoenix;
    opens org.umpmax to javafx.fxml;
    exports org.umpmax;
}