package com.jrohatsch.wordlehelperfx;

public class GridPosition {
    private final int maxRowPosition;
    private final int maxColumnPosition;
    private int rowPosition;
    private int columnPosition;
    private boolean readyForNextPosition;

    public GridPosition(int maxRowPosition, int maxColumnPosition) {
        rowPosition = 0;
        columnPosition = 0;
        this.maxRowPosition = maxRowPosition;
        this.maxColumnPosition = maxColumnPosition;
        readyForNextPosition = true;
    }

    public Position getPosition() {
        return new Position(columnPosition, rowPosition);
    }

    public void incrementPosition() {
        if (columnPosition < maxColumnPosition) {
            columnPosition++;
        } else if (columnPosition == maxColumnPosition && rowPosition < maxRowPosition) {
            columnPosition = 0;
            rowPosition++;
        } else {
            readyForNextPosition = false;
        }
    }

    public void decrementPosition() {
        if (columnPosition > 0) {
            columnPosition--;
        } else if (columnPosition == 0 && rowPosition > 0) {
            columnPosition = maxColumnPosition;
            rowPosition--;
        }
        if (!readyForNextPosition) {
            readyForNextPosition = true;
        }
    }

    public boolean readyForNextPosition() {
        return readyForNextPosition;
    }

}
