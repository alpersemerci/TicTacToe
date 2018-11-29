package com.alpersemerci.tictactoe.service.heuristics;

import com.alpersemerci.tictactoe.exception.*;
import com.alpersemerci.tictactoe.model.Cell;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

@Slf4j
public class StayCloseToCenterHeuristicStrategyTest extends AbstractHeuristicStrategyTest {

    public void setUp() throws Exception {
        super.setUp();
        strategy = new StayCloseToCenterHeuristicStrategy(boardService);
    }

    @Test
    public void test_getMove_on_empty_board() throws NoPossibleMovesLeftException, InvalidMoveException, NoPlayerInGameException {

        Integer currentDistance = Integer.MIN_VALUE;
        Integer center = game.getBoard().getSize() / 2;

        for (int i = 0; i < Math.pow(game.getBoard().getSize(), 2); i ++) {
            Cell cell = strategy.getMove(game);
            Integer distance = Math.abs(cell.getRow() - center) + Math.abs(cell.getColumn() - center);
            Assert.assertTrue("Suggested move distance must be greater than or equals to previous one", currentDistance <= distance);
            gameService.play(game, cell);
            currentDistance = distance;

        }

        log.info("test_getMove_on_empty_board \n " + boardService.printBoard(game.getBoard()));
    }

    @Test
    public void test_getMove_on_occupied_board() throws NoPossibleMovesLeftException, InvalidMoveException, NoPlayerInGameException {
        Integer currentDistance = 0;
        Integer center = game.getBoard().getSize() / 2;
        gameService.play(game, new Cell(center, center));
        Cell cell = strategy.getMove(game);
        Integer distance = Math.abs(cell.getRow() - center) + Math.abs(cell.getColumn() - center);
        Assert.assertTrue("Suggested move distance must be greater than or equals to previous one", currentDistance <= distance);

        log.info("test_getMove_on_occupied_board \n " + boardService.printBoard(game.getBoard()));
    }

    @Test(expected =  NoPossibleMovesLeftException.class)
    public void test_getMove_on_full_board() throws NoPossibleMovesLeftException, InvalidMoveException, NoPlayerInGameException {

        IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
            IntStream.range(0, game.getBoard().getSize()).forEach(j -> {
                try {
                    gameService.play(game, new Cell(i, j));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        });

        log.info("test_getMove_on_occupied_board \n " + boardService.printBoard(game.getBoard()));

        strategy.getMove(game);
    }
}