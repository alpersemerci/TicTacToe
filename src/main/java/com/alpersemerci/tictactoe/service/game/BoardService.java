package com.alpersemerci.tictactoe.service.game;

import com.alpersemerci.tictactoe.model.Board;
import com.alpersemerci.tictactoe.model.Cell;
import com.alpersemerci.tictactoe.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Board Service provides service operations for board.
 *
 * @author Alper Semerci
 */
public class BoardService {

    /**
     * Checks given cell is valid for given board.
     *
     * @param board
     * @param cell
     * @return Result of is valid cell or not
     */
    public Boolean isValidCellForBoard(Board board, Cell cell) {
        return (cell.getRow() > -1
                && cell.getRow() < board.getSize()
                && cell.getColumn() > -1
                && cell.getColumn() < board.getSize());
    }

    /**
     * Checks given cell is valid for given board and
     * cell is not occupied by another player.
     *
     * @param board
     * @param cell
     * @return Result of cell available for play.
     */
    public Boolean isCellAvailableForPlay(Board board, Cell cell) {

        if (isValidCellForBoard(board, cell)) {
            return board.getCells()[cell.getRow()][cell.getColumn()] == null;
        }
        return Boolean.FALSE;
    }

    /**
     * Marks given cell on given board for given player if cell is available for play.
     *
     * @param board
     * @param cell
     * @param player
     * @return Play result
     */
    public Boolean play(Board board, Cell cell, Player player) {
        if (isCellAvailableForPlay(board, cell)) {
            board.getCells()[cell.getRow()][cell.getColumn()] = player;
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Returns empty cells on board.
     *
     * @param board
     * @return List of cells
     */
    public List<Cell> getAvailableCells(Board board) {
        List<Cell> availableCells = new ArrayList<>();
        IntStream.range(0, board.getSize()).forEach(i ->
            IntStream.range(0, board.getSize()).forEach(j -> {
                Cell cell = new Cell(i, j);
                if (isCellAvailableForPlay(board, cell)) {
                    availableCells.add(cell);
                }
            })
        );
        return availableCells;
    }

    /**
     * Converts given board to string in console friendly mode.
     *
     * @param board
     * @return String representation of board.
     */
    public String printBoard(Board board) {
        Integer boardSize = board.getSize();
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("\n");
        builder.append("  ");
        IntStream.range(0, boardSize).forEach(i -> builder.append("  " + i + " "));
        builder.append("\n");
        builder.append("  ");
        IntStream.range(0, boardSize).forEach(i -> builder.append("===="));
        IntStream.range(0, boardSize).forEach(i -> {
            builder.append("\n").append(i + " ").append("|");
            IntStream.range(0, boardSize).forEach(j ->
                builder.append(" ")
                        .append(board.getCells()[i][j] == null ? " " : board.getCells()[i][j].getSymbol())
                        .append(" ")
                        .append("|")
            );

            builder.append("\n").append("  ");
            if (i < boardSize - 1) {
                IntStream.range(0, boardSize).forEach(j -> builder.append("----"));
            }
        });
        IntStream.range(0, boardSize).forEach(j -> builder.append("===="));
        builder.append("\n");
        return builder.toString();
    }
}
