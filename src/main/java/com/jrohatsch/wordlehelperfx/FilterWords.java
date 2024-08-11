package com.jrohatsch.wordlehelperfx;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterWords {


    public static List<Map.Entry<String,String>> convert(ResultSet resultSet){
        List<Map.Entry<String,String>> out = new ArrayList<>();
        for(Letter[] word : resultSet.letters){
            StringBuilder wordBuilder = new StringBuilder();
            StringBuilder stateBuilder = new StringBuilder();

            for(Letter letter : word){
                if(letter == null){
                    continue;
                }
                wordBuilder.append(letter.letter);
                stateBuilder.append(letter.letterState.ordinal());
            }
            out.add(Map.entry(wordBuilder.toString().toLowerCase(), stateBuilder.toString()));
        }

        out = out.stream().filter(entry -> entry.getKey().length()==5 && entry.getValue().length()==5).collect(Collectors.toList());

        return out;
    }

    public static List<String> filterWords(List<String> words, String input_word, String input_vector) {
        List<String> not_contained_chars = new ArrayList<>();
        List<String> contained_chars = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            String character = input_word.substring(i, i + 1);
            int number = input_vector.charAt(i) - '0';
            final int index = i;

            if (number == 0) {
                not_contained_chars.add(character);
            } else if (number == 1) {
                // word must contain the character
                Predicate<String> predicate_has = a -> (!a.contains(character));

                // but not on position i
                Predicate<String> predicate_pos = a -> (a.charAt(index) == input_word.charAt(index));

                words.removeIf(predicate_has);
                words.removeIf(predicate_pos);

                contained_chars.add(character);
            } else if (number == 2) {
                Predicate<String> pred = a -> (!(a.charAt(index) == input_word.charAt(index)));
                words.removeIf(pred);
                contained_chars.add(character);
            }
        }

        not_contained_chars.removeIf(markedToDelete -> contained_chars.contains(markedToDelete));

        not_contained_chars.forEach((String not_contained) -> {
            Predicate<String> predicate = a -> (a.contains(not_contained));
            words.removeIf(predicate);
        });

        return words;
    }

    public static List<String> readWordList(InputStream input){
        var reader = new BufferedReader(new InputStreamReader(input));
        return reader.lines().filter(word -> word.length() == 5).collect(Collectors.toList());
    }

    public static ArrayList<String> readWordList(List<String> paths) {
        ArrayList<String> words = new ArrayList<String>();

        var it = paths.iterator();

        while (it.hasNext()) {
            try {
                File f = new File(it.next());

                try (BufferedReader b = new BufferedReader(new FileReader(f))) {
                    String readLine = "";

                    while ((readLine = b.readLine()) != null) {

                        readLine = readLine.toLowerCase();

                        if (readLine.length() == 5 && !readLine.contains("-") && !readLine.contains("ä")
                                && !readLine.contains("ö") && !readLine.contains("ü") && !words.contains(readLine)) {
                            words.add(readLine.toLowerCase());
                        }
                    }
                }

            } catch (

                    IOException e) {
                e.printStackTrace();
            }
        }
        return words;
    }
}
