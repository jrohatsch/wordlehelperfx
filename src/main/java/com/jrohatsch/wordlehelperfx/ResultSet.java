package com.jrohatsch.wordlehelperfx;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResultSet {
    Letter[][] letters;

    public ResultSet(int maxRowCount, int maxColumnCount) {
        letters = new Letter[maxRowCount + 1][maxColumnCount + 1];
    }

    private void initState(String letter, int columnIndex, int rowIndex) {
        letters[rowIndex][columnIndex] = new Letter();
        letters[rowIndex][columnIndex].letter = letter.charAt(0);

        // check if the letter was already correct at that position
        if (rowIndex > 0) {
            var previousRowLetter = letters[rowIndex - 1][columnIndex];
            if (previousRowLetter != null && previousRowLetter.letter == letter.charAt(0) && previousRowLetter.letterState == LetterState.IN_WORD_AND_CORRECT_POSITION) {
                letters[rowIndex][columnIndex].letterState = LetterState.IN_WORD_AND_CORRECT_POSITION;
            } else {
                letters[rowIndex][columnIndex].letterState = LetterState.NOT_IN_WORD;
            }
        } else {
            letters[rowIndex][columnIndex].letterState = LetterState.NOT_IN_WORD;
        }
    }

    public void initState(String letter, Position position) {
        initState(letter, position.column(), position.row());
    }

    public void updateState(String letter, Position position) {
        updateState(letter, position.column(), position.row());
    }

    private void updateState(String letter, int columnIndex, int rowIndex) {
        if (letters[rowIndex][columnIndex] != null) {
            letters[rowIndex][columnIndex].letterState = switch (letters[rowIndex][columnIndex].letterState) {
                case IN_WORD_OTHER_POSITION -> LetterState.IN_WORD_AND_CORRECT_POSITION;
                case IN_WORD_AND_CORRECT_POSITION -> LetterState.NOT_IN_WORD;
                case NOT_IN_WORD -> LetterState.IN_WORD_OTHER_POSITION;
            };
        } else {
            var newLetter = new Letter();
            newLetter.letter = letter.charAt(0);
            newLetter.letterState = LetterState.NOT_IN_WORD;
            letters[rowIndex][columnIndex] = newLetter;
        }
    }

    private LetterState getState(int columnIndex, int rowIndex) {
        return letters[rowIndex][columnIndex].letterState;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Letter[] word : letters) {
            for (Letter letter : word) {
                if (letter == null) {
                    stringBuilder.append("[---]");
                    continue;
                }
                stringBuilder.append("[%s-%s]".formatted(letter.letter, letter.letterState));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void remove(Position position) {
        letters[position.row()][position.column()] = null;
    }

    public LetterState getState(Position position) {
        return getState(position.column(), position.row());
    }

    public List<Letter[]> getCompleteWords() {
        return Arrays.stream(letters).filter(word -> Arrays.stream(word).allMatch(letter -> letter != null && letter.letter != 0 && letter.letterState != null)).collect(Collectors.toList());
    }
}

