package com.alpersemerci.tictactoe.exception;

/**
 * NoPossibleMovesLeftException indicates current player is without any move alternatives.
 * If player is human there is no available cell on board. Game should be terminated more early.
 * If player is AI and there are available cells on board, it means that heuristic strategy is broken.
 */
public class NoPossibleMovesLeftException extends Exception {

    public NoPossibleMovesLeftException(String message) {
        super(message);
    }
}
