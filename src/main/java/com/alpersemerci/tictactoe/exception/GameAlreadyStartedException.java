package com.alpersemerci.tictactoe.exception;

/**
 * GameAlreadyStartedException indicates a game setup action which can not be performed after game started.
 * For example you can not change board size during game play on classical tic tac toe game.
 * Actually it is a good idea that, the ability of changing board size during game play.
 * It will bring a more challenging game.
 *
 * @author Alper Semerci
 */
public class GameAlreadyStartedException extends Exception {

    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
