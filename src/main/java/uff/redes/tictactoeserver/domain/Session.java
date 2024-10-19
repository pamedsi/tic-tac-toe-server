package uff.redes.tictactoeserver.domain;

import uff.redes.tictactoeserver.dto.Player;

import java.util.UUID;

public record Session (
        UUID id,
        Player player
) {}