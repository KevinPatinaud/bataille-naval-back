package com.patinaud.batailleservice.service;

import com.patinaud.bataillemodel.dto.CellDTO;
import com.patinaud.bataillemodel.dto.GridDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GridServiceImpl implements GridService {

    public GridDTO generateEmptyGrid(int width, int height) {

        List<List<CellDTO>> grid = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            List<CellDTO> line = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                CellDTO cell = new CellDTO();
                cell.setX(x);
                cell.setY(y);
                cell.setRevealed(false);
                cell.setOccupied(false);
                line.add(cell);
            }
            grid.add(line);
        }
        GridDTO gridDto = new GridDTO();
        gridDto.setCells(grid);
        return gridDto;
    }

}
