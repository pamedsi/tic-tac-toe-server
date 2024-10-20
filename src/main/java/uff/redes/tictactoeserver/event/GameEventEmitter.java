package uff.redes.tictactoeserver.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.GameEvent;

@Service
public class GameEventEmitter {
    private final SimpMessagingTemplate messagingTemplate;

    public GameEventEmitter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void emmit(GameEvent event) {
        messagingTemplate.convertAndSend("/topic/game", event);
    }
}
