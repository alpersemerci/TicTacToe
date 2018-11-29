package com.alpersemerci.tictactoe.exception;

/**
 * InvalidBoardSizeException indicates a board creation attempt with unsupported board size parameter.
 */
public class InvalidBoardSizeException extends Exception {

    public InvalidBoardSizeException(String message) {
        super(message);
    }
}
