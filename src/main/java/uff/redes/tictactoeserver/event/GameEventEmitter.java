package uff.redes.tictactoeserver.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.dto.GameEventDTO;

@Service
public class GameEventEmitter {
    private final SimpMessagingTemplate messagingTemplate;

    public GameEventEmitter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void emmit(GameEventDTO event) {
            messagingTemplate.convertAndSend("/topic/game", event);
        }

}
