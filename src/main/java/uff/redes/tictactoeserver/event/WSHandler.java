package uff.redes.tictactoeserver.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import uff.redes.tictactoeserver.domain.GameEvent;

@Component
public class WSHandler {
    private static final Logger log = LoggerFactory.getLogger(WSHandler.class);
    private final GameEventEmitter gameService;

    public WSHandler(GameEventEmitter gameService) {
        this.gameService = gameService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Nova conexão WebSocket estabelecida");
        log.info("Detalhes da sessão: {}", event.getSource());
        gameService.emmit(GameEvent.PLAYER_JOINED);
        // TODO
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("Conexão WebSocket encerrada");
        log.info("ID da sessão: {}", event.getSessionId());
        // TODO
    }
}
