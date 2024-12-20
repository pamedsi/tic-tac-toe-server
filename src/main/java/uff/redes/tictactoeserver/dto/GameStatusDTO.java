package uff.redes.tictactoeserver.dto;

import uff.redes.tictactoeserver.domain.Cell;
import uff.redes.tictactoeserver.domain.GameStatus;

import java.util.List;

public record GameStatusDTO(
        GameStatus status,
        ScoreDTO score,
        List<List<Cell>> grid
) {}
