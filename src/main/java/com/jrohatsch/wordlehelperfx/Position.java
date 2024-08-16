package com.jrohatsch.wordlehelperfx;

public record Position(int column, int row) {
    public String generateID() {
        return "Letter-%d-%d".formatted(column, row);
    }
}
