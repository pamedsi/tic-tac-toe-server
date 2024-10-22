package uff.redes.tictactoeserver.dto;

public record MoveEvent (
        Player player,
        int row,
        int column
) {}
