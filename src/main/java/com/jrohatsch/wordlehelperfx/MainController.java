package com.jrohatsch.wordlehelperfx;

import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainController {
    @FXML
    private GridPane grid;
    @FXML
    private Button calculate;
    private GridPosition currentPosition;
    private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private ResultSet resultSet;
    private List<String> wordsToTry;
    @FXML
    private ListView<String> displayedwords;
    @FXML
    private MenuButton languageButton;
    private final Map<String, String> dictionaryLocation = Map.of("Deutsch", "wortliste.txt", "English" , "wortliste_eng.txt");


    public void init() throws IOException {
        currentPosition = new GridPosition(grid.getRowCount()-1, grid.getColumnCount() - 1);
        grid.setGridLinesVisible(false);
        resultSet = new ResultSet(grid.getRowCount()-1, grid.getColumnCount()-1);
        wordsToTry = FilterWords.readWordList(getClass().getResourceAsStream("wortliste.txt"));

        for(var entry : dictionaryLocation.entrySet()){
            var item = new MenuItem();
            item.setText(entry.getKey());
            item.setOnAction(actionEvent -> {
                languageButton.setText(item.getText());
            });
            languageButton.getItems().add(item);
        }
        languageButton.setText("Deutsch");
    }

    @FXML
    public void calculate(){
        System.out.println(resultSet);
        wordsToTry = FilterWords.readWordList(getClass().getResourceAsStream(dictionaryLocation.get(languageButton.getText())));

        var input = FilterWords.convert(resultSet);
        input.forEach(entry -> {
            wordsToTry = FilterWords.filterWords(wordsToTry, entry.getKey(), entry.getValue());
        });

        displayedwords.getItems().clear();
        wordsToTry.forEach(word -> displayedwords.getItems().add(word.toUpperCase()));
    }

    public void handleKeyPressed(String character){

        if(character.equals("Backspace")){
            // delete character at current index
            currentPosition.decrementPosition();
            grid.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("Letter-%d-%d".formatted(currentPosition.getColumnPosition(), currentPosition.getRowPosition())));
            resultSet.remove(currentPosition.getColumnPosition(), currentPosition.getRowPosition());
            return;
        }

        if(!currentPosition.readyForNextPosition()){
            return;
        }

        if(!alphabet.contains(character)){
            return;
        }

        final int columnIndex = currentPosition.getColumnPosition();
        final int rowIndex = currentPosition.getRowPosition();
        Button text = new Button(character);
        text.setId("Letter-%d-%d".formatted(columnIndex, rowIndex));
        resultSet.initState(character, columnIndex, rowIndex);
        text.setStyle("-fx-base: grey;");
        text.setPrefWidth(Double.MAX_VALUE);
        text.setPrefHeight(Double.MAX_VALUE);
        text.setOnAction(a -> {
            resultSet.updateState(character, columnIndex, rowIndex);
            String color = switch (resultSet.getState(columnIndex, rowIndex)){
                case NOT_IN_WORD -> "grey";
                case IN_WORD -> "yellow";
                case IN_WORD_AND_CORRECT_POSITION -> "green";
            };
            text.setStyle("-fx-base: %s;".formatted(color));

        });
        grid.add(text, columnIndex, rowIndex,1,1);
        currentPosition.incrementPosition();
    }

}