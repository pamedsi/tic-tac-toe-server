package uff.redes.tictactoeserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.Session;
import uff.redes.tictactoeserver.dto.PlayerDTO;

import java.util.UUID;

@Service
public class SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private Session xSession;
    private Session oSession;

    public UUID startSession(PlayerDTO player) {
        validateIfCanStartSession(player);
        Session session = new Session(UUID.randomUUID(), player.player());
        switch (player.player()) {
            case X -> xSession = session;
            case O -> oSession = session;
        }
        return session.id();
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
            log.error("Session not found!");
        }
    }
}
