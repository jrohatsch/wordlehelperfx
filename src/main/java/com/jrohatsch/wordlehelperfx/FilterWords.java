package com.jrohatsch.wordlehelperfx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilterWords {

    public static List<String> filterWords(List<String> words, Letter[] word) {
        List<Character> not_contained_chars = new ArrayList<>();
        List<Character> contained_chars = new ArrayList<>();
        int position = 0;
        for (Letter letter : word) {
            final int currentPosition = position;
            final char character = letter.letter;

            switch (letter.letterState) {
                case NOT_IN_WORD -> not_contained_chars.add(character);
                case IN_WORD_OTHER_POSITION -> {
                    Predicate<String> letterMissing = a -> a.indexOf(character) == -1;
                    Predicate<String> letterAtWrongPosition = a -> (a.charAt(currentPosition) == character);

                    words.removeIf(letterMissing.or(letterAtWrongPosition));
                    contained_chars.add(letter.letter);
                }
                case IN_WORD_AND_CORRECT_POSITION -> {
                    words.removeIf(a -> a.charAt(currentPosition) != character);
                    contained_chars.add(letter.letter);
                }
            }
            position++;
        }

        not_contained_chars.removeIf(contained_chars::contains);

        not_contained_chars.forEach((Character not_contained) -> words.removeIf(wordToCheck -> (wordToCheck.indexOf(not_contained) != -1)));

        return words;
    }
}
