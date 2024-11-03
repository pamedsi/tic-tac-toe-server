package uff.redes.tictactoeserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import uff.redes.tictactoeserver.domain.Cell;
import uff.redes.tictactoeserver.event.GameEvent;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameEventDTO(
        GameEvent type,
        Optional<Cell> winner,
        Optional<UserDTO> user,
        Optional<MoveEvent> move
) {
    public GameEventDTO (GameEvent type) {
        this(type, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public GameEventDTO(GameEvent gameEvent, UserDTO userDTO, MoveEvent moveEvent) {
        this(gameEvent, Optional.empty(), Optional.of(userDTO), Optional.of(moveEvent));
    }

    public GameEventDTO(GameEvent gameEvent, Cell cell) {
        this(gameEvent, Optional.of(cell), Optional.empty(), Optional.empty());
    }
}
