package com.patinaud.bataillemodel.dto;

import java.util.List;

public class GridDTO {
    List<List<CellDTO>> cells;

    public List<List<CellDTO>> getCells() {
        return cells;
    }

    public void setCells(List<List<CellDTO>> cells) {
        this.cells = cells;
    }

    public int getWidth() {
        if (cells == null) {
            return 0;
        }
        return cells.size();
    }

    public int getHeight() {
        if (getWidth() >= 1) {
            return cells.get(0).size();
        }
        return 0;
    }

    public boolean isInGrid(int x, int y) {
        return cells != null && x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    public CellDTO getCell(int x, int y) {
        if (isInGrid(x, y)) {
            return cells.get(x).get(y);
        }
        return null;
    }

    public void updateCell(int x, int y, CellDTO cell) {
        if (isInGrid(x, y)) {
            cells.get(x).set(y, cell);
        }
    }

}
