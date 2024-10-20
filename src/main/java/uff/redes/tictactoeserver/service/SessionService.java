package uff.redes.tictactoeserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.GameEvent;
import uff.redes.tictactoeserver.domain.Session;
import uff.redes.tictactoeserver.dto.PlayerDTO;
import uff.redes.tictactoeserver.event.GameEventEmitter;
import uff.redes.tictactoeserver.exception.ServerException;

import java.util.UUID;

@Service
public class SessionService {
//    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private final GameEventEmitter gameEventEmitter;
    private Session xSession;
    private Session oSession;

    public SessionService(GameEventEmitter gameEventEmitter) {
        this.gameEventEmitter = gameEventEmitter;
    }

    public Session startSession(PlayerDTO player) {
        validateIfCanStartSession(player);
        Session session = new Session(UUID.randomUUID(), player.player());
        switch (player.player()) {
            case X -> xSession = session;
            case O -> oSession = session;
        }
        gameEventEmitter.emmit(GameEvent.PLAYER_JOINED);
        return session;
    }

    private void validateIfCanStartSession(PlayerDTO session) {
        switch (session.player()) {
            case X -> {
                if (xSession != null) {
                    throw new RuntimeException("X session already started");
                }
            }
            case O -> {
                if (oSession != null) {
                    throw new RuntimeException("O session already started");
                }
            }
        }
    }

    public void endSession(UUID id) {
        if (xSession.id().equals(id)) {
            this.xSession = null;
        }
        else if (oSession.id().equals(id)) {
            this.oSession = null;
        }
        else {
            throw new ServerException("Session not found!", HttpStatus.NOT_FOUND);
        }
    }

    public Session validateSession(UUID sessionID) {
        if (xSession.id().equals(sessionID)) return xSession;
        if (oSession.id().equals(sessionID)) return oSession;
        throw new ServerException("Session not found!", HttpStatus.NOT_FOUND);
    }

    public boolean bothPlayersConnected() {
        return xSession != null && oSession != null;
    }
}
