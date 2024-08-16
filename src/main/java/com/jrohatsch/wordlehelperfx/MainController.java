package com.jrohatsch.wordlehelperfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.util.List;

public class MainController {
    @FXML
    private GridPane grid;
    @SuppressWarnings("unused")
    @FXML
    private Button calculate;
    private GridPosition currentPosition;
    private ResultSet resultSet;
    private List<String> wordsToTry;
    @FXML
    private ListView<String> displayedwords;
    @FXML
    private MenuButton languageButton;
    private Dictionary dictionary;

    public void init() {
        currentPosition = new GridPosition(grid.getRowCount() - 1, grid.getColumnCount() - 1);
        grid.setGridLinesVisible(false);
        resultSet = new ResultSet(grid.getRowCount() - 1, grid.getColumnCount() - 1);
        dictionary = new Dictionary(grid.getColumnCount());
        var languages = dictionary.getLanguages();
        wordsToTry = dictionary.loadWordlist(languages.get(0));
        for (var name : languages) {
            var item = new MenuItem();
            item.setText(name);
            item.setOnAction(actionEvent -> languageButton.setText(item.getText()));
            languageButton.getItems().add(item);
        }
        languageButton.setText(languages.get(0));
    }

    @FXML
    public void calculate() {
        wordsToTry = dictionary.loadWordlist(languageButton.getText());

        resultSet.getCompleteWords().forEach(word -> wordsToTry = FilterWords.filterWords(wordsToTry, word));

        displayedwords.getItems().clear();
        wordsToTry.forEach(word -> displayedwords.getItems().add(word));
    }

    public void handleKeyPressed(String character) {

        if (character.equals("Backspace")) {
            // delete character at current index
            currentPosition.decrementPosition();
            grid.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(currentPosition.getPosition().generateID()));
            resultSet.remove(currentPosition.getPosition());
            return;
        }

        if (!currentPosition.readyForNextPosition()) {
            return;
        }

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (!alphabet.contains(character)) {
            return;
        }

        Position position = currentPosition.getPosition();
        Button button = new Button(character);
        button.setId(position.generateID());
        resultSet.initState(character, position);
        updateColor(position, button);
        button.setPrefWidth(Double.MAX_VALUE);
        button.setPrefHeight(Double.MAX_VALUE);
        button.setOnAction(a -> {
            resultSet.updateState(character, position);
            updateColor(position, button);
        });
        grid.add(button, position.column(), position.row(), 1, 1);
        currentPosition.incrementPosition();
    }

    private void updateColor(Position position, Button button) {
        String color = switch (resultSet.getState(position)) {
            case NOT_IN_WORD -> "grey";
            case IN_WORD_OTHER_POSITION -> "yellow";
            case IN_WORD_AND_CORRECT_POSITION -> "green";
        };
        button.setStyle("-fx-base: %s;".formatted(color));
    }
}