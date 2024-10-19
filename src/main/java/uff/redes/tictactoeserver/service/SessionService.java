package uff.redes.tictactoeserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.dto.Session;

import java.util.UUID;

@Service
public class SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private Session xSession;
    private Session oSession;

    public void startSession(Session session) {
        validateIfCanStartSession(session);
        switch (session.player()) {
            case X -> xSession = session;
            case O -> oSession = session;
        }
    }

    private void validateIfCanStartSession(Session session) {
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
