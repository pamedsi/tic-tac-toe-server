package uff.redes.tictactoeserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.Cell;
import uff.redes.tictactoeserver.domain.GameEvent;
import uff.redes.tictactoeserver.domain.GameStatus;
import uff.redes.tictactoeserver.domain.Score;
import uff.redes.tictactoeserver.dto.*;
import uff.redes.tictactoeserver.event.GameEventEmitter;
import uff.redes.tictactoeserver.exception.ServerException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private final GameEventEmitter gameEventEmitter;
    private Score score;
    private GameStatus gameStatus;
    private List<List<Cell>> grid;
    private int moves;

    public GameService(GameEventEmitter gameEventEmitter){
        initGame();
        this.gameEventEmitter = gameEventEmitter;
    }

    public void start() {
        if (gameStatus == GameStatus.X_TURN || gameStatus == GameStatus.O_TURN) {
            throw new ServerException("Game already started!", HttpStatus.CONFLICT);
        }
        gameStatus = GameStatus.X_TURN;
        initGrid();
        moves = 0;
        gameEventEmitter.emmit(new GameEventDTO(GameEvent.MATCH_STARTED));
    }

    public void move(MoveRequest moveRequest, Player player) {
        validateMove(moveRequest, player);
        executeMove(moveRequest.row(), moveRequest.column(), Cell.valueOf(player.toString()));
        this.moves++;
        if (!matchIsOver()) {
            switchTurn();
            emmitMove(moveRequest, player);
        }
    }

    private void emmitMove(MoveRequest moveRequest, Player player) {
        gameEventEmitter.emmit(new GameEventDTO(
                GameEvent.MOVE,
                new UserDTO(false, player),
                new MoveEvent(player, moveRequest.row(), moveRequest.column())
        ));
    }

    private void switchTurn() {
        if (gameStatus == GameStatus.X_TURN) gameStatus = GameStatus.O_TURN;
        else gameStatus = GameStatus.X_TURN;
    }

    private void executeMove(int rowIndex, int columnIndex, Cell value) {
        grid.get(rowIndex).set(columnIndex, value);
    }

    private void initGrid() {
        List<List<Cell>> grid = new ArrayList<>();
        grid.add(new ArrayList<>(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY})));
        grid.add(new ArrayList<>(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY})));
        grid.add(new ArrayList<>(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY})));
        this.grid = grid;
    }

    private void validateMove(MoveRequest moveRequest, Player player) {
        if (gameStatus != GameStatus.X_TURN && gameStatus != GameStatus.O_TURN) {
            throw new ServerException("The game hasn't started!", HttpStatus.BAD_REQUEST);
        }
        if (    gameStatus == GameStatus.X_TURN && player.equals(Player.O) ||
                gameStatus == GameStatus.O_TURN && player.equals(Player.X)
        ) {
            throw new ServerException("It's not your turn!", HttpStatus.FORBIDDEN);
        }
        if (grid.get(moveRequest.row()).get(moveRequest.column()) != Cell.EMPTY) {
            throw new ServerException("You can't play on that position. It's already occupied!", HttpStatus.CONFLICT);
        }
    }

    public GameStatusDTO getStatus() {
        return new GameStatusDTO(gameStatus, new ScoreDTO(score), grid);
    }

    public void setStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void initGame() {
        initGrid();
        gameStatus = GameStatus.WAITING_FIRST_PLAYER;
        score = new Score();
        moves = 0;
    }

    private boolean thereIsAWinner() {
        return IsThereAWinnerHorizontally() || IsThereAWinnerVertically() || IsThereAWinnerDiagonally();
    }

    private boolean IsThereAWinnerHorizontally() {
        boolean hasAWinnerHorizontally = false;
        for (List<Cell> row : this.grid) {
            if (row.stream().distinct().count() == 1 && row.getFirst() != Cell.EMPTY) {
                hasAWinnerHorizontally = true;
                break;
            }
        }
        return hasAWinnerHorizontally;
    }

    private boolean IsThereAWinnerVertically() {
        for (int columIndex = 0; columIndex < 3; columIndex++) {
            List<Cell> column = List.of(
                    this.grid.get(0).get(columIndex),
                    this.grid.get(1).get(columIndex),
                    this.grid.get(2).get(columIndex)
            );
            if (column.stream().distinct().count() == 1 && column.getFirst() != Cell.EMPTY) return true;
        }
        return false;
    }

    private boolean IsThereAWinnerDiagonally() {
        Cell topLeftValue = this.grid.get(0).getFirst();
        boolean allEqualFromTopLeftToBottomRight = topLeftValue == this.grid.get(1).get(1) && topLeftValue == this.grid.get(2).get(2);
        if (allEqualFromTopLeftToBottomRight && topLeftValue != Cell.EMPTY) return true;

        Cell topRightValue = this.grid.get(0).get(2);
        boolean allEqualFromTopRightToBottomLeft = topRightValue == this.grid.get(1).get(1) && topRightValue == this.grid.get(2).getFirst();
        return allEqualFromTopRightToBottomLeft && topRightValue != Cell.EMPTY;
    }

    private boolean itsADrawGame() {
        return moves == 9 && (gameStatus == GameStatus.X_TURN || gameStatus == GameStatus.O_TURN);
    }

    private boolean matchIsOver() {
        if (moves < 5) return false;
        Cell winner = null;
        boolean hasAWinner = false;
        if (thereIsAWinner()) {
            winner = gameStatus == GameStatus.X_TURN ? Cell.X : Cell.O;
            hasAWinner = true;
        }
        else if (itsADrawGame()) {
            winner = Cell.EMPTY;
            hasAWinner = true;
        }
        if (hasAWinner) {
            score(winner);
            return true;
        }
        return false;
    }

    private void score(Cell winner) {
        switch (winner) {
            case EMPTY -> score.addDraw();
            case X -> score.addXScore();
            case O -> score.addOScore();
        }
        gameEventEmitter.emmit(new GameEventDTO(
                GameEvent.MATCH_ENDED,
                winner
        ));
        gameStatus = GameStatus.WAITING_START;
    }

}
