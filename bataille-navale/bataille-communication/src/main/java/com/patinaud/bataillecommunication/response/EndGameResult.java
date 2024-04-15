package com.patinaud.bataillecommunication.response;

import com.patinaud.bataillemodel.constants.IdPlayer;

public class EndGameResult {
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
