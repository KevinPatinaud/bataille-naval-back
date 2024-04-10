package com.patinaud.batailleapi.controller;

import com.patinaud.batailleapi.mapper.GameMapper;
import com.patinaud.batailleapi.requestdata.Game;
import com.patinaud.batailleengine.gameengine.GameEngineService;
import com.patinaud.bataillemodel.constants.GameMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/bataille-navale/")
public class GameRestController {
    GameEngineService gameEngine;

    @Autowired
    public GameRestController(GameEngineService gameEngine) {
        this.gameEngine = gameEngine;
    }


    @GetMapping("new-id-game")
    public Game generateNewIdGame() {
        return new Game(gameEngine.generateIdGame());
    }

    @GetMapping("new-game")
    public Game generateNewGame(@RequestParam(name = "idGame") String idGame, @RequestParam(name = "gameMode") GameMode gameMode) throws Exception {
        return GameMapper.toResponse(gameEngine.generateNewGame(idGame, gameMode));
    }

}
