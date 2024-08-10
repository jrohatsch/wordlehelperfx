package com.jrohatsch.wordlehelperfx;

import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.util.List;

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

    public void init(){
        currentPosition = new GridPosition(grid.getRowCount()-1, grid.getColumnCount() - 1);
        grid.setGridLinesVisible(false);
        resultSet = new ResultSet(grid.getRowCount()-1, grid.getColumnCount()-1);
        wordsToTry = FilterWords.readWordList(List.of(getClass().getResource("wortliste.txt").getPath()));
        System.out.println(wordsToTry.size());
    }

    @FXML
    public void calculate(){
        System.out.println(resultSet);
        wordsToTry = FilterWords.readWordList(List.of(getClass().getResource("wortliste.txt").getPath()));

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