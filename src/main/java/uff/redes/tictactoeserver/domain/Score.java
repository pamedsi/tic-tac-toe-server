package uff.redes.tictactoeserver.domain;

public class Score {
    private int xScore;
    private int oScore;
    private int draws;

    public Score() {
        this.xScore = 0;
        this.oScore = 0;
        this.draws = 0;
    }

    public int getXScore() {
        return xScore;
    }

    public void addXScore() {
        xScore++;
    }

    public int getOScore() {
        return oScore;
    }

    public void addOScore() {
        oScore++;
    }

    public int getDraws() {
        return draws;
    }

    public void addDraw() {
        draws++;
    }
}
