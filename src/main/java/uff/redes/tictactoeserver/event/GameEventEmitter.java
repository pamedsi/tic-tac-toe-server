package uff.redes.tictactoeserver.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.GameEvent;
import uff.redes.tictactoeserver.dto.GameEventDTO;

import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

@Service
public class GameEventEmitter {
    private final SimpMessagingTemplate messagingTemplate;

    public GameEventEmitter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void emmit(GameEventDTO event) {
        if (event.type() == GameEvent.FIRST_PLAYER_JOINED || event.type() == GameEvent.BOTH_PLAYERS_JOINED) {
            CompletableFuture.runAsync(() -> {
                try {
                    sleep(1000);
                    messagingTemplate.convertAndSend("/topic/game", event);
                }
                catch (InterruptedException exception) {
                    System.out.println("Interrupted while waiting for event" + exception.getMessage());
                }
            });
        }
        else {
            messagingTemplate.convertAndSend("/topic/game", event);
        }
    }

}
