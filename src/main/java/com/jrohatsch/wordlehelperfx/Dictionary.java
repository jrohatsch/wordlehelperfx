package com.jrohatsch.wordlehelperfx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Dictionary {
    private final Map<String, List<String>> cache;

    public Dictionary() {
        cache = new HashMap<>();
    }

    private static List<String> readWordList(InputStream input) {
        var reader = new BufferedReader(new InputStreamReader(input));
        return reader.lines().filter(word -> word.length() == 5).map(word -> word.toLowerCase()).distinct().collect(Collectors.toList());
    }

    public List<String> loadWordlist(String language) {
        if (!cache.containsKey(language)) {
            loadToCache(language);
        }
        return cache.get(language);
    }

    public List<String> getLanguages(){
        return List.of("Deutsch", "English");
    }

    private void loadToCache(String language) {
        String fileName = getDictionaryFileName(language);
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        cache.put(language, readWordList(inputStream));
    }

    private String getDictionaryFileName(String language) {
        return switch (language) {
            case "Deutsch" -> "wortliste.txt";
            case "English" -> "wortliste_eng.txt";
            default -> "wortliste_eng_txt";
        };
    }
}


