package uff.redes.tictactoeserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import uff.redes.tictactoeserver.domain.GameEvent;
import uff.redes.tictactoeserver.domain.GameStatus;
import uff.redes.tictactoeserver.dto.GameEventDTO;
import uff.redes.tictactoeserver.event.GameEventEmitter;
import uff.redes.tictactoeserver.exception.ServerException;
import uff.redes.tictactoeserver.service.GameService;
import uff.redes.tictactoeserver.service.SessionService;

import java.util.UUID;

@Controller
public class WebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
    private final GameEventEmitter gameEventEmitter;
    private final SessionService sessionService;
    private final ObjectMapper objectMapper;
    private final GameService gameService;

    public WebSocketHandler(GameEventEmitter gameEventEmitter, SessionService sessionService, ObjectMapper objectMapper, GameService gameService) {
        this.gameEventEmitter = gameEventEmitter;
        this.sessionService = sessionService;
        this.objectMapper = objectMapper;
        this.gameService = gameService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        UUID sessionID = getSessionID(event);
        boolean isAPlayer = sessionService.isAPlayer(sessionID);
        if (isAPlayer) {
            sessionService.connectWS(sessionID);
            if (sessionService.firstPlayerJoined()) {
                gameService.setStatus(GameStatus.WAITING_SECOND_PLAYER);
                gameEventEmitter.emmit(new GameEventDTO(GameEvent.FIRST_PLAYER_JOINED));
            }
            else if (sessionService.bothPlayersConnected()) {
                gameService.setStatus(GameStatus.WAITING_START);
                gameEventEmitter.emmit(new GameEventDTO(GameEvent.BOTH_PLAYERS_JOINED));
            }
            else {
                gameEventEmitter.emmit(new GameEventDTO(GameEvent.GUEST_JOINED));
            }
        }
    }

    private UUID getSessionID(SessionConnectedEvent event) {
        String errorMessage = "Erro ao obter o ID da sessão!";
        try {
            String json = objectMapper.writeValueAsString(event.getMessage().getHeaders().get("simpConnectMessage"));
            JsonNode sessionIDFromConnection = objectMapper.readTree(json)
                    .get("headers")
                    .get("nativeHeaders")
                    .get("sessionID");
            for (JsonNode session : sessionIDFromConnection) {
                return UUID.fromString(session.asText());
            }
            throw new ServerException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JsonProcessingException e) {
            log.error("{}{}", errorMessage, e.getMessage());
            throw new ServerException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("Conexão WebSocket encerrada");
        log.info("ID da sessão: {}", event.getSessionId());
        // TODO
    }
}