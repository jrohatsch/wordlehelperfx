package com.jrohatsch.wordlehelperfx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
    private final int WORD_LENGTH;
    private final Map<String, List<String>> cache;

    public Dictionary(int wordLength) {
        cache = new HashMap<>();
        WORD_LENGTH = wordLength;
    }

    private List<String> readWordList(InputStream input) {
        var reader = new BufferedReader(new InputStreamReader(input));
        return reader.lines().filter(word -> word.length() == WORD_LENGTH).map(String::toUpperCase).distinct().collect(Collectors.toList());
    }

    public List<String> loadWordlist(String language) {
        if (!cache.containsKey(language)) {
            loadToCache(language);
        }
        return new ArrayList<>(cache.get(language));
    }

    public List<String> getLanguages() {
        return Arrays.stream(Language.values()).map(language -> language.displayedName).collect(Collectors.toList());
    }

    private void loadToCache(String language) {
        String fileName = getDictionaryFileName(language);
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        cache.put(language, readWordList(inputStream));
    }

    private String getDictionaryFileName(String languageName) {
        var foundLanguage = Arrays.stream(Language.values()).filter(language -> language.displayedName.equals(languageName)).findFirst();
        if (foundLanguage.isEmpty()) {
            return Language.ENG.fileName;
        } else {
            return foundLanguage.get().fileName;
        }
    }

    private enum Language {
        GER("wortliste.txt", "Deutsch"),
        ENG("wortliste_eng.txt", "English");
        final String fileName;
        final String displayedName;

        Language(String fileName, String displayedName) {
            this.fileName = fileName;
            this.displayedName = displayedName;
        }
    }
}


