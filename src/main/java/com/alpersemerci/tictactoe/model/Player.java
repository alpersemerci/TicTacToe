package com.alpersemerci.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Defines player details.
 *
 * @author Alper Semerci
 */
@Data
@AllArgsConstructor
public class Player {

    private PlayerType type;

    private Character symbol;

}
