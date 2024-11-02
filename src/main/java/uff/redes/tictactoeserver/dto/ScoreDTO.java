package uff.redes.tictactoeserver.dto;

import uff.redes.tictactoeserver.domain.Score;

public record ScoreDTO (
        int x,
        int o,
        int draws
) {
    public ScoreDTO(Score score) {
        this(score.getXScore(), score.getOScore(), score.getDraws());
    }
}
