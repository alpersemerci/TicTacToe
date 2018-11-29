package com.alpersemerci.tictactoe.exception;

/**
 * InvalidMoveException indicates an illegal move. Probably target cell is occupied with a player or target cell
 * is out of border. Simply it means you shall not pass.
 *
 * [Gandalf raises his sword and staff together into the air.]
 *
 * Gandalf: "You — shall not — pass!"
 *
 * [Gandalf drives his staff into the bridge, causing a bright flash of blue light to appear. Flaring its nostrils, the Balrog steps forward onto the bridge.]
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException(String message) {
        super(message);
    }
}
