package com.alpersemerci.tictactoe.service.heuristics;

import com.alpersemerci.tictactoe.exception.*;
import com.alpersemerci.tictactoe.model.Cell;
import com.alpersemerci.tictactoe.model.Game;
import com.alpersemerci.tictactoe.model.Player;
import com.alpersemerci.tictactoe.model.PlayerType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class AlphaBetaPruningHeuristicStrategyTest extends AbstractHeuristicStrategyTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        strategy = new AlphaBetaPruningHeuristicStrategy(boardService, gameService);
    }

    @Test
    public void test_getMove() throws InvalidBoardSizeException, GameAlreadyStartedException, InvalidMoveException, NoPlayerInGameException, NoPossibleMovesLeftException {
        Game game = gameService.createGame(3);
        Player player1 = new Player(PlayerType.HUMAN, 'H');
        Player player2 = new Player(PlayerType.AI, 'R');
        gameService.addPlayerToGame(game, player1);
        gameService.addPlayerToGame(game, player2);

        gameService.play(game, new Cell(0,0));
        gameService.play(game, new Cell(2,2));
        gameService.play(game, new Cell(1,0));

        log.info(boardService.printBoard(game.getBoard()));

        Cell cell = strategy.getMove(game);
        log.info(cell.toString());
        gameService.play(game, cell);

        log.info(boardService.printBoard(game.getBoard()));





    }
}