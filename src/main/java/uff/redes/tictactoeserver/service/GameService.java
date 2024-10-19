package uff.redes.tictactoeserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.Cell;
import uff.redes.tictactoeserver.domain.GameStatus;
import uff.redes.tictactoeserver.dto.Move;
import uff.redes.tictactoeserver.dto.Player;
import uff.redes.tictactoeserver.exception.ServerException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private GameStatus gameStatus;
    private SessionService sessionService;
    private List<List<Cell>> grid;
    private Player turn;
    private int moves;


    public GameService(SessionService sessionService){
        this.initGrid();
        this.gameStatus = GameStatus.WAITING_START;
        this.sessionService = sessionService;
        this.moves = 0;
    }

    public void start() {
        if (gameStatus == GameStatus.STARTED) {
            throw new ServerException("Game already started!", HttpStatus.CONFLICT);
        }
        gameStatus = GameStatus.STARTED;
        turn = Player.X;
        printGrid();
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

}
