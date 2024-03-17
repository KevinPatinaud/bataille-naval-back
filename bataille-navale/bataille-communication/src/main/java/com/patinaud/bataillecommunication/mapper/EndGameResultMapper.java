package com.patinaud.bataillecommunication.mapper;

import com.patinaud.bataillecommunication.responsedata.EndGameResult;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;

public class EndGameResultMapper {

    public static EndGameResult fromDtoToResponse(EndGameResultDTO dto) {
        EndGameResult endGame = new EndGameResult();
        endGame.setIdPlayerWin(dto.getIdPlayerWin());
        endGame.setIdPlayerLose(dto.getIdPlayerLose());
        return endGame;
    }
}
