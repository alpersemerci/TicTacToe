package com.alpersemerci.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Cell basically a tuple represents row & column.
 *
 * @author Alper Semerci
 */
@Data
@AllArgsConstructor
public class Cell {

    private Integer row;

    private Integer column;
}
