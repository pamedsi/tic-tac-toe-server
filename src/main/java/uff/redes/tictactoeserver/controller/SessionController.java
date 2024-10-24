package uff.redes.tictactoeserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uff.redes.tictactoeserver.domain.Session;
import uff.redes.tictactoeserver.dto.UserDTO;
import uff.redes.tictactoeserver.service.SessionService;

import java.util.UUID;

@RestController
@RequestMapping("/session")
public class SessionController {
    private static final Logger log = LoggerFactory.getLogger(SessionController.class);
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Session addNewSession(@RequestBody UserDTO session) {
        Session id;
        log.info("User joining...");
        id = sessionService.startSession(session);
        if (session.isGuest()) {
            log.info("Guest joined successfully! ✅");
        }
        else {
            log.info("Player {} joined successfully! ✅", session.player());
        }
        return id;
    }

    @GetMapping("/validate/{sessionID}")
    @ResponseStatus(HttpStatus.OK)
    public Session validateSession(@PathVariable UUID sessionID) {
        log.info("Validating session...");
        Session session = sessionService.validateSession(sessionID);
        log.info("Session ok! ✅ Player {} can join", session.getPlayer());
        return session;
    }
}
