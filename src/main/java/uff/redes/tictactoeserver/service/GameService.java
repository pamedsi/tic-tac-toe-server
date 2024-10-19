package uff.redes.tictactoeserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uff.redes.tictactoeserver.domain.Cell;
import uff.redes.tictactoeserver.domain.GameStatus;
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

    public GameService(SessionService sessionService){
        this.initGrid();
        this.gameStatus = GameStatus.WAITING_START;
        this.sessionService = sessionService;
    }

    public void start() {
        if (!sessionService.bothPlayersConnected()) {
            throw new ServerException("Os dois jogadores ainda não estão conectados!", HttpStatus.CONFLICT);
        }
        gameStatus = GameStatus.STARTED;
        turn = Player.X;
    }

    public void printGrid() {
        System.out.println(grid.get(0));
        System.out.println(grid.get(1));
        System.out.println(grid.get(2));
    }

    private void initGrid() {
        List<List<Cell>> grid = new ArrayList<>();
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        grid.add(List.of(new Cell[]{Cell.EMPTY, Cell.EMPTY, Cell.EMPTY}));
        this.grid = grid;
    }
}
