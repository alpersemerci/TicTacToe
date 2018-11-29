package com.alpersemerci.tictactoe.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class GameTest {

    private Game game;

    @Before
    public void setUp() {
        game = new Game(10);
        game.setPlayerList(Arrays.asList(new Player(PlayerType.HUMAN, 'X'), new Player(PlayerType.HUMAN, 'O')));
        Board board = new Board(10);
        game.setBoard(board);
    }

    @Test
    public void getPlayerList() {
        Assert.assertTrue("Player list shouldn't be empty.", !game.getPlayerList().isEmpty());
    }

    @Test
    public void getBoard() {
        Assert.assertTrue("Board shouldn't be empty.", game.getBoard() != null);
    }

    @Test
    public void getCurrentTurn() {
    }

    @Test
    public void setPlayerList() {
    }

    @Test
    public void setBoard() {
    }

    @Test
    public void setCurrentTurn() {
    }

}