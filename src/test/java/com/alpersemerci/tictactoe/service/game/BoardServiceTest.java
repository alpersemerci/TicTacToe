package com.alpersemerci.tictactoe.service.game;

import com.alpersemerci.tictactoe.model.Board;
import com.alpersemerci.tictactoe.model.Cell;
import com.alpersemerci.tictactoe.model.Player;
import com.alpersemerci.tictactoe.model.PlayerType;
import com.alpersemerci.tictactoe.service.game.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

@Slf4j
public class BoardServiceTest {

    private BoardService boardService;

    @Before
    public void setUp() throws Exception {
        boardService = new BoardService();
    }

    @Test
    public void test_valid_cells_for_board() {
        IntStream.range(3, 10).forEach(i -> {
            Board board = new Board(i);
            IntStream.range(0, i).forEach(j -> {
                        IntStream.range(0, i).forEach(k -> {
                            Assert.assertTrue("Cell should be valid for board.", boardService.isValidCellForBoard(board, new Cell(j, k)));
                        });
                    }
            );
        });
    }

    @Test
    public void test_invalid_cells_for_board() {
        IntStream.range(3, 10).forEach(i -> {
            Board board = new Board(i);
            Assert.assertTrue("Cell should not be valid for board.", !boardService.isValidCellForBoard(board, new Cell(0, i)));
            Assert.assertTrue("Cell should not be valid for board.", !boardService.isValidCellForBoard(board, new Cell(i, 0)));
        });
    }

    @Test
    public void test_is_cell_available_before_and_after_play() {
        IntStream.range(3, 10).forEach(boardSize -> {
            Board board = new Board(boardSize);
            IntStream.range(0, boardSize).forEach(i -> {
                IntStream.range(0, boardSize).forEach(j -> {
                    Cell cell = new Cell(i, j);
                    Assert.assertTrue("Cell should be available before play", boardService.isCellAvailableForPlay(board, cell));
                    boardService.play(board, cell, new Player(PlayerType.HUMAN, 'O'));
                    Assert.assertTrue("Cell shouldn't be available after play", !boardService.isCellAvailableForPlay(board, cell));
                });
            });

        });
    }

    @Test
    public void test_print_empty_board() {
        IntStream.range(3, 11).forEach(i -> {
            log.info("[BOARD_PRINT_TEST] [TESTING_FOR_SIZE = {}]", i);
            Board board = new Board(i);
            log.info(boardService.printBoard(board));
        });
    }

    @Test
    public void test_to_string_full_board() {
        IntStream.range(3, 11).forEach(k -> {

            Board board = new Board(k);

            IntStream.range(0, k).forEach(i -> {
                IntStream.range(0, k).forEach(j -> {
                    Cell cell = new Cell(i, j);
                    boardService.play(board, cell, new Player(PlayerType.HUMAN, 'O'));
                });
            });

            log.info("[BOARD_PRINT_TEST] [TESTING_FULL_BOARD_FOR_SIZE = {}]", k);
            log.info(boardService.printBoard(board));

        });
    }
}