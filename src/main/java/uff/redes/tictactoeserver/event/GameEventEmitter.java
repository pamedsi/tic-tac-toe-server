package uff.redes.tictactoeserver.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.dto.GameEventDTO;

import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

@Service
public class GameEventEmitter {
    private final SimpMessagingTemplate messagingTemplate;

    public GameEventEmitter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void emmit(GameEventDTO event, int delay) {
        CompletableFuture.runAsync(() -> {
            try {
                sleep(delay);
                messagingTemplate.convertAndSend("/topic/game", event);
            }
            catch (InterruptedException exception) {
                System.out.println("Interrupted while waiting for event" + exception.getMessage());
            }
        });
    }
}
