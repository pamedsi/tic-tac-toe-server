package uff.redes.tictactoeserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.Session;
import uff.redes.tictactoeserver.dto.PlayerDTO;
import uff.redes.tictactoeserver.exception.ServerException;

import java.util.List;
import java.util.UUID;

@Service
public class SessionService {
//    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private Session xSession;
    private Session oSession;
    private List<Session> guests;

//    public SessionService(GameEventEmitter gameEventEmitter) {
//        this.gameEventEmitter = gameEventEmitter;
//    }

    public Session startSession(PlayerDTO player) {
        validateIfCanStartSession(player);
        Session session = new Session(UUID.randomUUID(), player.player());
        switch (player.player()) {
            case X -> xSession = session;
            case O -> oSession = session;
        }
        return session;
    }

    public boolean firstPlayerJoined() {
        return (xSession == null && oSession != null) || (oSession == null && xSession != null);
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
        if (xSession.getID().equals(id)) {
            this.xSession = null;
        }
        else if (oSession.getID().equals(id)) {
            this.oSession = null;
        }
        else {
            throw new ServerException("Session not found!", HttpStatus.NOT_FOUND);
        }
    }

    public Session validateSession(UUID sessionID) {
        if (xSession.getID().equals(sessionID)) return xSession;
        if (oSession.getID().equals(sessionID)) return oSession;
        throw new ServerException("Session not found!", HttpStatus.NOT_FOUND);
    }

    public boolean bothPlayersConnected() {
        if (xSession != null && oSession != null) {
            return xSession.isWSConnected() && oSession.isWSConnected();
        }
        return false;
    }

    public boolean isAPlayer(UUID sessionID) {
        return xSession.getID().equals(sessionID) || oSession.getID().equals(sessionID);
    }

    public void connectWS(UUID sessionID) {
        if (xSession.getID().equals(sessionID)) {
            xSession.setWSConnected(true);
        }
        else if (oSession.getID().equals(sessionID)) {
            oSession.setWSConnected(true);
        }
    }

    public void joinGuest(Session session) {
        // TODO
    }
}
