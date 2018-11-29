package com.alpersemerci.tictactoe.service.game;

import com.alpersemerci.tictactoe.exception.GameAlreadyStartedException;
import com.alpersemerci.tictactoe.exception.InvalidBoardSizeException;
import com.alpersemerci.tictactoe.exception.InvalidMoveException;
import com.alpersemerci.tictactoe.exception.NoPlayerInGameException;
import com.alpersemerci.tictactoe.model.*;
import com.alpersemerci.tictactoe.service.game.BoardService;
import com.alpersemerci.tictactoe.service.game.GameService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class GameServiceTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private GameService gameService = new GameService(boardService);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test_create_game_with_valid_board_size() {
        IntStream.range(3, 10).forEach(i -> {
            try {
                gameService.createGame(i);
            } catch (InvalidBoardSizeException e) {
                Assert.assertTrue("Create game shouldn't be throw exception in legal board size range" , e != null);
            }
        });
    }

    @Test(expected = InvalidBoardSizeException.class)
    public void test_create_game_with_invalid_lower_board_size() throws InvalidBoardSizeException {
        gameService.createGame(2);
    }

    @Test(expected = InvalidBoardSizeException.class)
    public void test_create_game_with_invalid_higher_board_size() throws InvalidBoardSizeException {
        gameService.createGame(11);
    }

    @Test
    public void test_add_player_to_new_game() throws GameAlreadyStartedException {
        Game game = new Game(3);
        gameService.addPlayerToGame(game, new Player(PlayerType.AI, 'C'));
    }

    @Test(expected = GameAlreadyStartedException.class)
    public void test_add_player_to_existing_game() throws GameAlreadyStartedException, InvalidMoveException, NoPlayerInGameException {
        when(boardService.isCellAvailableForPlay(any(Board.class), any(Cell.class))).thenReturn(Boolean.TRUE);

        Game game = new Game(3);
        gameService.addPlayerToGame(game, new Player(PlayerType.AI, 'C'));
        gameService.addPlayerToGame(game, new Player(PlayerType.AI, 'O'));
        gameService.play(game, new Cell(0,0));
        //No humans allowed in this game
        gameService.addPlayerToGame(game, new Player(PlayerType.HUMAN, 'H'));

    }

    @Test
    public void getCurrentPlayer() throws GameAlreadyStartedException, NoPlayerInGameException, InvalidMoveException {

        when(boardService.isCellAvailableForPlay(any(Board.class), any(Cell.class))).thenReturn(Boolean.TRUE);

        Player player1 = new Player(PlayerType.HUMAN, '1');
        Player player2 = new Player(PlayerType.HUMAN, '2');
        Player player3 = new Player(PlayerType.AI, '3');

        Game game = new Game(3);
        gameService.addPlayerToGame(game, player1);
        gameService.addPlayerToGame(game, player2);
        gameService.addPlayerToGame(game, player3);

        Assert.assertTrue("Unexpected current player", gameService.getCurrentPlayer(game).equals(player1));
        gameService.play(game, new Cell(0, 0));
        Assert.assertTrue("Unexpected current player", gameService.getCurrentPlayer(game).equals(player2));
        gameService.play(game, new Cell(0, 1));
        Assert.assertTrue("Unexpected current player", gameService.getCurrentPlayer(game).equals(player3));
        gameService.play(game, new Cell(0, 2));
        Assert.assertTrue("Unexpected current player", gameService.getCurrentPlayer(game).equals(player1));
    }

    @Test
    public void test_play() throws GameAlreadyStartedException, InvalidMoveException, NoPlayerInGameException {

        when(boardService.isCellAvailableForPlay(any(Board.class), any(Cell.class))).thenReturn(Boolean.TRUE);

        Player player1 = new Player(PlayerType.HUMAN, '1');
        Player player2 = new Player(PlayerType.HUMAN, '2');

        Game game = new Game(3);
        gameService.addPlayerToGame(game, player1);
        gameService.addPlayerToGame(game, player2);

        gameService.play(game, new Cell(0, 0));
        gameService.play(game, new Cell(0, 1));

        Assert.assertTrue("Unexpected game turn value.", game.getCurrentTurn() == 2);

    }

    @Test
    public void test_isWinnerMove_horizontal() throws GameAlreadyStartedException, InvalidBoardSizeException {
        Player player = new Player(PlayerType.HUMAN, '1');
        Game game = gameService.createGame(10);
        gameService.addPlayerToGame(game, player);

        IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
            try {
                game.getBoard().getCells()[5][i] = player;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
            Assert.assertTrue("It should be winning move.", gameService.isWinnerMove(game, new Cell(5, i), player));
        });

        log.info(new BoardService().printBoard(game.getBoard()));
    }

    @Test
    public void test_isWinnerMove_vertical()  {
        IntStream.range(3, 11).forEach(boardSize -> {
            try {

                Player player = new Player(PlayerType.HUMAN, 'X');
                Game game = gameService.createGame(boardSize);
                gameService.addPlayerToGame(game, player);

                IntStream.range(0, boardSize).forEach(targetColumn -> {

                    log.info("Testing for board size = {}", boardSize);

                    IntStream.range(0, boardSize).forEach(i -> {
                        try {
                            game.getBoard().getCells()[i][targetColumn] = player;
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });

                    IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
                        Assert.assertTrue("It should be winning move. Board size = " + boardSize + " Column = " + targetColumn,
                                gameService.isWinnerMove(game, new Cell(i, targetColumn), player));
                    });

                    log.info(new BoardService().printBoard(game.getBoard()));
                    gameService.clearBoard(game);
                });

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @Test
    public void test_isWinnerMove_diagonal()  {
        IntStream.range(3, 11).forEach(boardSize -> {
            try {

                Player player = new Player(PlayerType.HUMAN, 'X');
                Game game = gameService.createGame(boardSize);
                gameService.addPlayerToGame(game, player);

                IntStream.range(0, boardSize).forEach(targetRow -> {

                    log.info("Testing for board size = {}", boardSize);

                    IntStream.range(0, boardSize).forEach(i -> {
                        try {
                            game.getBoard().getCells()[targetRow][i] = player;
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });

                    IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
                        Assert.assertTrue("It should be winning move. Board size = " + boardSize + " Row = " + targetRow,
                                gameService.isWinnerMove(game, new Cell(targetRow, i), player));
                    });

                    log.info(new BoardService().printBoard(game.getBoard()));
                    gameService.clearBoard(game);
                });

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @Test
    public void test_isWinnerMove_reverse_diagonal() throws GameAlreadyStartedException, InvalidBoardSizeException {
        Player player = new Player(PlayerType.HUMAN, 'X');
        Game game = gameService.createGame(10);
        gameService.addPlayerToGame(game, player);

        IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
            try {
                game.getBoard().getCells()[i][10 - i - 1] = player;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        log.info(new BoardService().printBoard(game.getBoard()));


        IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
            Assert.assertTrue("It should be winning move.", gameService.isWinnerMove(game, new Cell(i, i), player));
        });

        log.info(new BoardService().printBoard(game.getBoard()));
    }

    @Test
    public void test_isGameOver_on_empty_board() throws InvalidBoardSizeException, GameAlreadyStartedException {
        when(boardService.getAvailableCells(any(Board.class))).thenReturn(Arrays.asList(new Cell(0,0)));

        Player player = new Player(PlayerType.HUMAN, 'X');
        Game game = gameService.createGame(10);
        gameService.addPlayerToGame(game, player);

        Assert.assertTrue("Game shouldn't be over", !gameService.isGameOver(game));
    }

    @Test
    public void test_isGameOver_on_empty_full_board_with_winner() throws InvalidBoardSizeException, GameAlreadyStartedException {
        Player player = new Player(PlayerType.HUMAN, 'X');
        Game game = gameService.createGame(10);
        gameService.addPlayerToGame(game, player);

        IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
            try {
                game.getBoard().getCells()[i][10 - i - 1] = player;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        Assert.assertTrue("Game should be over", gameService.isGameOver(game));
    }

    @Test
    public void test_isGameOver_on_empty_full_board_without_winner() throws InvalidBoardSizeException, GameAlreadyStartedException {
        Player player = new Player(PlayerType.HUMAN, 'X');
        Game game = gameService.createGame(10);
        gameService.addPlayerToGame(game, player);

        IntStream.range(0, game.getBoard().getSize()).forEach(i -> {
            try {
                Player newPlayer = new Player(PlayerType.HUMAN, 'O');
                game.getBoard().getCells()[i][10 - i - 1] = newPlayer;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        Assert.assertTrue("Game should be over", gameService.isGameOver(game));
    }

}