package com.patinaud.persistence.mapper;

import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import com.patinaud.bataillepersistence.entity.Cell;
import com.patinaud.bataillepersistence.mapper.GridMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GridMapperTest {

    @Test
    void testSplitLineOK() {


        List<Cell> cells = new ArrayList<>();

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Cell cell = new Cell();
                cell.setX(x);
                cell.setY(y);
                cells.add(cell);
            }
        }


        GridDTO grid = GridMapper.toDto(cells);

        Assertions.assertEquals(5, grid.getWidth());
        Assertions.assertEquals(5, grid.getHeight());

    }

}
