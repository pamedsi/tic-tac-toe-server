package uff.redes.tictactoeserver.dto;

import java.util.UUID;

public record Move (
        UUID sessionID,
        int row,
        int column
) {}
