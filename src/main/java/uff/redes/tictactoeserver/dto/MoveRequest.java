package uff.redes.tictactoeserver.dto;

import java.util.UUID;

public record MoveRequest(
        UUID sessionID,
        int row,
        int column
) {}
