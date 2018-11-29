package com.alpersemerci.tictactoe.exception;

/**
 * NoPlayerInGameException indicates a game tried to start without any player.
 */
public class NoPlayerInGameException extends Exception {

    public NoPlayerInGameException(String message) {
        super(message);
    }
}
