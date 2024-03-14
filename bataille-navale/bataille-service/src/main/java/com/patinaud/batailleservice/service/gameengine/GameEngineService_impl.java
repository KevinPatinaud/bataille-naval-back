package com.patinaud.batailleservice.service.gameengine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.patinaud.bataillemodel.constants.CellContent;
import com.patinaud.bataillemodel.constants.GameAction;
import com.patinaud.bataillemodel.constants.IdPlayer;
import com.patinaud.bataillemodel.dto.Cell;
import com.patinaud.bataillemodel.dto.PlayerCells;
import com.patinaud.batailleservice.service.communication.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameEngineService_impl implements GameEngineService {
    @Autowired
    CommunicationService communicationService;


    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void playerAttack(String idGame, String idPlayerAttacker_in, int xTargeted, int yTargeted) {

        IdPlayer idPlayerAttacker = null;
        try {
            idPlayerAttacker = IdPlayer.valueOf(idPlayerAttacker_in.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        IdPlayer idPlayerOpponent = getIdOpponent(idGame, idPlayerAttacker);

        // vérifier que le joueur qui envoi l'attaque doit bien jouer
        if (!idPlayerAttacker.equals(getIdNextPlayerToPlay(idGame))) {
            System.out.println("for the game " + idGame + " player " + getIdNextPlayerToPlay(idGame) + " should play and not " + idPlayerAttacker);
            return;
        }

        // Révèle la cellule sur la grille adverse
        revealedCell(idGame, idPlayerOpponent, xTargeted, yTargeted);


        // communique les cellules adverses qui sont révélées
        communicationService.sendMessage(idGame, GameAction.REVEALED_CELLS.getValue(),
                gson.toJson(getPlayerRevealedCells(idGame, idPlayerOpponent))
        );

        // Si tous les bateaux du joueur adversaire sont détruit

        // met fin à la partie et communique le gagnant

        // Indique que c'est à l'autre joueur de jouer

    }

    private IdPlayer getIdNextPlayerToPlay(String idGame) {
        return IdPlayer.PLAYER_1;
    }

    private IdPlayer getIdOpponent(String idGame, IdPlayer idPlayer) {
        return idPlayer.equals(IdPlayer.PLAYER_1) ? IdPlayer.PLAYER_2 : IdPlayer.PLAYER_1;
    }

    private void revealedCell(String idGame, IdPlayer idPlayerTargeted, int xCellTargeted, int yCellTargeted) {

    }


    private PlayerCells getPlayerRevealedCells(String idGame, IdPlayer idPlayer) {
        Cell cell = new Cell();
        cell.setCellContent(CellContent.BOAT);
        cell.setX(3);
        cell.setY(4);

        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(cell);

        PlayerCells playerCells = new PlayerCells();
        playerCells.setIdPlayer(idPlayer);
        playerCells.setCells(cells);

        return playerCells;
    }


}
