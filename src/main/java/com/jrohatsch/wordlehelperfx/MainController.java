package com.jrohatsch.wordlehelperfx;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController {
    @FXML
    private GridPane grid;
    private GridPosition currentPosition;
    private ResultSet resultSet;
    private List<String> wordsToTry;
    @FXML
    private ListView<String> displayedwords;
    @FXML
    private MenuButton languageButton;
    private Dictionary dictionary;

    private AtomicBoolean welcomeTextShown = new AtomicBoolean(false);

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
            item.setOnAction(actionEvent -> {
                languageButton.setText(item.getText());
                calculate();
            });
            languageButton.getItems().add(item);
        }
        languageButton.setText(languages.get(0));
        calculate();
        setWelcomeText();
    }

    @FXML
    public void calculate() {
        wordsToTry = dictionary.loadWordlist(languageButton.getText());

        resultSet.getCompleteWords().forEach(word -> wordsToTry = FilterWords.filterWords(wordsToTry, word));

        displayedwords.getItems().clear();
        wordsToTry.forEach(word -> displayedwords.getItems().add(word));
    }

    public void handleKeyPressed(String character) {
        if (welcomeTextShown.get()) {
            grid.getChildren().clear();
            resultSet = new ResultSet(grid.getRowCount() - 1, grid.getColumnCount() - 1);
            welcomeTextShown.set(false);
        }

        if (character.equals("Backspace")) {
            // delete character at current index
            currentPosition.decrementPosition();
            grid.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(currentPosition.getPosition().generateID()));
            resultSet.remove(currentPosition.getPosition());
            calculate();
            return;
        }

        if (!currentPosition.readyForNextPosition()) {
            return;
        }

        if (character.length() > 1 || Dictionary.CHAR_FORBIDDEN.test(character.charAt(0))) {
            return;
        }

        Position position = currentPosition.getPosition();
        Button button = getButton(character, position);
        grid.add(button, position.column(), position.row(), 1, 1);
        currentPosition.incrementPosition();
        calculate();
    }

    private Button getButton(String character, Position position) {
        Button button = new Button(character);
        button.setId(position.generateID());
        resultSet.initState(character, position);
        updateColor(position, button);
        button.setPrefWidth(Double.MAX_VALUE);
        button.setPrefHeight(Double.MAX_VALUE);
        button.setOnAction(a -> {
            resultSet.updateState(character, position);
            updateColor(position, button);
            calculate();
        });
        return button;
    }

    private void updateColor(Position position, Button button) {
        String color = switch (resultSet.getState(position)) {
            case NOT_IN_WORD -> "grey";
            case IN_WORD_OTHER_POSITION -> "yellow";
            case IN_WORD_AND_CORRECT_POSITION -> "green";
        };
        button.setStyle("-fx-background-color: %s;".formatted(color));
    }

    private void addCharacterToGrid(String character, int column, int row) {
        Position position = new Position(column,row);
        Button button = getButton(character, position);
        button.setStyle("-fx-background-color: %s;".formatted(row % 2 == 0 ? "yellow" : "green"));
        grid.add(button, position.column(), position.row(), 1, 1);
    }

    private void setWelcomeText() {
        addCharacterToGrid("T",0,0);
        addCharacterToGrid("Y",1,0);
        addCharacterToGrid("P",2,0);
        addCharacterToGrid("E",3,0);

        addCharacterToGrid("Y",0,1);
        addCharacterToGrid("O",1,1);
        addCharacterToGrid("U",2,1);
        addCharacterToGrid("R",3,1);

        addCharacterToGrid("F",0,2);
        addCharacterToGrid("I",1,2);
        addCharacterToGrid("R",2,2);
        addCharacterToGrid("S",3,2);
        addCharacterToGrid("T",4,2);

        addCharacterToGrid("G",0,3);
        addCharacterToGrid("U",1,3);
        addCharacterToGrid("E",2,3);
        addCharacterToGrid("S",3,3);
        addCharacterToGrid("S",4,3);

        welcomeTextShown.set(true);
    }
}