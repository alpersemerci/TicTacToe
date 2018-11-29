package com.alpersemerci.tictactoe.service.heuristics;

import com.alpersemerci.tictactoe.exception.NoPossibleMovesLeftException;
import com.alpersemerci.tictactoe.model.Cell;
import com.alpersemerci.tictactoe.model.Game;
import com.alpersemerci.tictactoe.service.game.BoardService;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.IntStream;

/**
 * StayCloseToCenterHeuristicStrategy is an implementation of HeuristicStrategy.
 * Basically selects a cell from center of board or nearest free cell from center of board.
 */
public class StayCloseToCenterHeuristicStrategy implements HeuristicStrategy {

    private BoardService boardService;

    public StayCloseToCenterHeuristicStrategy(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Returns a nearest cell from center of board.
     * Throws NoPossibleMovesLeftException in there is no available cell left on board.
     * Uses Manhattan Distance Algorithm.
     *
     * @param game
     * @return
     * @throws NoPossibleMovesLeftException
     */
    @Override
    public Cell getMove(Game game) throws NoPossibleMovesLeftException {
        Integer boardSize = game.getBoard().getSize();

        //Create a PriorityQueue with Manhattan Distance Algorithm.
        Queue<Cell> bestPossibleMoves = new PriorityQueue<>((c1, c2) -> {
            Integer center = boardSize / 2;
            Integer c1Distance = Math.abs(c1.getRow() - center) + Math.abs(c1.getColumn() - center);
            Integer c2Distance = Math.abs(c2.getRow() - center) + Math.abs(c2.getColumn() - center);
            return c1Distance - c2Distance;
        });

        //Create queue
        IntStream.range(0, boardSize).forEach(i ->
            IntStream.range(0, boardSize).forEach(j -> {
                Cell cell = new Cell(i, j);
                if (boardService.isCellAvailableForPlay(game.getBoard(), cell)) {
                    bestPossibleMoves.add(cell);
                }
            })
        );

        //Than pick a watermelon...
        if (bestPossibleMoves.isEmpty()) {
            throw new NoPossibleMovesLeftException("Stay Close To Center Heuristic Strategy can not find possible move!");
        }

        return bestPossibleMoves.poll();
    }

}
