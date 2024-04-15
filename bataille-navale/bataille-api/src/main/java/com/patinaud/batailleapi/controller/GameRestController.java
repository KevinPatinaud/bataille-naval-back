package com.patinaud.batailleapi.controller;

import com.patinaud.batailleapi.mapper.GameMapper;
import com.patinaud.batailleapi.request.GameRequest;
import com.patinaud.batailleapi.response.Game;
import com.patinaud.batailleengine.gameengine.GameEngineService;
import com.patinaud.bataillepersistence.persistence.PersistenceGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/game")
public class GameRestController {
    GameEngineService gameEngine;

    @Autowired
    PersistenceGameService persistenceGameService;

    @Autowired
    public GameRestController(GameEngineService gameEngine) {
        this.gameEngine = gameEngine;
    }

    @PostMapping("/")
    public Game generateNewGame(@RequestBody GameRequest gameRequest) throws Exception {
        return GameMapper.toResponse(gameEngine.generateNewGame(gameRequest.getMode()));
    }


    @GetMapping("/{id}/iswaitingsecondplayer")
    public Boolean isGameWaitingSecondPlayer(@PathVariable("id") String id) {
        return gameEngine.isGameWaitingForSecondPlayerJoin(id);
    }

    @PutMapping("/{id}/join")
    public Boolean playerJoinGame(@PathVariable("id") String id) {
        return gameEngine.playerJoinGame(id);
    }

}
