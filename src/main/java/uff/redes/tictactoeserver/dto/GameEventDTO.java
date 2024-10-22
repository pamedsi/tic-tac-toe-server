package uff.redes.tictactoeserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import uff.redes.tictactoeserver.domain.GameEvent;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameEventDTO(
        GameEvent type,
        Optional<PlayerDTO> player,
        Optional<MoveRequest> move
) {}
