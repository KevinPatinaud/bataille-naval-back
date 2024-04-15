package com.patinaud.bataillecommunication.mapper;

import com.patinaud.bataillecommunication.response.EndGameResult;
import com.patinaud.bataillemodel.dto.EndGameResultDTO;

public class EndGameResultMapper {
    private EndGameResultMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static EndGameResult fromDtoToResponse(EndGameResultDTO dto) {
        EndGameResult endGame = new EndGameResult();
        endGame.setIdPlayerWin(dto.getIdPlayerWin());
        endGame.setIdPlayerLose(dto.getIdPlayerLose());
        return endGame;
    }
}
