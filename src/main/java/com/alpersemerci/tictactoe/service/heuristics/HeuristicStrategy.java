package com.alpersemerci.tictactoe.service.heuristics;

import com.alpersemerci.tictactoe.exception.NoPossibleMovesLeftException;
import com.alpersemerci.tictactoe.model.Cell;
import com.alpersemerci.tictactoe.model.Game;

/**
 * Defines Interface for heuristic strategies.
 */
public interface HeuristicStrategy {

    Cell getMove(Game game) throws NoPossibleMovesLeftException;

}
