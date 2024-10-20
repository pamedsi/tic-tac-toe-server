package uff.redes.tictactoeserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.Cell;
import uff.redes.tictactoeserver.domain.GameEvent;
import uff.redes.tictactoeserver.domain.GameStatus;
import uff.redes.tictactoeserver.dto.GameStatusDTO;
import uff.redes.tictactoeserver.dto.Move;
import uff.redes.tictactoeserver.dto.Player;
import uff.redes.tictactoeserver.event.GameEventEmitter;
import uff.redes.tictactoeserver.exception.ServerException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private final GameEventEmitter gameEventEmitter;
    private GameStatus gameStatus;
    private List<List<Cell>> grid;
    private Player turn;
    private int moves;

    public GameService(GameEventEmitter gameEventEmitter){
        this.initGrid();
        this.gameStatus = GameStatus.WAITING_START;
        this.moves = 0;
        this.gameEventEmitter = gameEventEmitter;
    }

    public void start() {
        if (gameStatus == GameStatus.STARTED) {
            throw new ServerException("Game already started!", HttpStatus.CONFLICT);
        }
        gameStatus = GameStatus.STARTED;
        turn = Player.X;
        printGrid();
        gameEventEmitter.emmit(GameEvent.MATCH_STARTED);
    }

    public void printGrid() {
        System.out.println(grid.get(0));
        System.out.println(grid.get(1));
        System.out.println(grid.get(2));
    }

    public void move(Move move, Player player) {
        validateMove(move);
        grid.get(move.row()).set(move.column(), Cell.valueOf(player.toString()));
        printGrid();
        if (turn == Player.X) turn = Player.O;
        else turn = Player.X;
        this.moves++;
        gameEventEmitter.emmit(GameEvent.MOVE);
    }

    private void initGrid() {
        List<List<Cell>> grid = new ArrayList<>();
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        this.grid = grid;
    }

    private void validateMove(Move move) {
        if (grid.get(move.row()).get(move.column()) != Cell.EMPTY) {
            throw new ServerException("Você não pode jogar nesta posição!", HttpStatus.CONFLICT);
        }
    }

    public GameStatusDTO getStatus() {
        return new GameStatusDTO(gameStatus);
    }
}
