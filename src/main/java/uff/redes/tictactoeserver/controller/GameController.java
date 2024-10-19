package uff.redes.tictactoeserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uff.redes.tictactoeserver.service.GameService;

@RestController
@RequestMapping("/game")
public class GameController {
    private static final Logger log = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.OK)
    public void start() {
        log.info("Starting new game...");
        gameService.start();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void validateSession() {
        gameService.printGrid();
    }
}
