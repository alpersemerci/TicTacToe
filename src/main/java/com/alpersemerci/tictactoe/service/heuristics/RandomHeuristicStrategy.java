package com.alpersemerci.tictactoe.service.heuristics;

import com.alpersemerci.tictactoe.exception.NoPossibleMovesLeftException;
import com.alpersemerci.tictactoe.model.Cell;
import com.alpersemerci.tictactoe.model.Game;
import com.alpersemerci.tictactoe.service.game.BoardService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * RandomHeuristicStrategy is an implementation of HeuristicStrategy.
 * Basically selects a random cell from empty cell.
 */
public class RandomHeuristicStrategy implements HeuristicStrategy {

    private BoardService boardService;

    public RandomHeuristicStrategy(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Returns a random empty cell from available alternatives.
     * Throws NoPossibleMovesLeftException in there is no available cell left on board.
     *
     * @param game
     * @return
     * @throws NoPossibleMovesLeftException
     */
    @Override
    public Cell getMove(Game game) throws NoPossibleMovesLeftException {
        Integer boardSize = game.getBoard().getSize();
        List<Cell> emptyCells = new ArrayList<>();

        //First collect all empty cells
        IntStream.range(0, boardSize).forEach(i ->
            IntStream.range(0, boardSize).forEach(j -> {
                Cell cell = new Cell(i, j);
                if (boardService.isCellAvailableForPlay(game.getBoard(), cell)) {
                    emptyCells.add(cell);
                }
            })
        );

        //Than pick a watermelon...
        if (!emptyCells.isEmpty()) {
            return emptyCells.get(new Random().nextInt(emptyCells.size()));
        }

        throw new NoPossibleMovesLeftException("Random Heuristic Strategy can not find possible move!");
    }
}
