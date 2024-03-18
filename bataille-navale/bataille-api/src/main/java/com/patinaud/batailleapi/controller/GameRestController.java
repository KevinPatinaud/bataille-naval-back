package com.patinaud.batailleapi.controller;

import com.patinaud.batailleapi.mapper.GameMapper;
import com.patinaud.batailleapi.requestdata.Game;
import com.patinaud.batailleengine.gameengine.GameEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/bataille-navale/")
public class GameRestController {

    @Autowired
    GameEngineService gameEngine;

    @GetMapping("new-game")
    public Game generateNewGame() {

        return GameMapper.toResponse(gameEngine.generateNewGame());

    }

}
