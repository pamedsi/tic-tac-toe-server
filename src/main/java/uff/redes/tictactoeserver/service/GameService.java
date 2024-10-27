package uff.redes.tictactoeserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.Cell;
import uff.redes.tictactoeserver.domain.GameEvent;
import uff.redes.tictactoeserver.domain.GameStatus;
import uff.redes.tictactoeserver.dto.*;
import uff.redes.tictactoeserver.event.GameEventEmitter;
import uff.redes.tictactoeserver.exception.ServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameEventEmitter gameEventEmitter;
    private GameStatus gameStatus;
    private List<List<Cell>> grid;
    private int moves;

    public GameService(GameEventEmitter gameEventEmitter){
        this.initGrid();
        this.gameStatus = GameStatus.WAITING_FIRST_PLAYER;
        this.moves = 0;
        this.gameEventEmitter = gameEventEmitter;
    }

    public void start() {
        if (gameStatus == GameStatus.X_TURN || gameStatus == GameStatus.O_TURN) {
            throw new ServerException("Game already started!", HttpStatus.CONFLICT);
        }
        gameStatus = GameStatus.X_TURN;
        printGrid();
        gameEventEmitter.emmit(new GameEventDTO(GameEvent.MATCH_STARTED));
    }

    public void printGrid() {
        System.out.println(grid.get(0));
        System.out.println(grid.get(1));
        System.out.println(grid.get(2));
    }

    public void move(MoveRequest moveRequest, Player player) {
        validateMove(moveRequest);
        grid.get(moveRequest.row()).set(moveRequest.column(), Cell.valueOf(player.toString()));
        printGrid();
        if (gameStatus == GameStatus.X_TURN) gameStatus = GameStatus.O_TURN;
        else gameStatus = GameStatus.X_TURN;
        this.moves++;
        GameEventDTO gameEventDTO = new GameEventDTO (
                GameEvent.MOVE,
                Optional.of(new UserDTO(false, player)),
                Optional.of(moveRequest)
        );
        gameEventEmitter.emmit(gameEventDTO);
    }

    private void initGrid() {
        List<List<Cell>> grid = new ArrayList<>();
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        this.grid = grid;
    }

    private void validateMove(MoveRequest moveRequest) {
        if (grid.get(moveRequest.row()).get(moveRequest.column()) != Cell.EMPTY) {
            throw new ServerException("You can't play on that position. It's already occupied!", HttpStatus.CONFLICT);
        }
    }

    public GameStatusDTO getStatus() {
        return new GameStatusDTO(gameStatus);
    }

    public void setStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
