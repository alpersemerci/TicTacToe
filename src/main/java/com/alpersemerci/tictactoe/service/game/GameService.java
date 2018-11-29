package com.alpersemerci.tictactoe.service.game;

import com.alpersemerci.tictactoe.exception.GameAlreadyStartedException;
import com.alpersemerci.tictactoe.exception.InvalidBoardSizeException;
import com.alpersemerci.tictactoe.exception.InvalidMoveException;
import com.alpersemerci.tictactoe.exception.NoPlayerInGameException;
import com.alpersemerci.tictactoe.model.Cell;
import com.alpersemerci.tictactoe.model.Game;
import com.alpersemerci.tictactoe.model.Player;

import java.util.List;

/**
 * Game Service provides service operations for game.
 *
 * @author Alper Semerci
 */
public class GameService {

    private BoardService boardService;

    public GameService(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Creates a new game for given board size. Throws InvalidBoardSizeException
     * if board size is not valid.
     *
     * @param boardSize
     * @return game
     */
    public Game createGame(Integer boardSize) throws InvalidBoardSizeException {

        if (boardSize < 3 || boardSize > 10) {
            throw new InvalidBoardSizeException("Board size must be between 3 and 10");
        }

        return new Game(boardSize);
    }

    /**
     * Adds player to current game. Throws GameAlreadyStartedException if game has
     * already started.
     *
     * @param player
     */
    public void addPlayerToGame(Game game, Player player) throws GameAlreadyStartedException {
        if (game.getCurrentTurn() > 0) {
            throw new GameAlreadyStartedException("Adding new player during game isn't a wise move!");
        }
        game.getPlayerList().add(player);
    }

    /**
     * Returns if given cell available for game board or not.
     *
     * @param game
     * @param cell
     * @return availability result
     */
    public Boolean isCellAvailableForPlay(Game game, Cell cell) {
        return boardService.isCellAvailableForPlay(game.getBoard(), cell);
    }

    /**
     * Returns current player for game.
     *
     * @param game
     * @return current player
     */
    public Player getCurrentPlayer(Game game) throws NoPlayerInGameException {
        Integer currentTurn = game.getCurrentTurn();
        List<Player> playerList = game.getPlayerList();

        if (playerList.isEmpty()) {
            throw new NoPlayerInGameException("There is no player in this game");
        }

        return playerList.get(currentTurn % playerList.size());
    }

    /**
     * Marks given cell on given board for given player if cell is available for play.
     *
     * @param game
     * @param cell
     * @return
     * @throws InvalidMoveException
     * @throws NoPlayerInGameException
     */
    public Boolean play(Game game, Cell cell) throws InvalidMoveException, NoPlayerInGameException {

        if (!isCellAvailableForPlay(game, cell)) {
            throw new InvalidMoveException("You shall not pass!");
        }

        Player player = getCurrentPlayer(game);
        boardService.play(game.getBoard(), cell, player);
        game.setCurrentTurn(game.getCurrentTurn() + 1);

        return isWinnerMove(game, cell, player);
    }

    /**
     * @param game
     * @param cell
     * @param player
     * @return
     * @throws NoPlayerInGameException
     */
    public Boolean isWinnerMove(Game game, Cell cell, Player player) {
        Integer boardSize = game.getBoard().getSize();

        Boolean vertical = Boolean.TRUE;
        Boolean horizontal = Boolean.TRUE;
        Boolean diagonal = Boolean.TRUE;
        Boolean reverseDiagonal = Boolean.TRUE;

        for (int i = 0; i < boardSize; i++) {
            horizontal = horizontal && player.equals(game.getBoard().getCells()[cell.getRow()][i]);
            vertical = vertical && player.equals(game.getBoard().getCells()[i][cell.getColumn()]);
            diagonal = diagonal && cell.getColumn().equals(cell.getRow()) && player.equals(game.getBoard().getCells()[i][i]);
            reverseDiagonal = reverseDiagonal && cell.getColumn().equals(cell.getRow()) && player.equals(game.getBoard().getCells()[i][boardSize - i - 1]);
        }

        return vertical || horizontal || diagonal || reverseDiagonal;
    }

    /**
     * Returns game over state according to available cells and winner move
     *
     * @param game
     * @return game over state
     */
    public Boolean isGameOver(Game game) {
        if (boardService.getAvailableCells(game.getBoard()).isEmpty()) {
            return Boolean.TRUE;
        } else {
            for (int i = 0; i < game.getBoard().getSize(); i++) {
                for (int j = 0; j < game.getBoard().getSize(); j++) {
                    Player player = game.getBoard().getCells()[i][j];
                    if (player != null && isWinnerMove(game, new Cell(i, j), player)) {
                        return Boolean.TRUE;
                    }
                }
            }
        }

        return Boolean.FALSE;
    }

    /**
     * @param game
     */
    public void clearBoard(Game game) {
        Integer boardSize = game.getBoard().getSize();
        game.getBoard().setCells(new Player[boardSize][boardSize]);
    }
}
