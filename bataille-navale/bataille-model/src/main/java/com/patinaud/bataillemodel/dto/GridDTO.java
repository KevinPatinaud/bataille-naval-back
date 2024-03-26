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

    public boolean isInTheGrid(int x, int y) {
        return cells != null && x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    public boolean isInTheGrid(CoordinateDTO coordinate) {
        return isInTheGrid(coordinate.getX(), coordinate.getY());
    }

    public CellDTO getCell(int x, int y) {
        if (isInTheGrid(x, y)) {
            return cells.get(y).get(x);
        }
        return null;
    }

    public CellDTO getCell(CoordinateDTO coordinate) {
        return getCell(coordinate.getX(), coordinate.getY());
    }

    public void updateCell(CellDTO cell) {
        if (isInTheGrid(cell.getX(), cell.getY())) {
            cells.get(cell.getY()).set(cell.getX(), cell);
        }
    }


}
