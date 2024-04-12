package com.patinaud.batailleapi.controller;

import com.patinaud.batailleapi.mapper.GameMapper;
import com.patinaud.batailleapi.request.GameRequest;
import com.patinaud.batailleapi.response.Game;
import com.patinaud.batailleengine.gameengine.GameEngineService;
import com.patinaud.bataillepersistence.persistence.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/game")
public class GameRestController {
    GameEngineService gameEngine;

    @Autowired
    PersistenceService persistenceService;

    @Autowired
    public GameRestController(GameEngineService gameEngine) {
        this.gameEngine = gameEngine;
        gameEngine.hashCode();
    }

    @PostMapping("/")
    public Game generateNewGame(@RequestBody GameRequest gameRequest) throws Exception {
        return GameMapper.toResponse(gameEngine.generateNewGame(gameRequest.getId(), gameRequest.getMode()));
    }

    @PostMapping("/newid")
    public Game generateNewIdGame() {
        Game response = new Game();
        response.setId(gameEngine.generateIdGame());
        return response;
    }


}
