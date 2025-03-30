package com.jrohatsch.wordlehelperfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WordleHelperFx extends Application {
    public static void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WordleHelperFx.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 549, 428);
        var controller = (MainController) fxmlLoader.getController();
        controller.init();

        Application.setUserAgentStylesheet(WordleHelperFx.class.getResource("primer-light.css").toString());

        stage.setResizable(false);
        stage.setTitle("WordleHelperFx");
        stage.setScene(scene);
        stage.show();
        scene.setOnKeyReleased(keyEvent -> controller.handleKeyPressed(keyEvent.getCode().getName()));
    }
}