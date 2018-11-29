package com.alpersemerci.tictactoe.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game state during game play. Holds game related data lik board, players and current turn.
 *
 * @author Alper Semerci
 */
@Data
public class Game {

    private List<Player> playerList;

    private Board board;

    private Integer currentTurn;

    public Game(Integer boardSize) {
        playerList = new ArrayList<>();
        board = new Board(boardSize);
        currentTurn = 0;
    }

}
