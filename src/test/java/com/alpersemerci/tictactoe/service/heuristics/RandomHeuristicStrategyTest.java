package com.alpersemerci.tictactoe.service.heuristics;

import com.alpersemerci.tictactoe.exception.InvalidMoveException;
import com.alpersemerci.tictactoe.exception.NoPlayerInGameException;
import com.alpersemerci.tictactoe.exception.NoPossibleMovesLeftException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class RandomHeuristicStrategyTest extends AbstractHeuristicStrategyTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        strategy = new RandomHeuristicStrategy(boardService);
    }

    @Test
    public void test_getMove_on_empty_board() throws NoPossibleMovesLeftException, InvalidMoveException, NoPlayerInGameException {
        for (int i = 0; i < Math.pow(game.getBoard().getSize(), 2); i++) {
            gameService.play(game, strategy.getMove(game));
        }

        log.info("test_getMove_on_empty_board \n " + boardService.printBoard(game.getBoard()));
    }

    @Test(expected = NoPossibleMovesLeftException.class)
    public void test_getMove_on_full_board() throws NoPossibleMovesLeftException, InvalidMoveException, NoPlayerInGameException {
        for (int i = 0; i < Math.pow(game.getBoard().getSize(), 2); i++) {
            gameService.play(game, strategy.getMove(game));
        }

        log.info("test_getMove_on_empty_board \n " + boardService.printBoard(game.getBoard()));

        gameService.play(game, strategy.getMove(game));
    }
}