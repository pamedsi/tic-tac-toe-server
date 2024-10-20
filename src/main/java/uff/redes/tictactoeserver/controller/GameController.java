package uff.redes.tictactoeserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uff.redes.tictactoeserver.domain.Session;
import uff.redes.tictactoeserver.dto.GameStatusDTO;
import uff.redes.tictactoeserver.dto.Move;
import uff.redes.tictactoeserver.exception.ServerException;
import uff.redes.tictactoeserver.service.GameService;
import uff.redes.tictactoeserver.service.SessionService;

@RestController
@RequestMapping("/game")
public class GameController {
    private static final Logger log = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;
    private final SessionService sessionService;

    public GameController(GameService gameService, SessionService sessionService) {
        this.gameService = gameService;
        this.sessionService = sessionService;
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.OK)
    public void start() {
        if (!sessionService.bothPlayersConnected()) {
            throw new ServerException("Os dois jogadores ainda não estão conectados!", HttpStatus.CONFLICT);
        }
        log.info("Starting new game...");
        gameService.start();
    }

    @PostMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    public void move(@RequestBody Move move) {
        Session session = this.sessionService.validateSession(move.sessionID());
        gameService.move(move, session.player());
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public GameStatusDTO getStatus() {
        log.info("Getting game status...");
        GameStatusDTO gameStatusDTO = gameService.getStatus();
        log.info("Game status got successfully!");
        return gameStatusDTO;
    }
}
