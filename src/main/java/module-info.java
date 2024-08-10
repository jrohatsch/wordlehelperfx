module com.jrohatsch.wordlehelperfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jrohatsch.wordlehelperfx to javafx.fxml;
    exports com.jrohatsch.wordlehelperfx;
}