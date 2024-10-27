package uff.redes.tictactoeserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import uff.redes.tictactoeserver.domain.GameEvent;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameEventDTO(
        GameEvent type,
        Optional<UserDTO> player,
        Optional<MoveRequest> move
) {
    public GameEventDTO (GameEvent type) {
        this(type, Optional.empty(), Optional.empty());
    }
}
