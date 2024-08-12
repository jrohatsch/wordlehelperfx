package com.jrohatsch.wordlehelperfx;

enum LetterState {
    NOT_IN_WORD, IN_WORD, IN_WORD_AND_CORRECT_POSITION
}

public class ResultSet {
    Letter[][] letters;

    public ResultSet(int maxRowCount, int maxColumnCount) {
        letters = new Letter[maxRowCount + 1][maxColumnCount + 1];
    }

    public void initState(String letter, int columnIndex, int rowIndex) {
        letters[rowIndex][columnIndex] = new Letter();
        letters[rowIndex][columnIndex].letter = letter;
        letters[rowIndex][columnIndex].letterState = LetterState.NOT_IN_WORD;
    }

    public void updateState(String letter, int columnIndex, int rowIndex) {
        if (letters[rowIndex][columnIndex] != null) {
            var updatedState = switch (letters[rowIndex][columnIndex].letterState) {
                case IN_WORD -> LetterState.IN_WORD_AND_CORRECT_POSITION;
                case IN_WORD_AND_CORRECT_POSITION -> LetterState.NOT_IN_WORD;
                case NOT_IN_WORD -> LetterState.IN_WORD;
            };
            letters[rowIndex][columnIndex].letterState = updatedState;
        } else {
            var newLetter = new Letter();
            newLetter.letter = letter;
            newLetter.letterState = LetterState.NOT_IN_WORD;
            letters[rowIndex][columnIndex] = newLetter;
        }
    }

    public LetterState getState(int columnIndex, int rowIndex) {
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

    public void remove(int columnPosition, int rowPosition) {
        letters[rowPosition][columnPosition] = null;
    }
}

class Letter {
    String letter;
    LetterState letterState;


}
