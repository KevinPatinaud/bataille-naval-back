package com.patinaud.bataillemodel.dto;

import com.patinaud.bataillemodel.constants.IdPlayer;

public class EndGameResultDTO {
    private IdPlayer idPlayerWin;
    private IdPlayer idPlayerLose;

    public IdPlayer getIdPlayerWin() {
        return idPlayerWin;
    }

    public void setIdPlayerWin(IdPlayer idPlayerWin) {
        this.idPlayerWin = idPlayerWin;
    }

    public IdPlayer getIdPlayerLose() {
        return idPlayerLose;
    }

    public void setIdPlayerLose(IdPlayer idPlayerLose) {
        this.idPlayerLose = idPlayerLose;
    }
}
