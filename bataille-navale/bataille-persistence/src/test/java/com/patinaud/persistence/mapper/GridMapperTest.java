package com.patinaud.persistence.mapper;

import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import com.patinaud.bataillepersistence.entity.Cell;
import com.patinaud.bataillepersistence.mapper.GridMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GridMapperTest {

     @Test
     void utilityClassConstructorShouldThrowException() throws NoSuchMethodException {
         Constructor<GridMapper> constructor = GridMapper.class.getDeclaredConstructor();
         constructor.setAccessible(true);

         Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
         Assertions.assertTrue(exception.getCause() instanceof IllegalStateException);
         Assertions.assertTrue("Utility class".equals(exception.getCause().getMessage()));
     }

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
