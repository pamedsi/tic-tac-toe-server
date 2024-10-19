package uff.redes.tictactoeserver.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record Session(
        UUID id,
        Player player,
        LocalDateTime startedAt
) {}
