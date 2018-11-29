package com.alpersemerci.tictactoe.model;

import lombok.Data;

/**
 * Represents board state during game play.
 *
 * @author Alper Semerci
 */
@Data
public class Board {

    private Player[][] cells;

    private Integer size;

    public Board(Integer size) {
        this.size = size;
        this.cells = new Player[size][size];
    }
}
